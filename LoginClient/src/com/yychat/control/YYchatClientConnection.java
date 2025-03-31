package com.yychat.control;

import com.yychat.model.User;

import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class YYchatClientConnection {
    private Socket socket = null;
    public YYchatClientConnection(){

        try {
            socket = new Socket("127.0.0.1", 3456);
            System.out.println("客户端连接成功" + socket);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loginValidate(User user){
        try{
            OutputStream os = socket.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(os);
            oos.writeObject(user);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
