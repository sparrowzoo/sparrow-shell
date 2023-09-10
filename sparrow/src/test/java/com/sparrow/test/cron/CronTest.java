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

package com.sparrow.test.cron;

import com.sparrow.constant.DateTime;
import com.sparrow.enums.DateTimeUnit;
import com.sparrow.support.cron.CronExpression;
import com.sparrow.utility.DateTimeUtility;

import java.text.ParseException;
import java.util.Date;

public class CronTest {
    public static void main(String[] args) throws ParseException {
        Date nextValidTime = new Date();
        for (int i = 0; i < 10; i++) {
            nextValidTime = new CronExpression("0 0 2 1 * ? ").getNextValidTimeAfter(nextValidTime);

//            nextValidTime = new CronExpression("0 0 10/1 * * ?").getNextValidTimeAfter(nextValidTime);
            System.out.println(DateTimeUtility.getFormatTime(nextValidTime, DateTime.FORMAT_YYYY_MM_DD_HH_MM_SS));
        }
    }
}
