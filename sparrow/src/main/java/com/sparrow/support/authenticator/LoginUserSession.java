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

package com.sparrow.support.authenticator;

import com.sparrow.protocol.LoginUser;
import com.sparrow.protocol.LoginUserStatus;
import com.sparrow.protocol.enums.DeviceType;

import java.util.List;
import java.util.Map;

public class LoginUserSession implements Session {

    private LoginUser user;
    private LoginUserStatus loginUserStatus;

    private Long id;

    private Long startTime;

    private Long lastAccessTime;

    private Long expireAt;

    private Integer deviceType;

    private String deviceId;

    private Map<String, Object> attributes;

    private Integer deviceStatus;

    private Long timeout;

    public LoginUserSession(LoginUser user, LoginUserStatus loginUserStatus) {
        this.user = user;
        this.loginUserStatus = loginUserStatus;
        this.startTime = System.currentTimeMillis();
        this.timeout = loginUserStatus.getExpireAt() - this.startTime;
    }

    @Override
    public Long getId() {
        return this.user.getUserId();
    }

    @Override
    public Long getStartTime() {
        return this.startTime;
    }

    @Override
    public Long getLastAccessTime() {
        return this.lastAccessTime;
    }

    @Override
    public DeviceType getDeviceType() {
        return null;
    }

    @Override
    public String getDeviceId() {
        return null;
    }

    @Override
    public List<String> getAttributes() {
        return null;
    }

    @Override
    public Object getAttribute(String name) {
        return null;
    }

    @Override
    public void setAttribute(String name, Object value) {

    }

    @Override
    public void removeAttribute(String name) {

    }

    @Override
    public Integer getDeviceStatus() {
        return null;
    }

    @Override
    public Long expireAt() {
        return this.loginUserStatus.getExpireAt();
    }

    @Override
    public void touch() {

    }

    @Override
    public void modifyStatus(Integer status) {
        this.loginUserStatus.setStatus(status);
    }

    @Override
    public boolean isExpired() {
        return this.expireAt > System.currentTimeMillis();
    }
}
