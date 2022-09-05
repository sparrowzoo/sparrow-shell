package com.sparrow.facade.thread.visible;

/**
 *  java -Xcomp -XX:+UnlockDiagnosticVMOptions -XX:+PrintAssembly -XX:CompileCommand=compileonly *MemorySync.main com.sparrow.facade.thread.visible.MemorySync
 */
public class MemorySync {
    static class ThreadDemo implements Runnable {

        private boolean flag = false;

        @Override
        public void run() {
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            flag = true;
        }
        public boolean getFlag() {
            return flag;
        }
    }
    public static void main(String[] args) throws InterruptedException {
        ThreadDemo threadDemo = new ThreadDemo();
        Thread t= new Thread(threadDemo, "t1");
        t.start();
        new Thread(new Runnable() {
            @Override public void run() {
                while (true) {
                    if (threadDemo.getFlag()) {
                        System.out.println("end task");
                        break;
                    }
                }
            }
        }).start();
    }
}


