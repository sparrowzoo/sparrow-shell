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

package com.sparrow.orm.transaction;

import com.sparrow.datasource.ConnectionContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 保持线程请求中的数据 与web应用程序解耦
 */
public class ConnectionContextHolderImpl implements ConnectionContextHolder {

    private Logger logger = LoggerFactory.getLogger(ConnectionContextHolderImpl.class);
    /**
     * 这里的功能主要针对thread local 中的映射，
     * <p>
     * 将datasource的具体实现分离 datasource 的connection close功能各链接池已实现
     * <p>
     * 请求过程中的事务链接 同一个线程,同一个时刻，只会有一个事务，只允许一个链接
     */
    private ThreadLocal<Connection> localConnectionHolder = new ThreadLocal<Connection>();


    @Override
    public Connection getConnection() {
        return this.localConnectionHolder.get();
    }

    @Override
    public void bindConnection(Connection proxyConnection) {
        this.localConnectionHolder.set(proxyConnection);
    }

    @Override
    public void unbindConnection(Connection originConnection) {
        Connection connection = this.localConnectionHolder.get();
        this.localConnectionHolder.remove();
        try {
            connection.close();
        } catch (SQLException e) {
            logger.error("connection close error", e);
        }
    }

    @Override
    public void removeAll() {
        Connection connection = this.localConnectionHolder.get();
        try {
            connection.close();
        } catch (SQLException ignore) {
        }
        this.localConnectionHolder.remove();
    }
}
