package com.yychat.view;

import com.yychat.control.YYchatClientConnection;
import com.yychat.model.User;

import javax.swing.*;
import java.awt.*;

public class ClientLogin extends JFrame{
    public ClientLogin(){
        JLabel headImage = new JLabel(new ImageIcon("./res/head.gif"));
        this.add(headImage,"North");
        JLabel YYNumberText = new JLabel("YY号码：", JLabel.CENTER);
        JLabel YYPasswordText = new JLabel("YY密码：", JLabel.CENTER);
        JLabel ForgotPasswordTest = new JLabel("忘记密码", JLabel.CENTER);
        ForgotPasswordTest.setForeground(Color.blue);
        JLabel passwordProtectText = new JLabel("申请密码保护", JLabel.CENTER);

        JButton clearPassword = new JButton(new ImageIcon("./res/clear.gif"));
        JTextField numberTextBox = new JTextField();
        JPasswordField passwordTextBox = new JPasswordField();

        JCheckBox stealthLogin = new JCheckBox("隐身登录");
        JCheckBox rememberPassword = new JCheckBox("记住密码");

        JPanel body = new JPanel(new GridLayout(3,3));
        body.add(YYNumberText);
        body.add(numberTextBox);
        body.add(clearPassword);
        body.add(YYPasswordText);
        body.add(passwordTextBox);
        body.add(ForgotPasswordTest);
        body.add(stealthLogin);
        body.add(rememberPassword);
        body.add(passwordProtectText);

        JTabbedPane loginMode = new JTabbedPane();
        loginMode.add(body,"YY号码");
        JPanel phoneNumberPanel = new JPanel();
        JPanel emailPanel = new JPanel();
        loginMode.add(phoneNumberPanel,"手机号码");
        loginMode.add(emailPanel,"电子邮箱");

        this.add(loginMode,"Center");

        JButton loginButton = new JButton(new ImageIcon("./res/login.gif"));
        JButton registerButton = new JButton(new ImageIcon("./res/register.gif"));
        JButton cancelButton = new JButton(new ImageIcon("./res/cancel.jpg"));
        JPanel buttonPanel = new JPanel();

        loginButton.addActionListener(event -> {
            String tarName = "kaizen";
            String tarPassword = "123456";
            String name = numberTextBox.getText();
            String password = new String(passwordTextBox.getPassword());
            if(tarName.equals(name) && tarPassword.equals(password)){
                User user = new User(name, password);
                new YYchatClientConnection().loginValidate(user);
                new FriendList(name);
                this.dispose();
            }
        });
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        buttonPanel.add(cancelButton);

        this.add(buttonPanel,"South");

        Image icon = new ImageIcon("./res/duck2.gif").getImage();
        this.setIconImage(icon);

        this.setLocationRelativeTo(null);
        this.setSize(350,250);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("YY聊天");
        this.setVisible(true);
    }

    public static void main(String[] args) {
        ClientLogin frame = new ClientLogin();
    }
}
