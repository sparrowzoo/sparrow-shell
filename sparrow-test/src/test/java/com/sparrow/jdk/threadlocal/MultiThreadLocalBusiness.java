package com.sparrow.jdk.threadlocal;

/**
 * @author by harry
 */
public class MultiThreadLocalBusiness {
    // java -Xcomp -XX:+UnlockDiagnosticVMOptions -XX:+PrintAssembly -XX:CompileCommand=compileonly,*MultiThreadLocalBusiness.business com.sparrow.jdk.threadlocal.MultiThreadLocalBusiness

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
        if (threadId.get() != Thread.currentThread().getId()) {
            System.out.println(Thread.currentThread().getId() + "-" + threadId.get());
        }
    }
}
