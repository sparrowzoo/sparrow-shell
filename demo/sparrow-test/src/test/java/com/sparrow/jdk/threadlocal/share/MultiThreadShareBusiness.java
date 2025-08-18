package com.sparrow.jdk.threadlocal.share;

/**
 * @author by harry
 */
public class MultiThreadShareBusiness {
    private Long threadId;

    public Long getThreadId() {
        return threadId;
    }

    public void setThreadId(Long threadId) {
        this.threadId = threadId;
    }

    public void business(){
       //如果变量的值与当前线程不同，说明线程不安全
        if(threadId!=Thread.currentThread().getId()) {
            System.out.println(Thread.currentThread().getId() + "-" + threadId);
        }
    }
}