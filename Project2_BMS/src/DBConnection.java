
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/java_banking_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "HRanjan2512";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found. Make sure the JAR is in your classpath.");
            throw new SQLException("JDBC Driver not found.", e);
        }
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
            } catch (SQLException e) {
                System.err.println("Error closing database connection: " + e.getMessage());
            }
        }
    }
}
