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

import com.sparrow.support.EnvironmentSupport;
import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class PropertyUtility {
    public static Map<String, String> load(InputStream stream, String charset) {
        if (stream == null) {
            return null;
        }
        Map<String, String> systemMessage = new ConcurrentHashMap<String, String>();
        Properties props = new Properties();
        try {
            props.load(stream);
        } catch (IOException e) {
            log.error("load config file error", e);
            return null;
        }

        for (Object key : props.keySet()) {
            String strKey = key.toString();
            String value = props.getProperty(strKey);
            if (StringUtility.isNullOrEmpty(charset)) {
                charset = StandardCharsets.UTF_8.name();
            }
            if (value.startsWith("${") && value.endsWith("}")) {
                String envKey = value.substring(2, value.length() - 1).toUpperCase();
                String envValue = System.getenv(envKey);
                if (envValue == null) {
                    log.warn("{} not found,please config env", envKey);
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
            log.error("{} file not found", configFilePath);
            return new HashMap<String, String>();
        }
        if (stream == null) {
            return new HashMap<String, String>();
        }
        return load(stream);
    }

    public static Map<String, String> loadFromClassesPath(String configFilePath, String charset) {
        InputStream stream = null;
        try {
            stream = EnvironmentSupport.getInstance().getFileInputStream(configFilePath);
        } catch (FileNotFoundException e) {
            log.error("[{}] file not found ", configFilePath);
            return null;
        }
        return load(stream, charset);
    }
}
