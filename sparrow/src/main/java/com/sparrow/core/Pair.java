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

package com.sparrow.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Pair<F, S> {
    private final F first;
    private final S second;

    public S getSecond() {
        return second;
    }

    public F getFirst() {
        return first;
    }

    /**
     * Constructor for a Pair.
     *
     * @param first  the first object in the Pair
     * @param second the second object in the pair
     */
    public Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    /**
     * Checks the two objects for equality by delegating to their respective {@link Object#equals(Object)} methods.
     *
     * @param o the {@link Pair} to which this one is to be checked for equality
     * @return true if the underlying objects of the Pair are both considered equal
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Pair)) {
            return false;
        }
        Pair<?, ?> p = (Pair<?, ?>) o;
        return p.first.equals(this.first) && p.second.equals(this.second);
    }

    /**
     * Compute a hash code using the hash codes of the underlying objects
     *
     * @return a hashcode of the Pair
     */
    @Override
    public int hashCode() {
        return (this.first == null ? 0 : this.first.hashCode()) ^ (this.second == null ? 0 : this.second.hashCode());
    }

    /**
     * Convenience method for creating an appropriately typed pair.
     *
     * @param a the first object in the Pair
     * @param b the second object in the pair
     * @return a Pair that is templatized with the types of a and b
     */
    public static <A, B> Pair<A, B> create(A a, B b) {
        return new Pair<A, B>(a, b);
    }

    public static Pair<String, String> split(String pair, String splitChar) {
        String[] p = pair.split(splitChar);
        if (p.length > 1) {
            return new Pair<String, String>(p[0], p[1]);
        }
        return new Pair<String, String>(p[0], "");
    }

    public static <A, B> Pair convert(Map<A, B> map) {
        if (map == null) {
            return null;
        }
        Iterator<A> it = map.keySet().iterator();
        if (it.hasNext()) {
            A k = it.next();
            return create(k, map.get(k));
        }
        return null;
    }

    public Map<F, S> toMap() {
        Map<F, S> map = new HashMap<F, S>();
        map.put(this.first, this.second);
        return map;
    }
}