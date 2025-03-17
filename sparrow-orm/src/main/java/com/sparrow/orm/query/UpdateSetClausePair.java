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

import com.sparrow.orm.query.impl.SimpleCriteriaField;
import com.sparrow.protocol.SFunction;
import com.sparrow.utility.ClassUtility;

public class UpdateSetClausePair {
    private CriteriaField field;
    private Object value;
    private Boolean add = false;

    public static UpdateSetClausePair field(String field) {
        return new UpdateSetClausePair(field);
    }

    public static <T> UpdateSetClausePair field(SFunction<T, ?> function) {
        String field = ClassUtility.getPropertyNameAndClassName(function).entityDotProperty();
        return new UpdateSetClausePair(field);
    }


    private UpdateSetClausePair(String field) {
        this.field = new SimpleCriteriaField(field);
    }

    public UpdateSetClausePair equal(Object value) {
        this.value = value;
        return this;
    }

    public UpdateSetClausePair add(Number value) {
        this.add = true;
        this.value = value;
        return this;
    }

    public CriteriaField getField() {
        return field;
    }

    public Object getValue() {
        return value;
    }

    public Boolean getAdd() {
        return add;
    }
}
