package com.sparrow.pipeline;

import com.alibaba.fastjson.JSON;


/**
 * @author by harry
 */
public class PipelineMain {
    static class PipelineData extends DefaultAsynPipelineData {
        public PipelineData() {
            super.setThrowWhenException();
        }

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
    }

    public static void main(String[] args) throws InterruptedException {
        HandlerPipeline handlerPipeline = new SimpleHandlerPipeline(true);
        handlerPipeline.add(new FirstHandler());
        handlerPipeline.add(new SecondHandler());
        handlerPipeline.add(new ThreeHandler());
        handlerPipeline.addAsync(new FourHandler());
        handlerPipeline.addAsync(new FiveHandler());
        PipelineData data = new PipelineData();
        handlerPipeline.fire(data);
        System.out.println("total is " + data.getData());
        System.out.println(JSON.toJSONString(data.getResult()));
    }
}
