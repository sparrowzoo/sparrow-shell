package com.sparrow.jdk.refer;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.ArrayList;
import java.util.List;

/**
 * -verbose:gc -Xloggc:d:\\jvm_gc_%t.log -Xmx60m -Xms60m -XX:+PrintTenuringDistribution -XX:+PrintGCDetails -XX:+PrintGCDateStamps  -XX:+UseParNewGC -XX:+UseConcMarkSweepGC -XX:CMSInitiatingOccupancyFraction=90 -XX:+UseCMSInitiatingOccupancyOnly
 * 60%3=20 年轻代20m 20/10=2 survivor=2M (2097152 2048k) eden=16M(16,777,216=16384K) cms=60-20=40M%90=36m
 * GC-Log-YG=Eden+Survivor=18M (18432K)
 */
public class PhantomEdenThreadReferenceTest {
    static ReferenceQueue<Byte[]> referenceQueue = new ReferenceQueue<>();
    static List<PhantomReference<Byte[]>> list = new ArrayList<>();
    public static void main(String[] args) throws IOException {
        while (true) {
            Reference reference = referenceQueue.poll();
            if (reference != null) {
                System.out.println("YGC");
                list.remove(reference);
            }
            byte[] bytes = new byte[1024 * 1024];
            PhantomReference phantomReference = new PhantomReference(bytes, referenceQueue);
            list.add(phantomReference);
        }
    }
}
