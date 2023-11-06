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

import com.sparrow.enums.LoginType;

import java.util.HashMap;
import java.util.Map;

public class Config {
    public static final String INTERNATIONALIZATION = "internationalization";
    public static final String REQUEST_HEADER_KEY_LOGIN_TOKEN = "request_header_key_login_token";
    public static final String LANGUAGE = "language";
    public static final String ROOT_PATH = "root_path";
    public static final String MVC_CONFIG = "mvc_config";
    public static final String REDIS_HOST = "redis_host";
    public static final String REDIS_PORT = "redis_port";
    /**
     * 资源文件所在域名
     */
    public static final String RESOURCE = "resource";
    /**
     * 上传文件的域名
     */
    public static final String UPLOAD = "upload";
    public static final String UPLOAD_PHYSICAL_PATH = "physical_upload";
    /**
     * 图片格式
     */
    public static final String IMAGE_EXTENSION = "image_extension";
    public static final String THEMES = "themes";
    public static final String RESOURCE_PHYSICAL_PATH = "physical_resource";
    public static final String TEMP = "temp";

    /**
     * 默认前台欢迎url http://www.sparrowzoo.com 在passport.sparrowzoo.com 中配置
     */
    public static final String DEFAULT_WELCOME_INDEX = "default_welcome_index";
    /**
     * 后台管理系统首页url http://admin.sparrowzoo.com 在admin.sparrowzoo.com中配置
     */
    public static final String DEFAULT_ADMIN_INDEX = "default_admin_index";
    public static final String DEFAULT_AVATAR = "default_avatar";
    public static final String DEFAULT_DATA_SOURCE_KEY = "default_data_source";
    public static final String DEFAULT_USER_PASSWORD = "default_user_password";
    public static final String DEFAULT_PAGE_EXTENSION = "default_page_extension";
    public static final String DEFAULT_PAGE_PREFIX = "default_page_prefix";

    public static final String WEBSITE = "website";
    public static final String IMAGE_WEBSITE = "image_website";
    public static final String DOMAIN = "domain";
    public static final String ROOT_DOMAIN = "root_domain";
    public static final String RESOURCE_VERSION = "resource_version";
    public static final String WATER_MARK = "watermark";
    public static final String ERROR_URL = "error_url";
    public static final String TRANSIT_URL = "transit_url";
    public static final String SUCCESS_TRANSIT_URL = "success_transit_url";
    public static final String EXCEPTION_SWITCH_MODE = "exception_switch_mode";

    public static final String EMAIL_HOST = "email_host";
    public static final String EMAIL_FROM = "email_from";
    public static final String EMAIL_USERNAME = "email_username";
    public static final String EMAIL_PASSWORD = "email_password";

    public static final String EMAIL_LOCAL_ADDRESS = "email_local_address";
    /**
     * 提供给日志
     */
    public static final String WEB_SERVER_PACKAGE_PREFIX = "web_server_package_prefix";

    /**
     * 激活码的有效期
     */
    public static final String VALIDATE_TOKEN_AVAILABLE_DAY = "validate_token_available_day";

    public static final String DEBUG = "debug";
    public static final String METHOD_ACCESS_DEBUG = "method_access_debug";
    public static final String METHOD_ACCESS_CLASS = "method_access_class";
    public static final String LOG_CLASS = "log_class";
    public static final String LOG_LEVEL = "log_level";
    public static final String LOG_PRINT_CONSOLE = "print_console";

    public static final Map<LoginType, String> LOGIN_TYPE_KEY = new HashMap<LoginType, String>() {
        private static final long serialVersionUID = 1L;

        {
            put(LoginType.LOGIN, "login_url");
            put(LoginType.LOGIN_IFRAME, "login_url");
            put(LoginType.DIALOG_LOGIN, "dialog_login_url");
        }
    };
    public static final String COMPILER_OPTION_ENCODING = "compiler_option_encoding";
}
