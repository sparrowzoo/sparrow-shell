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

package com.sparrow.support.workflow;

import com.sparrow.exception.Asserts;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.constant.SparrowError;
import com.sparrow.utility.StringUtility;

public class Operation {
    private String[] allowStatus;
    private String[] allowRole;
    private String name;
    private String description;
    private Integer resultStatus;

    public Operation(String[] allowStatus, String[] allowRole, String name, String description, Integer resultStatus) {
        this.allowStatus = allowStatus;
        this.allowRole = allowRole;
        this.name = name;
        this.description = description;
        this.resultStatus = resultStatus;
    }

    public Operation(String allowStatus, String allowRole, String name, String description, Integer resultStatus) {
        this.allowStatus = new String[] {allowStatus};
        this.allowRole = new String[] {allowRole};
        this.name = name;
        this.description = description;
        this.resultStatus = resultStatus;
    }

    public Operation(String allowStatus, String allowRole, String name, String description) {
        this(allowStatus, allowRole, name, description, null);
    }

    public Boolean validate(Integer currentStatus, Integer currentRole) throws BusinessException {
        Asserts.isTrue(StringUtility.existInArray(this.allowStatus, currentStatus), SparrowError.GLOBAL_OPERATION_VALIDATE_STATUS_INVALID);
        Asserts.isTrue(StringUtility.existInArray(this.allowRole, currentRole), SparrowError.GLOBAL_OPERATION_VALIDATE_ROLE_INVALID);
        return true;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Integer getResultStatus() {
        return resultStatus;
    }
}
