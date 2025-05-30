
package airprort_system;

import java.util.Date;
import java.util.UUID;

public class Ticket {

    private String ticketID;
    private Booking booking;
    private Passengers passenger;
    private String seatNumber;
    private Date boardingTime;
    private String gateNumber;
    private String ticketStatus;
    private String barcode;

    public Ticket(String ticketID, Booking booking, Passengers passenger, String seatNumber, Date boardingTime, String gateNumber, String ticketStatus, String barcode) {
        this.ticketID = ticketID;
        this.booking = booking;
        this.passenger = passenger;
        this.seatNumber = seatNumber;
        this.boardingTime = boardingTime;
        this.gateNumber = gateNumber;
        this.ticketStatus = ticketStatus;
        this.barcode = barcode;
    }

    Ticket() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public String getTicketID() {
        return ticketID;
    }

    public void setTicketID(String ticketID) {
        this.ticketID = ticketID;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public Passengers getPassenger() {
        return passenger;
    }

    public void setPassenger(Passengers passenger) {
        this.passenger = passenger;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public Date getBoardingTime() {
        return boardingTime;
    }

    public void setBoardingTime(Date boardingTime) {
        this.boardingTime = boardingTime;
    }

    public String getGateNumber() {
        return gateNumber;
    }

    public void setGateNumber(String gateNumber) {
        this.gateNumber = gateNumber;
    }

    public String getTicketStatus() {
        return ticketStatus;
    }

    public void setTicketStatus(String ticketStatus) {
        this.ticketStatus = ticketStatus;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
    
     public void generateBarcode() {
        this.barcode = "BC" + ticketID.hashCode() + System.currentTimeMillis();

}
     public boolean validateTicket() {
        return ticketStatus.equalsIgnoreCase("Confirmed") && barcode != null;
    }
    public boolean cancelTicket() {
        if (!ticketStatus.equalsIgnoreCase("Cancelled")) {
            this.ticketStatus = "Cancelled";
            return true;
        }
        return false;
    }

    public boolean sendToEmail(String email) {
        if (email.contains("@")) {
            System.out.println("Sending ticket to " + email + "...");
            return true;
        }
        return false;
    }

    public boolean printTicket() {
        if (validateTicket()) {
            System.out.println("========== TICKET ==========");
            System.out.println("Ticket ID: " + ticketID);
            System.out.println("Passenger: " + passenger.getFirstName()+passenger.getLastName());
            System.out.println("Flight: " + booking.getFlight().getFlightID());
            System.out.println("Seat: " + seatNumber);
            System.out.println("Boarding Time: " + boardingTime);
            System.out.println("Gate: " + gateNumber);
            System.out.println("Status: " + ticketStatus);
            System.out.println("Barcode: " + barcode);
            System.out.println("============================");
            return true;
        }
        return false;
    }

}
