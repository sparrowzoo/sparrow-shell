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

import com.sparrow.enums.LoginType;
import com.sparrow.mvc.resolver.impl.HandlerMethodArgumentResolverComposite;
import com.sparrow.mvc.result.MethodReturnValueResolverHandler;
import com.sparrow.utility.CollectionsUtility;
import com.sparrow.web.support.MethodParameter;
import java.lang.reflect.Method;
import java.util.List;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ServletInvokableHandlerMethod {
    /**
     * 登录类型 0:不需要认证 1:正常登录 2:框架内登录 default.jsp内登录 3:管理员登录 4:对话框登录 5:json 提示
     */
    private LoginType loginType = LoginType.NO_AUTHENTICATE;
    /**
     * 是否为json返回
     */
    private boolean json = false;
    /**
     * 出错返回的默认url
     */
    private String errorUrl;
    /**
     * 成功返回的默认url
     */
    private String successUrl;
    /**
     * 具体的执行方法
     */
    private Method method;
    /**
     * action 匹配url
     */
    private String actionName;

    /**
     * action regex 通过actionName 匹配获取
     */
    private String actionRegex;

    /**
     * path通过regex 匹配到的参数名称
     */
    private List<String> pathParameterNameList;
    /**
     * 是否需要授权
     */
    private boolean needAuthorizing;
    /**
     * 是否需要权限验证
     */
    private boolean validateRequest = true;
    /**
     * 参数名称列表
     */
    private List<String> parameterNameList;
    /**
     * 返回值类型
     */
    private Class returnType;
    /**
     * 具体的controller对象
     */
    private Object controller;
    /**
     * 具体的 controller 类
     */
    private Class<?> controllerClazz;
    /**
     * 方法参封装列表
     */
    private MethodParameter[] methodParameters;

    /**
     * action的参数解析对象 参数会多个
     */
    private HandlerMethodArgumentResolverComposite handlerMethodArgumentResolverComposite;
    /**
     * action 的返回值解析对象 只有一个
     */
    private MethodReturnValueResolverHandler methodReturnValueResolverHandler;

    public Class getReturnType() {
        return returnType;
    }

    public boolean isValidateRequest() {
        return validateRequest;
    }

    public void setValidateRequest(boolean validateRequest) {
        this.validateRequest = validateRequest;
    }

    public List<String> getParameterNameList() {
        return parameterNameList;
    }

    public void setParameterNameList(List<String> parameterNameList) {
        this.parameterNameList = parameterNameList;
    }

    public String getSuccessUrl() {
        return successUrl;
    }

    public void setSuccessUrl(String successUrl) {
        this.successUrl = successUrl;
    }

    public String getErrorUrl() {
        return errorUrl;
    }

    public void setErrorUrl(String errorUrl) {
        this.errorUrl = errorUrl;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
        this.returnType = this.method.getReturnType();
        this.methodParameters = this.initMethodParameters();
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public LoginType getLoginType() {
        return loginType;
    }

    public void setLoginType(LoginType loginType) {
        this.loginType = loginType;
    }

    public boolean isNeedAuthorizing() {
        return needAuthorizing;
    }

    public void setNeedAuthorizing(boolean needAuthorizing) {
        this.needAuthorizing = needAuthorizing;
    }

    public boolean isJson() {
        return json;
    }

    public void setJson(boolean ajax) {
        this.json = ajax;
    }

    private MethodParameter[] initMethodParameters() {
        Class<?>[] parameterClass = this.method
            .getParameterTypes();
        if (parameterClass == null || parameterClass.length == 0) {
            return null;
        }
        int count = parameterClass.length;
        MethodParameter[] result = new MethodParameter[count];
        for (int i = 0; i < count; i++) {
            String parameterName = null;
            if (!CollectionsUtility.isNullOrEmpty(parameterNameList) && parameterNameList.size() > i) {
                parameterName = parameterNameList.get(i);
            }
            result[i] = new MethodParameter(method, i, parameterClass[i], parameterName);
        }
        return result;
    }

    public Object invokeAndHandle(FilterChain chain, HttpServletRequest request,
        HttpServletResponse response) throws Exception {
        Object[] args = getMethodArgumentValues(request, response);
        Object returnValue = this.method.invoke(this.controller, args);
        methodReturnValueResolverHandler.resolve(this, returnValue, chain, request, response);
        return returnValue;
    }

    private Object[] getMethodArgumentValues(HttpServletRequest request,
        HttpServletResponse response) throws Exception {
        MethodParameter[] parameters = this.methodParameters;
        if (this.methodParameters == null || this.methodParameters.length == 0) {
            return null;
        }
        Object[] args = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            MethodParameter parameter = parameters[i];
            if (parameter.getParameterType().equals(HttpServletRequest.class)) {
                args[i] = request;
                continue;
            }
            if (parameter.getParameterType().equals(HttpServletResponse.class)) {
                args[i] = response;
                continue;
            }
            if (this.handlerMethodArgumentResolverComposite.supportsParameter(parameter)) {
                args[i] = this.handlerMethodArgumentResolverComposite.resolveArgument(
                    parameter, this, request);
            }
        }
        return args;
    }

    public void setReturnType(Class returnType) {
        this.returnType = returnType;
    }

    public Object getController() {
        return controller;
    }

    public void setController(Object controller) {
        this.controller = controller;
        this.controllerClazz = controller.getClass();
    }

    public Class<?> getControllerClazz() {
        return controllerClazz;
    }

    public MethodParameter[] getMethodParameters() {
        return methodParameters;
    }

    public HandlerMethodArgumentResolverComposite getHandlerMethodArgumentResolverComposite() {
        return handlerMethodArgumentResolverComposite;
    }

    public void setHandlerMethodArgumentResolverComposite(
        HandlerMethodArgumentResolverComposite handlerMethodArgumentResolverComposite) {
        this.handlerMethodArgumentResolverComposite = handlerMethodArgumentResolverComposite;
    }

    public MethodReturnValueResolverHandler getMethodReturnValueResolverHandler() {
        return methodReturnValueResolverHandler;
    }

    public void setMethodReturnValueResolverHandler(MethodReturnValueResolverHandler methodReturnValueResolverHandler) {
        this.methodReturnValueResolverHandler = methodReturnValueResolverHandler;
    }

    public String getActionRegex() {
        return actionRegex;
    }

    public void setActionRegex(String actionRegex) {
        this.actionRegex = actionRegex;
    }

    public List<String> getPathParameterNameList() {
        return pathParameterNameList;
    }

    public void setPathParameterNameList(List<String> pathParameterNameList) {
        this.pathParameterNameList = pathParameterNameList;
    }
}
