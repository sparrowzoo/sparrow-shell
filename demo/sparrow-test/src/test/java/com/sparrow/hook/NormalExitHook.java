package com.sparrow.hook;

/**
 * Created by harryy on 2018/5/21.
 */
public class NormalExitHook {
    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Execute Hook.....");
            }
        }));
        System.out.println("do something");
    }
}
