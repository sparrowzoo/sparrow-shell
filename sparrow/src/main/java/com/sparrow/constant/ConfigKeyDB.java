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

public class ConfigKeyDB {
    /**
     * 每页记录数
     */
    public static final String PAGE_SIZE = "PAGE_SIZE";

    public static final String PAGE_SIZE_THREAD = "PAGE_SIZE.THREAD";

    public static final String PAGE_SIZE_COMMENT = "PAGE_SIZE.COMMENT";

    /**
     * ORM 主键的唯一性索引由0开始
     */
    public static final String ORM_PRIMARY_KEY_UNIQUE = "PRIMARY";
    public static final String STRATAGEM_SYSTEM = "S.SYS";
    public static final String STRATAGEM_LEVEL = "S.LEL";

    public static final String TAG_FRONT_COLOR = "TAG.FRONT_COLOR";
    public static final String TAG_BACKGROUND_COLOR = "TAG.BACKGROUND_COLOR";
    public static final String TAG_FONT_SIZE = "TAG.FONT_SIZE";

    public static final String USER_IS_ACTIVE_LOGIN = "USER.IS_ACTIVE_LOGIN";
    public static final String USER_IS_VISIBLE_NOT_AUDITED = "USER.IS_VISIBLE_NOT_AUDITED";
    public static final String USER_CENT_LOGIN = "USER.CENT_LOGIN";
    public static final String USER_CENT_REGISTER = "USER.CENT_REGISTER";
    public static final String USER_CENT_ACTIVE = "USER.CENT_ACTIVE";
    public static final String USER_CENT_THREAD = "USER.CENT_THREAD";
    public static final String USER_CENT_COMMENT = "USER.CENT_COMMENT";

    public static final String USER_EMAIL_LOGIN_URL = "USER.EMAIL_LOGIN_URL";
    public static final String EDITOR = "USER.EDITOR";

    public static final String USER_LOGIN_REMEMBER_DAYS = "LOGIN.REMEMBER_DAYS";
}
