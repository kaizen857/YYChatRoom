package com.yychat.control;

import com.yychat.model.Message;
import com.yychat.model.MessageType;
import com.yychat.model.User;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class YYchatClientConnection {
    private static Socket socket = null;
    static private ClientReceiverThread thread = null;
    public YYchatClientConnection(){

        try {
            socket = new Socket("127.0.0.1", 3456);
            System.out.println("客户端连接成功" + getSocket());
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean loginValidate(User user){
        boolean loginSuccess = false;
        try{
            OutputStream os = getSocket().getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(os);
            oos.writeObject(user);
            ObjectInputStream ois = new ObjectInputStream(getSocket().getInputStream());
            Message message = (Message) ois.readObject();
            if(message.getMessageType().equals(MessageType.LOGIN_VALIDATE_SUCCESS)){
                loginSuccess = true;
                thread = new ClientReceiverThread(socket);
                thread.start();
            }
            else{
                socket.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return loginSuccess;
    }

    public static Socket getSocket() {
        return socket;
    }

    public static ClientReceiverThread getClientReceiverThread() {
        return thread;
    }
}
