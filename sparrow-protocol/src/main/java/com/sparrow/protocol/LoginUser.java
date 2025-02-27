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
package com.sparrow.protocol;

import java.util.LinkedHashMap;
import java.util.Map;

public class LoginUser implements VO {
    /**
     * 游客ID
     */
    public static final Long VISITOR_ID = 0L;
    public static final int CATEGORY_VISITOR = 0;
    public static final int CATEGORY_REGISTER = 1;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户类型
     */
    private Integer category;
    /**
     * 用户昵称
     */
    private String nickName;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 头象
     */
    private String avatar;
    /**
     * 设备id
     */
    private String deviceId;

    private Integer days;

    private Long expireAt;

    private Map<String, Object> extensions = new LinkedHashMap<>();

    public static LoginUser create(Long userId,
                                   Integer category,
                                   String userName,
                                   String nickName,
                                   String avatar,
                                   String deviceId,
                                   Integer days) {
        LoginUser login = new LoginUser();
        login.userId = userId;
        login.category = category;
        login.userName = userName;
        login.nickName = nickName;
        login.avatar = avatar;
        login.days = days;
        login.expireAt = System.currentTimeMillis() + days * 24 * 60 * 60 * 1000;
        login.deviceId = deviceId;
        return login;
    }

    public Long getUserId() {
        return userId;
    }

    public String getNickName() {
        return nickName;
    }

    public String getUserName() {
        return userName;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public Integer getDays() {
        return days;
    }

    public Long getExpireAt() {
        return expireAt;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public void setExpireAt(Long expireAt) {
        this.expireAt = expireAt;
    }

    public Map<String, Object> getExtensions() {
        return extensions;
    }

    public void setExtensions(Map<String, Object> extensions) {
        this.extensions = extensions;
    }

    public boolean isVisitor() {
        return VISITOR_ID.equals(this.getUserId());
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }
}
