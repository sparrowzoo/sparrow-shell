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

package com.sparrow.protocol.context;

import com.sparrow.protocol.ClientInformation;
import com.sparrow.protocol.LoginUser;

/**
 * Authentication data bound to the current thread and inherited by newly created child threads.
 */
public final class SessionContext {
    private static final InheritableThreadLocal<LoginUser> LOGIN_USER = new InheritableThreadLocal<>();
    private static final InheritableThreadLocal<ClientInformation> CLIENT_INFO = new InheritableThreadLocal<>();

    private SessionContext() {
    }

    public static void bindLoginUser(LoginUser loginUser) {
        LOGIN_USER.set(loginUser);
    }

    public static LoginUser getLoginUser() {
        return LOGIN_USER.get();
    }

    public static void clearToken() {
        LOGIN_USER.remove();
    }

    public static void bindClientInfo(ClientInformation client) {
        CLIENT_INFO.set(client);
    }

    public static ClientInformation getClientInfo() {
        return CLIENT_INFO.get();
    }

    public static void clearClient() {
        CLIENT_INFO.remove();
    }
}
