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

package com.sparrow.lang.joiner;

import com.sparrow.protocol.constant.magic.Symbol;
import lombok.Data;

import java.util.Collection;
import java.util.List;

@Data
public class ListJoiner {

    private List<List<Object>> collections;
    private String outerSeparator = Symbol.COMMA;
    private String innerSeparator = Symbol.COLON;

    public ListJoiner(List<List<Object>> collections, String outerSeparator, String innerSeparator) {
        this.collections = collections;
        if (outerSeparator != null) {
            this.outerSeparator = outerSeparator;
        }
        if (innerSeparator != null) {
            this.innerSeparator = innerSeparator;
        }
    }

    public String join() {
        if (this.collections == null || this.collections.isEmpty()) {
            return Symbol.EMPTY;
        }
        StringBuilder sb = new StringBuilder();
        for (Collection<?> collection : this.collections) {
            if (sb.length() > 0) {
                sb.append(this.outerSeparator);
            }
            StringJoiner stringJoiner = new StringJoiner(this.innerSeparator, collection);
            sb.append(stringJoiner.join());
        }
        return sb.toString();
    }
}
