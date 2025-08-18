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

package com.sparrow.facade.date;

import com.sparrow.constant.DateTime;
import com.sparrow.enums.DateTimeUnit;
import com.sparrow.utility.DateTimeUtility;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.junit.Test;

import java.util.Calendar;

/**
 * Created by harry on 2018/1/23.
 */
public class DateTimeUtilityTest {


    @Test
    public void floor() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis() + DateTime.MILLISECOND_UNIT.get(DateTimeUnit.DAY) * 80);
        Long time = DateTimeUtility.floor(calendar, DateTimeUnit.SECOND);
        System.out.println("SECOND" + DateTimeUtility.getFormatTime(time, DateTime.FORMAT_YYYYMMDDHHMMSS));
        time = DateTimeUtility.floor(calendar, DateTimeUnit.MINUTE);
        System.out.println("MINUTE" + DateTimeUtility.getFormatTime(time, DateTime.FORMAT_YYYYMMDDHHMMSS));
        time = DateTimeUtility.floor(calendar, DateTimeUnit.HOUR);
        System.out.println("HOUR  " + DateTimeUtility.getFormatTime(time, DateTime.FORMAT_YYYYMMDDHHMMSS));
        time = DateTimeUtility.floor(calendar, DateTimeUnit.DAY);
        System.out.println("DAY   " + DateTimeUtility.getFormatTime(time, DateTime.FORMAT_YYYYMMDDHHMMSS));
        time = DateTimeUtility.floor(calendar, DateTimeUnit.WEEK);
        System.out.println("MONTH " + DateTimeUtility.getFormatTime(time, DateTime.FORMAT_YYYYMMDDHHMMSS));
        System.out.println("YEAR  " + DateTimeUtility.getFormatTime(time, DateTime.FORMAT_YYYYMMDDHHMMSS));
    }

    @Test
    public void ceiling() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis() + DateTime.MILLISECOND_UNIT.get(DateTimeUnit.DAY) * 80);
        Long time = DateTimeUtility.ceiling(calendar, DateTimeUnit.SECOND);
        System.out.println("SECOND" + DateTimeUtility.getFormatTime(time, DateTime.FORMAT_YYYYMMDDHHMMSS));
        time = DateTimeUtility.ceiling(calendar, DateTimeUnit.MINUTE);
        System.out.println("MINUTE" + DateTimeUtility.getFormatTime(time, DateTime.FORMAT_YYYYMMDDHHMMSS));
        time = DateTimeUtility.ceiling(calendar, DateTimeUnit.HOUR);
        System.out.println("HOUR  " + DateTimeUtility.getFormatTime(time, DateTime.FORMAT_YYYYMMDDHHMMSS));
        time = DateTimeUtility.ceiling(calendar, DateTimeUnit.DAY);
        System.out.println("DAY   " + DateTimeUtility.getFormatTime(time, DateTime.FORMAT_YYYYMMDDHHMMSS));

        time = DateTimeUtility.ceiling(calendar, DateTimeUnit.MONTH);
        System.out.println("MONTH " + DateTimeUtility.getFormatTime(time, DateTime.FORMAT_YYYYMMDDHHMMSS));
        time = DateTimeUtility.ceiling(calendar, DateTimeUnit.YEAR);
        System.out.println("YEAR  " + DateTimeUtility.getFormatTime(time, DateTime.FORMAT_YYYYMMDDHHMMSS));
    }

    @Test
    public void interval() {
        Calendar calendar = Calendar.getInstance();
        Long time = calendar.getTimeInMillis();
        calendar.setTimeInMillis(time + DateTime.MILLISECOND_UNIT.get(DateTimeUnit.DAY) * 2);
        System.out.println("Interval  " + DateTimeUtility.getInterval(time, calendar.getTimeInMillis(), DateTimeUnit.HOUR));
    }


    @Test
    public void expireRounding() {
        Long t = 1555298987703L;
        System.out.println(DateTimeUtility.roundingExpire(t, 1000 * 60L * 60L));
        t += 1000 * 60L * 30L;
        System.out.println(DateTimeUtility.roundingExpire(t, 1000 * 60L * 60L));
    }

    @Test
    public void beforeFormat() {
//        System.out.println(DateTimeUtility.getBeforeFormatTimeBySecond(61L,3,0));
//        System.out.println(DateTimeUtility.getBeforeFormatTimeBySecond(3600 * 24 * 3670*100L + 3600 * 23 + 60 * 30 + 59L));
//        System.out.println(DateTimeUtility.getBeforeFormatTimeBySecond(3600 +120+ 59L,3,0));
    }

    @Test
    public void testSparrowFormat() {
        long t = System.currentTimeMillis();
        for (int i = 0; i < 5; i++) {
            DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMdd");
        }
        System.out.println(System.currentTimeMillis() - t);
    }

    @Test
    public void testCommonFormat() {
        long t = System.currentTimeMillis();
        for (int i = 0; i < 5; i++) {
            DateTimeUtility.getFormatTime(System.currentTimeMillis(), "yyyyMMdd");
        }
        System.out.println(System.currentTimeMillis() - t);
    }
}
