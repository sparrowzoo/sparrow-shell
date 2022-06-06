package com.sparrow.hook;

/**
 * Created by harry on 2018/5/21.
 */
public class UncatchExceptionHook {
    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Execute Hook.....");
            }
        }));
            //运行时异常
            int i = 1 / 0;
        //死循环保证不正常退出
        while (true) {
        }
    }
}
