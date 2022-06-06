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

package com.sparrow.jdk.io;

import com.sparrow.cryptogram.MessageSignature;
import com.sparrow.utility.FileUtility;

/**
 * Created by harry on 17/5/13.
 */
public class FileTester {
    public static void main(String[] args) {
        MessageSignature messageSignature=new MessageSignature();
        System.out.println(messageSignature.md5("12345"));

        long recordCount = 200000000;
        long recordSize = recordCount * 4;

        System.out.println("数据总量" + FileUtility.getInstance().getHumanReadableFileLength(recordSize));

        int second = 60 * 60 * 4;

        System.out.println("tps" + recordCount / second);

        System.out.println("每秒数据量" + FileUtility.getInstance().getHumanReadableFileLength(recordSize / second));
    }
}
