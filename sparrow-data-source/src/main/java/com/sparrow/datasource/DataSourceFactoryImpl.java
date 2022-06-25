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

import com.sparrow.constant.CacheKey;
import com.sparrow.core.cache.Cache;
import com.sparrow.core.cache.StrongDurationCache;
import com.sparrow.core.spi.ApplicationContext;
import com.sparrow.support.EnvironmentSupport;
import com.sparrow.utility.CollectionsUtility;
import com.sparrow.utility.StringUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import javax.sql.DataSource;

/**
 * getDatasourceConfig 初始化ContextLoaderListener.java 中配置 database identify
 */
public class DataSourceFactoryImpl implements DataSourceFactory {
    private static Logger logger = LoggerFactory.getLogger(DataSourceFactoryImpl.class);
    private static Map<String, DatasourceConfig> datasourceConfigMap = new ConcurrentHashMap<String, DatasourceConfig>();
    private static Cache<String, DatasourceKey> datasourceUrlMap = new StrongDurationCache<>(CacheKey.DATA_SOURCE_URL_PAIR);

    public DataSourceFactoryImpl(String initDatasourceKeys) {
        String[] datasourceKeyArray = initDatasourceKeys.split(",");
        if (CollectionsUtility.isNullOrEmpty(datasourceKeyArray)) {
            return;
        }
        for (String datasource : datasourceKeyArray) {
            this.getDatasourceConfig(datasource);
        }
    }

    public DataSourceFactoryImpl() {
        this("sparrow_default");
    }

    /**
     * 获取数据源
     *
     * @return
     */
    @Override
    public DataSource getDataSource(String dataSourceKey) {
        if (StringUtility.isNullOrEmpty(dataSourceKey)) {
            dataSourceKey = DatasourceKey.getDefault().getKey();
        }
        return ApplicationContext.getContainer().getBean(dataSourceKey);
    }

    @Override
    public DataSource getDataSource() {
        return getDataSource(null);
    }

    @Override
    public DatasourceConfig getDatasourceConfig() {
        return getDatasourceConfig(null);
    }

    /**
     * 读取数据库源 <p> data source key 与 connection url 映射 data source+suffix determine datasource data
     * source+database_split_key determine jdbc template
     *
     * @param dataSourceKey
     * @return
     */
    @Override
    public DatasourceConfig getDatasourceConfig(String dataSourceKey) {
        if (StringUtility.isNullOrEmpty(dataSourceKey)) {
            dataSourceKey = "sparrow_default";
        }
        DatasourceKey dsKey = DatasourceKey.parse(dataSourceKey);
        if (datasourceConfigMap.containsKey(dataSourceKey)) {
            return datasourceConfigMap.get(dataSourceKey);
        }

        synchronized (this) {
            if (datasourceConfigMap.containsKey(dataSourceKey)) {
                return datasourceConfigMap.get(dataSourceKey);
            }
            DatasourceConfig datasourceConfig = new DatasourceConfig();
            try {
                Properties props = new Properties();
                String filePath = "/" + dataSourceKey
                    + ".properties";
                String schema = dsKey.getSchema();
                props.load(EnvironmentSupport.getInstance().getFileInputStream(filePath));
                datasourceConfig.setDriverClassName(props.getProperty(schema + ".driverClassName"));
                datasourceConfig.setUsername(props.getProperty(schema + ".username"));
                datasourceConfig.setPassword(props.getProperty(schema + ".password"));
                datasourceConfig.setUrl(props.getProperty(schema + ".url"));
                datasourceConfig.setPoolSize(Integer.parseInt(props.getProperty(schema + ".poolSize")));
            } catch (Exception ignore) {
                throw new RuntimeException(ignore);
            }
            //detection jdbc config useful
            Connection connection = new ProxyConnection(datasourceConfig, null);
            Statement statement = null;
            try {
                statement = connection.createStatement();
                boolean effectCount = statement.execute("SELECT 1");
                if (effectCount) {
                    datasourceUrlMap.put(connection.getMetaData().getURL(), dsKey);
                }
                datasourceConfigMap.put(dataSourceKey, datasourceConfig);
            } catch (SQLException e) {
                logger.error(" cat't connection", e);
            } finally {
                try {
                    if (statement != null) {
                        statement.close();
                    }
                    connection.close();
                } catch (SQLException e) {
                    logger.error("test connection close error", e);
                }
            }
            return datasourceConfig;
        }
    }

    @Override
    public DatasourceKey getDatasourceKey(Connection connection) {
        if (connection == null) {
            return null;
        }
        try {
            return datasourceUrlMap.get(connection.getMetaData().getURL());
        } catch (SQLException e) {
            return null;
        }
    }
}
