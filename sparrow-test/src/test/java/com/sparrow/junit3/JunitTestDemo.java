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

package com.sparrow.junit3;

import junit.framework.TestCase;

/**
 * junit4 1. @Test : 测试方法，测试程序会运行的方法，后边可以跟参数代表不同的测试，如(expected=XXException.class) 异常测试，(timeout=xxx)超时测试 2. @Ignore :
 * 被忽略的测试方法 3. @Before: 每一个测试方法之前运行 4. @After : 每一个测试方法之后运行 5. @BeforeClass: 所有测试开始之前运行 junit3 无此方法 6. @AfterClass:
 * 所有测试结束之后运行 junit3 无此方法
 */
public class JunitTestDemo extends TestCase {
    public void setUp() {
        System.out.println("set up");
    }

    public void test() {
        System.out.println("test");
    }

    public void test2() {
        System.out.println("test2");
    }

    @Override
    protected void tearDown() throws Exception {
        System.out.println("end");
    }
}
