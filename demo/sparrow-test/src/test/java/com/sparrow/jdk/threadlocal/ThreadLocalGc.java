package com.sparrow.jdk.threadlocal;

/**
 * @author by harry
 */
public class ThreadLocalGc {
    //注意这里是thread local 非WeakReference
    private static ThreadLocal<String> s = new ThreadLocal<String>();

    public static void main(String[] args) {
        s.set("hello");
        System.out.println(s.get());
        System.gc();
        System.out.println(s.get());
    }
}
