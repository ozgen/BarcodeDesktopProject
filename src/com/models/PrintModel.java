package com.models;

import java.util.Date;

/**
 * Created by Ozgen on 5/17/17.
 */
public class PrintModel {


    private String tarih;

    private String barcodeNumber;

    private String barcodeNumber2;

    private Object brcd;

    private Object brcd2;

    public Object getBrcd() {
        return brcd;
    }

    public void setBrcd(Object brcd) {
        this.brcd = brcd;
    }

    public Object getBrcd2() {
        return brcd2;
    }

    public void setBrcd2(Object brcd2) {
        this.brcd2 = brcd2;
    }

    public String getTarih() {

        return tarih;
    }

    public void setTarih(String tarih) {
        this.tarih = tarih;
    }

    public String getBarcodeNumber() {
        return barcodeNumber;
    }

    public void setBarcodeNumber(String barcodeNumber) {
        this.barcodeNumber = barcodeNumber;
    }

    public String getBarcodeNumber2() {
        return barcodeNumber2;
    }

    public void setBarcodeNumber2(String barcodeNumber2) {
        this.barcodeNumber2 = barcodeNumber2;
    }
}
