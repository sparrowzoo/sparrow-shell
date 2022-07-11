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
import com.sparrow.protocol.dao.SplitTable;
import com.sparrow.protocol.dao.enums.TableSplitStrategy;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class Field extends TypeConverter {
    private TableSplitStrategy hashStrategy;
    private GenerationType generationType;
    private String columnName;
    private Integer hashIndex = -1;

    private String generator;
    private boolean primary = false;
    private boolean updatable;
    private boolean unique;
    private int scale;
    private boolean persistence = true;
    private String columnDefinition;

    public Field(String property, Class type, Column column, SplitTable splitTable, GeneratedValue generatedValue,
        Id id) {
        this.name = property;
        this.type = type;
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

    public GenerationType getGenerationType() {
        return generationType;
    }

    public TableSplitStrategy getHashStrategy() {
        return hashStrategy;
    }

    public String getColumnName() {
        return columnName;
    }

    public Integer getHashIndex() {
        return hashIndex;
    }

    public boolean isPrimary() {
        return primary;
    }

    public String getGenerator() {
        return generator;
    }

    public boolean isUpdatable() {
        return updatable;
    }

    public int getScale() {
        return scale;
    }

    public boolean isUnique() {
        return unique;
    }

    public boolean isPersistence() {
        return persistence;
    }

    public String getColumnDefinition() {
        return columnDefinition;
    }
}
