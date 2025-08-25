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

import com.sparrow.authenticator.AuthenticatorConfigReader;
import com.sparrow.authenticator.Signature;
import com.sparrow.authenticator.realm.EmptyRealm;
import com.sparrow.authenticator.resolvers.LoginUserArgumentResolver;
import com.sparrow.authenticator.session.DefaultSessionParser;
import com.sparrow.authenticator.session.SessionParser;
import com.sparrow.authenticator.signature.jwt.JwtRSSignature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import javax.inject.Inject;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@Slf4j
@AutoConfigureAfter(AuthenticatorConfig.class)
public class BasicAutoConfiguration {
    public BasicAutoConfiguration() {
        log.info("Initializing BasicAutoConfiguration");
    }

    /**
     * WHY?
     */
    @Inject
    AuthenticatorConfigReader configReader;

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
    @ConditionalOnMissingBean(LoginUserArgumentResolver.class)
    public LoginUserArgumentResolver loginUserArgumentResolver() {
        return new LoginUserArgumentResolver();
    }

    @Bean
    @ConditionalOnMissingBean(Signature.class)
    public Signature signature(AuthenticatorConfigReader configReader) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        return new JwtRSSignature(configReader.getJwtIssuer());
    }
}
