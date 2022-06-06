package com.sparrow.core.cache;

public class ExpirableData<V> {
    private long t;
    private int seconds;
    private V data;

    public ExpirableData(int seconds,V data) {
        this.seconds = seconds;
        this.data = data;
        this.t=System.currentTimeMillis();
    }

    public int getSeconds() {
        return seconds;
    }

    public V getData() {
        return data;
    }

    public void setTimestamp(long t) {
        this.t = t;
    }

    public boolean isExpire(){
        return (System.currentTimeMillis()-t)/1000>seconds;
    }
}
