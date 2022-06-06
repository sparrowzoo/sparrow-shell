package com.sparrow.pipeline;

import org.apache.poi.ss.formula.functions.T;

import java.util.concurrent.CountDownLatch;

/**
 * @author by harry
 */
public class PipelineMain {
    static class PipelineData implements PipelineAsyncData {


        private CountDownLatch countDownLatch;
        private int data = 0;

        public void add(int n) {
            this.data += n;
        }

        public int getData() {
            return data;
        }

        public void setData(int data) {
            this.data = data;
        }

        @Override
        public void initLatch(int count) {
            this.countDownLatch = new CountDownLatch(count);
        }

        @Override
        public CountDownLatch getCountDownLatch() {
            return countDownLatch;
        }

        @Override
        public void latch() throws InterruptedException {
            countDownLatch.await();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        HandlerPipeline handlerPipeline = new SimpleHandlerPipeline(false);
        handlerPipeline.add(new FirstHandler());
        handlerPipeline.add(new SecondHander());
        handlerPipeline.add(new ThreeHander());
        handlerPipeline.addAsync(new FourHander());
        handlerPipeline.addAsync(new FiveHander());
        for (int i = 0; i < 1; i++) {
           new Thread(new Runnable() {
                @Override
                public void run() {
                    PipelineData data = new PipelineData();
                    try {
                        handlerPipeline.fire(data);
                        System.out.println("end"+data.getData());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        System.out.println("over");
       // Thread.sleep(10000);
    }
}
