package com.sparrow.h2;

import org.h2.jdbcx.JdbcConnectionPool;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by harry on 2017/9/4.
 */
public class H2Test {
    public static void main(String[] args) throws SQLException {
        JdbcConnectionPool cp = JdbcConnectionPool.
            create("jdbc:h2:mem:simple", "sa", "sa");
        Connection conn = cp.getConnection();
        Statement statement= conn.createStatement();
        Boolean b= statement.execute("select * from test2");
        System.out.println(b);
    }
}
