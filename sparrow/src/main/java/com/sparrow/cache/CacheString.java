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

package com.sparrow.cache;

import com.sparrow.constant.cache.KEY;
import com.sparrow.exception.CacheConnectionException;

public interface CacheString {
    String set(KEY key, Object value) throws CacheConnectionException;

    String getSet(KEY key, Object value) throws CacheConnectionException;

    String get(KEY key) throws CacheConnectionException;

    String get(KEY key, CacheDataNotFound<String> hook);

    <T> T get(KEY key, Class clazz, CacheDataNotFound<T> hook);

    <T> T get(KEY key, Class clazz) throws CacheConnectionException;

    Long append(KEY key, Object value) throws CacheConnectionException;

    Long decrease(KEY key) throws CacheConnectionException;

    Long decrease(KEY key, Long count) throws CacheConnectionException;

    Long increase(KEY key, Long count) throws CacheConnectionException;

    Long increase(KEY key) throws CacheConnectionException;

    boolean bit(KEY key, Integer offset) throws CacheConnectionException;

    String setExpire(KEY key, Integer seconds, Object value) throws CacheConnectionException;

    Long setIfNotExist(KEY key, Object value) throws CacheConnectionException;
}
