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

package com.sparrow.core.algorithm;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by harry on 2018/4/13.
 */
public class Sharding<T> {
    private List<T> bucketList;
    private Integer shardingCount;

    public Sharding(List<T> bucketList, Integer shardingCount) {
        this.bucketList = bucketList;
        this.shardingCount = shardingCount;
    }

    public Map<T, List<Integer>> shardingAliquot() {
        Map<T, List<Integer>> result = new LinkedHashMap<T, List<Integer>>(this.shardingCount, 1);
        int shardingCountPerBucket = shardingCount / bucketList.size();
        int shardingIndex = 0;
        int bucketIndex = 0;
        List<Integer> shardingItems = null;
        T currentBucket;
        while (shardingIndex < this.bucketList.size() * shardingCountPerBucket) {
            if (shardingIndex % shardingCountPerBucket == 0) {
                currentBucket = this.bucketList.get(bucketIndex++);
                shardingItems = new ArrayList<Integer>(shardingCountPerBucket + 1);
                result.put(currentBucket, shardingItems);
            }
            shardingItems.add(shardingIndex);
            shardingIndex++;
        }
        while (shardingIndex < shardingCount) {
            result.get(this.bucketList.get(shardingIndex % this.bucketList.size())).add(shardingIndex);
            shardingIndex++;
        }
        return result;
    }
}
