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

import com.sparrow.protocol.UserCategory;

/**
 * 用户类别，一个用户只能有一个类别，在注册时确定，可以切换。但同一时刻只能有一个
 * ADMIN是角色不是用户类别
 */
public enum DefaultUserCategory implements UserCategory {
    VISITOR(0), REGISTER(1), AI(2), SERVER(3);

    private final int id;

    DefaultUserCategory(int id) {
        this.id = id;
    }

    @Override
    public Integer getIdentity() {
        return this.id;
    }

    @Override
    public String getDisplayName() {
        return this.name();
    }
}
