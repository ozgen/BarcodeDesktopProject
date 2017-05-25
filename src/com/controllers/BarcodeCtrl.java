package com.controllers;

import com.dao.IDao;
import com.models.IBarcodeModel;
import com.utils.IBrcdGenerator;
import com.utils.Utils;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ozgen on 5/8/17.
 */
public class BarcodeCtrl implements IBarcodeCtrl {


    private IDao<IBarcodeModel> dao = Utils.createBarcodeDao();
    boolean isFirstSave = false;
    private IBrcdGenerator generator = Utils.createBarcodeGenerator();
    private List barcodes = new ArrayList<>();

    @Override
    public boolean createBarcode(IBarcodeModel model) {
        if (model == null) {
            return false;
        }
        boolean isSaveOrUpdate = false;
        IBarcodeModel barcodeModel = dao.getLast(model.getProvinceCode());
        if (barcodeModel != null && barcodeModel.getBarcodeNumber() != null) {
            barcodes.addAll(generator.createBarcodes(model, barcodeModel.getBarcodeNumber()));
        } else {
            isFirstSave = true;
            barcodes.addAll(generator.createBarcodes(model, null));
        }

        if (isFirstSave)
            isSaveOrUpdate = dao.save((IBarcodeModel) barcodes.get(barcodes.size() - 1));
        else
            isSaveOrUpdate = dao.update((IBarcodeModel) barcodes.get(barcodes.size() - 1));

       
        return isSaveOrUpdate;
    }

    @Override
    public List getcreatedBarcodes() {
        return barcodes;
    }

    @Override
    public boolean printBarcodes(String path) {

        if (barcodes == null || barcodes.size() == 0)
            return false;

        JasperPrint jasperPrint = generator.createJasperPrint(barcodes);

        try {
            if (jasperPrint != null) {
                JasperExportManager.exportReportToPdfFile(jasperPrint, path + "/rapor.pdf");
                return true;
            } else
                return false;
        } catch (JRException e) {
            e.printStackTrace();
            return false;
        }


    }

    @Override
    public boolean saveBarcodeAsImages(String path) {
        if (barcodes == null)
            return false;
        int cnt = 0;
        for (Object model : barcodes) {
            IBarcodeModel barcodeModel = (IBarcodeModel) model;
            BufferedImage image = generator.createBarcode(barcodeModel.getBarcodeNumber());
            File outputfile = new File(path + "/" + barcodeModel.getBarcodeNumber() + ".jpg");
            try {
                ImageIO.write(image, "jpg", outputfile);
                cnt++;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return cnt == barcodes.size();
    }

    @Override
    public void clearList() {
        barcodes = new ArrayList<>();
    }
}
