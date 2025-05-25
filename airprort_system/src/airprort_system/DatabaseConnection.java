package airprort_system;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import airprort_system.Agent;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/airport?zeroDateTimeBehavior=CONVERT_TO_NULL";
    private static final String USER = "root";
    private static final String PASSWORD = "oqwe321asd";

    public static Connection connect() {
        try {
            System.out.println("Connecting to the database...");
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connection established.");
            return conn;
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC Driver not found.");
        } catch (SQLException e) {
            System.err.println("Connection error: " + e.getMessage());
        }
        return null;
    }
    public static void main(String[] args) {
      
    
        
        Agent agent1 = new Agent(1, 101, "Delta Airlines", 15.5, 2);
        Agent agent2 = new Agent(2, 102, "Qatar Airways", 12.0, 1);

        
        agent1.insertAgent();
        agent2.insertAgent();
    }
}

