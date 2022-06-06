package com.sparrow.job;

import com.sparrow.support.IpSupport;

import java.lang.management.ManagementFactory;

/**
 * @author harry
 */
public class JobInstance {
    private static final String DELIMITER = "@-@";
    private IpSupport ipSupport;

    public void setIpSupport(IpSupport ipSupport) {
        this.ipSupport = ipSupport;
    }

    /**
     * 作业实例主键.
     */
    private final String jobInstanceId;

    public String getJobInstanceId() {
        return jobInstanceId;
    }

    public JobInstance() {
        jobInstanceId = this.ipSupport.getLocalIp() + DELIMITER + ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
    }

    /**
     * 获取作业服务器IP地址.
     *
     * @return 作业服务器IP地址
     */
    public String getIp() {
        return jobInstanceId.substring(0, jobInstanceId.indexOf(DELIMITER));
    }
}
