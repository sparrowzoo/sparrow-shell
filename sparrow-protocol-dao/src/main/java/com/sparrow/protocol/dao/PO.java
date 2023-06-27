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

import com.sparrow.protocol.MethodOrder;
import com.sparrow.protocol.POJO;
import com.sparrow.protocol.enums.StatusRecord;

import javax.persistence.Column;

public class PO implements POJO {
    private String createUserName;
    private Long createUserId;
    private Long modifiedUserId;
    private String modifiedUserName;
    private Long gmtCreate;
    private Long gmtModified;
    private StatusRecord status;

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    public void setModifiedUserId(Long modifiedUserId) {
        this.modifiedUserId = modifiedUserId;
    }

    public void setGmtCreate(Long gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public void setGmtModified(Long gmtModified) {
        this.gmtModified = gmtModified;
    }

    public void setStatus(StatusRecord status) {
        this.status = status;
    }

    @MethodOrder(order = 100)
    @Column(name = "status", columnDefinition = "tinyint(3) UNSIGNED DEFAULT 0 COMMENT 'STATUS'", nullable = false)
    public StatusRecord getStatus() {
        return status;
    }

    @MethodOrder(order = 101)
    @Column(name = "create_user_id", columnDefinition = "int(11) UNSIGNED  DEFAULT 0 COMMENT '创建人ID'", nullable = false, updatable = false)
    public Long getCreateUserId() {
        return createUserId;
    }

    @MethodOrder(order = 102)
    @Column(name = "gmt_create", columnDefinition = "bigint(11)  DEFAULT 0 COMMENT '创建时间'", nullable = false, updatable = false)
    public Long getGmtCreate() {
        return gmtCreate;
    }

    @MethodOrder(order = 103)
    @Column(name = "modified_user_id", columnDefinition = "int(11) unsigned  DEFAULT 0 COMMENT '更新人ID'", nullable = false)
    public Long getModifiedUserId() {
        return modifiedUserId;
    }

    @MethodOrder(order = 104)
    @Column(name = "gmt_modified", columnDefinition = "bigint(11)  DEFAULT 0 COMMENT '更新时间'", nullable = false)
    public Long getGmtModified() {
        return gmtModified;
    }

    @MethodOrder(order = 105)
    @Column(name = "create_user_name", columnDefinition = "varchar(64)  DEFAULT '' COMMENT '创建人'", nullable = false, updatable = false)
    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    @MethodOrder(order = 105)
    @Column(name = "modified_user_name", columnDefinition = "varchar(64)  DEFAULT '' COMMENT '更新人'", nullable = false)
    public String getModifiedUserName() {
        return modifiedUserName;
    }

    public void setModifiedUserName(String modifiedUserName) {
        this.modifiedUserName = modifiedUserName;
    }
}
