package com.yychat.control;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DBUtil {
    private static final String db_url = "jdbc:mysql://localhost:3306/yychat2022s?useUnicode=true&characterEncoding=utf-8";
    private static final String db_user = "root";
    private static final String db_pass = "Xwl20050219";
    private static Connection dataBase = connectDB();

    private static Connection connectDB() {
        Connection tmp = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            tmp = DriverManager.getConnection(db_url, db_user, db_pass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tmp;
    }

    public static boolean loginValidate(String userName,String password){
        boolean loginSuccess = false;
        String query = "select * from user where username=? and password=?";
        PreparedStatement statement = null;
        try{
            statement = dataBase.prepareStatement(query);
            statement.setString(1, userName);
            statement.setString(2, password);
            ResultSet rs = statement.executeQuery();
            loginSuccess = rs.next();
        }catch (Exception e){
            e.printStackTrace();
        }
        return loginSuccess;
    }
}
