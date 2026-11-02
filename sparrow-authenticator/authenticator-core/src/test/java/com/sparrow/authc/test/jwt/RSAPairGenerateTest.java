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

import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Base64;

/**
 * 通过两个参数指定公钥和私钥输出文件，不向控制台输出私钥。
 */
public class RSAPairGenerateTest {
    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.out.println("Usage: RSAPairGenerateTest <public-key-file> <private-key-file>");
            return;
        }
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        KeyPair keyPair = generator.generateKeyPair();

        String publicKey = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());

        String privateKey = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
        Files.writeString(Path.of(args[0]), publicKey);
        Files.writeString(Path.of(args[1]), privateKey);
    }
}
