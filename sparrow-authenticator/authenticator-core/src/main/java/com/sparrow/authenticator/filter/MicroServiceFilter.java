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

import com.sparrow.authenticator.AuthenticatorConfigReader;
import com.sparrow.authenticator.DefaultLoginUser;
import com.sparrow.context.SessionContext;
import com.sparrow.core.spi.JsonFactory;
import com.sparrow.json.Json;
import com.sparrow.protocol.LoginUser;
import com.sparrow.protocol.enums.DeviceType;
import com.sparrow.utility.RegexUtility;
import com.sparrow.utility.StringUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
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
    private Json json = JsonFactory.getProvider();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        if (servletRequest instanceof HttpServletRequest) {
            HttpServletRequest req = (HttpServletRequest) servletRequest;
            String currentUrl = req.getServletPath();
            if (RegexUtility.matchPatterns(this.excludePatternList, currentUrl)) {
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }
            String loginTokenOfHeader = req.getHeader(tokenKey);
            LoginUser loginUser = null;
            if (!StringUtility.isNullOrEmpty(loginTokenOfHeader)) {
                loginUser = this.json.parse(loginTokenOfHeader, DefaultLoginUser.class);
            } else {
                if (mockLoginUser) {
                    loginUser = DefaultLoginUser.create(
                            1L, "",
                            DefaultLoginUser.CATEGORY_VISITOR,
                            "mock-user",
                            "mock-nick-name",
                            "header",
                            DeviceType.PC.getIdentity(),
                            "device id", 3D);
                }
            }
            SessionContext.bindLoginUser(loginUser);
        }
        filterChain.doFilter(servletRequest, servletResponse);
        SessionContext.clearToken();
    }

    @Override
    public void destroy() {

    }
}
