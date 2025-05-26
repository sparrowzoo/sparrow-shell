package com.sparrow.interceptor;


import com.sparrow.servlet.HandlerInterceptor;

import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by harry on 2018/2/1.
 */
@Named
public class SparrowHandlerInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("开始拦截，没截住....");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("处理完了");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("这里到最后了，finally");
    }
}
