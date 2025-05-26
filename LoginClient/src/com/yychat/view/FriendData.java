package com.yychat.view;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

class FriendData {
    String id;
    String name;
    String avatarPath; // Placeholder for image path or color
    //Color avatarColor;
//    ImageIcon avatar;
    String lastMessage;
    String lastMessageDate;
    List<ChatMessageData> chatHistory;

    public FriendData(String id, String name, String avatarColor, String lastMessage, String lastMessageDate) {
        this.id = id;
        this.name = name;
        this.avatarPath = avatarColor;
        this.lastMessage = lastMessage;
        this.lastMessageDate = lastMessageDate;
        this.chatHistory = new ArrayList<>();
    }

//    public ImageIcon getAvatar() {
//        return avatar;
//    }
}
