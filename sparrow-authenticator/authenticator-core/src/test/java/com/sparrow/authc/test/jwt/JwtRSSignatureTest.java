package com.sparrow.authc.test.jwt;

import com.alibaba.fastjson.JSON;
import com.sparrow.authenticator.DefaultLoginUser;
import com.sparrow.authenticator.config.AuthenticatorConfig;
import com.sparrow.authenticator.enums.AuthenticatorError;
import com.sparrow.authenticator.signature.jwt.JwtRSSignature;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.LoginUser;
import com.sparrow.protocol.constant.SparrowError;
import com.sparrow.protocol.enums.DeviceType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JwtRSSignatureTest {
    private static KeyPair keyPair;
    private static KeyPair otherKeyPair;

    @TempDir
    Path temporaryDirectory;

    @BeforeAll
    static void generateKeys() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        keyPair = generator.generateKeyPair();
        otherKeyPair = generator.generateKeyPair();
    }

    @Test
    void fileKeysKeepRs256AndJsonStringBody() throws Exception {
        JwtRSSignature signature = fileSignature();
        String token = signature.sign(loginUser(), null);

        Jws<Claims> jwt = Jwts.parser().verifyWith(keyPair.getPublic()).build().parseSignedClaims(token);
        assertEquals("RS256", jwt.getHeader().getAlgorithm());
        assertEquals("test-issuer", jwt.getPayload().getIssuer());
        assertEquals("42", jwt.getPayload().getSubject());
        String body = assertInstanceOf(String.class, jwt.getPayload().get("body"));
        assertEquals("alice", JSON.parseObject(body).getString("userName"));
        assertUser(signature.verify(token, null));
    }

    @Test
    void explicitKeysAcceptWhitespaceInsideBase64() throws Exception {
        JwtRSSignature signature = fileSignature();
        String token = signature.sign(loginUser(), wrapped(otherKeyPair.getPrivate().getEncoded()));

        Jwts.parser().verifyWith(otherKeyPair.getPublic()).build().parseSignedClaims(token);
        assertUser(signature.verify(token, wrapped(otherKeyPair.getPublic().getEncoded())));
    }

    @Test
    void blankOverrideKeysUseDefaultKeys() throws Exception {
        JwtRSSignature signature = fileSignature();
        String token = signature.sign(loginUser(), "\u001c \t\r\n");

        assertUser(signature.verify(token, "\u001c \t\r\n"));
    }

    @Test
    void incorrectPublicKeyKeepsTokenAbnormalError() throws Exception {
        JwtRSSignature signature = fileSignature();
        String token = signature.sign(loginUser(), null);

        BusinessException error = assertThrows(BusinessException.class,
                () -> signature.verify(token, Base64.getEncoder().encodeToString(otherKeyPair.getPublic().getEncoded())));
        assertEquals(AuthenticatorError.USER_TOKEN_ABNORMAL, error.getErrorSupport());
    }

    @Test
    void changedPayloadKeepsTokenAbnormalError() throws Exception {
        JwtRSSignature signature = fileSignature();
        String[] parts = signature.sign(loginUser(), null).split("\\.");
        String payload = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);
        parts[1] = Base64.getUrlEncoder().withoutPadding()
                .encodeToString(payload.replace("alice", "mallory").getBytes(StandardCharsets.UTF_8));
        String alteredToken = String.join(".", parts);

        BusinessException error = assertThrows(BusinessException.class, () -> signature.verify(alteredToken, null));
        assertEquals(AuthenticatorError.USER_TOKEN_ABNORMAL, error.getErrorSupport());
    }

    @Test
    void expiredTokenKeepsTokenAbnormalError() throws Exception {
        JwtRSSignature signature = fileSignature();
        DefaultLoginUser user = loginUser();
        user.setExpireAt(System.currentTimeMillis() - 60_000);
        String token = signature.sign(user, null);

        BusinessException error = assertThrows(BusinessException.class, () -> signature.verify(token, null));
        assertEquals(AuthenticatorError.USER_TOKEN_ABNORMAL, error.getErrorSupport());
    }

    @Test
    void malformedPrivateKeyKeepsServerError() throws Exception {
        JwtRSSignature signature = fileSignature();

        BusinessException error = assertThrows(BusinessException.class, () -> signature.sign(loginUser(), "invalid-key"));
        assertEquals(SparrowError.SYSTEM_SERVER_ERROR, error.getErrorSupport());
    }

    @Test
    void invalidPkcs8PrivateKeyKeepsServerError() throws Exception {
        JwtRSSignature signature = fileSignature();
        String invalidKey = Base64.getEncoder().encodeToString("not-an-rsa-key".getBytes(StandardCharsets.UTF_8));

        BusinessException error = assertThrows(BusinessException.class, () -> signature.sign(loginUser(), invalidKey));
        assertEquals(SparrowError.SYSTEM_SERVER_ERROR, error.getErrorSupport());
    }

    @Test
    void malformedPublicKeyKeepsServerError() throws Exception {
        JwtRSSignature signature = fileSignature();
        String token = signature.sign(loginUser(), null);

        BusinessException error = assertThrows(BusinessException.class, () -> signature.verify(token, "invalid-key"));
        assertEquals(SparrowError.SYSTEM_SERVER_ERROR, error.getErrorSupport());
    }

    @Test
    void originalAbsoluteClasspathPathsStillLoad() throws Exception {
        JwtRSSignature signature = new JwtRSSignature(config("/rsa/test", "/rsa/test.pub"));

        assertUser(signature.verify(signature.sign(loginUser(), null), null));
    }

    @Test
    void classpathPathsStillNormalizeBackslashes() throws Exception {
        JwtRSSignature signature = new JwtRSSignature(config("\\rsa\\test", "\\rsa\\test.pub"));

        assertUser(signature.verify(signature.sign(loginUser(), null), null));
    }

    @Test
    void relativeClasspathPathsUseOriginalSupportPackage() throws Exception {
        Path resources = temporaryDirectory.resolve("resources");
        Path privateKey = resources.resolve("com/sparrow/support/rsa/generated");
        Path publicKey = resources.resolve("com/sparrow/support/rsa/generated.pub");
        writeKeys(privateKey, publicKey);

        withResources(resources, () -> {
            JwtRSSignature signature = new JwtRSSignature(config("rsa/generated", "rsa/generated.pub"));
            String token = signature.sign(loginUser(), null);
            Jwts.parser().verifyWith(keyPair.getPublic()).build().parseSignedClaims(token);
            assertUser(signature.verify(token, null));
        });
    }

    @Test
    void actualFilesTakePriorityOverClasspathResources() throws Exception {
        Path privateKey = temporaryDirectory.resolve("private-key");
        Path publicKey = temporaryDirectory.resolve("public-key");
        writeKeys(privateKey, publicKey);
        Path resources = temporaryDirectory.resolve("resources");
        Path conflictingPrivateKey = resources.resolve(privateKey.toString().substring(1));
        Path conflictingPublicKey = resources.resolve(publicKey.toString().substring(1));
        Files.createDirectories(conflictingPrivateKey.getParent());
        Files.writeString(conflictingPrivateKey, "invalid-classpath-key");
        Files.writeString(conflictingPublicKey, "invalid-classpath-key");

        withResources(resources, () -> {
            JwtRSSignature signature = new JwtRSSignature(config(privateKey.toString(), publicKey.toString()));
            assertUser(signature.verify(signature.sign(loginUser(), null), null));
        });
    }

    @Test
    void absentDefaultKeyPathsDoNotFailConstruction() {
        JwtRSSignature signature = assertDoesNotThrow(() -> new JwtRSSignature(config(null, null)));

        BusinessException error = assertThrows(BusinessException.class, () -> signature.sign(loginUser(), null));
        assertEquals(SparrowError.SYSTEM_SERVER_ERROR, error.getErrorSupport());
    }

    @Test
    void missingDefaultKeyResourcesDoNotFailConstruction() {
        assertDoesNotThrow(() -> new JwtRSSignature(config("/rsa/does-not-exist", "/rsa/does-not-exist.pub")));
    }

    @Test
    void emptyPrivateKeyFileDoesNotFailConstruction() throws Exception {
        Path emptyKey = temporaryDirectory.resolve("empty-key");
        Files.writeString(emptyKey, " \t\r\n");

        assertDoesNotThrow(() -> new JwtRSSignature(config(emptyKey.toString(), "/rsa/test.pub")));
    }

    @Test
    void missingPublicKeyResourceDoesNotFailConstruction() {
        assertDoesNotThrow(() -> new JwtRSSignature(config("/rsa/test", "/rsa/does-not-exist.pub")));
    }

    @Test
    void emptyPublicKeyFileDoesNotFailConstruction() throws Exception {
        Path emptyKey = temporaryDirectory.resolve("empty-key");
        Files.writeString(emptyKey, " \t\r\n");

        assertDoesNotThrow(() -> new JwtRSSignature(config("/rsa/test", emptyKey.toString())));
    }

    private JwtRSSignature fileSignature() throws Exception {
        Path privateKey = temporaryDirectory.resolve("private-key");
        Path publicKey = temporaryDirectory.resolve("public-key");
        writeKeys(privateKey, publicKey);
        return new JwtRSSignature(config(privateKey.toString(), publicKey.toString()));
    }

    private static void writeKeys(Path privateKey, Path publicKey) throws Exception {
        Files.createDirectories(privateKey.getParent());
        Files.writeString(privateKey, wrapped(keyPair.getPrivate().getEncoded()));
        Files.writeString(publicKey, wrapped(keyPair.getPublic().getEncoded()));
    }

    private static String wrapped(byte[] key) {
        return " \t" + Base64.getMimeEncoder(64, new byte[]{'\n'}).encodeToString(key) + "\r\n ";
    }

    private static AuthenticatorConfig config(String privatePath, String publicPath) {
        AuthenticatorConfig config = new AuthenticatorConfig();
        config.setPrivateKeyPath(privatePath);
        config.setPublicKeyPath(publicPath);
        config.setJwtIssuer("test-issuer");
        return config;
    }

    private static DefaultLoginUser loginUser() {
        DefaultLoginUser user = new DefaultLoginUser();
        user.setUserId(42L);
        user.setUserName("alice");
        user.setNickName("测试用户");
        user.setTenantId("tenant-7");
        user.setDeviceType(DeviceType.PC);
        user.setExpireAt(System.currentTimeMillis() + 60_000);
        user.getExtensions().put("department", "engineering");
        return user;
    }

    private static void assertUser(LoginUser user) {
        assertEquals(42L, user.getUserId());
        assertEquals("alice", user.getUserName());
        assertEquals("测试用户", user.getNickName());
        assertEquals("tenant-7", user.getTenantId());
        assertEquals(DeviceType.PC, user.getDeviceType());
        assertEquals("engineering", user.getExtensions().get("department"));
    }

    private static void withResources(Path resources, org.junit.jupiter.api.function.Executable action) throws Exception {
        ClassLoader original = Thread.currentThread().getContextClassLoader();
        try (URLClassLoader loader = new URLClassLoader(new URL[]{resources.toUri().toURL()}, original)) {
            Thread.currentThread().setContextClassLoader(loader);
            try {
                assertDoesNotThrow(action);
            } finally {
                Thread.currentThread().setContextClassLoader(original);
            }
        }
    }
}
