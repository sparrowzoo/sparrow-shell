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
import java.util.Stack;

/**
 * 保持线程请求中的数据 与web应用程序解耦
 *
 * @author harry
 */
public class ConnectionContextHolderImpl implements ConnectionContextHolder {

    private DataSourceFactory dataSourceFactory;

    /**
     * 这里的功能主要针对thread local 中的映射，将datasource的具体实现分离
     * datasource 的connection close功能各链接池已实现
     * 请求过程中的事务链接 同一个线程，同一个事务中只允许一个链接
     */
    private ThreadLocal<Map<String, Connection>> transactionContainer = new ThreadLocal<Map<String, Connection>>();

    /**
     * 请求过程中的普通数据库链接
     * 同一个线程，可能查不同的数据源
     */
    private ThreadLocal<Map<String, Stack<Connection>>> connectionContainer = new ThreadLocal<Map<String, Stack<Connection>>>();

    public void setDataSourceFactory(DataSourceFactory dataSourceFactory) {
        this.dataSourceFactory = dataSourceFactory;
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

    private Map<String, Stack<Connection>> getConnectionHolder() {
        if (this.connectionContainer.get() == null) {
            this.connectionContainer.set(new HashMap<String, Stack<Connection>>());
        }
        return this.connectionContainer.get();
    }

    @Override public void bindConnection(Connection connection) {
        try {
            DatasourceKey dataSourceKey = this.dataSourceFactory.getDatasourceKey(connection);
            if (!connection.getAutoCommit()) {
                this.getTransactionHolder().put(dataSourceKey.getKey(), connection);
                return;
            }
            Stack<Connection> stack = this.getConnectionHolder().get(dataSourceKey.getKey());
            if (stack == null) {
                stack = new Stack<Connection>();
                this.getConnectionHolder().put(dataSourceKey.getKey(), stack);
            }
            stack.push(connection);
        } catch (SQLException ignore) {
            throw new RuntimeException(ignore);
        }
    }

    @Override public void unbindConnection(Connection connection) {
        try {
            DatasourceKey dataSourceKey = this.dataSourceFactory.getDatasourceKey(connection);
            Connection proxyConnection = this.getConnection(dataSourceKey.getKey());
            if (proxyConnection == null) {
                return;
            }
            if (!connection.getAutoCommit()) {
                this.getTransactionHolder().remove(dataSourceKey.getKey());
            } else {
                Stack<Connection> connectionStack=this.getConnectionHolder().get(dataSourceKey.getKey());
                connectionStack.pop();
            }
            proxyConnection.close();
        } catch (SQLException ignore) {
            throw new RuntimeException(ignore);
        }
    }

    @Override public Connection getConnection(String datasourceKey) {
        //以事务为优先，如果当前开启事务，未commit则用事务链接执行
        Connection connection = this.getTransactionHolder().get(datasourceKey);
        if (connection != null) {
            return connection;
        }
        Stack<Connection> connectionStack = this.getConnectionHolder().get(datasourceKey);
        if (connectionStack != null && connectionStack.size() > 0) {
            connection = connectionStack.peek();
        }
        return connection;
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
        Map<String, Stack<Connection>> connectionContainer =
                this.connectionContainer.get();
        if (connectionContainer != null) {
            for (String key : connectionContainer.keySet()) {
                Stack<Connection> connectionStack = connectionContainer.get(key);
                while (!connectionStack.empty()) {
                    try {
                        connectionStack.pop().close();
                    } catch (SQLException ignore) {
                    }
                }
            }
        }
        this.transactionContainer.remove();
        this.connectionContainer.remove();
    }
}
