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

import com.sparrow.utility.StringUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class Hmac {
    private static Logger logger = LoggerFactory.getLogger(Hmac.class);
    public static final String HMAC_SHA1 = "HmacSHA1";
    public static final String HMAC_SHA256 = "HmacSHA256";
    public static final String HMAC_SHA512 = "HmacSHA512";
    public static final String HMAC_MD5 = "HmacMD5";
    private static final int[] DIGITS_POWER
        // 0 1 2 3 4 5 6 7 8
        = {1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000};

    private static Hmac hmac = new Hmac();

    private Hmac() {
    }

    public static Hmac getInstance() {
        return hmac;
    }

    /**
     * HmacSHA1签名
     *
     * @param algorithm 加密算法
     * @param srcString 源串
     * @param secretKey 密钥
     * @return
     * @throws java.security.NoSuchAlgorithmException
     * @throws java.io.UnsupportedEncodingException
     * @throws java.security.InvalidKeyException
     */
    public byte[] cryptogram(String algorithm, String srcString, String secretKey) {
        try {
            byte[] oauthSignature = null;
            Mac mac = Mac.getInstance(algorithm);
            SecretKeySpec spec = new SecretKeySpec(secretKey
                .getBytes("UTF-8"), algorithm);
            mac.init(spec);
            oauthSignature = mac.doFinal(srcString.getBytes("US-ASCII"));
            return oauthSignature;
        } catch (Exception e) {
            logger.error("{} is error", algorithm, e);
            return null;
        }
    }

    public String cryptogramBase64(String algorithm, String srcString, String secretKey) {
        try {
            byte[] oauthSignature = this.cryptogram(algorithm, srcString, secretKey);
            return Base64.encodeBytes(oauthSignature);
        } catch (Exception e) {
            logger.error("encoder bytes error", e);
            return null;
        }
    }

    public String cryptogramHex(String algorithm, String srcString, String secretKey) {
        try {
            byte[] oauthSignature = this.cryptogram(algorithm, srcString, secretKey);
            return StringUtility.bytes2HexString(oauthSignature);
        } catch (Exception e) {
            return null;
        }
    }

    public String getSHA1Base64(String srcString, String secretKey) {
        return this.cryptogramBase64(HMAC_SHA1, srcString, secretKey);
    }

    public String getSHA256Base64(String srcString, String secretKey) {
        return this.cryptogramBase64(HMAC_SHA256, srcString, secretKey);
    }

    public String getSHA512Base64(String srcString, String secretKey) {
        return this.cryptogramBase64(HMAC_SHA512, srcString, secretKey);
    }

    public String getMD5Base64(String srcString, String secretKey) {
        return this.cryptogramBase64(HMAC_MD5, srcString, secretKey);
    }

    public String getSHA1Hex(String srcString, String secretKey) {
        return this.cryptogramHex(HMAC_SHA1, srcString, secretKey);
    }

    public String getSHA256Hex(String srcString, String secretKey) {
        return this.cryptogramHex(HMAC_SHA256, srcString, secretKey);
    }

    public String getSHA512Hex(String srcString, String secretKey) {
        return this.cryptogramHex(HMAC_SHA512, srcString, secretKey);
    }

    public String getMD5Hex(String srcString, String secretKey) {
        return this.cryptogramHex(HMAC_MD5, srcString, secretKey);
    }

    public byte[] getSHA1(String srcString, String secretKey) {
        return this.cryptogram(HMAC_SHA1, srcString, secretKey);
    }

    public byte[] getSHA256(String srcString, String secretKey) {
        return this.cryptogram(HMAC_SHA256, srcString, secretKey);
    }

    public byte[] getSHA512(String srcString, String secretKey) {
        return this.cryptogram(HMAC_SHA512, srcString, secretKey);
    }

    public byte[] getMD5(String srcString, String secretKey) {
        return this.cryptogram(HMAC_MD5, srcString, secretKey);
    }

    /**
     * K 共享密钥 T 时间 T0 开始计数的时间步长 X 时间步长
     *
     * @param secretKey
     * @param returnDigits
     * @param crypto
     * @return
     */
    public String generateTOTP(String secretKey,
        Integer returnDigits, String crypto, Integer seconds) {
        int codeDigits = returnDigits;
        String result;
        //取整后在seconds秒内T相等
        long t = (System.currentTimeMillis()) / (seconds * 1000);
        String time = Long.toHexString(t).toUpperCase();
        time = StringUtility.leftPad(time, '0', 16);
        // Get the HEX in a Byte[]
        byte[] hash = Hmac.getInstance().cryptogram(crypto, secretKey, time);
        // put selected bytes into result int
        int offset = hash[hash.length - 1] & 0xf;
        int binary = ((hash[offset] & 0x7f) << 24)
            | ((hash[offset + 1] & 0xff) << 16)
            | ((hash[offset + 2] & 0xff) << 8) | (hash[offset + 3] & 0xff);

        int otp = binary % DIGITS_POWER[codeDigits];
        result = Integer.toString(otp);
        result = StringUtility.leftPad(result, '0', codeDigits);
        return result;
    }
}
