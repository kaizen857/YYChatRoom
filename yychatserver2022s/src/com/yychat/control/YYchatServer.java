package com.yychat.control;

import com.yychat.model.Message;
import com.yychat.model.MessageType;
import com.yychat.model.User;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class YYchatServer {
    ServerSocket serverSocket = null;
    Socket socket = null;

    public YYchatServer(){
        try{
            serverSocket = new ServerSocket(3456);
            System.out.println("服务器启动成功，正在监听3456端口...");
            while(true){
                socket = serverSocket.accept();
                System.out.println("连接成功" + socket);
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                User user = (User) in.readObject();
                System.out.println("登录信息:\r\nuserName:" + user.getUserName()+",password:"+user.getPassword() );
                Message message = new Message();
                if(user.getPassword().equals("123456")){
                    System.out.println("密码验证通过!");
                    message.setMessageType(MessageType.LOGIN_VALIDATE_SUCCESS);
                }
                else{
                    System.out.println("密码验证失败!");
                    message.setMessageType(MessageType.LOGIN_VALIDATE_FAILURE);
                }
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                out.writeObject(message);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
