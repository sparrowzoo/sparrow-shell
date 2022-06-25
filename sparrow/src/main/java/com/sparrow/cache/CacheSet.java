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

import java.util.Set;

public interface CacheSet {

    Long getSize(KEY key) throws CacheConnectionException;

    <T> Long add(KEY key, T value) throws CacheConnectionException;

    Long add(KEY key, String... value) throws CacheConnectionException;

    <T> Integer add(KEY key, Iterable<T> values) throws CacheConnectionException;

    <T> Boolean remove(KEY key, T value) throws CacheConnectionException;

    <T> Boolean exist(KEY key, T value) throws CacheConnectionException;

    Set<String> list(KEY key) throws CacheConnectionException;

    <T> Set<T> list(KEY key, Class clazz) throws CacheConnectionException;

    Set<String> list(KEY key, CacheDataNotFound<Set<String>> hook);

    <T> Set<T> list(KEY key, Class clazz, CacheDataNotFound<Set<T>> hook);
}
