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

package com.sparrow.enums;

public enum LoginType {
    /**
     * 0不需求登录
     */
    NO_AUTHENTICATE,
    /**
     * 1正常网站登录
     */
    LOGIN,
    /**
     * 2框架内登录 default.jsp 内登录
     */
    LOGIN_IFRAME,
    /**
     * 3管理员登录
     */
    ADMINISTRATOR_LOGIN,
    /**
     * 4对话框登录(对话框不允许在框架内，如果在框架内则重定向至父页面)
     */
    DIALOG_LOGIN,
    /**
     * 直接打印错误信息
     */
    MESSAGE
}
