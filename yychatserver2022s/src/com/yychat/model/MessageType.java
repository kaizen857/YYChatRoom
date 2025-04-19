package com.yychat.model;

public interface MessageType {
    String LOGIN_VALIDATE_SUCCESS = "1";
    String LOGIN_VALIDATE_FAILURE = "2";
    String COMMON_CHAT_MESSAGE = "3";
    String EXIT = "-1";
    String REQUEST_ONLINE_FRIENDS = "4";
    String RESPONSE_ONLINE_FRIENDS = "5";
}
