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

import com.sparrow.constant.CacheKey;
import com.sparrow.core.cache.Cache;
import com.sparrow.core.cache.StrongDurationCache;
import com.sparrow.protocol.constant.Constant;
import com.sparrow.protocol.constant.magic.Symbol;
import com.sparrow.support.EnvironmentSupport;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigUtility {
    private static Logger logger = LoggerFactory.getLogger(ConfigUtility.class);
    private static Cache<String, String> configCache;
    private static Cache<String, Map<String, String>> internationalization;

    static {
        configCache = new StrongDurationCache<>(CacheKey.CONFIG_FILE);
        internationalization = new StrongDurationCache<>(CacheKey.INTERNATIONALIZATION);
    }

    public static String getLanguageValue(String propertiesKey) {
        String language = getValue(com.sparrow.constant.Config.LANGUAGE);
        return getLanguageValue(propertiesKey, language);
    }

    public static String getLanguageValue(String key, String language) {
        if (StringUtility.isNullOrEmpty(language)) {
            language = "zh_cn";
        }
        return getLanguageValue(key, language, null);
    }

    public static String getLanguageValue(String key, String language, String defaultValue) {
        if (StringUtility.isNullOrEmpty(language)) {
            language = getValue(com.sparrow.constant.Config.LANGUAGE);
        } else {
            language = language.toLowerCase();
        }

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
        String rootPath = ConfigUtility.getValue(com.sparrow.constant.Config.ROOT_PATH);
        if (!StringUtility.isNullOrEmpty(rootPath) && value.contains(Symbol.DOLLAR + com.sparrow.constant.Config.ROOT_PATH)) {
            value = value.replace(Symbol.DOLLAR + com.sparrow.constant.Config.ROOT_PATH, rootPath);
        }
        return value;
    }

    public static Map<String, String> load(InputStream stream, String charset) {
        if (stream == null) {
            return null;
        }
        Map<String, String> systemMessage = new ConcurrentHashMap<String, String>();
        Properties props = new Properties();

        try {
            props.load(stream);
        } catch (IOException e) {
            logger.error("load config file error", e);
            return null;
        }

        for (Object key : props.keySet()) {
            String strKey = key.toString();
            String value = props.getProperty(strKey);
            if (StringUtility.isNullOrEmpty(charset)) {
                charset = Constant.CHARSET_UTF_8;
            }
            try {
                value = new String(value.getBytes(Constant.CHARSET_ISO_8859_1), charset);
            } catch (UnsupportedEncodingException ignore) {
            }
            if (value.startsWith("${") && value.endsWith("}")) {
                String envKey = value.substring(2, value.length() - 1).toUpperCase();
                String envValue = System.getenv(envKey);
                if (envValue == null) {
                    logger.warn("{} not found,please config env", envKey);
                    continue;
                }
                value = envValue;
            }
            systemMessage.put(strKey, value);
        }
        return systemMessage;
    }

    public static Map<String, String> load(InputStream stream) {
        return load(stream, null);
    }

    public static Map<String, String> loadFromClassesPath(String configFilePath) {
        InputStream stream = null;
        try {
            stream = EnvironmentSupport.getInstance().getFileInputStream(configFilePath);
        } catch (FileNotFoundException e) {
            logger.error("{} file not found", configFilePath);
            return null;
        }
        if (stream == null) {
            return null;
        }
        return load(stream);
    }

    public static Map<String, String> loadFromClassesPath(String configFilePath, String charset) {
        InputStream stream = null;
        try {
            stream = EnvironmentSupport.getInstance().getFileInputStream(configFilePath);
        } catch (FileNotFoundException e) {
            logger.error("[{}] file not found ", configFilePath);
            return null;
        }
        return load(stream, charset);
    }

    public static void initSystem(String configFilePath) {
        Map<String, String> systemMessage = loadFromClassesPath(configFilePath);
        if (systemMessage == null) {
            return;
        }
        configCache.putAll(systemMessage);
        if (systemMessage.get(com.sparrow.constant.Config.RESOURCE_PHYSICAL_PATH) != null) {
            Constant.REPLACE_MAP.put("$physical_resource", systemMessage.get(com.sparrow.constant.Config.RESOURCE_PHYSICAL_PATH));
        }
        if (systemMessage.get(com.sparrow.constant.Config.RESOURCE) != null) {
            Constant.REPLACE_MAP.put("$resource", systemMessage.get(com.sparrow.constant.Config.RESOURCE));
        }
        if (systemMessage.get(com.sparrow.constant.Config.IMAGE_WEBSITE) != null) {
            Constant.REPLACE_MAP.put("$image_website", systemMessage.get(com.sparrow.constant.Config.IMAGE_WEBSITE));
        }
        logger.info("==========system config init============");
    }

    public static void initInternationalization(String language) {
        if (StringUtility.isNullOrEmpty(language)) {
            language = getValue(com.sparrow.constant.Config.LANGUAGE);
        }
        Map<String, String> properties = loadFromClassesPath("/messages_"
            + language
            + ".properties", Constant.CHARSET_UTF_8);
        if (properties != null) {
            internationalization.put(language, properties);
        }
    }

    public static String getValue(String key) {
        return getValue(key, null);
    }

    public static String getValue(String key, String defaultValue) {
        Object value = configCache.get(key);
        if (value == null) {
            return defaultValue;
        }
        String v = value.toString();
        v = StringUtility.replace(v, Constant.REPLACE_MAP);
        return v;
    }

    public static boolean getBooleanValue(String config) {
        String value = getValue(config);
        return !StringUtility.isNullOrEmpty(value) && Boolean.TRUE.toString().equalsIgnoreCase(value);
    }

    public static boolean getBooleanValue(String config, boolean defaultValue) {
        String value = getValue(config);
        if (StringUtility.isNullOrEmpty(value)) {
            return defaultValue;
        }
        return Boolean.TRUE.toString().equalsIgnoreCase(value);
    }

    public static Integer getIntegerValue(String config) {
        String value = ConfigUtility.getValue(config);
        if (StringUtility.isNullOrEmpty(value)) {
            return 0;
        }
        return Integer.valueOf(value);
    }

    public static void resetKey(String key, String website) {
        configCache.put(key, website);
    }
}
