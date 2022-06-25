package com.sparrow.core.algorithm.collections;/*
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

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PairCollection<E extends KeyPOJO, A extends E> {
    private Collection<E> expectedCollection;
    private Collection<A> actualCollection;

    /**
     * exist expectedCollection and actualCollection
     */
    private Set<A> intersection;
    /**
     * exist expectedCollection and not exist actualCollection
     */
    private Set<E> differenceExpected;

    public void compare() {
        //for performance
        Map<String, A> actualMap = new HashMap<>(this.expectedCollection.size());
        for (A a : this.actualCollection) {
            actualMap.put(a.getKey(), a);
        }

        for (E e : this.expectedCollection) {
            String expectedKey = e.getKey();
            if (actualMap.containsKey(expectedKey)) {
                this.intersection.add(actualMap.get(expectedKey));
                continue;
            }
            this.differenceExpected.add(e);
        }
    }

    public Set<A> getIntersection() {
        return intersection;
    }

    public Set<E> getDifferenceExpected() {
        return differenceExpected;
    }

    public PairCollection(Collection<E> expected, Collection<A> actual) {
        this.expectedCollection = expected;
        this.actualCollection = actual;

        this.intersection = new HashSet<>(expected.size());
        this.differenceExpected = new HashSet<>(expected.size());
    }
}
