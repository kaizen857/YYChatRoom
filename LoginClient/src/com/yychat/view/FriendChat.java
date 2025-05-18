package com.yychat.view;

import com.yychat.control.YYchatClientConnection;
import com.yychat.model.Message;
import com.yychat.model.MessageType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class FriendChat extends JFrame implements KeyListener {
    private JButton sendButton = new JButton("发送");
    private JTextArea textArea = new JTextArea();
    public FriendChat(String sender,String receiver) {
        textArea.setForeground(Color.red);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        this.add(scrollPane, BorderLayout.CENTER);

        JTextField messageField = new JTextField(15);
        messageField.addKeyListener(this);
//        messageField.setEditable(false);
//        messageField.setText("测试测试测试测试");
//        messageField.setBorder(null);
        sendButton.addActionListener(e ->
        {
            String msg = messageField.getText();
            textArea.append(msg+"\n");
            messageField.setText("");
            Message message = new Message();
            message.setSender(sender);
            message.setReceiver(receiver);
            message.setMessageType(MessageType.COMMON_CHAT_MESSAGE);
            message.setContent(msg);
            try{
                OutputStream os = YYchatClientConnection.getSocket().getOutputStream();
                ObjectOutputStream out = new ObjectOutputStream(os);
                out.writeObject(message);
            }catch(Exception ex){
                ex.printStackTrace();
            }
        });
        sendButton.setForeground(Color.blue);

        JPanel sendPanel = new JPanel();
        sendPanel.add(messageField);
        sendPanel.add(sendButton);
        this.add(sendPanel, BorderLayout.SOUTH);


        this.setSize(350,250);
        //this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setTitle(receiver + "聊天界面");
        this.setIconImage(new ImageIcon("./res/duck2.gif").getImage());
        this.setVisible(true);

    }
    public static void main(String[] args) {
        FriendChat  tmp= new FriendChat("def","abc");
    }
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            sendButton.doClick();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
    public void append(Message message){
        textArea.append(message.getTime().toString() + "\r\n" + message.getSender() + ": " + message.getContent() + "\r\n");
    }
}
