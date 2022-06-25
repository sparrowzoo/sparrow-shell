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
package com.sparrow.core.monitor.impl;

import com.sparrow.core.monitor.ElapsedSection;
import com.sparrow.core.monitor.ElapsedTimeMonitor;
import com.sparrow.utility.StringUtility;
import java.util.Stack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogElapsedTimeMonitorImpl implements ElapsedTimeMonitor {
    private static Logger logger = LoggerFactory.getLogger(LogElapsedTimeMonitorImpl.class);
    private ThreadLocal<Stack<Long>> start = new ThreadLocal<Stack<Long>>();

    @Override public void start() {
        if (start.get() == null) {
            this.start.set(new Stack<Long>());
        }
        start.get().push(System.currentTimeMillis());
    }

    @Override public void elapsed(Object... keys) {
        long current = System.currentTimeMillis();
        long start = this.start.get().pop();
        long elapsed = current - start;
        if (this.start.get() != null && this.start.get().size() == 0) {
            this.start.remove();
        }
        logger.info("thread id {},thread-name {} ,{},elapsed:[{}-{}={}],elapsed-section {}",
            Thread.currentThread().getId(),
            Thread.currentThread().getName(),
            StringUtility.join("-", keys), current, start, elapsed, ElapsedSection.section(elapsed));
    }

    @Override public void elapsedAndRestart(Object... keys) {
        this.elapsed(keys);
        this.start();
    }

    @Override
    public String toString() {
        return "[log elapsed time info ] " + (this.start.get() == null ? "null" : this.start.get().toString());
    }
}
