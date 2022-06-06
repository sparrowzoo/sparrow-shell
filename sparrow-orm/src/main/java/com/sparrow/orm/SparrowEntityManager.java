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
import com.sparrow.protocol.constant.magic.SYMBOL;
import com.sparrow.protocol.enums.DATABASE_SPLIT_STRATEGY;
import com.sparrow.utility.StringUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


/**
 * @author harry
 */
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

    public int getDatabaseSplitMaxId() {
        return databaseSplitMaxId;
    }

    public String getTableSuffix(Map<Integer, Object> suffixParameters) {
        List<Object> tableSuffix = new ArrayList<Object>();
        for (Integer key : suffixParameters.keySet()) {
            tableSuffix.add(suffixParameters.get(key));
        }
        return getTableSuffix(tableSuffix);
    }

    public String getTableSuffix(List<Object> suffixParameters) {
        Map<Integer, String> resultSuffix = new TreeMap<Integer, String>();
        for (Field field : this.getHashFieldList()) {
            if (suffixParameters.size() <= 0 || field.getHashIndex() > suffixParameters.size()) {
                continue;
            }
            Object parameter = null;
            if (suffixParameters.size() > 0) {
                parameter = suffixParameters.get(field.getHashIndex());
            }

            if (parameter == null) {
                continue;
            }
            String hash = null;
            switch (field.getHashStrategy()) {
                case HASH:
                    //only hash 不是数字
                    Long hashKey = Long.valueOf(parameter.toString());
                    if (hashKey == -1) {
                        logger.warn("hashKey is -1");
                        break;
                    }
                    hash = String.valueOf(hashKey % this.getTableBucketCount());
                    break;
                case ONLY_HASH:
                    hash = parameter.toString().toLowerCase();
                    break;
                default:
            }
            resultSuffix.put(field.getHashIndex(), hash);
        }
        if (resultSuffix.size() > 0) {
            return SYMBOL.UNDERLINE + StringUtility.join(resultSuffix, SYMBOL.UNDERLINE);
        }
        return SYMBOL.EMPTY;
    }

    public DATABASE_SPLIT_STRATEGY getDatabaseSplitStrategy() {
        if (databaseSplitStrategy == null) {
            databaseSplitStrategy = DATABASE_SPLIT_STRATEGY.DEFAULT;
        }
        return databaseSplitStrategy;
    }

    public List<Field> getHashFieldList() {
        return hashFieldList;
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
