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

package com.sparrow.test.lang;

import com.sparrow.core.cache.Cache;
import com.sparrow.core.cache.StringSoftExpirableCache;
import com.sparrow.lang.url.UrlAssembler;
import com.sparrow.lang.url.UrlMatcher;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.*;

public class UrlTest {
    public static void main(String[] args) {
        convert("action");
        System.out.println(new UrlMatcher("/templates/action.html", "action").match(new RequestTest()));
        System.out.println(new UrlMatcher("/templates/action.html", "action2").match(new RequestTest()));

    }

    private static void convert(String action) {
        Cache<String, String> cache = new StringSoftExpirableCache("cache", 10);
        System.out.println(new UrlAssembler(action).assemble());
        System.out.println(cache.get(action));
    }
}
