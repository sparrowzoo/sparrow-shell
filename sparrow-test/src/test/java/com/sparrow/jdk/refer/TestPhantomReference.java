package com.sparrow.jdk.refer;

import java.io.*;
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
    static List<PhantomReference<Byte[]>> list = new ArrayList<>();

    static class StreamWrap {
        private InputStream inputStream;
        private byte[] bytes;

        public StreamWrap(InputStream inputStream, byte[] bytes) {
            this.inputStream = inputStream;
            this.bytes = bytes;
        }
    }

    private static void newMonitorThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    Reference reference = referenceQueue.poll();
                    if (reference != null) {
                        System.out.println(reference);
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
                        System.out.println(reference);
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
            if (c > 0) {
                InputStream file = null;//new FileInputStream(new File("D:\\redis.txt"));
                StreamWrap streamWrap = new StreamWrap(file, new byte[1024 * 1024]);
                PhantomReference phantomReference = new PhantomReference(streamWrap, referenceQueue);
                list.add(phantomReference);
            }
        }
    }
}
