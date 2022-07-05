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

package com.sparrow.core.cache;

public class ExpirableData<V> {
    private long t;
    private int seconds;
    private V data;

    public ExpirableData(int seconds, V data) {
        this.seconds = seconds;
        this.data = data;
        this.t = System.currentTimeMillis();
    }

    public int getSeconds() {
        return seconds;
    }

    public V getData() {
        return data;
    }

    public void setTimestamp(long t) {
        this.t = t;
    }

    public boolean isExpire() {
        return (System.currentTimeMillis() - t) / 1000 > seconds;
    }
}
