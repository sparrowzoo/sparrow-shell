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

package com.sparrow.orm.query;

import com.sparrow.enums.ComparisonOperator;
import com.sparrow.orm.query.impl.SimpleCriteriaField;
import com.sparrow.protocol.SFunction;
import com.sparrow.utility.ClassUtility;

public class Criteria {

    private Criteria(String field, Boolean alias) {
        this.field = new SimpleCriteriaField(field);
        this.alias = alias;
    }

    public static Criteria alias(String field) {
        return new Criteria(field, true);
    }

    public static Criteria field(String field) {
        return new Criteria(field, false);
    }

    public static <T> Criteria field(SFunction<T,?> criteria) {
        ClassUtility.PropertyWithEntityName property = ClassUtility.getPropertyNameAndClassName(criteria);
        return Criteria.field(property.entityDotProperty());
    }

    private CriteriaField field;

    /**
     * 条件是否使用别名
     */
    private boolean alias = true;

    private CriteriaEntry criteriaEntry;

    public CriteriaEntry getCriteriaEntry() {
        return criteriaEntry;
    }

    public boolean isAlias() {
        return alias;
    }


    public Criteria() {
    }

    public Criteria lessThanEqual(Object upperBound) {
        this.criteriaEntry = new CriteriaEntry(ComparisonOperator.LESS_THAN_OR_EQUAL, upperBound);
        return this;
    }

    public Criteria lessThan(Object upperBound) {
        this.criteriaEntry = new CriteriaEntry(ComparisonOperator.LESS_THAN, upperBound);
        return this;
    }

    public Criteria notEqual(Object value) {
        this.criteriaEntry = new CriteriaEntry(ComparisonOperator.NOT_EQUAL, value);
        return this;
    }

    public Criteria equal(Object value) {
        this.criteriaEntry = new CriteriaEntry(ComparisonOperator.EQUAL, value);
        return this;
    }

    public Criteria greaterThanEqual(Object lowerBound) {
        this.criteriaEntry = new CriteriaEntry(ComparisonOperator.GREATER_THAN_OR_EQUAL, lowerBound);
        return this;
    }

    public Criteria greaterThan(Object lowerBound) {
        this.criteriaEntry = new CriteriaEntry(ComparisonOperator.GREATER_THAN, lowerBound);
        return this;
    }

    public Criteria startWith(Object value) {
        this.criteriaEntry = new CriteriaEntry(ComparisonOperator.START_WITH, value);
        return this;
    }

    public Criteria endWith(Object value) {
        this.criteriaEntry = new CriteriaEntry(ComparisonOperator.END_WITH, value);
        return this;
    }

    public Criteria contains(Object value) {
        this.criteriaEntry = new CriteriaEntry(ComparisonOperator.CONTAIN, value);
        return this;
    }

    public Criteria notContains(Object value) {
        this.criteriaEntry = new CriteriaEntry(ComparisonOperator.NOT_CONTAIN, value);
        return this;
    }

    public Criteria in(Object... values) {
        this.criteriaEntry = new CriteriaEntry(ComparisonOperator.IN, values);
        return this;
    }

    public Criteria in(String values) {
        this.criteriaEntry = new CriteriaEntry(ComparisonOperator.IN, values);
        return this;
    }

    public Criteria in(Iterable<?> values) {
        this.criteriaEntry = new CriteriaEntry(ComparisonOperator.IN, values);
        return this;
    }

    public Criteria notIn(Object... values) {
        this.criteriaEntry = new CriteriaEntry(ComparisonOperator.NOT_IN, values);
        return this;
    }

    public Criteria notIn(String values) {
        this.criteriaEntry = new CriteriaEntry(ComparisonOperator.NOT_IN, values);
        return this;
    }

    public Criteria notIn(Iterable<?> values) {
        this.criteriaEntry = new CriteriaEntry(ComparisonOperator.NOT_IN, values);
        return this;
    }

    public Criteria isNull() {
        this.criteriaEntry = new CriteriaEntry(ComparisonOperator.IS_NULL);
        return this;
    }

    public Criteria isNotNull() {
        this.criteriaEntry = new CriteriaEntry(ComparisonOperator.IS_NOT_NULL);
        return this;
    }

    public Criteria mod(Integer mod, Integer remainder) {
        this.criteriaEntry = new CriteriaEntry(ComparisonOperator.MOD, remainder, mod);
        return this;
    }

    public CriteriaField getField() {
        return this.field;
    }

    public static class CriteriaEntry {

        private ComparisonOperator key;
        private Object value;
        private Integer mod;

        CriteriaEntry(ComparisonOperator key) {
            this(key, null, null);
        }

        CriteriaEntry(ComparisonOperator key, Object value) {
            this(key, value, null);
        }

        CriteriaEntry(ComparisonOperator key, Object value, Integer mod) {
            this.key = key;
            this.value = value;
            this.mod = mod;
        }

        public ComparisonOperator getKey() {
            return key;
        }

        public Object getValue() {
            return value;
        }

        public Integer getMod() {
            return mod;
        }

        @Override
        public String toString() {
            return "CriteriaEntry{" +
                    "key=" + key +
                    ", value=" + value +
                    '}';
        }
    }
}
