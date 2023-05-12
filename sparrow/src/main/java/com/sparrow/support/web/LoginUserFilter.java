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

import com.sparrow.core.spi.JsonFactory;
import com.sparrow.json.Json;
import com.sparrow.protocol.LoginUser;
import com.sparrow.protocol.ThreadContext;
import com.sparrow.protocol.constant.Constant;
import com.sparrow.utility.StringUtility;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 分布式场景下，从请求头中获取登录用户信息
 */
public class LoginUserFilter implements Filter {
    public LoginUserFilter(Boolean mockLoginUser) {
        this.mockLoginUser = mockLoginUser;
    }

    private static Logger logger = LoggerFactory.getLogger(LoginUserFilter.class);

    private Boolean mockLoginUser;

    private Json json = JsonFactory.getProvider();

    @Override public void init(FilterConfig config) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
        FilterChain filterChain) throws IOException, ServletException {
        if (servletRequest instanceof HttpServletRequest) {
            HttpServletRequest req = (HttpServletRequest) servletRequest;
            String loginTokenOfHeader = req.getHeader(Constant.REQUEST_HEADER_KEY_LOGIN_TOKEN);
            LoginUser loginUser = null;
            if (!StringUtility.isNullOrEmpty(loginTokenOfHeader)) {
                loginUser = this.json.parse(loginTokenOfHeader, LoginUser.class);
            } else {
                if (mockLoginUser) {
                    loginUser = LoginUser.create(
                        1L,
                        "mock-user",
                        "mock-nick-name",
                        "header",
                        "device id", 3);
                }
            }
            ThreadContext.bindLoginToken(loginUser);
        }
        filterChain.doFilter(servletRequest, servletResponse);
        ThreadContext.clearToken();
    }

    @Override public void destroy() {

    }
}
