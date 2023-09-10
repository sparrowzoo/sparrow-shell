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

import com.sparrow.protocol.ClientInformation;
import com.sparrow.protocol.LoginUser;
import com.sparrow.protocol.ThreadContext;
import com.sparrow.protocol.constant.Constant;
import com.sparrow.support.Authenticator;
import com.sparrow.utility.StringUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * 单体应用解析，需要签名认证
 */
public class MonolithicLoginUserFilter implements Filter {
    private static Logger logger = LoggerFactory.getLogger(MonolithicLoginUserFilter.class);

    public MonolithicLoginUserFilter(Authenticator authenticator, Boolean mockLoginUser, List<String> whiteList) {
        this.authenticator = authenticator;
        this.mockLoginUser = mockLoginUser;
        this.whiteList = whiteList;
    }

    private Boolean mockLoginUser;
    private Authenticator authenticator;
    private List<String> whiteList;

    @Override
    public void init(FilterConfig config) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        if (servletRequest instanceof HttpServletRequest) {
            ClientInformation client = ThreadContext.getClientInfo();
            HttpServletRequest req = (HttpServletRequest) servletRequest;
            String currentUrl = req.getServletPath();
            if (this.whiteList.contains(currentUrl)) {
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }
            String loginToken = req.getHeader(Constant.REQUEST_HEADER_KEY_LOGIN_TOKEN);
            LoginUser loginUser = null;
            if (StringUtility.isNullOrEmpty(loginToken)) {
                loginToken = CookieUtility.getPermission(req);
            }
            if (!StringUtility.isNullOrEmpty(loginToken)) {
                try {
                    loginUser = this.authenticator.authenticate(loginToken, client.getDeviceId());
                } catch (Exception e) {
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
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ThreadContext.clearToken();
    }

    @Override
    public void destroy() {

    }
}
