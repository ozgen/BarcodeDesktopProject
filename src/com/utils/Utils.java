package com.utils;

import com.controllers.BarcodeCtrl;
import com.controllers.IBarcodeCtrl;
import com.dao.BarcodeDAO;
import com.dao.IDao;
import com.google.errorprone.matchers.EnclosingClass;
import com.models.IBarcodeModel;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Ozgen on 5/7/17.
 */
public class Utils {

	public static ArrayList<String> provinces = new ArrayList<>();
	public static HashMap<String, String> provincePlateCode = new HashMap<>();

	private URL provinceJSonFile = getClass().getClassLoader().getResource("province.txt");

	public static void loadData() {
		Utils utils = new Utils();
		JSONParser parser = new JSONParser();
		try {
			// Object o = parser.parse(new
			// FileReader(utils.provinceJSonFile.getPath()));
			Object o = parser.parse(Province.PROVINCE_JSON);
			// System.out.println(o.toString());
			JSONArray array = (JSONArray) o;
			Iterator<JSONObject> iterator = array.iterator();
			while (iterator.hasNext()) {
				JSONObject object = iterator.next();
				if (object != null) {
					String plate = (String) object.get("plaka");
					String province = (String) object.get("value");
					provinces.add(province);
					provincePlateCode.put(province, plate);
				}

			}
			// System.out.print(provinces.size());
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

	public static InputStream loadResource(final String resourcePath) throws IOException {
		InputStream url = Utils.class.getResourceAsStream(resourcePath);
		if (url == null) {
			throw new IOException(resourcePath + ": resource not found");

		}
		return url;

	}

	public static int getSelectedProvincePlate(String province) {
		return Integer.parseInt(provincePlateCode.get(province));
	}

	public static IDao<IBarcodeModel> createBarcodeDao() {
		return new BarcodeDAO();
	}

	public static IBarcodeCtrl createBarcodeCtrl() {
		return new BarcodeCtrl();
	}

	public static IBrcdGenerator createBarcodeGenerator() {
		return new BrcdGenerator();
	}

	public static IDao<IBarcodeModel> createBrcdDao() {
		return new BarcodeDAO();
	}

}
