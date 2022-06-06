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

package com.sparrow.enums;

/**
 * @author harry
 */

public enum KeyGenerator {
    /**
     * 全局唯一id
     */
    uuid,
    /**
     * 自增长
     */
    increment,
    /**
     * 非key
     */
    not_key,
    /**
     * 预先生成的key
     */
    prepare_id,
    /**
     * 表要加别名否则会报错
     * <p/>
     * You cant't specify target table 'TABLE_NAME' for update in FROM clause
     */
    sql
}
