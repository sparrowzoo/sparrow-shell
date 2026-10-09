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

public enum HttpTarget implements EnumIdentityAccessor {
    CONTENT(1, "content"),
    /**
     * 新页面
     */
    BLANK(2, "_blank"),
    /**
     * 当前自己
     */
    SELF(3, "_self"),

    /**
     * 父框架
     */
    PARENT(4, "_parent"),
    /**
     * 顶部
     */
    TOP(5, "_top");


    HttpTarget(int id, String target) {
        this.id = id;
        this.target = target;
    }

    private int id;
    private String target;

    @Override
    public Integer getIdentity() {
        return this.id;
    }

    public int getId() {
        return id;
    }

    public String getTarget() {
        return target;
    }
}
