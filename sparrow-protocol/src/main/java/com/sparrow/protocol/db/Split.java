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

package com.sparrow.protocol.db;

import com.sparrow.protocol.enums.DATABASE_SPLIT_STRATEGY;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by harry on 2015/7/14.
 */
@Target({TYPE})
@Retention(RUNTIME)
public @interface Split {

    /**
     * 分表的桶的大小
     * <p/>
     * 可以在system.config中配置
     * <p/>
     * this.modelClazz.getSimpleName()+".table_bucket_count";
     *
     * @return 2014-5-2下午12:05:35 harry
     */
    int table_bucket_count() default 1;

    /**
     * 分库的最大id
     *
     * @return 2014-5-2下午12:05:53 harry
     */
    int database_max_id() default -1;

    /**
     * 数据库拆分的key
     * <p>
     * 用作数据库名称的后缀
     *
     * @return 2014-5-2下午12:06:14 harry
     */
    DATABASE_SPLIT_STRATEGY strategy() default DATABASE_SPLIT_STRATEGY.DEFAULT;
}
