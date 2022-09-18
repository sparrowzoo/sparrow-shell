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

import com.sparrow.cache.exception.CacheConnectionException;

public interface CacheString {

    Boolean setIfNotExistWithMills(Key key, Object value,long expireMills) throws CacheConnectionException;

    String set(Key key, Object value) throws CacheConnectionException;

    String getSet(Key key, Object value) throws CacheConnectionException;

    String get(Key key) throws CacheConnectionException;

    String get(Key key, CacheDataNotFound<String> hook);

    <T> T get(Key key, Class clazz, CacheDataNotFound<T> hook);

    <T> T get(Key key, Class clazz) throws CacheConnectionException;

    Long append(Key key, Object value) throws CacheConnectionException;

    Long decrease(Key key) throws CacheConnectionException;

    Long decrease(Key key, Long count) throws CacheConnectionException;

    Long increase(Key key, Long count) throws CacheConnectionException;

    Long increase(Key key) throws CacheConnectionException;

    boolean bit(Key key, Integer offset) throws CacheConnectionException;

    String setExpire(Key key, Integer seconds, Object value) throws CacheConnectionException;

    Boolean setIfNotExist(Key key, Object value) throws CacheConnectionException;
}
