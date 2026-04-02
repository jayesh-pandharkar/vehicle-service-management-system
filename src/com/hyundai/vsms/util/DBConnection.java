package com.hyundai.vsms.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DBConnection — Singleton utility class
 *
 * Why singleton? We want exactly ONE shared connection throughout the
 * application to avoid resource waste. In production you'd use a
 * connection pool (HikariCP), but for a learning project this is perfect.
 */
public class DBConnection {

    // ── JDBC connection details ──────────────────────────────────────────────
    private static final String URL = "jdbc:mysql://localhost:3306/hyundai_vsms?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER     = "root";      // Change to your MySQL user
    private static final String PASSWORD = "Swajal@145";      // Change to your MySQL password

    // The single shared connection instance
    private static Connection connection = null;

    // Private constructor — prevents instantiation from outside
    private DBConnection() {}

    /**
     * Returns the single Connection instance.
     * Creates it on first call (lazy initialization).
     *
     * @return java.sql.Connection
     */
    public static Connection getConnection() {
        try {
            // If no connection exists OR the existing one is closed, create a new one
            if (connection == null || connection.isClosed()) {
                // Load the MySQL JDBC driver (needed for older Java versions)
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("[DB] Connected to MySQL: hyundai_vsms");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("[ERROR] MySQL JDBC Driver not found. Add mysql-connector-j to classpath.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("[ERROR] Could not connect to database. Check URL, user, and password.");
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * Closes the connection — call this when the application exits.
     */
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("[DB] Connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to close connection.");
            e.printStackTrace();
        }
    }
}
