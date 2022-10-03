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

package com.sparrow.protocol.enums;

import java.util.Objects;

/**
 * 记录状态
 *
 * @version 1.0
 */
public enum StatusRecord {
    /**
     * 被屏蔽(0)
     */
    DISABLE(0),
    /**
     * 可用(1)
     */
    ENABLE(1),
    /**
     * 草搞(2)
     */
    DRAFT(2),
    /**
     * 已发布(3)
     */
    PUBLISHED(3),
    /**
     * 排队中(4)...
     */
    QUEUE(4),
    /**
     * 队列发布出错(5)
     */
    ERROR(5),
    /**
     * 销毁(6)
     */
    DESTROYED(6);

    StatusRecord(int status) {
        this.status = status;
    }

    public final int status;

    /**
     * 比较状态值是否一致
     *
     * @param status
     * @return
     */
    public boolean equalsStatus(Integer status) {
        return Objects.equals(this.status, status);
    }

    /**
     * 通过status匹配枚举
     *
     * @param status
     * @return
     */
    public static StatusRecord valueOfStatus(Integer status) {
        if (status == null) {
            return null;
        }
        final StatusRecord[] values = StatusRecord.values();
        for (StatusRecord value : values) {
            if (value.equalsStatus(status)) {
                return value;
            }
        }
        return null;
    }

    /**
     * 判断状态是否合法
     *
     * @param status
     * @return
     */
    public static boolean isLegal(Integer status) {
        final StatusRecord statusRecord = valueOfStatus(status);
        return statusRecord != null;
    }
}
