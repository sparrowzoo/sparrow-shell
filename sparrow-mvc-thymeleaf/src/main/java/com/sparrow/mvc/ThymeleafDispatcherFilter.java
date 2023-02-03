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

package com.sparrow.mvc;

import com.sparrow.mvc.adapter.impl.MethodControllerHandlerAdapter;
import com.sparrow.mvc.adapter.impl.ThymeleafMethodControllerHandlerAdapter;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class ThymeleafDispatcherFilter extends DispatcherFilter {

    private ServletContext servletContext;

    @Override public void init(FilterConfig config) {
        servletContext = config.getServletContext();
        ThymeleafEngineUtils.initEngine(servletContext);
        super.init(config);
    }

    @Override protected void initAdapter() {
        this.handlerAdapters = new ArrayList(1);
        MethodControllerHandlerAdapter adapter = new ThymeleafMethodControllerHandlerAdapter();
        this.handlerAdapters.add(adapter);
    }

    @Override protected void forward(ServletRequest request, ServletResponse response,
        String actionKey) throws IOException {
        ThymeleafEngineUtils.forward(request, response, actionKey);
    }
}
