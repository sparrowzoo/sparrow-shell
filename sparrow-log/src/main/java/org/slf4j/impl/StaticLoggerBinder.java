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

package org.slf4j.impl;

import com.sparrow.concurrent.SparrowThreadFactory;
import com.sparrow.constant.CacheKey;
import com.sparrow.constant.Config;
import com.sparrow.core.cache.Cache;
import com.sparrow.core.cache.StrongDurationCache;
import com.sparrow.enums.LogLevel;

import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.sparrow.utility.ConfigUtility;
import org.slf4j.ILoggerFactory;
import org.slf4j.spi.LoggerFactoryBinder;

public class StaticLoggerBinder implements LoggerFactoryBinder {

    /**
     * The unique instance of this class.
     */
    private static final StaticLoggerBinder SINGLETON = new StaticLoggerBinder();

    /**
     * Return the singleton of this class.
     *
     * @return the StaticLoggerBinder singleton
     */
    public static final StaticLoggerBinder getSingleton() {
        return SINGLETON;
    }

    private static final String LOGGER_FACTORY_CLASS = SparrowLoggerFactory.class
        .getName();

    private static Cache<String, Object> logCache;

    private final ILoggerFactory loggerFactory;

    private StaticLoggerBinder() {
        logCache = new StrongDurationCache<>(CacheKey.LOG);
        Integer level = LogLevel.INFO.ordinal();
        logCache.put(Config.LOG_LEVEL, level);
        logCache.put(Config.LOG_PRINT_CONSOLE, true);

        ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1,
            new SparrowThreadFactory.Builder().namingPattern("log-config-%d").daemon(true).build());

        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override public void run() {
                Map<String, String> config = ConfigUtility.loadFromClassesPath("/log.properties");
                if (config != null) {
                    if (config.get(Config.LOG_LEVEL) != null) {
                        logCache.put(Config.LOG_LEVEL, LogLevel.valueOf(config.get(Config.LOG_LEVEL).toUpperCase()).ordinal());
                    }
                    if (config.get(Config.LOG_PRINT_CONSOLE) != null) {
                        logCache.put(Config.LOG_PRINT_CONSOLE, Boolean.valueOf(config.get(Config.LOG_PRINT_CONSOLE)));
                    }
                }
            }
        }, 0, 1, TimeUnit.SECONDS);
        loggerFactory = new SparrowLoggerFactory();
    }

    @Override
    public ILoggerFactory getLoggerFactory() {
        return loggerFactory;
    }

    @Override
    public String getLoggerFactoryClassStr() {
        return LOGGER_FACTORY_CLASS;
    }
}
