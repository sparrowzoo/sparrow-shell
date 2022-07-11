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

package com.sparrow.mvc.adapter.impl;

import com.sparrow.mvc.ServletInvokableHandlerMethod;
import com.sparrow.mvc.adapter.HandlerAdapter;
import com.sparrow.mvc.resolver.HandlerMethodArgumentResolver;
import com.sparrow.mvc.resolver.impl.*;
import com.sparrow.mvc.result.MethodReturnValueResolverHandler;
import com.sparrow.mvc.result.impl.JsonMethodReturnValueResolverHandlerImpl;
import com.sparrow.mvc.result.impl.MethodReturnValueResolverHandlerComposite;
import com.sparrow.mvc.result.impl.ViewWithModelMethodReturnValueResolverHandlerImpl;
import com.sparrow.mvc.result.impl.VoidReturnValueResolverHandlerImpl;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MethodControllerHandlerAdapter implements HandlerAdapter {

    private HandlerMethodArgumentResolverComposite argumentResolverComposite;

    private MethodReturnValueResolverHandlerComposite returnValueResolverHandlerComposite;

    public MethodControllerHandlerAdapter() {
        this.initArgumentResolvers();
        this.initReturnValueResolvers();
    }

    @Override
    public boolean supports(Object handler) {
        return handler instanceof ServletInvokableHandlerMethod;
    }

    @Override
    public Object handle(FilterChain chain, HttpServletRequest request, HttpServletResponse response,
                         Object handler) throws Exception {
        ServletInvokableHandlerMethod invokableHandlerMethod = (ServletInvokableHandlerMethod) handler;
        invokableHandlerMethod.setHandlerMethodArgumentResolverComposite(argumentResolverComposite);
        //返回值处理handler 只会存在一个
        if (!this.returnValueResolverHandlerComposite.support(invokableHandlerMethod)) {
            throw new RuntimeException("return value resolver handler not found");
        }
        invokableHandlerMethod.setController(invokableHandlerMethod.getController());
        return invokableHandlerMethod.invokeAndHandle(chain, request, response);
    }

    @Override
    public long getLastModified(HttpServletRequest request, Object handler) {
        return 0;
    }

    /**
     * 初始化参数解析器
     */
    private void initArgumentResolvers() {
        this.argumentResolverComposite = new HandlerMethodArgumentResolverComposite();
        List<HandlerMethodArgumentResolver> handlerMethodArgumentResolvers = new ArrayList<HandlerMethodArgumentResolver>();
        HandlerMethodArgumentResolver requestParameterArgumentResolver = new RequestParameterArgumentResolver();
        HandlerMethodArgumentResolver pathParameterArgumentResolver = new PathParameterArgumentResolver();
        HandlerMethodArgumentResolver attributeArgumentResolver = new RequestAttributeArgumentResolver();
        HandlerMethodArgumentResolver jsonBodyArgumentResolver = new JsonBodyArgumentResolver();

        handlerMethodArgumentResolvers.add(attributeArgumentResolver);
        handlerMethodArgumentResolvers.add(jsonBodyArgumentResolver);
        handlerMethodArgumentResolvers.add(pathParameterArgumentResolver);
        handlerMethodArgumentResolvers.add(requestParameterArgumentResolver);
        this.argumentResolverComposite.addResolvers(handlerMethodArgumentResolvers);
    }

    /**
     * 初始化返回值解析器
     */
    private void initReturnValueResolvers() {
        this.returnValueResolverHandlerComposite = new MethodReturnValueResolverHandlerComposite();
        List<MethodReturnValueResolverHandler> methodReturnValueResolverHandlers = new ArrayList<MethodReturnValueResolverHandler>();
        MethodReturnValueResolverHandler viewWithModelMethodReturnValueResolverHandler = new ViewWithModelMethodReturnValueResolverHandlerImpl();
        MethodReturnValueResolverHandler jsonMethodReturnValueResolverHandler = new JsonMethodReturnValueResolverHandlerImpl();
        MethodReturnValueResolverHandler voidMethodReturnValueResolverHandler = new VoidReturnValueResolverHandlerImpl();
        methodReturnValueResolverHandlers.add(jsonMethodReturnValueResolverHandler);
        methodReturnValueResolverHandlers.add(viewWithModelMethodReturnValueResolverHandler);
        methodReturnValueResolverHandlers.add(voidMethodReturnValueResolverHandler);
        this.returnValueResolverHandlerComposite.addResolvers(methodReturnValueResolverHandlers);
    }
}
