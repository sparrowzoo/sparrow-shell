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

package com.sparrow.utility;

import com.sparrow.constant.DateTime;
import com.sparrow.protocol.constant.magic.Digit;
import com.sparrow.core.Pair;
import com.sparrow.enums.DateTimeUnit;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateTimeUtility {

    private static Logger logger = LoggerFactory.getLogger(DateTimeUtility.class);

    public static Pair<Long, Long> getTimeSegment(DateTimeUnit condition, Long timeMillis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeMillis);
        Pair<Long, Long> pair = null;
        Long start = 0L;
        Long end = 0L;
        switch (condition) {
            case YEAR:
                calendar.set(Calendar.MILLISECOND, Digit.ZERO);
                calendar.set(Calendar.SECOND, Digit.ZERO);
                calendar.set(Calendar.MINUTE, Digit.ZERO);
                calendar.set(Calendar.HOUR_OF_DAY, Digit.ZERO);
                calendar.set(Calendar.DAY_OF_MONTH, Digit.ONE);
                calendar.set(Calendar.MONTH, Calendar.JANUARY);
                start = calendar.getTimeInMillis();
                calendar.add(Calendar.YEAR, Digit.ONE);
                end = calendar.getTimeInMillis();
                pair = Pair.create(start, end);
                break;
            case MONTH:
                calendar.set(Calendar.MILLISECOND, Digit.ZERO);
                calendar.set(Calendar.SECOND, Digit.ZERO);
                calendar.set(Calendar.MINUTE, Digit.ZERO);
                calendar.set(Calendar.HOUR_OF_DAY, Digit.ZERO);
                calendar.set(Calendar.DAY_OF_MONTH, Digit.ONE);
                start = calendar.getTimeInMillis();
                calendar.add(Calendar.MONTH, Digit.ONE);
                end = calendar.getTimeInMillis();
                pair = Pair.create(start, end);
                break;
            case WEEK:
                calendar.set(Calendar.MILLISECOND, Digit.ZERO);
                calendar.set(Calendar.SECOND, Digit.ZERO);
                calendar.set(Calendar.MINUTE, Digit.ZERO);
                calendar.set(Calendar.HOUR_OF_DAY, Digit.ZERO);
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                start = calendar.getTimeInMillis();
                calendar.add(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
                end = calendar.getTimeInMillis();
                pair = Pair.create(start, end);
                break;
            case DAY:
                calendar.set(Calendar.MILLISECOND, Digit.ZERO);
                calendar.set(Calendar.SECOND, Digit.ZERO);
                calendar.set(Calendar.MINUTE, Digit.ZERO);
                calendar.set(Calendar.HOUR_OF_DAY, Digit.ZERO);
                start = calendar.getTimeInMillis();
                calendar.add(Calendar.DAY_OF_MONTH, Digit.ONE);
                end = calendar.getTimeInMillis();
                pair = Pair.create(start, end);
                break;
            case HOUR:
                calendar.set(Calendar.MILLISECOND, Digit.ZERO);
                calendar.set(Calendar.SECOND, Digit.ZERO);
                calendar.set(Calendar.MINUTE, Digit.ZERO);
                start = calendar.getTimeInMillis();
                calendar.add(Calendar.HOUR_OF_DAY, Digit.ONE);
                end = calendar.getTimeInMillis();
                pair = Pair.create(start, end);
                break;
            case MINUTE:
                calendar.set(Calendar.MILLISECOND, Digit.ZERO);
                calendar.set(Calendar.SECOND, Digit.ZERO);
                start = calendar.getTimeInMillis();
                calendar.add(Calendar.MINUTE, Digit.ONE);
                end = calendar.getTimeInMillis();
                pair = Pair.create(start, end);
                break;
            case SECOND:
                calendar.set(Calendar.MILLISECOND, Digit.ZERO);
                start = calendar.getTimeInMillis();
                calendar.add(Calendar.SECOND, Digit.ONE);
                end = calendar.getTimeInMillis();
                pair = Pair.create(start, end);
                break;
            case MILLISECOND:
                throw new UnsupportedOperationException("MILLISECOND Unsupported  Operation!");
        }
        return pair;
    }

    /**
     * 获取时间间隔
     *
     * @param startTime    开始时间
     * @param endTime      结束时间
     * @param dateTimeUnit 以毫秒为时间单位
     * @return
     */
    public static int getInterval(Long startTime, Long endTime,
        DateTimeUnit dateTimeUnit) {
        if (startTime == null || endTime == null) {
            return Integer.MIN_VALUE;
        }
        return (int) ((endTime - startTime) / DateTime.MILLISECOND_UNIT.get(dateTimeUnit));
    }

    /**
     * 获取HH:mm:ss格式的时间
     *
     * @param seconds 时间 单位(秒)
     * @return
     */
    public static String getHHmmss(int seconds) {
        int mm = seconds / 60;
        int ss = seconds % 60;
        int hh = Digit.ZERO;
        if (mm / 60 >= Digit.ONE) {
            hh = mm / 60;
            mm %= 60;
        }
        return String.format("%1$s:%2$s:%3$s",
            StringUtility.leftPad(String.valueOf(hh), '0', Digit.TOW),
            StringUtility.leftPad(String.valueOf(mm), '0', Digit.TOW),
            StringUtility.leftPad(String.valueOf(ss), '0', Digit.TOW));
    }

    /**
     * 为当前时间加上指定单位的长度
     *
     * @param calendar Calendar.Date
     * @param amount   时间长度
     * @return
     */
    public static long addTime(int calendar, int amount) {
        return addTime(System.currentTimeMillis(), calendar, amount);
    }

    public static long addTime(Long time, int calendar, int amount) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(time));
        cal.add(calendar, amount);
        return cal.getTimeInMillis();
    }

    /**
     * 获取系统当前时间并根据format进行格式化
     *
     * @param format
     * @return
     */
    public static String getFormatCurrentTime(String format) {
        DateFormat sdf = DateTime.getInstance(format);
        return sdf.format(new Date(System.currentTimeMillis()));
    }

    public static Long parse(String date, String format) {
        DateFormat sdf = DateTime.getInstance(format);
        try {
            return sdf.parse(date).getTime();
        } catch (ParseException e) {
            logger.error("parse error", e);
            return 0L;
        }
    }

    /**
     * 获取系统当前时间
     *
     * @return
     */
    public static String getFormatCurrentTime() {
        return getFormatCurrentTime(DateTime.FORMAT_YYYY_MM_DD_HH_MM_SS);
    }

    /**
     * 获取格式化时间
     *
     * @param timestamp
     * @param format
     * @return
     */
    public static String getFormatTime(Date timestamp, String format) {
        DateFormat sdf = DateTime.getInstance(format);
        return sdf.format(timestamp);
    }

    /**
     * 获取格式化时间
     *
     * @param timestamp
     * @param format
     * @return
     */
    public static String getFormatTime(Long timestamp, String format) {
        DateFormat sdf = DateTime.getInstance(format);
        return sdf.format(timestamp);
    }

    public static String getBeforeFormatTimeBySecond(Long seconds) {
        return getBeforeFormatTimeBySecond(seconds, 0, 0);
    }

    /**
     * 根据秒间隔格式化时间
     *
     * @param seconds
     * @return
     */
    public static String getBeforeFormatTimeBySecond(Long seconds, int depth, int start) {
        if (depth == 0) {
            depth = DateTime.BEFORE_FORMAT.size();
        }
        Long interval = seconds;
        Iterator<String> it = DateTime.BEFORE_FORMAT.keySet().iterator();
        Stack<Pair<Integer, String>> result = new Stack<>();
        StringBuilder beforeFormat = new StringBuilder();
        do {
            String key = it.next();
            if (result.size() == depth - 1) {
                result.push(Pair.create(interval.intValue(), key));
                break;
            }
            Integer value = DateTime.BEFORE_FORMAT.get(key);
            result.push(Pair.create((int) (interval % value), key));
            interval = interval / value;
        }
        while (true);
        while (result.size() > start) {
            Pair<Integer, String> pair = result.pop();
            if (pair.getFirst() > 0) {
                beforeFormat.append(pair.getFirst() + ConfigUtility.getLanguageValue("date_time_unit_" + pair.getSecond(), null, pair.getSecond()));
            }
        }
        return beforeFormat.toString();
    }

    /**
     * 获取**前的格式的时间
     *
     * @return
     */
    public static String getBeforeFormatTime(Long timestamp, int depth, int start) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestamp);
        long timeSplit = (System.currentTimeMillis() - cal.getTimeInMillis()) / 1000;
        return getBeforeFormatTimeBySecond(timeSplit, depth, start);
    }

    /**
     * 获取大于当前时间的最小时间（其余时间单位向下取整）
     *
     * @param unit
     * @return 2014-3-10下午11:12:47 harry
     */
    public static long ceiling(Calendar calendar, DateTimeUnit unit) {
        if (unit.equals(DateTimeUnit.WEEK)) {
            throw new UnsupportedOperationException("WEEK date celling");
        }
        calendar.add(DateTime.DATE_TIME_UNIT_CALENDER_CONVERTER.get(unit), 1);
        floor(calendar, unit);
        return calendar.getTimeInMillis();
    }

    /**
     * 向下取整
     *
     * @param calendar
     * @param unit
     */
    public static long floor(Calendar calendar, DateTimeUnit unit) {
        if (unit.equals(DateTimeUnit.WEEK)) {
            throw new UnsupportedOperationException("WEEK date floor");
        }
        for (DateTimeUnit u : DateTime.DEFAULT_FIRST_VALUE.keySet()) {
            if (u.ordinal() >= unit.ordinal()) {
                continue;
            }
            calendar.set(DateTime.DATE_TIME_UNIT_CALENDER_CONVERTER.get(u), DateTime.DEFAULT_FIRST_VALUE.get(u));
        }
        return calendar.getTimeInMillis();
    }

    public static long roundingExpire(Long timestamp, Long expire) {
        return timestamp / expire;
    }
}
