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

package com.sparrow.authentication.autoconfiguration;

import com.sparrow.authenticator.*;
import com.sparrow.authenticator.realm.EmptyRealm;
import com.sparrow.authenticator.session.DefaultSessionParser;
import com.sparrow.authenticator.session.SessionParser;
import com.sparrow.authenticator.session.dao.RedisSessionDao;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;

public class FilterConfiguration {
    @Bean
    public EmptyRealm emptyRealm() {
        return new EmptyRealm();
    }

    @Bean
    public SessionParser sessionParser() {
        return new DefaultSessionParser();
    }

    @Bean
    public RedisSessionDao redisSessionDao(RedisTemplate redisTemplate,SessionParser sessionParser) {
        return new RedisSessionDao(redisTemplate, sessionParser);
    }


    @Bean
    public Authenticator securityManager(Realm realm,
                                           SessionParser sessionParser,
                                           SessionDao sessionDao,
                                           Signature signature,
                                           SessionManager sessionManager,
                                           AuthenticatorConfigReader configReader
                                           ) {
        return new DefaultSecurityManager(sessionParser,realm,signature,sessionManager,sessionDao,configReader);
    }
}
