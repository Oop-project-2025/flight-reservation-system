package airprort_system;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class Administrator extends User {
    private String adminId;
    private String department;
    private List<String> permissions;
    
    @Override
    public void accessDashBoared() {
        System.out.println("Administrator Dashboard Accessed");
    }
    
     public Administrator(String adminId, String department, List<String> permissions, String userID, String username, String email, String passwordHash, String role, int phoneNumber) {
        super(userID, username, email, passwordHash, role, phoneNumber);
        this.adminId = adminId;
        this.department = department;
        this.permissions = permissions;
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

    public boolean manageUserAccounts(String userId, String action) {
        if (userId != null && !userId.isEmpty() && action != null && !action.isEmpty()) {
            System.out.println("Action '" + action + "' applied to user ID: " + userId);
            return true;
        } else {
            System.out.println("Error: Invalid user ID or action.");
            return false;
        }
    }
    public SystemReport generateSystemReport() {
        
        System.out.println("System report generated.");
        return new SystemReport();
    }

    public boolean configureSystemSettings(Map<String, String> settings) {
        
        settings.forEach((k, v) -> System.out.println("Setting " + k + " = " + v));
        return true;
    }

    public boolean addToBlacklist(User user, String reason) {
        System.out.println("User " + user.getUsername() + " added to blacklist. Reason: " + reason);
        return true;
    }

    public List<SystemLog> viewSystemLogs() {
        System.out.println("System logs viewed.");
        return List.of(); 
    }

    
    public String getAdminId() {
        return adminId;
    }

    public String getDepartment() {
        return department;
    }

    public List<String> getPermissions() {
        return permissions;
    }
}



 class SystemLog {
    private String logId;
    private Date timestamp;
    private String userId;
    private String action;
    private String description;
    private String level; 

    
    public SystemLog(String logId, Date timestamp, String userId, String action, String description, String level) {
        this.logId = logId;
        this.timestamp = timestamp;
        this.userId = userId;
        this.action = action;
        this.description = description;
        this.level = level;
    }

    
    public String getLogId() {
        return logId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getUserId() {
        return userId;
    }

    public String getAction() {
        return action;
    }

    public String getDescription() {
        return description;
    }

    public String getLevel() {
        return level;
    }

    
    @Override
    public String toString() {
        return "[" + level + "] " + timestamp + " | User: " + userId + " | Action: " + action + " | " + description;
    }
}




 class SystemReport {
    private String reportId;
    private Date generatedAt;
    private int totalUsers;
    private int totalFlights;
    private int totalBookings;
    private double totalRevenue;
    private String generatedByAdminId;

    
    public SystemReport() {
        
        this.generatedAt = new Date();
    }

    public SystemReport(String reportId, Date generatedAt, int totalUsers, int totalFlights,
                        int totalBookings, double totalRevenue, String generatedByAdminId) {
        this.reportId = reportId;
        this.generatedAt = generatedAt;
        this.totalUsers = totalUsers;
        this.totalFlights = totalFlights;
        this.totalBookings = totalBookings;
        this.totalRevenue = totalRevenue;
        this.generatedByAdminId = generatedByAdminId;
    }

    
    public String getReportId() {
        return reportId;
    }

    public Date getGeneratedAt() {
        return generatedAt;
    }

    public int getTotalUsers() {
        return totalUsers;
    }

    public int getTotalFlights() {
        return totalFlights;
    }

    public int getTotalBookings() {
        return totalBookings;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public String getGeneratedByAdminId() {
        return generatedByAdminId;
    }

    
    @Override
    public String toString() {
        return "System Report\nGenerated At: " + generatedAt +
                "\nTotal Users: " + totalUsers +
                "\nTotal Flights: " + totalFlights +
                "\nTotal Bookings: " + totalBookings +
                "\nTotal Revenue: $" + totalRevenue +
                "\nGenerated by Admin ID: " + generatedByAdminId;
    }
}
