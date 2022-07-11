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
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * 保持线程请求中的数据 与web应用程序解耦
 */
public class ConnectionContextHolderImpl implements ConnectionContextHolder {

    private DataSourceFactory dataSourceFactory;

    /**
     * 这里的功能主要针对thread local 中的映射，将datasource的具体实现分离 datasource 的connection close功能各链接池已实现 请求过程中的事务链接
     * 同一个线程，同一个事务中只允许一个链接
     */
    private ThreadLocal<Map<String, Connection>> transactionContainer = new ThreadLocal<Map<String, Connection>>();

    private Map<Connection, ProxyConnection> originProxyMap = new HashMap<>();

    public void setDataSourceFactory(DataSourceFactory dataSourceFactory) {
        this.dataSourceFactory = dataSourceFactory;
    }

    @Override
    public void addOriginProxy(Connection proxy) {
        if (proxy instanceof ProxyConnection) {
            ProxyConnection proxyConnection = (ProxyConnection) proxy;
            Connection origin = proxyConnection.getOriginConnection();
            this.originProxyMap.put(origin, proxyConnection);
        }
    }

    @Override public DataSourceFactory getDataSourceFactory() {
        return dataSourceFactory;
    }

    private Map<String, Connection> getTransactionHolder() {
        if (this.transactionContainer.get() == null) {
            this.transactionContainer.set(new HashMap<String, Connection>());
        }
        return this.transactionContainer.get();
    }

    @Override public void bindConnection(Connection connection) {
        try {
            DatasourceKey dataSourceKey = this.dataSourceFactory.getDatasourceKey(connection);
            if (!connection.getAutoCommit()) {
                this.getTransactionHolder().put(dataSourceKey.getKey(), connection);
            }
        } catch (SQLException ignore) {
            throw new RuntimeException(ignore);
        }
    }

    @Override public void unbindConnection(Connection connection) {
        try {
            DatasourceKey dataSourceKey = this.dataSourceFactory.getDatasourceKey(connection);
            Connection proxyConnection = this.getConnection(dataSourceKey.getKey());
            if (proxyConnection == null) {
                proxyConnection = this.originProxyMap.get(connection);
                if (proxyConnection != null) {
                    proxyConnection.close();
                } else {
                    connection.close();
                }
                return;
            }
            if (!connection.getAutoCommit()) {
                this.getTransactionHolder().remove(dataSourceKey.getKey());
                proxyConnection.close();
            }
        } catch (SQLException ignore) {
            throw new RuntimeException(ignore);
        }
    }

    @Override public Connection getConnection(String datasourceKey) {
        //以事务为优先，如果当前开启事务，未commit则用事务链接执行
        return this.getTransactionHolder().get(datasourceKey);
    }

    @Override public void removeAll() {
        Map<String, Connection> transactionContainer = this.transactionContainer.get();
        if (transactionContainer != null) {
            for (String key : transactionContainer.keySet()) {
                try {
                    transactionContainer.remove(key).close();
                } catch (SQLException ignore) {
                }
            }
        }
        this.transactionContainer.remove();
    }
}
