package com.main;

import com.utils.Utils;
import com.views.FrmMain;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Ozgen on 5/7/17.
 */
public class GuiMain {

    public static void main(String a[]) {
        SplashScreen splashScreen = new SplashScreen(5000);

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                Utils.loadData();
                return null;
            }
        };
        worker.execute();
        splashScreen.showSplashAndExit();

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
            	if(Utils.provinces.size()==0)
            		Utils.loadData();
                FrmMain frmMain = new FrmMain();
                frmMain.setTitle("YKS Trafik");
                frmMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frmMain.pack();
                frmMain.setLocationRelativeTo(null);
                frmMain.setVisible(true);

            }
        });



    }
}
