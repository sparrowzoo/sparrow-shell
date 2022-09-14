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

package com.sparrow.orm.query.sql.impl.criteria.processor;

import com.sparrow.protocol.constant.magic.Symbol;
import com.sparrow.container.ClassFactoryBean;
import com.sparrow.enums.ComparisonOperator;
import com.sparrow.orm.EntityManager;
import com.sparrow.orm.EntityManagerFactoryBean;
import com.sparrow.orm.Field;
import com.sparrow.orm.Parameter;
import com.sparrow.orm.query.*;
import com.sparrow.orm.query.impl.SimpleCriteriaField;
import com.sparrow.orm.query.sql.CriteriaProcessor;
import com.sparrow.orm.query.sql.OperationEntity;
import com.sparrow.orm.query.sql.RelationOperationEntity;
import com.sparrow.orm.query.sql.impl.operation.*;
import com.sparrow.protocol.dao.enums.Aggregate;
import com.sparrow.utility.StringUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class SqlCriteriaProcessorImpl implements CriteriaProcessor {
    private static SqlCriteriaProcessorImpl sqlCriteriaProcessor = new SqlCriteriaProcessorImpl();

    private SqlCriteriaProcessorImpl() {
    }

    public static SqlCriteriaProcessorImpl getInstance() {
        return sqlCriteriaProcessor;
    }

    private Logger logger = LoggerFactory.getLogger(SqlCriteriaProcessorImpl.class);
    private ClassFactoryBean<EntityManager> entityManagerFactoryBean = EntityManagerFactoryBean.getInstance();

    @Override
    public String fields(String fields) {
        if (StringUtility.isNullOrEmpty(fields)) {
            return Symbol.STAR;
        }
        if (fields.contains(Symbol.COMMA)) {
            String[] fieldArray = fields.split(Symbol.COMMA);
            StringBuilder fieldBuilder = new StringBuilder();
            for (String field : fieldArray) {
                if (!StringUtility.isNullOrEmpty(fieldBuilder)) {
                    fieldBuilder.append(Symbol.COMMA);
                }
                CriteriaField criteriaField = new SimpleCriteriaField(field);
                String column = entityManagerFactoryBean.getObject(criteriaField.getAlias()).getColumnName(criteriaField.getName());
                fieldBuilder.append(column);
            }
            return fieldBuilder.toString();
        }

        CriteriaField criteriaField = new SimpleCriteriaField(fields);
        return entityManagerFactoryBean.getObject(criteriaField.getAlias()).getColumnName(criteriaField.getName());
    }

    @Override
    public OperationEntity where(BooleanCriteria booleanCriteria) {
        OperationEntity operationEntity = new OperationEntity();
        if (booleanCriteria == null) {
            return operationEntity;
        }
        if (booleanCriteria.getCriteriaList() != null && booleanCriteria.getCriteriaList().size() > 0) {

            StringBuilder whereClause = new StringBuilder();
            List<Parameter> parameters = new ArrayList<Parameter>();
            for (BooleanCriteria.CriteriaLinker linker : booleanCriteria.getCriteriaList()) {

                Criteria criteria = linker.getCriteria();
                RelationOperationEntity relationOperationEntity;

                if (criteria.getCriteriaEntry().getKey().equals(ComparisonOperator.IS_NULL) || criteria.getCriteriaEntry().getKey().equals(ComparisonOperator.IS_NOT_NULL)) {
                    relationOperationEntity = new IsNullOperation().operation(criteria);
                } else {
                    if (StringUtility.isNullOrEmpty(criteria.getCriteriaEntry().getValue())) {
                        continue;
                    }
                    switch (criteria.getCriteriaEntry().getKey()) {
                        case IN:
                        case NOT_IN:
                            relationOperationEntity = new InOperation().operation(criteria);
                            break;
                        case START_WITH:
                            relationOperationEntity = new WildcardOperation("CONCAT(?,'%')").operation(criteria);
                            break;
                        case END_WITH:
                            relationOperationEntity = new WildcardOperation("CONCAT('%',?)").operation(criteria);
                            break;
                        case CONTAIN:
                            relationOperationEntity = new WildcardOperation("CONCAT('%',?,'%')").operation(criteria);
                            break;
                        case NOT_CONTAIN:
                            relationOperationEntity = new WildcardOperation("CONCAT('%',?,'%')").operation(criteria);
                            break;
                        case MOD:
                            relationOperationEntity = new ModOperation().operation(criteria);
                            break;
                        default:
                            relationOperationEntity = new BinaryOperation().operation(criteria);
                    }
                }
                if (linker.getKey() != null && whereClause.length() > 0) {
                    whereClause.append(Symbol.BLANK);
                    whereClause.append(linker.getKey().name());
                }
                whereClause.append(Symbol.BLANK);
                whereClause.append(relationOperationEntity.getCriteria());
                if (relationOperationEntity.getParameter() != null) {
                    parameters.add(relationOperationEntity.getParameter());
                }
            }
            if (whereClause.length() > 1) {
                whereClause.insert(0, Symbol.LEFT_PARENTHESIS);
                whereClause.append(Symbol.RIGHT_PARENTHESIS);
                operationEntity.add(new OperationEntity(whereClause, parameters));
            }
        }

        if (booleanCriteria.getBooleanCriteriaList() != null && booleanCriteria.getBooleanCriteriaList().size() > 0) {
            List<BooleanCriteria.BooleanCriteriaLinker> boolOperationEntityList = booleanCriteria.getBooleanCriteriaList();
            for (BooleanCriteria.BooleanCriteriaLinker linker : boolOperationEntityList) {
                OperationEntity oe = this.where(linker.getCriteria());
                if (oe != null && !StringUtility.isNullOrEmpty(oe.getClause())) {
                    if (linker.getKey() != null && !StringUtility.isNullOrEmpty(operationEntity.getClause())) {
                        operationEntity.getClause().append(linker.getKey().name());
                    }
                    operationEntity.add(oe);
                }
            }
        }
        return operationEntity;
    }

    @Override
    public String order(List<OrderCriteria> orderCriteriaList) {
        if (orderCriteriaList == null || orderCriteriaList.size() == 0) {
            return Symbol.BLANK;
        }
        StringBuilder sb = new StringBuilder();
        for (OrderCriteria orderCriteria : orderCriteriaList) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            String column = entityManagerFactoryBean.getObject(orderCriteria.getField().getAlias()).getColumnName(orderCriteria.getField().getName());
            sb.append(column + Symbol.BLANK + orderCriteria.getOrder().name());
        }
        return sb.toString();
    }

    @Override
    public OperationEntity setClause(List<UpdateSetClausePair> setClausePairs) {
        StringBuilder clause = new StringBuilder();
        List<Parameter> parameters = new ArrayList<Parameter>();
        for (UpdateSetClausePair setClausePair : setClausePairs) {
            if (clause.length() > 0) {
                clause.append(",");
            }
            Field field = entityManagerFactoryBean.getObject(setClausePair.getField().getAlias()).getField(setClausePair.getField().getName());
            if (field == null) {
                throw new RuntimeException("field is not found \nalias is '" + setClausePair.getField().getAlias() + "' set-clause-name is '" + setClausePair.getField().getName() + "'");
            }
            String column = field.getColumnName();
            clause.append(column + Symbol.EQUAL);
            if (setClausePair.getAdd()) {
                clause.append(column + Symbol.ADD);
            }
            clause.append("?");
            Parameter parameter = new Parameter(field, setClausePair.getValue());
            parameters.add(parameter);
        }
        return new OperationEntity(clause, parameters);
    }

    @Override
    public String aggregate(Aggregate aggregate, String field) {
        String column = this.fields(field);
        switch (aggregate) {
            case COUNT:
                return "COUNT(" + column + ")";
            case SUM:
                return "SUM(" + column + ")";
            case AVG:
                return "AVG(" + column + ")";
            case MAX:
                return "MAX(" + column + ")";
            case MIN:
                return "MIN(" + column + ")";
            default:
                return "";
        }
    }
}
