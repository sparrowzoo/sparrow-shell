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

package com.sparrow.orm;

import com.sparrow.protocol.constant.magic.Symbol;
import com.sparrow.protocol.dao.enums.DatabaseSplitStrategy;
import com.sparrow.utility.StringUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class SparrowEntityManager extends AbstractEntityManagerAdapter {
    private static Logger logger = LoggerFactory.getLogger(SparrowEntityManager.class);

    public SparrowEntityManager(Class clazz) {
        super(clazz);
    }

    @Override
    public void init(Class clazz) {

    }

    @Override
    public String parsePropertyParameter(String column, String property) {
        return "?";
    }

    public int getTableBucketCount() {
        return tableBucketCount;
    }

    /**
     * insert update 通过注解解析表后缀
     *
     * @param suffixParameters
     * @return
     */
    public String getTableSuffix(Map<Integer, Object> suffixParameters) {
        List<Object> tableSuffix = new ArrayList<Object>();
        for (Integer key : suffixParameters.keySet()) {
            tableSuffix.add(suffixParameters.get(key));
        }
        return getTableSuffix(tableSuffix);
    }

    /**
     * 自定义条件，手动设置表
     *
     * @param suffixParameters 与hashFieldMap 一一映射 suffixParameters为运行时通过参数获取， hashFieldMap 为对象载时获取
     * @return
     */
    public String getTableSuffix(List<Object> suffixParameters) {
        Map<Integer, String> resultSuffix = new TreeMap<Integer, String>();
        if (suffixParameters.size() <= 0) {
            return "";
        }
        int i = 0;
        //hash index 不一定是连接的
        for (Integer index : this.hashFieldMap.keySet()) {
            Object parameter = null;
            if (suffixParameters.size() > 0) {
                parameter = suffixParameters.get(i++);
            }

            if (parameter == null) {
                continue;
            }
            String hash = null;
            Field field = this.hashFieldMap.get(index);
            switch (field.getHashStrategy()) {
                case HASH:
                    Long hashKey = Long.valueOf(parameter.toString());
                    if (hashKey == -1) {
                        logger.warn("hashKey is -1");
                        break;
                    }
                    hash = String.valueOf(hashKey % this.getTableBucketCount());
                    break;
                case ORIGIN:
                case ORIGIN_NOT_PERSISTENCE:
                    //only hash 不是数字
                    hash = parameter.toString().toLowerCase();
                    break;
                default:
            }
            resultSuffix.put(field.getHashIndex(), hash);
        }
        if (resultSuffix.size() > 0) {
            return Symbol.UNDERLINE + StringUtility.join(resultSuffix, Symbol.UNDERLINE);
        }
        return Symbol.EMPTY;
    }

    public DatabaseSplitStrategy getDatabaseSplitStrategy() {
        if (databaseSplitStrategy == null) {
            databaseSplitStrategy = DatabaseSplitStrategy.DEFAULT;
        }
        return databaseSplitStrategy;
    }

    public void parseField(Field field, List<Parameter> parameters, Object o, Map<Integer, Object> tableSuffix,
        boolean update) {
        if (field.isPersistence()) {
            if (!update || field.isUpdatable()) {
                parameters.add(new Parameter(field, o));
            }
        }
        if (field.getHashIndex() > -1) {
            tableSuffix.put(field.getHashIndex(), o);
        }
    }
}
