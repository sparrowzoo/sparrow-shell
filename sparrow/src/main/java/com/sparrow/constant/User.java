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

package com.sparrow.constant;

public class User {
    public static final String PERMISSION = "PERMISSION";
    /**
     * 游客ID
     */
    public static final Long VISITOR_ID = 0L;
    /**
     * 系统管理员
     */
    public static final String ADMIN = "admin";
    /**
     * 用户ID
     */
    public static final String ID = "user_id";
    /**
     * 用户登录帐号
     */
    public static final String LOGIN_TOKEN = "login_token";

    /**
     * 用户状态
     */
    public static final String STATUS = "user_status";
    /**
     * 内部用户
     */
    public static final String INNER_TYPE = "user_type_inner";

    /**
     * 在线用户列表
     */
    public static final String ONLINE_USER_LIST = "online_user_list";

    /**
     * 用户密码sha1加密密钥
     */
    public static final String PASSWORD_SHA1_SECRET_KEY = "password_secret_sha1_key";
    /**
     * 用户密码3das加密密钥
     */
    public static final String PASSWORD_3DAS_SECRET_KEY = "password_secret_3das_key";

    /**
     * 开放平台加密密钥
     */
    public static final String OAUTH_3DAS_SECRET_KEY = "oauth_3das_secret_key";
}
