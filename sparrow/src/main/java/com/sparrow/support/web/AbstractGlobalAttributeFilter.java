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
import com.sparrow.support.AttributeContext;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class AbstractGlobalAttributeFilter implements Filter {

    @Override
    public void init(FilterConfig config) throws ServletException {

    }

    public abstract AttributeContext parseAttributeContext();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        AttributeContext attributeContext = this.parseAttributeContext();
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpContext.getContext().setRequest(httpServletRequest);
        HttpContext.getContext().setResponse((HttpServletResponse) servletResponse);
        httpServletRequest.setAttribute(Config.ROOT_PATH, attributeContext.getRootPath());
        String internationalization = attributeContext.getLanguage();
        String language;
        //如果支持国际化
        if (internationalization != null) {
            language = servletRequest.getParameter(Config.LANGUAGE);
            if (language == null || !internationalization.contains(Config.INTERNATIONALIZATION)) {
                language = attributeContext.getLanguage();
            }
            HttpContext.getContext().put("sparrow_request_language", language);
            httpServletRequest.setAttribute("sparrow_request_language", language);
        }

        servletRequest.setAttribute(Config.RESOURCE, attributeContext.getResource());
        servletRequest.setAttribute(Config.RESOURCE_VERSION, attributeContext.getResourceVersion());
        servletRequest.setAttribute(Config.UPLOAD, attributeContext.getUpload());
        if (httpServletRequest.getQueryString() != null) {
            servletRequest.setAttribute("queryString", httpServletRequest.getQueryString());
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
