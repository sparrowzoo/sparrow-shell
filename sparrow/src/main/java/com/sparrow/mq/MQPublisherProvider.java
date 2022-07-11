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

public class MQPublisherProvider {

    private static MQPublisher publisher;

    public static MQPublisher getPublisher() {
        if (publisher != null) {
            return publisher;
        }
        synchronized (MQPublisherProvider.class) {
            if (publisher != null) {
                return publisher;
            }
            ServiceLoader<MQPublisher> loader = ServiceLoader.load(MQPublisher.class);
            Iterator<MQPublisher> it = loader.iterator();
            if (it.hasNext()) {
                publisher = it.next();
                return publisher;
            }
            String defaultProvider = "com.sparrow.rocketmq.impl.SparrowRocketMQPublisher";
            try {
                Class<?> rabbitPublisher = Class.forName(defaultProvider);
                publisher = (MQPublisher) rabbitPublisher.newInstance();
                return publisher;
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
