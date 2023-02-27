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
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.dialect.SpringStandardDialect;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

public class ThymeleafEngineUtils {
    private static TemplateEngine templateEngine;

    private static ServletContext innerServletContext;

    /**
     * https://www.thymeleaf.org/doc/tutorials/2.1/thymeleafspring.html
     *
     * @param filterConfig
     */
    public static void initEngine(FilterConfig filterConfig) {
        if (templateEngine != null) {
            return;
        }
        synchronized (ThymeleafEngineUtils.class) {
            if (templateEngine != null) {
                return;
            }

            ServletContext servletContext = filterConfig.getServletContext();
            String exp = filterConfig.getInitParameter("template_expression");
//            if ("spring".equals(exp)) {
//                SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
//                templateResolver.setPrefix("/WEB-INF/templates/");
//                templateResolver.setSuffix(".html");
//                // HTML is the default value, added here for the sake of clarity.
//                templateResolver.setTemplateMode(TemplateMode.HTML);
//                // Template cache is true by default. Set to false if you want
//                // templates to be automatically updated when modified.
//                templateResolver.setCacheable(true);
//                // SpringTemplateEngine automatically applies SpringStandardDialect and
//                // enables Spring's own MessageSource message resolution mechanisms.
//                SpringTemplateEngine springTemplateEngine = new SpringTemplateEngine();
//                springTemplateEngine.setTemplateResolver(templateResolver);
//                // Enabling the SpringEL compiler with Spring 4.2.4 or newer can
//                // speed up execution in most scenarios, but might be incompatible
//                // with specific cases when expressions in one template are reused
//                // across different data types, so this flag is "false" by default
//                // for safer backwards compatibility.
//                springTemplateEngine.setEnableSpringELCompiler(true);
//                templateEngine = springTemplateEngine;
//                return;
////            }

            innerServletContext = servletContext;
            ServletContextTemplateResolver resolver = new ServletContextTemplateResolver(servletContext);
            String extension = ConfigUtility.getValue(Config.DEFAULT_PAGE_EXTENSION, Extension.HTML);
            String pagePrefix = ConfigUtility.getValue(Config.DEFAULT_PAGE_PREFIX, "/template");
            resolver.setPrefix(pagePrefix);
            resolver.setSuffix(extension);
            resolver.setTemplateMode(TemplateMode.HTML);
            resolver.setCacheable(false);
            templateEngine = new TemplateEngine();
            if ("spring".equals(exp)) {
                templateEngine.setDialect(new SpringStandardDialect());
            }
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
