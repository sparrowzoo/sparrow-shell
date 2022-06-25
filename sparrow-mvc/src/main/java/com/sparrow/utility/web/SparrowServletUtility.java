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

package com.sparrow.utility.web;

import com.sparrow.protocol.constant.Constant;
import com.sparrow.core.Pair;
import com.sparrow.support.web.HttpContext;
import com.sparrow.support.web.ServletUtility;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class SparrowServletUtility {
    private static final SparrowServletUtility INSTANCE = new SparrowServletUtility();

    private ServletUtility servletUtility = ServletUtility.getInstance();

    public ServletUtility getServletUtility() {
        return servletUtility;
    }

    public static SparrowServletUtility getInstance() {
        return INSTANCE;
    }

    public void moveAttribute(ServletRequest request) {
        Map<String, Object> map = HttpContext.getContext().getHolder();
        for (String key : map.keySet()) {
            Object value = map.get(key);
            request.setAttribute(key, value);
        }
        HttpContext.getContext().remove();
    }

    public void flash(HttpServletRequest request, String sourceUrl) {
        Map<String, Object> values = HttpContext.getContext().getHolder();
        Pair<String, Map<String, Object>> sessionMap = Pair.create(sourceUrl, values);
        request.getSession().setAttribute(Constant.FLASH_KEY, sessionMap);
        HttpContext.getContext().remove();
    }
}
