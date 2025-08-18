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

package com.sparrow.authenticator.session;

import com.sparrow.authenticator.LoginUser;
import com.sparrow.authenticator.Session;
import com.sparrow.authenticator.SessionKey;
import com.sparrow.authenticator.SessionStatus;
import com.sparrow.authenticator.session.dao.DefaultSession;

import java.io.Serializable;

public class DefaultSessionParser implements SessionParser {
    public Session parse(String sessionKey, SessionStatus status) {
        String key = sessionKey.toString();
        String[] keyArray = key.split(":");
        if (keyArray.length < 4) {
            throw new IllegalArgumentException("session key length is illegal");
        }
        DefaultSession session = new DefaultSession();
        session.setSessionKey(sessionKey);
        session.setUserId(Long.valueOf(keyArray[0]));
        session.setCategory(Integer.valueOf(keyArray[1]));
        session.setDeviceType(Integer.valueOf(keyArray[2]));
        session.setHost(keyArray[3]);
        session.setStartTime(status.getStartTime());
        session.setLastAccessTime(status.getLastAccessTime());
        session.setExpireAt(status.getExpireAt());
        session.setStatus(status.getStatus());
        return session;
    }

    public SessionKey generateKey(LoginUser loginUser) {
        return new SessionKey() {
            @Override
            public Serializable sessionKey() {
                return String.format("%1$s:%2$s:%3$s:%4$s", loginUser.getUserId(), loginUser.getCategory(), loginUser.getDeviceType(), loginUser.getHost());
            }
        };
    }
}
