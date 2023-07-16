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

public class KeyCollectionUpsertSplitter<V> {

    private Collection<V> localKeys;
    private Collection<V> remoteKeys;
    private Set<V> all = new HashSet<>();

    private List<V> insertSet;
    private List<V> updateSet;
    private List<V> deleteSet;

    public KeyCollectionUpsertSplitter(Collection<V> locals, Collection<V> remotes) {
        this.localKeys = locals;
        this.remoteKeys = remotes;
        this.all.addAll(locals);
        this.all.addAll(remotes);
    }

    public void split() {
        if (CollectionsUtility.isNullOrEmpty(remoteKeys)) {
            throw new IllegalArgumentException("remote can't be empty");
        }
        Set<V> deletingSet = new HashSet<>(localKeys);
        Set<V> insertingSet = new HashSet<>(remoteKeys);
        Set<V> updatingSet = new HashSet<>(localKeys);

        deletingSet.removeAll(remoteKeys);
        this.deleteSet = new ArrayList<>(deletingSet);
        insertingSet.removeAll(localKeys);
        this.insertSet = new ArrayList<>(insertingSet);
        updatingSet.retainAll(remoteKeys);
        this.updateSet = new ArrayList<>(updatingSet);
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
