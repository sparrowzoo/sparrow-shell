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
package com.sparrow.authorizing;

import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.LoginUser;
import com.sparrow.protocol.LoginUserStatus;
import com.sparrow.support.AbstractAuthenticatorService;

public class AuthenticatorDemo extends AbstractAuthenticatorService {

    @Override
    protected String getEncryptKey() {
        return null;
    }

    @Override
    protected String getDecryptKey() {
        return null;
    }

    @Override
    protected boolean validateDeviceId() {
        return true;
    }

    @Override
    protected boolean validateStatus() {
        return true;
    }

    @Override
    protected String sign(LoginUser loginUser, String secretKey) {
        return null;
    }

    @Override
    protected LoginUser verify(String token, String secretKey) throws BusinessException {
        return null;
    }

    @Override
    protected void setUserStatus(Long userId, LoginUserStatus loginUserStatus) {
//张三 t1 登录  token  t1+n 11
    }

    @Override
    protected LoginUserStatus getUserStatus(Long userId) {
        return null;
    }

    @Override
    protected LoginUserStatus getUserStatusFromDB(Long userId) {
        return null;
    }

    @Override
    protected void renewal(Long userId, LoginUserStatus loginUserStatus) {

    }
}
