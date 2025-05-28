package airprort_system;

import static java.time.LocalDate.now;
import java.time.LocalDateTime;
import java.util.Date;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public abstract class User {
    String userID;
    String username;
    String email;
    String passwordHash;
    private String accountStatus;
    String role;
    private String address; // Added address field
    private String lastLoginIP;
    private Date dateOfBirth;
    private Date regestrationDate;
    private Date lastLogin;
    private Boolean isVerified;
    private Boolean twoFactorAuthorization;
    private int loginAttempts;
    private int phoneNumber;
    private Customer currentCustomer; // Seems like a redundant field, consider removing if not directly used
    private List<String> blacklistedCustomers = new ArrayList<>();
    private boolean isBlacklisted;
    private String blackListID;
    private String blacklistReason;


    public User(String userID, String username, String email, String passwordHash, String role, int phoneNumber) {
        this.userID = userID;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
        this.phoneNumber=phoneNumber;
        this.accountStatus = "Active"; // Default status
        this.regestrationDate = new Date(); // Set registration date
        this.isVerified = false;
        this.twoFactorAuthorization = false;
        this.loginAttempts = 0;
        this.address = ""; // Default empty address
    }
    
    // New constructor to include address
    public User(String userID, String username, String email, String passwordHash, String role, int phoneNumber, String address) {
        this(userID, username, email, passwordHash, role, phoneNumber); // Call existing constructor
        this.address = address;
    }

    public abstract void accessDashBoared();

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUsername() { // Made public
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLastLoginIP() {
        return lastLoginIP;
    }

    public void setLastLoginIP(String lastLoginIP) {
        this.lastLoginIP = lastLoginIP;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Date getRegestrationDate() {
        return regestrationDate;
    }

    public void setRegestrationDate(Date regestrationDate) {
        this.regestrationDate = regestrationDate;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public Boolean getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(Boolean isVerified) {
        this.isVerified = isVerified;
    }

    public Boolean getTwoFactorAuthorization() {
        return twoFactorAuthorization;
    }

    public void setTwoFactorAuthorization(Boolean twoFactorAuthorization) {
        this.twoFactorAuthorization = twoFactorAuthorization;
    }

    public int getLoginAttempts() {
        return loginAttempts;
    }

    public void setLoginAttempts(int loginAttempts) {
        this.loginAttempts = loginAttempts;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Customer getCurrentCustomer() {
        return currentCustomer;
    }

    public void setCurrentCustomer(Customer currentCustomer) {
        this.currentCustomer = currentCustomer;
    }

    public List<String> getBlacklistedCustomers() {
        return blacklistedCustomers;
    }

    public void setBlacklistedCustomers(List<String> blacklistedCustomers) {
        this.blacklistedCustomers = blacklistedCustomers;
    }

    public boolean isBlacklisted() {
        return isBlacklisted;
    }

    public void setIsBlacklisted(boolean isBlacklisted) {
        this.isBlacklisted = isBlacklisted;
    }

    public String getBlackListID() {
        return blackListID;
    }

    public void setBlackListID(String blackListID) {
        this.blackListID = blackListID;
    }

    public String getBlacklistReason() {
        return blacklistReason;
    }

    public void setBlacklistReason(String blacklistReason) {
        this.blacklistReason = blacklistReason;
    }

    public void blacklistUser(String id, String reason) {
        this.isBlacklisted = true;
        this.blackListID = id;
        this.blacklistReason = reason;
    }

    public void removeFromBlacklist() {
        this.isBlacklisted = false;
        this.blackListID = null;
        this.blacklistReason = null;
    }

    public boolean login(String inputEmail,String passwordHash,String ip){
        if(this.email.equals(inputEmail) && this.passwordHash.equals(passwordHash)) {
            this.lastLogin = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
            this.lastLoginIP= ip;
            this.loginAttempts=0;
            System.out.println("Login successful for " + username);
            return true;
        } else {
            this.loginAttempts++;
            System.out.println("Login failed");
            System.out.println("Attempts : "+loginAttempts);
            return false;
        }
    }
    
    public void logout(){
        System.out.println(username+" has logged out");
    }
    
    public void updateProfile(int newPhoneNumber,String newAddress){
        this.phoneNumber=newPhoneNumber;
        this.address=newAddress;
        System.out.println("The profile is updated in memory.");
    }
    
    public void verify(){
        this.isVerified=true;
        System.out.println("Your account is now verified");
    }
    
    public boolean approveRefund(int bookingID, int customerID, double amount, String currentStatus) {
if (!currentStatus.equals("Pending")) {
System.out.println("Refund request is not pending or already processed.");
return false;
}

String newStatus = "Approved";
System.out.println("Refund approved:");
System.out.println("Booking ID: " + bookingID);
System.out.println("Customer ID: " + customerID);
System.out.println("Amount: " + amount);
System.out.println("New Status: " + newStatus);

// Again, this can be extended to update real storage or interface
return true;
}
}
