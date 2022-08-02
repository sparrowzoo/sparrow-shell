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
package com.sparrow.interceptor;

import com.sparrow.servlet.HandlerInterceptor;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named
public class SparrowHandlerInterceptor implements HandlerInterceptor {
    private static Logger logger = LoggerFactory.getLogger(SparrowHandlerInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response)
        throws Exception {
        logger.info("开始拦截，没截住....");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response)
        throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response)
        throws Exception {
        logger.info("这里到最后了，finally");
    }
}
