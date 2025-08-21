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

package com.sparrow.context;

import com.sparrow.protocol.ClientInformation;
import com.sparrow.protocol.LoginUser;

public class SessionContext {
    private static InheritableThreadLocal<LoginUser> loginTokenThreadLocal = new InheritableThreadLocal<LoginUser>();
    private static InheritableThreadLocal<ClientInformation> clientThreadLocal = new InheritableThreadLocal<ClientInformation>();

    public static void bindLoginUser(LoginUser loginToken) {
        loginTokenThreadLocal.set(loginToken);
    }

    public static LoginUser getLoginUser() {
        return loginTokenThreadLocal.get();
    }

    public static ClientInformation getClientInfo() {
        return clientThreadLocal.get();
    }

    public static void bindClientInfo(ClientInformation client) {
        clientThreadLocal.set(client);
    }

    public static void clearToken() {
        loginTokenThreadLocal.remove();
    }

    public static void clearClient() {
        clientThreadLocal.remove();
    }
}
