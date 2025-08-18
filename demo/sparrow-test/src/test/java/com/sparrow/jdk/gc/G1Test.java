package com.sparrow.jdk.gc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 在 jvm 参数里添加-XX:PrintCMSStatistics=1 通过 gc 日志可以看到 cms 回收器在 preclean 阶段执行的操作：
 * G1 -Xmx100m -Xms100m -XX:+UseG1GC -XX:InitiatingHeapOccupancyPercent=30 -XX:+PrintGCDetails -XX:G1HeapRegionSize=2m -XX:G1HeapWastePercent=10 -XX:+PrintGCTimeStamps -XX:+PrintReferenceGC -XX:+PrintAdaptiveSizePolicy -XX:+PrintHeapAtGC -XX:+UnlockExperimentalVMOptions -XX:+G1TraceEagerReclaimHumongousObjects -XX:-G1EagerReclaimHumongousObjects -XX:+UnlockDiagnosticVMOptions -XX:+G1PrintRegionLivenessInfo -XX:+G1PrintHeapRegions
 * <p>
 * <p>
 * <p>
 * -Xmx12G -Xms12G -XX:MaxDirectMemorySize=3G -XX:+UnlockExperimentalVMOptions -XX:G1NewSizePercent=5 -XX:G1HeapRegionSize=16M -XX:G1RSetUpdatingPauseTimePercent=1 -XX:+ParallelRefProcEnabled -XX:MetaspaceSize=1G -XX:+UseG1GC -XX:MaxGCPauseMillis=100 -XX:GCPauseIntervalMillis=300 -XX:G1MixedGCCountTarget=16 -XX:StringTableSize=4000000 -XX:+PrintStringTableStatistics -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:/data/logs/intelligent-rec-gc.log -XX:+PrintGCApplicationConcurrentTime -XX:+PrintGCApplicationStoppedTime -XX:+HeapDumpOnOutOfMemoryError -Djava.net.preferIPv4Stack=true -XX:G1MaxNewSizePercent=50 -XX:InitiatingHeapOccupancyPercent=30 -XX:G1ReservePercent=15 -XX:+UnlockDiagnosticVMOptions -XX:+PrintAdaptiveSizePolicy -XX:+PrintHeapAtGC -XX:+G1TraceEagerReclaimHumongousObjects -XX:+G1PrintHeapRegions
 * <p>
 * -Xmx1G -Xms1G -XX:MaxDirectMemorySize=1G -XX:+UnlockExperimentalVMOptions -XX:G1NewSizePercent=1 -XX:G1HeapRegionSize=16M -XX:G1RSetUpdatingPauseTimePercent=1 -XX:+ParallelRefProcEnabled -XX:MetaspaceSize=1G -XX:+UseG1GC -XX:MaxGCPauseMillis=100 -XX:GCPauseIntervalMillis=300 -XX:G1MixedGCCountTarget=16 -XX:StringTableSize=4000000 -XX:+PrintStringTableStatistics -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:/data/logs/intelligent-rec-gc.log -XX:+PrintGCApplicationConcurrentTime -XX:+PrintGCApplicationStoppedTime -XX:+HeapDumpOnOutOfMemoryError -Djava.net.preferIPv4Stack=true -XX:G1MaxNewSizePercent=50 -XX:InitiatingHeapOccupancyPercent=30 -XX:G1ReservePercent=15 -XX:+UnlockDiagnosticVMOptions -XX:+PrintAdaptiveSizePolicy -XX:+PrintHeapAtGC -XX:+G1TraceEagerReclaimHumongousObjects -XX:+G1PrintHeapRegions
 * java -XX:+PrintFlagsFinal -version | grep G1EagerReclaimHumongousObjects
 * <p>
 * https://blogs.oracle.com/poonam/eager-reclamation-of-humongous-objects-with-g1
 * <p>
 * 除了图中3种常见的Region类型，还有第四种特殊的Region，即：Humongous，它是特殊的老年代Region。
 * 这种Region被设计用来保存超过Region的50%空间的对象，它们存储在一系列连续的Region中。
 * 通常来说，超大对象只会在最终标记结束后的清理阶段（cleanup）才会被回收，或者发生FullGC时。
 * 但是在JDK8U40的时候，JDK更新了一些收集超大对象的特性，以至于在YGC的时候，G1也能回收那些没有任何引用指向的超大对象，
 * 可以通过参数-XX:+G1ReclaimDeadHumongousObjectsAtYoungGC控制，这个参数后来被更名为-XX:+G1EagerReclaimHumongousObjects，
 * 并且可以通过参数-XX:+G1TraceEagerReclaimHumongousObjects跟踪并输出超大对象回收相关信息。
 * <p>
 * release note
 * https://www.oracle.com/java/technologies/javase/8u60-relnotes.html
 */
public class G1Test {
    //存活对象
    private static List<byte[]> liveObjs = new ArrayList<>(45);
    //    static {
//        for(int i=0;i<45;i++){
//            liveObjs.add(getBytes(1));
//            liveObjs.add(getBytes(1));
//        }
//    }
    private static List<Byte[]> liveBigObjs = new ArrayList<>(45);


    /**
     * @param args
     * @throws InterruptedException https://www.redhat.com/en/blog/part-1-introduction-g1-garbage-collector
     *                              <p>
     *                              8821.975: [G1Ergonomics (Mixed GCs) start mixed GCs, reason: candidate old regions available, candidate old regions: 553 regions, reclaimable: 6072062616 bytes (21.75 %), threshold: 5.00 %]
     *                              <p>
     *                              <p>
     *                              The above log tells us that a mixed collection is starting because the number of candidate Old regions (553) have a combined 21.75% reclaimable space.
     *                              This value is higher than our 5% minimum threshold (5% default in JDK8u40+ / 10% default in JDK7) defined by the G1HeapWastePercent and as such,
     *                              mixed collections will begin. Because we don’t want to perform wasted work, G1 stays true to the garbage first policy. Based on an ordered list,
     *                              candidate regions are selected based on their live object percentage. If an Old region has fewer live objects than the percentage defined by G1MixedGCLiveThresholdPercent (defaults to 85% in JDK8u40+ and 65% in JDK7),
     *                              we add it to the list. Simply put, if an Old region is greater than 65% (JDK7) or 85% (JDK8u40+) live, we don’t want to waste our time trying to collect and evacuate it during this mixed cycle.
     */
    public static void main(String[] args) throws InterruptedException, IOException {
        //创建新对象触发gc
        int i = 1;
        while (true) {
            int c = System.in.read();
            if (c == '\n') {
                continue;
            }
            if (c % 2 == 0) {
                byte[] bytes = getBytes(2);
            } else {
                byte[] bytes = getBytes(1);
            }
            //liveBigObjs.add(getBigBytes(1));
        }
    }


    /**
     * 大Byte 与byte 申请资源不同
     *
     * @param m
     * @return
     */
    private static Byte[] getBigBytes(int m) {
        return new Byte[1024];
    }

    private static byte[] getBytes(int m) {
        //1024*512-16 1M对应大对象阀值
        return new byte[1024 * 512 * m];
    }
}