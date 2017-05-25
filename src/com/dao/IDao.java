package com.dao;

import com.models.IBarcodeModel;

/**
 * Created by Ozgen on 5/5/17.
 */
public interface IDao<T> {

    boolean save(T model);

    boolean update(T model);

    boolean delete(T model);

    T getLast(int provinceCode);
}
