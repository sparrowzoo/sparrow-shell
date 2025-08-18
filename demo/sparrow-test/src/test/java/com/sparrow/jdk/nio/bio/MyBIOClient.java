package com.sparrow.jdk.nio.bio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class MyBIOClient {
    public static void main(String[] args) {
        Socket socket = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        Scanner scanner = new Scanner(System.in);
        try {
            socket = new Socket();
            System.out.println("客户端已启动...");
            socket.connect(new InetSocketAddress("127.0.0.1",1111));
            System.out.println("与服务器"+socket.getRemoteSocketAddress()+"连接建立成功...");
            //通信
            while (true){
                //发送消息
                //从控制台获取发送数据
                String s = scanner.next();
                //获取输出流
                outputStream = socket.getOutputStream();
                //发送数据
                outputStream.write(s.getBytes());
                //刷新缓存
                outputStream.flush();
                //接受消息
                //获取到套接字的输入流
                inputStream = socket.getInputStream();
                //创建存储数据空间
                byte[] bytes = new byte[1024];
                //读取数据 并 获取数据长度
                int len = inputStream.read(bytes);
                //将读到的数据打印到控制台
                System.out.println("服务器"+socket.getRemoteSocketAddress()+"发送的数据为: "+new String(bytes,0,len));

            }
        } catch ( IOException e) {
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
