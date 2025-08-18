package com.sparrow.hook;

/**
 * Created by harry on 2018/5/21.
 */
public class KillHook {
    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("execute Hook.....");
            }
        }));
        System.out.println("do something");
        //死循环保证不正常退出
        while (true) {
        }
    }
}
