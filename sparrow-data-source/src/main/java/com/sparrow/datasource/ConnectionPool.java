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

import com.sparrow.concurrent.SparrowThreadFactory;
import com.sparrow.container.Container;
import com.sparrow.container.ContainerAware;
import com.sparrow.support.EnvironmentSupport;
import com.sparrow.utility.StringUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Mysql数据库链接池
 */
public class ConnectionPool implements DataSource, ContainerAware {
    private static Logger logger = LoggerFactory.getLogger(ConnectionPool.class);
    private int openedConnectionCount = 0;
    private int closedConnectionCount = 0;

    private PrintWriter logWriter;
    /**
     * 登录超时时间，默认为5分钟
     */
    private int loginTimeout = 5;
    /**
     * 数据池数组对象 <p> connectionPool加最后一次使用时间
     */
    public List<Connection> pool;

    public Map<Connection, Long> usedPool;

    private DatasourceConfig connectionConfig;

    private ConnectionProxyContainer connectionProxyContainer;


    public ConnectionPool(ConnectionProxyContainer connectionProxyContainer) {
        this.connectionProxyContainer = connectionProxyContainer;
    }

    public ConnectionPool(String datasourceKey, ConnectionProxyContainer connectionProxyContainer) {
        this.connectionProxyContainer = connectionProxyContainer;
        this.aware(null, datasourceKey);
    }


    /**
     * 读取数据库源 <p> data source key 与 connection url 映射 data source+suffix determine datasource data
     * source+database_split_key determine jdbc template
     *
     * @param dataSourceKey
     * @return
     */
    private DatasourceConfig getDatasourceConfig(String dataSourceKey) {
        if (StringUtility.isNullOrEmpty(dataSourceKey)) {
            dataSourceKey = "sparrow_default";
        }
        DatasourceKey dsKey = DatasourceKey.parse(dataSourceKey);
        DatasourceConfig datasourceConfig = new DatasourceConfig();
        try {
            Properties props = new Properties();
            String filePath = "/" + dataSourceKey + ".properties";
            String schema = dsKey.getSchema();
            props.load(EnvironmentSupport.getInstance().getFileInputStream(filePath));
            datasourceConfig.setDriverClassName(props.getProperty(schema + ".driverClassName"));
            datasourceConfig.setUsername(props.getProperty(schema + ".username"));
            String envPasswordKey = "mysql_" + schema + "_password";
            String password = System.getenv(envPasswordKey);
            logger.info("password_key {},password {}", envPasswordKey, password);
            datasourceConfig.setPassword(password);
            if (StringUtility.isNullOrEmpty(datasourceConfig.getPassword())) {
                datasourceConfig.setPassword(props.getProperty(schema + ".password"));
            }
            datasourceConfig.setUrl(props.getProperty(schema + ".url"));
            datasourceConfig.setPoolSize(Integer.parseInt(props.getProperty(schema + ".poolSize")));
        } catch (Exception ignore) {
            throw new RuntimeException(ignore);
        }
        return datasourceConfig;
    }

    /**
     * 初始化连接池
     *
     * @param container      当前集合
     * @param datasourceName bean 名称
     */
    @Override
    public void aware(Container container, String datasourceName) {
        this.connectionConfig = this.getDatasourceConfig(datasourceName);
        pool = new ArrayList<>(this.connectionConfig.getPoolSize());
        this.usedPool = new ConcurrentHashMap<Connection, Long>(this.connectionConfig.getPoolSize());
        for (int i = 0; i < this.connectionConfig.getPoolSize(); i++) {
            pool.add(this.newConnection());
        }

        ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1, new SparrowThreadFactory.Builder().namingPattern("datasource-monitor-%d").daemon(true).build());

        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                Map<Connection, Long> usedPool = ConnectionPool.this.usedPool;
                for (Connection c : usedPool.keySet()) {
                    if (ConnectionPool.this.loginTimeout == 0) {
                        return;
                    }
                    if ((System.currentTimeMillis() - usedPool.get(c)) / 1000 / 60 > ConnectionPool.this.getLoginTimeout()) {
                        //expire connection
                        try {
                            ConnectionPool.this.release(c);
                        } catch (SQLException e) {
                            logger.error("connection release error", e);
                        }
                    }
                }
            }
        }, 5, 1, TimeUnit.SECONDS);
    }

    /**
     * 关闭连接池中的所有数据库连接
     */
    public synchronized void closePool() {
        for (int i = 0; i < pool.size(); i++) {
            try {
                (pool.get(i)).close();
            } catch (SQLException e) {
                logger.error("close pool error", e);
            }
            pool.remove(i);
        }
    }

    /**
     * 从链接池中获取链接对象
     *
     * @return Connection
     */
    @Override
    public synchronized Connection getConnection() {
        while (true) {
            Connection conn;
            if (pool.size() > 0) {
                // 如果池中存在链接对象则从池获取
                conn = pool.remove(0);
                try {
                    if (conn == null || conn.isClosed()) {
                        // 被物理关掉了，或其他原因丢掉链接
                        this.closedConnectionCount++;
                    }
                } catch (SQLException e) {
                    logger.error("connection error", e);
                }
            } else {
                // 如果池中不存在
                conn = newConnection();
            }
            // 从池中获取默认为自动提交
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    this.usedPool.put(conn, System.currentTimeMillis());
                    return conn;
                }
            } catch (SQLException e) {
                logger.error("set auto commit error", e);
            }
        }
    }

    public synchronized void release(Connection c) throws SQLException {
        if (c == null) {
            return;
        }
        usedPool.remove(c);
        if (!c.isClosed()) {
            pool.add(c);
        } else {
            this.connectionProxyContainer.unbind(c);
        }
    }

    private Connection newConnection() {
        Connection conn = null;
        // 此处不要放入池中.release时即放回池中
        try {
            conn = new ProxyConnection(this.connectionConfig, this, this.connectionProxyContainer);
            // 打开过的所有新链接
            this.openedConnectionCount++;
        } catch (Exception e) {
            logger.error("new connection error", e);
        }
        return conn;
    }

    @Override
    public synchronized PrintWriter getLogWriter() throws SQLException {
        return this.logWriter;
    }

    @Override
    public int getLoginTimeout() {
        return this.loginTimeout;
    }

    @Override
    public void setLogWriter(PrintWriter arg0) throws SQLException {
        this.logWriter = arg0;
    }

    @Override
    public void setLoginTimeout(int arg0) {
        this.loginTimeout = arg0;
    }

    @Override
    public boolean isWrapperFor(Class<?> arg0) throws SQLException {
        return DataSource.class.equals(arg0);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T unwrap(Class<T> arg0) throws SQLException {
        if (this.isWrapperFor(arg0)) {
            return (T) arg0;
        } else {
            throw new SQLException("no object found that implements the interface");
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.connectionConfig);
        sb.append(" information list");
        sb.append("<br/>");
        sb.append(" opened connection count:");
        sb.append(this.openedConnectionCount);
        sb.append("<br/>");
        sb.append("physical closed connection count:");
        sb.append(this.closedConnectionCount);
        sb.append("<br/>");
        sb.append("pool connection count:");
        sb.append(this.pool.size());
        sb.append("<br/>");
        sb.append("used pool connection count:");
        sb.append(this.usedPool.size());
        return sb.toString();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        throw new SQLException("not support getConnection(String username, String password) method");
    }

    //jdk1.6 without this api
    public java.util.logging.Logger getParentLogger() {
        return null;
    }
}
