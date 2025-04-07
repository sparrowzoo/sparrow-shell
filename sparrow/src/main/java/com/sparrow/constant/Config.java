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
    public static final String DOWNLOAD = "download";
    public static final String DOWNLOAD_PHYSICAL_PATH = "physical_download";
    public static final String LOGIN_URL = "login_url";
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
    public static final String DATA_SOURCE_PASSWORD_KEY = "mysql_sparrow_password";

    public static final String DEBUG_DATA_SOURCE_PASSWORD_KEY = "debug_data_source_password";

    public static final String TEMPLATE_ENGINE_PREFIX = "template_engine_prefix";
    public static final String TEMPLATE_ENGINE_SUFFIX = "template_engine_suffix";

    public static final String RESOURCE_VERSION = "resource_version";
    public static final String WATER_MARK = "watermark";
    public static final String ERROR_URL = "error_url";
    public static final String TRANSIT_URL = "transit_url";
    public static final String SUCCESS_TRANSIT_URL = "success_transit_url";
    public static final String EXCEPTION_SWITCH_MODE = "exception_switch_mode";

    public static final String SUPPORT_TEMPLATE_ENGINE = "support_template_engine";

    public static final String AJAX_PATTERNS = "ajax_patterns";

    public static final String AUTO_MAPPING_VIEW_NAMES = "auto_mapping_view_names";

    public static final String EMAIL_HOST = "email_host";
    public static final String EMAIL_FROM = "email_from";
    public static final String EMAIL_USERNAME = "email_username";
    public static final String EMAIL_PASSWORD = "email_password";
    public static final String EMAIL_LOCAL_ADDRESS = "email_local_address";
    public static final String METHOD_ACCESS_DEBUG = "method_access_debug";
}
