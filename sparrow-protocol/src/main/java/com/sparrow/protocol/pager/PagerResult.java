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

package com.sparrow.protocol.pager;

import com.sparrow.protocol.DTO;
import com.sparrow.protocol.KeyValue;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * pager result without html
 * <p>
 * T data type
 */
@Data
public class PagerResult<T> extends SimplePager implements DTO {

    public PagerResult() {
    }

    public PagerResult(SimplePager simplePager) {
        if (simplePager == null) {
            pageNo = 1;
            return;
        }
        this.pageNo = simplePager.getPageNo();
        this.pageSize = simplePager.getPageSize();
    }

    public PagerResult(Integer pageSize, Integer currentPageIndex) {
        super(pageSize, currentPageIndex);
    }

    protected Long recordTotal;

    protected List<T> list;
    /**
     * 字典
     */
    private Map<String, Object> dictionary = new HashMap<>();

    /**
     * 根据总记录数和每页记录数获取最后页码
     */
    public Integer getLastPageIndex() {
        return (int) Math.ceil((double) this.recordTotal / this.pageSize);
    }

    public <I> void putDictionary(String key, List<KeyValue<I, String>> values) {
        dictionary.put(key, values);
    }
}
