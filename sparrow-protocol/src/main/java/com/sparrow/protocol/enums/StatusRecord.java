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

/**
 * 记录状态
 *
 * @version 1.0
 */
public enum StatusRecord {
    /**
     * 被屏蔽(0)
     */
    DISABLE,
    /**
     * 可用(1)
     */
    ENABLE,
    /**
     * 草搞(2)
     */
    DRAFT,
    /**
     * 已发布(3)
     */
    PUBLISHED,
    /**
     * 排队中(4)...
     */
    QUEUE,
    /**
     * 队列发布出错(5)
     */
    ERROR,
    /**
     * 销毁(6)
     */
    DESTROYED
}
