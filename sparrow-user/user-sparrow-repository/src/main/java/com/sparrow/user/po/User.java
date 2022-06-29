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
package com.sparrow.user.po;

import com.sparrow.protocol.POJO;
import com.sparrow.protocol.MethodOrder;

import java.sql.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "user")
public class User implements POJO, Cloneable {
    /*-------基本信息-------------*/
    private Long userId;
    private String userName;
    private String nickName;
    private String password;
    private String avatar;
    private Integer gender;
    private Date birthday;
    private String email;
    private String mobile;
    private Long cent;
    private Boolean activate;
    private Long activateTime;
    private Long createTime;
    private Long updateTime;
    private Long lastLoginTime;
    private String website;
    private Long ip;
    //设备
    private String device;
    private String deviceId;
    private String deviceModel;
    //referer
    private String channel;
    private Boolean isOnline;
    private String personalSignature;
    //是否可用enable disable
    private Integer status;
    /**
     * 用户自定义域名 全部子站共用一个域名
     */
    private String zone;

    private String groupLevel;

    private String secretMobile;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", columnDefinition = "int(11) UNSIGNED AUTO_INCREMENT")
    @MethodOrder(order = 1)
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @MethodOrder(order = 2)
    @Column(name = "user_name",columnDefinition = " varchar(64) DEFAULT '' COMMENT '用户名'",updatable = false, unique = true)
    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    @MethodOrder(order = 3)
    @Column(name = "nick_name", columnDefinition = "varchar(64)  DEFAULT '' COMMENT '昵称'")
    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    @MethodOrder(order = 4)
    @Column(name = "email",columnDefinition = "varchar(256) DEFAULT '' comment 'E-MAIL'" ,unique = true, updatable = false)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    @MethodOrder(order = 5)
    @Column(name = "password", columnDefinition = "varchar(32) DEFAULT '' COMMENT '密码'" ,updatable = false,nullable = false)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @MethodOrder(order = 6)
    @Column(name = "gender",columnDefinition = "tinyint(2) DEFAULT 0 COMMENT '性别'")
    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    @MethodOrder(order = 7)
    @Column(name = "avatar", columnDefinition = "varchar(256) DEFAULT '' COMMENT '头象'",updatable = false)
    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String headImg) {
        this.avatar = headImg;
    }

    @MethodOrder(order = 8)
    @Column(name = "personal_signature",columnDefinition = "varchar(256) DEFAULT '' COMMENT '签名'")
    public String getPersonalSignature() {
        return personalSignature;
    }

    public void setPersonalSignature(String personalSignature) {
        this.personalSignature = personalSignature;
    }

    @MethodOrder(order = 9)
    @Column(name = "birthday",columnDefinition = "date COMMENT '生日'")
    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    @MethodOrder(order = 10)
    @Column(name = "mobile", columnDefinition = "varchar(11) DEFAULT '0' COMMENT '手机号码'",updatable = false)
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @MethodOrder(order = 11)
    @Column(name = "last_login_time", columnDefinition = "bigint(11) DEFAULT 0 COMMENT '最近登录时间'", updatable = false)
    public Long getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Long lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    @MethodOrder(order = 12)
    @Column(name = "cent", columnDefinition = "bigint(10) DEFAULT 0 COMMENT '积分'", updatable = false)
    public Long getCent() {
        return cent;
    }

    public void setCent(Long cent) {
        this.cent = cent;
    }

    @MethodOrder(order = 13)
    @Column(name = "status",columnDefinition = "tinyint(1) DEFAULT 0 COMMENT '状态'", updatable = false)
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }




    @MethodOrder(order = 14)
    @Column(name = "is_online",columnDefinition = "tinyint(1) DEFAULT 0 COMMENT '是否在线'",updatable = false)
    public Boolean getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(Boolean isOnline) {
        this.isOnline = isOnline;
    }

    @MethodOrder(order = 15)
    @Column(name = "activate",columnDefinition = "tinyint(1) DEFAULT 0 COMMENT '是否激活'",updatable = false)
    public Boolean getActivate() {
        return this.activate;
    }

    public void setActivate(Boolean activate) {
        this.activate = activate;
    }

    @MethodOrder(order = 16)
    @Column(name = "activate_time",columnDefinition = "bigint(11) DEFAULT 0 COMMENT '激活时间'", updatable = false)
    public Long getActivateTime() {
        return activateTime;
    }

    public void setActivateTime(Long activeTime) {
        this.activateTime = activeTime;
    }

    @MethodOrder(order = 17)
    @Column(name = "zone",columnDefinition = "varchar(32) DEFAULT '' COMMENT '主页'", updatable = false)
    public String getZone() {
        return zone;
    }

    @MethodOrder(order = 18)
    @Column(name = "channel",columnDefinition = "varchar(128) DEFAULT '' COMMENT '渠道来源'", updatable = false)
    public String getChannel() {
        return channel;
    }

    public void setChannel(String origin) {
        this.channel = origin;
    }

    @MethodOrder(order = 19)
    @Column(name = "website",columnDefinition = "varchar(128) DEFAULT '' COMMENT '用户注册网站'",updatable = false)
    public String getWebsite() {
        return website;
    }

    @MethodOrder(order = 20)
    @Column(name = "group_level",columnDefinition = "varchar(64) DEFAULT '' COMMENT '用户等级'",updatable = false)
    public String getGroupLevel() {
        return groupLevel;
    }

    public void setGroupLevel(String groupLevel) {
        this.groupLevel = groupLevel;
    }

    @MethodOrder(order = 21)
    @Column(name = "secret_mobile",columnDefinition = "varchar(64) DEFAULT '' COMMENT  'SECRET MOBILE'",updatable = false)
    public String getSecretMobile() {
        return secretMobile;
    }

    public void setSecretMobile(String secretMobile) {
        this.secretMobile = secretMobile;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    @MethodOrder(order = 22)
    @Column(name = "device", columnDefinition = "varchar(16) DEFAULT '' COMMENT '设备'",updatable = false)
    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    @MethodOrder(order = 23)
    @Column(name = "device_id",columnDefinition = "varchar(64) DEFAULT '' COMMENT '设备id'",updatable = false)
    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    @MethodOrder(order = 24)
    @Column(name = "device_model", columnDefinition = "varchar(32) DEFAULT '' COMMENT '设备模型'",updatable = false)
    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }


    @MethodOrder(order = 26)
    @Column(name = "ip", columnDefinition = "bigint(10) DEFAULT 0 COMMENT 'ip'",updatable = false)
    public Long getIp() {
        return ip;
    }

    public void setIp(Long ip) {
        this.ip = ip;
    }

    @MethodOrder(order = 27)
    @Column(name = "create_time", columnDefinition = "bigint(11) DEFAULT 0 COMMENT '注册日期'",updatable = false,nullable = false)
    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    @MethodOrder(order = 28)
    @Column(name = "update_time",columnDefinition = "bigint(10) DEFAULT 0 COMMENT '最近更新时间'")
    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }


}
