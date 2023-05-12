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
import com.sparrow.utility.ConfigUtility;
import com.sparrow.utility.StringUtility;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GlobalAttributeFilter implements Filter {
    @Override public void init(FilterConfig config) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
        FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpContext.getContext().setRequest(httpServletRequest);
        HttpContext.getContext().setResponse((HttpServletResponse) servletResponse);
        String rootPath = ConfigUtility.getValue(Config.ROOT_PATH);
        if (!StringUtility.isNullOrEmpty(rootPath)) {
            httpServletRequest.setAttribute(Config.ROOT_PATH, rootPath);
            httpServletRequest.setAttribute(Config.WEBSITE, ConfigUtility.getValue(Config.WEBSITE));
        }

        String internationalization = ConfigUtility.getValue(Config.INTERNATIONALIZATION);
        String language;
        if (internationalization != null) {
            language = servletRequest.getParameter(Config.LANGUAGE);
            if (language == null || !internationalization.contains(language)) {
                language = ConfigUtility.getValue(Config.LANGUAGE);
            }
            HttpContext.getContext().put("sparrow_request_language", language);
            httpServletRequest.setAttribute("sparrow_request_language", language);
        }

        servletRequest.setAttribute(Config.RESOURCE, ConfigUtility.getValue(Config.RESOURCE));
        servletRequest.setAttribute(Config.RESOURCE_VERSION, ConfigUtility.getValue(Config.RESOURCE_VERSION));
        servletRequest.setAttribute(Config.UPLOAD, ConfigUtility.getValue(Config.UPLOAD));
        servletRequest.setAttribute(Config.IMAGE_WEBSITE, ConfigUtility.getValue(Config.IMAGE_WEBSITE));
        if (httpServletRequest.getQueryString() != null) {
            servletRequest.setAttribute("queryString", httpServletRequest.getQueryString());
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override public void destroy() {

    }
}
