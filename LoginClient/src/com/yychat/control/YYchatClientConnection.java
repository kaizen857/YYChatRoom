package com.yychat.control;

import com.yychat.model.Message;
import com.yychat.model.MessageType;
import com.yychat.model.User;

import java.io.ObjectInputStream;
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

    public boolean loginValidate(User user){
        boolean loginSuccess = false;
        try{
            OutputStream os = socket.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(os);
            oos.writeObject(user);
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Message message = (Message) ois.readObject();
            if(message.getMessageType().equals(MessageType.LOGIN_VALIDATE_SUCCESS)){
                loginSuccess = true;
            }
            oos.close();
            os.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return loginSuccess;
    }
}
