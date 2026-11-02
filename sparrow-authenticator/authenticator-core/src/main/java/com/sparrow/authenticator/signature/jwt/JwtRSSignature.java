/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sparrow.authenticator.signature.jwt;

import com.alibaba.fastjson.JSON;
import com.sparrow.authenticator.AuthenticatorConfigReader;
import com.sparrow.authenticator.DefaultLoginUser;
import com.sparrow.authenticator.Signature;
import com.sparrow.authenticator.enums.AuthenticatorError;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.LoginUser;
import com.sparrow.protocol.constant.SparrowError;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

/**
 * RSA 签名验签功能，支持全局统一的默认公私钥对
 * 也支持用户自定义公私钥对。如果用户自定义上传公私钥对一定要符合公私钥格式 格式参考
 *  KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
 *  generator.initialize(2048);
 *  KeyPair keyPair = generator.generateKeyPair();
 *  String publicKey = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
 * @see java.security.KeyPairGenerator
 *
 * 注意：如果用户自定义的公私钥为自己的密码，则格式不正确 ，需要重写并重新注册签字对象，覆盖默认的签名对象
 * @see com.sparrow.authenticator.config.AuthcAutoConfiguration#signature
 */
@Slf4j
public class JwtRSSignature implements Signature {
    private PrivateKey defaultPrivateKey;
    private PublicKey defaultPublicKey;
    private SignatureAlgorithm signatureAlgorithm;
    private String issuer;

    public JwtRSSignature(AuthenticatorConfigReader configReader) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        //读取私钥
        String privateKey = readKeyContent(configReader.getPrivateKeyPath());
        if (StringUtils.isEmpty(StringUtils.trim(privateKey))) {
            log.error("[{}[ jwt private key not found ! ", configReader.getPrivateKeyPath());
            return;
        }
        //读取公钥
        String publicKey = readKeyContent(configReader.getPublicKeyPath());
        if (StringUtils.isEmpty(StringUtils.trim(publicKey))) {
            log.error("[{}[ jwt public key not found ! ", configReader.getPublicKeyPath());
            return;
        }
        this.defaultPrivateKey = parsePrivateKey(privateKey);
        this.defaultPublicKey = parsePublicKey(publicKey);
        this.signatureAlgorithm = SignatureAlgorithm.RS256;
        this.issuer = configReader.getJwtIssuer();
    }

    private static String readKeyContent(String location) {
        if (StringUtils.isEmpty(StringUtils.trim(location))) {
            return null;
        }
        String normalized = location.replace('\\', '/');
        try {
            Path file = Path.of(normalized);
            if (Files.exists(file)) {
                return Files.readString(file, StandardCharsets.UTF_8);
            }
            // 原工具使用 EnvironmentSupport.class.getResource，保留其包相对路径。
            String resourcePath = normalized.startsWith("/")
                    ? normalized.substring(1) : "com/sparrow/support/" + normalized;
            try (InputStream stream = new ClassPathResource(resourcePath).getInputStream()) {
                return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            log.error("[{}] jwt key not found or unreadable", location);
            return null;
        }
    }

    private static PrivateKey parsePrivateKey(String encodedKey) throws NoSuchAlgorithmException {
        byte[] decoded = Base64.getDecoder().decode(encodedKey.replaceAll("\\s", ""));
        try {
            return KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
        } catch (InvalidKeySpecException e) {
            log.error("failed to parse RSA private key", e);
            return null;
        }
    }

    private static PublicKey parsePublicKey(String encodedKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] decoded = Base64.getDecoder().decode(encodedKey.replaceAll("\\s", ""));
        return KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
    }


    @Override
    public String sign(LoginUser loginUser, String key) throws BusinessException {
        PrivateKey privateKey = this.defaultPrivateKey;
        if (!StringUtils.isEmpty(StringUtils.trim(key))) {
            try {
                //如果传入了用户私钥，则使用用户私钥进行签名
                //如果上层应用不使用公私钥的非对称加密，那么可以传用户密码作为加盐
                //如果用户传了密码，那么需要重写signer 接口
                privateKey = parsePrivateKey(key);
            } catch (Exception e) {
                log.error("faild to get user private key", e);
                throw new BusinessException(SparrowError.SYSTEM_SERVER_ERROR);
            }
        }
        //准备jwt荷载对象claims,本质就是map,我们可以在里面自定义任意key和value
        /**
         * 下面是一些常见的标准声明：
         * - "iss"（Issuer）: 表示JWT的签发者，即标识谁创建了该JWT。
         * - "sub"（Subject）: 表示JWT的主题，即标识该JWT所代表的用户或实体。
         * - "aud"（Audience）: 表示JWT的受众，即标识预期的接收者。
         * - "exp"（Expiration Time）: 表示JWT的过期时间，即标识JWT的有效期限。在该时间之后，该JWT将不再被接受。
         * - "nbf"（Not Before）: 表示JWT的生效时间，即在该时间之前，该JWT将不会被接受。
         * - "iat"（Issued At）: 表示JWT的签发时间，即标识JWT的创建时间。
         * - "jti"（JWT ID）: 表示JWT的唯一标识符，用于防止JWT重放攻击。
         */
        AuthClaims authClaims = new AuthClaims();
        authClaims.setSubject(loginUser.getUserId() + "");
        authClaims.setIssuer(this.issuer);
        authClaims.setId(UUID.randomUUID().toString());
        authClaims.setExpiration(new Date(loginUser.getExpireAt()));
        authClaims.setIssuedAt(new Date(System.currentTimeMillis()));
        authClaims.put("body", JSON.toJSONString(loginUser));
        return generateToken(authClaims, privateKey);
    }

    private String generateToken(Claims claims, PrivateKey privateKey) throws BusinessException {
        JwtBuilder builder = null;
        if (privateKey == null) {
            log.error("you are using null privateKey to encode jwt");
            throw new BusinessException(SparrowError.SYSTEM_SERVER_ERROR);
        }
        try {
            builder = Jwts.builder().claims(claims).signWith(privateKey, this.signatureAlgorithm);
        } catch (Exception e) {
            log.error("faild to generate rsa encoded jwt");
            throw new BusinessException(SparrowError.SYSTEM_SERVER_ERROR);
        }
        return builder.compact();
    }


    @Override
    public LoginUser verify(String token, String userPublicKey) throws BusinessException {
        PublicKey publicKey = this.defaultPublicKey;
        if (!StringUtils.isEmpty(StringUtils.trim(userPublicKey))) {
            try {
                publicKey = parsePublicKey(userPublicKey);
            } catch (Exception e) {
                log.error("faild to get user public key", e);
                throw new BusinessException(SparrowError.SYSTEM_SERVER_ERROR);
            }
        }
        Jws<Claims> jws = null;
        try {
            jws = Jwts.parser().verifyWith(publicKey).build().parseSignedClaims(token);
        } catch (Exception e) {
            log.error("faild to verify jwt token", e);
            throw new BusinessException(AuthenticatorError.USER_TOKEN_ABNORMAL);
        }
        Claims claims = jws.getBody();
//        JwsHeader header = jws.getHeader();
        String body = claims.get("body", String.class);
        return JSON.parseObject(body, DefaultLoginUser.class);
    }
}
