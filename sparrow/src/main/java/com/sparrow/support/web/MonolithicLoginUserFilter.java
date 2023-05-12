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

import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.ClientInformation;
import com.sparrow.protocol.LoginUser;
import com.sparrow.protocol.ThreadContext;
import com.sparrow.protocol.constant.Constant;
import com.sparrow.support.Authenticator;
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

public class MonolithicLoginUserFilter implements Filter {
    private static Logger logger = LoggerFactory.getLogger(MonolithicLoginUserFilter.class);

    public MonolithicLoginUserFilter(Authenticator authenticator, Boolean mockLoginUser) {
        this.authenticator = authenticator;
        this.mockLoginUser = mockLoginUser;
    }

    private Boolean mockLoginUser;
    private Authenticator authenticator;

    @Override public void init(FilterConfig config) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
        FilterChain filterChain) throws IOException, ServletException {
        if (servletRequest instanceof HttpServletRequest) {
            ClientInformation client = ThreadContext.getClientInfo();
            HttpServletRequest req = (HttpServletRequest) servletRequest;
            String loginTokenOfHeader = req.getHeader(Constant.REQUEST_HEADER_KEY_LOGIN_TOKEN);
            LoginUser loginUser = null;
            if (!StringUtility.isNullOrEmpty(loginTokenOfHeader)) {
                try {
                    loginUser = this.authenticator.authenticate(loginTokenOfHeader, client.getDeviceId());
                } catch (BusinessException e) {
                    logger.error("authenticate error", e);
                    throw new RuntimeException(e);
                }
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
