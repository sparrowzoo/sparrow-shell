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

import com.sparrow.container.Container;
import com.sparrow.container.ContainerAware;
import com.sparrow.core.TypeConverter;
import com.sparrow.core.spi.ApplicationContext;
import com.sparrow.mvc.ServletInvokableHandlerMethod;
import com.sparrow.mvc.resolver.HandlerMethodArgumentResolver;
import com.sparrow.protocol.constant.magic.Symbol;
import com.sparrow.support.web.ServletUtility;
import com.sparrow.utility.HtmlUtility;
import com.sparrow.utility.RegexUtility;
import com.sparrow.utility.StringUtility;
import com.sparrow.web.support.MethodParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PathParameterArgumentResolver implements HandlerMethodArgumentResolver, ContainerAware {

    private Logger logger = LoggerFactory.getLogger(PathParameterArgumentResolver.class);

    private Container container = ApplicationContext.getContainer();

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return true;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ServletInvokableHandlerMethod executionChain,
        HttpServletRequest request) throws Exception {

        if (StringUtility.isNullOrEmpty(executionChain.getActionRegex())) {
            return null;
        }
        List<String> pathParameterNameList = executionChain.getPathParameterNameList();
        if (pathParameterNameList == null) {
            logger.warn("path parameter name list is null");
            return null;
        }
        Map<String, String> pathParameterValueMap = new HashMap<String, String>(pathParameterNameList.size());
        //actual url
        String currentPath = ServletUtility.getInstance().getActionKey(request);

        List<List<String>> lists = RegexUtility.multiGroups(currentPath, executionChain.getActionRegex());
        for (List<String> list : lists) {
            for (String parameter : list) {
                pathParameterValueMap.put(pathParameterNameList.get(pathParameterValueMap.size()), parameter);
            }
        }

        String parameter = pathParameterValueMap.get(methodParameter.getParameterName());
        if (methodParameter.getParameterType().equals(String.class)) {
            if (StringUtility.isNullOrEmpty(parameter)) {
                return Symbol.EMPTY;
            }
            if (executionChain.isValidateRequest()) {
                parameter = HtmlUtility.encode(parameter);
            }
            return parameter;
        }
        return new TypeConverter(methodParameter.getParameterName(), methodParameter.getParameterType()).convert(parameter);
    }

    @Override
    public void aware(Container container, String beanName) {
        this.container = container;
    }
}
