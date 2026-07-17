package com.contacts.util;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

/**
 * 数据库工具类
 *
 * 【类说明】
 * - 提供数据库连接获取功能
 * - 管理MySQL JDBC驱动的加载
 * - 是整个项目数据库操作的入口
 *
 * 【功能】
 * - 加载MySQL JDBC驱动程序
 * - 提供获取数据库连接的方法
 * - 打印调试信息（驱动加载状态、已注册驱动列表）
 *
 * 【数据库配置】
 * - URL: jdbc:mysql://localhost:3306/contact_system
 * - 用户名: root
 * - 密码: 123456
 * - 数据库名: contact_system
 *
 * 【驱动说明】
 * - 支持 MySQL Connector/J 8.x (com.mysql.cj.jdbc.Driver)
 * - 兼容 MySQL Connector/J 5.x (com.mysql.jdbc.Driver)
 *
 * 【使用方式】
 * Connection conn = DBUtil.getConnection();
 * // 使用连接...
 * conn.close();
 */
public class DBUtil {

    /** 数据库连接URL */
    private static final String URL = "jdbc:mysql://localhost:3306/contact_system?serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true";

    /** 数据库用户名 */
    private static final String USER = "root";

    /** 数据库密码 */
    private static final String PASSWORD = "123456";

    /**
     * 静态初始化块
     *
     * 【执行时机】
     * - 当类被加载到JVM时执行
     * - 只执行一次
     *
     * 【功能】
     * - 尝试加载MySQL JDBC驱动
     * - 先尝试新版驱动(com.mysql.cj.jdbc.Driver)
     * - 如果失败则尝试旧版驱动(com.mysql.jdbc.Driver)
     * - 打印加载结果信息
     *
     * 【JDBC驱动加载原理】
     * - Class.forName()加载驱动类
     * - 驱动类会在静态块中向DriverManager注册自己
     * - 注册后可以通过DriverManager.getConnection()获取连接
     */
    static {
        try {
            // 尝试加载新版MySQL驱动（MySQL Connector/J 8.x）
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("MySQL driver loaded successfully (com.mysql.cj.jdbc.Driver)");
        } catch (ClassNotFoundException e) {
            try {
                // 新版驱动加载失败，尝试旧版驱动（MySQL Connector/J 5.x）
                Class.forName("com.mysql.jdbc.Driver");
                System.out.println("MySQL driver loaded successfully (com.mysql.jdbc.Driver)");
            } catch (ClassNotFoundException ex) {
                // 驱动加载失败，打印错误信息
                System.err.println("ERROR: MySQL driver not found!");
                ex.printStackTrace();
            }
        }
    }

    /**
     * 获取数据库连接
     *
     * 【功能】
     * - 通过DriverManager获取MySQL数据库连接
     * - 打印已注册的JDBC驱动列表（调试用）
     *
     * 【返回值】
     * - 成功：返回Connection对象
     * - 失败：抛出SQLException
     *
     * 【使用注意】
     * - 使用完毕后需要手动关闭连接
     * - 建议使用try-with-resources自动关闭
     *
     * @return 数据库连接对象
     * @throws SQLException 如果获取连接失败（数据库未启动、密码错误等）
     */
    public static Connection getConnection() throws SQLException {
        // 打印已注册的JDBC驱动列表，用于调试
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        System.out.println("Registered JDBC drivers:");
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            System.out.println("  - " + driver.getClass().getName());
        }

        // 使用DriverManager获取数据库连接
        // 需要传入URL、用户名、密码
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
