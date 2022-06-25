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

package com.sparrow.protocol.dao.enums;

/**
 * 分表hash 类型
 */
public enum TableSplitStrategy {
    /**
     * 字段值直接作为表后缀
     */
    ORIGIN,
    /**
     * 表打散使用
     */
    HASH,
    /**
     * 字段值只作为表后缀 但不入库
     */
    ORIGIN_NOT_PERSISTENCE
}
