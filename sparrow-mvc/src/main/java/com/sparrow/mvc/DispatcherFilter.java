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

import com.sparrow.container.Container;
import com.sparrow.container.FactoryBean;
import com.sparrow.core.Pair;
import com.sparrow.core.spi.ApplicationContext;
import com.sparrow.datasource.ConnectionContextHolder;
import com.sparrow.lang.url.UrlAssembler;
import com.sparrow.lang.url.UrlMatcher;
import com.sparrow.mvc.adapter.HandlerAdapter;
import com.sparrow.mvc.adapter.impl.MethodControllerHandlerAdapter;
import com.sparrow.mvc.mapping.HandlerMapping;
import com.sparrow.mvc.mapping.impl.UrlMethodHandlerMapping;
import com.sparrow.protocol.NotTryException;
import com.sparrow.protocol.constant.Constant;
import com.sparrow.protocol.constant.Extension;
import com.sparrow.servlet.HandlerInterceptor;
import com.sparrow.support.web.CookieUtility;
import com.sparrow.support.web.HttpContext;
import com.sparrow.support.web.ServletUtility;
import com.sparrow.support.web.WebConfigReader;
import com.sparrow.utility.StringUtility;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Slf4j
public class DispatcherFilter implements Filter {

    protected ServletUtility servletUtility = ServletUtility.getInstance();

    protected FilterConfig config;

    protected List<HandlerAdapter> handlerAdapters;

    protected List<HandlerMapping> handlerMappings;

    protected volatile List<HandlerInterceptor> handlerInterceptorList;

    protected String[] exceptUrl;

    //private List<HandlerExceptionResolver> handlerExceptionResolvers;

    protected Container container;

    protected CookieUtility cookieUtility;

    protected ConnectionContextHolder connectionContextHolder;

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String actionKey = servletUtility.getActionKey(request);
        if (ArrayUtils.contains(this.exceptUrl, actionKey)) {
            try {
                chain.doFilter(request, response);
            } catch (Exception e) {
                log.error("filter error", e);
            }
            return;
        }
        this.initInterceptors();
        ServletInvokableHandlerMethod invokableHandlerMethod = null;
        try {
            invokableHandlerMethod = this.getHandler(httpRequest);
            //验证用户需要httpContext 输出脚本
            this.initAttribute(httpRequest, httpResponse, invokableHandlerMethod);
            if (preHandler(httpRequest, httpResponse)) {
                return;
            }
            if (invokableHandlerMethod == null || invokableHandlerMethod.getMethod() == null) {
                log.warn("invokableHandlerMethod is null or method not exist action-key {}", actionKey);
                WebConfigReader configReader = ApplicationContext.getContainer().getBean(WebConfigReader.class);
                String extension = configReader.getTemplateEngineSuffix();
                if (actionKey.endsWith(extension) || actionKey.endsWith(Extension.JSON)) {
                    chain.doFilter(request, response);
                } else {
                    forward(request, response, actionKey);
                }
            } else {
                HandlerAdapter adapter = this.getHandlerAdapter(invokableHandlerMethod);
                adapter.handle(chain, httpRequest, httpResponse, invokableHandlerMethod);
            }
            this.postHandler(httpRequest, httpResponse);
        } catch (Exception e) {
            try {
                errorHandler(httpRequest, httpResponse, invokableHandlerMethod, e);
            } catch (Throwable throwable) {
                throw new ServletException(e);
            }
        } finally {
            //页面渲染完成之后执行
            afterCompletion(httpRequest, httpResponse);
        }
    }

    protected void forward(ServletRequest request, ServletResponse response, String actionKey) throws ServletException, IOException {
        String dispatcherUrl = new UrlAssembler(actionKey).assemble();
        log.debug("dispatcher url is {}", dispatcherUrl);
        RequestDispatcher dispatcher = request.getRequestDispatcher(dispatcherUrl);
        dispatcher.forward(request, response);
    }

    private void errorHandler(HttpServletRequest httpRequest, HttpServletResponse httpResponse, ServletInvokableHandlerMethod invocableHandlerMethod, Exception e) throws IOException, NotTryException {
        Throwable target = e;
        if (e.getCause() == null) {
            log.error("e.getCause==null", e);
        } else {
            target = e.getCause();
            log.error("e.getCause!=null", e.getCause());
        }
        if (invocableHandlerMethod != null) {
            invocableHandlerMethod.getMethodReturnValueResolverHandler().errorResolve(target, httpRequest, httpResponse);
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
                log.error(handlerInterceptor.getClass().getName() + "interception after handler error", ex);
            }
        }
    }

    private void postHandler(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        for (HandlerInterceptor handlerInterceptor : handlerInterceptorList) {
            try {
                handlerInterceptor.postHandle(httpRequest, httpResponse);
            } catch (Exception ex) {
                log.error(handlerInterceptor.getClass().getName() + "interception post handler error", ex);
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
                log.error(handlerInterceptor.getClass().getName() + "interception pre handler error", e);
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
        throw new ServletException("No adapter for handler [" + handler + "]: The DispatcherServlet configuration needs to include a HandlerAdapter that supports this handler");
    }

    private void initAttribute(HttpServletRequest request, HttpServletResponse response, ServletInvokableHandlerMethod invokableHandlerMethod) {
        request.setAttribute(Constant.REQUEST_INVOKABLE_HANDLER_METHOD, invokableHandlerMethod);
        if (servletUtility.include(request)) {
            return;
        }
        String actionKey = servletUtility.getActionKey(request);
        log.debug("PARAMETERS:" + servletUtility.getAllParameter(request));
        log.debug("ACTION KEY:" + actionKey);

        Pair<String, Map<String, Object>> sessionPair = (Pair<String, Map<String, Object>>) request.getSession().getAttribute(Constant.FLASH_KEY);
        if (sessionPair == null) {
            return;
        }

        if (new UrlMatcher(sessionPair.getFirst(), actionKey).match(request)) {
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
     * 初始化所有策略
     */
    protected void initStrategies() {
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
    protected void initAdapter() {
        this.handlerAdapters = new ArrayList<HandlerAdapter>();
        MethodControllerHandlerAdapter adapter = new MethodControllerHandlerAdapter();
        this.handlerAdapters.add(adapter);
    }

    @Override
    public void init(FilterConfig config) {
        this.config = config;
        this.container = ApplicationContext.getContainer();
        this.cookieUtility = this.container.getBean(CookieUtility.class);
        this.connectionContextHolder = this.container.getBean(ConnectionContextHolder.class);
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
