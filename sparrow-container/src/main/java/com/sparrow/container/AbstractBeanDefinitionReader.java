/*
 * Copyright 2002-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sparrow.container;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractBeanDefinitionReader implements BeanDefinitionReader {

    /** Logger available to subclasses. */
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private final SimpleBeanDefinitionRegistry registry;

    protected AbstractBeanDefinitionReader(SimpleBeanDefinitionRegistry registry) {
        this.registry = registry;
    }

    @Override
    public final SimpleBeanDefinitionRegistry getRegistry() {
        return this.registry;
    }

    @Override
    public void loadBeanDefinitions(String... xmlFileName) throws Exception {
        for (String resource : xmlFileName) {
            loadBeanDefinitions(resource);
        }
    }
}
