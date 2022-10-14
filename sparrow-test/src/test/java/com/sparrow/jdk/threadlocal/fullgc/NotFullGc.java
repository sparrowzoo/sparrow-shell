package com.sparrow.jdk.threadlocal.fullgc;

public class NotFullGc {
    public static class O {
        private byte[] bytes = new byte[1024 * 1024 * 10];
        private ThreadLocal locals = new ThreadLocal();

        public O() {
            locals.set(new byte[1024 * 1024 * 10]);
        }
    }

    public static void main(String[] args) {
        while (true) {
            new O();
        }
    }
}
