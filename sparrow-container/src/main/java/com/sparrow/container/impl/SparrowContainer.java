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
package com.sparrow.container.impl;

import com.sparrow.constant.Config;
import com.sparrow.container.*;
import com.sparrow.container.ConfigReader;
import com.sparrow.container.config.SparrowConfigReader;
import com.sparrow.protocol.POJO;
import com.sparrow.protocol.constant.Constant;
import com.sparrow.protocol.constant.magic.Symbol;
import com.sparrow.servlet.HandlerInterceptor;
import com.sparrow.support.Initializer;
import com.sparrow.utility.ClassUtility;
import com.sparrow.utility.StringUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;

public class SparrowContainer extends AbstractContainer {
    private static Logger logger = LoggerFactory.getLogger(SparrowContainer.class);

    private void initProxyBeans() {
        if (!this.builder.isInitProxyBean()) {
            return;
        }
        Iterator<String> iterator = this.beanDefinitionRegistry.keyIterator();
        while (iterator.hasNext()) {
            String beanName = iterator.next();
            try {
                BeanDefinition bd = beanDefinitionRegistry.getObject(beanName);
                if (!bd.isSingleton()) {
                    //this.initMethod(bd);
                    Class clazz = Class.forName(bd.getBeanClassName());
                    if (POJO.class.isAssignableFrom(clazz)) {
                        this.initProxyBean(clazz);
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    private void initEarlySingleton() {
        if (!builder.isInitSingletonBean()) {
            return;
        }
        Iterator<String> iterator = this.beanDefinitionRegistry.keyIterator();

        while (iterator.hasNext()) {
            String beanName = iterator.next();
            try {
                BeanDefinition bd = beanDefinitionRegistry.getObject(beanName);
                if (bd.isSingleton() && !bd.isController()) {
                    Object o = this.earlyInstance(bd);
                    this.earlySingletonRegistry.pubObject(beanName, o);
                    if (bd.alias() != null) {
                        this.earlySingletonRegistry.pubObject(bd.alias(), o);
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    @Override
    public void init(ContainerBuilder builder) {
        this.builder = builder;
        logger.info("----------------- container init ....-------------------");
        try {
            logger.info("-------------system config file init ...-------------------");
            initSystemConfig();
            logger.info("-------------init bean ...---------------------------");

            SimpleBeanDefinitionRegistry registry = new SimpleBeanDefinitionRegistry();
            BeanDefinitionParserDelegate delegate = new BeanDefinitionParserDelegate();

            AnnotationBeanDefinitionParserDelegate annotationDelegate = new AnnotationBeanDefinitionParserDelegate();
            AnnotationBeanDefinitionReader annotationBeanDefinitionReader = new AnnotationBeanDefinitionReader(registry, annotationDelegate);
            BeanDefinitionReader definitionReader = new XmlBeanDefinitionReader(registry, annotationBeanDefinitionReader, delegate);

            if (this.builder.isInitSingletonBean()) {
                //1. 解析xml 的bean
                //2. 解析 import 配置
                //3. 扫描xml中的scan base package 并解析beanDefinition
                definitionReader.loadBeanDefinitions(this.builder.getContextConfigLocation());
            }
            if (!StringUtility.isNullOrEmpty(builder.getScanBasePackage())) {
                //解析scan base package 下的bean definition
                annotationBeanDefinitionReader.loadBeanDefinitions(builder.getScanBasePackage());
            }
            this.beanDefinitionRegistry = registry;
            this.initProxyBeans();
            this.initEarlySingleton();
            this.initSingletonBeans(registry);


            logger.info("-------------init initializer ...--------------------------");
            if (builder.isInitSingletonBean()) {
                Initializer initializer = this.getBean(Initializer.class);

                if (initializer != null) {
                    initializer.init(this);
                }
            }
            logger.info("-----------------Ioc container init success...-------------------");
        } catch (Exception e) {
            logger.error("ioc init error", e);
        } finally {
            //annotation proxy
        }
    }

    private void initSingletonBeans(SimpleBeanDefinitionRegistry registry) {
        if (!builder.isInitSingletonBean()) {
            return;
        }
        Iterator<String> iterator = registry.keyIterator();
        while (iterator.hasNext()) {
            String beanName = iterator.next();
            try {
                BeanDefinition bd = registry.getObject(beanName);
                if (bd.isSingleton()) {
                    Object o = this.instance(bd, beanName);
                    this.singletonRegistry.pubObject(beanName, o);
                    this.earlySingletonRegistry.removeObject(beanName);
                    if (bd.alias() != null) {
                        this.singletonRegistry.pubObject(bd.alias(), o);
                        this.earlySingletonRegistry.removeObject(bd.alias());
                    }
                    if (bd.isController()) {
                        this.assembleController(beanName, o);
                    }
                    if (bd.isInterceptor()) {
                        this.interceptorRegistry.pubObject(beanName, (HandlerInterceptor) o);
                    }
                    if (o instanceof ContainerAware) {
                        ContainerAware containerAware = (ContainerAware) o;
                        containerAware.aware(this, beanName);
                    }
                }
            } catch (Throwable t) {
                logger.error("init bean error,bean-name {}", beanName);
            }
        }
    }

    private void initSystemConfig() {
        if (StringUtility.isNullOrEmpty(this.builder.getConfigLocation())) {
            return;
        }
        SparrowConfigReader configReader = new SparrowConfigReader();
        this.singletonRegistry.pubObject(ClassUtility.getBeanNameByClass(ConfigReader.class), configReader);
        configReader.initSystem(this.builder.getConfigLocation());
        String internationalization = configReader
                .getValue(Config.INTERNATIONALIZATION);

        if (StringUtility.isNullOrEmpty(internationalization)) {
            internationalization = configReader
                    .getValue(Config.LANGUAGE);
        }
        if (StringUtility.isNullOrEmpty(internationalization)) {
            internationalization = Constant.DEFAULT_LANGUAGE;
        }
        String[] internationalizationArray = internationalization
                .split(Symbol.COMMA);
        for (String i18n : internationalizationArray) {
            configReader.initInternationalization(i18n);
        }
    }
}
