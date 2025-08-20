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
package com.sparrow.authenticator;

import com.sparrow.protocol.DTO;
import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;


@Data
public class DefaultLoginUser implements LoginUser, DTO {
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
     * 租户ID
     */
    private String tenantId;

    /**
     * 用户类型
     */
    private Integer category = CATEGORY_REGISTER;
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
     * 设备类型
     */
    private Integer deviceType;
    /**
     * 设备id
     */
    private String host;

    private Double days;

    private Long expireAt;

    private Map<String, Object> extensions = new LinkedHashMap<>();

    public static DefaultLoginUser create(Long userId,
                                          String tenantId,
                                          Integer category,
                                          String userName,
                                          String nickName,
                                          String avatar,
                                          Integer deviceType,
                                          String host,
                                          Double days) {
        DefaultLoginUser login = new DefaultLoginUser();
        login.tenantId = tenantId;
        login.userId = userId;
        login.category = category;
        login.userName = userName;
        login.nickName = nickName;
        login.avatar = avatar;
        login.days = days;
        double t = days * 24 * 60 * 60 * 1000;
        login.expireAt = System.currentTimeMillis() + (long) t;
        login.deviceType = deviceType;
        login.host = host;
        return login;
    }

    public boolean isVisitor() {
        return VISITOR_ID.equals(this.userId) || CATEGORY_VISITOR == this.category;
    }
}
