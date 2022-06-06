package com.sparrow.hook;

/**
 * Created by harry on 2018/5/21.
 * <p>
 * -Xmx20M
 */
public class OutOfMemoryHook {
    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Execute Hook.....");
            }
        }));
        byte[] b = new byte[500 * 1024 * 1024];
        //死循环保证不正常退出
        while (true){}
    }
}
