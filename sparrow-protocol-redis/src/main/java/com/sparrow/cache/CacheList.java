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
import java.util.List;

public interface CacheList {
    Long getSize(Key key) throws CacheConnectionException;

    <T> Long add(Key key, T value) throws CacheConnectionException;

    Long add(Key key, String... value) throws CacheConnectionException;

    <T> Integer add(Key key, Iterable<T> values) throws CacheConnectionException;

    <T> Long remove(Key key, T value) throws CacheConnectionException;

    List<String> list(Key key) throws CacheConnectionException;

    <T> List<T> list(Key key, Class clazz) throws CacheConnectionException;

    List<String> list(Key key, CacheDataNotFound<List<String>> hook);

    <T> List<T> list(Key key, Class clazz, CacheDataNotFound<List<T>> hook);
}
