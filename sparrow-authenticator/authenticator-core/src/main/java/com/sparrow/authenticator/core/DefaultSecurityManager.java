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

package com.sparrow.authenticator.core;

public class DefaultSecurityManager implements Authenticator, SecurityManager {
    private DigitalSigner digitalSigner;
    private Realm realm;
    private SessionDao sessionDao;
    private SessionManager sessionManager;


    public DefaultSecurityManager(SecurityConfig config) {

    }

    @Override
    public Session authenticate(AuthenticationToken token) {
        AuthenticationInfo authenticationInfo = this.realm.getAuthenticationInfo(token);
        Principal principal = this.digitalSigner.verify(token.getCredential(), authenticationInfo.getCredential());
        Session session = this.sessionManager.getSession(principal.sessionKey());
        if (session.isExpired()) {

        }
        if (!session.matchDeviceId(principal.deviceId())) {

        }
        if (session.getStatus() == 0) {

        }
        session.touch();
        this.sessionDao.update(session);
        return session;
    }

    @Override
    public String login(AuthenticationToken token) {
        AuthenticationInfo info= this.realm.getAuthenticationInfo(token);
        Principal principal=info.getPrincipal();
        Session session=this.sessionManager.start(principal);
        this.sessionDao.create(session);
        return this.digitalSigner.sign(principal,info.getCredential());
    }
}
