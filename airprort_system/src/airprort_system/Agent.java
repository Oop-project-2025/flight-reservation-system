package airprort_system;

import java.lang.String;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;


public class Agent extends User {
    private String agentId;
    private String airline;
    private double commissionRate;
    private List<Booking> bookingsMade;
    private int accessLevel;
    
   


    

    
   @Override
    public void accessDashBoared() {
        System.out.println("Agent Dashboard Accessed");
    }

    public Agent(String agentId, String airline, double commissionRate, List<Booking> bookingsMade, int accessLevel, String userID, String username, String email, String passwordHash, String role, int phoneNumber) {
        super(userID, username, email, passwordHash, role, phoneNumber);
        this.agentId = agentId;
        this.airline = airline;
        this.commissionRate = commissionRate;
        this.bookingsMade = bookingsMade;
        this.accessLevel = accessLevel;
        
        
    }
    public Agent(int agentId, int userID, String airline, double commissionRate, int accessLevel) {
    super(String.valueOf(userID), "defaultUsername", "defaultEmail", "defaultPassword", "AGENT", 123456789);
    this.agentId = String.valueOf(agentId);
    this.airline = airline;
    this.commissionRate = commissionRate;
    this.accessLevel = accessLevel;
}
    
    public boolean insertAgent() {
    String checkUserQuery = "SELECT COUNT(*) FROM users WHERE user_id = " + super.getUserID();
    String query = "INSERT INTO agents (agent_id, user_id, airline, commission_rate, access_level) VALUES (" 
                    + agentId + ", " 
                    + super.getUserID() + ", '" 
                    + airline + "', " 
                    + commissionRate + ", " 
                    + accessLevel + ")";

    try (Connection conn = DatabaseConnection.connect(); 
         Statement stmt = conn.createStatement()) {

        
        ResultSet rs = stmt.executeQuery(checkUserQuery);
        rs.next();
        if (rs.getInt(1) == 0) {
            System.err.println("User with ID " + super.getUserID() + " does not exist. Cannot insert agent.");
            return false;
        }

        // Insert the agent
        int rowsAffected = stmt.executeUpdate(query);

        if (rowsAffected > 0) {
            System.out.println("Agent inserted successfully.");
            return true;
        }

    } catch (SQLException e) {
        System.err.println("SQL Exception: " + e.getMessage());
    }

    return false;
}

       

    
    
    
    
      public Booking createBookingForCustomer(Customer customer, Flight flight, List<Passengers> passengers) {
        if (customer == null || flight == null || passengers == null || passengers.isEmpty()) {
            System.out.println("Error: Invalid booking parameters.");
            return null;
        }
        Booking booking = new Booking();
        System.out.println("Booking created by agent for customer: " + customer.getUsername());
        bookingsMade.add(booking);
        return booking;
    }
    public Report generateBookingReport(Date startDate, Date endDate) {
        if (startDate == null || endDate == null || endDate.before(startDate)) {
            System.out.println("Error: Invalid date range for report.");
            return null; 
        }
        System.out.println("Generated booking report from " + startDate + " to " + endDate);
        return new Report();
    }

    private Map<String, Customer> customerMap = new HashMap<>();

public Customer viewCustomerDetails(String customerId) {
    System.out.println("Fetching details for customer ID: " + customerId);
    return customerMap.get(customerId);
}


    public Report generateReport(Date startDate, Date endDate) {
        System.out.println("Generating report from " + startDate + " to " + endDate);
        return new Report();
    }

   public boolean applyDiscount(Booking booking, double discountAmount) {
        if (booking == null || discountAmount <= 0) {
            System.out.println("Error: Invalid booking or discount amount.");
            return false;
        }
        System.out.println("Applied discount of " + discountAmount + " to booking: " + booking.getBookingID());
        return true;
    }

     public boolean blockCustomer(Customer customer, String reason) {
        if (customer == null || reason == null || reason.isEmpty()) {
            System.out.println("Error: Invalid customer or reason.");
            return false;
        }
        System.out.println("Customer " + customer.getUsername() + " blocked. Reason: " + reason);
        return true;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getAirline() {
        return airline;
    }

    public void setAirline(String airline) {
        this.airline = airline;
    }

    public double getCommissionRate() {
        return commissionRate;
    }

    public void setCommissionRate(double commissionRate) {
        this.commissionRate = commissionRate;
    }

    public List<Booking> getBookingsMade() {
        return bookingsMade;
    }

    public void setBookingsMade(List<Booking> bookingsMade) {
        this.bookingsMade = bookingsMade;
    }

    public int getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(int accessLevel) {
        this.accessLevel = accessLevel;
    }

    class Report {
    
    public Report() {
        System.out.println("Report object created.");
    }

    

   
}
        
}


