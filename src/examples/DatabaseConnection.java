package examples;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Class for managing a database connection.
 * 
 * @author didikmaulanaardiansyah
 */
public class DatabaseConnection {
    private final String url = "jdbc:mysql://localhost:8889/students_db";
    private final String user = "admin"; 
    private final String password = "";
    private Connection connection;
    
    
     /**
     * Returns the current database connection.
     * 
     * @return the current database connection
     */
    public Connection getConnection() {
        return connection;
    }

     /**
     * Connects to the database using the specified URL, user, and password.
     */
    public void connect() {
        try {
            connection = DriverManager.getConnection(url, user, password);
            if (connection != null) {
                System.out.println("Connected to the database");
            }

        } catch (SQLException error) {
            System.out.println("An error occurred. Maybe the credentials is invalid");
        }
    }

     /**
     * Closes the current database connection if it exists.
     */
    public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException error) {
            System.out.println("An error occurred. " + error.getLocalizedMessage());
        }
    }
}