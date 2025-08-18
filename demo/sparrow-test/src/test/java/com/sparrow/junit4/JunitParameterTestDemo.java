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

package com.sparrow.junit4;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

/**
 * created by harry on 2016/10/20.
 */
//(1)步骤一：测试类指定特殊的运行器org.junit.runners.Parameterized
@RunWith(Parameterized.class)
public class JunitParameterTestDemo {
    // (2)步骤二：为测试类声明几个变量，分别用于存放期望值和测试所用数据。此处我只放了测试所有数据，没放期望值。
    private Integer id;
    private String userName;

    @BeforeClass
    public static void setUp() {
        System.out.println("init");
    }

    // (3)步骤三：为测试类声明一个带有参数的公共构造函数，并在其中为第二个环节中声明的几个变量赋值。
    public JunitParameterTestDemo(Integer id, String userName) {
        this.id = id;
        this.userName = userName;
    }

    // (4)步骤四：为测试类声明一个使用注解 org.junit.runners.Parameterized.Parameters 修饰的，返回值为
    // java.util.Collection 的公共静态方法，并在此方法中初始化所有需要测试的参数对。
    @Parameterized.Parameters
    public static Collection initParameters() {
        return Arrays.asList(new Object[][] {
            {1, "jacky"}, {2, "andy"},
            {3, "tomcat"},});
    }

    // (5)步骤五：编写测试方法，使用定义的变量作为参数进行测试。
    @Test
    public void testFindByName() {
        System.out.println("-------------");
        System.out.println(id);
        System.out.println(userName);
    }
}
