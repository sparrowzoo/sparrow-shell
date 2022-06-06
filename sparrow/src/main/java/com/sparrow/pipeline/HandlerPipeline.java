package com.sparrow.pipeline;

import java.util.concurrent.ExecutorService;

/**
 * @author by harry
 */
public interface HandlerPipeline {

    int getAsyncCount();

    boolean isReverse();

    void add(Handler handler);

    void fire(Object arg) throws InterruptedException;

    void addAsync(Handler handler);

    ExecutorService getConsumerThreadPool();
}
