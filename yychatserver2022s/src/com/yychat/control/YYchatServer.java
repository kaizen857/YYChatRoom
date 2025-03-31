package com.yychat.control;

import java.net.ServerSocket;
import java.net.Socket;

public class YYchatServer {
    public YYchatServer(){
        ServerSocket serverSocket = null;
        Socket socket = null;
        try{
            serverSocket = new ServerSocket(3456);
            System.out.println("服务器启动成功，正在监听3456端口...");
            socket = serverSocket.accept();
            System.out.println("连接成功" + socket);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
