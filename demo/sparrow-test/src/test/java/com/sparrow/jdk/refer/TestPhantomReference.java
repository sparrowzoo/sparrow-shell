package com.sparrow.jdk.refer;

import java.io.IOException;
import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;

/**
 * -verbose:gc -Xloggc:d:\\jvm_gc_%t.log -Xmx60m -Xms60m -XX:+PrintTenuringDistribution -XX:+PrintGCDetails -XX:+PrintGCDateStamps  -XX:+UseParNewGC -XX:+UseConcMarkSweepGC -XX:CMSInitiatingOccupancyFraction=90 -XX:+UseCMSInitiatingOccupancyOnly
 * 60%3=20 年轻代20m 20/10=2 survivor=2M (2097152 2048k) eden=16M(16,777,216=16384K) cms=60-20=40M%90=36m
 * GC-Log-YG=Eden+Survivor=18M (18432K)
 */
public class TestPhantomReference {
    static ReferenceQueue<Byte[]> referenceQueue = new ReferenceQueue<>();

    /**
     * 1.cleaner 里的队列有没有用?
     * 2.remove 方法有没有必须加锁？
     * 为什么?
     */
    public static void main(String[] args) throws IOException {
        Byte[] data = new Byte[1024 * 1024 * 10];
        PhantomReference phantomReference = new PhantomReference(data, referenceQueue);
        data = null;
        phantomReference=null;
        System.gc();
        ThreadLocal.
        while (true) {
            Reference ref = referenceQueue.poll();
            if (ref != null) {
                System.out.println("after gc " + ref.get());
                System.exit(0);
            }
        }
    }
}
