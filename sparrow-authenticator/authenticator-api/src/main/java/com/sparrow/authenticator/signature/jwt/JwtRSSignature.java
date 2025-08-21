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

package com.sparrow.authenticator.signature.jwt;

import com.alibaba.fastjson.JSON;
import com.sparrow.authenticator.DefaultLoginUser;
import com.sparrow.authenticator.LoginUser;
import com.sparrow.authenticator.Signature;
import com.sparrow.authenticator.enums.AuthenticatorError;
import com.sparrow.cryptogram.RSAUtils;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.constant.SparrowError;
import com.sparrow.utility.StringUtility;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.UUID;

@Slf4j
public class JwtRSSignature implements Signature {
    private PrivateKey defaultPrivateKey;
    private PublicKey defaultPublicKey;
    private SignatureAlgorithm signatureAlgorithm;

    public JwtRSSignature(String defaultPublicKey, String defaultPrivateKey,SignatureAlgorithm signatureAlgorithm) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        this.defaultPrivateKey = RSAUtils.getRSAPrivateKey(defaultPrivateKey);
        this.defaultPublicKey = RSAUtils.getRSAPublicKey(defaultPublicKey);
        this.signatureAlgorithm = signatureAlgorithm;
    }

    public JwtRSSignature(String defaultPrivateKey, String defaultPublicKey) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        this(defaultPublicKey, defaultPrivateKey, SignatureAlgorithm.RS256);
    }


    @Override
    public String sign(LoginUser loginUser, String key) throws BusinessException {
        PrivateKey privateKey = this.defaultPrivateKey;
        if (!StringUtility.isNullOrEmpty(key)) {
            try {
                privateKey = RSAUtils.getRSAPrivateKey(key);
            }
            catch (Exception e){
                log.error("faild to get user private key", e);
                throw new BusinessException(SparrowError.SYSTEM_SERVER_ERROR);
            }
        }
        //准备jwt荷载对象claims,本质就是map,我们可以在里面自定义任意key和value
        /**
         * 下面是一些常见的标准声明：
         * - "iss"（Issuer）: 表示JWT的签发者，即标识谁创建了该JWT。
         * - "sub"（Subject）: 表示JWT的主题，即标识该JWT所代表的用户或实体。
         * - "aud"（Audience）: 表示JWT的受众，即标识预期的接收者。
         * - "exp"（Expiration Time）: 表示JWT的过期时间，即标识JWT的有效期限。在该时间之后，该JWT将不再被接受。
         * - "nbf"（Not Before）: 表示JWT的生效时间，即在该时间之前，该JWT将不会被接受。
         * - "iat"（Issued At）: 表示JWT的签发时间，即标识JWT的创建时间。
         * - "jti"（JWT ID）: 表示JWT的唯一标识符，用于防止JWT重放攻击。
         */
        AuthClaims authClaims = new AuthClaims();
        authClaims.setSubject(loginUser.getUserId() + "");
        authClaims.setIssuer("sparrow");
        authClaims.setId(UUID.randomUUID().toString());
        authClaims.setExpiration(new Date(loginUser.getExpireAt()));
        authClaims.setIssuedAt(new Date(System.currentTimeMillis()));
        authClaims.put("body", JSON.toJSONString(loginUser));
        return generateToken(authClaims,privateKey);
    }

    private String generateToken(Claims claims,PrivateKey privateKey) throws BusinessException {
        JwtBuilder builder = null;
        if (privateKey == null) {
            log.error("you are using null privateKey to encode jwt");
            throw new BusinessException(SparrowError.SYSTEM_SERVER_ERROR);
        }
        try {
            builder = Jwts.builder().setClaims(claims).signWith(this.signatureAlgorithm, privateKey);
        } catch (Exception e) {
            log.error("faild to generate rsa encoded jwt");
            throw new BusinessException(SparrowError.SYSTEM_SERVER_ERROR);
        }
        return builder.compact();
    }


    @Override
    public LoginUser verify(String token, String userPublicKey) throws BusinessException {
        PublicKey publicKey = this.defaultPublicKey;
        if (!StringUtility.isNullOrEmpty(userPublicKey)) {
            try {
                publicKey = RSAUtils.getRSAPublicKey(userPublicKey);
            }
            catch (Exception e){
                log.error("faild to get user public key", e);
                throw new BusinessException(SparrowError.SYSTEM_SERVER_ERROR);
            }
        }
        Jws<Claims> jws = null;
        try {
            jws = Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token);
        } catch (Exception e) {
            log.error("faild to verify jwt token", e);
            throw new BusinessException(AuthenticatorError.USER_TOKEN_ABNORMAL);
        }
        Claims claims = jws.getBody();
        JwsHeader header = jws.getHeader();
        String body = claims.get("body", String.class);
        return JSON.parseObject(body, DefaultLoginUser.class);
    }
}
