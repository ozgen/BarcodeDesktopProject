package com.utils;

import com.models.BarcodeModel;
import com.models.IBarcodeModel;
import com.models.PrintModel;

/**
 * Created by Ozgen on 5/5/17.
 */
public class ModelGenerator {


    public static IBarcodeModel createBarcodeModel() {
        return new BarcodeModel();
    }

    public static PrintModel createPrintModel() {
        return new PrintModel();
    }
}
