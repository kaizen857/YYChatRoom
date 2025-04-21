package com.yychat.model;

public interface MessageType {
    String LOGIN_VALIDATE_SUCCESS = "1";    //登录成功
    String LOGIN_VALIDATE_FAILURE = "2";    //登录失败
    String COMMON_CHAT_MESSAGE = "3";       //聊天消息
    String EXIT = "-1";                     //退出
    String REQUEST_ONLINE_FRIENDS = "4";    //请求在线好友
    String RESPONSE_ONLINE_FRIENDS = "5";   //请求在线好友的回应
    String NEW_ONLINE_TO_ALL_FRIENDS = "6"; //服务器广播给所有在线好友
    String NEW_ONLINE_FRIEND = "7";         //通知服务器新好友上线
}