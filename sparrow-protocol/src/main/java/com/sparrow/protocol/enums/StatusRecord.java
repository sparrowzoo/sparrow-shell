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

package com.sparrow.protocol.enums;

import com.sparrow.protocol.EnumIdentityAccessor;
import com.sparrow.protocol.EnumUniqueName;

/**
 * 记录状态
 *
 * @version 1.0
 */
@EnumUniqueName(name = "statusRecord")
public enum StatusRecord implements EnumIdentityAccessor {
    /**
     * 被屏蔽(0)
     */
    DISABLE,
    /**
     * 可用(1)
     */
    ENABLE;
    private final int identity;

    StatusRecord() {
        this.identity = ordinal();
    }

    @Override
    public Integer getIdentity() {
        return identity;
    }

    public static StatusRecord valueOf(int identity) {
        for (StatusRecord status : values()) {
            if (status.getIdentity() == identity) {
                return status;
            }
        }
        return null;
    }
}
