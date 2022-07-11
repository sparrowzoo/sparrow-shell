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

import com.sparrow.cg.MethodAccessor;
import com.sparrow.container.ClassFactoryBean;
import com.sparrow.container.Container;
import com.sparrow.core.spi.ApplicationContext;
import com.sparrow.protocol.POJO;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class RowMapperHelper {
    private static ClassFactoryBean<EntityManager> entityManagerFactoryBean = EntityManagerFactoryBean.getInstance();

    public static void mapper(ResultSet rs, POJO... entities) {
        Map<String, POJO> entityMap = new HashMap<String, POJO>();
        for (POJO entity : entities) {
            EntityManager entityManager = entityManagerFactoryBean.getObject(entity.getClass());
            entityMap.put(entityManager.getTableName().toLowerCase(), entity);
        }
        try {
            ResultSetMetaData resultSetMetaData = rs.getMetaData();
            for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
                String tableName = resultSetMetaData.getTableName(i);
                POJO entity = entityMap.get(tableName.toLowerCase());
                String column = resultSetMetaData.getColumnName(i);
                Container container = ApplicationContext.getContainer();
                MethodAccessor methodAccessor = container.getProxyBean(entity.getClass());
                EntityManager entityManager = entityManagerFactoryBean.getObject(entity.getClass());
                methodAccessor.set(entity, entityManager.getProperty(column), rs.getObject(i));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
