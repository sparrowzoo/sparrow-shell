package com.sparrow.jdk.gc;

import com.caucho.hessian.io.Hessian2Output;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;

/**
 * @author zhanglz
 *
 * -Xmx10m -Xms10m -XX:+UseG1GC -XX:+PrintGCDetails -XX:+G1HeapRegionSize=2m
 */
public class HessianGC {
    static class BigByte implements Serializable {
        private Byte[] bytes;

        public BigByte(Byte[] bytes) {
            this.bytes = bytes;
        }
    }

    static class SmallByte implements Serializable{
        private byte[]bytes;

        public SmallByte(byte[] bytes) {
            this.bytes = bytes;
        }
    }
    public static void main(String[] args) throws IOException {
        hessian(new BigByte(new Byte[1024]));
        hessian(new SmallByte(new byte[1024]));
        System.out.println(Integer.BYTES);
        System.out.println(Byte.BYTES);
    }

    private static void hessian(Object o) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Hessian2Output h2o = new Hessian2Output(os);
        h2o.startMessage();
        h2o.writeObject(o);
        h2o.completeMessage();
        h2o.close();
        byte[] buffer = os.toByteArray();
        os.close();
        System.out.println(buffer.length);
    }
}
