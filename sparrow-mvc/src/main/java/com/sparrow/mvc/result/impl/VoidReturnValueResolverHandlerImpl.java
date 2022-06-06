package com.sparrow.mvc.result.impl;

import com.sparrow.constant.SparrowError;
import com.sparrow.core.spi.JsonFactory;
import com.sparrow.mvc.ServletInvokableHandlerMethod;
import com.sparrow.mvc.result.MethodReturnValueResolverHandler;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.Result;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class VoidReturnValueResolverHandlerImpl implements MethodReturnValueResolverHandler {
    @Override
    public void resolve(ServletInvokableHandlerMethod handlerExecutionChain, Object returnValue, FilterChain chain, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

    }

    @Override
    public boolean support(ServletInvokableHandlerMethod handlerExecutionChain) {
        return void.class.equals(handlerExecutionChain.getReturnType());
    }

    @Override
    public void errorResolve(Throwable exception, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (exception.getCause() != null && exception.getCause() instanceof BusinessException) {
            Result result = Result.FAIL((BusinessException) exception.getCause());
            response.getWriter().write(JsonFactory.getProvider().toString(result));
            return;
        }
        Result result = Result.FAIL(new BusinessException(SparrowError.SYSTEM_SERVER_ERROR));
        response.getWriter().write(JsonFactory.getProvider().toString(result));
    }
}
