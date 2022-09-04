package com.sparrow.facade.thread.visible;

public class TestVolatile {
    public static void main(String[] args) throws InterruptedException {
        ThreadDemo threadDemo = new ThreadDemo();
        new Thread(threadDemo).start();
        new Thread(new Runnable() {
            @Override public void run() {
                while (true) {
                    if (threadDemo.isFlag()) {
                        System.out.println("-------------------可见");
                        break;
                    }
                }
            }
        }).start();
    }
}

class ThreadDemo implements Runnable {
    private boolean flag = false;

    @Override
    public void run() {
        System.out.println("flag = " + isFlag());
        long t=System.currentTimeMillis();
        for (int i = 0; i < 100000000; i++) {
            flag=false;
        }
        System.out.println(System.currentTimeMillis()-t);
        flag = true;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}