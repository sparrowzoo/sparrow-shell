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
package com.sparrow.job;

import com.sparrow.support.IpSupport;

import java.lang.management.ManagementFactory;

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
