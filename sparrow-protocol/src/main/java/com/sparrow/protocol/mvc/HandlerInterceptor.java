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

package com.sparrow.protocol.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author harry
 */
public interface HandlerInterceptor {

    /**
     * 该方法将在请求处理之前进行调用。 在一个应用中或者说是在一个请求中可以同时存在多个Interceptor 。每个Interceptor 的调用会依据它的声明顺序依次执行， 而且最先执行的都是Interceptor
     * 中的preHandle 方法， 所以可以在这个方法中进行一些前置初始化操作或者是对当前请求的一个预处理， 也可以在这个方法中进行一些判断来决定请求是否要继续进行下去。 该方法的返回值是布尔值Boolean
     * 类型的，当它返回为false 时，表示请求结束，后续的Interceptor 和Controller 都不会再执行； 当返回值为true 时就会继续调用下一个Interceptor 的preHandle 方法，
     * 如果已经是最后一个Interceptor 的时候就会是调用当前请求的Controller 方法。
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    boolean preHandle(HttpServletRequest request, HttpServletResponse response)
        throws Exception;

    /**
     * 由preHandle 方法的解释我们知道这个方法包括后面要说到的afterCompletion 方法都只能是在当前所属的Interceptor 的preHandle 方法的返回值为true 时才能被调用。postHandle
     * 方法，顾名思义就是在当前请求进行处理之后，也就是Controller 方法调用之后执行， 但是它会在DispatcherServlet 进行视图返回渲染之前被调用
     *
     * @param request
     * @param response
     * @throws Exception
     */
    void postHandle(HttpServletRequest request, HttpServletResponse response)
        throws Exception;

    /**
     * 该方法也是需要当前对应的Interceptor 的preHandle 方法的返回值为true 时才会执行。顾名思义， 该方法将在整个请求结束之后， 也就是在DispatcherServlet
     * 渲染了对应的视图之后执行。这个方法的主要作用是用于进行资源清理工作的。
     *
     * @param request
     * @param response
     * @throws Exception
     */
    void afterCompletion(HttpServletRequest request, HttpServletResponse response)
        throws Exception;

}
