package com.sparrow.jdk.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * NIO客户端
 *
 * @author 小路
 */
public class NIOClient {

    //通道管理器
    private Selector selector;

    /**
     * 获得一个Socket通道，并对该通道做一些初始化的工作
     *
     * @param ip   连接的服务器的ip
     * @param port 连接的服务器的端口号
     * @throws IOException
     */
    public void initClient(String ip, int port) throws IOException {
        // 获得一个Socket通道
        SocketChannel channel = SocketChannel.open();
        // 设置通道为非阻塞
        channel.configureBlocking(false);
        // 获得一个通道管理器
        this.selector = Selector.open();
        // 客户端连接服务器,其实方法执行并没有实现连接，需要在listen（）方法中调
        //用channel.finishConnect();才能完成连接

        //解发服务器accept就绪事件
        channel.connect(new InetSocketAddress(ip, port));
        //将通道管理器和该通道绑定，并为该通道注册SelectionKey.OP_CONNECT事件
        //客户端连接服务端事件
        channel.register(selector, SelectionKey.OP_CONNECT);
    }

    /**
     * 采用轮询的方式监听selector上是否有需要处理的事件，如果有，则进行处理
     *
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public void listen() throws IOException {
        // 轮询访问selector
        while (true) {
            selector.select();
            // 获得selector中选中的项的迭代器
            Iterator ite = this.selector.selectedKeys().iterator();
            while (ite.hasNext()) {
                SelectionKey key = (SelectionKey) ite.next();

                // 删除已选的key,以防重复处理
                ite.remove();
                // 连接事件发生
                if (key.isConnectable()) {
                    SocketChannel channel = (SocketChannel) key
                            .channel();

                    // 如果正在连接，则完成连接
                    if (channel.isConnectionPending()) {
                        channel.finishConnect();
                    }
                    // 设置成非阻塞
                    channel.configureBlocking(false);
                    //在这里可以给服务端发送信息 服务器读就诸事件
                    byte[] buffer= new String("to server message:").getBytes();
                    Integer bufferSize=buffer.length;
                    //bufferSize.byteValue();
                    channel.write(ByteBuffer.wrap(buffer));
                    System.out.println(channel);
                    //在和服务端连接成功之后，为了可以接收到服务端的信息，需要给通道设置读的事件。
                    channel.register(this.selector, SelectionKey.OP_READ);
                    // 获得了可读的事件
                } else if (key.isReadable()) {
                    read(key);
                }
            }
        }
    }

    /**
     * 处理读取服务端发来的信息 的事件
     *
     * @param key
     * @throws IOException
     */
    public void read(SelectionKey key) throws IOException {
        // 和服务端的read方法一样
        // 服务器可读取消息:得到事件发生的Socket通道
        SocketChannel channel = (SocketChannel) key.channel();
        // 创建读取的缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(10);
        StringBuffer stringBuffer = new StringBuffer();
        while (channel.read(buffer) > 0) {
            byte[] data = buffer.array();
            String msg = new String(data);
            stringBuffer.append(msg);
            buffer = ByteBuffer.allocate(10);
        }

        //在这里可以给服务端发送信息
        for (int i = 0; i < 10; i++) {
            channel.write(ByteBuffer.wrap(new String("to server message" + i+"\n").getBytes()));
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //事件只注册一次即可
        channel.register(this.selector, SelectionKey.OP_READ);
        System.out.println("client receive message：" + stringBuffer + System.nanoTime());
    }

    /**
     * 启动客户端测试
     *
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        NIOClient client = new NIOClient();
        client.initClient("localhost", 8000);
        client.listen();

        NIOClient client2 = new NIOClient();
        client2.initClient("localhost", 8000);
        client2.listen();
    }
}
