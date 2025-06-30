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
package com.sparrow.protocol.dao;

import com.sparrow.protocol.enums.StatusRecord;
import lombok.Data;

import java.util.Collection;

@Data
public class StatusCriteria<I> {
    public StatusCriteria(Collection<I> ids, StatusRecord status) {
        this.ids = ids;
        this.status = status;
    }

    private Collection<I> ids;
    private StatusRecord status;
    private Long modifiedUserId;
    private String modifiedUserName;
    private Long gmtModified;
}
