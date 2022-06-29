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

public class SecurityPrincipalEntity {
    private Long userId;
    private String userName;
    /**
     * 当前密码
     */
    private String password;
    private String email;
    private String mobile;
    private Boolean activate;
    private Long cent;
    private Long lastLoginTime;
    private Boolean rememberMe;
    private Integer days;
    private String findPasswordEmailContent;
    /**
     * 用户登录密码
     */
    private String loginPassword;
    /**
     * 准备修改的新密码
     */
    private String modifyingPassword;

    public void setLastLoginTime(Long lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getFindPasswordEmailContent() {
        return findPasswordEmailContent;
    }

    public void setFindPasswordEmailContent(String findPasswordEmailContent) {
        this.findPasswordEmailContent = findPasswordEmailContent;
    }

    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }

    public String getModifyingPassword() {
        return modifyingPassword;
    }

    public void setModifyingPassword(String modifyingPassword) {
        this.modifyingPassword = modifyingPassword;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Boolean getActivate() {
        return activate;
    }

    public void setActivate(Boolean activate) {
        this.activate = activate;
    }

    public Long getCent() {
        return cent;
    }

    public void setCent(Long cent) {
        this.cent = cent;
    }

    public Long getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime() {
        this.lastLoginTime = System.currentTimeMillis();
    }

    public Boolean getRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(Boolean rememberMe) {
        this.rememberMe = rememberMe;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public void setModifyingPassword() throws BusinessException {
        //新密码为空或格式不正确
        if (StringUtility.isNullOrEmpty(this.modifyingPassword) || !RegexUtility
                .matches(this.modifyingPassword, Regex.PASSWORD)) {
            throw new BusinessException(SparrowError.USER_PASSWORD_FORMAT_ERROR,
                    UserFieldSuffix.USER_NEW_PASSWORD);
        }
    }

    public void validatePassword(String encryptLoginPassword,String suffix) throws BusinessException {
        Asserts.isTrue(!password.equals(encryptLoginPassword), SparrowError.USER_PASSWORD_ERROR, suffix);
    }
}
