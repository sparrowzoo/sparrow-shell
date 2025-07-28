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
public enum ListDatasourceType implements EnumIdentityAccessor {
    NULL(1),
    DICTIONARY(2),
    TABLE(3),
    ENUM(4);

    private final int identity;

    ListDatasourceType(Integer identity) {
        this.identity = identity;
    }

    public static ListDatasourceType getById(int identity) {
        for (ListDatasourceType type : ListDatasourceType.values()) {
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
}
