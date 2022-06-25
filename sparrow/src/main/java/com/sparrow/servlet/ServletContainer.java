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

package com.sparrow.servlet;

import com.sparrow.exception.CacheNotFoundException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.Map;

public interface ServletContainer {

    HttpServletRequest getRequest();

    HttpServletResponse getResponse();

    String getActionKey() throws CacheNotFoundException;

    String getClientIp();

    void clearClientCache();

    void download(InputStream inputStream, String fileName);

    void download(InputStream inputStream, String fileName, String contentType);

    void flash(String key, Object value);

    <T> T flash(String key);

    <T> T removeFlash(String key);

    void clear();

    <T> T get(String key);

//    <T> void put(String key,T value);

    String referer();

    String queryString();

    Cookie[] cookies();

    void cookie(String key, String value, Integer expireDays) throws CacheNotFoundException;

    void rootCookie(String key, String value, Integer expireDays) throws CacheNotFoundException;

    Map<String, String> getParameters();

    String getRequestBody();
}
