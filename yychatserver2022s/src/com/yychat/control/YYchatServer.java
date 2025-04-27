package com.yychat.control;

import com.yychat.model.Message;
import com.yychat.model.MessageType;
import com.yychat.model.User;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class YYchatServer {
    private ServerSocket serverSocket = null;
    private Socket socket = null;
    private static HashMap<String,Socket> userSocketMap = new HashMap<>();

    public YYchatServer(){
        try{
            serverSocket = new ServerSocket(3456);
            System.out.println("服务器启动成功，正在监听3456端口...");
            while(true){
                socket = serverSocket.accept();
                System.out.println("连接成功" + socket);
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                User user = (User) in.readObject();
                System.out.println("登录信息:\r\nuserName:" + user.getUserName()+",password:"+user.getPassword());
                Message message = new Message();
                Class.forName("com.mysql.cj.jdbc.Driver");
                String db_url = "jdbc:mysql://localhost:3306/yychat2022s?useUnicode=true&characterEncoding=utf-8";
                String db_user = "root";
                String db_pwd = "Xwl20050219";
                Connection conn;
                boolean loginSuccess = false;
                try{
                    conn = DriverManager.getConnection(db_url, db_user, db_pwd);
                    String query = "select * from user where username=? and password=?";
                    PreparedStatement ps = conn.prepareStatement(query);
                    ps.setString(1,user.getUserName());
                    ps.setString(2,user.getPassword());
                    ResultSet rs = ps.executeQuery();
                    loginSuccess = rs.next();
                }catch(Exception e){
                    e.printStackTrace();
                }
                if(loginSuccess){
                    System.out.println("密码验证通过!");
                    message.setMessageType(MessageType.LOGIN_VALIDATE_SUCCESS);
                }
                else{
                    System.out.println("密码验证失败!");
                    message.setMessageType(MessageType.LOGIN_VALIDATE_FAILURE);
                }
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                out.writeObject(message);
                if(message.getMessageType().equals(MessageType.LOGIN_VALIDATE_SUCCESS)){
                    userSocketMap.put(user.getUserName(),socket);
                    new ServerReceiverThread(socket).start();
                    System.out.println("启动线程成功！");
                }
                else{
                    socket.close();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public static Socket getUserSocket(String userName){
        return (Socket) userSocketMap.get(userName);
    }
    public static HashMap<String,Socket> getUserSocketMap(){
        return userSocketMap;
    }
}
