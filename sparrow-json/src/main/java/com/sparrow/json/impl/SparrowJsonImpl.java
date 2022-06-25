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

package com.sparrow.json.impl;

import com.alibaba.fastjson.JSON;
import com.sparrow.protocol.constant.Constant;
import com.sparrow.json.Json;
import com.sparrow.protocol.POJO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class SparrowJsonImpl implements Json {
    private static Logger logger = LoggerFactory.getLogger(SparrowJsonImpl.class);

    @Override
    public String toString(POJO model) {
        return JSON.toJSONString(model);
    }

    @Override
    public String toString(Map<String, Object> map) {
        if (map == null || map.size() == 0) {
            return Constant.NULL_JSON;
        }
        return JSON.toJSONString(map);
    }

    @Override
    public <T> String toString(Collection<T> models) {
        return JSON.toJSONString(models);
    }

    /**
     * 解析json至实体对象
     *
     * @param json  {"name":"zlz","age":"28"}
     * @param clazz
     * @return
     */
    @Override
    public <T> T parse(String json, Class<T> clazz) {
        return JSON.parseObject(json, clazz);
    }

    @Override
    public <T> List<T> parseList(String json, Class<T> clazz) {
        return JSON.parseArray(json, clazz);
    }

    @Override
    public Map<String, Object> parse(String json) {
        return JSON.parseObject(json);
    }
}
