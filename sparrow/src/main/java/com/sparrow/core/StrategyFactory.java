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

package com.sparrow.core;

import com.sparrow.core.spi.ApplicationContext;
import com.sparrow.utility.ClassUtility;
import com.sparrow.utility.StringUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StrategyFactory {
    private static Logger logger = LoggerFactory.getLogger(StrategyFactory.class);

    private volatile Map<Class, Map<String, Object>> container = new HashMap<Class, Map<String, Object>>();

    public static StrategyFactory strategyFactory = new StrategyFactory();

    public static StrategyFactory getInstance() {
        return strategyFactory;
    }

    private StrategyFactory() {
    }

    public <T> T get(Class clazz, String key) {
        if (!clazz.isInterface()) {
            return null;
        }

        if (StringUtility.isNullOrEmpty(key)) {
            key = "default";
        }
        Map<String, Object> strategy = this.container.get(clazz);
        if (strategy != null && strategy.containsKey(key)) {
            return (T) strategy.get(key);
        }

        T t = ApplicationContext.getContainer().getBean(key + clazz.getSimpleName());
        if (t != null) {
            return t;
        }

        synchronized (this) {
            strategy = this.container.get(clazz);
            if (strategy != null && strategy.containsKey(key)) {
                t = (T) strategy.get(key);
                if (t != null) {
                    return t;
                }
            }

            if (strategy == null) {
                strategy = new HashMap<String, Object>();
                container.put(clazz, strategy);
            }
            List<Class> classList = ClassUtility.getAllClassByInterface(clazz);
            for (Class c : classList) {
                try {
                    String className = c.getSimpleName();
                    String k = className.substring(0, className.indexOf(clazz.getSimpleName() + "Impl")).toLowerCase();
                    strategy.put(k, c.newInstance());
                } catch (InstantiationException e) {
                    logger.error("InstantiationException", e);
                } catch (IllegalAccessException e) {
                    logger.error("illegal access", e);
                }
            }
            if (strategy.containsKey(key)) {
                t = (T) strategy.get(key);
            } else {
                //为防止第二次查找
                strategy.put(key, null);
            }
        }
        return t;
    }
}
