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
package com.sparrow.user.domain.entity;

import com.sparrow.constant.Regex;
import com.sparrow.constant.SparrowError;
import com.sparrow.exception.Asserts;
import com.sparrow.protocol.BusinessException;
import com.sparrow.user.support.suffix.UserFieldSuffix;
import com.sparrow.utility.RegexUtility;
import com.sparrow.utility.StringUtility;

public class RegisteringUserEntity {
    private String userName;
    private String email;
    private String mobile;
    private String password;
    private String passwordConfirm;
    private Long userId;
    private Long cent;

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }

    public String getPassword() {
        return password;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setUserName(String userName) throws BusinessException {
        Asserts.isTrue(!StringUtility.isNullOrEmpty(userName), SparrowError.GLOBAL_PARAMETER_NULL, UserFieldSuffix.USER_REGISTER);
        this.userName = userName;
    }

    public void setEmail(String email) throws BusinessException {
        Asserts.isTrue(email == null, SparrowError.GLOBAL_PARAMETER_NULL, UserFieldSuffix.USER_REGISTER_USER_EMAIL);
        this.email = email;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setPassword(String password) throws BusinessException {
        Asserts.isTrue(RegexUtility.matches(password, Regex.PASSWORD), SparrowError.USER_PASSWORD_FORMAT_ERROR, UserFieldSuffix.USER_REGISTER_PASSWORD);
        this.password = password;
    }

    public void setPasswordConfirm(String passwordConfirm) throws BusinessException {
        Asserts.isTrue(RegexUtility.matches(password, Regex.PASSWORD), SparrowError.USER_PASSWORD_FORMAT_ERROR, UserFieldSuffix.USER_REGISTER_CONFIRM_PASSWORD);
        Asserts.isTrue(passwordConfirm.equals(password), SparrowError.USER_PASSWORD_FORMAT_ERROR, UserFieldSuffix.USER_REGISTER_CONFIRM_PASSWORD);
        this.passwordConfirm = passwordConfirm;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCent() {
        return cent;
    }

    public void setCent(Long cent) {
        this.cent = cent;
    }
}
