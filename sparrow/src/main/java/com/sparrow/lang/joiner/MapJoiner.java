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

import java.util.Map;

public class MapJoiner {
    private Map<?, ?> map;

    private String outerSeparator = Symbol.COMMA;
    private String innerSeparator = Symbol.COLON;

    public MapJoiner(Map<?, ?> map, String outerSeparator, String innerSeparator) {
        this.map = map;
        if (outerSeparator != null) {
            this.outerSeparator = outerSeparator;
        }
        if (innerSeparator != null) {
            this.innerSeparator = innerSeparator;
        }
    }

    public String join(boolean withoutKey) {
        if (this.map == null || this.map.isEmpty()) {
            return Symbol.EMPTY;
        }
        StringBuilder sb = new StringBuilder();
        for (Object key : this.map.keySet()) {
            if (sb.length() > 0) {
                sb.append(outerSeparator);
            }
            if (withoutKey) {
                sb.append(this.map.get(key));
            } else {
                StringJoiner stringJoiner = new StringJoiner(this.innerSeparator, key, map.get(key));
                sb.append(stringJoiner.join());
            }
        }
        return sb.toString();
    }
}
