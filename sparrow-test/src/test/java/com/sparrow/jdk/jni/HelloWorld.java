package com.sparrow.jdk.jni;

public class HelloWorld {

    public static native String hi(String name); // 1.声明这是一个native函数，由本地代码实现public static void main(String[] args) {

    public static void main(String[] args) {
        String text = hi("jni");  // 3.调用本地函数
        System.out.println(text);
    }

    static {
        System.loadLibrary("SparrowJni");   // 2.加载实现了native函数的动态库，只需要写动态库的名字
    }
}