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

package com.sparrow.user.protocol.query.register;

import com.sparrow.protocol.ClientInformation;

public class RegisterQueryDTO {
    public RegisterQueryDTO() {
    }

    public RegisterQueryDTO(String password,
        String passwordConfirm,
        String validateCode,
        String introducer,
        ClientInformation client) {
        this.password = password;
        this.passwordConfirm = passwordConfirm;
        this.validateCode = validateCode;
        this.introducer = introducer;
        this.client = client;
    }

    /**
     * 密码
     */
    protected String password;
    /**
     * 密码确认
     */
    protected String passwordConfirm;
    /**
     * 验证码
     */
    protected String validateCode;

    /**
     * 介绍人
     */
    protected String introducer;

    private ClientInformation client;

    public ClientInformation getClient() {
        return client;
    }

    public String getPassword() {
        return password;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public String getValidateCode() {
        return validateCode;
    }

    public String getIntroducer() {
        return introducer;
    }
}
