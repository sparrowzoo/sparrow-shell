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

import com.alibaba.fastjson.JSON;
import com.sparrow.authenticator.AuthenticatorConfigReader;
import com.sparrow.authenticator.DefaultLoginUser;
import com.sparrow.protocol.context.SessionContext;
import com.sparrow.protocol.LoginUser;
import com.sparrow.protocol.enums.DefaultUserCategory;
import com.sparrow.protocol.enums.DeviceType;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 分布式场景下，从请求头中获取登录用户信息
 */
public class MicroServiceFilter extends AbstractAuthcFilter {
    public MicroServiceFilter(AuthenticatorConfigReader configReader) {
        this.mockLoginUser = configReader.getMockLoginUser();
        this.excludePatternList = configReader.getExcludePatterns();
    }

    private static Logger logger = LoggerFactory.getLogger(MicroServiceFilter.class);
    private Boolean mockLoginUser;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        LoginUser previous = SessionContext.getLoginUser();
        boolean bound = false;
        try {
            if (servletRequest instanceof HttpServletRequest) {
                HttpServletRequest req = (HttpServletRequest) servletRequest;
                String currentUrl = req.getServletPath();
                if (matchesPatterns(this.excludePatternList, currentUrl)) {
                    filterChain.doFilter(servletRequest, servletResponse);
                    return;
                }
                String loginTokenOfHeader = req.getHeader(tokenKey);
                LoginUser loginUser = null;
                if (!isEmpty(loginTokenOfHeader)) {
                    loginUser = JSON.parseObject(loginTokenOfHeader, DefaultLoginUser.class);
                } else {
                    if (mockLoginUser) {
                        loginUser = DefaultLoginUser.create(
                                1L, "",
                                DefaultUserCategory.REGISTER.getIdentity(),
                                "mock-user",
                                "mock-nick-name",
                                "header",
                                DeviceType.PC,
                                "device id", 1L, 30D);
                    }
                }
                SessionContext.bindLoginUser(loginUser);
                bound = true;
            }
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            if (bound) {
                SessionContext.clearToken();
                if (previous != null) {
                    SessionContext.bindLoginUser(previous);
                }
            }
        }
    }

    @Override
    public void destroy() {

    }
}
