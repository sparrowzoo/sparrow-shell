package com.sparrow.pipeline;

/**
 * @author by harry
 */
public class ThreeHandler implements Handler<PipelineMain.PipelineData> {
    @Override
    public HandlerNextAction invoke(PipelineMain.PipelineData arg) {
        System.out.println("Three handler executing ... " + Thread.currentThread().getName());
        arg.add(3);
        System.out.println("Three handler executed +3");
        return HandlerNextAction.next(3);
    }
}
