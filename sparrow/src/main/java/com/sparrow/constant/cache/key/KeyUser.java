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

package com.sparrow.constant.cache.key;

import com.sparrow.constant.SparrowModule;
import com.sparrow.constant.cache.KEY;
import com.sparrow.enums.DateTimeUnit;
import com.sparrow.utility.LockConfig;
import java.util.HashMap;
import java.util.Map;

public class KeyUser {
    /**
     * user profile
     */
    public static final KEY.Business USER_SIMPLE_INFO = new KEY.Business(SparrowModule.USER, "ENTITY", "User");
    /**
     * 用户权限码
     */
    public static final KEY.Business PERMISSION = new KEY.Business(SparrowModule.USER, "PERMISSION");

    /**
     * 用户权限码
     */
    public static final KEY.Business BACKEND_PERMISSION = new KEY.Business(SparrowModule.USER, "BACKEND_PERMISSION");

    /**
     * attention list
     */
    public static final KEY.Business LIST_ATTENTION = new KEY.Business(SparrowModule.USER, "LIST", "ATTENTION");
    /**
     * fans list
     */
    public static final KEY.Business LIST_FANS = new KEY.Business(SparrowModule.USER, "LIST", "FANS");
    /**
     * user thread count or comment count limit lock
     */
    public static final KEY.Business LOCK_PUBLISH = new KEY.Business(SparrowModule.USER, "LOCK", "PUBLISH");
    /**
     * user find password lock
     */
    public static final KEY.Business LOCK_FIND_PASSWORD = new KEY.Business(SparrowModule.USER, "LOCK", "FIND", "PASSWORD");
    /**
     * user login times lock
     */
    public static final KEY.Business LOCK_LOGIN = new KEY.Business(SparrowModule.USER, "LOCK", "LOGIN");
    /**
     * user cent lock
     */
    public static final KEY.Business LOCK_LOGIN_CENT = new KEY.Business(SparrowModule.USER, "LOCK", "LOGIN", "CENT");
    /**
     * user register lock
     */
    public static final KEY.Business LOCK_REGISTER = new KEY.Business(SparrowModule.USER, "LOCK", "REGISTER");
    /**
     * user dig lock
     */
    public static final KEY.Business LOCK_DIG = new KEY.Business(SparrowModule.USER, "LOCK", "DIG");
    /**
     * user like lock
     */
    public static final KEY.Business LOCK_LIKE = new KEY.Business(SparrowModule.USER, "LOCK", "LIKE");

    /**
     * user cent sort
     */
    public static final KEY.Business SORT_CENT = new KEY.Business(SparrowModule.USER, "SORT", "CENT");
    /**
     * lastest login and high cent
     */
    public static final KEY.Business SORT_POPULARITY = new KEY.Business(SparrowModule.USER, "SORT", "POPULARITY");

    public static final Map<KEY.Business, LockConfig> LOCK_CONFIG = new HashMap<KEY.Business, LockConfig>();

    static {
        // 5分钟不能超10次 不顺延
        LOCK_CONFIG.put(LOCK_PUBLISH, LockConfig.getRelativeLock(5 * 60, 10, false, false));
        // 30分钟之内不超过5次
        LOCK_CONFIG.put(LOCK_LOGIN, LockConfig.getRelativeLock(30 * 60, 5,
            false, false));
        // 24小时内登录一次加一次积分
        LOCK_CONFIG.put(LOCK_LOGIN_CENT, LockConfig.getRelativeLock(24 * 60 * 60, 1, false, false));
        // 30分钟之内不超过5次
        LOCK_CONFIG.put(LOCK_FIND_PASSWORD, LockConfig.getRelativeLock(30 * 60, 5, false, false));
        // 12小时内不超过20次
        LOCK_CONFIG.put(LOCK_REGISTER, LockConfig.getRelativeLock(12 * 60 * 60, 20, false, false));
        // 1天内只允许1 次
        LOCK_CONFIG.put(LOCK_DIG, LockConfig.getAbsoluteLock(DateTimeUnit.DAY, 1));
        // 1天只允许1次
        LOCK_CONFIG.put(LOCK_LIKE, LockConfig.getAbsoluteLock(DateTimeUnit.DAY, 1));
    }
}
