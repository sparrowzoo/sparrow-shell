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

import com.sparrow.protocol.dao.enums.DBDialect;

import java.util.HashMap;
import java.util.Map;

public class DialectReader {
    private DBDialect dialect;
    private static Map<String, DialectReader> dialectMap = new HashMap<String, DialectReader>();


    public DialectReader(DBDialect dialect) {
        this.dialect = dialect;
    }

    public String getOpenQuote() {
        switch (this.dialect) {
            case MYSQL:
                return "`";
            case SQLSERVER:
                return "[";
            default:
                break;
        }
        return null;
    }

    public String getCloseQuote() {
        switch (this.dialect) {
            case MYSQL:
                return "`";
            case SQLSERVER:
                return "]";
            default:
                break;
        }
        return null;
    }

    public DBDialect getDialect() {
        return dialect;
    }
}
