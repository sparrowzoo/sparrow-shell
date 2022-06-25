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

package com.sparrow.core.spi;

import com.sparrow.container.Container;

import java.util.Iterator;
import java.util.ServiceLoader;

public class ApplicationContext {
    private static volatile Container container = null;

    /**
     * 第三方类解耦
     * <p/>
     * Law of Demeter Principle
     */
    public static Container getContainer() {
        if (container != null) {
            return container;
        }
        synchronized (ApplicationContext.class) {
            if (container != null) {
                return container;
            }

            ServiceLoader<Container> loader = ServiceLoader.load(Container.class);
            Iterator<Container> it = loader.iterator();
            if (it.hasNext()) {
                container = it.next();
                return container;
            }
            String defaultProvider = "com.sparrow.container.impl.SparrowContainer";
            try {
                Class<?> containerClazz = Class.forName(defaultProvider);
                container = (Container) containerClazz.newInstance();
                return container;
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
