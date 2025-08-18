package com.sparrow.lesson.thread;

public class ThreadStop {
    public static void main(String[] args) {
        Thread mainThread = Thread.currentThread();
        mainThread.interrupt();
        mainThread.stop();
        mainThread.destroy();
        while (true) {
            if(mainThread.isInterrupted()){
                break;
            }
            //System.out.println(mainThread.getState()+",is interrupt "+mainThread.isInterrupted());
        }
    }
}
