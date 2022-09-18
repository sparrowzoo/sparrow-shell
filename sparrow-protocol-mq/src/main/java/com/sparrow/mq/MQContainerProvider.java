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

import java.util.Iterator;
import java.util.ServiceLoader;

public class MQContainerProvider {
    private static volatile EventHandlerMappingContainer queueHandlerMappingContainer;

    public static EventHandlerMappingContainer getContainer() {
        if (queueHandlerMappingContainer != null) {
            return queueHandlerMappingContainer;
        }
        synchronized (EventHandlerMappingContainer.class) {
            if (queueHandlerMappingContainer != null) {
                return queueHandlerMappingContainer;
            }
            ServiceLoader<EventHandlerMappingContainer> loader = ServiceLoader.load(EventHandlerMappingContainer.class);
            Iterator<EventHandlerMappingContainer> it = loader.iterator();
            if (it.hasNext()) {
                queueHandlerMappingContainer = it.next();
                return queueHandlerMappingContainer;
            }
            String defaultProvider = "com.sparrow.mq.DefaultQueueHandlerMappingContainer";
            try {
                Class<?> rabbitPublisher = Class.forName(defaultProvider);
                queueHandlerMappingContainer = (EventHandlerMappingContainer) rabbitPublisher.newInstance();
                return queueHandlerMappingContainer;
            } catch (ClassNotFoundException x) {
                throw new RuntimeException(
                    "Provider " + defaultProvider + " not found", x);
            } catch (Exception x) {
                throw new RuntimeException(
                    "Provider " + defaultProvider + " could not be instantiated: " + x,
                    x);
            }
        }
    }
}
