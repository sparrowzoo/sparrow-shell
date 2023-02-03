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

import com.sparrow.constant.Config;
import com.sparrow.protocol.constant.Extension;
import com.sparrow.utility.ConfigUtility;
import java.io.IOException;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

public class ThymeleafEngineUtils {
    private static TemplateEngine templateEngine;

    private static ServletContext innerServletContext;

    public static void initEngine(ServletContext servletContext) {
        if (templateEngine != null) {
            return;
        }
        synchronized (ThymeleafEngineUtils.class) {
            if (templateEngine != null) {
                return;
            }
            innerServletContext = servletContext;
            ServletContextTemplateResolver resolver = new ServletContextTemplateResolver(servletContext);
            String extension = ConfigUtility.getValue(Config.DEFAULT_PAGE_EXTENSION, Extension.HTML);
            String pagePrefix = ConfigUtility.getValue(Config.DEFAULT_PAGE_PREFIX, "/template");
            resolver.setPrefix(pagePrefix);
            resolver.setSuffix(extension);
            resolver.setTemplateMode(TemplateMode.HTML);
            resolver.setCacheable(false);
            templateEngine = new TemplateEngine();
            templateEngine.setTemplateResolver(resolver);
        }
    }

    public static void forward(ServletRequest request, ServletResponse response,
        String actionKey) throws IOException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        WebContext ctx = new WebContext(httpServletRequest, httpServletResponse, innerServletContext, request.getLocale());
        // 1.设置响应体内容类型和字符集
        httpServletResponse.setContentType("text/html;charset=UTF-8");
        //webContext.setVariable("variable-name", "variable-value");
        templateEngine.process(actionKey, ctx, response.getWriter());
    }
}
