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
import com.sparrow.support.web.ServletUtility;
import com.sparrow.utility.StringUtility;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractAuthcFilter implements Filter {
    protected List<String> excludePatternList = new ArrayList<String>();
    protected Boolean supportTemplate;
    protected List<String> ajaxPatternList = new ArrayList<String>();
    protected String tokenKey = Constant.REQUEST_HEADER_KEY_LOGIN_TOKEN;

    @Override
    public void init(FilterConfig config) throws ServletException {
        String excludePatterns = config.getInitParameter("excludePatterns");
        if (StringUtility.isNullOrEmpty(excludePatterns)) {
            return;
        }
        String[] patterns = excludePatterns.split(",");
        for (String pattern : patterns) {
            pattern = pattern.replace("*", ".*");
            this.excludePatternList.add(pattern);
        }
    }

    public Boolean isAjax(HttpServletRequest request) {
        return ServletUtility.getInstance().isAjax(request, this.supportTemplate, this.ajaxPatternList);
    }
}
