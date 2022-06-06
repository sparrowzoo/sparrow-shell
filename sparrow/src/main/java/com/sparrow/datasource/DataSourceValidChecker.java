package com.sparrow.datasource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Properties;

public interface DataSourceValidChecker {
    boolean isValid(DataSource c, String query, int validationQueryTimeout) throws Exception;
    boolean isValid(DataSource dataSource) throws Exception;
}
