package com.yychat.control;

import com.yychat.model.Message;
import com.yychat.model.MessageType;
import com.yychat.view.ClientLogin;
import com.yychat.view.FriendChat;
import com.yychat.view.FriendList;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;

public class ClientReceiverThread extends Thread{
    private Socket socket;
    public ClientReceiverThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        while(!this.isInterrupted()){
            try {
                if(!socket.isClosed()){
                    ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                    Message message = (Message) in.readObject();
                    switch (message.getMessageType()) {
                        case MessageType.COMMON_CHAT_MESSAGE -> {
                            String receiver = message.getReceiver();
                            String sender = message.getSender();
                            FriendChat chat = FriendList.getFriendChat(receiver + "to" + sender);
                            if (chat != null) {
                                chat.append(message);
                            } else {
                                System.out.println("请打开" + receiver + "to" + sender + "的聊天界面");
                            }
                        }
                        case MessageType.RESPONSE_ONLINE_FRIENDS -> {
                            FriendList friendList = ClientLogin.friendListHashMap.get(message.getReceiver());
                            if (friendList != null) {
                                friendList.activeOnlineFriendIcon(message.getContent());
                            }
                        }
                        case MessageType.NEW_ONLINE_TO_ALL_FRIENDS -> {
                            String receiver = message.getReceiver();
                            FriendList friendList = ClientLogin.friendListHashMap.get(receiver);
                            String sender = message.getSender();
                            if (friendList != null) {
                                friendList.activeNewOnlineFriendIcon(sender);
                            }
                        }
                        case MessageType.RESPONSE_USER_ADD_NEW_FRIEND -> {
                            String receiver = message.getReceiver();
                            FriendList friendList = ClientLogin.friendListHashMap.get(receiver);
                            if (friendList != null) {
                                friendList.setHasACKFromServer(true);
                            }
                        }
                    }
                }
                else{
                    this.interrupt();
                }
            }
            catch(SocketException se){
                if(this.isInterrupted() && socket.isClosed()){
                    break;
                }
                else{
                    se.printStackTrace();
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
