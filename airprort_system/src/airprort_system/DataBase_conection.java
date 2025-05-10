package airprort_system;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DataBase_conection {
    
     public static void main(String[] args) {
        
          String url = "jdbc:mysql://localhost:3306/airport";
        String username = "root";
        String password = "oqwe321asd";

        try {
            Connection conn = DriverManager.getConnection(url, username, password);
            System.out.println("Connection successful!");
        } catch (SQLException e) {
            System.out.println("Connection failed!");
            e.printStackTrace();    
 }

    }
}
