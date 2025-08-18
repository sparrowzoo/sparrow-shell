package com.sparrow.pipeline;

/**
 * @author by harry
 */
public class FiveHandler implements Handler<PipelineMain.PipelineData> {
    @Override
    public HandlerNextAction invoke(PipelineMain.PipelineData arg) {
        System.out.println("Five handler asny ... " + Thread.currentThread().getName());
        arg.add(5);
        System.out.println("Five handler executed +5");
        return HandlerNextAction.next();
    }
}
