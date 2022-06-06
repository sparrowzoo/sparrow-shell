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

/**
 * @author harry
 * @date 2018/1/26
 */
public interface CacheDataNotFound<T> {

    /**
     * 未发现则读源
     *
     * @param key
     * @return
     */
    T read(KEY key);

    /**
     * 将结果写回cache
     *
     * @param data
     * @return 是否执行默认回写
     */
    default boolean backWrite(T data) throws CacheConnectionException {
        return true;
    }
}
