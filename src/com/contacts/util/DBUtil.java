package com.contacts.util;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

public class DBUtil {
    private static final String URL = "jdbc:mysql://localhost:3306/contact_system?serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASSWORD = "123456";

    static {
        try {
            // 尝试加载新版驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("MySQL driver loaded successfully (com.mysql.cj.jdbc.Driver)");
        } catch (ClassNotFoundException e) {
            try {
                // 尝试加载旧版驱动
                Class.forName("com.mysql.jdbc.Driver");
                System.out.println("MySQL driver loaded successfully (com.mysql.jdbc.Driver)");
            } catch (ClassNotFoundException ex) {
                System.err.println("ERROR: MySQL driver not found!");
                ex.printStackTrace();
            }
        }
    }

    public static Connection getConnection() throws SQLException {
        // 打印已注册的驱动
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        System.out.println("Registered JDBC drivers:");
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            System.out.println("  - " + driver.getClass().getName());
        }
        
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}