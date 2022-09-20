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

package com.sparrow.daemon;

import com.sparrow.container.Container;
import com.sparrow.container.ContainerBuilder;
import com.sparrow.container.FactoryBean;
import com.sparrow.core.spi.ApplicationContext;
import java.util.Iterator;

public class Startup {
    public static void main(String[] args) {
        Container container =
            ApplicationContext.getContainer();
        ContainerBuilder builder = new ContainerBuilder();
        container.init(builder);
        FactoryBean factory = container.getSingletonRegister();
        Iterator iterator = factory.keyIterator();
        while (iterator.hasNext()) {
            String beanName = (String) iterator.next();
            Object o = factory.getObject(beanName);
            if (o instanceof Runnable) {
                ((Runnable) o).run();
            }
        }
    }
}
