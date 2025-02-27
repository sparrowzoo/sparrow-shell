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
import com.sparrow.utility.CollectionsUtility;
import com.sparrow.utility.JDBCUtils;
import com.sparrow.utility.StringUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * getDatasourceConfig 初始化ContextLoaderListener.java 中配置 database identify
 */
public class DataSourceFactoryImpl implements DataSourceFactory {
    private static Logger logger = LoggerFactory.getLogger(DataSourceFactoryImpl.class);
    /**
     * key:jdbc url
     * value:datasourceKey
     */
    private static Cache<String, DatasourceKey> urlDatasourceKeyMap = new StrongDurationCache<>(CacheKey.DATA_SOURCE_URL_PAIR);

    public DataSourceFactoryImpl(String initDatasourceKeys) {
        String[] datasourceKeyArray = initDatasourceKeys.split(",");
        if (CollectionsUtility.isNullOrEmpty(datasourceKeyArray)) {
            return;
        }
        for (String dsBeanName : datasourceKeyArray) {
            DataSource dataSource = ApplicationContext.getContainer().getBean(dsBeanName);
            if (dataSource == null) {
                logger.error("datasource is null, key is " + dsBeanName);
                continue;
            }
            Connection connection = null;
            try {
                connection = dataSource.getConnection();
                String url = connection.getMetaData().getURL();
                if (StringUtility.isNullOrEmpty(url)) {
                    logger.error("url is null, key is " + dsBeanName);
                    continue;
                }
                DatasourceKey datasourceKey = DatasourceKey.parse(dsBeanName);
                urlDatasourceKeyMap.put(url, datasourceKey);
            } catch (SQLException e) {
                logger.error("get connection error, key is " + dsBeanName, e);
            } finally {
                JDBCUtils.close(connection);
            }
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
    public DatasourceKey getDatasourceKey(Connection connection) {
        if (connection == null) {
            return null;
        }
        try {
            return urlDatasourceKeyMap.get(connection.getMetaData().getURL());
        } catch (SQLException e) {
            return null;
        }
    }
}
