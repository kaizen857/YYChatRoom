package com.yychat.control;

import com.yychat.model.Message;
import com.yychat.model.MessageType;

import java.io.ObjectInputStream;
import java.net.Socket;

public class ServerReceiverThread extends Thread {
    private Socket socket = null;
    public ServerReceiverThread(Socket socket) {
        this.socket = socket;
    }
    public void run() {
        while(true){
            try {
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                Message message = (Message) in.readObject();
                if(message.getMessageType().equals(MessageType.COMMON_CHAT_MESSAGE)){
                    System.out.println(message.getSender()+"对"+message.getReceiver()+"说："+message.getContent());
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
