package airprort_system;


import java.util.Date;

class Passengers {

    static double size() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    static void add(Passengers passenger) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    static Passengers get(int i) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    static void remove(int i) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
      private String passengerId;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private String passportNumber;
    private String nationality;
    private String gender;
    private boolean specialAssistance;
    private String mealPreference;

    private String seatNumber;
    private String ticketNumber;
    private float discount;



    public Passengers() {
       this.passengerId = "number";
        this.firstName = "Unknown";
        this.lastName = "Unknown";
        this.dateOfBirth = new Date(); 
        this.passportNumber = "number";
        this.nationality = "nationality";
        this.gender = "gender";
        this.specialAssistance = false; 
        this.mealPreference = "meal preference";
        this.seatNumber = "N/A";
        this.ticketNumber = "N/A";
        this.discount = 0.0f;
    }
    public Passengers(String passengerId, String firstName, String lastName, Date dateOfBirth, 
                     String passportNumber, String nationality, String gender, 
                     boolean specialAssistance, String mealPreference, 
                     String seatNumber, String ticketNumber, float discount) {
        this.passengerId = passengerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.passportNumber = passportNumber;
        this.nationality = nationality;
        this.gender = gender;
        this.specialAssistance = specialAssistance;
        this.mealPreference = mealPreference;
        this.seatNumber = seatNumber;
        this.ticketNumber = ticketNumber;
        this.discount = discount;
    }

    public String getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(String passengerId) {
        this.passengerId = passengerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean isSpecialAssistance() {
        return specialAssistance;
    }

    public void setSpecialAssistance(boolean specialAssistance) {
        this.specialAssistance = specialAssistance;
    }

    public String getMealPreference() {
        return mealPreference;
    }

    public void setMealPreference(String mealPreference) {
        this.mealPreference = mealPreference;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }
    
    
   public void displayInfo() {
    System.out.println("=== Passenger Information ===");
    System.out.println("Passenger ID: " + passengerId);
    System.out.println("First Name: " + firstName);
    System.out.println("Last Name: " + lastName);
    System.out.println("Date of Birth: " + dateOfBirth);
    System.out.println("Passport Number: " + passportNumber);
    System.out.println("Nationality: " + nationality);
    System.out.println("Gender: " + gender);
    System.out.println("Special Assistance Required: " + (specialAssistance ? "Yes" : "No"));
    System.out.println("Meal Preference: " + mealPreference);
    System.out.println("Seat Number: " + seatNumber);
    System.out.println("Ticket Number: " + ticketNumber);
    System.out.println("Discount: " + discount + "%");
}
    
}