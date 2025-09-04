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

import com.sparrow.authenticator.*;
import com.sparrow.authenticator.enums.UserStatus;
import com.sparrow.authenticator.session.dao.DefaultSession;
import com.sparrow.protocol.LoginUser;

public class DefaultSessionManager implements SessionManager {

    private SessionDao sessionDao;
    private SessionParser sessionParser;

    public DefaultSessionManager(SessionDao sessionDao, SessionParser sessionParser) {
        this.sessionDao = sessionDao;
        this.sessionParser = sessionParser;
    }

    @Override
    public Session start(LoginUser loginUser) {
        DefaultSession session = new DefaultSession();
        SessionKey sessionKey = this.sessionParser.generateKey(loginUser);
        session.setSessionKey(sessionKey.sessionKey());
        session.setUserId(loginUser.getUserId());
        session.setCategory(loginUser.getCategory());
        session.setDeviceType(loginUser.getDeviceType());
        session.setHost(loginUser.getHost());
        session.setExpireAt(loginUser.getExpireAt());
        session.setStartTime(System.currentTimeMillis());
        session.setStatus(UserStatus.NORMAL.getIdentity());
        session.setLastAccessTime(System.currentTimeMillis());
        return session;
    }

    @Override
    public Session getSession(LoginUser loginUser) {
        SessionKey sessionKey = this.sessionParser.generateKey(loginUser);
        SessionStatus sessionStatus = this.sessionDao.get(sessionKey);
        DefaultSession session = (DefaultSession) this.sessionParser.parse((String) sessionKey.sessionKey(), sessionStatus);
        if (session.getExpireAt() == null) {
            session.setExpireAt(loginUser.getExpireAt());
        }
        return session;
    }
}
