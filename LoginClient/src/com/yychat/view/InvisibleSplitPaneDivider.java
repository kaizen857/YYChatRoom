package com.yychat.view;

import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import java.awt.*;
// Custom UI that uses the InvisibleSplitPaneDivider
class InvisibleSplitPaneDivider extends BasicSplitPaneDivider {
    public InvisibleSplitPaneDivider(BasicSplitPaneUI ui) {
        super(ui);
        // We don't want any border on the divider itself
        setBorder(null);
        // The background color can be set if needed, but paint() override is key
        // setBackground(new Color(0,0,0,0)); // Transparent background
    }

    @Override
    public void paint(Graphics g) {
        // Override paint to do nothing, making the divider visually disappear.
        // The area will still be interactive due to the dividerSize.
        // If you want to ensure it's "transparent" by painting the parent's background:
        // Graphics2D g2d = (Graphics2D) g.create();
        // g2d.setColor(splitPane.getBackground()); // Or a specific transparent color
        // g2d.fillRect(0, 0, getWidth(), getHeight());
        // g2d.dispose();
        // For true invisibility, just leave this method empty.
    }

    // Optional: Ensure the divider has a minimum size for interaction if desired,
    // though JSplitPane's dividerSize property is the main controller.
}
