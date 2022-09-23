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

package com.sparrow.facade.beans;

import com.sparrow.container.ContainerBuilder;
import com.sparrow.core.spi.ApplicationContext;
import com.sparrow.utility.BeanUtility;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by harry on 16/9/7.
 */
public class Main {
    public static void main(String[] args) {
        String[] array = "12:00".split("\\:");
        Integer seconds = Integer.valueOf(array[1]);
        ApplicationContext.getContainer().init(new ContainerBuilder());
        Source source = new Source();
        source.setAge(100);
        source.setBirthday(new Date());
        source.setMoney(new BigDecimal(100));
        source.setName("zhangsan");
        source.setSb(true);
        source.setUserId(1L);

        Target target = new Target();
        BeanUtility.copyProperties(source, target);
        System.out.println(target.getAge());
    }
}
