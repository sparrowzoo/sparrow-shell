package com.sparrow.jdk.os;

import com.sun.management.OperatingSystemMXBean;

import java.lang.management.ManagementFactory;

public class OperationSystem {

    /**
     * @param args
     */
    public static void main(String[] args) {
        OperatingSystemMXBean systemBean = (OperatingSystemMXBean) ManagementFactory
                .getOperatingSystemMXBean();
        System.out.println();

        System.out.println(systemBean.getName());
        System.out.println(systemBean.getVersion());
        System.out
                .println(systemBean.getTotalPhysicalMemorySize());
        System.out
                .println(systemBean.getFreePhysicalMemorySize());

    }

}