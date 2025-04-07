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
package com.sparrow.log.impl;

import com.sparrow.constant.Config;
import com.sparrow.constant.DateTime;
import com.sparrow.log.LogUtils;
import com.sparrow.protocol.constant.Constant;
import com.sparrow.utility.DateTimeUtility;
import com.sparrow.utility.PropertyUtility;
import jdk.jfr.internal.LogLevel;
import org.slf4j.Logger;
import org.slf4j.Marker;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class SparrowLoggerImpl implements Logger {
    private String className;

    private int minLevel;

    private boolean logPrintConsole;

    public SparrowLoggerImpl(String className) {
        this.className = className;
        this.minLevel = LogLevel.INFO.ordinal();
        this.logPrintConsole = true;

        Map<String, String> configMap = PropertyUtility.loadFromClassesPath("/sparrow-log.properties");
        if (configMap.containsKey(Config.LOG_LEVEL)) {
            this.minLevel = Integer.parseInt(configMap.get(Config.LOG_LEVEL));
        }

        if (configMap.containsKey(Config.LOG_PRINT_CONSOLE)) {
            this.logPrintConsole = Boolean.parseBoolean(configMap.get(Config.LOG_PRINT_CONSOLE));
        }
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean isTraceEnabled() {
        return false;
    }

    @Override
    public void trace(String msg) {
        this.writeLog(msg, LogLevel.TRACE);
    }

    @Override
    public void trace(String format, Object arg) {
        this.writeLog(LogUtils.format(format, arg), LogLevel.TRACE);
    }

    @Override
    public void trace(String format, Object arg1, Object arg2) {
        this.writeLog(LogUtils.format(format, arg1, arg2), LogLevel.TRACE);
    }

    @Override
    public void trace(String format, Object... arguments) {
        this.writeLog(LogUtils.format(format, arguments), LogLevel.TRACE);
    }

    @Override
    public void trace(String msg, Throwable t) {
        this.writeLog(LogUtils.printStackTrace(msg, t), LogLevel.TRACE);
    }

    @Override
    public boolean isTraceEnabled(Marker marker) {
        return false;
    }

    @Override
    public void trace(Marker marker, String msg) {
        throw new UnsupportedOperationException("sparrow logger unsupport operation");
    }

    @Override
    public void trace(Marker marker, String format, Object arg) {
        throw new UnsupportedOperationException("sparrow logger unsupport operation");
    }

    @Override
    public void trace(Marker marker, String format, Object arg1, Object arg2) {
        throw new UnsupportedOperationException("sparrow logger unsupport operation");
    }

    @Override
    public void trace(Marker marker, String format, Object... argArray) {
        throw new UnsupportedOperationException("sparrow logger unsupport operation");
    }

    @Override
    public void trace(Marker marker, String msg, Throwable t) {
        throw new UnsupportedOperationException("sparrow logger unsupport operation");
    }

    @Override
    public boolean isDebugEnabled() {
        return false;
    }

    @Override
    public void debug(String log) {
        this.writeLog(log, LogLevel.DEBUG);
    }

    @Override
    public void debug(String format, Object arg) {
        this.writeLog(LogUtils.format(format, arg), LogLevel.DEBUG);
    }

    @Override
    public void debug(String format, Object arg1, Object arg2) {
        this.writeLog(LogUtils.format(format, arg1, arg2), LogLevel.DEBUG);
    }

    @Override
    public void debug(String format, Object... arguments) {
        this.writeLog(LogUtils.format(format, arguments), LogLevel.DEBUG);
    }

    @Override
    public void debug(String msg, Throwable t) {
        this.writeLog(LogUtils.printStackTrace(msg, t), LogLevel.DEBUG);
    }

    @Override
    public boolean isDebugEnabled(Marker marker) {
        return false;
    }

    @Override
    public void debug(Marker marker, String msg) {
        throw new UnsupportedOperationException("sparrow logger unsupport operation");
    }

    @Override
    public void debug(Marker marker, String format, Object arg) {
        throw new UnsupportedOperationException("sparrow logger unsupport operation");

    }

    @Override
    public void debug(Marker marker, String format, Object arg1, Object arg2) {
        throw new UnsupportedOperationException("sparrow logger unsupport operation");
    }

    @Override
    public void debug(Marker marker, String format, Object... arguments) {
        throw new UnsupportedOperationException("sparrow logger unsupport operation");
    }

    @Override
    public void debug(Marker marker, String msg, Throwable t) {
        throw new UnsupportedOperationException("sparrow logger unsupport operation");
    }

    @Override
    public boolean isInfoEnabled() {
        return false;
    }

    @Override
    public void warn(String log) {
        this.writeLog(log, LogLevel.WARN);
    }

    @Override
    public void warn(String format, Object arg) {
        this.writeLog(LogUtils.format(format, arg), LogLevel.WARN);
    }

    @Override
    public void warn(String format, Object... arguments) {
        this.writeLog(LogUtils.format(format, arguments), LogLevel.WARN);
    }

    @Override
    public void warn(String format, Object arg1, Object arg2) {
        this.writeLog(LogUtils.format(format, arg1, arg2), LogLevel.WARN);
    }

    @Override
    public void warn(String msg, Throwable t) {
        this.writeLog(LogUtils.printStackTrace(msg, t), LogLevel.WARN);
    }

    @Override
    public boolean isWarnEnabled(Marker marker) {
        return false;
    }

    @Override
    public void warn(Marker marker, String msg) {
        throw new UnsupportedOperationException("sparrow logger unsupport operation");
    }

    @Override
    public void warn(Marker marker, String format, Object arg) {
        throw new UnsupportedOperationException("sparrow logger unsupport operation");
    }

    @Override
    public void warn(Marker marker, String format, Object arg1, Object arg2) {
        throw new UnsupportedOperationException("sparrow logger unsupport operation");
    }

    @Override
    public void warn(Marker marker, String format, Object... arguments) {
        throw new UnsupportedOperationException("sparrow logger unsupport operation");
    }

    @Override
    public void warn(Marker marker, String msg, Throwable t) {
        throw new UnsupportedOperationException("sparrow logger unsupport operation");
    }

    @Override
    public boolean isErrorEnabled() {
        return false;
    }

    @Override
    public void info(String log) {
        this.writeLog(log, LogLevel.INFO);
    }

    @Override
    public void info(String format, Object arg) {
        this.writeLog(LogUtils.format(format, arg), LogLevel.INFO);
    }

    @Override
    public void info(String format, Object arg1, Object arg2) {
        this.writeLog(LogUtils.format(format, arg1, arg2), LogLevel.INFO);
    }

    @Override
    public void info(String format, Object... arguments) {
        this.writeLog(LogUtils.format(format, arguments), LogLevel.INFO);
    }

    @Override
    public void info(String msg, Throwable t) {
        this.writeLog(LogUtils.printStackTrace(msg, t), LogLevel.INFO);
    }

    @Override
    public boolean isInfoEnabled(Marker marker) {
        return false;
    }

    @Override
    public void info(Marker marker, String msg) {
        throw new UnsupportedOperationException("sparrow logger unsupport operation");
    }

    @Override
    public void info(Marker marker, String format, Object arg) {
        throw new UnsupportedOperationException("sparrow logger unsupport operation");
    }

    @Override
    public void info(Marker marker, String format, Object arg1, Object arg2) {
        throw new UnsupportedOperationException("sparrow logger unsupport operation");
    }

    @Override
    public void info(Marker marker, String format, Object... arguments) {
        throw new UnsupportedOperationException("sparrow logger unsupport operation");
    }

    @Override
    public void info(Marker marker, String msg, Throwable t) {
        throw new UnsupportedOperationException("sparrow logger unsupport operation");
    }

    @Override
    public boolean isWarnEnabled() {
        return false;
    }

    @Override
    public void error(String log) {
        this.writeLog(log, LogLevel.ERROR);
    }

    @Override
    public void error(String format, Object arg) {
        this.writeLog(LogUtils.format(format, arg), LogLevel.ERROR);
    }

    @Override
    public void error(String format, Object arg1, Object arg2) {
        this.writeLog(LogUtils.format(format, arg1, arg2), LogLevel.ERROR);
    }

    @Override
    public void error(String format, Object... arguments) {
        this.writeLog(LogUtils.format(format, arguments), LogLevel.ERROR);
    }

    public void error(Throwable exception) {
        this.error(null, exception);
    }

    @Override
    public void error(String prefixLog, Throwable exception) {
        this.writeLog(LogUtils.printStackTrace(prefixLog, exception), LogLevel.ERROR);
    }

    @Override
    public boolean isErrorEnabled(Marker marker) {
        return false;
    }

    @Override
    public void error(Marker marker, String msg) {
        throw new UnsupportedOperationException("sparrow logger unsupport operation");
    }

    @Override
    public void error(Marker marker, String format, Object arg) {
        throw new UnsupportedOperationException("sparrow logger unsupport operation");
    }

    @Override
    public void error(Marker marker, String format, Object arg1, Object arg2) {
        throw new UnsupportedOperationException("sparrow logger unsupport operation");
    }

    @Override
    public void error(Marker marker, String format, Object... arguments) {
        throw new UnsupportedOperationException("sparrow logger unsupport operation");

    }

    @Override
    public void error(Marker marker, String msg, Throwable t) {
        throw new UnsupportedOperationException("sparrow logger unsupport operation");
    }

    /**
     * 写日志文件。日志文件被入到WebRoot/errorlog.txt
     *
     * @param str log
     */
    private void writeLog(String str, LogLevel logLevel) {
        FileOutputStream fileOutputStream = null;
        try {
            if (logLevel.ordinal() < minLevel) {
                return;
            }
            String path = System.getProperty("user.dir") + "/logs";
            File directory = new File(path);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            fileOutputStream = new FileOutputStream(path + String.format("/log%1$s.log", DateTimeUtility.getFormatCurrentTime(DateTime.FORMAT_YYYYMMDD)), true);

            String log = logLevel + "|" + DateTimeUtility.getFormatCurrentTime() + "|" + this.className + LogUtils.ENTER_TEXT + "--------------------------------------------------------------" + Constant.ENTER_TEXT + str + Constant.ENTER_TEXT;

            //https://blog.csdn.net/shijinupc/article/details/7875826
            //阻塞文件锁
            //todo OverlappingFileLockException exception ?
            //采用共享锁
            //fl = fc.tryLock(0,Long.MAX_VALUE,true);
            FileChannel fileChannel = fileOutputStream.getChannel();
            FileLock fileLock = null;
            try {
                while (true) {
                    try {
                        fileLock = fileChannel.tryLock();
                        if (fileLock != null) {
                            break;
                        }
                    } catch (OverlappingFileLockException ignore) {
                    }
                }
                fileOutputStream.write(log.getBytes(StandardCharsets.UTF_8));
                if (Boolean.TRUE.equals(logPrintConsole)) {
                    System.out.println(log);
                }
            } finally {
                if (fileLock != null) {
                    fileLock.release();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                } catch (Exception ignore) {
                }
            }
        }
    }
}
