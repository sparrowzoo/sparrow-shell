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

package com.sparrow.authenticator.session.dao;

import com.sparrow.authenticator.Session;
import com.sparrow.authenticator.SessionStatus;
import lombok.Data;

import java.io.Serializable;

@Data
public class DefaultSession extends DefaultSessionStatus implements Session {
    public DefaultSession() {
    }

    private Serializable sessionKey;
    private Long userId;
    private Integer category;
    private Integer deviceType;
    private String host;

    @Override
    public Long getStartTime() {
        return this.startTime;
    }

    @Override
    public Long getLastAccessTime() {
        return this.lastAccessTime;
    }

    @Override
    public Long getExpireAt() {
        return this.expireAt;
    }

    @Override
    public Long getUserId() {
        return this.userId;
    }

    @Override
    public Integer getCategory() {
        return category;
    }

    @Override
    public Integer getDeviceType() {
        return this.deviceType;
    }

    @Override
    public String getHost() {
        return this.host;
    }

    @Override
    public SessionStatus getSessionStatus() {
        return new DefaultSessionStatus(this.startTime, this.lastAccessTime, this.expireAt, this.status);
    }

    @Override
    public void touch() {
        this.lastAccessTime = System.currentTimeMillis();
    }

    @Override
    public void renewal(Long renewalInterval) {
        this.expireAt = System.currentTimeMillis() + renewalInterval;
    }

    @Override
    public void modifyStatus(Integer status) {
        this.status = status;
    }

    @Override
    public boolean matchHost(String host) {
        if (this.host == null) {
            return false;
        }
        return this.host.equals(host);
    }

    @Override
    public boolean exceedSessionTimeout(Long sessionTimeout) {
        long diff = this.expireAt - System.currentTimeMillis();
        return diff > sessionTimeout;
    }

    @Override
    public Serializable sessionKey() {
        return this.sessionKey;
    }

    @Override
    public Boolean expire() {
        return System.currentTimeMillis() > this.expireAt;
    }

    @Override
    public Integer getStatus() {
        return this.status;
    }
}
