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
package com.sparrow.test;

import com.sparrow.cryptogram.RSAUtils;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.LoginUser;
import com.sparrow.protocol.constant.SparrowError;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;

public class JwtRSAGenerator {
    public JwtRSAGenerator(String privateKey, String publicKey) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        this.privateKey = RSAUtils.getRSAPrivateKey(privateKey);
        this.publicKey = RSAUtils.getRSAPublicKey(publicKey);
    }

    private static Logger logger = LoggerFactory.getLogger(JwtRSAGenerator.class);
    private PrivateKey privateKey;
    private PublicKey publicKey;


    public String generateToken(LoginUser loginUser) throws BusinessException, NoSuchAlgorithmException, IOException {
        //准备jwt荷载对象claims,本质就是map,我们可以在里面自定义任意key和value
        AuthClaims authClaims = new AuthClaims();
        // authClaims.setId(UUID.randomUUID() + "");
        authClaims.setSubject(loginUser.getUserId() + "");
        authClaims.setIssuer(authClaims.getSubject());
        //authClaims.setIssuedAt(new Date());
        authClaims.put("userName", loginUser.getUserName());
        return generateToken(authClaims);
    }

    private String generateToken(Claims claims) throws NoSuchAlgorithmException, IOException, BusinessException {
        JwtBuilder builder = null;
        if (privateKey == null) {
            logger.error("you are using null privateKey to encode jwt");
            throw new BusinessException(SparrowError.SYSTEM_SERVER_ERROR);
        }
        try {
            builder = Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.RS256, privateKey);
        } catch (Exception e) {
            logger.error("faild to generate rsa encoded jwt");
            throw new BusinessException(SparrowError.SYSTEM_SERVER_ERROR);
        }
        return builder.compact();
    }


    public LoginUser parseToken(String token) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException, BusinessException {
        Jws<Claims> jws = null;
        try {
            jws = Jwts.parser().setSigningKey(this.publicKey).parseClaimsJws(token);
        } catch (Exception e) {
            throw new BusinessException(SparrowError.SYSTEM_SERVER_ERROR);
        }
        Claims claims = jws.getBody();
        //TODO 转换成login user
        return null;
    }
}
