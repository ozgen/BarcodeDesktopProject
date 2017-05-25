package com.views.panels;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Ozgen on 5/15/17.
 */
public class ProgressPanel extends JPanel {
    Image progressImage;

    public ProgressPanel() {
        progressImage = Toolkit.getDefaultToolkit().createImage("progress.gif");

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (progressImage != null) {
            g.drawImage(progressImage, 0, 0, this);
        }
    }
}
