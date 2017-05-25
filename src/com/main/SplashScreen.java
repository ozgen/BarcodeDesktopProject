package com.main;

/**
 * Created by Ozgen on 5/5/17.
 */

import java.awt.*;
import java.net.URL;
import javax.swing.*;

public class SplashScreen extends JWindow {
    private int duration;

    public SplashScreen(int d) {
        duration = d;
    }

    // A simple little method to show a title screen in the center
    // of the screen for the amount of time given in the constructor
    public void showSplash() {
        JPanel content = (JPanel) getContentPane();
        content.setBackground(Color.white);

        // Set the window's bounds, centering the window
        int width = 450;
        int height = 150;
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screen.width - width) / 2;
        int y = (screen.height - height) / 2;
        setBounds(x, y, width, height);

        // Build the splash screen
        URL urlImage  = getClass().getClassLoader().getResource("logo.jpg");
        JLabel label = new JLabel(new ImageIcon(urlImage));
        JLabel copyrt = new JLabel("Copyright 2017, YKS Trafik 1.0.0.v", JLabel.CENTER);
        copyrt.setFont(new Font("Sans-Serif", Font.BOLD, 12));
        content.add(label, BorderLayout.CENTER);
        content.add(copyrt, BorderLayout.SOUTH);
        Color oraRed = new Color(156, 20, 20, 255);
        content.setBorder(BorderFactory.createLineBorder(oraRed, 10));

        // Display it
        setVisible(true);

        // Wait a little while, maybe while loading resources
        try {
            Thread.sleep(duration);
        } catch (Exception e) {
        }

        setVisible(false);
    }

    public void showSplashAndExit() {
        showSplash();
        this.setVisible(false);
    }

    public static void main(String[] args) {
        // Throw a nice little title page up on the screen first
       SplashScreen splash = new SplashScreen(10000);
        // Normally, we'd call splash.showSplash() and get on with the program.
        // But, since this is only a test...
        splash.showSplashAndExit();


    }
}