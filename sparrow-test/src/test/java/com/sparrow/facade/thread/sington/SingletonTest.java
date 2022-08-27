package com.sparrow.facade.thread.sington;

public class SingletonTest {
    private static SingletonTest singletonTest = new SingletonTest();

    private SingletonTest() {
        System.out.println("hello");
    }

    public static SingletonTest getInstance() {
        return singletonTest;
    }

    public static void main(String[] args) {
        System.out.println("hello2");
    }
}
