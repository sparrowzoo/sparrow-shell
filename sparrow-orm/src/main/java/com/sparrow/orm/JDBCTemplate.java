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

package com.sparrow.orm;

import com.sparrow.protocol.constant.Constant;
import com.sparrow.constant.SysObjectName;
import com.sparrow.protocol.constant.magic.Symbol;
import com.sparrow.core.spi.ApplicationContext;
import com.sparrow.datasource.ConnectionContextHolder;
import com.sparrow.datasource.DatasourceKey;
import com.sparrow.orm.type.TypeHandler;
import com.sparrow.orm.type.TypeHandlerRegistry;
import com.sparrow.protocol.dao.enums.DatabaseSplitStrategy;
import com.sparrow.support.web.HttpContext;
import com.sparrow.utility.StringUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;

/**
 * 兼顾事务异常必须全部抛出
 */
public class JDBCTemplate implements JDBCSupport {
    private static Logger logger = LoggerFactory.getLogger(JDBCTemplate.class);
    /**
     * *********************************** 变量定义 *******************************************************************
     */
    private static Map<String, JDBCSupport> executorPool = new ConcurrentHashMap<String, JDBCSupport>();

    /**
     * schema与dataSourceSplitStrategy 两者唯一标识一个jdbc template 通过这两个信息可以唯一确定一个数据源（唯一数据库）
     */
    private String schema;
    private DatabaseSplitStrategy dataSourceSplitStrategy;

    /**
     * 连接支持器
     */
    private ConnectionContextHolder getConnectionHolder() {
        return ApplicationContext.getContainer().getBean(SysObjectName.CONNECTION_CONTEXT_HOLDER);
    }

    /**
     * 串行执行 而且只加载一次
     *
     * @return
     */
    public static JDBCSupport getInstance(String schema, DatabaseSplitStrategy databaseSplitStrategy) {

        if (databaseSplitStrategy == null) {
            databaseSplitStrategy = DatabaseSplitStrategy.DEFAULT;
        }
        if (StringUtility.isNullOrEmpty(schema)) {
            schema = DatasourceKey.getDefault().getSchema();
        }
        String jdbcIdentify = schema + "_" + databaseSplitStrategy.toString().toLowerCase();
        if (schema != null && executorPool.containsKey(jdbcIdentify)) {
            return executorPool.get(jdbcIdentify);
        }
        JDBCSupport jdbcSupport = new JDBCTemplate(schema, databaseSplitStrategy);
        executorPool.put(jdbcIdentify, jdbcSupport);
        return jdbcSupport;
    }

    public static JDBCSupport getInstance() {
        return getInstance(null, null);
    }

    private JDBCTemplate(String schema, DatabaseSplitStrategy databaseSplitStrategy) {
        if (StringUtility.isNullOrEmpty(schema)) {
            schema = DatasourceKey.getDefault().getSchema();
        }
        this.schema = schema;
        this.dataSourceSplitStrategy = databaseSplitStrategy;
    }

    /************************************* 执行基础SQL调用 参数与存储过程 ***************************************************/

    /**
     * 设置参数
     * <p/>
     * 参数类型未知的情况下调用
     * <p/>
     * 仅提供给ORM 新增更新时，ORM反射未知数据类型时使用
     *
     * @param preparedStatement
     * @param parameter
     * @param index
     */
    private void bindParameter(PreparedStatement preparedStatement,
        Parameter parameter, int index) {
        Object value = parameter.getParameterValue();
        Class<?> fieldType = parameter.getType();
        TypeHandlerRegistry typeHandlerRegistry = TypeHandlerRegistry.getInstance();
        if (fieldType == null && value != null) {
            fieldType = value.getClass();
        }

        try {
            TypeHandler typeHandler = typeHandlerRegistry.getTypeHandler(fieldType, null);
            typeHandler.setParameter(preparedStatement, index, value);
            logger.debug("JDBCTemplate set SQLParameter error sqlType {} not exist", fieldType);
        } catch (Exception e) {
            logger.error(
                "Executor JDBCTemplate error attribute:"
                    + parameter.getName() + " value:" + value
                    + " type:" + fieldType, e);
            throw new RuntimeException(e);
        }
    }

    private String getDataSourceSuffix() {
        HttpContext httpContext = HttpContext.getContext();
        String suffix = null;
        switch (this.dataSourceSplitStrategy) {
            case LANGUAGE:
                suffix = (String) httpContext.get(Constant.REQUEST_LANGUAGE);
                break;
            case USER_DEFINED:
                suffix = (String) httpContext.get(Constant.REQUEST_DATABASE_SUFFIX);
                break;
            default:
                suffix = "default";
                break;
        }
        return suffix;
    }

    /**
     * 获取数据库连接
     *
     * @return
     * @see DatasourceKey schema+suffix（运行时确定）定位唯一 datasource
     * <p>
     * @see com.sparrow.datasource.DatasourceKey 与 connection url一致，在
     * @see com.sparrow.datasource.DataSourceFactory 中已做映射 schema+ DATABASE_SPLIT_STRATEGY（注解） 确定唯一 jdbc template（最小粒度）
     */
    private synchronized Connection getConnection() {
        ConnectionContextHolder connectionHolder = this.getConnectionHolder();
        DatasourceKey dataSourceKey = new DatasourceKey(this.schema, this.getDataSourceSuffix());
        Connection connection = connectionHolder.getConnection(dataSourceKey.getKey());
        //当前未绑定链接或已经绑定但不是事务
        try {
            if (connection == null || connection.getAutoCommit()) {
                // 新连接并与当前线程绑定
                DataSource dataSource = connectionHolder.getDataSourceFactory().getDataSource(dataSourceKey.getKey());
                connection = dataSource.getConnection();
                //不管是否为事务都需要绑定到线程上，以便执行完后关闭proxyConnection
                //(ProxyConnection)connection会报错，故getConnection之后无法放回池中
                connectionHolder
                    .bindConnection(connection);
            }
        } catch (SQLException e) {
            logger.error("get connection error", e);
        }
        return connection;
    }

    /**
     * 获取PreparedStatement对象用于参数化SQL的执行
     *
     * @param jdbcParameter
     * @return
     */
    private PreparedStatement getPreparedStatement(JDBCParameter jdbcParameter) {
        PreparedStatement preparedStatement = null;
        Connection connection = null;
        ConnectionContextHolder connectionContextHolder = this.getConnectionHolder();
        try {
            connection = this.getConnection();
            connection.setReadOnly(jdbcParameter.isReadOnly());
            if (jdbcParameter.isAutoIncrement()) {
                preparedStatement = connection.prepareStatement(jdbcParameter.getCommand(),
                    Statement.RETURN_GENERATED_KEYS);
            } else {
                // 存储过程
                if (jdbcParameter.getCommand().trim().toLowerCase().startsWith("call")) {
                    if (jdbcParameter.isReadOnly()) {
                        preparedStatement = connection.prepareCall(jdbcParameter.getCommand(),
                            ResultSet.TYPE_SCROLL_INSENSITIVE,
                            ResultSet.CONCUR_READ_ONLY);
                    } else {
                        preparedStatement = connection.prepareCall(jdbcParameter.getCommand());
                    }
                } else {
                    if (jdbcParameter.isReadOnly()) {
                        preparedStatement = connection.prepareStatement(jdbcParameter.getCommand(),
                            ResultSet.TYPE_SCROLL_INSENSITIVE,
                            ResultSet.CONCUR_READ_ONLY);
                    } else {
                        preparedStatement = connection.prepareStatement(jdbcParameter.getCommand());
                    }
                }
            }
            for (int i = 0; i < jdbcParameter.getParameters().size(); i++) {
                this.bindParameter(preparedStatement, jdbcParameter.getParameters().get(i), i + 1);
            }
            return preparedStatement;
        } catch (Exception e) {
            if (connection != null) {
                //自动提交，非事务
                boolean autoCommit = false;
                try {
                    autoCommit = connection.getAutoCommit();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                if (!autoCommit) {
                    connectionContextHolder.unbindConnection(connection);
                } else {
                    //如果是事务则抛出异常 rollback
                    throw new RuntimeException(e);
                }
            }
            logger.error("connection error", e);
            return null;
        } finally {
            String commandString = jdbcParameter.getCommand();
            for (Parameter parameter : jdbcParameter.getParameters()) {
                Object parameterValue = parameter.getParameterValue();
                if (parameterValue == null) {
                    parameterValue = Symbol.EMPTY;
                }
                commandString = commandString.replaceFirst("\\?",
                    Matcher.quoteReplacement(parameterValue.toString()));
            }
            logger.debug("SQL:" + commandString);
        }
    }

    /*************************************** 执行更新操作(增删改) ***********************************************************/
    /**
     * 执行多条更新语句
     *
     * @param commandString
     */
    @Override
    public void executeUpdate(String[] commandString) {
        Statement statement = null;
        try {
            statement = this.getConnection().createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        for (String command : commandString) {
            try {
                statement.addBatch(command);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            logger.debug("BATCH SQL:" + command);
        }
        try {
            statement.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            this.release(statement);
        }
    }

    /**
     * 执行一条非参数化的更新语句.
     *
     * @param commandString
     */
    @Override
    public int executeUpdate(String commandString) {
        Connection connection = this.getConnection();
        Statement statement = null;
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            return statement.executeUpdate(commandString);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            logger.debug("SQL:" + commandString);
            this.release(statement);
        }
    }

    /**
     * 执行一条参数化的更新语句
     *
     * @param jdbcParameter
     * @return
     */
    @Override
    public int executeUpdate(JDBCParameter jdbcParameter) {
        PreparedStatement preparedStatement = this.getPreparedStatement(jdbcParameter);
        if (preparedStatement == null) {
            return 0;
        }
        try {
            try {
                return preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } finally {
            this.release(preparedStatement);
        }
    }

    /**
     * 执行自增插入 (非事务)
     *
     * @param jdbcParameter
     * @return
     */
    @Override
    public Long executeAutoIncrementInsert(JDBCParameter jdbcParameter) {
        Long generatedKey = 0L;
        PreparedStatement preparedStatement = this.getPreparedStatement(jdbcParameter);
        if (preparedStatement == null) {
            return 0L;
        }
        try {
            preparedStatement.executeUpdate();
            ResultSet result = preparedStatement.getGeneratedKeys();
            if (result.next()) {
                generatedKey = result.getLong(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            this.release(preparedStatement);
        }
        return generatedKey;
    }

    /******************************************************* 返回结果集 ********************************************/
    /**
     * 执行一条SELECT语句 不关闭链接
     *
     * @param jdbcParameter
     * @return
     * @throws Exception
     */
    @Override
    public ResultSet executeQuery(JDBCParameter jdbcParameter) {
        Statement statement = null;
        Connection connection = null;
        ResultSet resultSet = null;
        try {
            if (jdbcParameter.getParameters() == null || jdbcParameter.getParameters().size() == 0) {
                connection = this.getConnection();
                statement = connection.createStatement(
                    ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
                logger.debug("SQL:" + jdbcParameter.getCommand());
                resultSet = statement.executeQuery(jdbcParameter.getCommand());
            } else {
                statement = this.getPreparedStatement(jdbcParameter);
                if (statement == null) {
                    return null;
                }
                resultSet = ((PreparedStatement) statement).executeQuery();
            }
            if (resultSet == null) {
                this.release(statement);
                return null;
            } else {
                return resultSet;
            }
        } catch (Exception e) {
            logger.error("execute query error" + jdbcParameter.getCommand(), e);
            this.release(statement);
            return null;
        }
    }

    @Override
    public ResultSet executeQuery(String commandString) {
        return executeQuery(new JDBCParameter(commandString));
    }

    /**
     * 执行SELECT语句返回一行一列
     *
     * @param jdbcParameter
     * @return
     */
    @Override
    public <P> P executeScalar(JDBCParameter jdbcParameter) {
        Object result = null;
        ResultSet rs = this.executeQuery(jdbcParameter);
        if (rs == null) {
            return null;
        }
        try {
            if (rs.next()) {
                result = rs.getObject(1);
            }
        } catch (Exception e) {
            logger.error(jdbcParameter.getCommand(), e);
            result = null;
        } finally {
            this.release(rs);
        }
        return (P) result;
    }

    @Override
    public <Z> Z executeScalar(String commandString) {
        return (Z) this.executeScalar(new JDBCParameter(commandString));
    }

    @Override
    public void release(Statement statement) {
        try {
            if (statement == null || statement.getConnection() == null) {
                return;
            }
            if (!statement.getConnection().getAutoCommit()) {
                return;
            }
            this.getConnectionHolder()
                .unbindConnection(statement.getConnection());
            statement.close();
        } catch (SQLException e) {
            logger.error("release statement", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void release(ResultSet rs) {
        if (rs == null) {
            return;
        }
        //如果是事务中的查询也不可关闭链接
        try {
            this.release(rs.getStatement());
            rs.close();
        } catch (SQLException e) {
            logger.error("release error", e);
        }
    }
}