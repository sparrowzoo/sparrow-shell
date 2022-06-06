package com.sparrow.redis;

public class RedisCapcityTest {
    public static void main(String[] args) {
        String key=String.format("mc:0:lrcsfs:%s:%s",Integer.MAX_VALUE,Integer.MAX_VALUE);
        long length= key.getBytes().length+"0.02886395".length()+8;
    }
}
