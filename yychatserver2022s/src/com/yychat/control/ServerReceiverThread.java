package com.yychat.control;

import com.yychat.model.Message;
import com.yychat.model.MessageType;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Iterator;
import java.util.Set;

public class ServerReceiverThread extends Thread {
    private Socket socket = null;
    public ServerReceiverThread(Socket socket) {
        this.socket = socket;
    }
    public void run() {
        while(!this.isInterrupted()){
            try {
                if(!socket.isClosed()){
                    ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                    Message message = (Message) in.readObject();
                    switch (message.getMessageType()) {
                        case MessageType.COMMON_CHAT_MESSAGE -> {
                            System.out.println(message.getSender()
                                    + "对" + message.getReceiver()
                                    + "说：" + message.getContent());
                            String receiver = message.getReceiver();
                            String content = message.getContent();
                            Socket receiverSocket = YYchatServer.getUserSocket(receiver);
                            System.out.println("接收方" + receiver + "的socket对象" + receiverSocket);
                            if (receiverSocket != null) {
                                sendMessage(receiverSocket, message);
                            } else {
                                System.out.println(receiver + "不在线上");
                            }
                        }
                        case MessageType.EXIT -> {
                            System.out.println(message.getSender() + "socket关闭");
                            socket.close();
                            YYchatServer.getUserSocketMap().remove(message.getSender());
                        }
                        case MessageType.REQUEST_ONLINE_FRIENDS -> {
                            Set<String> onlineFriendSet = YYchatServer.getUserSocketMap().keySet();
                            Iterator<String> it = onlineFriendSet.iterator();
                            StringBuilder onlineFriendBuilder = new StringBuilder();
                            while (it.hasNext()) {
                                onlineFriendBuilder.append(it.next());
                                if (it.hasNext()) {
                                    onlineFriendBuilder.append(",");
                                }
                            }
                            String onlineFriends = onlineFriendBuilder.toString();
                            message.setReceiver(message.getSender());
                            message.setSender("Server");
                            message.setMessageType(MessageType.RESPONSE_ONLINE_FRIENDS);
                            message.setContent(onlineFriends);
                            sendMessage(socket, message);
                        }
                        case MessageType.NEW_ONLINE_FRIEND -> {
                            message.setMessageType(MessageType.NEW_ONLINE_TO_ALL_FRIENDS);
                            Set onlineFriendSet = YYchatServer.getUserSocketMap().keySet();
                            Iterator<String> it = onlineFriendSet.iterator();
                            while (it.hasNext()) {
                                String friendName = it.next();
                                message.setReceiver(friendName);
                                Socket friendSocket = YYchatServer.getUserSocket(friendName);
                                sendMessage(friendSocket, message);
                            }
                        }
                        case MessageType.REQUEST_FRIEND_LIST -> {
                            message.setContent(DBUtil.getAllFriends(message.getSender(),1));
                            message.setMessageType(MessageType.RESPONSE_FRIEND_LIST);
                            message.setReceiver(message.getSender());
                            message.setSender("Server");
                            sendMessage(socket, message);
                        }
                        case MessageType.USER_ADD_NEW_FRIEND -> {

                        }
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
    public void sendMessage(Socket socket,Message message){
        ObjectOutputStream out = null;
        try{
            out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(message);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
