package com.utils;

import com.models.IBarcodeModel;
import com.models.PrintModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.configuration.DefaultConfiguration;
import org.apache.jasper.servlet.JasperLoader;
import org.krysalis.barcode4j.BarcodeException;
import org.krysalis.barcode4j.BarcodeGenerator;
import org.krysalis.barcode4j.BarcodeUtil;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Ozgen on 5/8/17.
 */
public class BrcdGenerator implements IBrcdGenerator {

	@Override
	public List createBarcodes(IBarcodeModel model, String lastBarcode) {
		List barcodes = new LinkedList<>();
		for (int i = 0; i < model.getCount(); i++) {
			IBarcodeModel barcodeModel = ModelGenerator.createBarcodeModel();
			barcodeModel.setProvinceCode(model.getProvinceCode());
			lastBarcode = createABarcode(model, lastBarcode);
			barcodeModel.setBarcodeNumber(lastBarcode);
			barcodeModel.setProvince(model.getProvince());

			barcodeModel.setCount(model.getCount());
			barcodes.add(barcodeModel);
		}
		return barcodes;
	}

	private String createABarcode(IBarcodeModel model, String lastBarcode) {
		String brcd = "";
		String last10digits = "";
		String plateNumber = "";
		int plateNum = model.getProvinceCode();
		plateNumber = String.valueOf(plateNum);
		if (plateNumber.length() == 1) {
			plateNumber = "0" + plateNumber;
		}
		if (lastBarcode == null) {

			// ilk iki karakteri barkodun alındı
			// son 10 karakteri belirlemede null oldugu için ilk 1 den
			// başlanacak
			int barcodelastNum = 1;
			last10digits = filllastDigitsZeroFor10length(barcodelastNum);

		} else {
			last10digits = filllastDigitsZeroFor10length(getLastDigits(lastBarcode) + 1);
		}
		return plateNumber + last10digits;
	}

	private int getLastDigits(String lastBarcode) {
		String numbers = lastBarcode.substring(2, lastBarcode.length());
		char[] array = numbers.toCharArray();
		int index = 0;
		for (int i = 0; i < array.length; i++) {
			if (Character.getNumericValue(array[i]) != 0)
				index = i;
			break;
		}

		String lastDigits = numbers.substring(index, numbers.length());
		if (lastDigits.length() > 0)
			return Integer.valueOf(lastDigits);
		else
			return 0;

	}

	private String filllastDigitsZeroFor10length(int lastDigits) {
		String s = String.valueOf(lastDigits);
		StringBuilder builder = new StringBuilder();
		builder.append(s);
		builder.reverse();
		while (builder.length() < 10) {
			builder.append("0");
		}
		builder = builder.reverse();
		return builder.toString();
	}

	public BufferedImage createBarcode(String number) {
		BufferedImage bufferedImage = null;
		if (number != null) {

			BarcodeUtil util = BarcodeUtil.getInstance();
			BarcodeGenerator gen = null;
			try {
				gen = util.createBarcodeGenerator(buildCfg("ean-13"));
			} catch (ConfigurationException e) {
				e.printStackTrace();
			} catch (BarcodeException e) {
				e.printStackTrace();
			}
			int resolution = 200;
			OutputStream fout = null;
			try {
				fout = new FileOutputStream("ean-13.jpg");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			BitmapCanvasProvider canvas;
			canvas = new BitmapCanvasProvider(fout, "image/jpeg", resolution, BufferedImage.TYPE_BYTE_BINARY, false, 0);
			gen.generateBarcode(canvas, number);
			try {
				canvas.finish();
			} catch (IOException e) {
				e.printStackTrace();
			}

			bufferedImage = canvas.getBufferedImage();

		}
		return bufferedImage;
	}

	private static Configuration buildCfg(String type) {
		DefaultConfiguration cfg = new DefaultConfiguration("barcode");

		// Bar code type
		DefaultConfiguration child = new DefaultConfiguration(type);
		cfg.addChild(child);

		// Human readable text position
		DefaultConfiguration attr = new DefaultConfiguration("human-readable");
		DefaultConfiguration subAttr = new DefaultConfiguration("placement");
		subAttr.setValue("bottom");
		attr.addChild(subAttr);

		child.addChild(attr);
		return cfg;
	}


	public JasperPrint createJasperPrint(List barcodes) {
		JasperPrint jasperPrint = null;
		JRBeanCollectionDataSource beanCollectionDataSource = null;
//		ClassLoader classLoader = getClass().getClassLoader();
//		File jasperFileName = new File(classLoader.getResource("report.jasper").getFile());
		InputStream jasperFileName = null;
		try {
			jasperFileName = Utils.loadResource("/report.jasper");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		List prList = convertBarcodeToPrintModel(barcodes);
		HashMap hashMap = new HashMap();
		if (prList == null)
			return null;
		if (prList.size() > 0) {
			for (Object o : prList) {
				PrintModel model = (PrintModel) o;
				Object img = createBarcode(model.getBarcodeNumber());
				hashMap.put("brcd", img);
				model.setBrcd(img);
				if (model.getBarcodeNumber2() != null) {
					hashMap.put("brcd2", createBarcode(model.getBarcodeNumber2()));
					Object img2 = createBarcode(model.getBarcodeNumber2());
					model.setBrcd2(img2);
				} else {
					try {
						BufferedImage image = ImageIO.read(getClass().getClassLoader().getResource("empty.png"));
						model.setBrcd2(image);
						model.setBarcodeNumber2("");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			beanCollectionDataSource = new JRBeanCollectionDataSource(prList);
		}

		try {
			jasperPrint = JasperFillManager.fillReport(jasperFileName, hashMap,
					beanCollectionDataSource);
		} catch (JRException e) {
			e.printStackTrace();
		}

		return jasperPrint;
	}

	private List convertBarcodeToPrintModel(List barcodes) {
		List printModel = new ArrayList<PrintModel>();
		if (barcodes == null || barcodes.size() == 0)
			return null;
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String requiredDate = df.format(new Date()).toString();
		if (barcodes.size() % 2 == 0) {
			for (int i = 0; i < barcodes.size() - 1;) {
				PrintModel model = ModelGenerator.createPrintModel();
				IBarcodeModel barcodeModel = (IBarcodeModel) barcodes.get(i);
				IBarcodeModel barcodeModel2 = (IBarcodeModel) barcodes.get(i + 1);
				model.setBarcodeNumber(barcodeModel.getBarcodeNumber());
				model.setBarcodeNumber2(barcodeModel2.getBarcodeNumber());
				model.setTarih(requiredDate);
				printModel.add(model);
				i = i + 2;
			}
		} else if (barcodes.size() == 1) {
			PrintModel model = ModelGenerator.createPrintModel();
			IBarcodeModel barcodeModel = (IBarcodeModel) barcodes.get(0);
			model.setBarcodeNumber(barcodeModel.getBarcodeNumber());

			model.setTarih(requiredDate);

			printModel.add(model);
		} else {
			for (int i = 0; i < barcodes.size() - 2;) {
				PrintModel model = ModelGenerator.createPrintModel();
				model.setTarih(requiredDate);
				IBarcodeModel barcodeModel = (IBarcodeModel) barcodes.get(i);
				IBarcodeModel barcodeModel2 = (IBarcodeModel) barcodes.get(i + 1);
				model.setBarcodeNumber(barcodeModel.getBarcodeNumber());
				model.setBarcodeNumber2(barcodeModel2.getBarcodeNumber());
				i = i + 2;

				printModel.add(model);
			}
			PrintModel model = ModelGenerator.createPrintModel();
			IBarcodeModel barcodeModel = (IBarcodeModel) barcodes.get(barcodes.size() - 1);
			model.setBarcodeNumber(barcodeModel.getBarcodeNumber());
			printModel.add(model);
		}
		return printModel;
	}

}
