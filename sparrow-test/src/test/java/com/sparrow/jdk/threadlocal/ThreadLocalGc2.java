package com.sparrow.jdk.threadlocal;

/**
 * @author by harry
 */
public class ThreadLocalGc2 {

    /**
     * -Xmx40m -Xms40m -Xmn20m -XX:+PrintTenuringDistribution -XX:+PrintGCDetails -XX:+PrintGCDateStamps  -XX:+UseParNewGC -XX:+UseConcMarkSweepGC -XX:CMSInitiatingOccupancyFraction=75 -XX:+UseCMSInitiatingOccupancyOnly
     *
     * @param args
     */
    public static void main(String[] args) throws InterruptedException {
        ThreadLocal<String> threadLocal = new ThreadLocal<>();
        threadLocal.set("aaa");
        threadLocal.get();
        threadLocal=null;
        System.gc();
        ThreadLocal<String> threadLocal1=new ThreadLocal<>();
        threadLocal1.set("bbb");
        System.gc();
    }
}
