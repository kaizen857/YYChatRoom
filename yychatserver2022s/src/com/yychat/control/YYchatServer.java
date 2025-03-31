package com.yychat.control;

import com.yychat.model.User;

import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class YYchatServer {
    ServerSocket serverSocket = null;
    Socket socket = null;

    public YYchatServer(){
        try{
            serverSocket = new ServerSocket(3456);
            System.out.println("服务器启动成功，正在监听3456端口...");
            socket = serverSocket.accept();
            System.out.println("连接成功" + socket);
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            User user = (User) in.readObject();
            System.out.println("登录信息:\r\nuserName:" + user.getUserName()+",password:"+user.getPassword() );

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
