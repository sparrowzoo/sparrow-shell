package com.sparrow.jdk.closeable;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by harry on 2018/5/15.
 */
public class CloseableDemo implements Closeable {

    public static void main(String[] args) {
        try (Closeable latch = new CloseableDemo()) {
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @Override
    public void close() throws IOException {
        System.out.println("close");
    }
}
