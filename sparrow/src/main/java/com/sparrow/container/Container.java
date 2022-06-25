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

package com.sparrow.container;

import com.sparrow.cg.MethodAccessor;
import com.sparrow.constant.SysObjectName;
import com.sparrow.core.TypeConverter;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public interface Container {

    FactoryBean getSingletonRegister();

    FactoryBean getControllerRegister();

    FactoryBean getProxyBeanRegister();

    FactoryBean getInterceptorRegister();

    /**
     * 获取类的代理对象
     */
    MethodAccessor getProxyBean(Class<?> clazz);

    /**
     * 获取类的字段转换器
     */
    List<TypeConverter> getFieldList(Class clazz);

    /**
     * 获取action method
     */
    Map<String, Method> getControllerMethod(String clazzName);

    /**
     * 获取bean 对象根据bean名称
     */
    <T> T getBean(String beanName);

    <T> T getBean(SysObjectName sysObjectName);

    /**
     * 初始化container
     */
    void init();

    void setConfigLocation(String configLocation);

    void setContextConfigLocation(String contextConfigLocation);
}
