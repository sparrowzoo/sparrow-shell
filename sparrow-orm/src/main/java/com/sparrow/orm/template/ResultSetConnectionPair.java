package com.sparrow.orm.template;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class ResultSetConnectionPair {
    public ResultSetConnectionPair(ResultSet resultSet, Connection connection) {
        this.resultSet = resultSet;
        this.connection = connection;
    }

    private ResultSet resultSet;
    private Connection connection;

    public ResultSet getResultSet() {
        return resultSet;
    }

    public void setResultSet(ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
