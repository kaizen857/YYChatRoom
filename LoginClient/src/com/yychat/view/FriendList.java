package com.yychat.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;

public class FriendList extends JFrame {
    final int FRIENDCOUNT = 50;
    final int STRANGERCOUNT = 20;
    private static String Name;
    private static HashMap friendChatMap = new HashMap<String,FriendChat>();
    public FriendList(String name){
        Name = name;
        JPanel friendPanel = new JPanel(new BorderLayout());
        JButton friendButton1 = new JButton("我的好友");
        JButton strangerButton1 = new JButton("陌生人");

        JButton blackListButton1 = new JButton("黑名单");
        friendPanel.add(friendButton1, BorderLayout.NORTH);
        JLabel[] friendLabel = new JLabel[FRIENDCOUNT];

        JPanel friendListPanel = new JPanel(new GridLayout(FRIENDCOUNT,1));
        for(int i = 0; i < FRIENDCOUNT; i++){
            ImageIcon icon = new ImageIcon("./res/" + (int) (Math.random() * 6) + ".jpg");
            friendLabel[i] = new JLabel(i +"",icon,JLabel.LEFT);

            friendLabel[i].addMouseListener(new MouseListener() {
                public void mouseClicked(MouseEvent e) {
                    if(e.getClickCount() ==2 && e.getSource() instanceof JLabel){
                        JLabel label = (JLabel) e.getSource();
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

            friendListPanel.add(friendLabel[i]);
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
        FriendList friendList = new FriendList("abc");
    }

    public static FriendChat getFriendChat(String name){
        return (FriendChat)friendChatMap.get(name);
    }

    public static String getUserName(){
        return Name;
    }
}
