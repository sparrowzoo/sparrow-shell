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

package com.sparrow.servlet.impl;

import com.sparrow.protocol.constant.magic.Symbol;
import com.sparrow.servlet.ServletContainer;
import com.sparrow.support.web.CookieUtility;
import com.sparrow.support.web.ServletUtility;
import com.sparrow.utility.FileUtility;
import com.sparrow.utility.StringUtility;

import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Map;
import java.util.TreeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractServletContainer implements ServletContainer {
    private static Logger logger = LoggerFactory.getLogger(AbstractServletContainer.class);
    private ServletUtility servletUtility = ServletUtility.getInstance();
    @Inject
    private CookieUtility cookieUtility;

    public void setCookieUtility(CookieUtility cookieUtility) {
        this.cookieUtility = cookieUtility;
    }

    @Override
    public String getActionKey() {
        return this.servletUtility.getActionKey(this.getRequest());
    }

    @Override
    public String getClientIp() {
        return this.servletUtility.getClientIp(this.getRequest());
    }

    @Override
    public void clearClientCache() {
        this.getResponse().setHeader("Pragma", "No-cache");
        this.getResponse().setHeader("Cache-Control", "no-cache");
        this.getResponse().setDateHeader("Expires", 0L);
    }

    @Override
    public void download(InputStream inputStream, String fileName) {
        this.download(inputStream, fileName, (String) null);
    }

    @Override
    public void download(InputStream inputStream, String fileName, String contentType) {
        if (inputStream == null) {
            return;
        }
        HttpServletResponse response = this.getResponse();
        response.reset();
        response.setCharacterEncoding("UTF-8");
        if (StringUtility.isNullOrEmpty(contentType)) {
            response.setContentType("application/x-msdownload");
        } else {
            response.setContentType(contentType);
        }
        response.addHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");

        try {
            FileUtility.getInstance().copy(inputStream, response.getOutputStream());
        } catch (IOException ignore) {
        }
    }

    @Override
    public String referer() {
        return this.servletUtility.referer(this.getRequest());
    }

    @Override
    public String queryString() {
        return this.getRequest().getQueryString();
    }

    @Override
    public Cookie[] cookies() {
        return this.getRequest().getCookies();
    }

    @Override
    public void cookie(String key, String value, Integer expireDays) {
        cookieUtility.set(this.getResponse(), key, value, expireDays);
    }

    @Override
    public void rootCookie(String key, String value, Integer expireDays) {
        cookieUtility.setRoot(this.getResponse(), key, value, expireDays);
    }

    @Override
    public Map<String, String> getParameters() {
        Enumeration enumeration = this.getRequest().getParameterNames();
        TreeMap parameters = new TreeMap();

        while (enumeration.hasMoreElements()) {
            String key = (String) enumeration.nextElement();
            parameters.put(key, this.getRequest().getParameter(key));
        }

        return parameters;
    }


    @Override
    public void flash(String key, Object value) {
        this.getRequest().getSession()
            .setAttribute(key, value);
    }

    @Override
    public <T> T flash(String key) {
        return (T) this.getRequest().getSession()
            .getAttribute(key);
    }

    @Override
    public <T> T removeFlash(String key) {
        T t = (T) this.getRequest().getSession()
            .getAttribute(key);
        this.getRequest().getSession().removeAttribute(key);
        return t;
    }

    @Override
    public String getRequestBody() {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(this.getRequest().getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (Exception e) {
            logger.error("request body error", e);
            return Symbol.EMPTY;
        }
    }
}
