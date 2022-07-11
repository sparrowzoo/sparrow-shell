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

import java.util.ArrayList;
import java.util.List;

public class BooleanCriteria {

    private List<CriteriaLinker> criteriaList = new ArrayList<CriteriaLinker>();

    private List<BooleanCriteriaLinker> booleanCriteriaList = new ArrayList<BooleanCriteriaLinker>();

    public static BooleanCriteria criteria(Criteria criteria) {
        return new BooleanCriteria(criteria);
    }

    public static BooleanCriteria criteria(BooleanCriteria booleanCriteria) {
        return new BooleanCriteria(booleanCriteria);
    }

    public List<CriteriaLinker> getCriteriaList() {
        return criteriaList;
    }

    public List<BooleanCriteriaLinker> getBooleanCriteriaList() {
        return booleanCriteriaList;
    }

    private BooleanCriteria(Criteria criteria) {
        this.criteriaList.add(new CriteriaLinker(null, criteria));
    }

    private BooleanCriteria(BooleanCriteria criteria) {
        this.booleanCriteriaList.add(new BooleanCriteriaLinker(null, criteria));
    }

    public BooleanCriteria and(Criteria criteria) {
        this.criteriaList.add(new CriteriaLinker(BoolOperationKey.AND, criteria));
        return this;
    }

    public BooleanCriteria and(BooleanCriteria booleanCriteria) {
        this.booleanCriteriaList.add(new BooleanCriteriaLinker(BoolOperationKey.AND, booleanCriteria));
        return this;
    }

    public BooleanCriteria or(Criteria criteria) {
        this.criteriaList.add(new CriteriaLinker(BoolOperationKey.OR, criteria));
        return this;
    }

    public BooleanCriteria or(BooleanCriteria booleanCriteria) {
        this.booleanCriteriaList.add(new BooleanCriteriaLinker(BoolOperationKey.OR, booleanCriteria));
        return this;
    }

    public enum BoolOperationKey {
        /**
         * 与
         */
        AND,
        /**
         * 或
         */
        OR
    }

    public class BooleanCriteriaLinker {
        BooleanCriteriaLinker(BoolOperationKey key, BooleanCriteria criteria) {
            this.key = key;
            this.criteria = criteria;
        }

        BoolOperationKey key;
        BooleanCriteria criteria;

        public BoolOperationKey getKey() {
            return key;
        }

        public BooleanCriteria getCriteria() {
            return criteria;
        }
    }

    public class CriteriaLinker {
        CriteriaLinker(BoolOperationKey key, Criteria criteria) {
            this.key = key;
            this.criteria = criteria;
        }

        BoolOperationKey key;
        Criteria criteria;

        public BoolOperationKey getKey() {
            return key;
        }

        public Criteria getCriteria() {
            return criteria;
        }
    }
}

