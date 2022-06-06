package com.sparrow.loader;

public class ClassloaderDemo {
    public static void main(String[] args) {

        try {
            Class.forName("com.sparrow.log.SparrowLogTest");
            Class clazz = ClassloaderDemo.class.getClassLoader().loadClass("com.sparrow.log.SparrowLogTest");
            System.out.println(clazz);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
