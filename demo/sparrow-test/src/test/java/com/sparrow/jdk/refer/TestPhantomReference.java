package com.sparrow.jdk.refer;

import java.io.File;
import java.io.FileInputStream;
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
public class TestPhantomReference {
    static ReferenceQueue<Byte[]> referenceQueue = new ReferenceQueue<>();
    //模拟使用
    static List<PhantomReference<Byte[]>> list = new ArrayList<>();
    //随机缓存
    static List<Byte[]> byteList = new ArrayList<>();

    private static void newMonitorThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    Reference reference = referenceQueue.poll();
                    if (reference != null) {
                        //ReferenceQueue.poll() 方法获取到的对象已经被垃圾回收器回收
                        byte[] bytes = (byte[]) reference.get();
                        list.remove(reference);
                        //todo @Override  PhantomReference method
                        /**
                         * static class ConnectionPhantomReference extends PhantomReference<ConnectionImpl> {
                         *         private NetworkResources io;
                         *
                         *         ConnectionPhantomReference(ConnectionImpl connectionImpl, ReferenceQueue<ConnectionImpl> q) {
                         *             super(connectionImpl, q);
                         *
                         *             try {
                         *                 this.io = connectionImpl.getIO().getNetworkResources();
                         *             } catch (SQLException e) {
                         *                 // if we somehow got here and there's really no i/o, we deal with it later
                         *             }
                         *         }
                         *
                         *         void cleanup() {
                         *             if (this.io != null) {
                         *                 try {
                         *                     this.io.forceClose();
                         *                 } finally {
                         *                     this.io = null;
                         *                 }
                         *             }
                         *         }
                         *     }
                         */
                        //((InputStream)reference.get()).close();
                        System.out.println("回收后" + list.size());
//                    System.exit(0);
                    }
                }
            }
        }).start();
    }

    public static void main(String[] args) throws IOException {
        newMonitorThread();
        while (true) {
            int c = System.in.read();
            if (c == '\n') {
                continue;
            }
            Byte[] data = new Byte[1024 * 1024 * 10];
            PhantomReference phantomReference = new PhantomReference(data, referenceQueue);
            list.add(phantomReference);
            if (c % 2 == 0) {
                byteList.add(data);
            }
            System.out.println("add " + list.size() + "BYTE LIST" + byteList.size());

            System.gc();
        }
    }
}
