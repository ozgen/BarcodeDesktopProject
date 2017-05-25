package com.utils;

import com.models.IBarcodeModel;
import net.sf.jasperreports.engine.JasperPrint;

import java.awt.image.BufferedImage;
import java.util.List;

/**
 * Created by Ozgen on 5/8/17.
 */
public interface IBrcdGenerator {

    List createBarcodes(IBarcodeModel model, String lastBarcode);

    BufferedImage createBarcode(String barcodeNumber);

    JasperPrint createJasperPrint(List barcodes);
}
