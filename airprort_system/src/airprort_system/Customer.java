package airprort_system;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ArrayList; // Import ArrayList
import java.text.SimpleDateFormat; // Added for displayInfo in Passengers (if needed, good practice)

public class Customer extends User {
    
    private String passportNumber;
    private String frequentFlyerNumber;
    private List<Booking> bookingHistory;
    private Map<String, String> preferences;
    private List<Payment> paymentMethods;

    public Customer(String userID, String username, String email, String passwordHash, String role, int phoneNumber) {
        super(userID, username, email, passwordHash, role, phoneNumber);
        this.bookingHistory = new ArrayList<>();
        this.preferences = new java.util.HashMap<>();
        this.paymentMethods = new ArrayList<>(); // Initialized here
    }

    // Constructor matching the User superclass constructor that takes address
    public Customer(String userID, String username, String email, String passwordHash, String role, int phoneNumber, String address) {
        super(userID, username, email, passwordHash, role, phoneNumber, address);
        this.bookingHistory = new ArrayList<>();
        this.preferences = new java.util.HashMap<>();
        this.paymentMethods = new ArrayList<>(); // Initialized here
    }
   
    // Original constructor with more fields (ensure it calls super correctly)
    public Customer(String passportNumber, String frequentFlyerNumber, List<Booking> bookingHistory, Map<String, String> preferences, List<Payment> paymentMethods, String userID, String username, String email, String passwordHash, String role, int phoneNumber) {
        super(userID, username, email, passwordHash, role, phoneNumber);
        this.passportNumber = passportNumber;
        this.frequentFlyerNumber = frequentFlyerNumber;
        this.bookingHistory = bookingHistory != null ? bookingHistory : new ArrayList<>();
        this.preferences = preferences != null ? preferences : new java.util.HashMap<>();
        this.paymentMethods = paymentMethods != null ? paymentMethods : new ArrayList<>(); // Initialized here
    }
    
    @Override
   public void accessDashBoared() {
        System.out.println("Customer Dashboard Accessed");}
   
     public List<Flight> searchFlights(FlightSearchCriteria criteria) {
        System.out.println("Searching flights with criteria: " + criteria);
        return List.of();
    }

    public Booking bookFlight(Flight flight, List<Passengers> passengers) {
        System.out.println("Attempting to book flight " + flight.getFlightNumber() + " for " + passengers.size() + " passengers.");
        return new Booking(); // Return a dummy booking
    }

    public boolean cancelBooking(Booking booking) {
        if (booking != null) {
            System.out.println("Cancelling booking: " + booking.getBookingID());
            return true;
        }
        return false;
    }

    public List<Booking> viewBookingHistory() {
        System.out.println("Viewing booking history for " + getUsername());
        return bookingHistory;
    }

    public boolean updatePreferences(String key, String value) {
        if (key != null && !key.isEmpty() && value != null) {
            if (this.preferences == null) { // Defensive check
                this.preferences = new java.util.HashMap<>();
            }
            preferences.put(key, value);
            System.out.println("Preference updated: " + key + " = " + value);
            return true;
        }
        return false;
    }

    public boolean addPaymentMethod(Payment payment) {
        if (payment != null) {
            if (this.paymentMethods == null) { // FIX: Ensure list is initialized before adding
                this.paymentMethods = new ArrayList<>();
            }
            this.paymentMethods.add(payment);
            System.out.println("Payment method added: " + payment.getPaymentMethod());
            return true;
        }
        return false;
    }

    // FIX: Changed access modifier to public to match User superclass
    @Override
    public String getUsername() {
        return super.getUsername(); // Call superclass method
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public String getFrequentFlyerNumber() {
        return frequentFlyerNumber;
    }

    public void setFrequentFlyerNumber(String frequentFlyerNumber) {
        this.frequentFlyerNumber = frequentFlyerNumber;
    }

    public List<Booking> getBookingHistory() {
        return bookingHistory;
    }

    public void setBookingHistory(List<Booking> bookingHistory) {
        this.bookingHistory = bookingHistory;
    }

    public Map<String, String> getPreferences() {
        return preferences;
    }

    public void setPreferences(Map<String, String> preferences) {
        this.preferences = preferences;
    }

    public List<Payment> getPaymentMethods() {
        return paymentMethods;
    }

    public void setPaymentMethods(List<Payment> paymentMethods) {
        this.paymentMethods = paymentMethods;
    }

    // Assuming this method is meant to return the UserID, not a generic ID
    public String getId() { // Made public
        return getUserID();
    }


 class FlightSearchCriteria {
    private String departureAirport;
    private String arrivalAirport;
    private Date departureDate;
    private String seatClass;

    public FlightSearchCriteria(String departureAirport, String arrivalAirport, Date departureDate, String seatClass) {
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
        this.departureDate = departureDate;
        this.seatClass = seatClass;
    }

    @Override
    public String toString() {
        return departureAirport + " to " + arrivalAirport + " on " + departureDate + " (" + seatClass + ")";
    }
}
 
 public boolean requestRefund(int bookingID, double amount) {
// Simulate creating a refund request without a separate class
String status = "Pending";
Date requestDate = new Date();

System.out.println("Refund request submitted:");
System.out.println("Booking ID: " + bookingID);
System.out.println("Customer ID: " + this.userID);
System.out.println("Amount: " + amount);
System.out.println("Status: " + status);
System.out.println("Request Date: " + requestDate.toString());

// You can later store this info in a file, list, or database if needed
return true;
}

}
