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

import com.sparrow.cg.Generator4MethodAccessor;

import java.util.Iterator;
import java.util.ServiceLoader;

public class MethodAccessorProvider {

    private static final String DEFAULT_PROVIDER = "com.sparrow.cg.impl.Generator4SetFieldMethodAccessor";
    private volatile static Generator4MethodAccessor methodAccessor;

    public static Generator4MethodAccessor getMethodAccessorProvider() {
        if (methodAccessor != null) {
            return methodAccessor;
        }
        synchronized (MethodAccessorProvider.class) {
            if (methodAccessor != null) {
                return methodAccessor;
            }

            ServiceLoader<Generator4MethodAccessor> loader = ServiceLoader.load(Generator4MethodAccessor.class);
            Iterator<Generator4MethodAccessor> it = loader.iterator();
            if (it.hasNext()) {
                methodAccessor = it.next();
                return methodAccessor;
            }

            try {
                Class<?> clazz = Class.forName(DEFAULT_PROVIDER);
                methodAccessor = (Generator4MethodAccessor) clazz.newInstance();
                return methodAccessor;
            } catch (Exception x) {
                throw new RuntimeException(
                        "Provider " + DEFAULT_PROVIDER + " could not be instantiated: " + x,
                        x);
            }
        }
    }
}

