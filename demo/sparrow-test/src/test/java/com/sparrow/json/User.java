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

package com.sparrow.json;


import com.sparrow.protocol.POJO;

import java.util.Map;

/**
 * Created by harry on 2015/5/13.
 */
public class User implements POJO {
    public User() {
    }

    public void test() {
        System.out.print("hellow");
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setParent(User parent) {
        this.parent = parent;
    }

    public void setExtend(Map<String, String> extend) {
        this.extend = extend;
    }

    public User(String userId, String userName, User parent, Map<String, String> extend) {
        this.userId = userId;
        this.userName = userName;
        this.parent = parent;
        this.extend = extend;
    }

    private String userId;
    private String userName;
    private User parent;
    private Map<String, String> extend;

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public User getParent() {
        return parent;
    }

    public Map<String, String> getExtend() {
        return extend;
    }
}
