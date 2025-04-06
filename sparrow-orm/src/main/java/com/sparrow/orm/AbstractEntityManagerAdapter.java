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
import com.sparrow.container.ConfigReader;
import com.sparrow.core.spi.ApplicationContext;
import com.sparrow.enums.OrmEntityMetadata;
import com.sparrow.protocol.constant.Constant;
import com.sparrow.protocol.constant.magic.Symbol;
import com.sparrow.protocol.dao.Dialect;
import com.sparrow.protocol.dao.Split;
import com.sparrow.protocol.dao.SplitTable;
import com.sparrow.protocol.dao.enums.DBDialect;
import com.sparrow.protocol.dao.enums.DatabaseSplitStrategy;
import com.sparrow.protocol.dao.enums.TableSplitStrategy;
import com.sparrow.utility.ClassUtility;
import com.sparrow.utility.StringUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.lang.reflect.Method;
import java.util.*;

public abstract class AbstractEntityManagerAdapter implements EntityManager {
    protected static Logger logger = LoggerFactory.getLogger(AbstractEntityManagerAdapter.class);

    protected String schema;
    protected Field primary;
    protected Field status;
    /**
     * 属性名和field
     */
    protected Map<String, Field> propertyFieldMap;
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
    protected DialectAdapter dialectAdapter;
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

        for (java.lang.reflect.Field property : fields) {
            if (!property.isAnnotationPresent(Column.class) && !property.isAnnotationPresent(SplitTable.class)) {
                continue;
            }
            Column column = property.getAnnotation(Column.class);
            SplitTable splitTable = property.getAnnotation(SplitTable.class);
            GeneratedValue generatedValue = property.getAnnotation(GeneratedValue.class);
            Id id = property.getAnnotation(Id.class);

            Field ormField = new Field(property.getName(), property.getType(), column, splitTable, generatedValue, id);
            fieldMap.put(property.getName(), ormField);
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

    private void appendFields(StringBuilder fieldBuilder, String fieldNameOfDialect) {
        if (fieldBuilder.length() > 0) {
            fieldBuilder.append(",");
        }
        fieldBuilder.append(fieldNameOfDialect);
    }

    public AbstractEntityManagerAdapter(Class<?> clazz) {
        this.clazz = clazz;
        this.className = clazz.getName();
        this.simpleClassName = clazz.getSimpleName();
        List<Field> fields = this.extractFields(clazz);
        int fieldCount = fields.size();
        uniqueFieldMap = new LinkedHashMap<>();
        this.columnPropertyMap = new LinkedHashMap<>(fieldCount);
        // 初始化字段列表
        this.propertyFieldMap = new LinkedHashMap<>(fieldCount);
        hashFieldMap = new TreeMap<>();

        StringBuilder fieldBuilder = new StringBuilder();
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
            String propertyName = field.getPropertyName();
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
                uniqueFieldMap.put(field.getPropertyName(), field);
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
            this.propertyFieldMap.put(field.getPropertyName(), field);
            String fieldNameOfDialect = dialectAdapter.getOpenQuote() + column.name()
                    + dialectAdapter.getCloseQuote();
            this.appendFields(fieldBuilder, fieldNameOfDialect);

            // insertSQL
            if (!TableSplitStrategy.ORIGIN_NOT_PERSISTENCE.equals(field.getHashStrategy()) && !GenerationType.IDENTITY.equals(field.getGenerationType())) {
                insertSQL.append("\n");
                insertSQL.append(fieldNameOfDialect);
                insertSQL.append(Symbol.COMMA);

                insertParameter.append("\n");
                insertParameter.append(this.parsePropertyParameter(column.name(), propertyName));
                insertParameter.append(Symbol.COMMA);
            }

            // updateSQL
            if (field.isPrimary()) {
                this.primary = field;
                continue;
            }

            if (column.updatable()) {
                updateSQL.append("\n");
                updateSQL.append(fieldNameOfDialect).append(Symbol.EQUAL);
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
                .append("=").append(this.parsePropertyParameter(this.primary.getColumnName(), this.primary.getPropertyName()));
        String deleteSQL = "delete from " + this.dialectTableName + " where "
                + this.primary.getColumnName() + "=" + this.parsePropertyParameter(this.primary.getColumnName(), this.primary.getPropertyName());

        createDDLField.append(String.format("PRIMARY KEY (`%s`)\n", this.primary.getColumnName()));
        createDDLField.append(") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='").append(tableName).append("';\n");

        this.createDDL = createDDLHeader + primaryCreateDDL + createDDLField;
        this.insert = insertSQL.toString();
        // 初始化delete SQL语句
        this.delete = deleteSQL;
        // 初始化update SQL语句
        this.update = updateSQL.toString();
        this.fields = fieldBuilder.toString();
    }

    @Override
    public void initTable() {
        // 初始化表名
        if (clazz.isAnnotationPresent(Table.class)) {
            Table table = clazz.getAnnotation(Table.class);
            this.schema = table.schema();
            this.tableName = table.name();
        } else {
            this.tableName = StringUtility.setFirstByteLowerCase(clazz.getSimpleName());
        }
        Dialect dialect = clazz.getAnnotation(Dialect.class);
        DBDialect dbDialect = DBDialect.MYSQL;
        if (dialect != null) {
            dbDialect = dialect.strategy();
        }
        this.dialectAdapter = new DialectAdapter(dbDialect);
        Split split = null;
        if (clazz.isAnnotationPresent(Split.class)) {
            split = clazz.getAnnotation(Split.class);
        }

        if (split == null) {
            //`table-name`
            this.dialectTableName = String.format("%s%s%s", dialectAdapter.getOpenQuote(), tableName, dialectAdapter.getCloseQuote());
            return;
        }
        this.dialectTableName = String.format("%s%s%s%s", dialectAdapter.getOpenQuote(), tableName, Constant.TABLE_SUFFIX, dialectAdapter.getCloseQuote());
        this.databaseSplitStrategy = split.strategy();
        // 分表的桶数
        int bucketCount;
        if (split.table_bucket_count() > 1) {
            bucketCount = split.table_bucket_count();
            this.tableBucketCount = bucketCount;
            return;
        }
        String bucketCountConfigKey = this.simpleClassName.toLowerCase() + "." + OrmEntityMetadata.TABLE_BUCKET_COUNT.toString().toLowerCase();
        ConfigReader configReader = ApplicationContext.getContainer().getBean(ConfigReader.class);
        Object configBucketCount = configReader.getValue(bucketCountConfigKey);
        if (configBucketCount != null) {
            this.tableBucketCount = Integer.parseInt(configBucketCount.toString());
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
    public DialectAdapter getDialect() {
        return dialectAdapter;
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
    public Map<String, Field> getPropertyFieldMap() {
        return propertyFieldMap;
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
        return propertyFieldMap.get(property);
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
