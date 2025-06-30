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
import com.sparrow.container.Container;
import com.sparrow.core.Pair;
import com.sparrow.core.StrategyFactory;
import com.sparrow.core.spi.ApplicationContext;
import com.sparrow.orm.query.SearchCriteria;
import com.sparrow.orm.query.UpdateCriteria;
import com.sparrow.orm.query.sql.CriteriaProcessor;
import com.sparrow.orm.query.sql.OperationEntity;
import com.sparrow.orm.type.TypeHandler;
import com.sparrow.orm.type.TypeHandlerRegistry;
import com.sparrow.protocol.constant.Constant;
import com.sparrow.protocol.constant.magic.Symbol;
import com.sparrow.protocol.dao.StatusCriteria;
import com.sparrow.utility.ClassUtility;
import com.sparrow.utility.StringUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.*;

public class OrmMetadataAccessor<T, I> {
    private static Logger logger = LoggerFactory.getLogger(OrmMetadataAccessor.class);

    private CriteriaProcessor criteriaProcessor;
    /**
     * 实体类
     */
    private Class<?> modelClazz = null;

    private String modelName = null;

    private Container container = ApplicationContext.getContainer();

    private SparrowEntityManager entityManager = null;
    /**
     * 方法访问对象
     */
    private MethodAccessor methodAccessor = null;

    public MethodAccessor getMethodAccessor() {
        return methodAccessor;
    }

    public SparrowEntityManager getEntityManager() {
        return entityManager;
    }

    public Container getContainer() {
        return container;
    }

    public Class<?> getModelClazz() {
        return modelClazz;
    }

    public String getTableName(List<Object> tableSuffix) {
        if (tableSuffix == null) {
            return this.entityManager.getDialectTableName();
        }
        return this.entityManager.getDialectTableName().replace(Constant.TABLE_SUFFIX, this.entityManager.getTableSuffix(tableSuffix));
    }

    /**
     * JORM.java的构造函数
     */
    public OrmMetadataAccessor(Class modelClazz, CriteriaProcessor criteriaProcessor) {
        this.modelClazz = modelClazz;
        this.methodAccessor = container.getProxyBean(
                this.modelClazz);
        if (this.methodAccessor == null) {
            throw new NullPointerException(modelClazz.getName() + "'s method accessor not found please config proxy bean");
        }
        this.entityManager = new SparrowEntityManager(this.modelClazz);
        this.entityManager.init();
        EntityManagerFactoryBean.getInstance().pubObject(this.modelClazz, this.entityManager);
        this.modelName = ClassUtility.getBeanNameByClass(this.modelClazz);
        this.criteriaProcessor = criteriaProcessor;
    }

    public JDBCParameter insert(T model) {
        if (model == null) {
            throw new IllegalArgumentException("insert model can't be null");
        }
        String insertSQL = this.entityManager.getInsert();
        boolean isIncrement = false;
        List<Parameter> parameters = new ArrayList<Parameter>();
        Map<Integer, Object> tableSuffix = new TreeMap<Integer, Object>();
        for (Field field : this.entityManager.getPropertyFieldMap().values()) {
            Object o = this.methodAccessor.get(model, field.getPropertyName());
            if (field.getGenerationType() == null) {
                this.entityManager.parseField(field, parameters, o, tableSuffix, false);
                continue;
            }
            switch (field.getGenerationType()) {
                case TABLE:
                case SEQUENCE:
                    String key = field.getGenerator();
                    //todo 根据指定表的KEY生成id
                    break;
                case AUTO:
                    String generator = field.getGenerator();
                    String id;
                    //预先设定的的主键
                    if (StringUtility.isNullOrEmpty(generator) || "set".equalsIgnoreCase(generator)) {
                        parameters.add(new Parameter(field, o));
                        break;
                    }
                    //uuid IDGeneratorImpl
                    IDGenerator idGenerator = StrategyFactory.getInstance().get(IDGenerator.class, generator);
                    if (idGenerator != null) {
                        id = idGenerator.generate();
                        parameters.add(new Parameter(field, id));
                        this.methodAccessor
                                .set(model, field.getPropertyName(), id);
                    }
                    break;
                case IDENTITY:
                    isIncrement = true;
                    break;
                default:
            }
        }
        if (tableSuffix.size() > 0) {
            if (insertSQL.contains(Constant.TABLE_SUFFIX)) {
                insertSQL = insertSQL.replace(Constant.TABLE_SUFFIX, this.entityManager.getTableSuffix(tableSuffix));
            }
        }
        return new JDBCParameter(insertSQL, parameters, isIncrement);
    }

    public JDBCParameter update(T model) {
        if (model == null) {
            throw new IllegalArgumentException("model can't be null");
        }
        String updateSQL = this.entityManager.getUpdate();
        List<Parameter> parameters = new ArrayList<Parameter>();
        Parameter whereParameter = null;
        Map<Integer, Object> tableSuffix = new TreeMap<Integer, Object>();
        for (Field field : this.entityManager.getPropertyFieldMap().values()) {
            Object o = this.methodAccessor.get(model, field.getPropertyName());
            if (field.isPrimary()) {
                whereParameter = new Parameter(field, o);
            } else {
                this.entityManager.parseField(field, parameters, o, tableSuffix, true);
            }
        }
        if (tableSuffix.size() > 0) {
            if (updateSQL.contains(Constant.TABLE_SUFFIX)) {
                updateSQL = updateSQL.replace(Constant.TABLE_SUFFIX, this.entityManager.getTableSuffix(tableSuffix));
            }
        }
        parameters.add(whereParameter);
        return new JDBCParameter(updateSQL, parameters, false);
    }

    public JDBCParameter update(UpdateCriteria criteria) {
        if (criteria == null) {
            throw new IllegalArgumentException("criteria can't be null");
        }
        OperationEntity where = this.criteriaProcessor.where(criteria.getWhere());
        OperationEntity setClause = this.criteriaProcessor.setClause(criteria.getSetClausePairList());
        String update = String.format("update %1$s set %2$s where %3$s",
                this.getTableName(criteria.getTableSuffix()), setClause.getClause(),
                where.getClause());
        List<Parameter> updateParameters = new ArrayList<Parameter>();
        updateParameters.addAll(setClause.getParameterList());
        updateParameters.addAll(where.getParameterList());
        return new JDBCParameter(update, updateParameters);
    }

    public JDBCParameter delete(I id) {
        if (id == null) {
            throw new IllegalArgumentException("delete id can't be null");
        }
        Field primaryField = this.entityManager.getPrimary();
        return new JDBCParameter(this.entityManager.getDelete(),
                Collections.singletonList(new Parameter(primaryField, primaryField.convert(id.toString()))));
    }

    public JDBCParameter delete(SearchCriteria searchCriteria) {
        if (searchCriteria == null) {
            throw new IllegalArgumentException("delete by search criteria [searchCriteria]can't be null");
        }

        String delete = String.format("DELETE FROM %1$s", this.getTableName(searchCriteria.getTableSuffix()));
        OperationEntity where = this.criteriaProcessor.where(searchCriteria.getWhere());
        if (!StringUtility.isNullOrEmpty(where.getClause())) {
            delete += " WHERE " + where.getClause();
        }
        return new JDBCParameter(delete, where.getParameterList());
    }

    public T setEntity(ResultSet rs, ResultSetCallback resultSetCallback) {
        try {
            ResultSetMetaData resultSetMetaData = rs.getMetaData();
            T model = (T) this.modelClazz.getConstructor().newInstance();

            TypeHandlerRegistry typeHandlerRegistry = TypeHandlerRegistry.getInstance();

            for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
                String columnName = "";
                try {
                    columnName = resultSetMetaData.getColumnName(i);
                    String propertyName = this.entityManager.getProperty(columnName);
                    if (propertyName == null) {
                        logger.warn("column name [{}] not found in entity [{}]", columnName, this.modelClazz.getSimpleName());
                        continue;
                    }
                    Class javaType = this.entityManager.getField(propertyName).getType();
                    TypeHandler typeHandler = typeHandlerRegistry.getTypeHandler(javaType);
                    Object fieldValue = typeHandler.getResult(rs, i);

                    this.getMethodAccessor().set(model, propertyName, fieldValue);
                } catch (Exception e) {
                    logger.error(this.modelClazz.getSimpleName() + Symbol.VERTICAL_LINE + columnName, e);
                }
            }
            return model;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (resultSetCallback != null) {
                resultSetCallback.close(rs);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public JDBCParameter getEntity(I key, String uniqueKey) {
        StringBuilder select = new StringBuilder("select ");
        select.append(this.entityManager.getFields());
        select.append(" from "
                + this.entityManager.getDialectTableName());
        select.append(" where " + this.entityManager.getPrimary().getColumnName() + "=?");
        return new JDBCParameter(select.toString(), Collections.singletonList(new Parameter(this.entityManager.getUniqueField(uniqueKey), key)));
    }

    public JDBCParameter getCount(I key, String uniqueKey) {
        StringBuilder select = new StringBuilder("select count(*) from "
                + this.entityManager.getDialectTableName());
        Field uniqueField = this.entityManager.getUniqueField(uniqueKey);
        select.append(" where "
                + uniqueField.getColumnName() + "=?");
        return new JDBCParameter(select.toString(), Collections.singletonList(new Parameter(uniqueField, key)));
    }

    public JDBCParameter getCount(SearchCriteria criteria) {
        if (criteria == null) {
            throw new IllegalArgumentException("criteria can't be null");
        }
        OperationEntity boolOperationEntity = this.criteriaProcessor.where(criteria.getWhere());
        String field = this.criteriaProcessor.fields(criteria.getFields());
        if (StringUtility.isNullOrEmpty(field)) {
            field = "*";
        }
        String whereClause = null;
        whereClause = boolOperationEntity.getClause().toString();
        String tableName = null;
        if (criteria.getDistinct()) {
            field = "distinct " + field;
        }
        StringBuilder select = new StringBuilder();

        tableName = this.getTableName(criteria.getTableSuffix());
        select.append(String.format("select count(%1$s) from %2$s",
                field, tableName));
        if (!StringUtility.isNullOrEmpty(whereClause)) {
            select.append(" where " + whereClause);
        }
        return new JDBCParameter(select.toString(), boolOperationEntity.getParameterList());
    }

    public JDBCParameter getFieldValue(String fieldName, Object key, String uniqueKey) {
        if (fieldName.contains(Symbol.COMMA)) {
            fieldName = fieldName.split("\\.")[1];
        }

        Field uniqueField = null;
        if (StringUtility.isNullOrEmpty(uniqueKey)) {
            uniqueField = this.entityManager.getPrimary();
        } else {
            uniqueField = this.entityManager.getUniqueField(uniqueKey);
        }

        fieldName = this.entityManager.getField(fieldName).getColumnName();
        String select = String.format("select %1$s from %2$s where %3$s=?", fieldName,
                this.entityManager.getDialectTableName(), uniqueField.getColumnName());
        return new JDBCParameter(select, Collections.singletonList(new Parameter(uniqueField, key)));
    }

    private Pair<String, List<Parameter>> generateIdsParameters(Collection<I> ids) {
        if (ids.size() <= 1) {
            throw new IllegalArgumentException("ids must be great 1");
        }
        String whereClause = null;
        List<Parameter> parameterList = new ArrayList<>();
        String idArguments = StringUtility.generateCharacter(ids.size(), '?', ',');
        /**
         * select * from t where id in (ids)
         * select * from t where id in (?,?,?)
         */
        whereClause = String.format(" in(%s)", idArguments);
        for (I id : ids) {
            parameterList.add(new Parameter("id", this.entityManager.getPrimary().getType(), 0, this.entityManager.getPrimary().convert(id)));
        }
        return new Pair<>(whereClause, parameterList);
    }

    /**
     * 1. in 条件下存在 sql注入问题
     * <p>
     * 2. modified user name 相关写死在代码里
     * <p>
     * 3. sql 的拼接不够优雅
     *
     * @param statusCriteria
     * @return
     */
    public JDBCParameter changeStatus(StatusCriteria<I> statusCriteria) {
        List<Parameter> parameterList = new ArrayList<>();
        parameterList.add(new Parameter(this.entityManager.getStatus(), statusCriteria.getStatus()));
        parameterList.add(new Parameter("modifiedUserName", String.class, 0, statusCriteria.getModifiedUserName()));
        parameterList.add(new Parameter("modifiedUserId", Long.class, 0, statusCriteria.getModifiedUserId()));
        parameterList.add(new Parameter("gmtModified", Long.class, 0, statusCriteria.getGmtModified()));

        Collection<I> idArray = statusCriteria.getIds();
        String whereClause = null;
        if (idArray.size() > 1) {
            Pair<String, List<Parameter>> idsParameters = this.generateIdsParameters(idArray);
            /**
             * select * from t where id in (ids)
             * select * from t where id in (?,?,?)
             */
            whereClause = idsParameters.getFirst();
            parameterList.addAll(idsParameters.getSecond());
        } else {
            whereClause = " =?";
            parameterList.add(new Parameter(this.entityManager.getPrimary(), this.entityManager.getPrimary().convert(statusCriteria.getIds())));
        }

        String updateSql = String.format("update %1$s set " +
                        " %2$s=?," +
                        "`modified_user_name`=?," +
                        "`modified_user_id`=?," +
                        "`gmt_modified`=? where %3$s %4$s",
                this.entityManager.getDialectTableName(),
                this.entityManager.getStatus().getColumnName(),
                this.entityManager.getPrimary().getColumnName(),
                whereClause);
        return new JDBCParameter(updateSql, parameterList);
    }

    public JDBCParameter batchDelete(Collection<I> ids) {
        List<Parameter> parameterList = new ArrayList<>();
        String whereClause;
        if (ids.size() > 1) {
            Pair<String, List<Parameter>> idsParameters = this.generateIdsParameters(ids);
            /**
             * select * from t where id in (ids)
             * select * from t where id in (?,?,?)
             */
            whereClause = idsParameters.getFirst();
            parameterList.addAll(idsParameters.getSecond());
        } else {
            whereClause = " =?";
            parameterList.add(new Parameter(this.entityManager.getPrimary(), this.entityManager.getPrimary().convert(ids.iterator().next())));
        }
        String updateSql = String.format("DELETE FROM %1$s where %2$s %3$s",
                this.entityManager.getDialectTableName(),
                this.entityManager.getPrimary().getColumnName(),
                whereClause);
        return new JDBCParameter(updateSql, parameterList);
    }
}
