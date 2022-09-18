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

package com.sparrow.mq;


import java.lang.reflect.ParameterizedType;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultQueueHandlerMappingContainer implements EventHandlerMappingContainer {
    private static Logger logger = LoggerFactory.getLogger(DefaultQueueHandlerMappingContainer.class);

    public DefaultQueueHandlerMappingContainer() {
    }

    /**
     * map<EventName,MQHandler>
     */
    private Map<String, MQHandler> queueHandlerMappings = new HashMap<String, MQHandler>();

    @Override
    public void put(MQHandler handler) {
        Class parameterClass = (Class) ((ParameterizedType) handler.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        if (parameterClass != null) {
            queueHandlerMappings.put(parameterClass.getName(), handler);
        }
    }

    @Override
    public void init(String packageName) {
        try {
            List<Class> clazzList = ClassUtility.getClasses(packageName);
            for (Class clazz : clazzList) {
                if (MQHandler.class.isAssignableFrom(clazz)) {
                    this.put((MQHandler) clazz.newInstance());
                }
            }
        } catch (Exception e) {
            logger.error("init by package error", e);
        }
    }

    @Override
    public MQHandler get(String eventClazzName) {
        if (this.queueHandlerMappings != null) {
            return queueHandlerMappings.get(eventClazzName);
        }
        return null;
    }
}
