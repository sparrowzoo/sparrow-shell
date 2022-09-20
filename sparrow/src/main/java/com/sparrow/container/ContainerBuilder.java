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

public class ContainerBuilder {
    private boolean initSingletonBean = true;
    private boolean initProxyBean = true;
    private boolean initController = true;
    private boolean initInterceptor = true;
    private String scanBasePackage;
    private String contextConfigLocation = "/beans.xml";
    private String configLocation = "/system_config.properties";

    public ContainerBuilder() {
    }

    public boolean isInitProxyBean() {
        return initProxyBean;
    }

    public boolean isInitController() {
        return initController;
    }

    public boolean isInitInterceptor() {
        return initInterceptor;
    }

    public String getContextConfigLocation() {
        return contextConfigLocation;
    }

    public String getConfigLocation() {
        return configLocation;
    }

    public String getScanBasePackage() {
        return scanBasePackage;
    }

    public boolean isInitSingletonBean() {
        return initSingletonBean;
    }

    public ContainerBuilder contextConfigLocation(String contextConfigLocation) {
        this.contextConfigLocation = contextConfigLocation;
        return this;
    }

    public ContainerBuilder configLocation(String configLocation) {
        this.configLocation = configLocation;
        return this;
    }

    public ContainerBuilder initProxyBean(boolean initProxyBean) {
        this.initProxyBean = initProxyBean;
        return this;
    }

    public ContainerBuilder initController(boolean initController) {
        this.initController = initController;
        return this;
    }

    public ContainerBuilder initSingletonBean(boolean initSingletonBean) {
        this.initSingletonBean = initSingletonBean;
        return this;
    }

    public ContainerBuilder initInterceptor(boolean initInterceptor) {
        this.initInterceptor = initInterceptor;
        return this;
    }

    public ContainerBuilder scanBasePackage(String scanBasePackage) {
        this.scanBasePackage = scanBasePackage;
        return this;
    }
}
