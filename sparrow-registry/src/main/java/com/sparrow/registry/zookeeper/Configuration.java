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
package com.sparrow.registry.zookeeper;

public class Configuration {
    /**
     * host1:2181,host2:2181
     */
    private String serverLists;

    /**
     * namespace.
     */
    private String namespace;

    /**
     * wait retry interval time. unit milliseconds
     */
    private int waitRetryIntervalTime = 1000;

    /**
     * max wait retry interval time unit milliseconds
     */
    private int maxWaitRetryIntervalTime = 3000;

    /**
     * max retry times
     */
    private int maxRetryTimes = 3;

    /**
     * sessionTimeoutMilliseconds. unit milliseconds.
     */
    private int sessionTimeout;

    /**
     * connection timeout.
     */
    private int connectionTimeout;

    /**
     * digest.
     */
    private String digest;

    public String getServerLists() {
        return serverLists;
    }

    public void setServerLists(String serverLists) {
        this.serverLists = serverLists;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public int getWaitRetryIntervalTime() {
        return waitRetryIntervalTime;
    }

    public void setWaitRetryIntervalTime(int waitRetryIntervalTime) {
        this.waitRetryIntervalTime = waitRetryIntervalTime;
    }

    public int getMaxWaitRetryIntervalTime() {
        return maxWaitRetryIntervalTime;
    }

    public void setMaxWaitRetryIntervalTime(int maxWaitRetryIntervalTime) {
        this.maxWaitRetryIntervalTime = maxWaitRetryIntervalTime;
    }

    public int getMaxRetryTimes() {
        return maxRetryTimes;
    }

    public void setMaxRetryTimes(int maxRetryTimes) {
        this.maxRetryTimes = maxRetryTimes;
    }

    public int getSessionTimeout() {
        return sessionTimeout;
    }

    public void setSessionTimeout(int sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }
}
