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

import com.sparrow.cg.PropertyNamer;
import com.sparrow.constant.ConfigKeyDB;
import com.sparrow.enums.OrmEntityMetadata;
import com.sparrow.protocol.constant.Constant;
import com.sparrow.protocol.constant.magic.Symbol;
import com.sparrow.protocol.dao.Split;
import com.sparrow.protocol.dao.SplitTable;
import com.sparrow.protocol.dao.enums.DatabaseSplitStrategy;
import com.sparrow.protocol.dao.enums.TableSplitStrategy;
import com.sparrow.utility.ClassUtility;
import com.sparrow.utility.ConfigUtility;
import com.sparrow.utility.StringUtility;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractEntityManagerAdapter implements EntityManager {
    protected static Logger logger = LoggerFactory.getLogger(AbstractEntityManagerAdapter.class);

    protected String schema;
    protected Field primary;
    protected Field status;
    /**
     * 属性名和field
     */
    protected Map<String, Field> fieldMap;
    /**
     * 列名与属性名映射
     */
    protected Map<String, String> columnPropertyMap;
    protected Map<String, Field> uniqueFieldMap;
    protected Map<Integer, Field> hashFieldMap;
    protected String tableName;
    protected String dialectTableName;
    protected String className;
    protected Class<?> clazz;
    protected String simpleClassName;
    protected DialectReader dialectReader;
    protected int tableBucketCount;
    protected DatabaseSplitStrategy databaseSplitStrategy;
    protected String insert;
    protected String update;
    protected String delete;
    protected String fields;
    protected String createDDL;

    private List<Field> extractFields(Class<?> clazz) {
        Method[] orderedMethods = ClassUtility.getOrderedMethod(clazz.getMethods());
        List<java.lang.reflect.Field> fieldList = ClassUtility.extractFields(clazz);
        java.lang.reflect.Field[] fields = ClassUtility.getOrderedFields(fieldList);
        Map<String, Field> fieldMap = new LinkedHashMap<>();

        for (java.lang.reflect.Field field : fields) {
            if (!field.isAnnotationPresent(Column.class) && !field.isAnnotationPresent(SplitTable.class)) {
                continue;
            }
            Column column = field.getAnnotation(Column.class);
            SplitTable splitTable = field.getAnnotation(SplitTable.class);
            GeneratedValue generatedValue = field.getAnnotation(GeneratedValue.class);
            Id id = field.getAnnotation(Id.class);

            Field ormField = new Field(field.getName(), field.getType(), column, splitTable, generatedValue, id);
            fieldMap.put(field.getName(), ormField);
        }

        for (Method method : orderedMethods) {
            if (!method.isAnnotationPresent(Column.class) && !method.isAnnotationPresent(SplitTable.class)) {
                continue;
            }
            String propertyName = StringUtility.setFirstByteLowerCase(PropertyNamer.methodToProperty(method.getName()));
            if (fieldMap.containsKey(propertyName)) {
                continue;
            }
            Column column = method.getAnnotation(Column.class);
            SplitTable splitTable = method.getAnnotation(SplitTable.class);
            GeneratedValue generatedValue = method.getAnnotation(GeneratedValue.class);
            Id id = method.getAnnotation(Id.class);
            Field field = new Field(propertyName, method.getReturnType(), column, splitTable, generatedValue, id);
            fieldMap.put(propertyName, field);
        }
        return new ArrayList<>(fieldMap.values());
    }

    public AbstractEntityManagerAdapter(Class<?> clazz) {
        this.clazz = clazz;
        this.className = clazz.getName();
        this.simpleClassName = clazz.getSimpleName();
        List<Field> fields = this.extractFields(clazz);
        int fieldCount = fields.size();
        uniqueFieldMap = new LinkedHashMap<>();
        columnPropertyMap = new LinkedHashMap<String, String>(fieldCount);
        hashFieldMap = new TreeMap<>();

        StringBuilder insertSQL = new StringBuilder("insert into ");
        StringBuilder insertParameter = new StringBuilder();
        StringBuilder updateSQL = new StringBuilder("update ");
        StringBuilder createDDLField = new StringBuilder();
        initTable();

        updateSQL.append(this.dialectTableName);
        insertSQL.append(this.dialectTableName);

        String createDDLHeader = String.format("DROP TABLE IF EXISTS %1$s;\nCREATE TABLE %1$s (\n", this.dialectTableName);
        String primaryCreateDDL = "";
        insertSQL.append("(");
        updateSQL.append(" set ");
        for (Field field : fields) {
            String propertyName = field.getName();
            SplitTable splitTable = field.getSplitTable();
            if (splitTable != null) {
                this.hashFieldMap.put(splitTable.index(), field);
                if (!field.isPersistence()) {
                    continue;
                }
            }

            Column column = field.getColumn();
            if (column == null) {
                continue;
            }
            if (field.isUnique()) {
                uniqueFieldMap.put(field.getName(), field);
            }
            if ("status".equalsIgnoreCase(column.name())) {
                this.status = field;
            }

            if (!field.isPrimary()) {
                createDDLField.append(String.format(" `%s` %s  %s,\n", column.name(), column.columnDefinition(), column.nullable() ? "" : "NOT NULL"));
            } else {
                if (field.getGenerationType().equals(GenerationType.IDENTITY)) {
                    primaryCreateDDL = String.format(" `%s` %s NOT NULL AUTO_INCREMENT,\n", column.name(), column.columnDefinition());
                } else {
                    primaryCreateDDL = String.format(" `%s` %s NOT NULL,\n", column.name(), column.columnDefinition());
                }
            }

            this.columnPropertyMap.put(column.name(), propertyName);
            String fieldName = dialectReader.getOpenQuote() + column.name()
                + dialectReader.getCloseQuote();
            // insertSQL
            if (!TableSplitStrategy.ORIGIN_NOT_PERSISTENCE.equals(field.getHashStrategy()) && !GenerationType.IDENTITY.equals(field.getGenerationType())) {
                insertSQL.append("\n");
                insertSQL.append(fieldName);
                insertSQL.append(Symbol.COMMA);

                insertParameter.append("\n");
                insertParameter.append(this.parsePropertyParameter(column.name(), propertyName));
                insertParameter.append(",");
            }

            // updateSQL
            if (field.isPrimary()) {
                this.primary = field;
                continue;
            }

            if (column.updatable()) {
                updateSQL.append("\n");
                updateSQL.append(fieldName).append(Symbol.EQUAL);
                updateSQL.append(this.parsePropertyParameter(column.name(), propertyName));
                updateSQL.append(",");
            }
        }
        insertSQL.deleteCharAt(insertSQL.length() - 1);
        insertParameter.deleteCharAt(insertParameter.length() - 1);
        insertSQL.append(")values(");
        insertSQL.append(insertParameter);
        insertSQL.append(Symbol.RIGHT_PARENTHESIS);

        updateSQL.deleteCharAt(updateSQL.length() - 1)
            .append(" where ")
            .append(this.primary.getColumnName())
            .append("=").append(this.parsePropertyParameter(this.primary.getColumnName(), this.primary.getName()));
        String deleteSQL = "delete from " + this.dialectTableName + " where "
            + this.primary.getColumnName() + "=" + this.parsePropertyParameter(this.primary.getColumnName(), this.primary.getName());

        createDDLField.append(String.format("PRIMARY KEY (`%s`)\n", this.primary.getColumnName()));
        createDDLField.append(") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='").append(tableName).append("';\n");

        this.createDDL = createDDLHeader + primaryCreateDDL + createDDLField.toString();
        this.insert = insertSQL.toString();
        // 初始化delete SQL语句
        this.delete = deleteSQL;
        // 初始化update SQL语句
        this.update = updateSQL.toString();
        // 初始化字段列表
        this.fieldMap = new LinkedHashMap<String, Field>(fieldCount);
        StringBuilder fieldBuilder = new StringBuilder();
        for (Field field : fields) {
            if (fieldBuilder.length() > 0) {
                fieldBuilder.append(",\n");
            }
            if (field.isPersistence()) {
                fieldBuilder.append(dialectReader.getOpenQuote()).append(field.getColumnName()).append(dialectReader.getCloseQuote());
            }
            this.fieldMap.put(field.getName(), field);
        }
        this.fields = fieldBuilder.toString();
    }

    @Override
    public void initTable() {
        // 初始化表名
        if (clazz.isAnnotationPresent(Table.class)) {
            Table table = (Table) clazz.getAnnotation(Table.class);
            this.schema = table.schema();
            this.tableName = table.name();
        } else {
            this.tableName = StringUtility.setFirstByteLowerCase(clazz.getSimpleName());
        }
        this.dialectReader = DialectReader.getInstance(schema);
        Split split = null;
        if (clazz.isAnnotationPresent(Split.class)) {
            split = clazz.getAnnotation(Split.class);
        }

        if (split == null) {
            //`table-name`
            this.dialectTableName = String.format("%s%s%s", dialectReader.getOpenQuote(), tableName, dialectReader.getCloseQuote());
            return;
        }
        this.dialectTableName = String.format("%s%s%s%s", dialectReader.getOpenQuote(), tableName, Constant.TABLE_SUFFIX, dialectReader.getCloseQuote());
        // 分表的桶数
        int bucketCount;
        if (split.table_bucket_count() > 1) {
            bucketCount = split.table_bucket_count();
            String bucketCountConfigKey = this.simpleClassName.toLowerCase() + "." + OrmEntityMetadata.TABLE_BUCKET_COUNT.toString().toLowerCase();
            Object configBucketCount = ConfigUtility.getValue(bucketCountConfigKey);
            if (configBucketCount != null) {
                bucketCount = Integer.parseInt(configBucketCount.toString());
            }
            this.tableBucketCount = bucketCount;
            this.databaseSplitStrategy = split.strategy();
        }
    }

    @Override
    public Field getPrimary() {
        return primary;
    }

    @Override
    public Field getStatus() {
        return status;
    }

    @Override
    public String getTableName() {
        return tableName;
    }

    @Override
    public String getDialectTableName() {
        return this.dialectTableName;
    }

    @Override
    public DialectReader getDialect() {
        return dialectReader;
    }

    @Override
    public String getInsert() {
        return insert;
    }

    @Override
    public String getUpdate() {
        return update;
    }

    @Override
    public String getDelete() {
        return delete;
    }

    @Override
    public String getFields() {
        return fields;
    }

    @Override
    public Map<String, Field> getFieldMap() {
        return fieldMap;
    }

    @Override
    public Field getUniqueField(String unique) {
        if (unique.equalsIgnoreCase(ConfigKeyDB.ORM_PRIMARY_KEY_UNIQUE)) {
            return this.primary;
        } else {
            return this.uniqueFieldMap.get(unique);
        }
    }

    @Override
    public String getProperty(String columnName) {
        return columnPropertyMap.get(columnName);
    }

    @Override
    public Field getField(String property) {
        return fieldMap.get(property);
    }

    @Override
    public String getColumnName(String property) {
        Field field = this.getField(property);
        if (field == null) {
            throw new RuntimeException("field is not found \nproperty is '" + property + "'");
        }
        return field.getColumnName();
    }

    @Override
    public String getSchema() {
        return schema;
    }

    @Override
    public String getClassName() {
        return className;
    }

    @Override
    public String getSimpleClassName() {
        return simpleClassName;
    }

    @Override
    public String getCreateDDL() {
        return createDDL;
    }
}
