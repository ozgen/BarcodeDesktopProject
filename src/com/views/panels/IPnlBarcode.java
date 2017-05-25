package com.views.panels;

import com.models.IBarcodeModel;

import javax.swing.*;

/**
 * Created by Ozgen on 5/7/17.
 */
public interface IPnlBarcode {

    IBarcodeModel getBarcodeModel();

    JPanel asPanel();

    JPanel getProgressPanel();
}
