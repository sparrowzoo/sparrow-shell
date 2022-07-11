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

package com.sparrow.concurrent;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

public class SparrowThreadFactory implements ThreadFactory {
    private final AtomicLong threadCounter;
    private final ThreadFactory wrappedFactory;
    private final Thread.UncaughtExceptionHandler uncaughtExceptionHandler;
    private final String namingPattern;
    private final Integer priority;
    private final Boolean daemonFlag;

    private SparrowThreadFactory(SparrowThreadFactory.Builder builder) {
        if (builder.wrappedFactory == null) {
            this.wrappedFactory = Executors.defaultThreadFactory();
        } else {
            this.wrappedFactory = builder.wrappedFactory;
        }

        this.namingPattern = builder.namingPattern;
        this.priority = builder.priority;
        this.daemonFlag = builder.daemonFlag;
        this.uncaughtExceptionHandler = builder.exceptionHandler;
        this.threadCounter = new AtomicLong();
    }

    public final ThreadFactory getWrappedFactory() {
        return this.wrappedFactory;
    }

    public final String getNamingPattern() {
        return this.namingPattern;
    }

    public final Boolean getDaemonFlag() {
        return this.daemonFlag;
    }

    public final Integer getPriority() {
        return this.priority;
    }

    public final Thread.UncaughtExceptionHandler getUncaughtExceptionHandler() {
        return this.uncaughtExceptionHandler;
    }

    public long getThreadCount() {
        return this.threadCounter.get();
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = this.getWrappedFactory().newThread(r);
        this.initializeThread(t);
        return t;
    }

    private void initializeThread(Thread t) {
        if (this.getNamingPattern() != null) {
            Long count = this.threadCounter.incrementAndGet();
            t.setName(String.format(this.getNamingPattern(), count));
        }

        if (this.getUncaughtExceptionHandler() != null) {
            t.setUncaughtExceptionHandler(this.getUncaughtExceptionHandler());
        }

        if (this.getPriority() != null) {
            t.setPriority(this.getPriority());
        }

        if (this.getDaemonFlag() != null) {
            t.setDaemon(this.getDaemonFlag());
        }

    }

    public static class Builder {
        private ThreadFactory wrappedFactory;
        private Thread.UncaughtExceptionHandler exceptionHandler;
        private String namingPattern;
        private Integer priority;
        private Boolean daemonFlag;

        public Builder() {
        }

        public Builder wrappedFactory(ThreadFactory factory) {
            if (factory == null) {
                throw new NullPointerException("Wrapped ThreadFactory must not be null!");
            } else {
                this.wrappedFactory = factory;
                return this;
            }
        }

        public Builder namingPattern(String pattern) {
            if (pattern == null) {
                throw new NullPointerException("Naming pattern must not be null!");
            } else {
                this.namingPattern = pattern;
                return this;
            }
        }

        public Builder daemon(boolean f) {
            this.daemonFlag = f;
            return this;
        }

        public Builder priority(int prio) {
            this.priority = prio;
            return this;
        }

        public Builder uncaughtExceptionHandler(Thread.UncaughtExceptionHandler handler) {
            if (handler == null) {
                throw new NullPointerException("Uncaught exception handler must not be null!");
            } else {
                this.exceptionHandler = handler;
                return this;
            }
        }

        public void reset() {
            this.wrappedFactory = null;
            this.exceptionHandler = null;
            this.namingPattern = null;
            this.priority = null;
            this.daemonFlag = null;
        }

        public SparrowThreadFactory build() {
            SparrowThreadFactory factory = new SparrowThreadFactory(this);
            this.reset();
            return factory;
        }
    }
}
