package com.sparrow.pipeline;

/**
 * @author by harry
 */
public class FourHander implements Handler<PipelineMain.PipelineData> {
    @Override public void invoke(PipelineMain.PipelineData arg) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        arg.add(4);
        System.out.println("tid="+Thread.currentThread().getId()+"- 4");
    }
}
