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
package com.sparrow.orm.type;

import java.sql.*;
import java.util.Date;

public class TimeTypeHandler implements TypeHandler<Date> {

    @Override
    public void setParameter(PreparedStatement ps, int i, Date parameter) throws SQLException {
        ps.setTime(i, new Time(parameter.getTime()));
    }

    @Override
    public Date getResult(ResultSet rs, String columnName) throws SQLException {
        Time date = rs.getTime(columnName);
        if (date != null) {
            return new Date(date.getTime());
        }
        return null;
    }

    @Override
    public Date getResult(ResultSet rs, int columnIndex) throws SQLException {
        java.sql.Time date = rs.getTime(columnIndex);
        if (date != null) {
            return new Date(date.getTime());
        }
        return null;
    }

    @Override
    public Date getResult(CallableStatement cs, int columnIndex) throws SQLException {
        java.sql.Time date = cs.getTime(columnIndex);
        if (date != null) {
            return new Date(date.getTime());
        }
        return null;
    }
}
