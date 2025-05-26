package com.yychat.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;

class ChatMessageBubble extends JPanel {
    private JLabel messageLabel;
    private JLabel timeLabel;
    private RoundedAvatar avatar;
    private ChatMessageData data;
    private boolean isSelf;

    public ChatMessageBubble(ChatMessageData data) {
        this.data = data;
        this.isSelf = data.senderType == ChatMessageData.SenderType.SELF;

        setLayout(new GridBagLayout());
        setOpaque(false); // Important for bubble-like appearance against chat background
        setBorder(new EmptyBorder(5, 10, 5, 10));

        avatar = new RoundedAvatar(30, data.avatarColor);
        messageLabel = new JLabel("<html><body style='width: 200px;'>" + data.message + "</body></html>"); // Basic HTML for wrapping
        messageLabel.setOpaque(true);
        messageLabel.setBorder(new EmptyBorder(8, 12, 8, 12));



        timeLabel = new JLabel();
        timeLabel.setFont(new Font("微软雅黑", Font.PLAIN, 9));
        timeLabel.setForeground(Color.GRAY);
        timeLabel.setVisible(false); // Initially hidden

        GridBagConstraints gbc = new GridBagConstraints();

        // Configure based on sender
        if (isSelf) {
            messageLabel.setBackground(new Color(0xC9E6FF)); // Light blue for self
            // Message first, then avatar
            gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 1.0; gbc.anchor = GridBagConstraints.EAST;
            add(timeLabel, gbc); // Time above message
            gbc.gridy = 1;
            add(messageLabel, gbc);
            gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 0; gbc.insets = new Insets(0, 5, 0, 0); gbc.anchor = GridBagConstraints.NORTHEAST;
            add(avatar, gbc);
        } else {
            messageLabel.setBackground(Color.WHITE); // White for other
            // Avatar first, then message
            gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0; gbc.insets = new Insets(0, 0, 0, 5); gbc.anchor = GridBagConstraints.NORTHWEST;
            add(avatar, gbc);
            gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0; gbc.anchor = GridBagConstraints.WEST;
            add(timeLabel, gbc); // Time above message
            gbc.gridy = 1;
            add(messageLabel, gbc);
        }

        // Make the bubble focusable to show time
        setFocusable(true);
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                timeLabel.setText(sdf.format(data.timestamp));
                timeLabel.setVisible(true);
                revalidate();
                repaint();
            }

            @Override
            public void focusLost(FocusEvent e) {
                timeLabel.setVisible(false);
                revalidate();
                repaint();
            }
        });
        // Allow click to gain focus
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                requestFocusInWindow();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                requestFocusInWindow();
            }
        });
    }
}