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

package com.sparrow.orm.template.impl;

import com.sparrow.constant.ConfigKeyDB;
import com.sparrow.orm.query.Criteria;
import com.sparrow.protocol.constant.magic.DIGIT;
import com.sparrow.core.Pair;
import com.sparrow.orm.*;
import com.sparrow.orm.query.SearchCriteria;
import com.sparrow.orm.query.UpdateCriteria;
import com.sparrow.orm.query.sql.CriteriaProcessor;
import com.sparrow.orm.query.sql.OperationEntity;
import com.sparrow.orm.query.sql.impl.criteria.processor.SqlCriteriaProcessorImpl;
import com.sparrow.orm.template.SparrowDaoSupport;
import com.sparrow.protocol.dao.AggregateCriteria;
import com.sparrow.protocol.dao.StatusCriteria;
import com.sparrow.protocol.dao.UniqueKeyCriteria;
import com.sparrow.protocol.dao.enums.DatabaseSplitStrategy;
import com.sparrow.protocol.pager.PagerQuery;
import com.sparrow.orm.JDBCSupport;
import com.sparrow.utility.ClassUtility;
import com.sparrow.utility.StringUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class DBORMTemplate<T, I> implements SparrowDaoSupport<T, I> {
    private static Logger logger = LoggerFactory.getLogger(OrmMetadataAccessor.class);
    protected CriteriaProcessor criteriaProcessor = SqlCriteriaProcessorImpl.getInstance();
    /**
     * 实体类
     */
    private Class<?> modelClazz = null;

    private String modelName;
    /**
     * 数据库辅助对象
     */
    protected final JDBCSupport jdbcSupport;

    private OrmMetadataAccessor<T> ormMetadataAccessor;

    public DBORMTemplate(Class clazz) {
        this.modelClazz = clazz;
        if (this.modelClazz != null) {
            this.modelName = ClassUtility.getEntityNameByClass(this.modelClazz);
        }
        this.ormMetadataAccessor = new OrmMetadataAccessor<T>(this.modelClazz, this.criteriaProcessor);
        DatabaseSplitStrategy databaseSplitKey = this.ormMetadataAccessor.getEntityManager().getDatabaseSplitStrategy();
        this.jdbcSupport = JDBCTemplate.getInstance(this.ormMetadataAccessor.getEntityManager().getSchema(), databaseSplitKey);
    }

    @Override
    public Long insert(T model) {
        try {
            JDBCParameter jdbcParameter = this.ormMetadataAccessor.insert(model);
            if (jdbcParameter.isAutoIncrement()) {
                Long id = this.jdbcSupport.executeAutoIncrementInsert(jdbcParameter);
                this.ormMetadataAccessor.getMethodAccessor().set(model, this.ormMetadataAccessor.getEntityManager().getPrimary().getName(), id);
                return id;
            } else {
                this.jdbcSupport.executeUpdate(jdbcParameter);
                return 0L;
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int update(T model) {
        return this.jdbcSupport.executeUpdate(this.ormMetadataAccessor.update(model));
    }

    @Override
    public int update(UpdateCriteria criteria) {
        return this.jdbcSupport.executeUpdate(this.ormMetadataAccessor.update(criteria));
    }

    @Override
    public int delete(I id) {
        return this.jdbcSupport.executeUpdate(this.ormMetadataAccessor.delete(id));
    }

    @Override
    public int delete(SearchCriteria criteria) {
        return this.jdbcSupport.executeUpdate(this.ormMetadataAccessor.delete(criteria));
    }

    @Override
    public int batchDelete(String ids) {
        JDBCParameter parameter = this.ormMetadataAccessor.batchDelete(ids);
        return this.jdbcSupport.executeUpdate(parameter);
    }

    private JDBCParameter getSelectSql(SearchCriteria searchCriteria) {
        if (searchCriteria == null) {
            searchCriteria = new SearchCriteria();
        }
        StringBuilder selectSql = new StringBuilder();
        OperationEntity boolOperationEntity = this.criteriaProcessor.where(searchCriteria.getWhere());
        String whereClause = boolOperationEntity.getClause().toString();
        String orderClause = this.criteriaProcessor.order(searchCriteria.getOrderCriteriaList());
        selectSql.append("select ");

        if (searchCriteria.getAggregate() == null) {
            String fields = this.criteriaProcessor.fields(searchCriteria.getFields());
            if (searchCriteria.getDistinct()) {
                selectSql.append(" distinct ");
            }
            selectSql.append(fields);
        } else {
            String columns = this.criteriaProcessor.aggregate(searchCriteria.getAggregate(), searchCriteria.getFields());
            selectSql.append(columns);
        }
        selectSql.append(" from " + this.ormMetadataAccessor.getTableName(searchCriteria.getTableSuffix()));
        if (!StringUtility.isNullOrEmpty(whereClause)) {
            selectSql.append(" where " + whereClause);
        }

        if (!StringUtility.isNullOrEmpty(orderClause)) {
            selectSql.append(" order by " + orderClause);
        }

        if (!StringUtility.isNullOrEmpty(searchCriteria.getPageSize())
            && searchCriteria.getPageSize() != DIGIT.ALL) {
            selectSql.append(searchCriteria.getLimitClause());
        }
        logger.info(selectSql.toString());
        return new JDBCParameter(selectSql.toString(), boolOperationEntity.getParameterList());
    }

    private ResultSet select(SearchCriteria searchCriteria) {
        if (searchCriteria == null) {
            searchCriteria = new SearchCriteria();
        }
        JDBCParameter jdbcParameter = this.getSelectSql(searchCriteria);
        return this.jdbcSupport.executeQuery(jdbcParameter);
    }

    @Override
    public T getEntity(I id) {
        return this.getEntityByUnique(UniqueKeyCriteria.createUniqueCriteria(id, ConfigKeyDB.ORM_PRIMARY_KEY_UNIQUE));
    }

    @Override
    public T getEntityByUnique(UniqueKeyCriteria uniqueKeyCriteria) {
        StringBuilder select = new StringBuilder("select ");
        select.append(this.ormMetadataAccessor.getEntityManager().getFields());
        select.append(" from "
            + this.ormMetadataAccessor.getEntityManager().getDialectTableName());
        Field uniqueField = this.ormMetadataAccessor.getEntityManager().getUniqueField(uniqueKeyCriteria.getUniqueFieldName());
        select.append(" where " + uniqueField.getColumnName() + "=?");
        JDBCParameter jdbcParameter = new JDBCParameter(select.toString(), Collections.singletonList(new Parameter(uniqueField, uniqueField.convert(uniqueKeyCriteria.getKey().toString()))));
        ResultSet rs = this.jdbcSupport.executeQuery(jdbcParameter);

        if (rs == null) {
            return null;
        }
        T t = null;
        try {
            if (rs.next()) {
                t = this.ormMetadataAccessor.setEntity(rs, null);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            this.jdbcSupport.release(rs);
        }
        return t;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T getEntity(SearchCriteria criteria) {
        ResultSet rs = this.select(criteria);
        if (rs == null) {
            return null;
        }
        T model = null;
        try {
            if (rs.next()) {
                if (criteria != null && criteria.getRowMapper() != null) {
                    model = (T) criteria.getRowMapper().mapRow(rs, rs.getRow());
                } else {
                    model = this.ormMetadataAccessor.setEntity(rs, null);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            jdbcSupport.release(rs);
        }
        return model;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> getList(SearchCriteria criteria) {
        //返回null会报错
        List<T> list;
        if (criteria != null && criteria.getPageSize() != null && criteria.getPageSize() > 0) {
            list = new ArrayList<T>(criteria.getPageSize());
        } else {
            list = new ArrayList<T>();
        }

        ResultSet rs = this.select(criteria);
        if (rs == null) {
            return list;
        }

        try {
            while (rs.next()) {
                T m;
                if (criteria != null && criteria.getRowMapper() != null) {
                    m = (T) criteria.getRowMapper().mapRow(rs, rs.getRow());
                } else {
                    m = this.ormMetadataAccessor.setEntity(rs, null);
                }
                list.add(m);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            this.jdbcSupport.release(rs);
        }
        return list;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<I, T> getEntityMap(SearchCriteria criteria) {
        //返回null会报错
        Map<I, T> map;
        if (criteria != null && criteria.getPageSize() != null && criteria.getPageSize() > 0) {
            map = new LinkedHashMap<>(criteria.getPageSize());
        } else {
            map = new LinkedHashMap<>();
        }

        ResultSet rs = this.select(criteria);
        if (rs == null) {
            return map;
        }

        try {
            while (rs.next()) {
                String primaryName = this.ormMetadataAccessor.getEntityManager().getPrimary().getName();
                T m = this.ormMetadataAccessor.setEntity(rs, null);
                I key = (I) this.ormMetadataAccessor.getMethodAccessor().get(m, primaryName);
                map.put(key, m);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            this.jdbcSupport.release(rs);
        }
        return map;
    }

    @Override
    public <Z> Set<Z> firstList(SearchCriteria criteria) {
        Set<Z> list = new LinkedHashSet<Z>();
        ResultSet rs = this.select(criteria);
        if (rs == null) {
            return list;
        }

        try {
            while (rs.next()) {
                Object o = rs.getObject(1);
                if (o instanceof Number) {
                    list.add((Z) o);
                } else {
                    list.add((Z) ("'" + o.toString().trim() + "'"));
                }
            }
        } catch (Exception ex) {
            logger.error("first result", ex);
            return list;
        } finally {
            this.jdbcSupport.release(rs);
        }
        return list;
    }

    @Override
    public <P> P scalar(SearchCriteria criteria) {
        return (P) this.jdbcSupport.executeScalar(this.getSelectSql(criteria));
    }

    @Override
    public List<T> getList() {
        return this.getList(new SearchCriteria());
    }

    @Override
    public List<T> getList(Collection<I> ids) {
        SearchCriteria searchCriteria = new SearchCriteria();
        String entityClassName = ClassUtility.getEntityNameByClass(this.ormMetadataAccessor.getModelClazz());
        String primaryProperty = this.ormMetadataAccessor.getEntityManager().getPrimary().getName();
        searchCriteria.setWhere(Criteria.field(entityClassName + "." + primaryProperty).in(ids));
        return this.getList(searchCriteria);
    }

    @Override
    public Map<I, T> getEntityMap(Collection<I> ids) {
        SearchCriteria searchCriteria = new SearchCriteria();
        String entityClassName = ClassUtility.getEntityNameByClass(this.ormMetadataAccessor.getModelClazz());
        String primaryProperty = this.ormMetadataAccessor.getEntityManager().getPrimary().getName();
        searchCriteria.setWhere(Criteria.field(entityClassName + "." + primaryProperty).in(ids));
        return this.getEntityMap(searchCriteria);
    }

    @Override
    public List<T> getList(PagerQuery query) {
        SearchCriteria criteria = new SearchCriteria(query);
        return this.getList(criteria);
    }

    @Override
    public <P, Q> Map<P, Q> getMap(SearchCriteria searchCriteria) {
        Map<P, Q> map = new LinkedHashMap<P, Q>();
        ResultSet rs = this.select(searchCriteria);
        if (rs == null) {
            return map;
        }
        try {
            String fields = null;
            if (searchCriteria.getRowMapper() == null) {
                fields = this.criteriaProcessor.fields(searchCriteria.getFields());
            }
            while (rs.next()) {
                if (searchCriteria.getRowMapper() != null) {
                    Pair<P, Q> entry = (Pair<P, Q>) searchCriteria.getRowMapper().mapRow(rs, rs.getRow());
                    map.put(entry.getFirst(), entry.getSecond());
                    continue;
                }
                Pair<String, String> fieldPair = Pair.split(fields, ",");
                map.put((P) rs.getObject(fieldPair.getFirst()), (Q) rs.getObject(fieldPair.getSecond()));
            }
            return map;
        } catch (Exception ex) {
            logger.error("get map", ex);
            return map;
        } finally {
            this.jdbcSupport.release(rs);
        }
    }

    @Override
    public Long getCountByUnique(UniqueKeyCriteria uniqueKeyCriteria) {
        JDBCParameter jdbcParameter = this.ormMetadataAccessor.getCount(uniqueKeyCriteria.getKey(), uniqueKeyCriteria.getUniqueFieldName());
        Object count = this.jdbcSupport.executeScalar(jdbcParameter);
        if (count == null) {
            return 0L;
        }
        return Long.valueOf(count.toString());
    }

    @Override
    public Long getCount(I key) {
        return this.getCountByUnique(UniqueKeyCriteria.createUniqueCriteria(key, ConfigKeyDB.ORM_PRIMARY_KEY_UNIQUE));
    }

    @Override
    public Long getCount(SearchCriteria criteria) {
        JDBCParameter jdbcParameter = this.ormMetadataAccessor.getCount(criteria);
        Long count = this.jdbcSupport.executeScalar(jdbcParameter);
        if (count == null) {
            return 0L;
        }
        return count;
    }

    @Override
    public <X> X getAggregateByCriteria(SearchCriteria searchCriteria) {
        JDBCParameter jdbcParameter = this.getSelectSql(searchCriteria);
        Object fieldValue = this.jdbcSupport.executeScalar(jdbcParameter);
        if (fieldValue == null) {
            return null;
        }
        return (X) fieldValue;
    }

    @Override
    public <X> X getAggregate(AggregateCriteria aggregateCriteria) {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setFields(aggregateCriteria.getField());
        searchCriteria.setAggregate(aggregateCriteria.getAggregate());
        return this.getAggregateByCriteria(searchCriteria);
    }

    @Override
    public <X> X getFieldValueByUnique(UniqueKeyCriteria uniqueKeyCriteria) {
        JDBCParameter jdbcParameter = this.ormMetadataAccessor.getFieldValue(uniqueKeyCriteria.getResultFiled(), uniqueKeyCriteria.getKey(), uniqueKeyCriteria.getUniqueFieldName());
        Object fieldValue = this.jdbcSupport.executeScalar(jdbcParameter);
        if (fieldValue == null) {
            return null;
        }
        return (X) fieldValue;
    }

    @Override
    public <X> X getFieldValue(SearchCriteria searchCriteria) {
        JDBCParameter jdbcParameter = this.getSelectSql(searchCriteria);
        Object fieldValue = this.jdbcSupport.executeScalar(jdbcParameter);
        if (fieldValue == null) {
            return null;
        }
        return (X) fieldValue;
    }

    @Override
    public int changeStatus(StatusCriteria statusCriteria) {
        JDBCParameter jdbcParameter = this.ormMetadataAccessor.changeStatus(statusCriteria.getIds(), statusCriteria.getStatus());
        return this.jdbcSupport.executeUpdate(jdbcParameter);
    }
}