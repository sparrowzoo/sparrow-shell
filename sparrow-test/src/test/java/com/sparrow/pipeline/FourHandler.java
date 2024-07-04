package com.sparrow.pipeline;

/**
 * @author by harry
 */
public class FourHandler implements Handler<PipelineMain.PipelineData> {
    @Override
    public HandlerNextAction invoke(PipelineMain.PipelineData arg) {
        System.out.println("Four handler asny sleep 5s ... " + Thread.currentThread().getName());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Four handler executed +4");
        int i = 1 / 0;
        arg.add(4);
        return HandlerNextAction.next(4);
    }
}
