package airprort_system;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class Customer extends User {
    
    private String passportNumber;
    private String frequentFlyerNumber;
    private List<Booking> bookingHistory;
    private Map<String, String> preferences;
    private List<Payment> paymentMethods;
    
    @Override
    public void accessDashBoared(){};

    public Customer(String passportNumber, String frequentFlyerNumber, List<Booking> bookingHistory, Map<String, String> preferences, List<Payment> paymentMethods, String userID, String username, String email, String passwordHash, String role, int phoneNumber) {
        super(userID, username, email, passwordHash, role, phoneNumber);
        this.passportNumber = passportNumber;
        this.frequentFlyerNumber = frequentFlyerNumber;
        this.bookingHistory = bookingHistory;
        this.preferences = preferences;
        this.paymentMethods = paymentMethods;
    }
    
     public List<Flight> searchFlights(FlightSearchCriteria criteria) {
        System.out.println("Searching flights with criteria: " + criteria);
        return List.of();
    }

    public Booking bookFlight(Flight flight, List<Passenger> passengers) {
        System.out.println("Booking flight: " + flight.getFlightNumber());
        Booking booking = new Booking(); // You’d build a real booking
        bookingHistory.add(booking);
        return booking;
    }

    public boolean cancelBooking(String bookingId) {
        System.out.println("Canceling booking with ID: " + bookingId);
        return true;
    }

    public List<Booking> viewBookingHistory() {
        return bookingHistory;
    }

    public boolean addPaymentMethod(Payment payment) {
        System.out.println("Adding new payment method.");
        return paymentMethods.add(payment);
    }

    public boolean requestRefund(String bookingId) {
        System.out.println("Refund requested for booking ID: " + bookingId);
        return true;
    }

    public boolean rateFlight(Flight flight, int rating, String review) {
        System.out.println("Rated flight " + flight.getFlightNumber() + ": " + rating + " stars");
        return true;
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

}
