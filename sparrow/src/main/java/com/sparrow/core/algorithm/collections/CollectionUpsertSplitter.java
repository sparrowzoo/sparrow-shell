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
package com.sparrow.core.algorithm.collections;

import com.sparrow.utility.CollectionsUtility;

import java.util.*;
public class CollectionUpsertSplitter<K, V extends IdentityGetter<K>> {
    private Set<K> localKeys = new HashSet<>();
    private Set<K> remoteKeys = new HashSet<>();

    private Map<K, V> allMap = new HashMap<>();

    private List<V> insertSet;
    private List<V> updateSet;
    private List<V> deleteSet;

    public CollectionUpsertSplitter(Collection<V> locals, Collection<V> remotes) {
        for (V v : locals) {
            this.localKeys.add(v.getIdentity());
            this.allMap.put(v.getIdentity(), v);
        }

        for (V v : remotes) {
            this.remoteKeys.add(v.getIdentity());
            this.allMap.put(v.getIdentity(), v);
        }
    }

    private List<V> getValueByKeySet(Set<K> keys) {
        List<V> set = new LinkedList<>();
        for (K k : keys) {
            set.add(this.allMap.get(k));
        }
        return set;
    }

    public void split() {
        if (CollectionsUtility.isNullOrEmpty(remoteKeys)) {
            throw new IllegalArgumentException("remote can't be empty");
        }
        Set<K> deletingSet = new HashSet<>(localKeys);
        Set<K> insertingSet = new HashSet<>(remoteKeys);
        Set<K> updatingSet = new HashSet<>(localKeys);

        deletingSet.removeAll(remoteKeys);
        this.deleteSet = this.getValueByKeySet(deletingSet);
        insertingSet.removeAll(localKeys);
        this.insertSet = this.getValueByKeySet(insertingSet);
        updatingSet.retainAll(remoteKeys);
        this.updateSet = this.getValueByKeySet(updatingSet);
    }

    public List<V> getInsertSet() {
        return insertSet;
    }

    public List<V> getUpdateSet() {
        return updateSet;
    }

    public List<V> getDeleteSet() {
        return deleteSet;
    }
}
