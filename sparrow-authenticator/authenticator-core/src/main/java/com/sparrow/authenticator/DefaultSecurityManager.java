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

package com.sparrow.authenticator;

import com.sparrow.authenticator.enums.AuthenticatorError;
import com.sparrow.authenticator.enums.UserStatus;
import com.sparrow.authenticator.notifier.AuthcEventType;
import com.sparrow.authenticator.notifier.Notifier;
import com.sparrow.authenticator.notifier.NotifyRegistry;
import com.sparrow.authenticator.notifier.impl.LoginEvent;
import com.sparrow.authenticator.session.SessionParser;
import com.sparrow.exception.Asserts;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.LoginUser;

public class DefaultSecurityManager implements Authenticator {
    private Signature signature;
    private Realm realm;
    private SessionManager sessionManager;
    private SessionDao sessionDao;
    private AuthenticatorConfigReader config;
    private SessionParser sessionParser;


    public DefaultSecurityManager(
            SessionParser sessionParser,
            Realm realm,
            Signature signature,
            SessionManager sessionManager,
            SessionDao sessionDao,
            AuthenticatorConfigReader config) {
        this.config = config;
        this.realm = realm;
        this.signature = signature;
        this.sessionManager = sessionManager;
        this.sessionDao = sessionDao;
        this.sessionParser = sessionParser;
    }

    @Override
    public LoginUser authenticate(HostAuthenticationToken token) throws BusinessException {
        Boolean validateDevice = this.config.getValidateHost();
        Boolean isRenewal = this.config.getRenewal();
        Boolean validateStatus = this.config.getValidateStatus();
        if (validateDevice == null) {
            validateDevice = true;
        }
        if (isRenewal == null) {
            isRenewal = true;
        }
        if (validateStatus == null) {
            validateStatus = true;
        }
        AuthenticationInfo authenticationInfo = this.realm.getAuthenticationInfo(token);
        LoginUser loginUser = this.signature.verify(token.getCredential(), authenticationInfo.getCredential());
        Session session = this.sessionManager.getSession(loginUser);
        Asserts.isTrue(session.expire(), AuthenticatorError.USER_TOKEN_EXPIRED);
        Asserts.isTrue(validateDevice && !session.matchHost(token.getHost()), AuthenticatorError.USER_DEVICE_NOT_MATCH);
        if (validateStatus) {
            Asserts.isTrue(UserStatus.KICK_OUT.getIdentity().equals(session.getStatus()), AuthenticatorError.USER_KICKOUT);
            Asserts.isTrue(UserStatus.DEVICE_KICK_OUT.getIdentity().equals(session.getStatus()), AuthenticatorError.USER_DEVICE_KICKOUT);
            Asserts.isTrue(UserStatus.REPLACED.getIdentity().equals(session.getStatus()), AuthenticatorError.USER_REPLACED);
            Asserts.isTrue(UserStatus.DISABLED.getIdentity().equals(session.getStatus()), AuthenticatorError.USER_DISABLE);
        }
        session.touch();
        Long sessionTimeout = this.config.getSessionTimeout();
        if (sessionTimeout == null) {
            sessionTimeout = 30 * 60 * 1000L;
        }
        if (isRenewal && !session.exceedSessionTimeout(sessionTimeout)) {
            Long renewalInterval = this.config.getRenewalInterval();
            if (renewalInterval == null) {
                renewalInterval = 60 * 60 * 1000L;
            }
            session.renewal(renewalInterval);
            this.sessionDao.update(session);
        }
        return loginUser;
    }

    @Override
    public String login(AuthenticationInfo authentication) throws BusinessException {
        LoginUser loginUser = authentication.getUser();
        Session session = this.sessionManager.start(loginUser);
        this.sessionDao.create(session);
        Notifier<LoginUser> notifier = NotifyRegistry.getInstance().getObject(AuthcEventType.LOGIN.name());
        notifier.notify(new LoginEvent(loginUser));
        return this.signature.sign(loginUser, authentication.getCredential());
    }
}
