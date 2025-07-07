// src/main/java/com/library/jdbc/DBConnection.java
package com.library.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utility class for managing database connections.
 */
public class DBConnection {

    // Database credentials and URL
    private static final String URL = "jdbc:mysql://localhost:3306/library_db";
    private static final String USER = "root"; // Your MySQL username
    private static final String PASSWORD = "HRanjan2512"; // Your MySQL password

    /**
     * Establishes a connection to the database.
     *
     * @return A Connection object if successful, null otherwise.
     */
    public static Connection getConnection() {
        Connection connection = null;
        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Establish the connection
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Database connection established successfully.");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found. Make sure it's in your classpath.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Error connecting to the database: " + e.getMessage());
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * Closes the given database connection.
     *
     * @param connection The Connection object to close.
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Database connection closed.");
            } catch (SQLException e) {
                System.err.println("Error closing the database connection: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    // Main method for testing the connection
    public static void main(String[] args) {
        Connection conn = DBConnection.getConnection();
        if (conn != null) {
            DBConnection.closeConnection(conn);
        }
    }
}
