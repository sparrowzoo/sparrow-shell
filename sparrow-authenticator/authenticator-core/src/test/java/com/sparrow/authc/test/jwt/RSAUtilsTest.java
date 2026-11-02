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

package com.sparrow.authc.test.jwt;


import com.alibaba.fastjson.JSON;
import com.sparrow.authenticator.DefaultLoginUser;
import com.sparrow.authenticator.config.AuthenticatorConfig;
import com.sparrow.authenticator.signature.jwt.JwtRSSignature;
import com.sparrow.protocol.LoginUser;

import javax.crypto.Cipher;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RSAUtilsTest {
    public static void main(String[] args) throws Exception {
        String privateKey;
        String publicKey;
        try (InputStream privateStream = RSAUtilsTest.class.getResourceAsStream("/rsa/test");
             InputStream publicStream = RSAUtilsTest.class.getResourceAsStream("/rsa/test.pub")) {
            if (privateStream == null || publicStream == null) {
                throw new IllegalStateException("RSA example key resources not found");
            }
            privateKey = new String(privateStream.readAllBytes(), StandardCharsets.UTF_8);
            publicKey = new String(publicStream.readAllBytes(), StandardCharsets.UTF_8);
        }
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey1 = keyFactory.generatePrivate(
                new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey.replaceAll("\\s", ""))));
        PublicKey publicKey1 = keyFactory.generatePublic(
                new X509EncodedKeySpec(Base64.getDecoder().decode(publicKey.replaceAll("\\s", ""))));

        //公钥加密
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey1);
        String encrypt = Base64.getEncoder().encodeToString(cipher.doFinal("test".getBytes(StandardCharsets.UTF_8)));
        System.out.printf("encrypt: %s\n", encrypt);
        //私钥解密
        cipher.init(Cipher.DECRYPT_MODE, privateKey1);
        String content = new String(cipher.doFinal(Base64.getDecoder().decode(encrypt)), StandardCharsets.UTF_8);
        System.out.printf("content: %s\n", content);

        //私钥签名
        Signature rsaSignature = Signature.getInstance("SHA256withRSA");
        rsaSignature.initSign(privateKey1);
        rsaSignature.update("test".getBytes(StandardCharsets.UTF_8));
        String sign = Base64.getEncoder().encodeToString(rsaSignature.sign());
        System.out.printf("sign: %s\n", sign);

        //公钥验签
        rsaSignature.initVerify(publicKey1);
        rsaSignature.update("test".getBytes(StandardCharsets.UTF_8));
        boolean verify = rsaSignature.verify(Base64.getDecoder().decode(sign));
        System.out.printf("verify: %s\n", verify);

        AuthenticatorConfig authenticatorConfig=new AuthenticatorConfig();
        authenticatorConfig.setPrivateKeyPath("/rsa/test");
        authenticatorConfig.setPublicKeyPath("/rsa/test.pub");


        JwtRSSignature jwtRSAGenerator = new JwtRSSignature(authenticatorConfig);
        DefaultLoginUser loginUser = new DefaultLoginUser();
        loginUser.setUserId(1L);
        loginUser.setNickName("harry");
        loginUser.setUserName("harry");
        loginUser.setAvatar("http://avatar.com");
        loginUser.setHost("");
        loginUser.setDays(2D);
        loginUser.setExpireAt(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 2);
//        loginUser.setExtensions();


        String token = jwtRSAGenerator.sign(loginUser, null);
        //token = token += "22";
        LoginUser loginUser2 = jwtRSAGenerator.verify(token, null);
        System.out.println(JSON.toJSONString(loginUser2));
    }
}
