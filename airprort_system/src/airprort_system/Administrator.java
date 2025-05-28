package airprort_system;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ArrayList; // Added for List initialization

import java.sql.Connection; // Needed for JDBC
import java.sql.PreparedStatement; // Needed for JDBC
import java.sql.ResultSet; // Needed for JDBC
import java.sql.SQLException; // Needed for JDBC

public class Administrator extends User {
    private String adminId;
    private String department;
    private List<String> permissions;
    
    @Override
    public void accessDashBoared() {
        System.out.println("Administrator Dashboard Accessed");
    }
    
    // Constructor matching the User superclass constructor that takes address
    public Administrator(String adminId, String department, List<String> permissions, String userID, String username, String email, String passwordHash, String role, int phoneNumber, String address) {
        super(userID, username, email, passwordHash, role, phoneNumber, address);
        this.adminId = adminId;
        this.department = department;
        this.permissions = permissions != null ? permissions : new ArrayList<>();
    }

    // Original constructor (ensure it calls super correctly)
    public Administrator(String adminId, String department, List<String> permissions, String userID, String username, String email, String passwordHash, String role, int phoneNumber) {
        super(userID, username, email, passwordHash, role, phoneNumber);
        this.adminId = adminId;
        this.department = department;
        this.permissions = permissions != null ? permissions : new ArrayList<>();
    }
    
    public boolean addFlight(Flight flight) {
        if (flight != null) {
            System.out.println("Flight added: " + flight.getFlightNumber());
            return true;
        } else {
            System.out.println("Error: Cannot add a null flight.");
            return false;
        }
    }

    public boolean removeFlight(String flightId) {
        if (flightId != null && !flightId.isEmpty()) {
            System.out.println("Flight removed: " + flightId);
            return true;
        } else {
            System.out.println("Error: Invalid flight ID.");
            return false;
        }
    }

    public boolean updateFlightDetails(Flight flight) {
        if (flight != null) {
            System.out.println("Flight updated: " + flight.getFlightNumber());
            return true;
        } else {
            System.out.println("Error: Cannot update a null flight.");
            return false;
        }
    }

    /**
     * Generates a system report by fetching data directly from the database.
     * @return A SystemReport object populated with current database statistics.
     */
    public SystemReport generateSystemReport() { // Removed BookingSystem parameter
        int totalUsers = 0;
        int totalFlights = 0;
        int totalBookings = 0;
        double totalRevenue = 0.0;

        try (Connection conn = DatabaseConnection.connect()) {
            if (conn == null) {
                System.err.println("Failed to connect to database for report generation.");
                // Return a report with default (zero) values if connection fails
                return new SystemReport("SR" + System.currentTimeMillis(), new Date(), 0, 0, 0, 0.0, this.adminId);
            }

            // Get total users
            String usersSql = "SELECT COUNT(*) FROM users";
            try (PreparedStatement pstmt = conn.prepareStatement(usersSql);
                 ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    totalUsers = rs.getInt(1);
                }
            }

            // Get total flights
            String flightsSql = "SELECT COUNT(*) FROM flight";
            try (PreparedStatement pstmt = conn.prepareStatement(flightsSql);
                 ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    totalFlights = rs.getInt(1);
                }
            }

            // Get total bookings and total revenue
            String bookingsSql = "SELECT COUNT(*), SUM(total_price) FROM booking WHERE booking_status = 'confirmed'";
            try (PreparedStatement pstmt = conn.prepareStatement(bookingsSql);
                 ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    totalBookings = rs.getInt(1);
                    totalRevenue = rs.getDouble(2);
                }
            }

        } catch (SQLException e) {
            System.err.println("SQL Error during system report generation: " + e.getMessage());
            e.printStackTrace();
            // Return a report with default (zero) values in case of SQL error
            return new SystemReport("SR" + System.currentTimeMillis(), new Date(), 0, 0, 0, 0.0, this.adminId);
        }

        String reportId = "SR" + System.currentTimeMillis();
        return new SystemReport(reportId, new Date(), totalUsers, totalFlights, totalBookings, totalRevenue, this.adminId);
    }

    public boolean manageUsers(User user, String action) {
        if (user == null || action == null || action.isEmpty()) {
            System.out.println("Error: Invalid user or action.");
            return false;
        }
        System.out.println("User " + user.getUsername() + " managed with action: " + action);
        return true;
    }

    public boolean viewLogs() {
        System.out.println("Viewing system logs...");
        return true;
    }

    public boolean manageAirports(Airport airport, String action) {
        if (airport == null || action == null || action.isEmpty()) {
            System.out.println("Error: Invalid airport or action.");
            return false;
        }
        System.out.println("Airport " + airport.getName() + " managed with action: " + action);
        return true;
    }

    @Override
    public String getUsername() {
        return super.getUsername(); // Call superclass method
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }
}
