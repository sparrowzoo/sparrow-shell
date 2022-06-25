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

package com.sparrow.utility;

import com.sparrow.cg.MethodAccessor;
import com.sparrow.container.Container;
import com.sparrow.core.TypeConverter;
import com.sparrow.core.spi.ApplicationContext;

import java.util.List;

public class BeanUtility {
    public static void copyProperties(Object source, Object target, String... ignoreProperties) {
        Container container = ApplicationContext.getContainer();
        MethodAccessor sourceMethodAccessor = container.getProxyBean(source.getClass());
        MethodAccessor targetMethodAccessor = container.getProxyBean(target.getClass());
        List<TypeConverter> targetFieldList = container.getFieldList(target.getClass());
        for (TypeConverter targetField : targetFieldList) {
            if (StringUtility.existInArray(ignoreProperties, targetField.getName())) {
                continue;
            }
            targetMethodAccessor.set(target, targetField.getName(), sourceMethodAccessor.get(source, targetField.getName()));
        }
    }

    public static void copyProperties(Object source, Object target) {
        copyProperties(source, target, null);
    }
}
