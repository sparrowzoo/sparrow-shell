package com.sparrow.datasource.checker;

import com.sparrow.datasource.DataSourceValidChecker;
import com.sparrow.utility.JDBCUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class ConnectionValidCheckerAdapter implements DataSourceValidChecker {
    private static Logger logger = LoggerFactory.getLogger(ConnectionValidCheckerAdapter.class);

    @Override
    public boolean isValid(DataSource dataSource, String query, int validationQueryTimeout) throws Exception {
        if (query == null || query.length() == 0) {
            return true;
        }
        Statement stmt = null;
        ResultSet rs = null;
        Connection connection=null;
        try{
            connection=dataSource.getConnection();
            logger.info("used datasource"+dataSource);
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
