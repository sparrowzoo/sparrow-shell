/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sparrow.protocol.enums;

import com.sparrow.protocol.EnumIdentityAccessor;

public enum PermissionType implements EnumIdentityAccessor {
    /**
     * 系统菜单(后台管理的版块)
     */
    MENU(1),
    /**
     * 系统页面(后台管理的页面)
     */
    PAGE(2),
    /**
     * 事件(后台操作的url)
     */
    EVENT(3);

    PermissionType(int id) {
        this.id = id;
    }

    private int id;

    @Override
    public Integer getIdentity() {
        return this.id;
    }
}
