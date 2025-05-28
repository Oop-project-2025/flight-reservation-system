package airprort_system;

import java.lang.String;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.Connection; // Ensure this is java.sql.Connection
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList; // Import ArrayList

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

    // Constructor matching the User superclass constructor that takes address
    public Agent(String agentId, String airline, double commissionRate, List<Booking> bookingsMade, int accessLevel, String userID, String username, String email, String passwordHash, String role, int phoneNumber, String address) {
        super(userID, username, email, passwordHash, role, phoneNumber, address);
        this.agentId = agentId;
        this.airline = airline;
        this.commissionRate = commissionRate;
        this.bookingsMade = bookingsMade != null ? bookingsMade : new ArrayList<>();
        this.accessLevel = accessLevel;
    }

    // Original constructor (ensure it calls super correctly)
    public Agent(String agentId, String airline, double commissionRate, List<Booking> bookingsMade, int accessLevel, String userID, String username, String email, String passwordHash, String role, int phoneNumber) {
        super(userID, username, email, passwordHash, role, phoneNumber);
        this.agentId = agentId;
        this.airline = airline;
        this.commissionRate = commissionRate;
        this.bookingsMade = bookingsMade != null ? bookingsMade : new ArrayList<>();
        this.accessLevel = accessLevel;
    }

    // Original constructor that might have been used for database retrieval
    // Note: This constructor doesn't pass all User fields to super, might lead to incomplete User objects
    public Agent(int agentId, int userID, String airline, double commissionRate, int accessLevel) {
        super(String.valueOf(userID), "defaultUsername", "defaultEmail", "defaultPassword", "AGENT", 123456789);
        this.agentId = String.valueOf(agentId);
        this.airline = airline;
        this.commissionRate = commissionRate;
        this.accessLevel = accessLevel;
        this.bookingsMade = new ArrayList<>(); // Initialize the list
    }

    public boolean createBooking(Customer customer, Flight flight, List<Passengers> passengers, String seatClass) {
        if (customer == null || flight == null || passengers == null || passengers.isEmpty() || seatClass == null || seatClass.isEmpty()) {
            System.out.println("Error: Invalid booking details.");
            return false;
        }
        // In a real system, this would interact with BookingSystem or BookingDatabaseManager
        System.out.println("Booking created by agent for customer " + customer.getUsername() + " on flight " + flight.getFlightNumber());
        return true;
    }

    public boolean updateBookingStatus(Booking booking, String newStatus) {
        if (booking == null || newStatus == null || newStatus.isEmpty()) {
            System.out.println("Error: Invalid booking or status.");
            return false;
        }
        booking.setBookingStatus(newStatus);
        System.out.println("Booking " + booking.getBookingID() + " status updated to: " + newStatus);
        return true;
    }

    public boolean applyDiscount(Booking booking, double discountAmount) {
        if (booking == null || discountAmount <= 0) {
            System.out.println("Error: Invalid booking or discount amount.");
            return false;
        }
        booking.setTotalPrice(booking.getTotalPrice() - discountAmount);
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

    // FIX: Changed access modifier to public to match User superclass
    @Override
    public String getUsername() {
        return super.getUsername(); // Call superclass method
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

    // Nested class Report
    class Report {
        public Report() {
            System.out.println("Report object created.");
        }
    }
}
