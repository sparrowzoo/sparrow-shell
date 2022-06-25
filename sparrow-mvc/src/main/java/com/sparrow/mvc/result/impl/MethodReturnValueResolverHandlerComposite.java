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

import com.sparrow.mvc.ServletInvokableHandlerMethod;
import com.sparrow.mvc.result.MethodReturnValueResolverHandler;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MethodReturnValueResolverHandlerComposite implements MethodReturnValueResolverHandler {
    private static Logger logger = LoggerFactory.getLogger(MethodReturnValueResolverHandlerComposite.class);
    private List<MethodReturnValueResolverHandler> resolverHandlers = new ArrayList<MethodReturnValueResolverHandler>();

    public MethodReturnValueResolverHandlerComposite() {
    }

    @Override
    public boolean support(ServletInvokableHandlerMethod executionChain) {
        for (MethodReturnValueResolverHandler resolverHandler : this.resolverHandlers) {
            if (resolverHandler.support(executionChain)) {
                executionChain.setMethodReturnValueResolverHandler(resolverHandler);
                return true;
            }
        }
        logger.warn("not method return value resolver handler found !");
        return false;
    }

    @Override
    public void errorResolve(Throwable exception, HttpServletRequest request,
        HttpServletResponse response) throws IOException, ServletException {
        return;
    }

    @Override
    public void resolve(ServletInvokableHandlerMethod handlerExecutionChain, Object returnValue, FilterChain chain,
        HttpServletRequest request,
        HttpServletResponse response) throws IOException, ServletException {
        for (MethodReturnValueResolverHandler resolverHandler : this.resolverHandlers) {
            if (resolverHandler.support(handlerExecutionChain)) {
                resolverHandler.resolve(handlerExecutionChain, returnValue, chain, request, response);
                return;
            }
        }
    }

    public MethodReturnValueResolverHandlerComposite addResolvers(
        List<? extends MethodReturnValueResolverHandler> resolvers) {
        if (resolvers != null) {
            for (MethodReturnValueResolverHandler resolver : resolvers) {
                this.resolverHandlers.add(resolver);
            }
        }
        return this;
    }
}
