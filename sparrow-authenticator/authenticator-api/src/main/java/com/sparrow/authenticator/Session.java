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

package com.sparrow.authenticator;

import com.sparrow.protocol.POJO;

public interface Session extends SessionStatus, SessionKey, POJO {
    // key 静态属性
    Long getUserId();
    Integer getCategory();
    Integer getDeviceType();
    String getHost();

    //状态属性
    SessionStatus getSessionStatus();
    Boolean expire();
    void touch();
    void renewal(Long renewalInterval);
    void modifyStatus(Integer status);
    boolean matchHost(String host);
    boolean exceedSessionTimeout(Long sessionTimeout);
}
