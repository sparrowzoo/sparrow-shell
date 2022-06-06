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
import com.alibaba.fastjson.util.ParameterizedTypeImpl;
import com.google.gson.Gson;
import com.sparrow.json.Json;
import com.sparrow.protocol.POJO;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author by harry
 */
public class FastJsonExtensionJsonImpl implements Json {
    @Override
    public String toString(POJO model) {
        return JSON.toJSONString(model);
    }

    @Override
    public String toString(Map<String, Object> map) {
        return JSON.toJSONString(map);
    }

    @Override
    public <T> String toString(Collection<T> models) {
        return new Gson().toJson(models);
    }

    @Override
    public <T> T parse(String json, Class<T> clazz) {
        Gson gson = new Gson();
        T result = JSON.parseObject(json, clazz);
        return result;
    }

    private Type buildType(Class... types) {
        ParameterizedTypeImpl beforeType = null;
        if (types != null && types.length > 0) {
            for (int i = types.length - 1; i > 0; i--) {
                beforeType = new ParameterizedTypeImpl(new Type[]{beforeType == null ? types[i] : beforeType}, null, types[i - 1]);
            }
        }
        return beforeType;
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
