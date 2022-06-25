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

import java.util.Map;

public interface CacheSortedSet {

    Long getSize(KEY key) throws CacheConnectionException;

    <T> Long add(KEY key, T value, double score) throws CacheConnectionException;

    <T> Long remove(KEY key, T value) throws CacheConnectionException;

    Long remove(KEY key, Long from, Long to) throws CacheConnectionException;

    <T> Double getScore(KEY key, T value) throws CacheConnectionException;

    <T> Long getRank(KEY key, T value) throws CacheConnectionException;

    <T> Map<T, Double> getAllWithScore(KEY key) throws CacheConnectionException;

    <T> Integer putAllWithScore(KEY key, Map<T, Double> keyScoreMap) throws CacheConnectionException;

    <T> Map<T, Double> getAllWithScore(KEY key, Class keyClazz, CacheDataNotFound<Map<T, Double>> hook);
}
