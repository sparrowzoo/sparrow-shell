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
import com.sparrow.authenticator.LoginUser;
import com.sparrow.authenticator.signature.jwt.JwtRSSignature;
import com.sparrow.cryptogram.RSAUtils;
import com.sparrow.utility.FileUtility;

import java.security.PrivateKey;
import java.security.PublicKey;

public class RSAUtilsTest {
    public static void main(String[] args) throws Exception {
        //读取私钥
        String privateKey = FileUtility.getInstance().readFileContent("/rsa/test");
        System.out.printf("private key: %s", privateKey);

        //读取公钥
        String publicKey = FileUtility.getInstance().readFileContent("/rsa/test.pub");
        System.out.printf("public key: %s", publicKey);
        privateKey = privateKey.replaceAll("\\s", "");
        PrivateKey privateKey1 = RSAUtils.getRSAPrivateKey(privateKey);

        PublicKey publicKey1 = RSAUtils.getRSAPublicKey(publicKey);

        //RSAUtils.encryptByPublicKey("CONTENT",)

        System.out.println(privateKey1.toString());
        System.out.println(publicKey1.toString());

        //公钥加密
        String encrypt = RSAUtils.encryptByPublicKey("test", publicKey1);
        System.out.printf("encrypt: %s\n", encrypt);
        //私钥解密
        String content = RSAUtils.decryptByPrivateKey(encrypt, privateKey1);
        System.out.printf("content: %s\n", content);

        //私钥签名
        String sign = RSAUtils.sign("test", privateKey1);
        System.out.printf("sign: %s\n", sign);

        //公钥验签
        boolean verify = RSAUtils.verify("test", sign, publicKey1);
        System.out.printf("verify: %s\n", verify);


        JwtRSSignature jwtRSAGenerator = new JwtRSSignature(privateKey, publicKey);
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
        token = token += "22";
        LoginUser loginUser2 = jwtRSAGenerator.verify(token, null);
        System.out.println(JSON.toJSONString(loginUser2));

    }
}
