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

package com.sparrow.constant;

import com.sparrow.enums.DateTimeUnit;
import com.sparrow.protocol.constant.magic.Digit;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DateTime {

    /**
     * second
     */
    private static final long SECOND = 1000;
    /**
     * minute
     */
    private static final long MINUTE = SECOND * 60;
    /**
     * hour
     */
    private static final long HOUR = MINUTE * 60;
    /**
     * day
     */
    private static final long DAY = HOUR * 24;
    /**
     * week
     */
    private static final long WEEK = DAY * 7;
    /**
     * month
     */
    private static final long MONTH = DAY * 30;

    public static final String FORMAT_YYYY_MM_DD_HH_MM_SS_MS = "yyyy-MM-dd HH:mm:ss SSS";

    public static final String FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    public static final String FORMAT_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";

    public static final String FORMAT_YYYY_MM_DD = "yyyy-MM-dd";

    public static final String FORMAT_MM_DD = "MM-dd";

    public static final String FORMAT_YYYYMMDD = "yyyyMMdd";

    public static final String FORMAT_YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    private static final ThreadLocal<Map<String, DateFormat>> DATE_FORMAT_CONTAINER = new ThreadLocal<>();

    public static DateFormat getInstance(final String format) {
        Map<String, DateFormat> dateFormatMap = DATE_FORMAT_CONTAINER.get();
        if (dateFormatMap == null) {
            dateFormatMap = new ConcurrentHashMap<>();
            DATE_FORMAT_CONTAINER.set(dateFormatMap);
        }
        if (dateFormatMap.containsKey(format)) {
            return dateFormatMap.get(format);
        }
        dateFormatMap.put(format, new SimpleDateFormat(format));
        return dateFormatMap.get(format);
    }

    /**
     * min unix timestamp
     */
    public static final Timestamp MIN_UNIX_TIMESTAMP = Timestamp
        .valueOf("1970-01-01 08:00:00");

    public static final Timestamp MAX_UNIX_TIMESTAMP = Timestamp.valueOf("9999-12-31 23:59:59");

    /**
     * time unit from Millis to year
     */
    @SuppressWarnings("serial")
    public static final Map<String, Integer> BEFORE_FORMAT = new LinkedHashMap<String, Integer>();

    public static final Map<DateTimeUnit, Integer> DEFAULT_FIRST_VALUE = new HashMap<DateTimeUnit, Integer>(6);
    public static final Map<DateTimeUnit, Integer> DATE_TIME_UNIT_CALENDER_CONVERTER = new HashMap<DateTimeUnit, Integer>(7);
    public static final Map<DateTimeUnit, Long> MILLISECOND_UNIT = new HashMap<DateTimeUnit, Long>(6);

    static {
        BEFORE_FORMAT.put("s", 60);
        BEFORE_FORMAT.put("m", 60);
        BEFORE_FORMAT.put("h", 24);
        BEFORE_FORMAT.put("d", 30);
        BEFORE_FORMAT.put("M", 12);
        BEFORE_FORMAT.put("y", 0);

        //0表示1月
        DEFAULT_FIRST_VALUE.put(DateTimeUnit.MONTH, Digit.ZERO);
        DEFAULT_FIRST_VALUE.put(DateTimeUnit.DAY, Digit.ONE);
        DEFAULT_FIRST_VALUE.put(DateTimeUnit.HOUR, Digit.ZERO);
        DEFAULT_FIRST_VALUE.put(DateTimeUnit.MINUTE, Digit.ZERO);
        DEFAULT_FIRST_VALUE.put(DateTimeUnit.SECOND, Digit.ZERO);
        DEFAULT_FIRST_VALUE.put(DateTimeUnit.MILLISECOND, Digit.ZERO);

        DATE_TIME_UNIT_CALENDER_CONVERTER.put(DateTimeUnit.YEAR, Calendar.YEAR);
        DATE_TIME_UNIT_CALENDER_CONVERTER.put(DateTimeUnit.MONTH, Calendar.MONTH);
        DATE_TIME_UNIT_CALENDER_CONVERTER.put(DateTimeUnit.DAY, Calendar.DATE);
        DATE_TIME_UNIT_CALENDER_CONVERTER.put(DateTimeUnit.HOUR, Calendar.HOUR_OF_DAY);
        DATE_TIME_UNIT_CALENDER_CONVERTER.put(DateTimeUnit.MINUTE, Calendar.MINUTE);
        DATE_TIME_UNIT_CALENDER_CONVERTER.put(DateTimeUnit.SECOND, Calendar.SECOND);
        DATE_TIME_UNIT_CALENDER_CONVERTER.put(DateTimeUnit.MILLISECOND, Calendar.MILLISECOND);

        /**
         * second
         */
        MILLISECOND_UNIT.put(DateTimeUnit.SECOND, SECOND);
        /**
         * minute
         */
        MILLISECOND_UNIT.put(DateTimeUnit.MINUTE, SECOND * 60);
        /**
         * hour
         */
        MILLISECOND_UNIT.put(DateTimeUnit.HOUR, MINUTE * 60);
        /**
         * day
         */
        MILLISECOND_UNIT.put(DateTimeUnit.DAY, HOUR * 24);
        /**
         * week
         */
        MILLISECOND_UNIT.put(DateTimeUnit.WEEK, DAY * 7);
        /**
         * month
         */
        MILLISECOND_UNIT.put(DateTimeUnit.MONTH, DAY * 30);
    }
}
