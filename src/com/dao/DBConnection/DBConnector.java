package com.dao.DBConnection;

import java.io.File;
import java.sql.*;

/**
 * Created by Ozgen on 5/5/17.
 */
public class DBConnector {

    /**
     * Connect to a sample database
     */
    //private String url = "jdbc:sqlite:barcode.db";

    public Connection connection = null;

    public static String TABLE_NAME = "BARCODE";
    public static String COL_ID = "ID";
    public static String COL_BARCODE_NUM = "BARCODE_NUM";
    public static String COL_PROVINCE_CODE = "PROVINCE_CODE";
    private static String DB_PATH = "C:/sqlite/db/";


    /**
     * Gereksiz method :)
     */
    public void createNewDatabase() {

        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try (Connection conn = DriverManager.getConnection(getUrl())) {


            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private String getUrl() {
        String url = "jdbc:sqlite:barcode.db";
        if (OSValidator.isMac())
            return url;
        else if (OSValidator.isWindows()) {
            url = "jdbc:sqlite:C:/sqlite/db/barcode.db";
            File directory = new File(DB_PATH);
            if (!directory.exists()) {
				directory.mkdir();
			}
            return url;
        }
        return null;

    }

    public void connectDB() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(getUrl());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void disconnectDB() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String a[]) {
        DBConnector dbConnector = new DBConnector();
        System.out.println(dbConnector.createBarcodeTable());

    }


    public boolean createBarcodeTable() {
        connectDB();
        try {
            Statement stmt = connection.createStatement();
            String sql = "CREATE TABLE BARCODE " +
                    "(BARCODE_NUM             INT    NOT NULL, " +
                    " PROVINCE_CODE            INT     NOT NULL)";

            stmt.execute(sql);
            stmt.close();
            disconnectDB();
        } catch (Exception e) {
            if (e.getMessage().contains("already exists"))
                return true;
            return false;

        }
        return true;
    }

    public static class OSValidator {

        private static String OS = System.getProperty("os.name").toLowerCase();


        public static boolean isWindows() {
            return (OS.indexOf("win") >= 0);
        }

        public static boolean isMac() {
            return (OS.indexOf("mac") >= 0);
        }

        public static boolean isUnix() {
            return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0);
        }

        public static boolean isSolaris() {
            return (OS.indexOf("sunos") >= 0);
        }

        public static String getOS() {
            if (isWindows()) {
                return "win";
            } else if (isMac()) {
                return "osx";
            } else if (isUnix()) {
                return "uni";
            } else if (isSolaris()) {
                return "sol";
            } else {
                return "err";
            }
        }

    }


}
