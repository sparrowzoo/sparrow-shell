package com.sparrow.serializer;

import java.io.*;

public class KryoSerializableTest {
    public static void main(String[] args) throws Exception {
        Hello hello = new Demo();
        byte[] bytes = SerializeUtil.serialize(hello);
        Hello simpleDe = (Hello) SerializeUtil.deserialize(bytes);
        //simpleDe.hello();


        bytes = jdkSerializable(hello);
        simpleDe = (Hello) jdkDeserilizer(bytes);
        simpleDe.hello();
    }

    public static Object jdkDeserilizer(byte[] bytes) {
        //定义内存变量
        Object obj = null;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bis);
            obj = ois.readObject();
            ois.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return obj;
    }

    public static byte[] jdkSerializable(Object object) {
        //定义内存变量
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(object);
            oos.flush();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bos.toByteArray();
    }
}
