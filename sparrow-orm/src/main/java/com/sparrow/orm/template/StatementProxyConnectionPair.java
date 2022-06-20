package com.sparrow.orm.template;

import java.sql.Connection;
import java.sql.Statement;

public class StatementProxyConnectionPair {
    public StatementProxyConnectionPair(Statement statement, Connection connection) {
        this.statement = statement;
        this.connection = connection;
    }

    private Statement statement;
    private Connection connection;

    public Statement getStatement() {
        return statement;
    }

    public void setStatement(Statement statement) {
        this.statement = statement;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
