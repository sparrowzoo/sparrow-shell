package com.sparrow.jdk.refer;

import com.sparrow.jdk.volatilekey.User;

import java.io.IOException;
import java.lang.ref.*;
import java.util.ArrayList;
import java.util.List;

/**
 * -verbose:gc -Xloggc:d:\\jvm_gc_%t.log -Xmx60m -Xms60m -XX:+PrintTenuringDistribution -XX:+PrintGCDetails -XX:+PrintGCDateStamps  -XX:+UseParNewGC -XX:+UseConcMarkSweepGC -XX:CMSInitiatingOccupancyFraction=90 -XX:+UseCMSInitiatingOccupancyOnly
 * 60%3=20 年轻代20m 20/10=2 survivor=2M (2097152 2048k) eden=16M(16,777,216=16384K) cms=60-20=40M*0.1=4m
 * GC-Log-YG=Eden+Survivor=18M (18432K)
 * <p>
 * <p>
 * 2020-04-03T18:31:56.344+0800: [GC (Allocation Failure) 2020-04-03T18:31:56.344+0800: [ParNew: 17411K->17411K(18432K), 0.0000412 secs]2020-04-03T18:31:56.344+0800: [CMS: 40819K->40819K(40960K), 0.0055953 secs] 58230K->58230K(59392K), [Metaspace: 3315K->3315K(1056768K)], 0.0057484 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]
 * 2020-04-03T18:31:56.350+0800: [Full GC (Allocation Failure) 2020-04-03T18:31:56.350+0800: [CMS: 40819K->880K(40960K), 0.0047811 secs] 58230K->880K(59392K), [Metaspace: 3315K->3315K(1056768K)], 0.0048825 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]
 * 2end
 * <p>
 * jmap -heap 25412
 * jstat -gc pid count
 */
public class EdenTest {
    public static void main(String[] args) throws IOException, InterruptedException {
        WeakReference<List<User>> weakReference = new WeakReference<>(new ArrayList<User>());
        while (true) {
            //换成byte[]内存溢出
            //todo 动态获取内存大小
            User u = new User(100, new byte[1024 * 1024]);
            List<User> users = weakReference.get();
            if (users != null) {
                users.add(u);
                Thread.sleep(100);
            } else {
                int c = System.in.read();
                System.out.println((char) c);
                weakReference = new WeakReference<>(new ArrayList<User>());
            }
        }
    }
}
