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

import com.sparrow.servlet.Controller;
import com.sparrow.servlet.HandlerInterceptor;
import java.lang.reflect.Field;
import javax.inject.Inject;
import javax.inject.Named;

public class AnnotationBeanDefinitionParserDelegate {

    public BeanDefinition processBeanDefinition(Class clazz) {
        GenericBeanDefinition bd = new GenericBeanDefinition();
        bd.setSingleton(true);
        bd.setClassName(clazz.getName());
        Controller controller = (Controller) clazz.getAnnotation(Controller.class);
        bd.setController(controller != null);
        bd.setInterceptor(HandlerInterceptor.class.isAssignableFrom(clazz));
        bd.setPrototype(false);
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            addProperty(bd, field);
        }
        Class superClazz = clazz.getSuperclass();
        fields = superClazz.getDeclaredFields();
        for (Field field : fields) {
            addProperty(bd, field);
        }
        return bd;
    }

    private void addProperty(GenericBeanDefinition bd, Field field) {
        Inject inject = field.getAnnotation(Inject.class);
        if (inject == null) {
            return;
        }
        Named refNamed = field.getAnnotation(Named.class);
        String refName = field.getName();
        if (refNamed != null) {
            refName = refNamed.value();
        }
        ValueHolder valueHolder = new ValueHolder(field.getName(), refName, true);
        bd.addProperty(valueHolder);
    }
}
