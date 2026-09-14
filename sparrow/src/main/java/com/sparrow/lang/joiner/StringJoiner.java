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
import com.sparrow.utility.CollectionsUtility;
import lombok.Data;
import org.apache.commons.lang3.ArrayUtils;

@Data
public class StringJoiner {
    private Object[] array;
    private Iterable<?> iterator;
    private String separator;
    private Object[] exceptArray;

    private void setSeparator(String separator) {
        this.separator = separator == null ? Symbol.COMMA : separator;
    }

    public StringJoiner(Object[] array, String separator) {
        this.array = array;
        this.setSeparator(separator);
    }

    public StringJoiner(String separator, Iterable<?> array) {
        this.iterator = array;
        this.setSeparator(separator);
    }

    public StringJoiner(String separator, Object... array) {
        this.array = array;
        this.setSeparator(separator);
    }

    /**
     * 从数组array中排除exceptArray并拼接成数组 用于标签删除时的帖子标签更新
     */
    public String join() {
        StringBuilder sb = new StringBuilder();
        if (!CollectionsUtility.isNullOrEmpty(this.array)) {
            for (Object object : this.array) {
                if (ArrayUtils.contains(exceptArray, object)) {
                    continue;
                }
                if (sb.length() > 0) {
                    sb.append(separator);
                }
                sb.append(object);
            }
            return sb.toString();
        }

        if (!CollectionsUtility.isNullOrEmpty(this.iterator)) {
            for (Object object : this.iterator) {
                if (ArrayUtils.contains(exceptArray, object)) {
                    continue;
                }
                if (sb.length() > 0) {
                    sb.append(separator);
                }
                sb.append(object);
            }
        }
        return Symbol.EMPTY;
    }
}
