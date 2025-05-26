package com.yychat.view;

import java.awt.*;
import java.util.Date;

class ChatMessageData {
    enum SenderType { SELF, OTHER }
    SenderType senderType;
    String message;
    Date timestamp;
    Color avatarColor; // For the sender of this message
//    ImageIcon avatarIcon;
    String avatarPath;

    public ChatMessageData(SenderType senderType, String message, Date timestamp, String avatarColor) {
        this.senderType = senderType;
        this.message = message;
        this.timestamp = timestamp;
        this.avatarPath = avatarColor;
    }
}