package com.sparrow.jdk.clinit;

public class Main {
    static {
        System.out.println("static init");
    }
    private static int i=initInt(1);
    private static int j=initInt(2);
    static {
        System.out.println("static init 2");
    }
    private static Integer initInt(int i){
        System.out.println("static field init"+i);
        return 100;
    }

    public static void main(String[] args) {
        System.out.println("main method ");
    }
}
