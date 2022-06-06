package com.sparrow.pipeline;

import com.sparrow.concurrent.SparrowThreadFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author by harry
 */
public class SimpleHandlerPipeline implements HandlerPipeline {
    private ExecutorService consumerThreadPool = Executors.newCachedThreadPool(new SparrowThreadFactory.Builder().namingPattern("pipeline-async-%d").build());

    public SimpleHandlerPipeline(boolean reverse) {
        this.reverse = reverse;
    }

    public SimpleHandlerPipeline() {
        this.reverse = false;
    }

    private HandlerContext head;
    private HandlerContext tail;
    private int asyncCount;

    private boolean reverse;

    @Override
    public int getAsyncCount() {
        return this.asyncCount;
    }

    @Override
    public boolean isReverse() {
        return reverse;
    }


    @Override
    public void add(Handler handler) {
        this.add(handler, false);
    }

    private void add(Handler handler, boolean asyc) {
        HandlerContext handlerContext = new HandlerContext(this, handler, asyc);
        if (head == null) {
            head = handlerContext;
            tail = handlerContext;
            return;
        }

        handlerContext.prev = tail;
        tail.next = handlerContext;
        tail = handlerContext;
    }

    @Override
    public void addAsync(Handler handler) {
        this.add(handler, true);
        this.asyncCount++;
    }

    @Override
    public void fire(Object arg) throws InterruptedException {
        PipelineAsyncData pipelineAsyncData = null;
        if (asyncCount > 0 && arg instanceof PipelineAsyncData) {
            pipelineAsyncData = (PipelineAsyncData) arg;
            pipelineAsyncData.initLatch(this.asyncCount);
        }
        if (!reverse) {
            head.fire(arg);
        } else {
            tail.fire(arg);
        }

        if (pipelineAsyncData != null) {
            pipelineAsyncData.getCountDownLatch().await();
        }

    }

    public ExecutorService getConsumerThreadPool() {
        return consumerThreadPool;
    }
}
