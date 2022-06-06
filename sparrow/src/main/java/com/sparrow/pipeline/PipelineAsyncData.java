package com.sparrow.pipeline;

import java.util.concurrent.CountDownLatch;

/**
 * @author: zhanglizhi01@meicai.cn
 * @date: 2019/6/21 10:37
 * @description:
 */
public interface PipelineAsyncData {
     void initLatch(int count);

    CountDownLatch getCountDownLatch();

    void latch() throws InterruptedException;
}
