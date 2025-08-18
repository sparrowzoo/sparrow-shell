package com.sparrow.milvus;

public class VectorMemoryTest {
    public static void main(String[] args) {
        double count=200000*1000*64D;
        System.out.println(count/1024/1024/1024);
        count=200000*8*100D;
        System.out.println(count/1024/1024/1024D);
    }
}
