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

package com.sparrow.constant.cache.key;

import com.sparrow.cache.Key;
import com.sparrow.constant.SparrowModule;

public class KeyResource {

    /**
     * 所有版块
     */
    public static final Key.Business ALL = new Key.Business(SparrowModule.FORUM, "ALL");
    /**
     * url与编码对应关系
     */
    public static final Key.Business ACCESS_URL_CODE_PAIR = new Key.Business(SparrowModule.FORUM, "ACCESS", "URL", "CODE", "PAIR");
    /**
     * id与编码对应关系
     */
    public static final Key.Business ID_CODE_PAIR = new Key.Business(SparrowModule.FORUM, "ID", "CODE", "PAIR");
}