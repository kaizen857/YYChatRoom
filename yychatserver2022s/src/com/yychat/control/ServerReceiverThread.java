package com.yychat.control;

import com.yychat.model.Message;
import com.yychat.model.MessageType;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerReceiverThread extends Thread {
    private Socket socket = null;
    public ServerReceiverThread(Socket socket) {
        this.socket = socket;
    }
    public void run() {
        while(true){
            try {
                if(!socket.isClosed()){
                    ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                    Message message = (Message) in.readObject();
                    if(message.getMessageType().equals(MessageType.COMMON_CHAT_MESSAGE)){
                        System.out.println(message.getSender()
                                +"对"+message.getReceiver()
                                +"说："+message.getContent());
                        String receiver = message.getReceiver();
                        String content = message.getContent();
                        Socket receiverSocket = YYchatServer.getUserSocket(receiver);
                        System.out.println("接收方" + receiver + "的socket对象" + receiverSocket);
                        if(receiverSocket != null){
                            ObjectOutputStream out = new ObjectOutputStream(receiverSocket.getOutputStream());
                            out.writeObject(message);
                        }
                        else{
                            System.out.println(receiver + "不在线上");
                        }
                    }
                    else if(message.getMessageType().equals(MessageType.EXIT)){
                        System.out.println(message.getSender() + "socket关闭");
                        socket.close();
                    }
                }
                else{
                    this.interrupt();
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
