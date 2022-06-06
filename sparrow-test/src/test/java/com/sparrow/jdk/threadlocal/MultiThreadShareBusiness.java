package com.sparrow.jdk.threadlocal;

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
        if(threadId!=Thread.currentThread().getId()) {
            System.out.println(Thread.currentThread().getId() + "-" + threadId);
        }
    }
}
