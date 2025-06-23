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

package com.sparrow.json;

import com.sparrow.protocol.POJO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface Json {

    String toString(POJO model);

    String toString(Map<String, Object> map);

    <T> String toString(Collection<T> models);

    <T> T parse(String json, Class<T> clazz);

    Map<String, Object> parse(String json);

    List<Object> parseArray(String json);

    <T> T toJavaObject(Object originJsonObject, Class<T> clazz);

    Object getJSONObject(Object originJsonObject, String key);

    <T> List<T> parseList(String json, Class<T> clazz);
}
