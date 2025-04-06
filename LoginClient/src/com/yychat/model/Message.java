package com.yychat.model;

import java.io.Serializable;

public class Message implements Serializable,MessageType {
    String MessageType;

    public String getMessageType() {
        return MessageType;
    }
    public void setMessageType(String messageType) {
        this.MessageType = messageType;
    }
}
