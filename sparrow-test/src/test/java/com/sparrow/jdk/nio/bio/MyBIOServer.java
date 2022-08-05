package com.sparrow.jdk.nio.bio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MyBIOServer {
    static class SocketHandler extends Thread{
        private Socket socket;

        public SocketHandler(Socket socket,String name){
            super(name);
            this.socket = socket;
        }

        @Override
        public void run(){
            InputStream inputStream = null;
            OutputStream outputStream = null;
            Scanner scanner = new Scanner(System.in);
            try {
                //通信
                while (true){
                    System.out.println("socket is blocking "+Thread.currentThread().getName()+"-status"+Thread.currentThread().getState());
                    //接受消息
                    //获取到套接字的输入流
                    inputStream = socket.getInputStream();
                    System.out.println("stop?");
                    //创建存储数据空间
                    byte[] bytes = new byte[1024];
                    //读取数据 并 获取数据长度
                    int len = inputStream.read(bytes);
                    //将读到的数据打印到控制台
                    System.out.println("客户端"+socket.getRemoteSocketAddress()+"发送的数据为: "+new String(bytes,0,len));
                    //发送消息
                    //从控制台获取发送数据
                    String s = scanner.next();
                    //获取输出流
                    outputStream = socket.getOutputStream();
                    //发送数据
                    outputStream.write(s.getBytes());
                    //刷新缓存
                    outputStream.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                    //退出输入输出流
                    inputStream.close();
                    outputStream.close();
                    //关闭套接字 关闭连接
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args){
        ServerSocket serverSocket = null;
        List<Thread> threads=new ArrayList<>();
        new Thread(new Runnable() {
            @Override public void run() {
                while (true) {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    for (Thread thread : threads) {
                        System.out.println(thread.getName() + "-" + thread.getState());
                    }
                }
            }
        }).start();
        try {
            //实例化ServerSocket
            serverSocket = new ServerSocket();
            //绑定端口
            serverSocket.bind(new InetSocketAddress(1111));
            System.out.println("服务器已启动...");
            while (true){
                threads.add(Thread.currentThread());
                //监听连接   建立连接
                Socket socket = serverSocket.accept();
                System.out.println("与客户端"+socket.getRemoteSocketAddress()+"连接建立成功...");
                //分配给子线程
                SocketHandler socketHandler = new SocketHandler(socket,"socket server");

                threads.add(socketHandler);
                //子线程启动通信
                socketHandler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
