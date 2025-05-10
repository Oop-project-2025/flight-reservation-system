package airprort_system;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class Administrator extends User {
    private String adminId;
    private String department;
    private List<String> permissions;
    
    @Override
    public void accessDashBoared(){};

    // Constructor
    public Administrator(String adminId, String department, List<String> permissions, String userID, String username, String email, String passwordHash, String role, int phoneNumber) {
        super(userID, username, email, passwordHash, role, phoneNumber);
        this.adminId = adminId;
        this.department = department;
        this.permissions = permissions;
    }

    // Methods from UML
    public boolean addFlight(Flight flight) {
        // Logic to add flight to system
        System.out.println("Flight added: " + flight.getFlightNumber());
        return true;
    }

    public boolean removeFlight(String flightId) {
        // Logic to remove flight by ID
        System.out.println("Flight removed: " + flightId);
        return true;
    }

    public boolean updateFlightDetails(Flight flight) {
        // Logic to update flight details
        System.out.println("Flight updated: " + flight.getFlightNumber());
        return true;
    }

    public boolean manageUserAccounts(String userId, String action) {
        // Example actions: "disable", "enable", "delete"
        System.out.println("Action '" + action + "' applied to user ID: " + userId);
        return true;
    }

    public SystemReport generateSystemReport() {
        // Generate a dummy report
        System.out.println("System report generated.");
        return new SystemReport();
    }

    public boolean configureSystemSettings(Map<String, String> settings) {
        // Apply system settings
        settings.forEach((k, v) -> System.out.println("Setting " + k + " = " + v));
        return true;
    }

    public boolean addToBlacklist(User user, String reason) {
        System.out.println("User " + user.getUsername() + " added to blacklist. Reason: " + reason);
        return true;
    }

    public List<SystemLog> viewSystemLogs() {
        System.out.println("System logs viewed.");
        return List.of(); // Dummy list
    }

    // Getters and setters
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
    private String level; // INFO, WARNING, ERROR

    // Constructor
    public SystemLog(String logId, Date timestamp, String userId, String action, String description, String level) {
        this.logId = logId;
        this.timestamp = timestamp;
        this.userId = userId;
        this.action = action;
        this.description = description;
        this.level = level;
    }

    // Getters
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

    // To String
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

    // Constructor
    public SystemReport() {
        // يمكن توليد البيانات بشكل ديناميكي من قاعدة البيانات لاحقًا
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

    // Getters
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

    // To String
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
