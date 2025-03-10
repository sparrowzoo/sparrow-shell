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

package com.sparrow.datasource;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

/**
 * 自定义的数据源没有重写statement 等类，这里会获取到原始connection,故实现关联类，以方便close 时释放回链接池
 */
public class ConnectionProxyContainer {
    private Map<Connection, ProxyConnection> container = new HashMap<>();

    public void bind(Connection connection, ProxyConnection proxyConnection) {
        this.container.put(connection, proxyConnection);
    }

    public ProxyConnection getProxyConnection(Connection connection) {
        return this.container.get(connection);
    }

    public void unbind(Connection connection) {
        this.container.remove(connection);
    }
}
