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

package com.sparrow.support.web;

import com.sparrow.constant.Config;
import com.sparrow.protocol.constant.ClientInformation;
import com.sparrow.protocol.constant.Constant;
import com.sparrow.protocol.constant.Extension;
import com.sparrow.protocol.constant.magic.Symbol;
import com.sparrow.utility.ConfigUtility;
import com.sparrow.utility.StringUtility;
import java.util.Enumeration;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

public class ServletUtility {

    private static final ServletUtility INSTANCE = new ServletUtility();

    private ServletUtility() {
    }

    public static ServletUtility getInstance() {
        return INSTANCE;
    }

    public boolean include(ServletRequest request) {
        return request
            .getAttribute(Constant.REQUEST_ACTION_INCLUDE) != null;
    }

    public String assembleActualUrl(String url) {
        //rootPath is null when contain init ...
        String rootPath = ConfigUtility.getValue(Config.ROOT_PATH);
        if (rootPath != null && url.startsWith(rootPath)) {
            url = url.substring(rootPath.length());
        }
        if (!url.startsWith(Symbol.SLASH)) {
            url = Symbol.SLASH + url;
        }
        String extension = ConfigUtility.getValue(Config.DEFAULT_PAGE_EXTENSION, Extension.JSP);
        String pagePrefix = ConfigUtility.getValue(Config.DEFAULT_PAGE_PREFIX, "/template");
        if (!url.endsWith(extension)) {
            url = url + extension;
        }
        if (!url.startsWith(pagePrefix)) {
            url = pagePrefix + url;
        }
        return url;
    }

    public String getActionKey(ServletRequest request) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        Object servletPath = request
            .getAttribute(Constant.REQUEST_ACTION_INCLUDE);
        String actionKey;
        if (servletPath != null) {
            actionKey = servletPath.toString();
        } else {
            actionKey = httpServletRequest.getServletPath();
        }

        String rootPath = ConfigUtility.getValue(Config.ROOT_PATH);
        if (!StringUtility.isNullOrEmpty(rootPath)) {
            return actionKey;
        }

        //第一次请求时初始化rootPath website  和domain
        String serverName = request.getServerName();
        String path = httpServletRequest.getContextPath();
        rootPath = request.getScheme()
            + "://"
            + serverName
            + (request.getServerPort() == 80 ? "" : ":"
            + request.getServerPort()) + path;
        // eclipse tomcat 启动时会默认请求http://localhost故此处加此判断
        //只解析一二级域名 http://www.sparrowzoo.com
        if (rootPath.indexOf(Constant.LOCALHOST) != 0 && rootPath.indexOf(Constant.LOCALHOST_127) != 0) {
            String website = serverName.substring(serverName.indexOf(".") + 1);
            website = website.substring(0, website.indexOf("."));
            ConfigUtility.resetKey(Config.WEBSITE, website);

            String rootDomain = ConfigUtility.getValue(Config.ROOT_DOMAIN);
            if (rootDomain == null) {
                ConfigUtility.resetKey(Config.ROOT_DOMAIN, serverName.substring(serverName.indexOf(".")));
            }

            String domain = ConfigUtility.getValue(Config.DOMAIN);
            if (domain == null) {
                ConfigUtility.resetKey(Config.DOMAIN,
                    serverName);
            }
            Constant.REPLACE_MAP.put("$website", website);
        }
        ConfigUtility.resetKey(Config.ROOT_PATH, rootPath);
        return actionKey;
    }

    public String getClientIp(ServletRequest request) {
        if (request == null) {
            return Constant.LOCALHOST;
        }
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String ip = httpRequest.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = httpRequest.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = httpRequest.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip.indexOf(Symbol.COLON) > 0) {
            ip = ip.split(Symbol.COLON)[0];
        }
        return ip;
    }

    public String getDeviceId(HttpServletRequest request) {
        String deviceId = request.getHeader(ClientInformation.DEVICE_ID);
        if (StringUtility.isNullOrEmpty(deviceId)) {
            deviceId = ServletUtility.getInstance().getClientIp(request);
        }
        return deviceId;
    }

    public String getAllParameter(ServletRequest request) {
        StringBuilder sb = new StringBuilder();
        Enumeration<?> enumeration = request.getParameterNames();
        while (enumeration.hasMoreElements()) {
            String key = (String) enumeration.nextElement();
            String value = request.getParameter(key);
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append(key);
            sb.append(":");
            sb.append(value);
        }
        return sb.toString();
    }

    public String referer(HttpServletRequest request) {
        return request.getHeader("Referer");
    }
}
