/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"), you may not use this file except in compliance with
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

package com.sparrow.authenticator.enums;

import com.sparrow.protocol.ErrorSupport;
import com.sparrow.protocol.ModuleSupport;

public enum AuthenticatorError implements ErrorSupport {
    USER_NOT_LOGIN("30", "user not login"),
    USER_DISABLE("31", "user disable"),
    USER_DEVICE_KICKOUT("32", "user device kickout"),
    USER_KICKOUT("33", "user kickout"),
    USER_REPLACED("34", "user replaced"),
    USER_LOGIN_TOKEN_NOT_FOUND("35", "user token not found"),
    USER_DEVICE_NOT_MATCH("36", "user device not match"),
    USER_TOKEN_EXPIRED("37", "user token expired"),
    USER_TOKEN_ABNORMAL("38", "user token abnormal"),
    USER_TOKEN_ERROR("40", "user token error");


    private boolean system;
    private ModuleSupport module;
    private String code;
    private String message;

    AuthenticatorError(String code, String message) {
        this.system = false;
        this.message = message;
        this.module = AuthenticatorModule.AUTHC;
        this.code = (system ? 0 : 1) + module.code() + code;
    }

    @Override
    public boolean system() {
        return this.system;
    }

    @Override
    public ModuleSupport module() {
        return this.module;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
