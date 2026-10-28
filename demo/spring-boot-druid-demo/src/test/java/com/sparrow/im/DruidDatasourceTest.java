package com.sparrow.im;

import com.sparrow.datasource.ConnectionReleaser;
import com.sparrow.spring.test.TestWithoutBootstrap;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;


@TestWithoutBootstrap
public class DruidDatasourceTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private ConnectionReleaser connectionReleaser;

    @Test
    public void testDruidDatasource() throws SQLException {

        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();
        statement.execute("SELECT 1");
        this.connectionReleaser.release(statement.getConnection());
        System.out.println("testUserSecret" + dataSource);
    }
}

