package com.yychat.model;

import java.io.Serializable;

public class User implements Serializable {
    String userName;
    String password;

    public String getPassword() {
        return password;
    }

    public String getUserName() {
        return userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }
}
