package com.sparrow.registry.zookeeper;

/**
 * @author harry
 * @date 2018/4/13
 */
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
     * wait retry interval time.
     * unit milliseconds
     */
    private int waitRetryIntervalTime = 1000;

    /**
     * max wait retry interval time
     * unit milliseconds
     */
    private int maxWaitRetryIntervalTime = 3000;

    /**
     * max retry times
     */
    private int maxRetryTimes = 3;

    /**
     * sessionTimeoutMilliseconds.
     * unit milliseconds.
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
