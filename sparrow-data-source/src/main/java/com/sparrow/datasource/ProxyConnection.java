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

import com.sparrow.constant.SysObjectName;
import com.sparrow.container.Container;
import com.sparrow.core.spi.ApplicationContext;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProxyConnection implements Connection {
    private static Logger logger = LoggerFactory.getLogger(ProxyConnection.class);
    private Connection conn = null;
    private ConnectionPool connectionPool;

    public Connection getOriginConnection() {
        return this.conn;
    }

    public ProxyConnection(DatasourceConfig datasourceConfig, ConnectionPool connectionPool) {
        try {
            Class.forName(datasourceConfig.getDriverClassName());

            this.conn = DriverManager.getConnection(datasourceConfig.getUrl(), datasourceConfig.getUsername(), datasourceConfig.getPassword());
            this.connectionPool = connectionPool;
            Container container = ApplicationContext.getContainer();
            ConnectionContextHolder connectionContextHolder = container.getBean(SysObjectName.CONNECTION_CONTEXT_HOLDER);
            connectionContextHolder.addOriginProxy(this);
        } catch (Exception e) {
            logger.error("for driver class name error", e);
        }
    }

    @Override
    public boolean isWrapperFor(Class<?> arg0) throws SQLException {
        return this.conn.isWrapperFor(arg0);
    }

    @Override
    public <T> T unwrap(Class<T> arg0) throws SQLException {
        return this.conn.unwrap(arg0);
    }

    @Override
    public void clearWarnings() throws SQLException {
        this.conn.clearWarnings();
    }

    @Override
    public synchronized void close() {
        System.err.println("proxy connection close");
        if (this.connectionPool == null) {
            try {
                this.conn.close();
            } catch (SQLException e) {
                logger.error("close connection is error", e);
            }
            return;
        }
        try {
            if (this.conn == null) {
                return;
            }
            if (this.connectionPool.usedPool.containsKey(this)) {
                this.connectionPool.usedPool.remove(this);
            }
            if (!this.conn.isClosed()) {
                this.connectionPool.pool.add(this);
            }
        } catch (SQLException e) {
            logger.error("close connection error", e);
        }
        logger.debug("release-->connection pool size:"
            + this.connectionPool.pool.size() + "used pool size:"
            + this.connectionPool.usedPool.size());
    }

    @Override
    public void commit() throws SQLException {
        this.conn.commit();
    }

    @Override
    public Array createArrayOf(String typeName, Object[] elements)
        throws SQLException {
        return this.conn.createArrayOf(typeName, elements);
    }

    @Override
    public Blob createBlob() throws SQLException {
        return this.conn.createBlob();
    }

    @Override
    public Clob createClob() throws SQLException {
        return this.conn.createClob();
    }

    @Override
    public NClob createNClob() throws SQLException {
        return this.conn.createNClob();
    }

    @Override
    public SQLXML createSQLXML() throws SQLException {
        return this.conn.createSQLXML();
    }

    @Override
    public Statement createStatement() throws SQLException {
        return this.conn.createStatement();
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency)
        throws SQLException {
        return this.conn.createStatement(resultSetType, resultSetConcurrency);
    }

    @Override
    public Statement createStatement(int resultSetType,
        int resultSetConcurrency, int resultSetHoldability)
        throws SQLException {
        return this.conn.createStatement(resultSetType, resultSetConcurrency,
            resultSetHoldability);
    }

    @Override
    public Struct createStruct(String typeName, Object[] attributes)
        throws SQLException {
        return this.conn.createStruct(typeName, attributes);
    }

    @Override
    public boolean getAutoCommit() throws SQLException {
        return this.conn.getAutoCommit();
    }

    @Override
    public String getCatalog() throws SQLException {
        return this.conn.getCatalog();
    }

    @Override
    public Properties getClientInfo() throws SQLException {
        return this.conn.getClientInfo();
    }

    @Override
    public String getClientInfo(String name) throws SQLException {
        return this.conn.getClientInfo(name);
    }

    @Override
    public int getHoldability() throws SQLException {
        return this.conn.getHoldability();
    }

    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        return this.conn.getMetaData();
    }

    @Override
    public int getTransactionIsolation() throws SQLException {
        return this.conn.getTransactionIsolation();
    }

    @Override
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        return this.conn.getTypeMap();
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return this.conn.getWarnings();
    }

    @Override
    public boolean isClosed() throws SQLException {
        return this.conn.isClosed();
    }

    @Override
    public boolean isReadOnly() throws SQLException {
        return this.conn.isReadOnly();
    }

    @Override
    public boolean isValid(int timeout) throws SQLException {
        return this.conn.isReadOnly();
    }

    @Override
    public String nativeSQL(String sql) throws SQLException {
        return this.conn.nativeSQL(sql);
    }

    @Override
    public CallableStatement prepareCall(String sql) throws SQLException {
        return this.conn.prepareCall(sql);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType,
        int resultSetConcurrency) throws SQLException {
        return this.conn.prepareCall(sql, resultSetType, resultSetConcurrency);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType,
        int resultSetConcurrency, int resultSetHoldability)
        throws SQLException {
        return this.conn.prepareCall(sql, resultSetType, resultSetConcurrency,
            resultSetHoldability);
    }

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return this.conn.prepareStatement(sql);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys)
        throws SQLException {
        return this.conn.prepareStatement(sql, autoGeneratedKeys);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes)
        throws SQLException {
        return this.conn.prepareStatement(sql, columnIndexes);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, String[] columnNames)
        throws SQLException {
        return this.conn.prepareStatement(sql, columnNames);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType,
        int resultSetConcurrency) throws SQLException {
        return this.conn.prepareStatement(sql, resultSetType,
            resultSetConcurrency);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType,
        int resultSetConcurrency, int resultSetHoldability)
        throws SQLException {
        return this.conn.prepareStatement(sql, resultSetType,
            resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        this.conn.releaseSavepoint(savepoint);
    }

    @Override
    public void rollback() throws SQLException {
        this.conn.rollback();
    }

    @Override
    public void rollback(Savepoint savepoint) throws SQLException {
        this.conn.rollback(savepoint);
    }

    @Override
    public void setAutoCommit(boolean autoCommit) throws SQLException {
        this.conn.setAutoCommit(autoCommit);
    }

    @Override
    public void setCatalog(String catalog) throws SQLException {
        this.conn.setCatalog(catalog);
    }

    @Override
    public void setClientInfo(Properties properties)
        throws SQLClientInfoException {
        this.conn.setClientInfo(properties);
    }

    @Override
    public void setClientInfo(String name, String value)
        throws SQLClientInfoException {
        this.conn.setClientInfo(name, value);
    }

    @Override
    public void setHoldability(int holdability) throws SQLException {
        this.conn.setHoldability(holdability);
    }

    @Override
    public void setReadOnly(boolean readOnly) throws SQLException {
        this.conn.setReadOnly(readOnly);
    }

    @Override
    public Savepoint setSavepoint() throws SQLException {
        return this.conn.setSavepoint();
    }

    @Override
    public Savepoint setSavepoint(String name) throws SQLException {
        return this.conn.setSavepoint(name);
    }

    @Override
    public void setTransactionIsolation(int level) throws SQLException {
        this.conn.setTransactionIsolation(level);
    }

    @Override
    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        this.conn.setTypeMap(map);
    }

    /**
     * since jdk 1.7
     *
     * @param arg0
     */
    public void abort(Executor arg0) throws SQLException {

    }

    /**
     * since jdk 1.7
     *
     * @return
     * @throws SQLException
     */
    public int getNetworkTimeout() throws SQLException {
        return 0;
    }

    /**
     * since jdk 1.7
     *
     * @return
     * @throws SQLException
     */
    public String getSchema() throws SQLException {
        return null;
    }

    /**
     * since jdk 1.7
     *
     * @param arg0
     * @param arg1
     * @throws SQLException
     */
    public void setNetworkTimeout(Executor arg0, int arg1) throws SQLException {

    }

    /**
     * since jdk 1.7
     *
     * @param arg0
     * @throws SQLException
     */
    public void setSchema(String arg0) throws SQLException {
    }
}
