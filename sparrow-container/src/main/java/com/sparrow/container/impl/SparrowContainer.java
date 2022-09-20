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

import com.sparrow.cache.exception.CacheNotFoundException;
import com.sparrow.constant.Config;
import com.sparrow.constant.SysObjectName;
import com.sparrow.container.AbstractContainer;
import com.sparrow.container.AnnotationBeanDefinitionParserDelegate;
import com.sparrow.container.AnnotationBeanDefinitionReader;
import com.sparrow.container.BeanDefinition;
import com.sparrow.container.BeanDefinitionParserDelegate;
import com.sparrow.container.BeanDefinitionReader;
import com.sparrow.container.ContainerAware;
import com.sparrow.container.ContainerBuilder;
import com.sparrow.container.SimpleBeanDefinitionRegistry;
import com.sparrow.container.XmlBeanDefinitionReader;
import com.sparrow.protocol.POJO;
import com.sparrow.protocol.constant.Constant;
import com.sparrow.protocol.constant.magic.Symbol;
import com.sparrow.servlet.HandlerInterceptor;
import com.sparrow.support.Initializer;
import com.sparrow.utility.ConfigUtility;
import com.sparrow.utility.StringUtility;
import java.util.Iterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
                definitionReader.loadBeanDefinitions(this.builder.getContextConfigLocation());
            }
            if (!StringUtility.isNullOrEmpty(builder.getScanBasePackage())) {
                annotationBeanDefinitionReader.loadBeanDefinitions(builder.getScanBasePackage());
            }
            this.beanDefinitionRegistry = registry;
            this.initProxyBeans();
            this.initEarlySingleton();
            this.initSingletonBeans(registry);

            logger.info("-------------init initializer ...--------------------------");
            Initializer initializer = this.getBean(
                SysObjectName.INITIALIZER);

            if (initializer != null) {
                initializer.init(this);
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

    private void initSystemConfig() throws CacheNotFoundException {
        if (StringUtility.isNullOrEmpty(this.builder.getConfigLocation())) {
            return;
        }
        ConfigUtility.initSystem(this.builder.getConfigLocation());
        String internationalization = ConfigUtility
            .getValue(Config.INTERNATIONALIZATION);

        if (StringUtility.isNullOrEmpty(internationalization)) {
            internationalization = ConfigUtility
                .getValue(Config.LANGUAGE);
        }
        if (StringUtility.isNullOrEmpty(internationalization)) {
            internationalization = Constant.DEFAULT_LANGUAGE;
        }
        String[] internationalizationArray = internationalization
            .split(Symbol.COMMA);
        for (String i18n : internationalizationArray) {
            ConfigUtility.initInternationalization(i18n);
        }
    }
}
