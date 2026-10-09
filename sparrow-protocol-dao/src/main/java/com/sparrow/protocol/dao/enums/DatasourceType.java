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

import com.sparrow.protocol.EnumIdentityAccessor;
import com.sparrow.protocol.EnumUniqueName;

@EnumUniqueName(name = "datasourceType")
public enum DatasourceType implements EnumIdentityAccessor {
    NULL(0),
    DICTIONARY(2),
    TABLE(3),
    ENUM(4),
    UPLOAD(5);

    private final int identity;

    DatasourceType(Integer identity) {
        this.identity = identity;
    }

    public static DatasourceType getById(int identity) {
        for (DatasourceType type : DatasourceType.values()) {
            if (type.identity == identity) {
                return type;
            }
        }
        return NULL;
    }

    @Override
    public Integer getIdentity() {
        return this.identity;
    }

    public static Boolean isList(DatasourceType datasourceType) {
        return TABLE.equals(datasourceType) ||
                ENUM.equals(datasourceType) ||
                DICTIONARY.equals(datasourceType);
    }

    public static Boolean isList(Integer datasourceType) {
        return TABLE.getIdentity().equals(datasourceType) ||
                ENUM.getIdentity().equals(datasourceType) ||
                DICTIONARY.getIdentity().equals(datasourceType);
    }
}
