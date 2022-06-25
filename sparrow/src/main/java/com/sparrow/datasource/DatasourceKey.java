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

package com.sparrow.datasource;

import com.sparrow.constant.Config;
import com.sparrow.protocol.constant.Constant;
import com.sparrow.protocol.constant.magic.Symbol;
import com.sparrow.core.Pair;
import com.sparrow.utility.ConfigUtility;
import com.sparrow.utility.StringUtility;

public class DatasourceKey {
    public static DatasourceKey parse(String key) {
        Pair<String, String> pair = Pair.split(key, Symbol.UNDERLINE);
        return new DatasourceKey(pair.getFirst(), pair.getSecond());
    }

    public static DatasourceKey getDefault() {
        return new DatasourceKey();
    }

    private DatasourceKey() {
    }

    public DatasourceKey(String schema, String suffix) {
        this.schema = schema;
        this.suffix = suffix;
    }

    /**
     * 数据库模式 从entity 匹配中读取
     * <p>
     * 通过该属性与上下文的suffix唯一确定一个datasource key
     *
     * @see javax.persistence.Table
     **/
    private String schema;
    /**
     * 分库的后缀 connection context的后缀suffix
     */
    private String suffix;

    public String getSchema() {
        if (this.schema != null) {
            return this.schema;
        }
        String schema = ConfigUtility.getValue(Config.DEFAULT_DATA_SOURCE_KEY);
        if (StringUtility.isNullOrEmpty(schema)) {
            schema = Constant.SPARROW;
        }
        this.schema = schema;
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getSuffix() {
        if (StringUtility.isNullOrEmpty(suffix)) {
            suffix = Constant.DEFAULT;
        }
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    /**
     * Datasource 的实现类的构建参数 bean factory的key
     *
     * @return
     */
    public String getKey() {
        return this.getSchema() + Symbol.UNDERLINE + this.getSuffix();
    }
}
