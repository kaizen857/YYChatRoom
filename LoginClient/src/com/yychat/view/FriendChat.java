package com.yychat.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class FriendChat extends JFrame implements KeyListener {
    JButton sendButton = new JButton("发送");
    public FriendChat(String name) {
        JTextArea textArea = new JTextArea();
        textArea.setForeground(Color.red);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        this.add(scrollPane, BorderLayout.CENTER);

        JTextField messageField = new JTextField(15);

        //TODO:添加发送事件
        sendButton.addActionListener(e ->
        {
            textArea.append(messageField.getText()+"\r\n");
            messageField.setText("");
        });
        sendButton.setForeground(Color.blue);

        JPanel sendPanel = new JPanel();
        sendPanel.add(messageField);
        sendPanel.add(sendButton);
        this.add(sendPanel, BorderLayout.SOUTH);


        this.setSize(350,250);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setTitle(name + "聊天界面");
        this.setIconImage(new ImageIcon("./res/duck2.gif").getImage());
        this.setVisible(true);

    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println("keyPressed" + e.getKeyCode());
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            sendButton.doClick();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        System.out.println("点击");
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public static void main(String[] args) {
        FriendChat  tmp= new FriendChat("abc");


    }
}
