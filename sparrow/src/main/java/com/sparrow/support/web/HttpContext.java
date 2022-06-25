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

package com.sparrow.support.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class HttpContext {
    private static HttpContext context = new HttpContext();

    public static HttpContext getContext() {
        return context;
    }

    /**
     * 请求过程中的参数
     */
    private ThreadLocal<Map<String, Object>> holder = new ThreadLocal<Map<String, Object>>();

    public Map<String, Object> getHolder() {
        if (this.holder.get() == null) {
            this.holder.set(new HashMap<String, Object>());
        }
        return this.holder.get();
    }

    public void remove() {
        Map<String, Object> values = this.holder.get();
        if (values != null) {
            for (String key : values.keySet()) {
                this.request.get().setAttribute(key, values.get(key));
            }
        }
        this.holder.remove();
    }

    public Object get(String key) {
        return this.getHolder().get(key);
    }

    public void put(String key, Object value) {
        if (value == null) {
            return;
        }
        this.getHolder().put(key, value);
    }

    public void setRequest(HttpServletRequest request) {
        this.request.set(request);
    }

    public void setResponse(HttpServletResponse response) {
        this.response.set(response);
    }

    /**
     * 接收到的request对象
     */
    private ThreadLocal<HttpServletRequest> request = new ThreadLocal<HttpServletRequest>();
    /**
     * 接收到的response对象
     */
    private ThreadLocal<HttpServletResponse> response = new ThreadLocal<HttpServletResponse>();

    public HttpServletRequest getRequest() {
        return request.get();
    }

    public HttpServletResponse getResponse() {
        return response.get();
    }

    public void removeRequest() {
        request.remove();
    }

    public void removeResponse() {
        response.remove();
    }

    public void execute(String script) {
        script = "<script type=\"text/javascript\">" + script + "</script>";
        PrintWriter out = null;
        try {
            out = response.get().getWriter();
            out.println(script);
        } catch (IOException ignore) {
        }
    }
}
