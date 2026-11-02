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
import com.sparrow.authenticator.Authenticator;
import com.sparrow.authenticator.AuthenticatorConfigReader;
import com.sparrow.authenticator.DefaultLoginUser;
import com.sparrow.authenticator.enums.AuthenticatorError;
import com.sparrow.authenticator.token.BearerToken;
import com.sparrow.protocol.context.SessionContext;
import com.sparrow.protocol.*;
import com.sparrow.protocol.constant.Constant;
import com.sparrow.protocol.constant.ClientInfoConstant;
import com.sparrow.protocol.constant.Extension;
import com.sparrow.protocol.constant.magic.Symbol;
import com.sparrow.protocol.enums.DeviceType;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Cookie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedHashSet;

/**
 * 单体应用解析，需要签名认证
 */
@Slf4j
public class MonolithicBearerFilter extends AbstractAuthcFilter {
    public MonolithicBearerFilter(Authenticator authenticator, AuthenticatorConfigReader configReader, Environment environment) {
        this.authenticator = authenticator;
        this.environment = environment;
        this.mockLoginUser = configReader.getMockLoginUser();
        if (this.mockLoginUser == null) {
            this.mockLoginUser = false;
        }
        this.supportTemplate = environment.getProperty("support_template_engine", Boolean.class, false);
        String ajaxPatterns = environment.getProperty("ajax_patterns");
        if (!isEmpty(ajaxPatterns)) {
            this.ajaxPatternList = new LinkedHashSet<>(Arrays.asList(ajaxPatterns.split(",")));
        }
        this.excludePatternList = configReader.getExcludePatterns();
        if (isEmpty(tokenKey)) {
            this.tokenKey = Constant.REQUEST_HEADER_KEY_LOGIN_TOKEN;
        } else {
            this.tokenKey = configReader.getTokenKey();
        }
    }

    private Boolean mockLoginUser;
    private Authenticator authenticator;
    private String tokenKey;
    private final Environment environment;


    private void loginSuccess(LoginUser loginUser, FilterChain filterChain, HttpServletRequest request, HttpServletResponse response) {
        LoginUser previous = SessionContext.getLoginUser();
        SessionContext.bindLoginUser(loginUser);
        try {
            log.info("login success bind success, user:{}", JSON.toJSONString(loginUser));
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error("do filter error", e);
        } finally {
            SessionContext.clearToken();
            if (previous != null) {
                SessionContext.bindLoginUser(previous);
            }
        }
    }

    private void loginFail(HttpServletRequest request, HttpServletResponse servletResponse, ErrorSupport e) throws IOException {
        log.error("login fail, error:{}", e.getMessage());
        boolean isAjax = this.isAjax(request);
        if (isAjax) {
            servletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            servletResponse.setContentType(Constant.CONTENT_TYPE_JSON);
            Result result = Result.fail(e);
            servletResponse.getWriter().write(JSON.toJSONString(result));
            return;
        }
        String loginUrl = this.environment.getProperty("login_url");

        if (isEmpty(loginUrl)) {
            log.error("please config login url 【{}】", "login_url");
            return;
        }

        String redirectUrl = request.getRequestURL().toString();
        if (redirectUrl.endsWith(Extension.DO) || redirectUrl.endsWith(Extension.JSON)) {
            redirectUrl = request.getHeader("Referer");
        }
        //https://passport.jd.com/new/login.aspx?ReturnUrl=https%3A%2F%2Fitem.jd.com%2F100061319193.html
        if (!isEmpty(redirectUrl)) {
            if (request.getQueryString() != null) {
                redirectUrl += Symbol.QUESTION_MARK + request.getQueryString();
            }
            loginUrl = loginUrl + Symbol.QUESTION_MARK + redirectUrl;
        }
        servletResponse.sendRedirect(loginUrl);
    }

    private boolean isNullOrEmpty(String str) {
        if (str == null || str.trim().equals("") || str.trim().equals("null")) {
            return true;
        }
        return false;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        boolean isHttpServlet = servletRequest instanceof HttpServletRequest;
        if (!isHttpServlet) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse rep = (HttpServletResponse) servletResponse;

        String currentUrl = req.getServletPath();
        if (matchesPatterns(this.excludePatternList, currentUrl)) {
            log.info("exclude url {}", currentUrl);
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        String loginToken = req.getHeader(tokenKey);//密文
        LoginUser loginUser = null;
        //token 在header  里没拿到
        if (this.isNullOrEmpty(loginToken)) {
            loginToken = getCookieToken(req);
        }
        //token 没拿到
        if (!this.isNullOrEmpty(loginToken)) {
            try {
                //认证逻辑
                loginUser = this.authenticator.authenticate(new BearerToken(loginToken, getDeviceId(req)));
                this.loginSuccess(loginUser, filterChain, req, rep);
            } catch (BusinessException e) {
                this.loginFail(req, (HttpServletResponse) servletResponse, e.getErrorSupport());
            }
            return;
        }
        if (mockLoginUser) {
            loginUser = DefaultLoginUser.create(1L, "", 1, "mock-user", "mock-nick-name", "header", DeviceType.PC, "device id", 0L,3D);
            this.loginSuccess(loginUser, filterChain, req, rep);
            return;
        }
        this.loginFail(req, rep, AuthenticatorError.USER_NOT_LOGIN);
    }

    private String getCookieToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equalsIgnoreCase(tokenKey)) {
                return URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8);
            }
        }
        return null;
    }

    private String getDeviceId(HttpServletRequest request) {
        ClientInformation client = SessionContext.getClientInfo();
        String deviceId = client == null ? null : client.getDeviceId();
        if (isEmpty(deviceId)) {
            deviceId = request.getHeader(ClientInfoConstant.DEVICE_ID);
        }
        return isEmpty(deviceId) ? request.getRemoteAddr() : deviceId;
    }

    @Override
    public void destroy() {

    }
}
