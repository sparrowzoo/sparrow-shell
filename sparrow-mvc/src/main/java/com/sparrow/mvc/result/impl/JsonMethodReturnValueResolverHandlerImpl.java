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

import com.sparrow.core.spi.JsonFactory;
import com.sparrow.mvc.ServletInvokableHandlerMethod;
import com.sparrow.mvc.result.MethodReturnValueResolverHandler;
import com.sparrow.mvc.result.ResultAssembler;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.Result;
import com.sparrow.protocol.constant.Constant;
import com.sparrow.protocol.constant.Extension;
import com.sparrow.protocol.constant.SparrowError;
import com.sparrow.protocol.constant.magic.Symbol;
import com.sparrow.support.web.HttpContext;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JsonMethodReturnValueResolverHandlerImpl implements MethodReturnValueResolverHandler {

    public JsonMethodReturnValueResolverHandlerImpl() {
    }

    @Override
    public boolean support(ServletInvokableHandlerMethod executionChain) {
        return executionChain.getActionName().endsWith(Extension.JSON);
    }

    @Override
    public void errorResolve(Throwable exception, HttpServletRequest request,
        HttpServletResponse response) throws IOException {
        response.setHeader("Content-Type", Constant.CONTENT_TYPE_JSON);
        if (exception instanceof BusinessException) {
            Result result = ResultAssembler.assemble((BusinessException) exception, null);
            response.getWriter().write(JsonFactory.getProvider().toString(result));
            return;
        }
        //业务异常
        if (exception.getCause() != null && exception.getCause() instanceof BusinessException) {
            Result result = ResultAssembler.assemble((BusinessException) exception.getCause(), null);
            response.getWriter().write(JsonFactory.getProvider().toString(result));
            return;
        }
        Result result = Result.fail(new BusinessException(SparrowError.SYSTEM_SERVER_ERROR));
        //result.setError();
        response.getWriter().write(JsonFactory.getProvider().toString(result));
    }

    @Override
    public void resolve(ServletInvokableHandlerMethod handlerExecutionChain, Object returnValue, FilterChain chain,
        HttpServletRequest request,
        HttpServletResponse response) throws IOException {
        response.setHeader("Content-Type", Constant.CONTENT_TYPE_JSON);
        if (returnValue == null) {
            returnValue = Symbol.EMPTY;
        }
        try {
            Result result = new Result(returnValue);
            response.getWriter().write(JsonFactory.getProvider().toString(result));
        } finally {
            HttpContext.getContext().remove();
        }
    }
}
