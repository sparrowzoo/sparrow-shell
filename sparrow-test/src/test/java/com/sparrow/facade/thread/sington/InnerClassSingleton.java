package com.sparrow.facade.thread.sington;

public class InnerClassSingleton {
    private static class Inner {
        private static InnerClassSingleton innerClassSington = new InnerClassSingleton();
    }

    private InnerClassSingleton() {
        System.out.println("instance");
    }

    public static InnerClassSingleton getInstance() {
        return Inner.innerClassSington;
    }

    public static void main(String[] args) {
        System.out.println(InnerClassSingleton.class);
        //InnerClassSington.getInstance();
    }
}
