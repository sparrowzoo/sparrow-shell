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

import com.sparrow.protocol.constant.magic.Symbol;
import com.sparrow.core.Pair;
import com.sparrow.enums.ComparisonOperator;
import com.sparrow.utility.StringUtility;
import java.util.List;

public class Expression {
    private boolean showAlias;
    private String alias;
    private Field field;

    public String getAlias() {
        return showAlias ? this.alias + Symbol.DOT : Symbol.EMPTY;
    }

    public Field getField() {
        return field;
    }

    public static Expression create(String property) {
        return create(property, null, true);
    }

    public static Expression create(String property, boolean showAlias) {
        return create(property, null, showAlias);
    }

    public static Expression create(String property, ComparisonOperator comparisonOperator, boolean showAlias) {
        return create(property, comparisonOperator, showAlias, null, null, null, true);
    }

    public static Expression create(String property, ComparisonOperator comparisonOperator, boolean showAlias,
        Object value, List<Parameter> parameters, List<String> clause) {
        return create(property, comparisonOperator, showAlias, value, parameters, clause, true);
    }

    public static Expression create(String property, ComparisonOperator comparisonOperator, boolean showAlias,
        Object value, List<Parameter> parameters, List<String> clause, boolean ignoreEmpty) {
        Expression expression = new Expression();
        Pair<String, String> propertyPair = Pair.split(property, "\\.");
        expression.alias = propertyPair.getFirst();
//        EntityManager entityManager = EntityManager.get(expression.alias);
//        Pair<String, String> fieldPair = Pair.split(propertyPair.getSecond(), "\\$");
//        expression.field = entityManager.getField(fieldPair.getFirst());
        expression.showAlias = showAlias;

        if (ignoreEmpty && StringUtility.isNullOrEmpty(value)) {
            return expression;
        }

        //value 不要换成 因为复杂数据类型转换会出错
        if (parameters != null && !StringUtility.isNullOrEmpty(value)) {
            parameters.add(new Parameter(expression.field, value));
        }

        String result;
        if (comparisonOperator != null && clause != null && value != null) {
            String alias = expression.showAlias ? expression.alias + Symbol.DOT : Symbol.EMPTY;
            result = alias + expression.field.getColumnName() + Symbol.BLANK;
//            if (!StringUtility.isNullOrEmpty(fieldPair.getSecond())) {
//                String operator = fieldPair.getSecond().substring(0, 1);
//                result = result + operator + fieldPair.getSecond().substring(1);
//            }
            result = result + comparisonOperator.rendered();
            if (parameters != null) {
                result = result + Symbol.QUESTION_MARK;
            } else {
                if (StringUtility.isNullOrEmpty(value)) {
                    value = "''";
                }
                result = result + Symbol.BLANK + value;
            }
            clause.add(result);
        }
        return expression;
    }

    public static Expression empty(String property, boolean showAlias, List<String> clause) {
        return create(property, ComparisonOperator.EQUAL, showAlias, Symbol.EMPTY, null, clause, false);
    }

    public static Expression notEmpty(String property, boolean showAlias, List<String> clause) {
        return create(property, ComparisonOperator.NOT_EQUAL, showAlias, Symbol.EMPTY, null, clause, false);
    }
}
