package com.models;

/**
 * Created by Ozgen on 5/5/17.
 */
public interface IBarcodeModel {

    int getProvinceCode();

    void setProvinceCode(int provinceCode);

    int getCount();

    void setCount(int count);

    String getBarcodeNumber();

    void setBarcodeNumber(String barcodeNumber);

    String getProvince();

    void setProvince(String province);
}
