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

import java.util.Map;
import java.util.Set;

public interface EntityManager {
    Set<String> getPoPropertyNames();

    String getClassName();

    String getSimpleClassName();

    Field getPrimary();

    Field getStatus();

    String getTableName();

    String getDialectTableName();

    DialectAdapter getDialect();

    String getInsert();

    String getUpdate();

    String getDelete();

    String getFields();

    String getCreateDDL();

    Map<String, Field> getPropertyFieldMap();

    Field getUniqueField(String unique);

    String getProperty(String columnName);

    Field getField(String propertity);

    String getColumnName(String property);

    String getSchema();

    void init();

    void initTable();

    String parsePropertyParameter(String column, String property);

    boolean isAssignableFromDisplayText();

    String joinTableName();
}
