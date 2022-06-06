package com.sparrow.pipeline;

/**
 * @author by harry
 */
public class FiveHander implements Handler<PipelineMain.PipelineData> {
    @Override public void invoke(PipelineMain.PipelineData arg) {
        arg.add(5);
        System.out.println("tid="+Thread.currentThread().getId()+"- 5");
    }
}
