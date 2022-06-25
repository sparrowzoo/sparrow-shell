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

import java.security.MessageDigest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageSignature {
    private Logger logger = LoggerFactory.getLogger(MessageSignature.class);
    private static MessageSignature messageDigest = new MessageSignature();

    public static MessageSignature getInstance() {
        return messageDigest;
    }

    public static final String MD5 = "MD5";
    public static final String SHA1 = "SHA-1";
    public static final String SHA256 = "SHA-256";

    public String cryptogram(String source, String algorithm, String charset) {
        MessageDigest md = null;
        String cryptogram = null;
        try {
            md = java.security.MessageDigest.getInstance(algorithm);
            byte[] bytes = null;
            if (charset != null) {
                bytes = source.getBytes(charset);
            } else {
                bytes = source.getBytes();
            }
            byte[] digest = md.digest(bytes);
            cryptogram = StringUtility.byteToStr(digest);
        } catch (Exception ignored) {
            logger.error("cryptogram error", ignored);
        }
        return cryptogram;
    }

    /**
     * 默认utf-8 编码
     *
     * @param source
     * @return
     */
    public String md5(String source) {
        return this.cryptogram(source, MD5, Constant.CHARSET_UTF_8);
    }

    /**
     * 默认utf-8编码
     *
     * @param source
     * @return
     */
    public String sha1(String source) {
        return this.cryptogram(source, SHA1, Constant.CHARSET_UTF_8);
    }

    public String md5(String source, String charset) {
        return this.cryptogram(source, MD5, charset);
    }

    public String sha1(String source, String charset) {
        return this.cryptogram(source, SHA1, charset);
    }
}
