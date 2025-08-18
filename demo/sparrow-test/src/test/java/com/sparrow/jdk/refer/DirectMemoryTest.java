package com.sparrow.jdk.refer;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * -verbose:gc -Xloggc:d:\\jvm_gc_%t.log -Xmx60m -Xms60m -XX:+PrintTenuringDistribution -XX:+PrintGCDetails -XX:+PrintGCDateStamps  -XX:+UseParNewGC -XX:+UseConcMarkSweepGC -XX:CMSInitiatingOccupancyFraction=90 -XX:+UseCMSInitiatingOccupancyOnly -XX:MaxDirectMemorySize=20m
 * -XX:+AlwaysPreTouch
 * <p>
 * https://www.elastic.co/guide/en/elasticsearch/reference/current/jvm-options.html
 * https://new.qq.com/omn/20190116/20190116G11RDY.html
 * https://docs.oracle.com/javase/8/docs/technotes/guides/vm/gctuning/g1_gc_tuning.html#g1_gc_tuning
 **/
public class DirectMemoryTest {

    private static final MemoryMXBean memoryMXBean;

    static {
        memoryMXBean = ManagementFactory.getMemoryMXBean();
    }

    public static void main(String[] args) {
        System.out.println("maxMemoryValue:" + sun.misc.VM.maxDirectMemory() / 1024 / 1024);
        /**
         * The maxMemory() method returns the maximum amount of memory that the VM
         * will attempt to use, allowing applications to better manage memory load.
         * If the implementation-dependent -Xmx flag is used then this method will
         * return that value.
         *
         * 可见默认设置和Heap的Size差不多
         */
        System.out.println("directMemory:" + Runtime.getRuntime().maxMemory() / 1024 / 1024);
        //ByteBuffer buffer = ByteBuffer.allocateDirect(1024 * 1024 * 58);
        //ByteBuffer inner = ByteBuffer.allocate(1024 * 1024 * 30);


        //264241152:10
        //266338304:11
        //268435456:12
        //270532608:13
        //452984832:100
        //473956352:110
        System.out.println(270532608 - 268435456);
        System.out.println(268435456 - 266338304);
        System.out.println(473956352 - 264241152);
        System.out.println(264241152 / 1024 / 1024);
        System.out.println(452984832 / 1024 / 1024);
        System.out.println("max:" + memoryMXBean.getNonHeapMemoryUsage().getMax());
        System.out.println("used:" + memoryMXBean.getNonHeapMemoryUsage().getUsed());
        System.out.println("init:" + memoryMXBean.getNonHeapMemoryUsage().getInit());
        //默认是commit
        System.out.println("commit:" + memoryMXBean.getNonHeapMemoryUsage().getCommitted());


        List<ByteBuffer> list = new ArrayList<ByteBuffer>();
        while (true) {
            //ByteBuffer buffer = ByteBuffer.allocateDirect(1 * 1024 * 1024);
            //list.add(buffer);
        }
    }
}
