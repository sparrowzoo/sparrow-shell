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
package com.sparrow.user.domain.service;

import com.sparrow.constant.SparrowError;
import com.sparrow.core.Pair;
import com.sparrow.exception.Asserts;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.ClientInformation;
import com.sparrow.protocol.constant.magic.Symbol;
import com.sparrow.protocol.enums.StatusRecord;
import com.sparrow.support.mobile.ShortMessageService;
import com.sparrow.user.domain.entity.SecurityPrincipalEntity;
import com.sparrow.user.domain.entity.UserStatusEntity;
import com.sparrow.user.domain.object.value.EmailFindPasswordToken;
import com.sparrow.user.domain.object.value.FindPasswordOriginToken;
import com.sparrow.user.repository.SecurityPrincipalRepository;
import com.sparrow.user.support.DomainRegistry;
import com.sparrow.user.support.suffix.UserFieldSuffix;
import com.sparrow.utility.DateTimeUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SecurityPrincipalService {
    private static Logger logger = LoggerFactory.getLogger(SecurityPrincipalService.class);

    private SecurityPrincipalEntity findByLoginName(String loginName,
        DomainRegistry domainRegistry) throws BusinessException {
        SecurityPrincipalRepository securityPrincipalRepository = domainRegistry.getSecurityPrincipalRepository();
        ShortMessageService shortMessageService = domainRegistry.getShortMessageService();
        SecurityPrincipalEntity securityPrincipal;

        if (loginName.contains(Symbol.AT)) {
            securityPrincipal = securityPrincipalRepository.findByEmail(loginName);
        } else {
            securityPrincipal = securityPrincipalRepository.findByName(loginName);
            if (securityPrincipal == null) {
                Pair<String, String> mobilePair = shortMessageService.secretMobile(loginName);
                securityPrincipal = securityPrincipalRepository.findByMobile(mobilePair.getFirst(), mobilePair.getSecond());
            }
        }
        Asserts.isTrue(securityPrincipal == null, SparrowError.USER_NAME_NOT_EXIST, UserFieldSuffix.USER_LOGIN_USER_NAME);
        UserStatusEntity userStatus = domainRegistry.getUserStatusRepository().findByUserId(securityPrincipal.getUserId());
        Asserts.isTrue(StatusRecord.DISABLE.ordinal() == userStatus.getStatus(),
            SparrowError.USER_DISABLED,
            UserFieldSuffix.USER_LOGIN);

        //CodeService codeService = domainRegistry.getCodeService();
        Boolean isActiveLogin = false;//codeService
        //.getBooleanValueByCode(ConfigKeyDB.USER_IS_ACTIVE_LOGIN);

        if (isActiveLogin == null) {
            isActiveLogin = true;
        }

        if (isActiveLogin && !securityPrincipal.getActivate()) {
            throw new BusinessException(SparrowError.USER_NOT_ACTIVATE, UserFieldSuffix.USER_LOGIN);
        }
        return securityPrincipal;
    }

    private void addLoginEvent(Long userId, ClientInformation client,
        DomainRegistry domainRegistry) throws BusinessException {

    }

    public void login(SecurityPrincipalEntity login, ClientInformation client,
        DomainRegistry domainRegistry) throws BusinessException {
        String loginName = login.getUserName();
        domainRegistry.getUserLimitService().canLogin(login.getUserId());
        SecurityPrincipalEntity securityPrincipal = this.findByLoginName(loginName, domainRegistry);
        String encryptPassword = domainRegistry.getEncryptionService().encryptPassword(login.getLoginPassword());
        securityPrincipal.validatePassword(encryptPassword, UserFieldSuffix.USER_LOGIN_PASSWORD);
        securityPrincipal.setLastLoginTime();
        Long loginCent = 100L;//domainRegistry.getCodeService().getLongValueByCode(ConfigKeyDB.USER_CENT_LOGIN);
        securityPrincipal.setCent(login.getCent() + loginCent);
        domainRegistry.getSecurityPrincipalRepository().saveSecurity(securityPrincipal);
        this.addLoginEvent(securityPrincipal.getUserId(), client, domainRegistry);
        Integer days = 1;
        if (securityPrincipal.getRememberMe()) {
            days = 10;//domainRegistry.getCodeService().getIntegerValueByCode(ConfigKeyDB.USER_LOGIN_REMEMBER_DAYS);
            if (days == null) {
                days = 15;
            }
        }
        securityPrincipal.setDays(days);
    }

    public EmailFindPasswordToken getFindPasswordEmailToken(String email,
        DomainRegistry domainRegistry) throws BusinessException {
        EncryptionService encryptionService = domainRegistry.getEncryptionService();
        SecurityPrincipalEntity securityPrincipal = domainRegistry.getSecurityPrincipalRepository().findByEmail(email);
        // 当前时间
        String currentDate = DateTimeUtility.getFormatCurrentTime();
        String originToken = FindPasswordOriginToken.createToken(securityPrincipal, currentDate).generateOriginToken();
        // 验证token
        String validateToken = encryptionService.generateToken(originToken, securityPrincipal.getPassword());
        String base64Email = encryptionService.base64Encode(email);
        return EmailFindPasswordToken.create(base64Email, validateToken);
    }

    public void resetPasswordByEmailToken(String token, String newPassword, DomainRegistry domainRegistry)
        throws BusinessException {
        EncryptionService encryptionService = domainRegistry.getEncryptionService();
        EmailFindPasswordToken emailFindPasswordToken = EmailFindPasswordToken.parse(token);
        String originEmail = encryptionService.base64Decode(emailFindPasswordToken.getBase64Email());
        SecurityPrincipalEntity securityPrincipal = domainRegistry.getSecurityPrincipalRepository().findByEmail(originEmail);
        String sourceToken = encryptionService.decryptToken(emailFindPasswordToken.getEncryptedToken(), securityPrincipal.getPassword());
        Asserts.isTrue(sourceToken == null, SparrowError.USER_PASSWORD_VALIDATE_TOKEN_ERROR);
        FindPasswordOriginToken findPasswordOriginToken = FindPasswordOriginToken.parse(sourceToken);
        findPasswordOriginToken.isValid(securityPrincipal.getUserName());
        securityPrincipal.setPassword(newPassword);
        domainRegistry.getSecurityPrincipalRepository().saveSecurity(securityPrincipal);
    }

    public void modifyPassword(SecurityPrincipalEntity securityPrincipal, ClientInformation client,
        DomainRegistry domainRegistry) throws BusinessException {
        EncryptionService encryptionService = domainRegistry.getEncryptionService();
        String encryptionLoginPassword = encryptionService.encryptPassword(securityPrincipal.getLoginPassword());
        securityPrincipal.validatePassword(encryptionLoginPassword, UserFieldSuffix.USER_OLD_PASSWORD);
        String newPassword = encryptionService.encryptPassword(securityPrincipal.getModifyingPassword());
        securityPrincipal.setPassword(newPassword);
        domainRegistry.getSecurityPrincipalRepository().saveSecurity(securityPrincipal);
        this.addPasswordModifiedEvent(securityPrincipal, client, domainRegistry);
    }

    private void addPasswordModifiedEvent(SecurityPrincipalEntity securityPrincipal, ClientInformation client,
        DomainRegistry domainRegistry) {

    }
}
