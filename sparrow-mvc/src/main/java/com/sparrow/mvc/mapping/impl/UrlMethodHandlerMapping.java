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

package com.sparrow.mvc.mapping.impl;

import com.sparrow.constant.Config;
import com.sparrow.container.ConfigReader;
import com.sparrow.container.Container;
import com.sparrow.core.Pair;
import com.sparrow.core.spi.ApplicationContext;
import com.sparrow.enums.LoginType;
import com.sparrow.lang.url.UrlAssembler;
import com.sparrow.mvc.RequestParameters;
import com.sparrow.mvc.ServletInvokableHandlerMethod;
import com.sparrow.mvc.mapping.HandlerMapping;
import com.sparrow.protocol.constant.Constant;
import com.sparrow.protocol.constant.Extension;
import com.sparrow.protocol.constant.magic.Symbol;
import com.sparrow.support.web.ServletUtility;
import com.sparrow.utility.RegexUtility;
import com.sparrow.utility.StringUtility;
import com.sparrow.utility.Xml;
import com.sparrow.xml.DefaultDocumentLoader;
import com.sparrow.xml.DocumentLoader;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class UrlMethodHandlerMapping implements HandlerMapping {

    private ServletUtility servletUtility = ServletUtility.getInstance();

    private Map<String, ServletInvokableHandlerMethod> mapping = new HashMap<String, ServletInvokableHandlerMethod>();

    private Map<String, ServletInvokableHandlerMethod> dynamicMapping = new HashMap<String, ServletInvokableHandlerMethod>();

    public UrlMethodHandlerMapping() {
        this.init();
    }

    private void init() {
        Container container = ApplicationContext.getContainer();
        DocumentLoader documentLoader = new DefaultDocumentLoader();

        ConfigReader configReader = ApplicationContext.getContainer().getBean(ConfigReader.class);
        String xmlConfig = configReader.getValue(Config.MVC_CONFIG);
        if (StringUtility.isNullOrEmpty(xmlConfig)) {
            xmlConfig = "/controller.xml";
        }
        Document document;

        try {
            document = documentLoader.loadDocument(xmlConfig, false);
        } catch (IOException e) {
            log.warn("io exception maybe [{}]file not found", xmlConfig);
            return;
        } catch (SAXException e) {
            log.error("xml sax exception", e);
            return;
        } catch (ParserConfigurationException e) {
            log.error("parser error", e);
            return;
        }

        List<Element> actionElementList = Xml.getElementsByTagName(document, "action");
        for (Element actionElement : actionElementList) {
            String actionName = actionElement.getAttribute("name");
            try {
                ServletInvokableHandlerMethod invokableHandlerMethod = new ServletInvokableHandlerMethod();
                String beanName = ((Element) actionElement.getParentNode()).getAttribute("id");

                invokableHandlerMethod.setActionName(actionName);
                invokableHandlerMethod.setJson(actionName.endsWith(Extension.JSON));
                invokableHandlerMethod.setNeedAuthorizing(Boolean.parseBoolean(actionElement.getAttribute("needAuthorizing")));
                String loginType = actionElement.getAttribute("login");
                String validateRequest = actionElement.getAttribute("validateRequest");
                if (StringUtility.isNullOrEmpty(validateRequest)) {
                    validateRequest = "true";
                }
                LoginType loginTypeEnum = StringUtility.isNullOrEmpty(loginType) ? LoginType.NO_AUTHENTICATE : LoginType.valueOf(loginType);
                invokableHandlerMethod.setValidateRequest(Boolean.parseBoolean(validateRequest));
                invokableHandlerMethod.setLoginType(loginTypeEnum);
                String actionMethodName = actionElement.getAttribute("method");
                Map<String, Method> actionMethodMap = container.getControllerMethod(beanName);
                if (actionMethodMap == null) {
                    log.warn(beanName + " is null");
                    continue;
                }
                Method method = actionMethodMap.get(actionMethodName);
                if (method == null) {
                    mapping.put(actionName, invokableHandlerMethod);
                    continue;
                }
                // 获取所有参数名称列表
                RequestParameters requestParameters = method.getAnnotation(RequestParameters.class);
                if (requestParameters != null) {
                    String[] names = requestParameters.value().split(Symbol.COMMA);
                    List<String> parameterNameList = new ArrayList<String>(names.length);
                    for (String parameter : names) {
                        parameterNameList.add(parameter.trim());
                    }
                    invokableHandlerMethod.setParameterNameList(parameterNameList);
                }
                invokableHandlerMethod.setMethod(method);
                invokableHandlerMethod.setController(container.getBean(beanName));
                NodeList resultList = actionElement.getChildNodes();
                for (int i = 0; i < resultList.getLength(); i++) {
                    if (resultList.item(i).getNodeType() != 1) {
                        continue;
                    }
                    Element resultElement = (Element) resultList.item(i);
                    String resultName = resultElement.getAttribute("name");
                    if (Constant.ERROR.equalsIgnoreCase(resultName.trim())) {
                        invokableHandlerMethod.setErrorUrl(resultElement.getTextContent().trim());
                    } else if (Constant.SUCCESS.equalsIgnoreCase(resultName.trim())) {
                        invokableHandlerMethod.setSuccessUrl(resultElement.getTextContent().trim());
                    }
                }

                if (invokableHandlerMethod.getSuccessUrl() == null) {
                    if (!invokableHandlerMethod.getActionName().contains(".")) {
                        String dispatcherUrl = new UrlAssembler(invokableHandlerMethod.getActionName()).assemble();
                        invokableHandlerMethod.setSuccessUrl(dispatcherUrl);
                    }
                }

                if (actionName.contains(Symbol.BIG_LEFT_PARENTHESIS) && actionName.contains(Symbol.BIG_RIGHT_PARENTHESIS)) {
                    Pair<String, List<String>> pathParameters = RegexUtility.getActionRegex(actionName);
                    invokableHandlerMethod.setActionRegex(pathParameters.getFirst());
                    invokableHandlerMethod.setPathParameterNameList(pathParameters.getSecond());
                    log.info("controller dynamic mapping action:{},invokableHandlerMethod:{}", actionName, invokableHandlerMethod.getMethod());
                    dynamicMapping.put(pathParameters.getFirst(), invokableHandlerMethod);
                } else {
                    log.info("controller mapping action:{},invokableHandlerMethod:{}", actionName, invokableHandlerMethod.getMethod());
                    mapping.put(actionName, invokableHandlerMethod);
                }
            } catch (Throwable e) {
                log.error("controller parse error action-key {}", actionName);
            }
        }
    }

    @Override
    public ServletInvokableHandlerMethod getHandler(HttpServletRequest request) throws Exception {
        String actionKey = servletUtility.getActionKey(request);
        ServletInvokableHandlerMethod servletInvocableHandlerMethod = this.mapping.get(actionKey);
        if (servletInvocableHandlerMethod != null) {
            return servletInvocableHandlerMethod;
        }
        for (String regex : this.dynamicMapping.keySet()) {
            if (RegexUtility.matches(actionKey, regex)) {
                return this.dynamicMapping.get(regex);
            }
        }
        return null;
    }
}
