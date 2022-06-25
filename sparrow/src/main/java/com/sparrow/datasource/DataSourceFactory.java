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
import javax.sql.DataSource;

public interface DataSourceFactory {
    /**
     * get datasource by key
     *
     * @param dataSourceKey the identify of the datasource to confirm one datasource it's same to the file name of
     *                      datasource properties config file
     * @return return datasource
     * @see javax.sql.DataSource
     */
    DataSource getDataSource(String dataSourceKey);

    /**
     * get datasource by sparrow_default key
     *
     * @return
     */
    DataSource getDataSource();

    /**
     * get datasource config by default key sparrow_default
     *
     * @return datasource config object
     */
    DatasourceConfig getDatasourceConfig();

    /**
     * get datasource config object by datasource key
     *
     * @param dataSourceKey the identify of the datasource to confirm one datasource it's same to the file name of
     *                      datasource properties config file
     * @return
     */
    DatasourceConfig getDatasourceConfig(String dataSourceKey);

    /**
     * get datasource key by connection
     *
     * @param connection java connection object
     * @return
     */
    DatasourceKey getDatasourceKey(Connection connection);
}
