package com.sparrow.authenticator;

import java.util.Map;

public interface LoginUser {
    /**
     * 用户ID
     */
    Long getUserId();

    /**
     * 租户ID
     */
    String getTenantId();

    /**
     * 用户类型
     */
    Integer getCategory();

    /**
     * 用户昵称
     */
    String getNickName();

    /**
     * 用户名
     */
    String getUserName();

    /**
     * 头象
     */
    String getAvatar();

    /**
     * 设备类型
     */
    Integer getDeviceType();

    /**
     * 设备id
     */
    String getHost();

    Double getDays();

    Long getExpireAt();

    Map<String, Object> getExtensions();

    boolean isVisitor();
}
