package com.yychat.control;

import com.yychat.model.Message;
import com.yychat.model.MessageType;
import com.yychat.view.ClientLogin;
import com.yychat.view.FriendList;

import java.io.ObjectOutputStream;

public class ShutdownHook extends Thread implements Runnable{

    @Override
    public void run() {
        System.out.println("ShutdownHook execute start...");
        try {
            ClientReceiverThread thread = ClientLogin.getClientReceiverThread();
            if(thread != null) {
                thread.interrupt();
            }
            if(YYchatClientConnection.getSocket() != null
                    && YYchatClientConnection.getSocket().isConnected()) {
                Message message = new Message();
                message.setMessageType(MessageType.EXIT);
                message.setSender(FriendList.getUserName());
                ObjectOutputStream out = new ObjectOutputStream(YYchatClientConnection.getSocket().getOutputStream());
                out.writeObject(message);
                //out.close();
                YYchatClientConnection.getSocket().close();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("ShutdownHook execute end...");
    }
}
