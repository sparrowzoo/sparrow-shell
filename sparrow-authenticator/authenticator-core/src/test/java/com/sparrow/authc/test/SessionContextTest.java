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

package com.sparrow.authc.test;

import com.sparrow.authenticator.DefaultLoginUser;
import com.sparrow.protocol.context.SessionContext;
import com.sparrow.protocol.ClientInformation;
import com.sparrow.protocol.LoginUser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

public class SessionContextTest {
    @AfterEach
    void clear() {
        SessionContext.clearToken();
        SessionContext.clearClient();
    }

    @Test
    void childInheritsBothValuesAndCanClearItsOwnBindings() throws InterruptedException {
        LoginUser user = new DefaultLoginUser();
        ClientInformation client = new ClientInformation();
        SessionContext.bindLoginUser(user);
        SessionContext.bindClientInfo(client);
        AtomicReference<LoginUser> inheritedUser = new AtomicReference<>();
        AtomicReference<ClientInformation> inheritedClient = new AtomicReference<>();
        AtomicReference<LoginUser> clearedUser = new AtomicReference<>(user);
        AtomicReference<ClientInformation> clearedClient = new AtomicReference<>(client);
        Thread child = new Thread(() -> {
            inheritedUser.set(SessionContext.getLoginUser());
            inheritedClient.set(SessionContext.getClientInfo());
            SessionContext.clearToken();
            SessionContext.clearClient();
            clearedUser.set(SessionContext.getLoginUser());
            clearedClient.set(SessionContext.getClientInfo());
        });
        child.start();
        child.join();
        assertSame(user, inheritedUser.get());
        assertSame(client, inheritedClient.get());
        assertNull(clearedUser.get());
        assertNull(clearedClient.get());
        assertSame(user, SessionContext.getLoginUser());
        assertSame(client, SessionContext.getClientInfo());
    }
}
