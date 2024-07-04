package com.sparrow.pipeline;

/**
 * @author by harry
 */
public class SecondHandler implements Handler<PipelineMain.PipelineData> {
    @Override
    public HandlerNextAction invoke(PipelineMain.PipelineData arg) {
        System.out.println("Second handler executing sleep 5s " + Thread.currentThread().getName());
        arg.add(2);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Second handler break +2");
        return HandlerNextAction.next();
    }
}
