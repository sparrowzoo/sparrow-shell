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

import com.sparrow.enums.Order;
import com.sparrow.orm.query.impl.SimpleCriteriaField;

public class OrderCriteria {
    private CriteriaField field;
    private Order order;

    public static OrderCriteria asc(String field) {
        return new OrderCriteria(field, Order.ASC);
    }

    public static OrderCriteria desc(String field) {
        return new OrderCriteria(field, Order.DESC);
    }

    private OrderCriteria(String field, Order order) {
        this.field = new SimpleCriteriaField(field);
        this.order = order;
    }

    public CriteriaField getField() {
        return field;
    }

    public Order getOrder() {
        return order;
    }
}
