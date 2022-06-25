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

package com.sparrow.mvc.resolver.impl;

import com.sparrow.mvc.ServletInvokableHandlerMethod;
import com.sparrow.mvc.resolver.HandlerMethodArgumentResolver;
import com.sparrow.web.support.MethodParameter;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HandlerMethodArgumentResolverComposite implements HandlerMethodArgumentResolver {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private final List<HandlerMethodArgumentResolver> argumentResolvers =
        new LinkedList<HandlerMethodArgumentResolver>();

    private final Map<MethodParameter, HandlerMethodArgumentResolver> argumentResolverCache =
        new ConcurrentHashMap<MethodParameter, HandlerMethodArgumentResolver>(256);

    public HandlerMethodArgumentResolverComposite addResolver(HandlerMethodArgumentResolver resolver) {
        this.argumentResolvers.add(resolver);
        return this;
    }

    public HandlerMethodArgumentResolverComposite addResolvers(
        List<? extends HandlerMethodArgumentResolver> resolvers) {
        if (resolvers != null) {
            for (HandlerMethodArgumentResolver resolver : resolvers) {
                this.argumentResolvers.add(resolver);
            }
        }
        return this;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return true;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ServletInvokableHandlerMethod executionChain,
        HttpServletRequest request) throws Exception {
        HandlerMethodArgumentResolver resolver = this.argumentResolverCache.get(parameter);
        if (resolver != null) {
            return resolver.resolveArgument(parameter, executionChain, request);
        }
        //先从request resolver
        Object result = null;
        for (HandlerMethodArgumentResolver methodArgumentResolver : this.argumentResolvers) {
            if (methodArgumentResolver.supportsParameter(parameter)) {
                result = methodArgumentResolver.resolveArgument(parameter, executionChain, request);
                if (result != null) {
                    this.argumentResolverCache.put(parameter, methodArgumentResolver);
                    return result;
                }
            }
        }
        logger.warn("[{}] can't parse please check parameter name is correct", parameter.getParameterName());
        return null;
    }
}