package com.sparrow.pipeline;

/**
 * @author by harry
 */
public class FirstHandler implements Handler<PipelineMain.PipelineData> {

    @Override
    public HandlerNextAction invoke(PipelineMain.PipelineData arg) {
        System.out.println("First handler executing ... " + Thread.currentThread().getName());
        arg.add(1);
        System.out.println("First handler executed +1");
        return HandlerNextAction.next();
    }
}
