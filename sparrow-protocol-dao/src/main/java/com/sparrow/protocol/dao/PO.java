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

import com.sparrow.protocol.POJO;
import com.sparrow.protocol.enums.StatusRecord;
import lombok.Data;

import javax.persistence.Column;

@Data
public class PO implements POJO {
    @Column(name = "create_user_name", columnDefinition = "varchar(64)  DEFAULT '' COMMENT '创建人'", nullable = false, updatable = false)
    private String createUserName;
    @Column(name = "create_user_id", columnDefinition = "int(11) UNSIGNED  DEFAULT 0 COMMENT '创建人ID'", nullable = false, updatable = false)
    private Long createUserId;
    @Column(name = "modified_user_id", columnDefinition = "int(11) unsigned  DEFAULT 0 COMMENT '更新人ID'", nullable = false)
    private Long modifiedUserId;
    @Column(name = "modified_user_name", columnDefinition = "varchar(64)  DEFAULT '' COMMENT '更新人'", nullable = false)
    private String modifiedUserName;
    @Column(name = "gmt_create", columnDefinition = "bigint(11)  DEFAULT 0 COMMENT '创建时间'", nullable = false, updatable = false)
    private Long gmtCreate;
    @Column(name = "gmt_modified", columnDefinition = "bigint(11)  DEFAULT 0 COMMENT '更新时间'", nullable = false)
    private Long gmtModified;
    @Column(name = "deleted", columnDefinition = "tinyint(1)  DEFAULT 0 COMMENT '是否删除'", nullable = false)
    private Boolean deleted;
    @Column(name = "status",updatable = false,
            columnDefinition = "tinyint(3) UNSIGNED DEFAULT 0 COMMENT '状态'",
            nullable = false)
    private StatusRecord status;
}
