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

import com.sparrow.protocol.dao.RowMapper;
import com.sparrow.protocol.pager.PagerQuery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WhereCriteria extends PagerQuery {
    public WhereCriteria() {
        super(0, 0);
    }

    public WhereCriteria(Integer currentPageIndex, Integer pageSize) {
        super(currentPageIndex, pageSize);
    }

    private List<Object> tableSuffix = new ArrayList<Object>();

    /**
     * 条件
     */
    private BooleanCriteria where;

    /**
     * 排序条件
     */
    private List<OrderCriteria> orderCriteriaList;

    private RowMapper rowMapper;

    public BooleanCriteria getWhere() {
        return where;
    }

    public void setWhere(BooleanCriteria where) {
        this.where = where;
    }

    public void setWhere(Criteria where) {
        this.where = BooleanCriteria.criteria(where);
    }

    public List<OrderCriteria> getOrderCriteriaList() {
        return orderCriteriaList;
    }

    public List<OrderCriteria> setOrderCriteria(OrderCriteria orderCriteria) {
        this.orderCriteriaList = new ArrayList<OrderCriteria>(5);
        this.orderCriteriaList.add(orderCriteria);
        return this.orderCriteriaList;
    }

    public List<OrderCriteria> addOrderCriteria(OrderCriteria orderCriteria) {
        if (this.orderCriteriaList == null) {
            this.orderCriteriaList = new ArrayList<OrderCriteria>(5);
        }
        this.orderCriteriaList.add(orderCriteria);
        return this.orderCriteriaList;
    }

    public RowMapper getRowMapper() {
        return rowMapper;
    }

    public void setRowMapper(RowMapper rowMapper) {
        this.rowMapper = rowMapper;
    }

    public List<Object> getTableSuffix() {
        return tableSuffix;
    }

    /**
     * 自定义条件，需要手动设置表后缀f
     *
     * @param tableSuffix
     */
    public void setTableSuffix(Object... tableSuffix) {
        List<Object> suffixList = new ArrayList<Object>(tableSuffix.length);
        Collections.addAll(suffixList, tableSuffix);
        this.tableSuffix = suffixList;
    }
}
