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
import com.sparrow.exception.Asserts;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.ClientInformation;
import com.sparrow.user.domain.entity.RegisteringUserEntity;
import com.sparrow.user.repository.RegisteringUserRepository;
import com.sparrow.user.support.DomainRegistry;
import com.sparrow.user.support.suffix.UserFieldSuffix;

public class RegisteringUserService {
    private DomainRegistry domainRegistry;

    public void setDomainRegistry(DomainRegistry domainRegistry) {
        this.domainRegistry = domainRegistry;
    }

    private void success(Long userId, ClientInformation client) throws BusinessException {

    }

    public void registerByEmail(RegisteringUserEntity register,
        ClientInformation client) throws BusinessException {
        domainRegistry.getUserLimitService().canRegister(client.getIp());
        RegisteringUserRepository registeringUserRepository = domainRegistry.getRegisteringUserRepository();
        RegisteringUserEntity oldUser = registeringUserRepository.findByEmail(register.getEmail());
        Asserts.isTrue(oldUser != null,
            SparrowError.USER_EMAIL_EXIST,
            UserFieldSuffix.USER_REGISTER_USER_EMAIL);
        //register.setCent(domainRegistry..getLongValueByCode(ConfigKeyDB.USER_CENT_REGISTER));
        registeringUserRepository.saveRegisteringUser(register);
        this.success(register.getUserId(), client);
    }
}
