package airprort_system;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class Agent extends User {
    private String agentId;
    private String airline;
    private double commissionRate;
    private List<Booking> bookingsMade;
    private int accessLevel;
    
   @Override
    public void accessDashBoared(){};

    public Agent(String agentId, String airline, double commissionRate, List<Booking> bookingsMade, int accessLevel, String userID, String username, String email, String passwordHash, String role, int phoneNumber) {
        super(userID, username, email, passwordHash, role, phoneNumber);
        this.agentId = agentId;
        this.airline = airline;
        this.commissionRate = commissionRate;
        this.bookingsMade = bookingsMade;
        this.accessLevel = accessLevel;
    }
    
     public Booking createBookingForCustomer(Customer customer, Flight flight, List<Passenger> passengers) {
        Booking booking = new Booking(/* parameters you'd define */);
        System.out.println("Booking created by agent for customer: " + customer.getUsername());
        bookingsMade.add(booking);
        return booking;
    }

    public boolean modifyCustomerBooking(String bookingId, Map<String, String> changes) {
        System.out.println("Modified booking: " + bookingId + " with changes: " + changes);
        return true;
    }

    public Customer viewCustomerDetails(String customerId) {
        System.out.println("Fetching details for customer ID: " + customerId);
        return new Customer(); // You should load actual customer object
    }

    public Report generateReport(Date startDate, Date endDate) {
        System.out.println("Generating report from " + startDate + " to " + endDate);
        return new Report();
    }

    public boolean applyDiscount(Booking booking, double discountAmount) {
        System.out.println("Applied discount of " + discountAmount + " to booking: " + booking.getBookingId());
        return true;
    }

    public boolean blockCustomer(Customer customer, String reason) {
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
