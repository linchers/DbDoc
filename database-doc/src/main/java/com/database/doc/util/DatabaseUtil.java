package com.database.doc.util;


import com.database.doc.config.DatabaseConfig;
import com.database.doc.exception.ToolException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DatabaseUtil {

    public static Connection getConnection(DatabaseConfig config) {
        String driverClass = config.getDriverClass();
        String connectionURL = config.getConnectionUrl();
        String username = config.getUsername();
        String password = config.getPassword();
        try {
            Class.forName(driverClass).newInstance();
            Properties prop = new Properties();
            prop.put("user", username);
            prop.put("password", password);
            prop.put("remarksReporting", "true");
            prop.setProperty("useInformationSchema", "true");
            prop.setProperty("nullCatalogMeansCurrent", "true");
            return DriverManager.getConnection(connectionURL, prop);
        } catch (Exception e) {
            throw new ToolException("100001", e.toString());
        }
    }

    public static void releaseConnection(Connection connection) {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}