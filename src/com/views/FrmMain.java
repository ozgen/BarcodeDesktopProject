package com.views;

import com.controllers.IBarcodeCtrl;
import com.utils.Utils;
import com.views.panels.IPnlBarcode;
import com.views.panels.PnlBarcode;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * Created by Ozgen on 5/7/17.
 */
public class FrmMain extends JFrame {
    private IPnlBarcode pnlBarcode;
    private IBarcodeCtrl controller;

    public FrmMain() {
        this.setMinimumSize(new Dimension(460, 300));
        //this.setMaximumSize(new Dimension(460, 160));
        controller = Utils.createBarcodeCtrl();
        pnlBarcode = new PnlBarcode(controller);

        this.setLayout(new BorderLayout());
        this.add(pnlBarcode.asPanel());


    }


}



