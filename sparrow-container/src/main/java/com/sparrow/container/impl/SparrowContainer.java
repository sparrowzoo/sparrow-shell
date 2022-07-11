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
import com.sparrow.constant.SysObjectName;
import com.sparrow.container.AbstractContainer;
import com.sparrow.container.BeanDefinition;
import com.sparrow.container.BeanDefinitionParserDelegate;
import com.sparrow.container.BeanDefinitionReader;
import com.sparrow.container.ContainerAware;
import com.sparrow.container.SimpleBeanDefinitionRegistry;
import com.sparrow.container.XmlBeanDefinitionReader;
import com.sparrow.exception.CacheNotFoundException;
import com.sparrow.protocol.LoginToken;
import com.sparrow.protocol.Result;
import com.sparrow.protocol.constant.Constant;
import com.sparrow.protocol.constant.magic.Symbol;
import com.sparrow.protocol.dto.AuthorDTO;
import com.sparrow.protocol.dto.ImageDTO;
import com.sparrow.protocol.dto.SimpleItemDTO;
import com.sparrow.protocol.pager.PagerQuery;
import com.sparrow.servlet.HandlerInterceptor;
import com.sparrow.support.Initializer;
import com.sparrow.support.LoginDialog;
import com.sparrow.utility.ConfigUtility;
import com.sparrow.utility.StringUtility;

import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SparrowContainer extends AbstractContainer {
    private static Logger logger = LoggerFactory.getLogger(SparrowContainer.class);

    @Override
    public void init() {
        logger.info("----------------- container init ....-------------------");
        try {
            logger.info("-------------system config file init ...-------------------");
            initSystemConfig();
            logger.info("-------------init bean ...---------------------------");
            SimpleBeanDefinitionRegistry registry = new SimpleBeanDefinitionRegistry();
            BeanDefinitionParserDelegate delegate = new BeanDefinitionParserDelegate();
            BeanDefinitionReader definitionReader = new XmlBeanDefinitionReader(registry, delegate);
            definitionReader.loadBeanDefinitions(this.contextConfigLocation);

            this.beanDefinitionRegistry = registry;

            Iterator<String> iterator = registry.keyIterator();
            while (iterator.hasNext()) {
                String beanName = iterator.next();
                try {
                    BeanDefinition bd = registry.getObject(beanName);
                    this.initMethod(bd);
                    if (bd.isSingleton()) {
                        Object o = this.instance(bd);
                        this.singletonRegistry.pubObject(beanName, o);
                        if (bd.alias() != null) {
                            this.singletonRegistry.pubObject(bd.alias(), o);
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
                    } else {
                        Class clazz = Class.forName(bd.getBeanClassName());
                        this.initProxyBean(clazz);
                    }
                } catch (Throwable t) {
                    logger.error("init bean error,bean-name {}", beanName);
                }
            }

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
            this.initProxyBean(Result.class);
            this.initProxyBean(LoginToken.class);
            this.initProxyBean(LoginDialog.class);
            this.initProxyBean(PagerQuery.class);
            this.initProxyBean(AuthorDTO.class);
            this.initProxyBean(ImageDTO.class);
            this.initProxyBean(SimpleItemDTO.class);
        }
    }

    private void initSystemConfig() throws CacheNotFoundException {
        if (StringUtility.isNullOrEmpty(this.configLocation)) {
            return;
        }
        ConfigUtility.initSystem(this.configLocation);
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
