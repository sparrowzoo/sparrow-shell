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

package com.sparrow.protocol;

public class LoginUserStatus {
    public static final Integer STATUS_NORMAL = 1;
    public static final Integer STATUS_FREEZE = 2;
    public static final Integer STATUS_DISABLE = 0;


    public LoginUserStatus(Integer status, Long expireAt) {
        this.status = status;
        this.expireAt = expireAt;
    }

    /**
     * 用户状态 1:正常 2:冻结 0：禁用
     */
    private Integer status;

    /**
     * 过期时间
     */
    private Long expireAt;


    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getExpireAt() {
        return expireAt;
    }

    public void setExpireAt(Long expireAt) {
        this.expireAt = expireAt;
    }

}
