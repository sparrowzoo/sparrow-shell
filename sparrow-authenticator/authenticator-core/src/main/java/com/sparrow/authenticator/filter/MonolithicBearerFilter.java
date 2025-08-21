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
import com.sparrow.authenticator.*;
import com.sparrow.authenticator.enums.AuthenticatorError;
import com.sparrow.authenticator.token.BearerToken;
import com.sparrow.constant.Config;
import com.sparrow.context.SessionContext;
import com.sparrow.core.spi.ApplicationContext;
import com.sparrow.core.spi.JsonFactory;
import com.sparrow.protocol.*;
import com.sparrow.protocol.constant.Constant;
import com.sparrow.protocol.constant.Extension;
import com.sparrow.protocol.constant.magic.Symbol;
import com.sparrow.protocol.enums.DeviceType;
import com.sparrow.support.web.CookieUtility;
import com.sparrow.support.web.ServletUtility;
import com.sparrow.support.web.WebConfigReader;
import com.sparrow.utility.RegexUtility;
import com.sparrow.utility.StringUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 单体应用解析，需要签名认证
 */
public class MonolithicBearerFilter extends AbstractAuthcFilter {
    private static Logger logger = LoggerFactory.getLogger(MonolithicBearerFilter.class);

    public MonolithicBearerFilter(Authenticator authenticator, AuthenticatorConfigReader configReader, WebConfigReader webConfigReader) {
        this.authenticator = authenticator;
        this.mockLoginUser = configReader.getMockLoginUser();
        this.supportTemplate = supportTemplate;
        this.ajaxPatternList = webConfigReader.getAjaxPattens();
        this.excludePatternList = configReader.getExcludePatterns();
        if (StringUtility.isNullOrEmpty(tokenKey)) {
            this.tokenKey = Constant.REQUEST_HEADER_KEY_LOGIN_TOKEN;
        } else {
            this.tokenKey = configReader.getTokenKey();
        }
    }

    private Boolean mockLoginUser;
    private Authenticator authenticator;
    private String tokenKey;


    private void loginSuccess(LoginUser loginUser, FilterChain filterChain, HttpServletRequest request, HttpServletResponse response) {
        SessionContext.bindLoginUser(loginUser);
        logger.info("login success bind success, user:{}", JSON.toJSONString(loginUser));
        try {
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            logger.error("do filter error", e);
        } finally {
            SessionContext.clearToken();
        }
    }

    private void loginFail(HttpServletRequest request, HttpServletResponse servletResponse, ErrorSupport e) throws IOException {
        logger.error("login fail, error:{}", e.getMessage());
        WebConfigReader configReader = ApplicationContext.getContainer().getBean(WebConfigReader.class);
        boolean isAjax = this.isAjax(request);
        if (isAjax) {
            servletResponse.setContentType(Constant.CONTENT_TYPE_JSON);
            Result result = Result.fail(e);
            servletResponse.getWriter().write(JsonFactory.getProvider().toString(result));
            return;
        }
        String loginUrl = configReader.getLoginUrl();

        if (StringUtility.isNullOrEmpty(loginUrl)) {
            logger.error("please config login url 【{}】", Config.LOGIN_URL);
            return;
        }

        String redirectUrl = request.getRequestURL().toString();
        if (redirectUrl.endsWith(Extension.DO) || redirectUrl.endsWith(Extension.JSON)) {
            redirectUrl = ServletUtility.getInstance().referer(request);
        }
        //https://passport.jd.com/new/login.aspx?ReturnUrl=https%3A%2F%2Fitem.jd.com%2F100061319193.html
        if (!StringUtility.isNullOrEmpty(redirectUrl)) {
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
        ClientInformation client = SessionContext.getClientInfo();
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse rep = (HttpServletResponse) servletResponse;

        String currentUrl = req.getServletPath();
        if (RegexUtility.matchPatterns(this.excludePatternList, currentUrl)) {
            logger.info("exclude url {}", currentUrl);
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        String loginToken = req.getHeader(tokenKey);//密文
        LoginUser loginUser = null;
        //token 在header  里没拿到
        if (this.isNullOrEmpty(loginToken)) {
            loginToken = CookieUtility.getPermission(req, tokenKey);
        }
        //token 没拿到
        if (!this.isNullOrEmpty(loginToken)) {
            try {
                //认证逻辑
                loginUser = this.authenticator.authenticate(new BearerToken(loginToken, client.getDeviceId()));
                this.loginSuccess(loginUser, filterChain, req, rep);
            } catch (BusinessException e) {
                this.loginFail(req, (HttpServletResponse) servletResponse, e.getErrorSupport());
            }
            return;
        }
        if (mockLoginUser) {
            loginUser = DefaultLoginUser.create(1L, "", 1, "mock-user", "mock-nick-name", "header", DeviceType.PC.getIdentity(), "device id", 3D);
            this.loginSuccess(loginUser, filterChain, req, rep);
            return;
        }
        this.loginFail(req, rep, AuthenticatorError.USER_NOT_LOGIN);
    }

    @Override
    public void destroy() {

    }
}
