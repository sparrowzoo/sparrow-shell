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

package com.sparrow.authenticator.filter;

import com.sparrow.protocol.constant.Constant;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedHashSet;
import java.util.Set;

public abstract class AbstractAuthcFilter implements Filter {
    public static final int ORDER = 2;
    protected Set<String> excludePatternList = new LinkedHashSet<>();
    protected Boolean supportTemplate;
    protected Set<String> ajaxPatternList = new LinkedHashSet<>();
    protected String tokenKey = Constant.REQUEST_HEADER_KEY_LOGIN_TOKEN;

    @Override
    public void init(FilterConfig config) throws ServletException {
        String excludePatterns = config.getInitParameter("excludePatterns");
        if (isEmpty(excludePatterns)) {
            return;
        }
        if (this.excludePatternList == null) {
            this.excludePatternList = new LinkedHashSet<>();
        }
        String[] patterns = excludePatterns.split(",");
        for (String pattern : patterns) {
            pattern = pattern.replace("*", ".*");
            this.excludePatternList.add(pattern);
        }
    }

    public Boolean isAjax(HttpServletRequest request) {
        if (!Boolean.TRUE.equals(this.supportTemplate)) {
            return true;
        }
        if (this.ajaxPatternList != null && !this.ajaxPatternList.isEmpty()) {
            return matchesPatterns(this.ajaxPatternList, request.getRequestURI());
        }
        String ajax = request.getHeader(Constant.IS_AJAX);
        return !isEmpty(ajax) && "true".equalsIgnoreCase(ajax);
    }

    protected boolean matchesPatterns(Set<String> patterns, String path) {
        if (patterns == null || patterns.isEmpty()) {
            return false;
        }
        for (String pattern : patterns) {
            if (path.matches(pattern)) {
                return true;
            }
        }
        return false;
    }

    protected boolean isEmpty(String value) {
        return StringUtils.isEmpty(StringUtils.trim(value));
    }
}
