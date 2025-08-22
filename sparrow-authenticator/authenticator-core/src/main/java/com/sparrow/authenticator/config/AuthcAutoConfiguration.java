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

package com.sparrow.authenticator.config;

import com.sparrow.authenticator.*;
import com.sparrow.authenticator.realm.EmptyRealm;
import com.sparrow.authenticator.resolvers.LoginUserArgumentResolver;
import com.sparrow.authenticator.session.DefaultSessionManager;
import com.sparrow.authenticator.session.DefaultSessionParser;
import com.sparrow.authenticator.session.SessionParser;
import com.sparrow.authenticator.session.dao.RedisSessionDao;
import com.sparrow.authenticator.signature.jwt.JwtRSSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;


public class AuthcAutoConfiguration {
    @Autowired
    private AuthenticatorConfigReader configReader;

    @Bean
    @ConditionalOnMissingBean(EmptyRealm.class)
    public EmptyRealm emptyRealm() {
        return new EmptyRealm();
    }

    @Bean
    @ConditionalOnMissingBean(SessionParser.class)
    public SessionParser sessionParser() {
        return new DefaultSessionParser();
    }



    @Bean
    @ConditionalOnMissingBean(Signature.class)
    public Signature signature() throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        return new JwtRSSignature(this.configReader.getJwtIssuer());
    }

    @Bean
    @ConditionalOnMissingBean(SessionManager.class)
    public SessionManager sessionManager(SessionDao sessionDao, SessionParser sessionParser) {
        return new DefaultSessionManager(sessionDao, sessionParser);
    }


    @Bean
    @ConditionalOnMissingBean(Authenticator.class)
    public Authenticator securityManager(Realm realm,
                                         SessionParser sessionParser,
                                         SessionDao sessionDao,
                                         Signature signature,
                                         SessionManager sessionManager,
                                         AuthenticatorConfigReader configReader
    ) {
        return new DefaultSecurityManager(sessionParser, realm, signature, sessionManager, sessionDao, configReader);
    }


    @Bean
    @ConditionalOnMissingBean(LoginUserArgumentResolver.class)
    public LoginUserArgumentResolver loginUserArgumentResolver() {
        return new LoginUserArgumentResolver();
    }


    @ConditionalOnClass(RedisTemplate.class)
    public static class RedisSessionDaoConfig {
        @Bean
        @ConditionalOnMissingBean(SessionDao.class)
        public RedisSessionDao redisSessionDao(RedisTemplate redisTemplate, SessionParser sessionParser) {
            return new RedisSessionDao(redisTemplate, sessionParser);
        }
    }
}
