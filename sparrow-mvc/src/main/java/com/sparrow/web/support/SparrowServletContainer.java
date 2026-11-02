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

package com.sparrow.web.support;

import com.sparrow.servlet.impl.AbstractServletContainer;
import com.sparrow.support.web.HttpContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Sparrow framework controller support class that used by sparrow only
 */
public class SparrowServletContainer extends AbstractServletContainer {
    private static SparrowServletContainer servletContainer = new SparrowServletContainer();

    public static SparrowServletContainer getInstance() {
        return servletContainer;
    }

    private HttpContext httpContext = HttpContext.getContext();

    @Override
    public HttpServletRequest getRequest() {
        return HttpContext.getContext().getRequest();
    }

    @Override
    public HttpServletResponse getResponse() {
        return HttpContext.getContext().getResponse();
    }

    public void clear() {
        httpContext.remove();
    }

    public <T> T get(String key) {
        return (T) httpContext.get(key);
    }
}
