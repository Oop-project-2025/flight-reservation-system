package airprort_system;


import java.util.ArrayList;
import java.util.List;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

class BookingSystem {
    
     private List<Flight> availableFlights;
    private List<Booking> bookings;
    private List<User> users;
    private List<String> waitList;  

    public BookingSystem() {
        this.availableFlights = new ArrayList<>();
        this.bookings = new ArrayList<>();
        this.users = new ArrayList<>();
        this.waitList = new ArrayList<>();
    }

public List<Flight> searchFlights(String from, String to, String date) {
    List<Flight> matchedFlights = new ArrayList<>();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    Date searchDate;
    try {
        searchDate = sdf.parse(date);
    } catch (ParseException e) {
        System.out.println("Invalid date format. Use yyyy-MM-dd");
        return matchedFlights;
    }

    for (Flight flight : availableFlights) {
        boolean matchesSource = flight.getDepartureAirport().getAirportCode().equalsIgnoreCase(from);
        boolean matchesDestination = flight.getArrivalAirport().getAirportCode().equalsIgnoreCase(to);

        
        String flightDate = sdf.format(flight.getDepartureTime());
        String targetDate = sdf.format(searchDate);
        boolean matchesDate = flightDate.equals(targetDate);

        if (matchesSource && matchesDestination && matchesDate) {
            matchedFlights.add(flight);
        }
    }

    return matchedFlights;
}


    public String makeBooking(Booking booking) {
        bookings.add(booking);
        return "Booking Confirmed. Booking ID: " + booking.getBookingID();
    }

    public boolean cancelBooking(String bookingId) {
        for (Booking booking : bookings) {
            if (booking.getBookingID().equals(bookingId)) {
                bookings.remove(booking);
                System.out.println("Booking cancelled: " + bookingId);
                return true;
            }
        }
        System.out.println("Booking not found: " + bookingId);
        return false;
    }

    public String checkFlightStatus(String flightId) {
        for (Flight flight : availableFlights) {
            if (flight.getFlightID().equals(flightId)) {
                return flight.getStatus();
            }
        }
        return "Flight not found";
    }

    
    public void generateBookingReport() {
        System.out.println("===== Booking Report =====");
        for (Booking booking : bookings) {
            System.out.println("Booking ID: " + booking.getBookingID());
            System.out.println("Passenger: " + booking.getPassenger());
            System.out.println("Flight ID: " + booking.getFlight().getFlightID());
            System.out.println("--------------------------");
        }
    }

   
    
    public boolean handleWaitlist() {
        if (waitList.isEmpty()) {
            System.out.println("No users in waitlist.");
            return false;
        }

        System.out.println("Processing waitlist:");
        for (String request : waitList) {
            System.out.println("Handling waitlist request: " + request);
        }
        waitList.clear();
        return true;
    }

    public void addFlight(Flight flight) {
        availableFlights.add(flight);
    }

    public void addUser(User user) {
        users.add(user);
    }

    public void addToWaitList(String request) {
        waitList.add(request);
    }
    }