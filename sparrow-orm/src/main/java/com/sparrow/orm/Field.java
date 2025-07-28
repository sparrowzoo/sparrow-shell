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

import com.sparrow.core.TypeConverter;
import com.sparrow.protocol.dao.ListDatasource;
import com.sparrow.protocol.dao.SplitTable;
import com.sparrow.protocol.dao.enums.TableSplitStrategy;
import lombok.Data;

import javax.persistence.*;

@Data
public class Field extends TypeConverter {
    private TableSplitStrategy hashStrategy;
    private GenerationType generationType;
    private Id id;
    private Column column;
    private SplitTable splitTable;
    private GeneratedValue generatedValue;
    private ListDatasource listDatasource;
    private String columnName;
    private Integer hashIndex = -1;

    private String generator;
    private boolean primary = false;
    private boolean updatable;
    private boolean unique;
    private int scale;
    private boolean persistence = true;
    private String columnDefinition;

    public Field(String property, Class<?> type, Column column, SplitTable splitTable, GeneratedValue generatedValue,
        Id id) {
        this.id = id;
        this.column = column;
        this.splitTable = splitTable;
        this.propertyName = property;
        this.type = type;
        this.generatedValue = generatedValue;

        if (column != null) {
            this.columnName = column.name();
            this.unique = column.unique();
            this.updatable = column.updatable();
            this.scale = column.scale();
            this.columnDefinition = column.columnDefinition();
        }

        if (splitTable != null) {
            this.hashStrategy = splitTable.strategy();
            this.hashIndex = splitTable.index();
            if (this.hashStrategy.equals(TableSplitStrategy.ORIGIN_NOT_PERSISTENCE)) {
                this.persistence = false;
            }
        }

        if (generatedValue != null) {
            this.generationType = generatedValue.strategy();
            this.generator = generatedValue.generator();
        }
        if (id != null) {
            this.primary = true;
        }
    }
}
