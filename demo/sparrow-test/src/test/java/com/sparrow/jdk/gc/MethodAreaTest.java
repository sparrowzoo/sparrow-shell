package com.sparrow.jdk.gc;

/**
 * @author by harry
 * -Xmx10m -XX:MaxPermSize=9M //<=1.7
 * -Xmx10m -Xms128M -XX:MetaspaceSize=9M //1.8
 */
public class MethodAreaTest {
    public static void main(String[] args) throws InterruptedException {
        //byte[] a = new byte[1024 * 1024 * 500];
        System.out.println("welcome");
        while (true);
    }
}
