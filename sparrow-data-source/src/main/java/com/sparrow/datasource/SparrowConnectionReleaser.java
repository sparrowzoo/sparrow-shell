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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class SparrowConnectionReleaser implements ConnectionReleaser {
    private Logger logger = LoggerFactory.getLogger(SparrowConnectionReleaser.class);
    private ConnectionProxyContainer connectionProxyContainer;

    public SparrowConnectionReleaser(ConnectionProxyContainer connectionProxyContainer) {
        this.connectionProxyContainer = connectionProxyContainer;
    }

    @Override
    public void release(Connection connection) {
        boolean autoCommit = true;
        try {
            autoCommit = connection.getAutoCommit();
        } catch (SQLException ignored) {
            logger.error("get auto commit error", ignored);
        }
        // 非自动提交，事务，则不关闭连接，交由事务管理器关闭
        if (!autoCommit) {
            return;
        }
        ProxyConnection proxyConnection = this.connectionProxyContainer.getProxyConnection(connection);
        if (proxyConnection != null) {
            proxyConnection.close();
            return;
        }
        try {
            connection.close();
        } catch (SQLException e) {
            logger.error("close connection error", e);
        }
    }
}
