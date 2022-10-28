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
    private Long createUserId;
    private Long updateUserId;
    private Long createTime;
    private Long updateTime;
    private String remark;
    private StatusRecord status;


    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }


    public void setUpdateUserId(Long updateUserId) {
        this.updateUserId = updateUserId;
    }


    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }


    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }


    public void setRemark(String remark) {
        this.remark = remark;
    }
    public void setStatus(StatusRecord status) {
        this.status = status;
    }

    @MethodOrder(order = 100)
    @Column(name = "status",columnDefinition = "tinyint(1) DEFAULT 0 COMMENT 'STATUS'",nullable = false)
    public StatusRecord getStatus() {
        return status;
    }



    @MethodOrder(order = 101)
    @Column(name = "create_user_id", columnDefinition = "int(11) unsigned  DEFAULT 0 COMMENT 'create user id'", nullable = false, updatable = false)
    public Long getCreateUserId() {
        return createUserId;
    }


    @MethodOrder(order = 102)
    @Column(name = "create_time", columnDefinition = "bigint(11)  DEFAULT 0 COMMENT 'create time'", nullable = false, updatable = false)
    public Long getCreateTime() {
        return createTime;
    }

    @MethodOrder(order = 103)
    @Column(name = "update_user_id", columnDefinition = "int(11) unsigned  DEFAULT 0 COMMENT 'update user id'", nullable = false)
    public Long getUpdateUserId() {
        return updateUserId;
    }


    @MethodOrder(order = 104)
    @Column(name = "update_time", columnDefinition = "bigint(11)  DEFAULT 0 COMMENT 'update time'", nullable = false)
    public Long getUpdateTime() {
        return updateTime;
    }

    @MethodOrder(order = 105)
    @Column(name = "remark", columnDefinition = "varchar(512)  DEFAULT '' COMMENT 'remark'", nullable = false)
    public String getRemark() {
        if (remark == null) {
            remark = "";
        }
        return remark;
    }
}