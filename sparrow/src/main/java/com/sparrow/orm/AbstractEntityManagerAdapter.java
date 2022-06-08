package com.sparrow.orm;

import com.sparrow.cg.PropertyNamer;
import com.sparrow.constant.ConfigKeyDB;
import com.sparrow.enums.OrmEntityMetadata;
import com.sparrow.protocol.constant.CONSTANT;
import com.sparrow.protocol.constant.magic.SYMBOL;
import com.sparrow.protocol.dao.Hash;
import com.sparrow.protocol.dao.Split;
import com.sparrow.protocol.dao.enums.DATABASE_SPLIT_STRATEGY;
import com.sparrow.protocol.dao.enums.HashType;
import com.sparrow.utility.ClassUtility;
import com.sparrow.utility.ConfigUtility;
import com.sparrow.utility.StringUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
    protected List<Field> hashFieldList;
    protected String tableName;
    protected String dialectTableName;
    protected String className;
    protected String simpleClassName;
    protected Dialect dialect;
    protected int tableBucketCount;
    protected int databaseSplitMaxId;
    protected DATABASE_SPLIT_STRATEGY databaseSplitStrategy;
    protected String insert;
    protected String update;
    protected String delete;
    protected String fields;
    protected String createDDL;


    public AbstractEntityManagerAdapter(Class clazz) {
        this.className = clazz.getName();
        this.simpleClassName = clazz.getSimpleName();
        Method[] methods = ClassUtility.getOrderedMethod(clazz.getDeclaredMethods());
        int fieldCount = methods.length;

        List<Field> fields = new ArrayList<Field>(fieldCount);
        uniqueFieldMap = new LinkedHashMap<>();
        columnPropertyMap = new LinkedHashMap<String, String>(fieldCount);
        hashFieldList = new ArrayList<Field>();

        StringBuilder insertSQL = new StringBuilder("insert into ");
        StringBuilder insertParameter = new StringBuilder();
        StringBuilder updateSQL = new StringBuilder("update ");
        StringBuilder createDDLField = new StringBuilder();
        initTable(clazz);

        updateSQL.append(this.dialectTableName);
        insertSQL.append(this.dialectTableName);


        String createDDLHeader = String.format("DROP TABLE IF EXISTS %1$s;\nCREATE TABLE %1$s (\n", this.dialectTableName);
        String primaryCreateDDL = "";
        insertSQL.append("(");
        updateSQL.append(" set ");
        for (Method method : methods) {
            if (method.getName().startsWith("set")) {
                continue;
            }
            if (!method.isAnnotationPresent(Column.class) && !method.isAnnotationPresent(Hash.class)) {
                continue;
            }

            Column column = method.getAnnotation(Column.class);
            Hash hash = method.getAnnotation(Hash.class);
            GeneratedValue generatedValue = method.getAnnotation(GeneratedValue.class);
            Id id = method.getAnnotation(Id.class);

            String propertyName = StringUtility.setFirstByteLowerCase(PropertyNamer.methodToProperty(method.getName()));
            Field field = new Field(propertyName, method.getReturnType(), column, hash, generatedValue, id);
            fields.add(field);

            if (hash != null) {
                this.hashFieldList.add(field);
                if (!field.isPersistence()) {
                    continue;
                }
            }

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
                //todo AUTO_INCREMENT
                primaryCreateDDL = String.format(" `%s` %s NOT NULL ,\n", column.name(), column.columnDefinition());
            }


            this.columnPropertyMap.put(column.name(), propertyName);
            String fieldName = dialect.getOpenQuote() + column.name()
                    + dialect.getCloseQuote();
            // insertSQL
            if (!HashType.ONLY_HASH.equals(field.getHashStrategy()) && !GenerationType.IDENTITY.equals(field.getGenerationType())) {
                insertSQL.append(fieldName);
                insertSQL.append(SYMBOL.COMMA);
                insertParameter.append(this.parsePropertyParameter(column.name(), propertyName));
                insertParameter.append(",");
            }

            // updateSQL
            if (field.isPrimary()) {
                this.primary = field;
                continue;
            }

            if (column.updatable()) {
                updateSQL.append(fieldName + SYMBOL.EQUAL);
                updateSQL.append(this.parsePropertyParameter(column.name(), propertyName));
                updateSQL.append(",");
            }
        }
        insertSQL.deleteCharAt(insertSQL.length() - 1);
        insertParameter.deleteCharAt(insertParameter.length() - 1);
        insertSQL.append(")values(");
        insertSQL.append(insertParameter);
        insertSQL.append(SYMBOL.RIGHT_PARENTHESIS);

        updateSQL.deleteCharAt(updateSQL.length() - 1).append(
                " where " + this.primary.getColumnName() + "=" + this.parsePropertyParameter(this.primary.getColumnName(), this.primary.getName()));
        String deleteSQL = "delete from " + this.dialectTableName + " where "
                + this.primary.getColumnName() + "=" + this.parsePropertyParameter(this.primary.getColumnName(), this.primary.getName());

        createDDLField.append(String.format("PRIMARY KEY (`%s`)\n", this.primary.getColumnName()));
        createDDLField.append(") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='" + tableName + "';\n");

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
                fieldBuilder.append(",");
            }
            if (field.isPersistence()) {
                fieldBuilder.append(dialect.getOpenQuote() + field.getColumnName() + dialect.getCloseQuote());
            }
            this.fieldMap.put(field.getName(), field);
        }
        this.fields = fieldBuilder.toString();
        this.init(clazz);
    }

    @Override
    public boolean initTable(Class clazz) {
        // 初始化表名
        if (!clazz.isAnnotationPresent(Table.class)) {
            return false;
        }
        Table table = (Table) clazz.getAnnotation(Table.class);
        Split split = (Split) clazz.getAnnotation(Split.class);
        this.tableName = table.name();
        this.schema = table.schema();
        this.dialect = Dialect.getInstance(schema);
        if (split == null) {
            //`table-name`
            this.dialectTableName = String.format("%s%s%s", dialect.getOpenQuote(), tableName, dialect.getCloseQuote());
            return false;
        }
        this.dialectTableName = String.format("%s%s%s%s", dialect.getOpenQuote(), tableName, CONSTANT.TABLE_SUFFIX, dialect.getCloseQuote());
        // 分表的桶数
        int bucketCount;
        if (split.table_bucket_count() > 1) {
            bucketCount = split.table_bucket_count();
            String bucketCountConfigKey = this.simpleClassName.toLowerCase() + "." + OrmEntityMetadata.TABLE_BUCKET_COUNT.toString().toLowerCase();
            Object configBucketCount = ConfigUtility.getValue(bucketCountConfigKey);
            if (configBucketCount != null) {
                bucketCount = Integer.valueOf(configBucketCount.toString());
            }
            this.tableBucketCount = bucketCount;
            this.databaseSplitMaxId = split.database_max_id();
            this.databaseSplitStrategy = split.strategy();
        }
        return true;
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
    public Dialect getDialect() {
        return dialect;
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
