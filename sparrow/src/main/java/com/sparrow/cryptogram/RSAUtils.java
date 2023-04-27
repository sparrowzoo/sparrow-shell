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
package com.sparrow.cryptogram;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RSAUtils {
    private static Logger log = LoggerFactory.getLogger(RSAUtils.class);

    // 加密算法 RSA
    private static final String RSA = "RSA";
    // 签名算法
    private static final String SHA256_WITH_RSA = "SHA256withRSA";

    private static final int INITIALIZE_LENGTH = 2048;

    /**
     * https://uutool.cn/rsa-generate
     *
     * @return
     */
    public static KeyPair generateKeyPair() {
        // 1.初始化秘钥
        KeyPairGenerator keyPairGenerator;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance(RSA);
            // 随机数生成器
            SecureRandom sr = new SecureRandom();
            // 设置秘钥长度
            keyPairGenerator.initialize(INITIALIZE_LENGTH, sr);
            // 开始创建
            return keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            log.error("generateKeys fail", e);
            return null;
        }
    }

    public static RSAPrivateKey getRSAPrivateKey(String privateKey) throws NoSuchAlgorithmException, IOException {
        //移除空白字符
        privateKey = privateKey.replaceAll("\\s", "");
        // 私钥要用PKCS8进行处理
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.decode(privateKey));
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        try {
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            log.error("get RSAPrivateKey fail ,priKey:[{}]]", privateKey, e);
            return null;
        }
    }

    public static RSAPublicKey getRSAPublicKey(String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
        publicKey = publicKey.replaceAll("\\s", "");
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.decode(publicKey));
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        try {
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (InvalidKeySpecException e) {
            log.error("get RSAPublicKey fail ,pubKey:[{}]", publicKey, e);
            throw e;
        }
    }


    public static String encryptByPublicKey(String content, PublicKey publicKey) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {
        // 公钥要用X509进行处理
        try {
            Cipher cipher = Cipher.getInstance(RSA);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] result = cipher.doFinal(content.getBytes());
            return Base64.encodeBytes(result);
        } catch (Exception e) {
            log.error("encrypt by publicKey fail ,content:[{}]", content, e);
            throw e;
        }
    }

    public static String decryptByPrivateKey(String content, PrivateKey privateKey) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, IOException {
        // 公钥要用X509进行处理
        try {
            Cipher cipher = Cipher.getInstance(RSA);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            byte[] result = cipher.doFinal(Base64.decode(content.getBytes()));
            return new String(result, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("encrypt by publicKey fail ,content:[{}]", content, e);
            throw e;
        }
    }


    public static String sign(String message, PrivateKey privateKey) throws Exception {
        Signature sign = Signature.getInstance(SHA256_WITH_RSA);
        sign.initSign(privateKey);
        sign.update(message.getBytes(StandardCharsets.UTF_8));
        return Base64.encodeBytes(sign.sign());
    }

    public static boolean verify(String message, String sign, PublicKey publicKey) throws Exception {
        Signature signature = Signature.getInstance(SHA256_WITH_RSA);
        signature.initVerify(publicKey);
        signature.update(message.getBytes(StandardCharsets.UTF_8));
        return signature.verify(Base64.decode(sign));
    }
}
