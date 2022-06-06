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

package com.sparrow.protocol.constant;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 常用常量
 *
 * @author harry
 * @version 1.0
 */
public class CONSTANT {
    public static final String SPARROW = "sparrow";
    public static final String DEFAULT = "default";
    public static final String ERROR = "error";
    public static final String SUCCESS = "success";
    // ***************************COMMON*********************************************//
    /**
     * 用于保存空json串;如:jsonField:jsonValue 如果jsonValue也是一个json则空串返回该常量定义
     */
    public static final String WORKSPACE = "workspace";
    public static final String NULL_JSON = "''";
    public static final String ENTER_TEXT = "\r\n";
    public static final String ENTER_TEXT_N = "\n";
    public static final String ERROR_CSS_CLASS = "error";
    public static final String VALIDATE_CODE = "validate_code";
    public static final String IMAGE_EXTENSION = ".jpg|.jpeg|.gif|.png";

    public static final String CHARSET_UTF_8 = "UTF-8";
    public static final String CHARSET_GBK = "GBK";
    public static final String CHARSET_ISO_8859_1 = "ISO-8859-1";

    public static final String CONTENT_TYPE_FORM = "application/x-www-form-urlencoded";
    public static final String CONTENT_TYPE_MS_DOWNLOAD = "application/x-msdownload";

    public static final String CONTENT_TYPE_BYTES = "application/octet-stream";

    public static final String CONTENT_TYPE_TEXT_PLAIN = "text/plain";

    public static final String CONTENT_TYPE_SERIALIZED_OBJECT = "application/x-java-serialized-object";

    public static final String CONTENT_TYPE_JSON = "application/json";

    public static final String CONTENT_TYPE_JSON_ALT = "text/x-json";

    public static final String CONTENT_TYPE_XML = "application/xml";

    public static final String TABLE_SUFFIX = "$suffix";

    public static final String IMAGE_TEMP_MARK = "#image_temp_mark%1$s#";

    public static final String IMAGE_MARKDOWN_MARK_FORMAT = "![%1$s](%2$s)";

    public static final String IMAGE_HTML_MARK_FORMAT = "<img src=\"%1$s\"/>";
    /**
     * 访问被拒绝
     */
    public static final String ACCESS_DENIED = "Access Denied";

    public static final String DEFAULT_LANGUAGE = "zh_cn";
    public static final String RESULT_OK_CODE = "0";
    public static final String MESSAGE_KEY_PREFIX = "mk_";

    /**
     * flash key非业务key
     */
    public static final String FLASH_KEY = "flash";
    public static final String FLASH_EXCEPTION_RESULT = "flash_exception_result";
    public static final String FLASH_SUCCESS_RESULT = "flash_success_result";

    public static final String REQUEST_LANGUAGE = "sparrow_request_language";
    public static final String REQUEST_USER_ID = "sparrow_request_user_id";
    public static final String REQUEST_DATABASE_SUFFIX = "sparrow_request_database_suffix";
    public static final String REQUEST_ACTION_INCLUDE = "javax.servlet.include.servlet_path";
    public static final String REQUEST_ACTION_CURRENT_FORUM = "request_action_current_forum";
    public static final String REQUEST_CLIENT_INFORMATION = "client";


    public static final String STRING_ALL = "all";
    /**
     * 空链接
     */
    public static final String NULL_LINK = "javascript:void(0);";
    public static final String LOCALHOST = "http://localhost";
    public static final String LOCALHOST_127 = "http://127.0.0.1";
    public static final String HTTP_PROTOCOL = "http://";
    public static final String HTTPS_PROTOCOL = "https://";
    public static final String LOCALHOST_IP = "127.0.0.1";



    /**
     * 用于宏替换的常量
     */
    public static final Map<String, String> REPLACE_MAP = new ConcurrentHashMap<String, String>();

}
