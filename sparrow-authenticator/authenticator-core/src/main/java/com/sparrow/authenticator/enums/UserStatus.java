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

package com.sparrow.authenticator.enums;

import com.sparrow.protocol.EnumIdentityAccessor;

public enum UserStatus implements EnumIdentityAccessor {

    /**
     * 正常状态
     */
    NORMAL(1),
    /**
     * 禁用状态
     */
    DISABLED(0),
    /**
     * 踢出状态
     */
    KICK_OUT(-1),
    /**
     * 设备踢出状态
     */
    DEVICE_KICK_OUT(-2),
    /**
     * 被顶状态
     */
    REPLACED(-3);

    private final Integer identity;
    UserStatus(Integer identity) {
        this.identity = identity;
    }
    @Override
    public Integer getIdentity() {
        return null;
    }
}
