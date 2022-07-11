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

package com.sparrow.orm.query.sql;

import com.sparrow.orm.Parameter;
import java.util.ArrayList;
import java.util.List;

public class OperationEntity {
    private StringBuilder clause = new StringBuilder();
    private List<Parameter> parameterList = new ArrayList<Parameter>();

    public OperationEntity() {
    }

    public OperationEntity(StringBuilder clause, List<Parameter> parameterList) {
        this.clause = clause;
        this.parameterList = parameterList;
    }

    public void add(OperationEntity boolOperationEntity) {
        this.clause.append(boolOperationEntity.getClause());
        this.parameterList.addAll(boolOperationEntity.getParameterList());
    }

    public StringBuilder getClause() {
        return clause;
    }

    public void setClause(StringBuilder clause) {
        this.clause = clause;
    }

    public List<Parameter> getParameterList() {
        return parameterList;
    }

    public void setParameterList(List<Parameter> parameterList) {
        this.parameterList = parameterList;
    }
}
