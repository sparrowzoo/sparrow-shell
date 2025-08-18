/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sparrow.jdk.io.disk;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * 文件读取，缓冲区大小（BF_SIZE）对NIO的性能影响特别大，对BIO无影响
 * <p>
 * 10M的文件，BIO耗时87毫秒，NIO耗时68毫秒，Files.read耗时62毫秒
 */

public class IoFileRead {
    public static final String FILE_NAME = "/Users/zhanglizhi/workspace/sparrow/sparrow-shell/sparrow-test/src/test/java/com/sparrow/jdk/io/disk/f.txt";
    public static final String FILE_NAME2 = "/Users/zhanglizhi/workspace/sparrow/sparrow-shell/sparrow-test/src/test/java/com/sparrow/jdk/io/disk/f2.txt";

    /**
     * 缓冲区大小
     */

    public static final int BF_SIZE = 1024 * 1024;

    /**
     * 使用BIO读取文件
     */

    public static String bioRead(String fileName) throws IOException {
        long startTime = System.currentTimeMillis();
        try {
            FileReader reader = new FileReader(fileName);
            StringBuffer buf = new StringBuffer();
            char[] cbuf = new char[BF_SIZE];
            while (reader.read(cbuf) != -1) {
                //buf.append(cbuf);
            }
            reader.close();
            return buf.toString();
        } finally {
            System.out.println("使用BIO读取文件耗时：" + (System.currentTimeMillis() - startTime) + "毫秒");
        }
    }


    public static String nioReadDirect(String fileName) throws IOException {
        long startTime = System.currentTimeMillis();
        try {
            StringBuilder sb = new StringBuilder();
            FileChannel channel = FileChannel.open(Paths.get(fileName), StandardOpenOption.READ);
            ByteBuffer buf = ByteBuffer.allocateDirect(IoFileRead.BF_SIZE);
            while (channel.read(buf) != -1) {
                buf.flip();
                //sb.append(new String(buf.array()));
                buf.clear();
            }
            channel.close();
            return sb.toString();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            System.out.println("使用NIO读取文件耗时：direct " + (System.currentTimeMillis() - startTime) + "毫秒");
        }
    }

    public static String nioReadMap(String fileName) throws IOException {
        long startTime = System.currentTimeMillis();
        try {
            StringBuilder sb = new StringBuilder();
            FileChannel channel = FileChannel.open(Paths.get(fileName), StandardOpenOption.READ);
            ByteBuffer buf = ByteBuffer.allocateDirect(IoFileRead.BF_SIZE);
            while (channel.read(buf) != -1) {
                buf.flip();
                //sb.append(new String(buf.array()));
                buf.clear();
            }
            channel.close();
            return sb.toString();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            System.out.println("使用NIO读取文件耗时 map：" + (System.currentTimeMillis() - startTime) + "毫秒");
        }
    }

    /**
     * 使用Files.read读取文件
     *
     * @param fileName 待读文件名
     * @return
     * @throws IOException
     */

    public static String nioRead2(String fileName) throws IOException {
        long startTime = System.currentTimeMillis();
        try {
            byte[] byt = Files.readAllBytes(Paths.get(fileName));
            //System.out.println(new String(byt));
            return null;//new String(byt);
        } finally {

            System.out.println("使用Files.read读取文件耗时：" + (System.currentTimeMillis() - startTime) + "毫秒");
        }
    }

    public static void map() throws IOException {
        long t=System.currentTimeMillis();
        FileChannel inChannel = FileChannel.open(Paths.get(FILE_NAME2), StandardOpenOption.READ);
        FileChannel outChannel = FileChannel.open(Paths.get(FILE_NAME), StandardOpenOption.WRITE,StandardOpenOption.READ,StandardOpenOption.CREATE);
//        System.out.println("outChannel = " + outChannel);

        long size = inChannel.size();
//        System.out.println("size = " + size);
        MappedByteBuffer inMappedBuffer = inChannel.map(FileChannel.MapMode.READ_ONLY, 0, size);


        byte[] bytes = new byte[inMappedBuffer.limit()];
        inMappedBuffer.get(bytes);
        //可读的channel才可以建立mappedByteBuffer...
        MappedByteBuffer outMappedBuffer = outChannel.map(FileChannel.MapMode.READ_WRITE, 0, size);
        outMappedBuffer.put(bytes);
        System.out.println(System.currentTimeMillis()-t);
    }
    public static void transfer() throws IOException {
        long t=System.currentTimeMillis();
        FileChannel inChannel = FileChannel.open(Paths.get(FILE_NAME), StandardOpenOption.READ);
        FileChannel outChannel = FileChannel.open(Paths.get(FILE_NAME2), StandardOpenOption.WRITE,StandardOpenOption.READ,StandardOpenOption.CREATE);
        inChannel.transferTo(0,inChannel.size(),outChannel);
        System.out.println(System.currentTimeMillis()-t);
    }

    public static void main(String[] args) throws IOException {
        long t = System.currentTimeMillis();
        while (true) {
            if (System.currentTimeMillis() - t > 2000) {
                break;
            }
        }
        IoFileRead.nioReadMap(IoFileRead.FILE_NAME);

        //IoFileRead.bioRead(IoFileRead.FILE_NAME);
        IoFileRead.nioReadDirect(IoFileRead.FILE_NAME2);
        //IoFileRead.nioRead2(IoFileRead.FILE_NAME);
        transfer();
        map();
    }
}
