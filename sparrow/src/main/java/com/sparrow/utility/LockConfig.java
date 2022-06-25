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

import com.sparrow.enums.DateTimeUnit;

import java.util.Calendar;

public class LockConfig {

    /**
     * 锁构造
     *
     * @param absolute           是否绝对时间
     * @param lockTime           锁定时间
     * @param maxTimes           锁定时间内最大操作次数
     * @param isContinueLockTime 是否顺延时间
     * @param containOperationId key是否包含operation id
     */
    private LockConfig(Boolean absolute, int lockTime, int maxTimes,
        boolean isContinueLockTime, boolean containOperationId, DateTimeUnit dateTimeUnit) {
        this.absolute = absolute;
        this.lockTime = lockTime;
        this.maxTimes = maxTimes;
        this.isContinueLockTime = isContinueLockTime;
        this.containOperationId = containOperationId;
        this.dateTimeUnit = dateTimeUnit;
    }

    /**
     * 获取相对过期锁
     *
     * @param lockTime
     * @param maxTimes
     * @param isContinueLockTime 允许延长过期时间
     * @param containOperationId
     * @return
     */
    public static LockConfig getRelativeLock(int lockTime, int maxTimes,
        boolean isContinueLockTime, boolean containOperationId) {
        return new LockConfig(false, lockTime, maxTimes, isContinueLockTime, containOperationId, null);
    }

    /**
     * 绝对时间构照 每次必须new生成，因为不允许为单例出现，线程不安全
     * <p>
     * isContinueLockTime 不延长过期时间
     *
     * @param maxTimes
     */
    public static LockConfig getAbsoluteLock(DateTimeUnit dateTimeUnit, int maxTimes) {
        return new LockConfig(true, 1, maxTimes, false, true, dateTimeUnit);
    }

    /**
     * 是否绝对时间 举例说明... 过期时间为当天,当天的最大时间减去当前时间，即剩余绝对过期时间 相对过期时间为24小时(1天)
     */
    private boolean absolute;
    /**
     * 锁定时间
     */
    private int lockTime;

    /**
     * 锁过期时间
     */
    private DateTimeUnit dateTimeUnit;
    /**
     * 锁定时间内最多操作次数
     */
    private int maxTimes;
    /**
     * 是否会顺延锁定时间
     */
    private boolean isContinueLockTime;
    /**
     * key中是否包含operationId
     */
    private boolean containOperationId;

    public boolean isContainOperationId() {
        return containOperationId;
    }

    public void setContainOperationId(boolean containOperationId) {
        this.containOperationId = containOperationId;
    }

    public long getAbsoluteLockTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        return DateTimeUtility.ceiling(calendar, this.dateTimeUnit);
    }

    public int getLockTime() {
        return lockTime;
    }

    public void setLockTime(int lockTime) {
        this.lockTime = lockTime;
    }

    public int getMaxTimes() {
        return maxTimes;
    }

    public void setMaxTimes(int maxTimes) {
        this.maxTimes = maxTimes;
    }

    public boolean isContinueLockTime() {
        return isContinueLockTime;
    }

    public void setContinueLockTime(boolean isContinueLockTime) {
        this.isContinueLockTime = isContinueLockTime;
    }

    public DateTimeUnit getDateTimeUnit() {
        return dateTimeUnit;
    }

    public void setDateTimeUnit(DateTimeUnit dateTimeUnit) {
        this.dateTimeUnit = dateTimeUnit;
    }

    public boolean isAbsolute() {
        return absolute;
    }
}
