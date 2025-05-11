package com.yychat.view;

import com.yychat.control.YYchatClientConnection;
import com.yychat.model.Message;
import com.yychat.model.MessageType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.ObjectOutputStream;
import java.util.HashMap;

public class FriendList extends JFrame {
    final int FRIENDCOUNT = 50;
    final int STRANGERCOUNT = 20;
    private static String Name;
    JLabel[] friendLabel;
    JPanel friendListPanel = null;
    private static HashMap<String, FriendChat> friendChatMap = new HashMap<String,FriendChat>();
    public FriendList(String name,String friendListName){
        Name = name;
        JPanel friendPanel = new JPanel(new BorderLayout());
        JPanel addFriendPanel = new JPanel(new GridLayout(2,1));
        JButton friendButton1 = new JButton("我的好友");
        JButton strangerButton1 = new JButton("陌生人");
        JButton addFriendButton = new JButton("添加好友");

        addFriendButton.addActionListener(e->{
            String newFriendName = JOptionPane.showInputDialog("请输入新好友的名字：");
            System.out.println("new friend:" + newFriendName);
            if(newFriendName != null){
                Message message = new Message();
                message.setSender(Name);
                message.setReceiver("Server");
                message.setContent(newFriendName);
                message.setMessageType(MessageType.USER_ADD_NEW_FRIEND);
                try{
                    ObjectOutputStream out = new ObjectOutputStream(YYchatClientConnection.getSocket().getOutputStream());
                    out.writeObject(message);
//                    long startTime = System.currentTimeMillis();
//                    boolean flag = true;
//                    while(ACKFromServer.equals("-1")){
//                        long currentTime = System.currentTimeMillis();
//                        if(currentTime - startTime > 10000){
//                            flag = false;
//                            break;
//                        }
//
//                    }
//                    if(flag){
//                        switch (ACKFromServer){
//                            case MessageType.USER_ADD_NEW_FRIEND_SUCCESS -> {
//                                JOptionPane.showMessageDialog(this,"添加成功！");
//                                this.addNewFriend(newFriendName);
//                            }
//                            case MessageType.USER_ADD_NEW_FRIEND_FAILURE_NO_USER -> {
//                                JOptionPane.showMessageDialog(this,"添加失败！没有该用户");
//                            }
//                            case MessageType.USER_ADD_NEW_FRIEND_FAILURE_ALREADY_FRIEND -> {
//                                JOptionPane.showMessageDialog(this,"添加失败！该用户已经是你的朋友！");
//                            }
//                        }
//                    }
//                    else{
//                        JOptionPane.showMessageDialog(this,"服务器响应超时！");
//                    }
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        });

        addFriendPanel.add(addFriendButton);
        addFriendPanel.add(friendButton1);

        JButton blackListButton1 = new JButton("黑名单");
        friendPanel.add(addFriendPanel, BorderLayout.NORTH);

        String[] friendList = friendListName.split(" ");
        friendListPanel = new JPanel(new GridLayout(0,1));//friendList.length
        friendLabel = new JLabel[friendList.length];
        for(int i=0;i<friendList.length;i++){
            if(friendList[i] != null){
                ImageIcon icon = new ImageIcon("res/"+ i%6 +".jpg");
                friendLabel[i] = new JLabel(friendList[i],icon,JLabel.LEFT);
                if(!friendLabel[i].getText().equals(name)){
                    friendLabel[i].setEnabled(false);
                }
                friendLabel[i].addMouseListener(new MouseListener() {
                    public void mouseClicked(MouseEvent e) {
                        if(e.getClickCount() ==2 && e.getSource() instanceof JLabel label){
                            String friendName = label.getText();
                            FriendChat chat = new FriendChat(name,friendName);
                            friendChatMap.put(name + "to" + friendName,chat);
                        }
                    }
                    @Override
                    public void mousePressed(MouseEvent e) {}
                    @Override
                    public void mouseReleased(MouseEvent e) {}
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        if(e.getSource() instanceof JLabel label){
                            label.setForeground(Color.red);
                        }
                    }
                    @Override
                    public void mouseExited(MouseEvent e) {
                        if(e.getSource() instanceof JLabel label){
                            label.setForeground(Color.black);
                        }
                    }
                });
                friendListPanel.add(friendLabel[i]);
            }
        }

        JScrollPane friendListScrollPane = new JScrollPane(friendListPanel);
        friendPanel.add(friendListScrollPane, BorderLayout.CENTER);

        JPanel strangerBlackPanel = new JPanel(new GridLayout(2,1));
        strangerBlackPanel.add(strangerButton1);
        strangerBlackPanel.add(blackListButton1);
        friendPanel.add(strangerBlackPanel, BorderLayout.SOUTH);

        JPanel strangerPanel = new JPanel(new BorderLayout());
        JButton friendButton2 = new JButton("我的好友");
        JButton strangerButton2 = new JButton("陌生人");
        JPanel friendStrangerPanel = new JPanel(new GridLayout(2,1));
        friendStrangerPanel.add(friendButton2);
        friendStrangerPanel.add(strangerButton2);
        strangerPanel.add(friendStrangerPanel, BorderLayout.NORTH);

        JLabel[] strangerLabel = new JLabel[STRANGERCOUNT];
        JPanel strangerListPanel = new JPanel(new GridLayout(STRANGERCOUNT,1));
        for(int i = 0; i < STRANGERCOUNT; i++){
            ImageIcon icon = new ImageIcon("./res/tortoise.gif");
            strangerLabel[i] = new JLabel(i + "号陌生人",icon,JLabel.LEFT);
            strangerListPanel.add(strangerLabel[i]);
        }
        JScrollPane strangerListScrollPane = new JScrollPane(strangerListPanel);
        strangerPanel.add(strangerListScrollPane, BorderLayout.CENTER);

        JButton blackListButton2 = new JButton("黑名单");
        strangerPanel.add(blackListButton2, BorderLayout.SOUTH);

        CardLayout cardLayout = new CardLayout();
        this.setLayout(cardLayout);
        this.add(friendPanel, "Card1");
        this.add(strangerPanel, "Card2");
        cardLayout.show(this.getContentPane(),"Card1");

        strangerButton1.addActionListener(e -> cardLayout.show(this.getContentPane(),"Card2"));
        friendButton2.addActionListener(e -> cardLayout.show(this.getContentPane(),"Card1"));

        this.setIconImage(new ImageIcon("./res/duck2.gif").getImage());
        this.setTitle(name + "好友列表");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBounds(800, 600,350,250);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        FriendList friendList = new FriendList("abc"," ");
    }

    public static FriendChat getFriendChat(String name){
        return (FriendChat)friendChatMap.get(name);
    }

    public static String getUserName(){
        return Name;
    }

    public void activeOnlineFriendIcon(String s){
        String[] onlineFriendsName = s.split(" ");
        for(int i = 0; i < onlineFriendsName.length; i++){
            for(int j = 0; j < friendLabel.length; j++){
                if(friendLabel[j].getText().equals(onlineFriendsName[i])){
                    friendLabel[j].setEnabled(true);
                }
            }
        }
    }
    public void activeNewOnlineFriendIcon(String s){
        for(int i = 0; i < friendLabel.length; i++){
            if(friendLabel[i].getText().equals(s)){
                friendLabel[i].setEnabled(true);
            }
        }
    }

    public void addNewFriend(String friendName){
        ImageIcon icon = new ImageIcon("res/"+ 2 +".jpg");
        JLabel label = new JLabel(friendName,icon,JLabel.LEFT);
        label.setEnabled(false);
        label.addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() ==2 && e.getSource() instanceof JLabel){
                    JLabel label = (JLabel) e.getSource();
                    String friendName = label.getText();
                    FriendChat chat = new FriendChat(Name,friendName);
                    friendChatMap.put(Name + "to" + friendName,chat);
                }
            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {
                if(e.getSource() instanceof JLabel){
                    JLabel label = (JLabel) e.getSource();
                    label.setForeground(Color.red);
                }
            }
            @Override
            public void mouseExited(MouseEvent e) {
                if(e.getSource() instanceof JLabel){
                    JLabel label = (JLabel) e.getSource();
                    label.setForeground(Color.black);
                }
            }
        });

        friendListPanel.add(label);
        friendListPanel.revalidate();
        friendListPanel.repaint();
    }
}


/*

 */