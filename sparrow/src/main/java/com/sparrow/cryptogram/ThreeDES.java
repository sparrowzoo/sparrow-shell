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

import com.sparrow.protocol.constant.Constant;
import com.sparrow.utility.StringUtility;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreeDES {

    private static Logger logger = LoggerFactory.getLogger(ThreeDES.class);

    private static ThreeDES threeDES = new ThreeDES();

    public static ThreeDES getInstance() {
        return threeDES;
    }

    /**
     * DESede(3DES)
     */
    private static final String ALGORITHM = "DESede";

    public static final String CIPHER_ALGORITHM_ECB = "DESede/ECB/PKCS5Padding";
    public static final String CIPHER_ALGORITHM_CBC = "DESede/CBC/PKCS5Padding";

    private static final int KEY_SIZE = 24; //

    public String encryptHex(String key, String text, String cipherAlgorithm) {
        byte[] encryptBytes = this.encryptByAlgorithm(key, text, cipherAlgorithm);
        return StringUtility.bytes2HexString(encryptBytes);
    }

    public String decryptHex(String key, String encrypted, String cipherAlgorithm) {
        byte[] encryptBytes = null;
        try {
            encryptBytes = StringUtility.hexString2Bytes(encrypted);
        } catch (Exception e) {
            logger.error("decrypt error", e);
        }
        return this.decryptAlgorithm(key, encryptBytes, cipherAlgorithm);
    }

    public String encrypt(String key, String text, String cipherAlgorithm) {
        byte[] encryptBytes = this.encryptByAlgorithm(key, text, cipherAlgorithm);
        return Base64.encodeBytes(encryptBytes);
    }

    public String decrypt(String key, String encrypted, String cipherAlgorithm) {
        byte[] encryptBytes = null;
        try {
            encryptBytes = Base64.decode(encrypted);
        } catch (Exception e) {
            logger.error("charset not support", e);
        }
        return this.decryptAlgorithm(key, encryptBytes, cipherAlgorithm);
    }

    public String encryptHex(String key, String text) {
        return encryptHex(key, text, CIPHER_ALGORITHM_ECB);
    }

    public String decryptHex(String key, String encrypted) {
        return decryptHex(key, encrypted, CIPHER_ALGORITHM_ECB);
    }

    public String encrypt(String key, String text) {
        return encrypt(key, text, CIPHER_ALGORITHM_ECB);
    }

    public String decrypt(String key, String encrypted) {
        return decrypt(key, encrypted, CIPHER_ALGORITHM_ECB);
    }

    private byte[] encryptByAlgorithm(String key, String text, String cipherAlgorithm) {
        if (key == null || text == null) {
            return null;
        }
        byte[] encrypted = null;
        try {
            Cipher encrypter = createCipher(key, Cipher.ENCRYPT_MODE, cipherAlgorithm);
            encrypted = encrypter.doFinal(text.getBytes(Constant.CHARSET_UTF_8));
        } catch (Exception e) {
            logger.error("encrypt error", e);
        }
        if (encrypted != null) {
            return encrypted;
        }
        return null;
    }

    private String decryptAlgorithm(String key, byte[] encrypted, String cipherAlgorithm) {
        String decrypted = null;
        try {
            Cipher encrypter = createCipher(key, Cipher.DECRYPT_MODE, cipherAlgorithm);
            decrypted = new String(encrypter.doFinal(encrypted),
                Constant.CHARSET_UTF_8);
        } catch (Exception e) {
            logger.error("encrypt error", e);
        }
        return decrypted;
    }

    private Cipher createCipher(String key, int mode, String cipherAlgorithm)
        throws InvalidKeyException, InvalidKeySpecException,
        NoSuchAlgorithmException, NoSuchPaddingException,
        InvalidAlgorithmParameterException {

        if (key.length() > KEY_SIZE) {
            throw new IllegalArgumentException(
                "Key size must be under 24 characters.");
        } else {
            StringBuilder stringBuilder = new StringBuilder(key);
            while (stringBuilder.length() < KEY_SIZE) {
                stringBuilder.append(" ");
            }
            key = stringBuilder.toString();
        }

        byte[] keyValue = key.getBytes();
        DESedeKeySpec keySpec = new DESedeKeySpec(keyValue);
        SecretKey secretKey = SecretKeyFactory.getInstance(ALGORITHM)
            .generateSecret(keySpec);
        Cipher encrypter = Cipher.getInstance(cipherAlgorithm);
        if (!cipherAlgorithm.equals(CIPHER_ALGORITHM_ECB)) {
            IvParameterSpec iv = new IvParameterSpec(new byte[8]);
            encrypter.init(mode, secretKey, iv);
        } else {
            encrypter.init(mode, secretKey);
        }
        return encrypter;
    }
}
