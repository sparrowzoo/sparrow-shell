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

package com.sparrow.mvc.interceptor;

import com.sparrow.constant.Config;
import com.sparrow.constant.User;
import com.sparrow.container.Container;
import com.sparrow.core.spi.ApplicationContext;
import com.sparrow.core.spi.JsonFactory;
import com.sparrow.enums.LoginType;
import com.sparrow.mvc.ServletInvokableHandlerMethod;
import com.sparrow.protocol.LoginUser;
import com.sparrow.protocol.Result;
import com.sparrow.protocol.constant.Constant;
import com.sparrow.protocol.constant.Extension;
import com.sparrow.protocol.constant.SparrowError;
import com.sparrow.protocol.constant.magic.Symbol;
import com.sparrow.servlet.HandlerInterceptor;
import com.sparrow.support.Authenticator;
import com.sparrow.support.Authorizer;
import com.sparrow.support.LoginDialog;
import com.sparrow.support.web.CookieUtility;
import com.sparrow.support.web.HttpContext;
import com.sparrow.support.web.ServletUtility;
import com.sparrow.support.web.WebConfigReader;
import com.sparrow.utility.StringUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Named
public class AuthInterceptor implements HandlerInterceptor {
    private static Logger logger = LoggerFactory.getLogger(AuthInterceptor.class);
    private ServletUtility servletUtility = ServletUtility.getInstance();

    @Override
    public boolean preHandle(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws Exception {
        Container container = ApplicationContext.getContainer();
        if (servletUtility.include(httpRequest)) {
            return true;
        }
        ServletInvokableHandlerMethod handlerExecutionChain = null;
        try {
            handlerExecutionChain = (ServletInvokableHandlerMethod) httpRequest.getAttribute(Constant.REQUEST_INVOKABLE_HANDLER_METHOD);
        } catch (Exception e) {
            logger.error("mapping handler error", e);
            return false;
        }

        if (handlerExecutionChain == null) {
            return true;
        }

        //未授权
        if (!handlerExecutionChain.isNeedAuthorizing()) {
            return true;
        }

        String actionName = handlerExecutionChain.getActionName();
        Authenticator authenticator = container.getBean(Authenticator.class);
        String permission = CookieUtility.getPermission(httpRequest, Constant.REQUEST_HEADER_KEY_LOGIN_TOKEN);
        String deviceId = servletUtility.getDeviceId(httpRequest);
        LoginUser user = authenticator.authenticate(permission, deviceId);
        httpRequest.setAttribute(User.ID, user.getUserId());
        httpRequest.setAttribute(User.LOGIN_TOKEN, user);

        if (user.isVisitor()) {
            if (LoginType.MESSAGE.equals(handlerExecutionChain.getLoginType())) {
                Result result = Result.fail(SparrowError.USER_NOT_LOGIN);
                httpResponse.setHeader("Content-Type", Constant.CONTENT_TYPE_JSON);
                httpResponse.getWriter().write(JsonFactory.getProvider().toString(result));
                return false;
            }

            WebConfigReader configReader = container.getBean(WebConfigReader.class);
            String loginUrl = configReader.getLoginUrl();
            if (StringUtility.isNullOrEmpty(loginUrl)) {
                logger.error("please config login url 【{}】", Config.LOGIN_URL);
                return false;
            }
            boolean isInFrame = LoginType.LOGIN_IFRAME.equals(handlerExecutionChain.getLoginType());
            if (!StringUtility.isNullOrEmpty(loginUrl)) {
                String redirectUrl = httpRequest.getRequestURL().toString();
                if (redirectUrl.endsWith(Extension.DO) || redirectUrl.endsWith(Extension.JSON)) {
                    redirectUrl = servletUtility.referer(httpRequest);
                }

                if (!StringUtility.isNullOrEmpty(redirectUrl)) {
                    if (httpRequest.getQueryString() != null) {
                        redirectUrl += Symbol.QUESTION_MARK + httpRequest.getQueryString();
                    }
                    if (isInFrame) {
                        String defaultSystemPage = configReader.getAdminPage();
                        if (!defaultSystemPage.equals(redirectUrl)) {
                            redirectUrl = defaultSystemPage + Symbol.QUESTION_MARK + redirectUrl;
                        } else {
                            redirectUrl = defaultSystemPage;
                        }
                    }
                    loginUrl = loginUrl + Symbol.QUESTION_MARK + redirectUrl;
                }
            }
            if (!handlerExecutionChain.isJson()) {
                if (isInFrame) {
                    HttpContext.getContext().execute(String.format("window.parent.location.href='%1$s'", loginUrl));
                } else {
                    httpResponse.sendRedirect(loginUrl);
                }
            } else {
                LoginDialog loginDialog = new LoginDialog(false, false, loginUrl, isInFrame);
                httpResponse.setHeader("Content-Type", Constant.CONTENT_TYPE_JSON);
                httpResponse.getWriter().write(JsonFactory.getProvider().toString(loginDialog));
            }
            logger.info("login false{}", actionName);
            //如果权限验证失败则移出属性
            this.servletUtility.moveAttribute(httpRequest);
            return false;
        }

        Authorizer authorizer = container.getBean(Authorizer.class);
        if (!authorizer.isPermitted(user, actionName)) {
            httpResponse.getWriter().write(Constant.ACCESS_DENIED);
            this.servletUtility.moveAttribute(httpRequest);
            return false;
        }
        HttpContext.getContext().put(Constant.REQUEST_USER_ID, user.getUserId());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response) throws Exception {

    }
}
