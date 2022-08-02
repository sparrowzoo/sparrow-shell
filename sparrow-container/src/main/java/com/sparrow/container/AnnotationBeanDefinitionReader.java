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

import com.sparrow.protocol.POJO;
import com.sparrow.utility.ClassUtility;
import com.sparrow.utility.StringUtility;
import java.util.List;
import javax.inject.Named;

public class AnnotationBeanDefinitionReader extends AbstractBeanDefinitionReader {
    public AnnotationBeanDefinitionReader(SimpleBeanDefinitionRegistry registry,
        AnnotationBeanDefinitionParserDelegate delegate) {
        super(registry);
        this.delegate = delegate;
    }

    private AnnotationBeanDefinitionParserDelegate delegate;

    @Override public void loadBeanDefinitions(String basePackages) throws Exception {
        String[] basePackageArray = basePackages.split(",");
        for (String basePackage : basePackageArray) {
            List<Class> classes = ClassUtility.getClasses(basePackage);
            for (Class clazz : classes) {
                if (clazz.isAssignableFrom(POJO.class)) {
                    GenericBeanDefinition bd = new GenericBeanDefinition();
                    bd.setClassName(clazz.getName());
                    bd.setPrototype(true);
                    this.getRegistry().pubObject(clazz.getSimpleName(), bd);
                    continue;
                }
                Named named = (Named) clazz.getAnnotation(Named.class);
                if (named == null) {
                    continue;
                }
                String beanName = named.value();
                if (StringUtility.isNullOrEmpty(beanName)) {
                    beanName = StringUtility.setFirstByteLowerCase(clazz.getSimpleName());
                    int implIndex = beanName.indexOf("Impl");
                    if (implIndex > 0) {
                        beanName = beanName.substring(0, implIndex);
                    }
                }
                BeanDefinition bd = this.delegate.processBeanDefinition(clazz);
                this.getRegistry().pubObject(beanName, bd);
            }
        }
    }
}
