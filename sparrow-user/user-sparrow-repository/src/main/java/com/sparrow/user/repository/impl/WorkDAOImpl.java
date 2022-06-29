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
package com.sparrow.user.repository.impl;

import com.sparrow.orm.query.Criteria;
import com.sparrow.orm.query.OrderCriteria;
import com.sparrow.orm.query.SearchCriteria;
import com.sparrow.orm.template.impl.ORMStrategy;
import com.sparrow.protocol.dao.DaoSupport;
import com.sparrow.user.po.Work;

import java.util.List;

public class WorkDAOImpl extends ORMStrategy<Work, Long> implements
        DaoSupport<Work, Long> {
    public List<Work> getWork(Long userId) {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setWhere(Criteria.field("work.userId").equal(userId));
        searchCriteria.setOrderCriteria(OrderCriteria.asc("work.startTime"));
        try {
            return this.getList(searchCriteria);
        } catch (Exception e) {
            return null;
        }
    }
}
