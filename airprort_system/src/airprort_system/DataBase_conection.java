package airprort_system;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBase_conection {

    public static void main(String[] args) {
    System.out.println("Starting database connection...");  
    
    String url = "jdbc:mysql://localhost:3306/airport";
    String username = "root";
    String password = "oqwe321asd";

    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        System.out.println("Driver loaded.");  

        Connection conn = DriverManager.getConnection(url, username, password);
        System.out.println("Connection successful!");  
        conn.close();
    } catch (ClassNotFoundException e) {
        System.out.println("MySQL JDBC Driver not found!");
        e.printStackTrace();
    } catch (SQLException e) {
        System.out.println("Connection failed!");
        e.printStackTrace();
    }

    System.out.println("End of main method.");  
}
}