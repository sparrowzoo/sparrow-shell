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

import com.alibaba.fastjson.JSON;
import com.sparrow.json.Json;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.LoginUser;
import io.jsonwebtoken.Claims;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.UUID;

public class JWTTest {
    public static void main(String[] args) throws BusinessException, NoSuchAlgorithmException, IOException {
        AuthClaims authClaims = new AuthClaims();
        authClaims.setId(UUID.randomUUID().toString())
                .setIssuer("zhangsan")
                .setSubject("zhangsan")
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 10))
                .setIssuedAt(new Date(System.currentTimeMillis()));
        authClaims.put("age", 100);
        authClaims.get("age", Integer.class);
        System.out.println(JSON.toJSONString(authClaims));

    }
}
