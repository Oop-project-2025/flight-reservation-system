package airprort_system;

import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.time.LocalDateTime;

public class Flight {
    
    String flihtID;
    String flightNumber;
    Airport departureAirport;
    Airport arrivalAirport;
    Date departureTime;
    Date arrivalTime;
    String airCraftType;
    int totalSeats;
    Map<String,Double> fare;
    String status;
    List<String> crewMembers;

    public Flight(String flihtID, String flightNumber, Airport departureAirport, 
            Airport arrivalAirport, Date departureTime, Date arrivalTime, 
            String airCraftType, int totalSeats, Map<String, Double> fare
            , String status, List<String> crewMembers) {
        
        this.flihtID = flihtID;
        this.flightNumber = flightNumber;
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.airCraftType = airCraftType;
        this.totalSeats = totalSeats;
        this.fare = fare;
        this.status = status;
        this.crewMembers = crewMembers;
    }

    public String getFlihtID() {
        return flihtID;
    }

    public void setFlihtID(String flihtID) {
        this.flihtID = flihtID;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public Airport getDepartureAirport() {
        return departureAirport;
    }

    public void setDepartureAirport(Airport departureAirport) {
        this.departureAirport = departureAirport;
    }

    public Airport getArrivalAirport() {
        return arrivalAirport;
    }

    public void setArrivalAirport(Airport arrivalAirport) {
        this.arrivalAirport = arrivalAirport;
    }

    public Date getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Date departureTime) {
        this.departureTime = departureTime;
    }

    public Date getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Date arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getAirCraftType() {
        return airCraftType;
    }

    public void setAirCraftType(String airCraftType) {
        this.airCraftType = airCraftType;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }

    public Map<String, Double> getFare() {
        return fare;
    }

    public void setFare(Map<String, Double> fare) {
        this.fare = fare;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getCrewMembers() {
        return crewMembers;
    }

    public void setCrewMembers(List<String> crewMembers) {
        this.crewMembers = crewMembers;
    }
    

    public void setFare(String seatClass, double price) {
        fare.put(seatClass.toLowerCase(), price);
    }
    
    public double getFare(String seatClass) {
        return fare.getOrDefault(seatClass.toLowerCase(), 0.0);
    }

    public int checkAvailability(String seatClass) {
        return totalSeats;
    }
    
    public boolean updateStatus(String newStatus) {
        if (newStatus.equals("on-time") || newStatus.equals("delayed") || newStatus.equals("cancelled")) {
            status = newStatus;
            return true;
        }
        return false;
    }
    
    public Duration calculateDuration() {
       return Duration.between(departureTime.toInstant(), arrivalTime.toInstant());

    }
    
    public boolean reserveSeats(int quantity) {
        if (quantity > 0 && totalSeats >= quantity) {
            totalSeats -= quantity;
            return true;
        }
        return false;
    }
    
   public boolean releaseSeats(int quantity, String seatClass) {
    if (quantity > 0) {
        totalSeats += quantity;  
        return true;
    }
    return false;
}



    public void addCrewMember(String crewId) {
        if (!crewMembers.contains(crewId)) {
            crewMembers.add(crewId);
        }
    }
    
    public void removeCrewMember(String crewId) {
        crewMembers.remove(crewId);
    }
    
    @Override
    public String toString() {
        return flightNumber + " from " + departureAirport.getAirportCode() + 
               " to " + arrivalAirport.getAirportCode() + " (" + status + ")";
    }

    void reserveSeats(String seatClass, double size) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    
    
}
