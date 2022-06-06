package com.sparrow.jdk.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * accept
 * public Socket accept() //阻塞
 * throws IOException
 * Listens for a connection to be made to this socket and accepts it. The method blocks until a connection is made.
 * <p>
 * SOCKET 会阻塞
 * <p>
 * NIO服务端
 *
 * @author 小路
 */
public class NIOServer {

    //通道管理器
    private Selector selector;

    /**
     * 获得一个ServerSocket通道，并对该通道做一些初始化的工作
     *
     * @param port 绑定的端口号
     * @throws IOException
     */
    public void initServer(int port) throws IOException {
        // 获得一个ServerSocket通道
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        // 设置通道为非阻塞
        serverChannel.configureBlocking(false);
        // 将该通道对应的ServerSocket绑定到port端口
        serverChannel.socket().bind(new InetSocketAddress(port));
        // 获得一个通道管理器
        this.selector = Selector.open();
        //将通道管理器和该通道绑定，并为该通道注册SelectionKey.OP_ACCEPT事件,注册该事件后，
        //当该事件到达时，selector.select()会返回，如果该事件没到达 selector.select()会一直阻塞。
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

    /**
     * 采用轮询的方式监听selector上是否有需要处理的事件，如果有，则进行处理
     *
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public void listen() throws IOException {
        System.out.println("服务端启动成功！" + System.nanoTime()+"-thread-id:"+Thread.currentThread().getId());
        // 轮询访问selector
        while (true) {
            //当注册的事件到达时，方法返回；否则,该方法会一直阻塞
            selector.select();
            // 获得selector中选中的项的迭代器，选中的项为注册的事件
            Iterator ite = this.selector.selectedKeys().iterator();
            while (ite.hasNext()) {
                SelectionKey key = (SelectionKey) ite.next();
                // 删除已选的key,以防重复处理
                ite.remove();
                // 客户端请求连接事件
                if (key.isAcceptable()) {
                    ServerSocketChannel server = (ServerSocketChannel) key
                            .channel();
                    // 获得和客户端连接的通道 与客户的对象不同
                    // 触发客户端 OP_CONNECT 事件
                    SocketChannel channel = server.accept();
                    // 设置成非阻塞
                    channel.configureBlocking(false);
                    //在这里可以给客户端发送信息
                    // 触发客户端 OP_READ
                    channel.write(ByteBuffer.wrap("server.accept()".getBytes()));
                    //在和客户端连接成功之后，为了可以接收到客户端的信息
                    channel.register(this.selector, SelectionKey.OP_READ, "attach");

                    System.out.println(channel.toString());
                    // 获得了可读的事件
                } else if (key.isReadable()) {
                    try {
                        read(key);
                    } catch (Exception ex) {

                    }
                }
            }
        }
    }

    /**
     * 处理读取客户端发来的信息 的事件
     * <p>
     * 在Client/Server模型中，Server往往需要同时处理大量来自Client的访问请求，
     * 因此Server端需采用支持高并发访问的架构。一种简单而又直接的解决方案是“one-thread-per-connection”。
     * 这是一种基于阻塞式I/O的多线程模型。在该模型中，Server为每个Client连接创建一个处理线程，每个处理线程阻塞式等待可能达到的数据，
     * 一旦数据到达，则立即处理请求、返回处理结果并再次进入等待状态。由于每个Client连接有一个单独的处理线程为其服务，
     * 因此可保证良好的响应时间。但当系统负载增大（并发请求增多）时，Server端需要的线程数会增加，这将成为系统扩展的瓶颈所在
     *
     * @param key
     * @throws IOException
     */
    public void read(SelectionKey key) throws IOException {
        // 服务器可读取消息:得到事件发生的Socket通道
        SocketChannel channel = (SocketChannel) key.channel();
        System.out.println(key.attachment());
        // 创建读取的缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(10);
        StringBuilder stringBuffer = new StringBuilder();
        while (channel.read(buffer) > 0) {
            byte[] data = buffer.array();
            String msg = new String(data);
            stringBuffer.append(msg);
            buffer = ByteBuffer.allocate(10);
        }
        System.out.println("服务端收到信息：" + stringBuffer.toString() + System.nanoTime());
        ByteBuffer outBuffer = ByteBuffer.wrap(("service received").getBytes());
        System.out.println(channel.toString());
        channel.write(outBuffer);// 将消息回送给客户端
        //事件只注册一次即可
        //channel.register(this.selector, SelectionKey.OP_READ,"attach");
    }

    /**
     * 启动服务端测试
     */
    public static void main(String[] args) throws IOException {
        NIOServer server = new NIOServer();
        server.initServer(8000);
        server.listen();
    }
}
