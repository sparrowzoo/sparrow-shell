package com.sparrow.serializer;

public class Demo implements Hello {

    @Override
    public void hello() {
        while (true) {
            System.out.println("hello world");
        }
    }
}
