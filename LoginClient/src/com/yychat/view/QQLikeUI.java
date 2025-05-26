package com.yychat.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class QQLikeUI extends JFrame {

    private AccountPanel accountPanel;
    private FriendListPanel friendListPanel;
    private ChatPanel chatPanel;

    private FriendData currentUser; // Currently logged-in user
    private FriendData currentChatFriend; // Currently chatting with

    public QQLikeUI(String userName,String userAvatarPath) {
        currentUser = new FriendData("user0", userName, userAvatarPath, "", "");

        setTitle("Java Swing QQ-Like Chat - Invisible Dividers");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(850, 650);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        accountPanel = new AccountPanel(currentUser);
        friendListPanel = new FriendListPanel(this);
        chatPanel = new ChatPanel(this, currentUser);

        accountPanel.setMinimumSize(new Dimension(80, 0));
        friendListPanel.setMinimumSize(new Dimension(100, 0)); // Adjusted min width
        chatPanel.setMinimumSize(new Dimension(150, 0));    // Adjusted min width


        // --- Configure Left Split Pane ---
        JSplitPane leftSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, accountPanel, friendListPanel);
        leftSplitPane.setUI(new InvisibleSplitPaneUI()); // Apply custom UI
        leftSplitPane.setContinuousLayout(true);       // Ensure real-time resizing (default)
        leftSplitPane.setOneTouchExpandable(false);    // No expander buttons
        leftSplitPane.setDividerSize(7);               // Width of the invisible draggable area
        leftSplitPane.setDividerLocation(90);
        leftSplitPane.setResizeWeight(0.0);            // AccountPanel size prioritized

        // --- Configure Main Split Pane ---
        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftSplitPane, chatPanel);
        mainSplitPane.setUI(new InvisibleSplitPaneUI()); // Apply custom UI
        mainSplitPane.setContinuousLayout(true);        // Ensure real-time resizing
        mainSplitPane.setOneTouchExpandable(false);     // No expander buttons
        mainSplitPane.setDividerSize(7);                // Width of the invisible draggable area
        mainSplitPane.setDividerLocation(280);
        mainSplitPane.setResizeWeight(0.3);             // ChatPanel gets 70% of resize delta

        add(mainSplitPane, BorderLayout.CENTER);

        friendListPanel.loadFriends(createDummyFriends());
        chatPanel.setActiveChat(null); // Hides input area initially
    }

    private List<FriendData> createDummyFriends() {
        List<FriendData> friends = new ArrayList<>();
        friends.add(new FriendData("friend1", "Alice", "./res/0.jpg", "Hey there!", "10:30"));
        friends.add(new FriendData("friend2", "Bob", "./res/3.jpg", "See you soon.", "09:15"));
        friends.add(new FriendData("friend3", "Charlie", "./res/4.jpg", "Lunch tomorrow?", "Yesterday"));
        friends.add(new FriendData("friend4", "Diana", "./res/5.jpg", "Good night!", "02/05"));
//        for(int i=5; i<15; i++){
//            friends.add(new FriendData("friend"+i, "User "+i, new Color((int)(Math.random() * 0xFFFFFF)), "Random msg "+i, "01/05"));
//        }
        return friends;
    }

    public void switchChatSession(FriendData friend) {
        this.currentChatFriend = friend;
        chatPanel.setActiveChat(friend);
    }

    public FriendData getCurrentUser() {
        return currentUser;
    }

    public FriendData getCurrentChatFriend() {
        return currentChatFriend;
    }


    // --- Inner Panels ---
    class AccountPanel extends JPanel {
        private RoundedAvatar avatarLabel;
        private JButton settingsButton;

        public AccountPanel(FriendData user) {
            setLayout(new GridBagLayout());
            setBackground(new Color(0xF0F0F0));
            setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.LIGHT_GRAY));

            GridBagConstraints c = new GridBagConstraints();

            avatarLabel = new RoundedAvatar(60, user.avatarPath);
            c.gridx = 0;
            c.gridy = 0;
            c.anchor = GridBagConstraints.NORTH;
            c.insets = new Insets(10, 10, 10, 10);
            add(avatarLabel, c);

            settingsButton = new JButton("⚙");
            settingsButton.setToolTipText("Settings");
            settingsButton.setFont(new Font("SansSerif", Font.PLAIN, 20));
            settingsButton.setMargin(new Insets(2, 2, 2, 2));
            settingsButton.addActionListener(e -> {
                System.out.println("Settings button clicked");
                JOptionPane.showMessageDialog(this, "TODO: Implement Settings");
            });
            c.gridx = 0;
            c.gridy = 1;
            c.weighty = 1.0;
            c.anchor = GridBagConstraints.SOUTH;
            c.insets = new Insets(10, 10, 10, 10);
            add(settingsButton, c);
        }
    }

    class FriendListPanel extends JPanel {
        private JTextField searchField;
        private JButton addFriendButton;
        private JPanel friendListContainer;
        private JScrollPane scrollPane;
        private QQLikeUI mainFrame;

        public FriendListPanel(QQLikeUI mainFrame) {
            this.mainFrame = mainFrame;
            setLayout(new GridBagLayout());
            setBackground(new Color(0xF8F8F8));
            setBorder(BorderFactory.createMatteBorder(0,0,0,1, Color.LIGHT_GRAY));

            GridBagConstraints gbc = new GridBagConstraints();

            // Top: Search and Add button
            JPanel topPanel = new JPanel(new GridBagLayout());
            searchField = new JTextField(10);
            searchField.setToolTipText("Search friends");
            searchField.addActionListener(e -> {
                // TODO: Implement search functionality
                System.out.println("Search initiated: " + searchField.getText());
            });

            addFriendButton = new JButton("+");
            addFriendButton.setToolTipText("Add friend");
            addFriendButton.addActionListener(e -> {
                // TODO: Implement add friend functionality
                System.out.println("Add friend button clicked");
                JOptionPane.showMessageDialog(this, "TODO: Implement Add Friend");
            });

            GridBagConstraints topGbc = new GridBagConstraints();
            topGbc.gridx = 0; topGbc.gridy = 0; topGbc.weightx = 1.0; topGbc.fill = GridBagConstraints.HORIZONTAL; topGbc.insets = new Insets(5,5,2,5);
            topPanel.add(searchField, topGbc);
            topGbc.gridx = 1; topGbc.weightx = 0; topGbc.fill = GridBagConstraints.NONE; topGbc.insets = new Insets(5,0,2,5);
            topPanel.add(addFriendButton, topGbc);

            gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
            add(topPanel, gbc);


            // Bottom: Friend List
            friendListContainer = new JPanel();
            friendListContainer.setLayout(new BoxLayout(friendListContainer, BoxLayout.Y_AXIS));
            friendListContainer.setBackground(Color.WHITE);

            scrollPane = new JScrollPane(friendListContainer);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scrollPane.setBorder(null);

            // Scrollbar hide/show on hover (Basic - AS_NEEDED is usually sufficient)
            // For more advanced hiding, custom UI or listeners on JScrollBar visibility are needed.
            // This is a simplified version of the request.
            scrollPane.getVerticalScrollBar().addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    // scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    // if (!scrollPane.getBounds().contains(e.getPoint())) { // check if still over scrollpane
                    //    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
                    //}
                }
            });


            gbc.gridx = 0; gbc.gridy = 1; gbc.weighty = 1.0; gbc.fill = GridBagConstraints.BOTH;
            add(scrollPane, gbc);
        }

        public void loadFriends(List<FriendData> friends) {
            friendListContainer.removeAll();
            for (FriendData friend : friends) {
                FriendEntry entry = new FriendEntry(friend, mainFrame);
                friendListContainer.add(entry);
                friendListContainer.add(Box.createRigidArea(new Dimension(0,1))); // Small separator
            }
            friendListContainer.revalidate();
            friendListContainer.repaint();
        }

        class FriendEntry extends JPanel {
            private FriendData friend;
            private QQLikeUI mainFrame;

            public FriendEntry(FriendData friend, QQLikeUI mainFrame) {
                this.friend = friend;
                this.mainFrame = mainFrame;
                setLayout(new GridBagLayout());
                setBorder(new EmptyBorder(5, 5, 5, 5));
                setBackground(Color.WHITE);
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

                GridBagConstraints c = new GridBagConstraints();

                RoundedAvatar avatar = new RoundedAvatar(40, friend.avatarPath);
                c.gridx = 0; c.gridy = 0; c.gridheight = 2; c.anchor = GridBagConstraints.WEST; c.insets = new Insets(0,0,0,10);
                add(avatar, c);

                JLabel nameLabel = new JLabel(friend.name);
                nameLabel.setFont(new Font("微软雅黑", Font.BOLD, 14));
                c.gridx = 1; c.gridy = 0; c.gridheight = 1; c.weightx = 1.0; c.anchor = GridBagConstraints.NORTHWEST; c.insets = new Insets(0,0,0,0);
                add(nameLabel, c);

                JLabel lastMessageLabel = new JLabel(friend.lastMessage);
                lastMessageLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
                lastMessageLabel.setForeground(Color.GRAY);
                c.gridx = 1; c.gridy = 1; c.anchor = GridBagConstraints.SOUTHWEST;
                add(lastMessageLabel, c);

                JLabel dateLabel = new JLabel(friend.lastMessageDate);
                dateLabel.setFont(new Font("微软雅黑", Font.PLAIN, 10));
                dateLabel.setForeground(Color.LIGHT_GRAY);
                c.gridx = 2; c.gridy = 0; c.weightx = 0; c.anchor = GridBagConstraints.NORTHEAST;
                add(dateLabel, c);

                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        // TODO: Implement friend click to open chat
                        System.out.println("Clicked on friend: " + friend.name);
                        mainFrame.switchChatSession(friend);
                        // Highlight selected friend (optional)
                        for(Component comp : getParent().getComponents()){
                            if(comp instanceof FriendEntry){
                                comp.setBackground(Color.WHITE);
                            }
                        }
                        setBackground(new Color(0xE0E0E0)); // Light selection color
                    }
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        if(getBackground() != new Color(0xE0E0E0)) // Don't change if selected
                            setBackground(new Color(0xF0F0F0));
                    }
                    @Override
                    public void mouseExited(MouseEvent e) {
                        if(getBackground() != new Color(0xE0E0E0))
                            setBackground(Color.WHITE);
                    }
                });
            }
        }
    }

    class ChatPanel extends JPanel {
        private JLabel friendNameLabel;
        private JPanel messageDisplayArea;
        private JScrollPane messageScrollPane;
        private JTextArea inputTextArea;
        private JButton emojiButton, fileButton, imageButton, sendButton;
        private JPanel initialEmptyPanel;
        private CardLayout cardLayout;
        private JPanel contentPanel;
        private QQLikeUI mainFrame;
        private FriendData currentUser;
        private JPanel inputPanelContainer; // Field for the input area container


        public ChatPanel(QQLikeUI mainFrame, FriendData currentUser) {
            this.mainFrame = mainFrame;
            this.currentUser = currentUser;
            setLayout(new GridBagLayout());
            setBackground(new Color(0xF5F5F5));

            GridBagConstraints gbc = new GridBagConstraints();

            friendNameLabel = new JLabel(" ");
            friendNameLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
            friendNameLabel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0,0,1,0,Color.LIGHT_GRAY),
                    new EmptyBorder(10,10,10,10)
            ));
            friendNameLabel.setHorizontalAlignment(SwingConstants.LEFT);
            gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
            add(friendNameLabel, gbc);

            cardLayout = new CardLayout();
            contentPanel = new JPanel(cardLayout);
            initialEmptyPanel = new JPanel(new GridBagLayout()) {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g.create();
                    int size = Math.min(getWidth(), getHeight()) / 2;
                    int x = (getWidth() - size) / 2;
                    int y = (getHeight() - size) / 2;
                    g2d.setColor(new Color(0xFFE0E0));
                    g2d.fillRect(0,0, getWidth(), getHeight());
                    g2d.setColor(Color.RED);
                    g2d.fillRect(x, y, size, size);
                    g2d.setColor(Color.WHITE);
                    g2d.setFont(new Font("微软雅黑", Font.BOLD, size/4));
                    FontMetrics fm = g2d.getFontMetrics();
                    String text = "LOGO";
                    g2d.drawString(text, x + (size - fm.stringWidth(text))/2, y + (size + fm.getAscent())/2);
                    g2d.dispose();
                }
            };
            initialEmptyPanel.setBackground(new Color(0xF5F5F5));

            JPanel chatMessagesPanel = new JPanel(new BorderLayout());
            chatMessagesPanel.setOpaque(false);
            messageDisplayArea = new JPanel();
            messageDisplayArea.setLayout(new BoxLayout(messageDisplayArea, BoxLayout.Y_AXIS));
            messageDisplayArea.setBackground(new Color(0xF5F5F5));
            messageDisplayArea.setBorder(new EmptyBorder(10,10,10,10));
            messageScrollPane = new JScrollPane(messageDisplayArea);
            messageScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            messageScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            messageScrollPane.setBorder(null);
            messageScrollPane.getViewport().setBackground(new Color(0xF5F5F5));
            chatMessagesPanel.add(messageScrollPane, BorderLayout.CENTER);
            contentPanel.add(initialEmptyPanel, "INITIAL");
            contentPanel.add(chatMessagesPanel, "CHAT");
            gbc.gridx = 0; gbc.gridy = 1; gbc.weighty = 1.0; gbc.fill = GridBagConstraints.BOTH;
            add(contentPanel, gbc);

            inputPanelContainer = new JPanel(new GridBagLayout());
            inputPanelContainer.setBorder(BorderFactory.createMatteBorder(1,0,0,0,Color.LIGHT_GRAY));
            inputPanelContainer.setBackground(Color.WHITE);

            GridBagConstraints inputGbc = new GridBagConstraints();

            JPanel toolbarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
            toolbarPanel.setOpaque(false);
            emojiButton = new JButton("\uD83D\uDE00"); fileButton = new JButton("📁"); imageButton = new JButton("🖼️");
            emojiButton.addActionListener(e -> { System.out.println("Emoji clicked"); });
            fileButton.addActionListener(e -> { System.out.println("File clicked"); });
            imageButton.addActionListener(e -> { System.out.println("Image clicked"); });
            styleToolbarButton(emojiButton); styleToolbarButton(fileButton); styleToolbarButton(imageButton);
            toolbarPanel.add(emojiButton); toolbarPanel.add(fileButton); toolbarPanel.add(imageButton);
            inputGbc.gridx = 0; inputGbc.gridy = 0; inputGbc.gridwidth = 2; inputGbc.fill = GridBagConstraints.HORIZONTAL; inputGbc.insets = new Insets(5,5,5,5);
            inputPanelContainer.add(toolbarPanel, inputGbc);

            inputTextArea = new JTextArea(String.valueOf(3));
            inputTextArea.setFont(new Font("微软雅黑", Font.PLAIN, 14));
            inputTextArea.setLineWrap(true); inputTextArea.setWrapStyleWord(true);
            JScrollPane inputScrollPane = new JScrollPane(inputTextArea);
            inputScrollPane.setBorder(BorderFactory.createEmptyBorder());
            inputScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            inputGbc.gridx = 0; inputGbc.gridy = 1; inputGbc.gridwidth = 2; inputGbc.weightx = 1.0; inputGbc.weighty = 1.0; inputGbc.fill = GridBagConstraints.BOTH; inputGbc.insets = new Insets(0,10,5,10);
            inputPanelContainer.add(inputScrollPane, inputGbc);

            sendButton = new JButton("发送");
            sendButton.setBackground(new Color(0x007AFF)); sendButton.setForeground(Color.WHITE);
            sendButton.setEnabled(false); sendButton.setOpaque(true); sendButton.setBorderPainted(false);
            sendButton.setFont(new Font("微软雅黑", Font.PLAIN, 13)); sendButton.setPreferredSize(new Dimension(70, 30));
            inputGbc.gridx = 1; inputGbc.gridy = 2; inputGbc.gridwidth = 1; inputGbc.weightx = 0; inputGbc.weighty = 0; inputGbc.fill = GridBagConstraints.NONE; inputGbc.anchor = GridBagConstraints.SOUTHEAST; inputGbc.insets = new Insets(5,5,10,10);
            inputPanelContainer.add(sendButton, inputGbc);

            gbc.gridx = 0; gbc.gridy = 2; gbc.weighty = 0; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
            add(inputPanelContainer, gbc);

            inputTextArea.getDocument().addDocumentListener(new DocumentListener() {
                @Override public void insertUpdate(DocumentEvent e) { checkInput(); }
                @Override public void removeUpdate(DocumentEvent e) { checkInput(); }
                @Override public void changedUpdate(DocumentEvent e) { checkInput(); }
            });
            sendButton.addActionListener(e -> sendMessage());
            inputTextArea.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        if (e.isShiftDown()) {
                            inputTextArea.append("\n");
                        } else {
                            e.consume();
                            if (sendButton.isEnabled()) {
                                sendMessage();
                            }
                        }
                    }
                }
            });
        }

        private void styleToolbarButton(JButton button) {
            button.setOpaque(false);
            button.setContentAreaFilled(false);
            button.setBorderPainted(false);
            button.setFont(new Font("SansSerif", Font.PLAIN, 20));
            button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }


        private void checkInput() {
            String text = inputTextArea.getText().trim();
            if (text.isEmpty()) {
                sendButton.setEnabled(false);
                sendButton.setForeground(Color.GRAY);
            } else {
                sendButton.setEnabled(true);
                sendButton.setForeground(Color.WHITE);
            }
        }

        private void sendMessage() {
            //TODO: 发送信息
            String messageText = inputTextArea.getText().trim();
            if (!messageText.isEmpty() && mainFrame.getCurrentChatFriend() != null) {
                FriendData currentChat = mainFrame.getCurrentChatFriend();
                System.out.println("Sending to " + currentChat.name + ": " + messageText);

                ChatMessageData msgData = new ChatMessageData(
                        ChatMessageData.SenderType.SELF,
                        messageText,
                        new Date(),
                        currentUser.avatarPath
                );
                addMessageBubble(msgData);
                currentChat.chatHistory.add(msgData);

                inputTextArea.setText("");
                Timer timer = new Timer(1000, ae -> {
                    ChatMessageData replyData = new ChatMessageData(
                            ChatMessageData.SenderType.OTHER,
                            "Got it: \"" + messageText + "\"",
                            new Date(),
                            currentChat.avatarPath
                    );
                    addMessageBubble(replyData);
                    currentChat.chatHistory.add(replyData);
                });
                timer.setRepeats(false);
                timer.start();
            }
        }

        public void setActiveChat(FriendData friend) {
            if (friend == null) {
                friendNameLabel.setText(" ");
                cardLayout.show(contentPanel, "INITIAL");
                inputTextArea.setEnabled(false);
                checkInput();
                if (inputPanelContainer != null) {
                    inputPanelContainer.setVisible(false);
                }
            } else {
                friendNameLabel.setText(friend.name);
                messageDisplayArea.removeAll();
                for (ChatMessageData msg : friend.chatHistory) {
                    addMessageBubble(msg);
                }
                cardLayout.show(contentPanel, "CHAT");
                if (inputPanelContainer != null) {
                    inputPanelContainer.setVisible(true);
                }
                inputTextArea.setEnabled(true);
                messageDisplayArea.revalidate();
                messageDisplayArea.repaint();
                scrollToBottom();
                inputTextArea.requestFocusInWindow();
            }
            this.revalidate();
            this.repaint();
        }

        private void addMessageBubble(ChatMessageData messageData) {
            ChatMessageBubble bubble = new ChatMessageBubble(messageData);
            JPanel wrapper = new JPanel(new BorderLayout());
            wrapper.setOpaque(false);

            if (messageData.senderType == ChatMessageData.SenderType.SELF) {
                wrapper.add(bubble, BorderLayout.EAST);
            } else {
                wrapper.add(bubble, BorderLayout.WEST);
            }
            messageDisplayArea.add(wrapper);
            messageDisplayArea.add(Box.createRigidArea(new Dimension(0, 5)));

            messageDisplayArea.revalidate();
            messageDisplayArea.repaint();
            scrollToBottom();
        }

        private void scrollToBottom() {
            SwingUtilities.invokeLater(() -> {
                JScrollBar verticalBar = messageScrollPane.getVerticalScrollBar();
                if (verticalBar != null) {
                    verticalBar.setValue(verticalBar.getMaximum());
                }
            });
        }
    }


    public static void main(String[] args) {
        // Set Look and Feel (Optional, for better aesthetics)
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            new QQLikeUI("kaizen","./res/1.jpg").setVisible(true);
        });
    }
}