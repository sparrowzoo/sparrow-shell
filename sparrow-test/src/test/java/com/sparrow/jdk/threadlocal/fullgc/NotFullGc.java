package com.sparrow.jdk.threadlocal.fullgc;

public class NotFullGc {
    public static class O2 {
        private byte[] bytes = new byte[1024 * 1024 * 10];
        public O1 o1;
    }

    public static class O1 {
        public O2 o2;
        private byte[] bytes = new byte[1024 * 1024 * 10];
    }

    public static void main(String[] args) {
        while (true) {
            O2 o2=new O2();
            O1 o1=new O1();
            o1.o2=o2;
            o2.o1=o1;
        }
    }
}
