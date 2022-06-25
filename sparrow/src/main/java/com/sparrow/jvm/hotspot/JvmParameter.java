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
package com.sparrow.jvm.hotspot;

import com.sparrow.utility.StringUtility;

/**
 * https://www.oracle.com/technetwork/java/javase/tech/vmoptions-jsp-140102.html https://docs.oracle.com/javase/8/docs/technotes/tools/unix/java.html
 * https://www.oracle.com/technetwork/java/javase/gc-tuning-6-140523.html
 */
public class JvmParameter {
    /**
     * @param memory      jvm 进程可用的最大内存K为单位 包括heap+stack+DirectMemory
     * @param threadCount 总线程数
     * @param threadStack 每线程内存大小默认为1024K
     */
    public JvmParameter(long memory, int threadCount, int threadStack, int survivorRatio, long directMemory,
        String gcLogFilePath, String dumpLogPath, String errorPath) {
        this.memory = memory;
        if (directMemory > 0) {
            this.directMemory = directMemory;
        }

        this.threadCount = threadCount;
        if (threadStack > 0) {
            this.xss = threadStack;
        }
        long heap = memory - threadCount * threadStack - this.directMemory;
        heap = (heap / 1024) * 1024;

        this.xmx = heap;
        this.xms = heap;

        this.xmn = heap * 3 / 8;
        this.newSize = this.xmn;
        this.maxNewSize = this.xmn;

        /*

        JAVA_OPTS="-server -XX:-OmitStackTraceInFastThrow -Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,address=8081,server=y,suspend=n"
        JAVA_OPTS="$JAVA_OPTS -XX:+UseConcMarkSweepGC -XX:SurvivorRatio=5 -XX:CMSInitiatingOccupancyFraction=80"
        JAVA_OPTS="$JAVA_OPTS -XX:+PrintTenuringDistribution -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+PrintGCDateStamps -XX:+PrintGCApplicationStoppedTime -XX:+PrintGCApplicationConcurrentTime -Xloggc:$LOGS_HOME/gc.log"
        JAVA_OPTS="$JAVA_OPTS -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=LOGS_HOME/dump.hprof"
*/

        if (survivorRatio > 0) {
            this.survivorRatio = survivorRatio;
        }

        if (!StringUtility.isNullOrEmpty(gcLogFilePath)) {
            //-XX:+PrintGCDetails (-verbose:gc & -XX:+PrintGC)
            this.gcLog = String.format(" -Xloggc:%s -XX:+PrintGCDetails -XX:+UseGCLogFileRotation -XX:GCLogFileSize=128M -XX:NumberOfGClogFiles=1", gcLogFilePath);
        }

        if (!StringUtility.isNullOrEmpty(errorPath)) {
            this.errorLog = String.format(" -XX:ErrorFile=%s<pid>.log", errorPath);
        }

        if (!StringUtility.isNullOrEmpty(dumpLogPath)) {
            this.dumpLog = String.format(" -XX:+HeapDumpOnOutOfMemoryError" +
                " -XX:+HeapDumpBeforeFullGC" +
                " -XX:+HeapDumpBeforeFullGC" +
                " -XX:HeapDumpPath=%s.hprof", dumpLogPath);
        }
    }

    private long memory;
    /**
     * 初始堆大小
     */
    private long xms;
    /**
     * 最大堆大小
     */
    private long xmx;
    /**
     * 年轻代大小
     */
    private long xmn;
    /**
     * 年轻代大小
     */
    private long newSize;
    /**
     * 年轻代最大值
     */
    private long maxNewSize;

    /**
     * 默认直接内存256M DirectMemory 的JVM 默认大小是64M， 而JDK6之前和JDK6的某些版本的SUN JVM，存在一个BUG， 在用-Xmx设定堆空间大小的时候，也设置了DirectMemory的大小。假如设置了-Xmx2048m，那么jvm最终可
     * 分配的内存大小为4G多一些，是预期的两倍。
     */
    private long directMemory = 256 * 1024;

    /**
     * 持久代
     */
    private long permSize = 256 * 1024;
    /**
     * 持久代最大值
     */
    private long maxPermSize = 256 * 1024;
    /**
     * 每个线程的堆栈大小
     */
    private long xss = 1024;
    /**
     * 线程数
     */
    private int threadCount;
    /**
     * 年轻代(包括Eden和两个Survivor区)与年老代的比值(除去持久代)
     */
    private int newRatio = 2;
    /**
     * Eden区与Survivor区的大小比值 Eden+Survivor*2=10
     */
    private int survivorRatio = 8;

    private String errorLog;
    private String gcLog;
    private String dumpLog;

    public long getXms() {
        return xms;
    }

    public void setXms(long xms) {
        this.xms = xms;
    }

    public long getXmx() {
        return xmx;
    }

    public void setXmx(long xmx) {
        this.xmx = xmx;
    }

    public long getXmn() {
        return xmn;
    }

    public void setXmn(long xmn) {
        this.xmn = xmn;
    }

    public long getNewSize() {
        return newSize;
    }

    public void setNewSize(long newSize) {
        this.newSize = newSize;
    }

    public long getPermSize() {
        return permSize;
    }

    public void setPermSize(long permSize) {
        this.permSize = permSize;
    }

    public long getMaxPermSize() {
        return maxPermSize;
    }

    public void setMaxPermSize(long maxPermSize) {
        this.maxPermSize = maxPermSize;
    }

    public long getXss() {
        return xss;
    }

    public void setXss(long xss) {
        this.xss = xss;
    }

    public int getNewRatio() {
        return newRatio;
    }

    public void setNewRatio(int newRatio) {
        this.newRatio = newRatio;
    }

    public int getSurvivorRatio() {
        return survivorRatio;
    }

    public void setSurvivorRatio(int survivorRatio) {
        this.survivorRatio = survivorRatio;
    }

    public long getMaxNewSize() {
        return maxNewSize;
    }

    public void setMaxNewSize(long maxNewSize) {
        this.maxNewSize = maxNewSize;
    }

    public String desc() {
        long eden = this.xmn / (this.survivorRatio + 2) * this.survivorRatio;

        return String.format(
            "key| value\n" +
                "---|---\n" +
                "jvm memory(heap+stack+direct memory)|%sM\n" +
                "max heap | %sM\n " +
                "small heap |%sM \n " +
                "young generation |%sM \n  " +
                "old generation memory| %sM\n" +
                "permanent generation |%sM\n " +
                "max permanent generation |%sM \n " +
                "thread stack memory| %sK \n " +
                "stack memory| %sM \n " +
                "survivor ratio| %s\n" +
                "new ratio|%s\n " +
                "eden ratio,memory| %s, %sM\n " +
                "survivor ratio,memory| %s,%sM\n" +
                "direct memory| %sM\n" +
                "error flag| %s\n" +
                "gc log flag| %s\n" +
                "dump flag| %s\n",
            this.memory / 1024,
            this.xmx / 1024,
            this.xms / 1024,
            this.xmn / 1024,
            (this.memory - this.newSize) / 1024,
            this.permSize / 1024,
            this.maxPermSize / 1024,
            this.xss,
            this.xss * this.threadCount / 1024,
            this.survivorRatio,
            this.newRatio,
            this.survivorRatio,
            eden / 1024,
            1,
            (this.xmn - eden) / 2 / 1024,
            this.directMemory / 1024,
            this.errorLog,
            this.gcLog,
            this.dumpLog);
    }

    public String toString() {
        return String.format("-Xmx%sM -Xms%sM -Xmn%sM -XX:PermSize=%sM -XX:MaxPermSize=%sM -Xss%sK XX:SurvivorRatio=%s XX:NewRatio=%s -XX:MaxDirectMemorySize =%sM %s %s %s",
            this.xmx / 1024,
            this.xms / 1024,
            this.xmn / 1024,
            this.permSize / 1024,
            this.maxPermSize / 1024,
            this.xss,
            this.survivorRatio,
            this.newRatio,
            this.directMemory / 1024,
            this.errorLog,
            this.gcLog,
            this.dumpLog);
    }
}
