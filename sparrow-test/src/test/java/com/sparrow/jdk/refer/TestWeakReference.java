package com.sparrow.jdk.refer;

import com.sparrow.jdk.volatilekey.User;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * -verbose:gc -Xloggc:d:\\jvm_gc_%t.log -Xmx60m -Xms60m -XX:+PrintTenuringDistribution -XX:+PrintGCDetails -XX:+PrintGCDateStamps  -XX:+UseParNewGC -XX:+UseConcMarkSweepGC -XX:CMSInitiatingOccupancyFraction=90 -XX:+UseCMSInitiatingOccupancyOnly
 * 60%3=20 年轻代20m 20/10=2 survivor=2M (2097152 2048k) eden=16M(16,777,216=16384K) cms=60-20=40M%90=36m
 * GC-Log-YG=Eden+Survivor=18M (18432K)
 */
public class TestWeakReference {
    static WeakReference<List<User>> userList = new WeakReference<List<User>>(new ArrayList<>());

    public static void main(String[] args) {
        int i = 0;
        while (true) {
            User u = new User(100, new byte[1024 * 1024 * 1]);
            if (userList.get() != null) {
                userList.get().add(u);
                i++;
                System.out.println("YGC "+i);
            } else {
                System.out.println("Object has been collected.");
                break;
            }
        }
    }
}
