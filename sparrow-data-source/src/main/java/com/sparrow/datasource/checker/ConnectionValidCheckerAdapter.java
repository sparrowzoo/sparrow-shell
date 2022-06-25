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
package com.sparrow.datasource.checker;

import com.sparrow.datasource.DataSourceValidChecker;
import com.sparrow.utility.JDBCUtils;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectionValidCheckerAdapter implements DataSourceValidChecker {
    private static Logger logger = LoggerFactory.getLogger(ConnectionValidCheckerAdapter.class);

    @Override
    public boolean isValid(DataSource dataSource, String query, int validationQueryTimeout) throws Exception {
        if (query == null || query.length() == 0) {
            return true;
        }
        Statement stmt = null;
        ResultSet rs = null;
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            logger.info("used datasource" + dataSource);
            stmt = connection.createStatement();
            if (validationQueryTimeout > 0) {
                stmt.setQueryTimeout(validationQueryTimeout);
            }
            rs = stmt.executeQuery(query);
            return true;
        } finally {
            JDBCUtils.close(rs);
            JDBCUtils.close(stmt);
            // JDBCUtils.close(connection);
        }
    }

    @Override
    public boolean isValid(DataSource dataSource) throws Exception {
        logger.info("before valid datasource {}", dataSource);
        boolean isValid = this.isValid(dataSource, "select 1", 3000);
        logger.info("is valid {}", isValid);
        return isValid;
    }
}
