package com.yychat.control;

import com.yychat.model.Message;
import com.yychat.model.MessageType;
import com.yychat.view.FriendChat;
import com.yychat.view.FriendList;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ClientReceiverThread extends Thread{
    private Socket socket;
    public ClientReceiverThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        while(true){
            try {
                if(!socket.isClosed()){
                    ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                    Message message = (Message) in.readObject();
                    if(message.getMessageType().equals(MessageType.COMMON_CHAT_MESSAGE)){
                        String receiver = message.getReceiver();
                        String sender = message.getSender();
                        FriendChat chat = FriendList.getFriendChat(receiver + "to" + sender);
                        if(chat != null){
                            chat.append(message);
                        }
                        else{
                            System.out.println("请打开" + receiver + "to" + sender + "的聊天界面");
                        }
                    }
                    else if(message.getMessageType().equals(MessageType.RESPONSE_ONLINE_FRIENDS)){

                    }
                }
                else{
                    this.interrupt();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
