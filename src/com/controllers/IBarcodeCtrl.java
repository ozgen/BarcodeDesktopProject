package com.controllers;

import com.models.IBarcodeModel;

import java.util.List;

/**
 * Created by Ozgen on 5/8/17.
 */
public interface IBarcodeCtrl {



    boolean createBarcode(IBarcodeModel model);
    List getcreatedBarcodes();
    boolean printBarcodes(String path);
    boolean saveBarcodeAsImages(String path);
    void clearList();
}
