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

import com.sparrow.protocol.enums.Platform;

import java.io.Serializable;

/**
 * @description: iOS: {"end_time":"1535971770858","os":"iOS","longitude":"116.446014","model":"iPhone10,1","start_time":"1535971439568","ssid":"sparrow-zoo.com","bssid":"10:c1:72:92:b4:c0","version":"11.4.1","latitude":"39.970479","resume_time":"1535971802314","idfa":"EF4F9CCD-3B6A-4553-95FB-486FB33885B7"}
 * Android: {"version":"8.0.0","os":"Android","start_time":"0","resume_time":"1535980166621","channel":"200","model":"Xiaomi||MI6","ssid":"\"sparrow-zoo.com\"","bssid":"10:c1:72:92:b4:d0","imei":"99000939602526","longitude":116.445757,"dzt":0,"latitude":39.970334,"network":"WIFI"}
 */

public class ClientInformation implements Serializable {
    /**
     * root path
     */
    private String website;
    /**
     * ip
     */
    private String ip;
    /**
     * 设备 iphone/三星/华为/浏览器
     */
    private String device;
    /**
     * unique id
     */
    private String deviceId;

    /**
     * 设备型号 iphone 5s/小米5
     */
    private String deviceModel;

    /**
     * app id
     */
    private int appId;

    /**
     * app version e.g 7.02
     */
    private float appVersion;

    /**
     * platform  PC IOS ANDROID 微信
     */
    private Platform platform;

    /**
     * 操作系统
     */
    private String os;

    private String userAgent;

    /**
     * os version
     */
    private String clientVersion;

    /**
     * 渠道
     */
    private String channel;

    /**
     * 经度
     */
    private double longitude;

    /**
     * 纬度
     */
    private double latitude;

    /**
     * 中国移动 中国联通 wifi
     */
    private String network;

    /**
     * 是否模拟机
     */
    private Boolean simulate;

    /**
     * International Mobile Equipment Identity
     */
    private String imei;
    /**
     * Basic Service Set IDentifier
     */
    private String bssid;

    /**
     * SSID = Service Set IDentifier
     */
    private String ssid;

    /**
     * for apple unique id
     */
    private String idfa;

    /**
     * 启动时间
     */
    private long startTime;

    /**
     * 重启时间
     */
    private long resumeTime;

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public float getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(float appVersion) {
        this.appVersion = appVersion;
    }

    public Platform getPlatform() {
        return platform;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getClientVersion() {
        return clientVersion;
    }

    public void setClientVersion(String clientVersion) {
        this.clientVersion = clientVersion;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public Boolean getSimulate() {
        return simulate;
    }

    public void setSimulate(Boolean simulate) {
        this.simulate = simulate;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getBssid() {
        return bssid;
    }

    public void setBssid(String bssid) {
        this.bssid = bssid;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getIdfa() {
        return idfa;
    }

    public void setIdfa(String idfa) {
        this.idfa = idfa;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getResumeTime() {
        return resumeTime;
    }

    public void setResumeTime(long resumeTime) {
        this.resumeTime = resumeTime;
    }
}
