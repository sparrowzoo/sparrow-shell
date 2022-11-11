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

package com.sparrow.mvc;

import com.sparrow.constant.Config;
import com.sparrow.constant.ConfigKeyLanguage;
import com.sparrow.constant.SysObjectName;
import com.sparrow.constant.User;
import com.sparrow.container.Container;
import com.sparrow.container.FactoryBean;
import com.sparrow.core.Pair;
import com.sparrow.core.spi.ApplicationContext;
import com.sparrow.core.spi.JsonFactory;
import com.sparrow.datasource.ConnectionContextHolder;
import com.sparrow.enums.LoginType;
import com.sparrow.mvc.adapter.HandlerAdapter;
import com.sparrow.mvc.adapter.impl.MethodControllerHandlerAdapter;
import com.sparrow.mvc.mapping.HandlerMapping;
import com.sparrow.mvc.mapping.impl.UrlMethodHandlerMapping;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.LoginToken;
import com.sparrow.protocol.Result;
import com.sparrow.protocol.constant.Constant;
import com.sparrow.protocol.constant.Extension;
import com.sparrow.protocol.constant.SparrowError;
import com.sparrow.protocol.constant.magic.Digit;
import com.sparrow.protocol.constant.magic.Symbol;
import com.sparrow.servlet.HandlerInterceptor;
import com.sparrow.support.Authenticator;
import com.sparrow.support.Authorizer;
import com.sparrow.support.LoginDialog;
import com.sparrow.support.web.CookieUtility;
import com.sparrow.support.web.HttpContext;
import com.sparrow.utility.ConfigUtility;
import com.sparrow.utility.StringUtility;
import com.sparrow.utility.web.SparrowServletUtility;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherFilter implements Filter {

    private static Logger logger = LoggerFactory.getLogger(DispatcherFilter.class);

    private SparrowServletUtility sparrowServletUtility = SparrowServletUtility.getInstance();

    private FilterConfig config;

    private List<HandlerAdapter> handlerAdapters;

    private List<HandlerMapping> handlerMappings;

    private volatile List<HandlerInterceptor> handlerInterceptorList;

    private String[] exceptUrl;

    //private List<HandlerExceptionResolver> handlerExceptionResolvers;

    private Container container;

    private CookieUtility cookieUtility;

    private ConnectionContextHolder connectionContextHolder;

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
        FilterChain chain) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String actionKey = sparrowServletUtility.getServletUtility().getActionKey(request);
        if (StringUtility.existInArray(this.exceptUrl, actionKey)) {
            try {
                chain.doFilter(request, response);
            } catch (Exception e) {
                logger.error("filter error", e);
            }
            return;
        }
        this.initInterceptors();

        if (preHandler(httpRequest, httpResponse)) {
            return;
        }
        ServletInvokableHandlerMethod invokableHandlerMethod = null;
        try {
            invokableHandlerMethod = this.getHandler(httpRequest);
            //验证用户需要httpContext 输出脚本
            this.initAttribute(httpRequest, httpResponse);
            if (!this.validateUser(httpRequest, httpResponse)) {
                return;
            }
            if (invokableHandlerMethod == null || invokableHandlerMethod.getMethod() == null) {
                logger.warn("invokableHandlerMethod is null or method not exist action-key {}", actionKey);
                String extension = ConfigUtility.getValue(Config.DEFAULT_PAGE_EXTENSION, Extension.JSP);
                if (actionKey.endsWith(extension) || actionKey.endsWith(Extension.JSON)) {
                    chain.doFilter(request, response);
                } else {
                    String dispatcherUrl = sparrowServletUtility.getServletUtility().assembleActualUrl(actionKey);
                    logger.debug("dispatcher url is {}", dispatcherUrl);
                    RequestDispatcher dispatcher = request.getRequestDispatcher(dispatcherUrl);
                    dispatcher.forward(request, response);
                }
            } else {
                HandlerAdapter adapter = this.getHandlerAdapter(invokableHandlerMethod);
                adapter.handle(chain, httpRequest, httpResponse, invokableHandlerMethod);
            }
            this.postHandler(httpRequest, httpResponse);
        } catch (Exception e) {
            errorHandler(httpRequest, httpResponse, invokableHandlerMethod, e);
        } finally {
            //页面渲染完成之后执行
            afterCompletion(httpRequest, httpResponse);
        }
    }

    private void errorHandler(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
        ServletInvokableHandlerMethod invocableHandlerMethod, Exception e) {
        Throwable target = e;
        if (e.getCause() == null) {
            logger.error("e.getCause==null", e);
        } else {
            target = e.getCause();
            logger.error("e.getCause!=null", e.getCause());
        }
        if (invocableHandlerMethod != null) {
            try {
                invocableHandlerMethod.getMethodReturnValueResolverHandler().errorResolve(target, httpRequest, httpResponse);
            } catch (Exception ignore) {
                logger.error("exception resolve error", ignore);
            }
        }
        if (this.connectionContextHolder != null) {
            this.connectionContextHolder.removeAll();
        }
    }

    private void afterCompletion(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        HttpContext.getContext().remove();
        for (HandlerInterceptor handlerInterceptor : handlerInterceptorList) {
            try {
                handlerInterceptor.afterCompletion(httpRequest, httpResponse);
            } catch (Exception ex) {
                logger.error(handlerInterceptor.getClass().getName() + "interception after handler error", ex);
            }
        }
    }

    private void postHandler(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        for (HandlerInterceptor handlerInterceptor : handlerInterceptorList) {
            try {
                handlerInterceptor.postHandle(httpRequest, httpResponse);
            } catch (Exception ex) {
                logger.error(handlerInterceptor.getClass().getName() + "interception post handler error", ex);
            }
        }
    }

    private boolean preHandler(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        for (HandlerInterceptor handlerInterceptor : handlerInterceptorList) {
            try {
                if (!handlerInterceptor.preHandle(httpRequest, httpResponse)) {
                    return true;
                }
            } catch (Exception e) {
                logger.error(handlerInterceptor.getClass().getName() + "interception pre handler error", e);
            }
        }
        return false;
    }

    private void initInterceptors() {
        if (this.handlerInterceptorList != null) {
            return;
        }
        synchronized (this) {
            if (this.handlerInterceptorList != null) {
                return;
            }

            FactoryBean<HandlerInterceptor> handlerInterceptorRegister = container.getInterceptorRegister();

            Iterator<String> iterator = handlerInterceptorRegister.keyIterator();
            List<HandlerInterceptor> handlerInterceptorList = new ArrayList<HandlerInterceptor>();
            while (iterator.hasNext()) {
                String key = iterator.next();
                handlerInterceptorList.add(handlerInterceptorRegister.getObject(key));

            }
            this.handlerInterceptorList = handlerInterceptorList;
        }
    }

    private ServletInvokableHandlerMethod getHandler(HttpServletRequest request) throws Exception {
        for (HandlerMapping handlerMapping : this.handlerMappings) {
            ServletInvokableHandlerMethod invocableHandlerMethod = handlerMapping.getHandler(request);
            if (invocableHandlerMethod != null) {
                return invocableHandlerMethod;
            }
        }
        return null;
    }

    private HandlerAdapter getHandlerAdapter(Object handler) throws ServletException {
        for (HandlerAdapter ha : this.handlerAdapters) {
            if (ha.supports(handler)) {
                return ha;
            }
        }
        throw new ServletException("No adapter for handler [" + handler +
            "]: The DispatcherServlet configuration needs to include a HandlerAdapter that supports this handler");
    }

    private void initAttribute(HttpServletRequest request,
        HttpServletResponse response) {
        //初始化 request
        HttpContext.getContext().setRequest(request);
        //初始化 response
        HttpContext.getContext().setResponse(response);

        if (sparrowServletUtility.getServletUtility().include(request)) {
            return;
        }
        String actionKey = sparrowServletUtility.getServletUtility().getActionKey(request);
        logger.debug("PARAMETERS:" + sparrowServletUtility.getServletUtility().getAllParameter(request));
        logger.debug("ACTION KEY:" + actionKey);
        String forumCode = request.getParameter("forumCode");
        request.setAttribute(Constant.REQUEST_ACTION_CURRENT_FORUM, forumCode);
        request.setAttribute("divNavigation.current", forumCode);

        String rootPath = ConfigUtility.getValue(Config.ROOT_PATH);
        if (!StringUtility.isNullOrEmpty(rootPath)) {
            request.setAttribute(Config.ROOT_PATH, rootPath);
            request.setAttribute(Config.WEBSITE,
                ConfigUtility.getValue(Config.WEBSITE));
        }

        //国际化
        String internationalization = ConfigUtility
            .getValue(Config.INTERNATIONALIZATION);
        if (internationalization != null) {
            //设置当前请求语言
            String language = request.getParameter(Config.LANGUAGE);
            if (language == null
                || !internationalization.contains(language)) {
                language = ConfigUtility.getValue(Config.LANGUAGE);
            }
            HttpContext.getContext().put(Constant.REQUEST_LANGUAGE, language);
        }
        //设置资源根域名
        request.setAttribute(Config.RESOURCE,
            ConfigUtility.getValue(Config.RESOURCE));

        //设置上传文件域名
        request.setAttribute(Config.UPLOAD, ConfigUtility.getValue(Config.UPLOAD));

        //设置图片域
        request.setAttribute(Config.IMAGE_WEBSITE, ConfigUtility.getValue(Config.IMAGE_WEBSITE));

        String configWebsiteName = ConfigUtility.getLanguageValue(
            ConfigKeyLanguage.WEBSITE_NAME, ConfigUtility.getValue(Config.LANGUAGE));
        request.setAttribute(ConfigKeyLanguage.WEBSITE_NAME, configWebsiteName);

        if (configWebsiteName != null) {
            String currentWebsiteName = cookieUtility.get(request.getCookies(),
                ConfigKeyLanguage.WEBSITE_NAME);
            if (!configWebsiteName.equals(currentWebsiteName)) {
                cookieUtility.set(response, ConfigKeyLanguage.WEBSITE_NAME,
                    configWebsiteName, Digit.ALL);
            }
        }
        if (request.getQueryString() != null) {
            request.setAttribute("previous_url", request.getQueryString());
        }

        Pair<String, Map<String, Object>> sessionPair = (Pair<String, Map<String, Object>>) request.getSession().getAttribute(Constant.FLASH_KEY);
        if (sessionPair == null) {
            return;
        }

        if (this.matchUrl(sessionPair.getFirst(), actionKey, request)) {
            Map<String, Object> values = sessionPair.getSecond();
            for (String key : values.keySet()) {
                request.setAttribute(key, values.get(key));
            }
            return;
        }
        //url换掉时，则session 被清空 （非include）
        request.getSession().removeAttribute(Constant.FLASH_KEY);
    }

    /**
     * flash key -->/template/action-url.jsp  final url
     * <p>
     * direct mode action url-->action-url
     * <p>
     * <p>
     * transit mode transit url--> transit-url?action_url
     */
    private boolean matchUrl(String flashKey, String actionKey, HttpServletRequest request) {
        //redirect final jsp url
        if (StringUtility.matchUrl(flashKey, actionKey)) {
            return true;
        }

        //redirect action url
        String actualUrl = sparrowServletUtility.getServletUtility().assembleActualUrl(actionKey);
        if (StringUtility.matchUrl(flashKey, actualUrl)) {
            return true;
        }
        //transit final jsp url
        actionKey = request.getQueryString();
        if (actionKey == null) {
            return false;
        }
        if (StringUtility.matchUrl(flashKey, actionKey)) {
            return true;
        }

        //transit action url
        String actualTransitUrl = sparrowServletUtility.getServletUtility().assembleActualUrl(actionKey);
        if (StringUtility.matchUrl(flashKey, actualTransitUrl)) {
            return true;
        }

        return false;
    }

    private boolean validateUser(
        HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException, BusinessException {
        if (sparrowServletUtility.getServletUtility().include(httpRequest)) {
            return true;
        }
        ServletInvokableHandlerMethod handlerExecutionChain = null;
        try {
            handlerExecutionChain = this.getHandler(httpRequest);
        } catch (Exception e) {
            logger.error("mapping handler error", e);
            return false;
        }

        if (handlerExecutionChain == null) {
            return true;
        }

        //未授权F
        if (!handlerExecutionChain.isNeedAuthorizing()) {
            return true;
        }

        String actionName = handlerExecutionChain.getActionName();
        Authenticator authenticator = this.container.getBean(SysObjectName.AUTHENTICATOR_SERVICE);
        String permission = this.cookieUtility.getPermission(httpRequest);
        String deviceId = this.sparrowServletUtility.getServletUtility().getDeviceId(httpRequest);
        LoginToken user = authenticator.authenticate(permission, deviceId);
        httpRequest.setAttribute(User.ID, user.getUserId());
        httpRequest.setAttribute(User.LOGIN_TOKEN, user);

        if (user.getUserId().equals(User.VISITOR_ID)) {
            if (LoginType.MESSAGE.equals(handlerExecutionChain.getLoginType())) {
                Result result = Result.fail(SparrowError.USER_NOT_LOGIN);
                httpResponse.setHeader("Content-Type", Constant.CONTENT_TYPE_JSON);
                httpResponse.getWriter().write(JsonFactory.getProvider().toString(result));
                return false;
            }

            String loginKey = Config.LOGIN_TYPE_KEY
                .get(handlerExecutionChain.getLoginType());
            String loginUrl = ConfigUtility.getValue(loginKey);
            if (StringUtility.isNullOrEmpty(loginUrl)) {
                logger.error("please config login url 【{}】", loginKey);
            }
            boolean isInFrame = LoginType.LOGIN_IFRAME
                .equals(handlerExecutionChain.getLoginType());
            if (!StringUtility.isNullOrEmpty(loginUrl)) {
                String defaultSystemPage = ConfigUtility.getValue(Config.DEFAULT_ADMIN_INDEX);
                if (!defaultSystemPage.endsWith("/")) {
                    defaultSystemPage += "/";
                }
                String redirectUrl = httpRequest.getRequestURL().toString();
                if (redirectUrl.endsWith(Extension.DO) || redirectUrl.endsWith(Extension.JSON)) {
                    redirectUrl = sparrowServletUtility.getServletUtility().referer(httpRequest);
                }

                if (!StringUtility.isNullOrEmpty(redirectUrl)) {
                    if (httpRequest.getQueryString() != null) {
                        redirectUrl += Symbol.QUESTION_MARK + httpRequest.getQueryString();
                    }
                    if (isInFrame) {
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
            this.sparrowServletUtility.moveAttribute(httpRequest);
            return false;
        }

        Authorizer authorizer = this.container.getBean(SysObjectName.AUTHORIZER_SERVICE);
        if (!authorizer.isPermitted(
            user, actionName)) {
            httpResponse.getWriter().write(Constant.ACCESS_DENIED);
            this.sparrowServletUtility.moveAttribute(httpRequest);
            return false;
        }
        HttpContext.getContext().put(Constant.REQUEST_USER_ID, user.getUserId());
        return true;
    }

    /**
     * 初始化所有策略
     */
    private void initStrategies() {
        this.initHandlerMapping();
        this.initAdapter();
    }

    /**
     * 初始化handler mapping
     */
    private void initHandlerMapping() {
        this.handlerMappings = new ArrayList<HandlerMapping>();
        this.handlerMappings.add(new UrlMethodHandlerMapping());
    }

    /**
     * 初始化适配器
     */
    private void initAdapter() {
        this.handlerAdapters = new ArrayList<HandlerAdapter>();
        MethodControllerHandlerAdapter adapter = new MethodControllerHandlerAdapter();
        this.handlerAdapters.add(adapter);
    }

    @Override
    public void init(FilterConfig config) {
        this.config = config;
        this.container = ApplicationContext.getContainer();
        this.cookieUtility = this.container.getBean(SysObjectName.COOKIE_UTILITY);
        this.connectionContextHolder = this.container.getBean(SysObjectName.CONNECTION_CONTEXT_HOLDER);
        String exceptUrl = config.getInitParameter("except_url");
        if (!StringUtility.isNullOrEmpty(exceptUrl)) {
            this.exceptUrl = exceptUrl.split(",");
        }
        this.initStrategies();
    }

    public FilterConfig getConfig() {
        return config;
    }

    public void setConfig(FilterConfig config) {
        this.config = config;
    }
}
