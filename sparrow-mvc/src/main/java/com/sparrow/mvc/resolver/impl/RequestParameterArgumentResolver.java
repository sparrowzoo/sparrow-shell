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

package com.sparrow.mvc.resolver.impl;

import com.sparrow.cg.MethodAccessor;
import com.sparrow.container.Container;
import com.sparrow.container.ContainerAware;
import com.sparrow.core.TypeConverter;
import com.sparrow.core.spi.ApplicationContext;
import com.sparrow.mvc.ServletInvokableHandlerMethod;
import com.sparrow.mvc.resolver.HandlerMethodArgumentResolver;
import com.sparrow.protocol.POJO;
import com.sparrow.protocol.constant.magic.Symbol;
import com.sparrow.utility.ClassUtility;
import com.sparrow.utility.HtmlUtility;
import com.sparrow.utility.StringUtility;
import com.sparrow.web.support.MethodParameter;

import java.util.List;
import javax.servlet.http.HttpServletRequest;

public class RequestParameterArgumentResolver implements HandlerMethodArgumentResolver, ContainerAware {

    private Container container = ApplicationContext.getContainer();

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return true;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ServletInvokableHandlerMethod executionChain,
        HttpServletRequest request) throws Exception {
        String parameterName = methodParameter.getParameterName();
        String[] parameters = null;
        String parameter = null;
        if (POJO.class.isAssignableFrom(methodParameter.getParameterType())) {
            Object entity;
            try {
                entity = methodParameter.getParameterType().newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            MethodAccessor methodAccessor = container
                .getProxyBean(methodParameter.getParameterType());

            List<TypeConverter> methods = container.getFieldList(methodParameter.getParameterType());
            for (TypeConverter field : methods) {
                String parameterName4Request = StringUtility.setFirstByteLowerCase(field.getName());
                parameter = request.getParameter(parameterName4Request);

                if (parameter == null) {
                    String entityName = ClassUtility.getEntityNameByClass(methodParameter.getParameterType());
                    parameter = request.getParameter(entityName + "." + parameterName4Request);
                }

                if (parameter == null) {
                    continue;
                }
                if (executionChain.isValidateRequest() && field.getType().equals(String.class)) {
                    parameter = HtmlUtility.encode(parameter);
                }
                methodAccessor.set(entity, field.getName(), field.convert(parameter));
            }
            return entity;
        }

        if (methodParameter.getParameterType().equals(String[].class)) {
            parameters = request.getParameterValues(methodParameter.getParameterName());
            if (executionChain.isValidateRequest()) {
                for (int p = 0; p < parameters.length; p++) {
                    parameters[p] = HtmlUtility.encode(parameters[p]);
                }
            }
            return parameters;
        }

        parameter = request.getParameter(methodParameter.getParameterName());
        if (methodParameter.getParameterType().equals(String.class)) {
            if (StringUtility.isNullOrEmpty(parameter)) {
                return Symbol.EMPTY;
            }
            if (executionChain.isValidateRequest()) {
                parameter = HtmlUtility.encode(parameter);
            }
            return parameter;
        }
        return new TypeConverter(parameterName, methodParameter.getParameterType()).convert(parameter);
    }

    @Override
    public void aware(Container container, String beanName) {
        this.container = container;
    }
}
