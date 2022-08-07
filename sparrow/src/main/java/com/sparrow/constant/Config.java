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
    public static final String LANGUAGE = "language";
    public static final String ROOT_PATH = "root_path";
    public static final String FRONTEND_ROOT_PATH = "frontend_root_path";
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
    public static final String DEFAULT_FORUM_ICO = "default_forum_ico";
    public static final String DEFAULT_AVATAR = "default_avatar";
    public static final String DEFAULT_BLOG_URL = "default_blog_url";
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

    /**
     * 用户中心URL格式
     */
    public static final String USER_CENTER_URL_FORMAT = "user_center_url_format";
    public static final String ERROR_URL = "error_url";
    public static final String TRANSIT_URL = "transit_url";
    public static final String SUCCESS_TRANSIT_URL = "success_transit_url";
    public static final String EXCEPTION_SWITCH_MODE = "exception_switch_mode";

    public static final String EMAIL_HOST = "email_host";
    public static final String EMAIL_FROM = "email_from";
    public static final String EMAIL_USERNAME = "email_username";
    public static final String EMAIL_PASSWORD = "email_password";
    public static final String EMAIL_LOCAL_ADDRESS = "email_local_address";

    public static final String MOBILE_KEY = "mobile_key";
    public static final String MOBILE_TEMPLATE_ID = "mobile_template_id";
    public static final String MOBILE_SECRET_3DAS_KEY = "mobile_secret_3das_key";
    public static final String MOBILE_VALIDATE_TOKEN_AVAILABLE_TIME = "mobile_validate_token_available_time";

    public static final String WECHAT_LOTTERY_3DES_KEY = "wechat_lottery_3das_key";
    /**
     * 提供给日志
     */
    public static final String WEB_SERVER_PACKAGE_PREFIX = "web_server_package_prefix";

    /**
     * 激活码的有效期
     */
    public static final String VALIDATE_TOKEN_AVAILABLE_DAY = "validate_token_available_day";
    public static final String DEBUG = "debug";
    public static final String DEBUG_METHOD_ACCESS = "debug_method_access";
    public static final String DB_POOL_NAME_ENUM = "db_pool_name_enum";
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

    public static final String LUCENCE_INDEX_PATH_FOR_SEARCH = "lucence_index_path_for_search";
    public static final String LUCENCE_DISABLE_KEYWORDS_PATH = "lucence_disable_keywords_path";
    public static final String LUCENCE_ENABLE_KEYWORDS_PATH = "lucence_enable_keywords_path";
    public static final String LUCENCE_IDF_KEYWORDS_PATH = "lucence_idf_keywords_path";

    public static final String CMS_PAGE_CHAR_COUNT = "cms_page_char_count";

    public static final String FORUM_CODE_CMS = "forum_code_cms";
    public static final String FORUM_CODE_CMS_HYPER = "forum_code_cms_hyper";
    public static final String FORUM_CODE_CMS_TOP_MENU_HYPER = "forum_code_cms_top_menu_hyper";
    public static final String FORUM_CODE_CMS_INDEX_FRIEND_HYPER_LINK = "forum_code_cms_index_friend_hyper_link";
    public static final String FORUM_CODE_BLOG = "forum_code_blog";
    public static final String FORUM_CODE_DEFAULT = "forum_code_default";
    public static final String FORUM_CODE_LEFT_AD = "forum_code_left_ad";
    public static final String FORUM_CODE_INDEX_PIC = "forum_code_index_pic";
    public static final String FORUM_CODE_CMS_ONLINE_TALK = "forum_code_cms_online_talk";

    public static final String TAG_LINK_FORMAT = "tag_link_format";
    public static final String COMPILER_OPTION_ENCODING = "compiler_option_encoding";

    /**
     * 应用名称 网站名称
     */
    public static final String PLATFORM_NAME = "name";
    /**
     * app key
     */
    public static final String PLATFORM_APP_KEY = "app_key";
    /**
     * 回调的url
     */
    public static final String PLATFORM_CALL_BACK_URL = "call_back_url";
    /**
     * 状态
     */
    public static final String PLATFORM_STATE = "state";
    /**
     * 允许调用的方法
     */
    public static final String PLATFORM_SCOPE = "scope";
    /**
     * 密钥
     */
    public static final String PLATFORM_APP_SECRET = "app_secret";

    /**
     * 卖家商号
     */
    public static final String PLATFORM_PARTNER = "partner";

    /**
     * 卖家帐号
     */
    public static final String PLATFORM_SELLER_ACCOUNT = "seller_account";

    /**
     * 支付密钥
     */
    public static final String PLATFORM_PAY_SECRET = "pay_secret";

    /**
     * 通知url
     */
    public static final String PLATFORM_NOTIFY_URL = "notify_url";

    public static final String PLATFORM_CHARSET = "charset";

    public static final String ACCESS_TOKEN_CONFIG_PATH = "access_token_config_path";
}
