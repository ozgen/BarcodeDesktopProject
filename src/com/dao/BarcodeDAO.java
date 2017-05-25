package com.dao;

import com.dao.DBConnection.DBConnector;
import com.models.BarcodeModel;
import com.models.IBarcodeModel;
import com.utils.ModelGenerator;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Ozgen on 5/7/17.
 */
public class BarcodeDAO extends DBConnector implements IDao<IBarcodeModel> {
	Statement stmt;
	ResultSet rs;
	String SQL = "";

	@Override
	public boolean save(IBarcodeModel model) {
		int isSaved = 0;
		connectDB();
		if (createBarcodeTable()) {
			try {
				stmt = connection.createStatement();
				SQL = "INSERT INTO " + TABLE_NAME + " (" + COL_BARCODE_NUM + ", " + COL_PROVINCE_CODE + ")" + "VALUES ("
						+ model.getBarcodeNumber() + ", " + model.getProvinceCode() + " )";
				isSaved = stmt.executeUpdate(SQL);
				System.out.println(isSaved);

			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				disconnectDB();
			}

		}
		if (isSaved == 1) {
			return true;

		} else {
			return false;

		}
	}

	@Override
	public boolean update(IBarcodeModel model) {
		boolean isUpdated = false;
		connectDB();
		int result = 0;
		if (createBarcodeTable()) {
			try {
				stmt = connection.createStatement();

				SQL = "UPDATE " + TABLE_NAME + " set " + COL_BARCODE_NUM + " = " + model.getBarcodeNumber() + " where "
						+ COL_PROVINCE_CODE + " = " + model.getProvinceCode() + " ;";
				result = stmt.executeUpdate(SQL);
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				disconnectDB();
			}
			if (result == 1)
				isUpdated = true;
		}
		return isUpdated;
	}

	@Override
	public boolean delete(IBarcodeModel model) {
		return false;
	}

	@Override
	public IBarcodeModel getLast(int provinceCode) {
		IBarcodeModel model = ModelGenerator.createBarcodeModel();
		connectDB();

		if (createBarcodeTable()) {
			try {
				stmt = connection.createStatement();
				SQL = "SELECT * FROM  " + TABLE_NAME + " WHERE " + COL_PROVINCE_CODE + " = " + provinceCode;
				rs = stmt.executeQuery(SQL);

				while (rs.next()) {
					model.setBarcodeNumber(rs.getString(COL_BARCODE_NUM));
					model.setProvinceCode(provinceCode);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}

		return model;
	}
}
