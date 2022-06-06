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

public enum OrmEntityMetadata {
    /**
     * 表名
     *
     * String
     */
    TABLE_NAME,

    /**
     * SQL 方言
     */
    DIALECT,
    /**
     * 表被hash打散的桶数
     */
    TABLE_BUCKET_COUNT,
    /**
     * 字段列表
     *
     * String
     */
    FIELDS,
    /**
     * DBField元数据
     */
    DB_FIELDS,

    /**
     * 每个字段的类型
     */
    DB_FIELD_TYPES,
    /**
     * 唯一字段
     */
    UNIQUE_FIELD,
    /**
     * 唯一字段类型
     *
     * 主键的索引则为0
     */
    UNIQUE_TYPE,
    /**
     * SET方法列表
     *
     * Method方法对象列表
     */
    SET_METHODS,
    /**
     * GET方法列表
     */
    GET_METHODS,
    /**
     * 新建记录SQL
     */
    SQL_INSERT,
    /**
     * 删除一条记录（按主键)
     */
    SQL_DELETE,
    /**
     * 更新记录(按主键)
     */
    SQL_UPDATE,

    /**
     * 状态字段
     */
    STATUS_FIELD_NAME,
    /**
     * 数据库分库后缀
     */
    DATABASE_SPLIT_KEY,

    /**
     * 分库的最大id
     */
    DATABASE_SPLIT_MAX_ID
}
