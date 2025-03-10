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

package com.sparrow.orm.datasource;

import com.sparrow.datasource.DataSourceFactory;
import com.sparrow.datasource.DatasourceKey;
import com.sparrow.protocol.constant.Constant;
import com.sparrow.protocol.dao.DataSourceDispatcher;
import com.sparrow.protocol.dao.enums.DatabaseSplitStrategy;
import com.sparrow.support.web.HttpContext;

import javax.sql.DataSource;

public class DefaultDataSourceDispatcher implements DataSourceDispatcher {
    private DataSourceFactory dataSourceFactory;

    public DefaultDataSourceDispatcher(DataSourceFactory dataSourceFactory) {
        this.dataSourceFactory = dataSourceFactory;
    }
    @Override
    public DataSource dispatch(String schema, DatabaseSplitStrategy strategy) {
        DatasourceKey dataSourceKey = new DatasourceKey(schema, this.getDataSourceSuffix(strategy));
        return this.dataSourceFactory.getDataSource(dataSourceKey.getKey());
    }

    private String getDataSourceSuffix(DatabaseSplitStrategy dataSourceSplitStrategy) {
        HttpContext httpContext = HttpContext.getContext();
        String suffix = null;
        switch (dataSourceSplitStrategy) {
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
}
