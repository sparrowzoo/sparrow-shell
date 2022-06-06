///*
// * Licensed to the Apache Software Foundation (ASF) under one or more
// * contributor license agreements.  See the NOTICE file distributed with
// * this work for additional information regarding copyright ownership.
// * The ASF licenses this file to You under the Apache License, Version 2.0
// * (the "License"); you may not use this file except in compliance with
// * the License.  You may obtain a copy of the License at
// *
// *     http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package com.sparrow.log.impl;
//
//import ch.qos.logback.classic.Level;
//import ch.qos.logback.classic.Logger;
//import ch.qos.logback.classic.LoggerContext;
//import com.sparrow.utility.StringUtility;
//import java.util.List;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;
//import org.apache.commons.lang.StringUtils;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.InitializingBean;
//
///**
// * Created by harry on 2017/4/19.
// */
//public abstract class AbstractLevelListener implements InitializingBean {
//    public abstract String getLevel();
//
//    private List<String> exceptionLogList;
//
//    public void setExceptionLogList(List<String> exceptionLogList) {
//        this.exceptionLogList = exceptionLogList;
//    }
//
//    private void scan() {
//        String level = this.getLevel();
//        if (StringUtility.isNullOrEmpty(level)) {
//            return;
//        }
//
//        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
//
//        List<Logger> loggerList = loggerContext.getLoggerList();
//        ch.qos.logback.classic.Logger rootLogger = loggerContext.getLogger("ROOT");
//
//        if (rootLogger == null || rootLogger.getLevel() == null) {
//            return;
//        }
//
//        if (rootLogger.getLevel().levelStr.equalsIgnoreCase(level)) {
//            return;
//        }
//
//        for (ch.qos.logback.classic.Logger logger : loggerList) {
//            if(!except(logger.getName())) {
//                logger.setLevel(Level.toLevel(level, logger.getLevel()));
//            }
//        }
//    }
//    private boolean except(String logger){
//        if(exceptionLogList==null){
//            return false;
//        }
//        for(String log:exceptionLogList){
//            if(logger.contains(log)){
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public void afterPropertiesSet() {
//        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
//        executorService.scheduleAtFixedRate(new Runnable() {
//            @Override
//            public void run() {
//                scan();
//            }
//        }, 0, 1, TimeUnit.SECONDS);
//    }
//}
