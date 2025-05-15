package airprort_system;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Booking {
    private String bookingID;
    private Customer customer;
    private Flight flight;
    private List<Passengers> passenger;
    private Date bookingDate;
    private List<String> seatNumbers;
    private String seatClass;
    private double totalPrice;
    private String paymentStatus;
    private String bookingStatus;
    private List<String> specialRequests;
    private Map<String, Double> fare;

    public Booking(String bookingID, Customer customer, Flight flight, List<Passengers> passenger, Date bookingDate, List<String> seatNumbers, String seatClass, double totalPrice, String paymentStatus, String bookingStatus, List<String> specialRequests) {
        this.bookingID = bookingID;
        this.customer = customer;
        this.flight = flight;
        this.passenger = passenger;
        this.bookingDate = bookingDate;
        this.seatNumbers = seatNumbers;
        this.seatClass = seatClass;
        this.totalPrice = totalPrice;
        this.paymentStatus = paymentStatus;
        this.bookingStatus = bookingStatus;
        this.specialRequests = specialRequests;
    }

    Booking() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public String getBookingID() {
        return bookingID;
    }

    public void setBookingID(String bookingID) {
        this.bookingID = bookingID;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public List<Passengers> getPassenger() {
        return passenger;
    }

    public void setPassenger(List<Passengers> passenger) {
        this.passenger = passenger;
    }

    public Date getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }

    public List<String> getSeatNumbers() {
        return seatNumbers;
    }

    public void setSeatNumbers(List<String> seatNumbers) {
        this.seatNumbers = seatNumbers;
    }

    public String getSeatClass() {
        return seatClass;
    }

    public void setSeatClass(String seatClass) {
        this.seatClass = seatClass;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public List<String> getSpecialRequests() {
        return specialRequests;
    }

    public void setSpecialRequests(List<String> specialRequests) {
        this.specialRequests = specialRequests;
    }
    
    public double calculateTotal(){
     return totalPrice = getFareForClass() * Passengers.size();
    }

    public double getFareForClass(String seatClass) {
        if (fare.containsKey(seatClass)) {
        return fare.get(seatClass);
    } else {
        throw new IllegalArgumentException("Invalid seat class: " + seatClass);
    }
    }
    
    public boolean confirmBooking() {
    if (flight.checkAvailability(seatClass) >= Passengers.size()) {
        bookingStatus = "confirmed";
        paymentStatus = "paid";
        flight.reserveSeats(seatClass, Passengers.size());
        return true;
    }
    bookingStatus = "waitlisted";
    return false;
}
    
     public boolean cancelBooking() {
    if (!bookingStatus.equalsIgnoreCase("cancelled")) {
        bookingStatus = "cancelled";
        paymentStatus = "refunded";
        flight.releaseSeats((int) Passengers.size(), seatClass);

        return true;
    }
    return false;
}

     
    public Ticket generateTicket() {
    Ticket ticket = new Ticket();
    ticket.setBooking(this);
    for (int i = 0; i < Passengers.size(); i++) {
        ticket.setPassenger(Passengers.get(i));
        ticket.setSeatNumber(seatNumbers.get(i));
        
    }
    return ticket;
}
    public boolean addPassenger(Passengers passenger) {
    Passengers.add(passenger);
    return true;
}
    public boolean removePassenger(String passengerId) {
    for (int i = 0; i < Passengers.size(); i++) {
        if (Passengers.get(i).getPassengerId().equals(passengerId)) {
            Passengers.remove(i);
            return true;
        }
    }
    return false;
}



    
    public boolean updatePaymentStatus(String status) {
    this.paymentStatus = status;
    return true;
}

    private double getFareForClass() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    }



