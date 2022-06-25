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

package com.sparrow.support;

public abstract class AbstractBitStatusSupport {

    /**
     * 莸取状态数组
     *
     * @return
     */
    public abstract long[] getStatusArray();

    public long add(int originalStatus, int status) {
        return originalStatus | status;
    }

    public long minus(long originalStatus, long minus) {
        return originalStatus & (~minus);
    }

    public long total() {
        long[] statusArray = this.getStatusArray();
        long status = 0;
        for (long s : statusArray) {
            status |= s;
        }
        return status;
    }

    public boolean isExist(int originalStatus, int status) {
        return (originalStatus & status) == status;
    }
}
