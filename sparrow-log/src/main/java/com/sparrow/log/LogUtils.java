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

package com.sparrow.log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LogUtils {
    public static final String ENTER_TEXT = "\r\n";
    public static final String FORMAT_YYYY_MM_DD_HH_MM_SS_MS = "yyyy-MM-dd HH:mm:ss SSS";
    public static final String FORMAT_YYYYMMDD = "yyyyMMdd";

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
     * 获取系统当前时间
     *
     * @return
     */
    public static String getFormatCurrentTime(String format) {
        DateFormat sdf = getInstance(format);
        return sdf.format(new Date(System.currentTimeMillis()));
    }


    public static String format(String format, Object... args) {
        if (format == null) {
            return "";
        }

        if (!format.contains("{}") || args == null) {
            return format;
        }

        for (Object arg : args) {
            format = format.replaceFirst("\\{\\}", String.valueOf(arg));
        }
        return format;
    }

    public static String printStackTrace(String msg, Throwable t) {
        PrintWriter pw = null;
        try {
            StringWriter sw = new StringWriter();
            pw = new PrintWriter(sw);
            t.printStackTrace(pw);
            StringBuilder exceptionString = new StringBuilder();
            if (msg != null && !msg.isEmpty()) {
                exceptionString.append(msg);
                exceptionString.append(ENTER_TEXT);
            }
            exceptionString.append(sw);
            return exceptionString.toString();
        } finally {
            if (pw != null) {
                pw.flush();
                pw.close();
            }
        }
    }
}
