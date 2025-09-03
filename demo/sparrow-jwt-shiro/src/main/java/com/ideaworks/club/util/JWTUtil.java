package com.ideaworks.club.util;

import java.util.Date;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;

public class JWTUtil {
    // 5 分钟后过期
    private static final long EXPIRE_TIME = 5 * 60 * 10000000L;

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
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm).withClaim("username", username).build();
            @SuppressWarnings("unused")
            DecodedJWT jwt = verifier.verify(token);
            return true;
        } catch (Exception exception) {
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
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("username").asString();
        } catch (JWTDecodeException e) {
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
        Algorithm algorithm = Algorithm.HMAC256(secret);

        return JWT.create().withClaim("username", username).withExpiresAt(date).sign(algorithm);

    }

}
