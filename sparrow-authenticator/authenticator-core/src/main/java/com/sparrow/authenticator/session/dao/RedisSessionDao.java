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

package com.sparrow.authenticator.session.dao;

import com.sparrow.authenticator.Session;
import com.sparrow.authenticator.SessionDao;
import com.sparrow.authenticator.SessionKey;
import com.sparrow.authenticator.SessionStatus;
import com.sparrow.authenticator.session.SessionParser;
import com.sparrow.core.spi.JsonFactory;
import com.sparrow.json.Json;
import com.sparrow.utility.CollectionsUtility;
import com.sparrow.utility.StringUtility;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class RedisSessionDao implements SessionDao {
    private static final String TERMINAL_KEY = "authc:status:terminal:%s";
    private static final String USER_KEY = "authc:status:%s";

    private SessionParser sessionParser;

    public RedisSessionDao(RedisTemplate redisTemplate, SessionParser sessionParser) {
        this.redisTemplate = redisTemplate;
        this.sessionParser = sessionParser;
    }

    private RedisTemplate redisTemplate;
    private Json json = JsonFactory.getProvider();

    private String getTerminalKey(Serializable key) {
        return String.format(TERMINAL_KEY, key);
    }

    @Override
    public void create(Session session) {
        String key = this.getTerminalKey(session.sessionKey());
        SessionStatus sessionStatus = session.getSessionStatus();
        redisTemplate.opsForValue().set(key, json.toString(sessionStatus));
        redisTemplate.expireAt(key, new Date(session.getExpireAt()));
        String userKey = String.format(USER_KEY, session.getUserId());
        redisTemplate.opsForSet().add(userKey, session.sessionKey());
        long ttl = redisTemplate.getExpire(userKey);
        //所有key中取最长redis过期时间
        long expireAt = Long.max(session.getExpireAt(), System.currentTimeMillis() + ttl);
        redisTemplate.expireAt(userKey, new Date(expireAt));
    }

    @Override
    public void update(Session session) {
        this.create(session);
    }

    @Override
    public void delete(Session session) {
        this.redisTemplate.delete(this.getTerminalKey(session.sessionKey()));
        String userKey = String.format(USER_KEY, session.getUserId());
        if (redisTemplate.opsForSet().size(userKey) == 0) {
            redisTemplate.delete(userKey);
            return;
        }
        redisTemplate.opsForSet().remove(userKey, session.sessionKey());
    }

    @Override
    public SessionStatus get(SessionKey key) {
        String value = (String) this.redisTemplate.opsForValue().get(this.getTerminalKey(key.sessionKey()));
        return this.json.parse(value, SessionStatus.class);
    }

    @Override
    public List<Session> getSessions(Object userId) {
        String userKey = String.format(USER_KEY, userId);
        Set<String> sessionKeys = this.redisTemplate.opsForSet().members(userKey);
        List<String> keys = new ArrayList<>();
        for (String sessionKey : sessionKeys) {
            keys.add(getTerminalKey(sessionKey));
        }
        List<String> values = this.redisTemplate.opsForValue().multiGet(sessionKeys);
        List<Session> sessions = new ArrayList<>();
        for (int i = 0; i < sessions.size(); i++) {
            String value = values.get(i);
            if (StringUtility.isNullOrEmpty(value)) {
                continue;
            }
            SessionStatus sessionStatus = this.json.parse(value, SessionStatus.class);
            String key = keys.get(i);
            Session session = this.sessionParser.parse(key, sessionStatus);
            sessions.add(session);
        }
        if (CollectionsUtility.isNullOrEmpty(sessions)) {
            this.redisTemplate.delete(userKey);
        }
        return sessions;
    }
}
