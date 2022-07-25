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
import com.sparrow.constant.cache.KEY;
import com.sparrow.constant.cache.key.KeyUser;
import com.sparrow.utility.ConfigUtility;
import com.sparrow.utility.JSUtility;
import com.sparrow.utility.StringUtility;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CookieUtility {
    private static Logger logger = LoggerFactory.getLogger(CookieUtility.class);

    private KEY.Business permissionBusiness = KeyUser.PERMISSION;

    public void set(HttpServletResponse response, String key,
        String value, int days) {
        set(response, key, value, days, null);
    }

    public void setRoot(HttpServletResponse response, String key,
        String value, int days) {
        String domain = ConfigUtility.getValue(Config.ROOT_DOMAIN);
        set(response, key, value, days, domain);
    }

    public void set(HttpServletResponse response, String key,
        String value, int days, String domain) {
        if (StringUtility.isNullOrEmpty(domain)) {
            domain = ConfigUtility.getValue(Config.DOMAIN);
        }
        Cookie cookie = new Cookie(key, JSUtility.encodeURIComponent(value));
        if (domain != null) {
            cookie.setDomain(domain);
        } else {
            logger.warn("please config [domain] key in sparrow system config ");
        }
        cookie.setPath("/");
        if (days > 0) {
            cookie.setMaxAge(days * 24 * 60 * 60);
        }
        response.addCookie(cookie);
    }

    public String get(Cookie[] cookies, String key) {
        logger.debug("get cookie {}", key);
        if (cookies == null || cookies.length == 0) {
            return null;
        } else {
            for (Cookie cookie : cookies) {
                logger.debug("cookie {} value {}", cookie.getName(), cookie.getValue());
                if (cookie.getName().equalsIgnoreCase(key)) {
                    return JSUtility.decodeURIComponent(cookie.getValue());
                }
            }
        }
        return null;
    }

    public String getPermission(HttpServletRequest request) {
        return this.get(request.getCookies(), permissionBusiness.getKey());
    }
}
