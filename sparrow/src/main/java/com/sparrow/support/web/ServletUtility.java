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

import com.sparrow.core.spi.ApplicationContext;
import com.sparrow.protocol.constant.ClientInfoConstant;
import com.sparrow.protocol.constant.Constant;
import com.sparrow.protocol.constant.magic.Symbol;
import com.sparrow.utility.CollectionsUtility;
import com.sparrow.utility.RegexUtility;
import com.sparrow.utility.StringUtility;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

public class ServletUtility {

    private static final ServletUtility INSTANCE = new ServletUtility();

    public ServletUtility() {
    }

    public static ServletUtility getInstance() {
        return INSTANCE;
    }

    public boolean include(ServletRequest request) {
        return request
                .getAttribute(Constant.REQUEST_ACTION_INCLUDE) != null;
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
        if (ip == null) {
            return "";
        }
        if (ip.contains(",")) {
            ip = ip.substring(0, ip.indexOf(","));
        }
        if (ip.indexOf(Symbol.COLON) > 0) {
            ip = ip.split(Symbol.COLON)[0];
        }
        return ip;
    }

    public String getDeviceId(HttpServletRequest request) {
        String deviceId = request.getHeader(ClientInfoConstant.DEVICE_ID);
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

    public boolean isAjax(HttpServletRequest request, boolean supportTemplate, List<String> ajaxPattens) {
        //非模板引擎，直接返回json
        if (!supportTemplate) {
            return true;
        }
        //支持模板引擎，判断URL前缀，如果是api前缀，则返回json
        if (!CollectionsUtility.isNullOrEmpty(ajaxPattens)) {
            String uri = request.getRequestURI();
            return RegexUtility.matchPatterns(ajaxPattens, uri);
        }
        //如果请求参数中明确指定了isAjax，则返回json
        String ajax = request.getHeader(Constant.IS_AJAX);
        if (!StringUtility.isNullOrEmpty(ajax)) {
            return "true".equalsIgnoreCase(ajax);
        }
        return false;
    }

    public void moveAttribute(ServletRequest request) {
        Map<String, Object> map = HttpContext.getContext().getHolder();
        for (String key : map.keySet()) {
            Object value = map.get(key);
            request.setAttribute(key, value);
        }
        HttpContext.getContext().remove();
    }

    public String assembleActualUrl(String url) {

        if (url.contains("?")) {
            url = url.substring(0, url.indexOf("?"));
        }

        WebConfigReader configReader = ApplicationContext.getContainer().getBean(WebConfigReader.class);
        String rootPath = configReader.getRootPath();
        if (rootPath != null && url.startsWith(rootPath)) {
            url = url.substring(rootPath.length());
        }
        if (!url.startsWith(Symbol.SLASH)) {
            url = Symbol.SLASH + url;
        }
        String extension = configReader.getTemplateEngineSuffix();
        String pagePrefix = configReader.getTemplateEnginePrefix();
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
        return actionKey;
    }
}
