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
package com.sparrow.orm;

import com.sparrow.container.Container;
import com.sparrow.core.spi.ApplicationContext;
import com.sparrow.datasource.DataSourceFactory;
import com.sparrow.datasource.DataSourceValidChecker;
import com.sparrow.datasource.checker.ConnectionValidCheckerAdapter;
import org.junit.Test;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @author by harry
 */
public class DatasourceTest {

  @Test
  public void datasourceTest() throws SQLException {
    Container container = ApplicationContext.getContainer();
    container.setContextConfigLocation("/dao.xml");
    //container.setConfigLocation("/syste2.properties");
    container.init();
    DataSourceFactory dataSourceFactory = container.getBean("dataSourceFactory");
    DataSource dataSource = dataSourceFactory.getDataSource("user_default");

    System.err.println("datasource valid before " + dataSource);
    DataSourceValidChecker connectionValidChecker = new ConnectionValidCheckerAdapter();
    try {
      while (true) {
        connectionValidChecker.isValid(dataSource);
      }
      //System.err.println("datasource valid after "+dataSource);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
