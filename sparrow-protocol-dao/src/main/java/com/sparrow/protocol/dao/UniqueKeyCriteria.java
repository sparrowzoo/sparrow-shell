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
package com.sparrow.protocol.dao;

public class UniqueKeyCriteria {
    /**
     * 返回结果字段
     */
    private String resultFiled;
    /**
     * unique key
     */
    private Object key;
    /**
     * unique filed name default is primary key
     */
    private String uniqueFieldName;

    private UniqueKeyCriteria(String resultFiled, Object key, String uniqueFieldName) {
        this.resultFiled = resultFiled;
        this.key = key;
        this.uniqueFieldName = uniqueFieldName;
    }

    public static UniqueKeyCriteria createUniqueCriteria(Object key, String uniqueFieldName) {
        return new UniqueKeyCriteria(null, key, uniqueFieldName);
    }

    public static UniqueKeyCriteria createFieldCriteria(Object key, String resultFiled) {
        return new UniqueKeyCriteria(resultFiled, key, null);
    }

    public static UniqueKeyCriteria createUniqueWithFieldCriteria(String resultFiled, Object key,
        String uniqueFieldName) {
        return new UniqueKeyCriteria(resultFiled, key, uniqueFieldName);
    }

    public String getResultFiled() {
        return resultFiled;
    }

    public void setResultFiled(String resultFiled) {
        this.resultFiled = resultFiled;
    }

    public Object getKey() {
        return key;
    }

    public void setKey(Object key) {
        this.key = key;
    }

    public String getUniqueFieldName() {
        return uniqueFieldName;
    }

    public void setUniqueFieldName(String uniqueFieldName) {
        this.uniqueFieldName = uniqueFieldName;
    }
}
