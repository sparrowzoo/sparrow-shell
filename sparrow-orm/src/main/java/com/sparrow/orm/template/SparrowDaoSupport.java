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

package com.sparrow.orm.template;

import com.sparrow.orm.query.SearchCriteria;
import com.sparrow.orm.query.UpdateCriteria;
import com.sparrow.protocol.dao.DaoSupport;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * sparrow 跨jdbc
 */
public interface SparrowDaoSupport<T, I> extends DaoSupport<T, I> {

    /**
     * 获取map 非pair pair只能一行 map 则可以有多行 多行适应面更广
     *
     * @param criteria
     */
    <P, Q> Map<P, Q> getMap(SearchCriteria criteria);

    Map<I, T> getEntityMap(SearchCriteria criteria);

    <Z> Set<Z> firstList(SearchCriteria criteria);

    Long getCount(SearchCriteria criteria);

    <X> X getFieldValue(SearchCriteria criteria);

    T getEntity(SearchCriteria criteria);

    List<T> getList(SearchCriteria criteria);

    <P> P scalar(SearchCriteria criteria);

    /**
     * 删除N条记录根据条件
     *
     * @param criteria
     */
    int delete(SearchCriteria criteria);

    /**
     * 更新 指定字段
     * <p/>
     * <p/>
     *
     * @param criteria
     */
    int update(UpdateCriteria criteria);

    <X> X getAggregateByCriteria(SearchCriteria searchCriteria);
}
