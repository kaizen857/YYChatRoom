package com.yychat.view;

import javax.swing.*;
import java.awt.*;

public class FriendChat extends JFrame {
    public FriendChat() {
        JTextArea textArea = new JTextArea();
        textArea.setForeground(Color.red);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        this.add(scrollPane, BorderLayout.CENTER);

        JTextField messageField = new JTextField(15);
        JButton sendButton = new JButton("发送");
        //TODO:添加发送事件
    }
}
