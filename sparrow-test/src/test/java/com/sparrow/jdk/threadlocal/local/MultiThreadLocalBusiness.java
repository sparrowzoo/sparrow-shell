package com.sparrow.jdk.threadlocal.local;

/**
 * @author by harry
 */
public class MultiThreadLocalBusiness {

    public static void main(String[] args) {
        MultiThreadLocalBusiness m=new MultiThreadLocalBusiness();
        m.setThreadId(1L);
        m.business();
    }
    private ThreadLocal<Long> threadId = new ThreadLocal<>();

    public void setThreadId(Long threadId) {
        this.threadId.set(threadId);
    }

    public void business() {
        ThreadLocal<Long> t=this.threadId;
        if (t.get() != Thread.currentThread().getId()) {
            System.out.println(Thread.currentThread().getId() + "-" + t.get());
        }
    }
}