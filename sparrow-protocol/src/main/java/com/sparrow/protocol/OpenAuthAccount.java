package com.sparrow.protocol;

import com.sparrow.protocol.enums.PLATFORM;

import java.io.Serializable;

/**
 * open auth platform account information
 *
 * @author: zh_harry@163.com
 * @date: 2019-04-06 18:45
 * @description:
 */
public class OpenAuthAccount implements Serializable {
    /**
     * 接入类型 pc app
     */
    private PLATFORM platform;
    /**
     * 应用名称 网站名称
     */
    private String name;
    /**
     * 开放平台
     */
    private String openPlatform;
    /**
     * app key
     */
    private String appKey;
    /**
     * 密钥
     */
    private String appSecret;
    /**
     * 回调的url
     */
    private String callBackUrl;
    /**
     * 状态
     */
    private String state;
    /**
     * 允许调用的方法
     */
    private String scope;

    /**
     * 分享的状态标志位
     */
    private long shareStatus;
    /**
     * 绑定的状态标志位
     */
    private long bindStatus;

    /**
     * 卖家商号
     */
    private String partner;

    /**
     * 卖家帐号
     */
    private String sellerAccount;

    /**
     * 支付密钥
     */
    private String paySecret;
    /**
     * 通知url
     */
    private String notifyUrl;

    private String charset;

    public OpenAuthAccount() {
    }

    public OpenAuthAccount(String name,
                           String openPlatform,
                           PLATFORM platform,
                           String appKey,
                           String appSecret,
                           String callBackUrl,
                           String state,
                           String scope,
                           long shareStatus,
                           long bindStatus) {
        this.name = name;
        this.openPlatform = openPlatform;
        this.appKey = appKey;
        this.callBackUrl = callBackUrl;
        this.state = state;
        this.scope = scope;
        this.appSecret = appSecret;
        this.platform = platform;
        this.shareStatus = shareStatus;
        this.bindStatus = bindStatus;
    }

    public void setPlatform(PLATFORM platform) {
        this.platform = platform;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PLATFORM getPlatform() {
        return platform;
    }

    public String getOpenPlatform() {
        return openPlatform;
    }

    public void setOpenPlatform(String openPlatform) {
        this.openPlatform = openPlatform;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getCallBackUrl() {
        return callBackUrl;
    }

    public void setCallBackUrl(String callBackUrl) {
        this.callBackUrl = callBackUrl;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public long getShareStatus() {
        return shareStatus;
    }

    public void setShareStatus(long shareStatus) {
        this.shareStatus = shareStatus;
    }

    public long getBindStatus() {
        return bindStatus;
    }

    public void setBindStatus(long bindStatus) {
        this.bindStatus = bindStatus;
    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public String getSellerAccount() {
        return sellerAccount;
    }

    public void setSellerAccount(String sellerAccount) {
        this.sellerAccount = sellerAccount;
    }

    public String getPaySecret() {
        return paySecret;
    }

    public void setPaySecret(String paySecret) {
        this.paySecret = paySecret;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getKey() {
        return (platform.name() + "_" + openPlatform + "_" + name).toUpperCase();
    }
}
