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
package com.sparrow.container.config;

import com.sparrow.constant.CacheKey;
import com.sparrow.constant.Config;
import com.sparrow.container.ConfigReader;
import com.sparrow.core.cache.Cache;
import com.sparrow.core.cache.StrongDurationCache;
import com.sparrow.protocol.constant.Constant;
import com.sparrow.protocol.constant.magic.Symbol;
import com.sparrow.utility.PropertyUtility;
import com.sparrow.utility.StringUtility;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
public class SparrowConfigReader implements ConfigReader {
    private static Cache<String, String> configCache;
    private static Cache<String, Map<String, String>> internationalization;

    static {
        configCache = new StrongDurationCache<>(CacheKey.CONFIG_FILE);
        internationalization = new StrongDurationCache<>(CacheKey.INTERNATIONALIZATION);
    }

    public String getI18nValue(String propertiesKey) {
        String language = getValue(com.sparrow.constant.Config.LANGUAGE);
        return getI18nValue(propertiesKey, language);
    }

    public String getI18nValue(String key, String language) {
        if (StringUtility.isNullOrEmpty(language)) {
            language = "zh_cn";
        }
        return getI18nValue(key, language, null);
    }

    public String getI18nValue(String key, String language, String defaultValue) {
        if (StringUtility.isNullOrEmpty(language)) {
            language = getValue(Config.LANGUAGE,Constant.DEFAULT_LANGUAGE);
        }
        language = language.toLowerCase();
        if (internationalization == null) {
            return defaultValue;
        }

        Map<String, String> internationalizationMap = internationalization
                .get(language);
        if (internationalizationMap == null) {
            return defaultValue;
        }
        String value = internationalizationMap.get(key.toLowerCase());
        if (value == null) {
            return defaultValue;
        }
        String rootPath = this.getValue(com.sparrow.constant.Config.ROOT_PATH);
        if (!StringUtility.isNullOrEmpty(rootPath) && value.contains(Symbol.DOLLAR + com.sparrow.constant.Config.ROOT_PATH)) {
            value = value.replace(Symbol.DOLLAR + com.sparrow.constant.Config.ROOT_PATH, rootPath);
        }
        return value;
    }

    public void initSystem(String configFilePath) {
        Map<String, String> systemMessage = PropertyUtility.loadFromClassesPath(configFilePath);
        if (systemMessage == null) {
            return;
        }

        if (systemMessage.get(Config.RESOURCE_PHYSICAL_PATH) != null) {
            Constant.REPLACE_MAP.put("$physical_resource", systemMessage.get(com.sparrow.constant.Config.RESOURCE_PHYSICAL_PATH));
        }
        if (systemMessage.get(Config.RESOURCE) != null) {
            Constant.REPLACE_MAP.put("$resource", systemMessage.get(com.sparrow.constant.Config.RESOURCE));
        }

        if (systemMessage.get(Config.UPLOAD_PHYSICAL_PATH) != null) {
            Constant.REPLACE_MAP.put("$physical_upload", systemMessage.get(Config.UPLOAD_PHYSICAL_PATH));
        }

        if (systemMessage.get(Config.UPLOAD) != null) {
            Constant.REPLACE_MAP.put("$upload", systemMessage.get(Config.UPLOAD));
        }
        for (String key : systemMessage.keySet()) {
            String v = systemMessage.get(key);
            v = StringUtility.replace(v, Constant.REPLACE_MAP);
            systemMessage.put(key, v);
        }
        configCache.putAll(systemMessage);
        log.info("==========system config init============");
    }

    public void initInternationalization(String language) {
        if (StringUtility.isNullOrEmpty(language)) {
            language = getValue(com.sparrow.constant.Config.LANGUAGE);
        }
        Map<String, String> properties = PropertyUtility.loadFromClassesPath("/messages_"
                + language
                + ".properties", StandardCharsets.UTF_8.name());
        if (properties != null) {
            internationalization.put(language, properties);
        }
    }

    public String getValue(String key) {
        return getValue(key, null);
    }

    public String getValue(String key, String defaultValue) {
        Object value = configCache.get(key);
        if (value == null) {
            return defaultValue;
        }
        return value.toString();
    }

    public boolean getBooleanValue(String config) {
        String value = getValue(config);
        return !StringUtility.isNullOrEmpty(value) && Boolean.TRUE.toString().equalsIgnoreCase(value);
    }

    public boolean getBooleanValue(String config, boolean defaultValue) {
        String value = getValue(config);
        if (StringUtility.isNullOrEmpty(value)) {
            return defaultValue;
        }
        return Boolean.TRUE.toString().equalsIgnoreCase(value);
    }

    public Integer getIntegerValue(String config) {
        String value = this.getValue(config);
        if (StringUtility.isNullOrEmpty(value)) {
            return 0;
        }
        return Integer.valueOf(value);
    }

    public void resetKey(String key, String value) {
        configCache.put(key, value);
    }

}
