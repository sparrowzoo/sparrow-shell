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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class AbstractBeanDefinition implements BeanDefinition {

    private boolean singleton;
    private boolean prototype;
    private boolean controller;
    private boolean interceptor;
    private String alias;
    private TreeMap<Integer, ValueHolder> constructorArgsMap = new TreeMap<>();
    private Class[] constructorTypes;
    private Object[] constructorArgs;
    private List<ValueHolder> propertyList = new ArrayList<>();

    private Map<String, ValueHolder> placeholder = new HashMap();
    private String className;
    private String scope;


    public void addProperty(ValueHolder valueHolder) {
        this.propertyList.add(valueHolder);
    }

    public void addPlaceholder(String placeholder, ValueHolder valueHolder) {
        this.placeholder.put(placeholder, valueHolder);
    }

    public void addConstructorArg(int index, ValueHolder valueHolder) {
        this.constructorArgsMap.put(index, valueHolder);
    }

    @Override
    public String getBeanClassName() {
        return this.className;
    }

    @Override
    public String getScope() {
        return this.scope;
    }

    @Override
    public TreeMap<Integer, ValueHolder> getConstructorArgsMap() {
        return this.constructorArgsMap;
    }

    @Override
    public List<ValueHolder> getPropertyValues() {
        return this.propertyList;
    }

    @Override
    public boolean isSingleton() {
        return singleton;
    }

    @Override
    public boolean isPrototype() {
        return prototype;
    }

    public void setSingleton(boolean singleton) {
        this.singleton = singleton;
    }

    public void setPrototype(boolean prototype) {
        this.prototype = prototype;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    @Override
    public boolean isInterceptor() {
        return interceptor;
    }

    @Override
    public String alias() {
        return this.alias;
    }

    public void setInterceptor(boolean interceptor) {
        this.interceptor = interceptor;
    }

    @Override
    public boolean isController() {
        return controller;
    }

    public void setController(boolean controller) {
        this.controller = controller;
    }
}
