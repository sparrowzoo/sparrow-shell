package com.ideaworks.club.util;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;

public class JWTUtil {
    // 5 分钟后过期
    private static final long EXPIRE_TIME = 5 * 60 * 1000L;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * 验证 token 是否正确
     *
     * @param token
     * @param username
     * @param secret
     * @return boolean
     */
    public static boolean verify(String token, String username, String secret) {
        try {
            String tokenUsername = Jwts.parser().verifyWith(key(secret)).build()
                    .parseSignedClaims(token).getPayload().get("username", String.class);
            return username.equals(tokenUsername);
        } catch (JwtException | IllegalArgumentException exception) {
            return false;
        }
    }

    /**
     * 从 token 中获取用户信息
     *
     * @param token
     * @return username
     */
    public static String getUsername(String token) {
        try {
            // This claim is untrusted until verify() checks the signature.
            String[] parts = token.split("\\.", -1);
            if (parts.length != 3) {
                return null;
            }
            byte[] payload = Base64.getUrlDecoder().decode(parts[1]);
            JsonNode username = OBJECT_MAPPER.readTree(payload).get("username");
            return username != null && username.isTextual() ? username.asText() : null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 生成签名 signature
     *
     * @param username
     * @param secret
     * @return 加密后的 token
     */
    public static String sign(String username, String secret) {
        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        return Jwts.builder().claim("username", username).expiration(date)
                .signWith(key(secret), Jwts.SIG.HS256).compact();
    }

    private static SecretKey key(String secret) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256")
                    .digest(secret.getBytes(StandardCharsets.UTF_8));
            return new SecretKeySpec(digest, "HmacSHA256");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 is unavailable", e);
        }
    }
}
