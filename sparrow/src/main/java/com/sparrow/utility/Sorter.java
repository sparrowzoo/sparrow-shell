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

package com.sparrow.utility;

import com.sparrow.protocol.constant.magic.Symbol;
import com.sparrow.core.Pair;
import com.sparrow.enums.Order;

public class Sorter {
    private Order direction;
    private String sortKey;
    private String queryString;

    /**
     * 排序字符，以_分隔
     *
     * @param sort field_direction
     */
    public Sorter(String sort, String queryString) {
        if (!StringUtility.isNullOrEmpty(sort)) {
            Pair<String, String> sortPair = Pair.split(sort, Symbol.UNDERLINE);
            sortKey = sortPair.getFirst();
            direction = sortPair.getSecond().equalsIgnoreCase(Order.ASC.toString()) ?
                Order.DESC : Order.ASC;
        }
        this.queryString = queryString;
    }

    @Override
    public String toString() {
        String newSort = String.format("sort=%1$s_%2$s", this.sortKey, this.direction);
        return QueryStringParser.replace(this.queryString, newSort);
    }
}
