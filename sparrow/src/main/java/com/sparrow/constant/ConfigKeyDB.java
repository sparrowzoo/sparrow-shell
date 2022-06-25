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
    public static final String PAGE_SIZE = "PAGE-SIZE";

    public static final String PAGESIZE_THREAD = "PAGESIZE-THREAD";

    public static final String PAGESIZE_COMMENT = "PAGESIZE-COMMENT";
    /**
     * 数据库空 数值型的默认值
     */
    public static final String DEFAULT_VALUE = "0";

    /**
     * ORM 主键的唯一性索引由0开始
     */
    public static final String ORM_PRIMARY_KEY_UNIQUE = "PRIMARY";

    /**
     * int 型函数数据库访问错误 或者是记录不存在
     */
    public static final long ERROR_INT = -1L;
    /**
     * String 型函数数据库访问错误
     */
    public static final String ERROR = "-1";

    public static final String WEBSITE_CONFIG_PARENT = "WEBSITE_CONFIG";

    public static final class WebsiteConfig {
        public static final String TITLE = "Title";
        public static final String DESCRIPTION = "Description";
        public static final String BANNER = "Banner";
        public static final String ICP = "ICP";
        public static final String BANNER_FLASH = "BannerFlash";
        public static final String KEYWORDS = "Keywords";
        public static final String LOGO = "Logo";
        public static final String CONTACT = "Contact";
    }

    public static final String STRATAGEM_SYSTEM = "S-SYS";
    public static final String STRAEAGEM_LEVEL = "S-LEL";

    public static final String TAG_FRONT_COLOR = "TAG-FRONT-COLOR";
    public static final String TAG_BACKGROUND_COLOR = "TAG-BACKGROUND-COLOR";
    public static final String TAG_FONT_SIZE = "TAG-FONT-SIZE";

    public static final String USER_IS_ACTIVE_LOGIN = "User-IS_ACTIVE-LOGIN";
    public static final String USER_IS_VISIBLE_NOT_AUDITED = "User-IS-VISIBLE-NOT-AUDITED";
    public static final String USER_CENT_LOGIN = "User-CENT-LOGIN";
    public static final String USER_CENT_REGISTER = "User-CENT-REGISTER";
    public static final String USER_CENT_ACTIVE = "User-CENT-ACTIVE";
    public static final String USER_CENT_THREAD = "User-CENT-THREAD";
    public static final String USER_CENT_COMMENT = "User-CENT-COMMENT";

    public static final String USER_EMAIL_LOGIN_URL = "User-EMAIL-LOGIN-URL";
    public static final String EDITOR = "User-EDITOR";

    public static final String USER_LOGIN_REMEMBER_DAYS = "LOGIN_REMEMBER_DAYS";
}
