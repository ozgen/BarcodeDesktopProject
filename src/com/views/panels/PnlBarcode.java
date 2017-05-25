package com.views.panels;

import com.controllers.IBarcodeCtrl;
import com.models.IBarcodeModel;
import com.utils.ModelGenerator;
import com.utils.Utils;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileSystemView;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.concurrent.ExecutionException;

/**
 * Created by Ozgen on 5/7/17.
 */
public class PnlBarcode extends JPanel implements IPnlBarcode {

    private String TITLE = "BARCODE GENERATOR";
    private JLabel lblProvinceCombo = new JLabel("Il seciniz");
    private JComboBox cmbProvince = new JComboBox(new DefaultComboBoxModel(Utils.provinces.toArray()));
    private JLabel lblCount = new JLabel("Miktar Giriniz");
    private JFormattedTextField txtCount;
    Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
    Border borderBtn = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
    TitledBorder title = BorderFactory.createTitledBorder(lowerEtched, TITLE);

    private JButton btnCreateBarcode = new JButton("Barkod Olustur");
    private JButton btnPrint = new JButton("Yazdir");
    private JButton btnImage = new JButton("Resim Kaydet");
    private JButton btnClean = new JButton("Temizle");
    private JPanel pnlLabels = new JPanel(new GridLayout(0, 1));
    private JPanel pnlData = new JPanel(new GridLayout(0, 1));
    private JPanel pnlBtn = new JPanel(new FlowLayout());
    private JPanel pnlMain = new JPanel(new BorderLayout());

    private Image imgBtnBrcd;
    private Image imgBtnPrint;
    private Image imgBtnImage;
    private Image imgBtnClean;

    private IPnlResults pnlResults;

    private IBarcodeCtrl barcodeCtrl;

    private IBarcodeModel updatedModel = null;

    public PnlBarcode(IBarcodeCtrl ctrl) {
        this.barcodeCtrl = ctrl;
        jinit();
    }


    private void jinit() {
        try {

            imgBtnBrcd = ImageIO.read(getClass().getClassLoader().getResource("brcdbtn.png"));
            imgBtnPrint = ImageIO.read(getClass().getClassLoader().getResource("icnpdf.png"));
            imgBtnImage = ImageIO.read(getClass().getClassLoader().getResource("icnimage.png"));
            imgBtnClean = ImageIO.read(getClass().getClassLoader().getResource("imgclean.png"));

            NumberFormat format = NumberFormat.getInstance();
            NumberFormatter formatter = new NumberFormatter(format);
            formatter.setValueClass(Integer.class);
            formatter.setMinimum(0);
            formatter.setMaximum(Integer.MAX_VALUE);
            formatter.setAllowsInvalid(false);
            // If you want the value to be committed on each keystroke instead of focus lost
            formatter.setCommitsOnValidEdit(true);
            txtCount = new JFormattedTextField(formatter);
        } catch (Exception e) {
            e.printStackTrace();
        }

        pnlMain.setMinimumSize(new Dimension(450, 150));
        //this.setMaximumSize(new Dimension(450, 150));
        this.setLayout(new BorderLayout(0, 1));
        this.setBorder(title);
        pnlLabels.add(lblProvinceCombo);
        pnlLabels.add(lblCount);

        pnlData.add(cmbProvince);
        pnlData.add(txtCount);


        btnCreateBarcode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                IBarcodeModel model = getBarcodeModel();
                if (model == null) {
                    JOptionPane.showMessageDialog(null, "Lütfen zorunlu alanları doldurunuz!");
                    return;
                }

                boolean isSuccess = barcodeCtrl.createBarcode(model);
                if (pnlResults == null) {
                    pnlResults = new PnlResults(barcodeCtrl.getcreatedBarcodes());
                    PnlBarcode.this.add(pnlResults.asPanel(), BorderLayout.CENTER);
                    PnlBarcode.this.setMinimumSize(new Dimension(450, 500));
                    PnlBarcode.this.revalidate();
                    PnlBarcode.this.repaint();
                } else
                    pnlResults.setBarcodes(barcodeCtrl.getcreatedBarcodes());


                if (isSuccess)
                    JOptionPane.showMessageDialog(null, "Barkod/lar basarılı sekilde olusturulmustur");
                else
                    JOptionPane.showMessageDialog(null, "Barkod/lar olusumunda Hata!!!");


            }
        });

        btnImage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (barcodeCtrl.getcreatedBarcodes() == null || barcodeCtrl.getcreatedBarcodes().size() == 0)
                    return;
                final JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
                jfc.setDialogTitle("Kaydedeceginiz klasoru seciniz : ");
                jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

                // int returnValue = jfc.showSaveDialog(null);
                int returnValue = jfc.showSaveDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    if (jfc.getSelectedFile().isDirectory()) {
                        SwingWorker swingWorker = new SwingWorker() {
                            @Override
                            protected Object doInBackground() throws Exception {
                                boolean isDone = barcodeCtrl.saveBarcodeAsImages(jfc.getSelectedFile().getAbsolutePath());
                                return isDone;
                            }

                            @Override
                            protected void done() {

                                try {
                                    Object isDone = get();
                                    boolean done = (boolean) isDone;
                                    if (done)
                                        JOptionPane.showMessageDialog(null, "Barkod/lar basarılı sekilde olusturulmustur");
                                    else
                                        JOptionPane.showMessageDialog(null, "Barkod/lar olusumunda Hata!!!");

                                } catch (InterruptedException e1) {
                                    e1.printStackTrace();
                                } catch (ExecutionException e1) {
                                    e1.printStackTrace();
                                }
                            }
                        };
                        swingWorker.execute();


                    }
                }
            }
        });

        btnClean.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clean();
            }
        });

        btnPrint.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
                jfc.setDialogTitle("Kaydedeceginiz klasoru seciniz : ");
                jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

                // int returnValue = jfc.showSaveDialog(null);
                int returnValue = jfc.showSaveDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    if (jfc.getSelectedFile().isDirectory()) {
                        SwingWorker swingWorker = new SwingWorker() {
                            @Override
                            protected Object doInBackground() throws Exception {
                                boolean isDone = barcodeCtrl.printBarcodes(jfc.getSelectedFile().getAbsolutePath());
                                return isDone;
                            }

                            @Override
                            protected void done() {

                                try {
                                    Object isDone = get();
                                    boolean done = (boolean) isDone;
                                    if (done)
                                        JOptionPane.showMessageDialog(null, "Rapor basarılı sekilde olusturulmustur");
                                    else
                                        JOptionPane.showMessageDialog(null, "Rapor olusumunda Hata!!!");

                                } catch (InterruptedException e1) {
                                    e1.printStackTrace();
                                } catch (ExecutionException e1) {
                                    e1.printStackTrace();
                                }
                            }
                        };
                        swingWorker.execute();


                    }
                }
            }
        });
        btnCreateBarcode.setIcon(new ImageIcon(imgBtnBrcd));
        btnPrint.setIcon(new ImageIcon(imgBtnPrint));
        btnImage.setIcon(new ImageIcon(imgBtnImage));
        btnClean.setIcon(new ImageIcon(imgBtnClean));
        pnlBtn.add(btnCreateBarcode);
        pnlBtn.add(btnPrint);
        pnlBtn.add(btnImage);
        pnlBtn.add(btnClean);
        pnlBtn.setBorder(borderBtn);

        pnlMain.add(pnlLabels, BorderLayout.WEST);
        pnlMain.add(pnlData, BorderLayout.CENTER);
        pnlMain.add(pnlBtn, BorderLayout.SOUTH);
        this.add(pnlMain, BorderLayout.NORTH);
    }

    @Override
    public IBarcodeModel getBarcodeModel() {
        IBarcodeModel model = ModelGenerator.createBarcodeModel();
        if (txtCount.getText().toString().trim().length() == 0)
            return null;
        model.setCount(Integer.valueOf(txtCount.getText().toString().trim()));
        model.setProvince(cmbProvince.getSelectedItem().toString());
        model.setProvinceCode(Utils.getSelectedProvincePlate(cmbProvince.getSelectedItem().toString()));
        return model;
    }

    @Override
    public JPanel asPanel() {
        return this;
    }

    public JPanel getProgressPanel() {
        JPanel panel = new JPanel();
        try {
            ImageIcon loading = new ImageIcon(ImageIO.read(getClass().getClassLoader().getResource("progress.gif")));
            JLabel background = new JLabel("", loading, JLabel.CENTER);
            panel.add(background);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return panel;
    }

    private void clean() {
        txtCount.setText("0");
        barcodeCtrl.clearList();
        if (pnlResults != null)
            pnlResults.setBarcodes(null);
        pnlMain.revalidate();
    }
}
