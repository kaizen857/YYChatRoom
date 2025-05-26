package com.yychat.view;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

class RoundedAvatar extends JPanel {
    private Image image;
    private Color placeholderColor;
    private int diameter;

    public RoundedAvatar(int diameter, String imagePath) {
        this.diameter = Math.min(diameter, 80); // Max 80px
        setPreferredSize(new Dimension(this.diameter, this.diameter));
        setOpaque(false);
        try {
            if (imagePath != null) {
                this.image = new ImageIcon(imagePath).getImage();
            }
        } catch (Exception e) {
            this.image = null; // Fallback to color if image fails
            e.printStackTrace();
        }
        if (this.image == null) {
            System.out.println("Image is null");
            this.placeholderColor = new Color((int)(Math.random() * 0xFFFFFF));
        }
    }

    public RoundedAvatar(int diameter, Color placeholderColor) {
        this.diameter = Math.min(diameter, 80);
        this.placeholderColor = placeholderColor;
        setPreferredSize(new Dimension(this.diameter, this.diameter));
        setOpaque(false);
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Ellipse2D.Float ellipse = new Ellipse2D.Float(0, 0, diameter - 1, diameter - 1);
        g2d.setClip(ellipse);

        if (image != null) {
            g2d.drawImage(image, 0, 0, diameter, diameter, this);
        } else {
            g2d.setColor(placeholderColor);
            g2d.fillRect(0, 0, diameter, diameter);
        }
        g2d.dispose();
    }
}
