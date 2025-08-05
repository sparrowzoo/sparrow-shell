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

package com.sparrowzoo.thymeleaf;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.dialect.SpringStandardDialect;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.StringTemplateResolver;

public class CodeGenerator {
    public static void main(String[] args) {
        CodeGenerator generator = new CodeGenerator();
        System.out.println(generator.generateEntity(String.class));
    }

    private final TemplateEngine templateEngine;

    public CodeGenerator() {
        SpringTemplateEngine springTemplateEngine = new SpringTemplateEngine();
        springTemplateEngine.setTemplateResolver(new StringTemplateResolver());
        springTemplateEngine.setDialect(new SpringStandardDialect());
        springTemplateEngine.setEnableSpringELCompiler(true);
        this.templateEngine = springTemplateEngine;
    }

    public String generateEntity(Class<?> clazz) {
        Context ctx = new Context();
        ctx.setVariable("className", clazz.getSimpleName());
        ctx.setVariable("fields", clazz.getDeclaredFields());
        return templateEngine.process("\n" +
                        "public [[${className}]] {\n" +
                        "<div th:remove=\"tag\" th:each=\"field : ${fields}\">\n" +
                        "    private <span th:remove=\"tag\" th:text=\"${field.type.simpleName}\"></span>\n" +
                        "    <span th:remove=\"tag\" th:text=\"${field.name}\"></span>;</div>}\n"
                , ctx);
    }
}
