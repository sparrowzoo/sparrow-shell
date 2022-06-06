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

/**
 * simple pager search parameter
 *
 * @author harry
 */
public class PagerQuery extends SimplePager {
    public PagerQuery() {
    }

    /**
     * avoid deep pager
     */
    public PagerQuery(Integer pageSize) {
        super(pageSize, 0);
    }


    public PagerQuery(Integer pageSize, Integer currentPageIndex) {
        super(pageSize, currentPageIndex);
    }


    public String getLimitClause() {
        //no page
        if (pageSize <= 0) {
            return "";
        }
        if (this.currentPageIndex == null) {
            this.currentPageIndex = 1;
        }
        int offset = pageSize * (this.currentPageIndex <= 1 ? 0 : this.currentPageIndex-1);
        return " limit " + offset + "," + this.getPageSize();
    }
}
