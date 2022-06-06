package com.sparrow.orm;

import java.util.Map;

public interface EntityManager {

    String getClassName();

    String getSimpleClassName();

    Field getPrimary();

    Field getStatus();

    String getTableName();

    String getDialectTableName();

    Dialect getDialect();

    String getInsert();

    String getUpdate();

    String getDelete();

    String getFields();

    String getCreateDDL();

    Map<String, Field> getFieldMap();

    Field getUniqueField(String unique);

    String getProperty(String columnName);

    Field getField(String propertity);

    String getColumnName(String property);

    String getSchema();

    void init(Class clazz);

    boolean initTable(Class clazz);

    String parsePropertyParameter(String column,String property);
}
