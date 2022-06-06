package com.sparrow.pipeline;

/**
 * @author by harry
 */
public class ThreeHander implements Handler<PipelineMain.PipelineData> {
    @Override public void invoke(PipelineMain.PipelineData arg) {
        arg.add(3);
        System.out.println("tid="+Thread.currentThread().getId()+"- 3");
    }
}
