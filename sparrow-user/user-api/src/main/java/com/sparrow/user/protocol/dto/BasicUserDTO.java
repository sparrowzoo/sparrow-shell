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
package com.sparrow.user.protocol.dto;

import com.sparrow.protocol.POJO;
import java.sql.Date;

public class BasicUserDTO implements POJO {
    private Long userId;

    private String userName;
    private String nickName;
    private String avatar;
    private String zone;
    private String gender;
    private Date birthday;
    private String email;
    private String mobile;
    private Long cent;
    private Boolean activate;
    private Long activateTime;
    private Long createTime;
    private Long updateTime;
    private Long lastLoginTime;
    private String device;
    private String deviceModel;
    private String deviceId;
    private Long ip;
    private String personalSignature;
    private String status;
    private Boolean isOnline;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
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

    public Long getCent() {
        return cent;
    }

    public void setCent(Long cent) {
        this.cent = cent;
    }

    public Boolean getActivate() {
        return activate;
    }

    public void setActivate(Boolean activate) {
        this.activate = activate;
    }

    public Long getActivateTime() {
        return activateTime;
    }

    public void setActivateTime(Long activateTime) {
        this.activateTime = activateTime;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public Long getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Long lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Long getIp() {
        return ip;
    }

    public void setIp(Long ip) {
        this.ip = ip;
    }

    public String getPersonalSignature() {
        return personalSignature;
    }

    public void setPersonalSignature(String personalSignature) {
        this.personalSignature = personalSignature;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public Boolean getOnline() {
        return isOnline;
    }

    public void setOnline(Boolean online) {
        isOnline = online;
    }

    @Override public String toString() {
        return "BasicUserDTO{" +
            "userId=" + userId +
            ", userName='" + userName + '\'' +
            ", nickName='" + nickName + '\'' +
            ", avatar='" + avatar + '\'' +
            ", zone='" + zone + '\'' +
            ", gender='" + gender + '\'' +
            ", birthday=" + birthday +
            ", email='" + email + '\'' +
            ", mobile='" + mobile + '\'' +
            ", cent=" + cent +
            ", activate=" + activate +
            ", activateTime=" + activateTime +
            ", createTime=" + createTime +
            ", updateTime=" + updateTime +
            ", lastLoginTime=" + lastLoginTime +
            ", device='" + device + '\'' +
            ", deviceModel='" + deviceModel + '\'' +
            ", deviceId='" + deviceId + '\'' +
            ", ip=" + ip +
            ", personalSignature='" + personalSignature + '\'' +
            ", status='" + status + '\'' +
            ", isOnline=" + isOnline +
            '}';
    }
}
