package com.views.panels;

import com.models.IBarcodeModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Vector;

/**
 * Created by Ozgen on 5/14/17.
 */
public class PnlResults extends JPanel implements IPnlResults {

    //headers for the table
    String[] columns = new String[]{
            "Il", "Plaka Kodu", "Barkod Numarası"
    };

    private List barcodes;
    private JTable table;


    //create table model with data
    DefaultTableModel tableModel = new DefaultTableModel(null, columns);

    private void jInit() {
        fillModel();
        table = new JTable(tableModel);
        this.setLayout(new BorderLayout(0, 1));
        this.add(new JScrollPane(table), BorderLayout.CENTER);
        this.setMinimumSize(new Dimension(450, 200));
    }

    public PnlResults(List barcodes) {
        this.barcodes = barcodes;
        jInit();
    }

    @Override
    public List getBarcodes() {
        return barcodes;
    }

    @Override
    public void setBarcodes(List barcodes) {
        this.barcodes = barcodes;
        fillModel();
        DefaultTableModel tbModel = (DefaultTableModel) table.getModel();
        tbModel.fireTableDataChanged();
    }

    @Override
    public JPanel asPanel() {
        return this;
    }

    private void fillModel() {
        if (barcodes == null || barcodes.size() == 0) {
            tableModel.setRowCount(0);
            return;
        }
        for (Object o : barcodes) {
            IBarcodeModel model = (IBarcodeModel) o;
            Object[] obs = {model.getProvince(), model.getProvinceCode(), model.getBarcodeNumber()};
            tableModel.addRow(obs);
        }

    }

    public List getData() {
        DefaultTableModel tbModel = (DefaultTableModel) table.getModel();
        return tbModel.getDataVector();
    }


}
