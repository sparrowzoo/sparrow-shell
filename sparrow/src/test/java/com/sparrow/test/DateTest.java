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

package com.sparrow.test;

import com.sparrow.enums.DateTimeUnit;
import com.sparrow.utility.DateTimeUtility;

import java.util.Calendar;
import java.util.Random;

public class DateTest {
    public static void main(String[] args) {
        //Right BICEP
        for(int i=0;i<100;i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis()+new Random().nextInt());
            int dayOfWeek=calendar.get(Calendar.DAY_OF_WEEK);
            //dayOfWeek=(dayOfWeek+5)%7;
            //calendar.add(Calendar.DATE,-dayOfWeek);
            //1 2 3 4 5 6 7
            //2 3 4 5 6 7 1

            int week=dayOfWeek-1;

            /**
             * 打印周几
             * 不允许用if else 和map
             */
            System.out.println(DateTimeUtility.getFormatTime(calendar.getTime(), "yyyy-MM-dd")+"周"+week);
        }
        /**
         * 需求 给定一个日期
         * 求出来离这个日期最近的过去的周一
         * 要求
         * 空间复杂度O 1
         * 时间复杂度O 1
         * 不允许 用if else
         */
    }
}
