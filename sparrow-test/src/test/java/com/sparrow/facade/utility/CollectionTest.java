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
package com.sparrow.facade.utility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author by harry
 */
public class CollectionTest {
    public static void main(String[] args) {
        List<CompareEntity> list=new ArrayList<CompareEntity>();
        list.add(new CompareEntity(1,"zhangsan"));
        list.add(new CompareEntity(5,"lisi"));
        list.add(new CompareEntity(3,"wangwu"));
        Collections.sort(list);
        for(CompareEntity i:list){
            System.out.println(i.getOrder());
        }
    }
}
