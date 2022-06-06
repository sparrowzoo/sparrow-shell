package com.sparrow.hook;

/**
 * Created by harry on 2018/5/21.
 * <p>
 * JAVA_HOME：D:\Java\jdk1.8.0_91
 * <p>
 * CLASSPATH：.;%JAVA_HOME%\lib\dt.jar;%JAVA_HOME%\lib\tools.jar;
 */
public class CtrlCHook {
    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Execute Hook.....");
            }
        }));
        System.out.println("do something");
        System.out.println("press ctrl+c");
        //死循环保证不正常退出
        while (true) {
        }
    }
}
