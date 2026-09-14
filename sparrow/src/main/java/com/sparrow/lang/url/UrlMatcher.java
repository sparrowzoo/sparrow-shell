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

package com.sparrow.lang.url;

import jakarta.servlet.http.HttpServletRequest;

public class UrlMatcher {
    private String source;
    private String target;

    public UrlMatcher(String source, String target) {
        this.target = target;
        this.source = source;
    }

    /**
     * source  -->/template/action-url.html
     * <p>
     * direct mode action url-->action-url
     * <p>
     * <p>
     * transit mode transit url--> transit-url?action_url
     */
    public boolean match(HttpServletRequest request) {
        //直接匹配
        if (source.equalsIgnoreCase(target)) {
            return true;
        }
        //是否和最终的url 匹配
        String actualActionUrl = new UrlAssembler(target).assemble();
        if (source.equalsIgnoreCase(actualActionUrl)) {
            return true;
        }

        //transit final  url
        target = request.getQueryString();
        if (target == null) {
            return false;
        }
        actualActionUrl = new UrlAssembler(target).assemble();
        if (source.equalsIgnoreCase(actualActionUrl)) {
            return true;
        }
        return false;
    }
}
