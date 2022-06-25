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

package com.sparrow.mvc.result.impl;

import com.sparrow.constant.Config;
import com.sparrow.constant.SparrowError;
import com.sparrow.core.Pair;
import com.sparrow.mvc.ServletInvokableHandlerMethod;
import com.sparrow.mvc.result.MethodReturnValueResolverHandler;
import com.sparrow.mvc.result.ResultErrorAssembler;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.Result;
import com.sparrow.protocol.VO;
import com.sparrow.protocol.constant.Constant;
import com.sparrow.protocol.constant.magic.Symbol;
import com.sparrow.mvc.PageSwitchMode;
import com.sparrow.mvc.ViewWithModel;
import com.sparrow.support.web.HttpContext;
import com.sparrow.support.web.ServletUtility;
import com.sparrow.utility.ClassUtility;
import com.sparrow.utility.CollectionsUtility;
import com.sparrow.utility.ConfigUtility;
import com.sparrow.utility.StringUtility;

import java.io.IOException;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ViewWithModelMethodReturnValueResolverHandlerImpl implements MethodReturnValueResolverHandler {

    private ServletUtility servletUtility = ServletUtility.getInstance();

    public ViewWithModelMethodReturnValueResolverHandlerImpl() {
    }

    @Override
    public boolean support(ServletInvokableHandlerMethod executionChain) {
        return executionChain.getReturnType().equals(ViewWithModel.class) || executionChain.getReturnType().equals(String.class);
    }

    private void flash(HttpServletRequest request, String flashUrl, String key, Object o) {
        Map<String, Object> values = HttpContext.getContext().getHolder();
        if (o != null) {
            values.put(key, o);
        }
        Pair<String, Map<String, Object>> sessionMap = Pair.create(flashUrl, values);
        request.getSession().setAttribute(Constant.FLASH_KEY, sessionMap);
        HttpContext.getContext().remove();
    }

    /**
     * 根据返回结果判断url
     *
     * @param actionResult      direct:login
     *                          <p>
     *                          direct:login.jsp
     *                          <p>
     *                          direct:success
     *                          <p>
     *                          login
     *                          <p>
     *                          login.jsp success
     * @param referer           当前refer
     * @param defaultSuccessUrl 默认成功页
     */
    private ViewWithModel parse(String actionResult, String referer, String defaultSuccessUrl) {
        String url;
        PageSwitchMode pageSwitchMode = PageSwitchMode.REDIRECT;
        //手动返回url
        if (actionResult.contains(Symbol.COLON)) {
            Pair<String, String> switchModeAndUrl = Pair.split(actionResult, Symbol.COLON);
            pageSwitchMode = PageSwitchMode.valueOf(switchModeAndUrl.getFirst().toUpperCase());
            url = switchModeAndUrl.getSecond();
        } else {
            url = actionResult;
        }
        url = assembleUrl(referer, defaultSuccessUrl, url, pageSwitchMode, null);
        switch (pageSwitchMode) {
            case REDIRECT:
                return ViewWithModel.redirect(url);
            case TRANSIT:
                return ViewWithModel.transit(url);
            case FORWARD:
            default:
                return ViewWithModel.forward(url);
        }
    }

    private String assembleUrl(String referer, String defaultSuccessUrl, String url, PageSwitchMode pageSwitchMode,
        String[] urlArgs) {
        if (Constant.SUCCESS.equals(url) || StringUtility.isNullOrEmpty(url)) {
            url = defaultSuccessUrl;
        }

        if (StringUtility.isNullOrEmpty(url)) {
            url = referer;
        }

        if (StringUtility.isNullOrEmpty(url)) {
            return null;
        }
        //index-->/index
        if (!url.startsWith(Constant.HTTP_PROTOCOL) && !url.startsWith(Constant.HTTPS_PROTOCOL) && !url.startsWith(Symbol.SLASH)) {
            url = Symbol.SLASH + url;
        }

        // /index-->template/index.jsp
        if (PageSwitchMode.FORWARD.equals(pageSwitchMode) && !url.contains(Symbol.DOT)) {
            url = servletUtility.assembleActualUrl(url);
        }

        if (CollectionsUtility.isNullOrEmpty(urlArgs)) {
            return url;
        }
        for (int i = 0; i < urlArgs.length; i++) {
            if (urlArgs[i] != null) {
                url = url.replace(Symbol.DOLLAR, Symbol.AND).replace(
                    "{" + i + "}", urlArgs[i]);
            }
        }
        return url;
    }

    @Override
    public void resolve(ServletInvokableHandlerMethod handlerExecutionChain, Object returnValue, FilterChain chain,
        HttpServletRequest request,
        HttpServletResponse response) throws IOException, ServletException {
        String referer = servletUtility.referer(request);
        ViewWithModel viewWithModel = null;

        String url = null;
        if (returnValue instanceof String) {
            viewWithModel = this.parse((String) returnValue, servletUtility.referer(request), handlerExecutionChain.getSuccessUrl());
            url = viewWithModel.getUrl();
        } else if (returnValue instanceof ViewWithModel) {
            viewWithModel = (ViewWithModel) returnValue;
            url = this.assembleUrl(referer, handlerExecutionChain.getSuccessUrl(), viewWithModel.getUrl(), viewWithModel.getSwitchMode(), viewWithModel.getUrlArgs());
        }

        //无返回值，直接返回 不处理
        if (viewWithModel == null) {
            chain.doFilter(request, response);
            return;
        }

        if (url == null) {
            //兼容默认首页 /template/index.jsp 的场景
            VO data = viewWithModel.getVo();
            if (data != null) {
                request.setAttribute(ClassUtility.getEntityNameByClass(data.getClass()), data);
            }
            chain.doFilter(request, response);
            return;
        }

        String flashUrl;
        String rootPath = ConfigUtility.getValue(Config.ROOT_PATH);
        switch (viewWithModel.getSwitchMode()) {
            case REDIRECT:
                flashUrl = servletUtility.assembleActualUrl(url);
                this.flash(request, flashUrl, Constant.FLASH_SUCCESS_RESULT, viewWithModel.getVo());
                if (!url.startsWith(Constant.HTTP_PROTOCOL)) {
                    url = rootPath + url;
                }
                response.sendRedirect(url);
                break;
            case TRANSIT:
                flashUrl = servletUtility.assembleActualUrl(url);
                this.flash(request, flashUrl, Constant.FLASH_SUCCESS_RESULT, viewWithModel.getVo());
                String transitUrl = viewWithModel.getTransitUrl();
                if (StringUtility.isNullOrEmpty(transitUrl)) {
                    transitUrl = ConfigUtility.getValue(Config.TRANSIT_URL);
                }
                if (transitUrl != null && !transitUrl.startsWith(Constant.HTTP_PROTOCOL)) {
                    transitUrl = rootPath + transitUrl;
                }

                if (!url.startsWith(Constant.HTTP_PROTOCOL)) {
                    url = rootPath + url;
                }
                response.sendRedirect(transitUrl + "?" + url);
                break;
            case FORWARD:
                if (rootPath != null && url.startsWith(rootPath)) {
                    url = url.substring(rootPath.length());
                }
                VO data = viewWithModel.getVo();
                if (data != null) {
                    request.setAttribute(ClassUtility.getEntityNameByClass(data.getClass()), data);
                }
                RequestDispatcher dispatcher = request.getRequestDispatcher(url);
                dispatcher.forward(request, response);
                break;
            default:
        }
    }

    @Override
    public void errorResolve(Throwable exception,
        HttpServletRequest request,
        HttpServletResponse response) throws IOException, ServletException {

        PageSwitchMode errorPageSwitch = PageSwitchMode.REDIRECT;
        String exceptionSwitchMode = ConfigUtility.getValue(Config.EXCEPTION_SWITCH_MODE);
        if (!StringUtility.isNullOrEmpty(exceptionSwitchMode)) {
            errorPageSwitch = PageSwitchMode.valueOf(exceptionSwitchMode.toUpperCase());
        }
        BusinessException businessException = null;
        //业务异常
        if (exception instanceof BusinessException) {
            businessException = (BusinessException) exception;
        } else {
            businessException = new BusinessException(SparrowError.SYSTEM_SERVER_ERROR);
        }
        Result result = ResultErrorAssembler.assemble(businessException, null);
        String flashUrl;
        switch (errorPageSwitch) {
            case FORWARD:
            case REDIRECT:
                String url = ConfigUtility.getValue(Config.ERROR_URL);
                if (StringUtility.isNullOrEmpty(url)) {
                    url = "/500";
                }
                flashUrl = servletUtility.assembleActualUrl(url);
                this.flash(request, flashUrl, Constant.FLASH_EXCEPTION_RESULT, result);
                response.sendRedirect(url);
                break;
        }
    }
}
