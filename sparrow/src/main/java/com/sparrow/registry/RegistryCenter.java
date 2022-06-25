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

package com.sparrow.registry;

import java.util.List;

public interface RegistryCenter {

    /**
     * get data by key
     *
     * @param key key
     * @return value
     */
    String get(String key);

    /**
     * existed
     *
     * @param key key
     * @return is exist
     */
    boolean exist(String key);

    /**
     * persist .
     *
     * @param key   key
     * @param value value
     */
    void persist(String key, String value);

    /**
     * modify.
     *
     * @param key   键
     * @param value 值
     */
    void modify(String key, String value);

    /**
     * remove.
     *
     * @param key 键
     */
    void remove(String key);

    /**
     * get registry center time.
     *
     * @param key key
     * @return current time
     */
    long getRegistryCenterTime(String key);

    /**
     * get data from remote
     *
     * @param key key
     * @return value
     */
    String getFromRemote(String key);

    /**
     * get children key
     *
     * @param key key
     * @return children key list
     */
    List<String> getChildrenKeys(String key);

    /**
     * get children count
     *
     * @param key key
     * @return count
     */
    int getChildrenCount(String key);

    /**
     * persist ephemeral.
     *
     * @param key   key
     * @param value value
     */
    void persistEphemeral(String key, String value);

    /**
     * persist sequential.
     *
     * @param key   key
     * @param value value
     * @return znode name
     */
    String persistSequential(String key, String value);

    /**
     * persist ephemeral sequential.
     *
     * @param key key
     */
    void persistEphemeralSequential(String key);

    /**
     * add local cache.
     *
     * @param cachePath cache path
     */
    void addLocalCache(String cachePath);

    /**
     * evict local cache
     *
     * @param cachePath cache path
     */
    void evictCacheData(String cachePath);

    /**
     * get register cache object
     *
     * @param cachePath cache path
     * @return object
     */
    Object getCache(String cachePath);

    /**
     * 直接获取操作注册中心的原生客户端. 如：Zookeeper或Redis等原生客户端.
     *
     * @return 注册中心的原生客户端
     */
    Object getRawClient();
}
