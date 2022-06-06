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

package com.sparrow.protocol.db;

import com.sparrow.protocol.pager.PagerQuery;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * table identify 可以跨db mybatis hibernate jdbc elastic search
 *
 * @param <T>
 * @author harry
 */
public interface DaoSupport<T, I> {

    /**
     * 新建(支持分表)
     *
     * @param model
     */
    Long insert(T model);

    /**
     * 批量插入
     * @param models
     * @return
     */
    //Long batchInsert(List<T> models);

    /**
     * 更新
     * <p/>
     * 支持分表
     *
     * @param model
     */
    int update(T model);

    /**
     * 修改记录状态
     *
     * @param statusCriteria
     * @return
     */
    int changeStatus(StatusCriteria statusCriteria);

    /**
     * 删除指定记录
     *
     * @param id
     */
    int delete(I id);

    /**
     * 根据id批量删除
     *
     * @param ids
     */
    int batchDelete(String ids);

    T getEntity(I id);

    T getEntityByUnique(UniqueKeyCriteria uniqueKeyCriteria);

    List<T> getList();

    List<T> getList(Collection<I> ids);

    Map<I, T> getEntityMap(Collection<I> ids);

    List<T> getList(PagerQuery query);

    Long getCountByUnique(UniqueKeyCriteria uniqueKeyCriteria);

    Long getCount(I key);

    <X> X getFieldValueByUnique(UniqueKeyCriteria uniqueKeyCriteria);

    <X> X getAggregate(AggregateCriteria aggregateCriteria);
}