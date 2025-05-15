package airprort_system;

import java.time.LocalDateTime;

class Payment {
     private String paymentId;
    private Booking booking;
    private double amount;
    private LocalDateTime paymentDate;
    private String paymentMethod; 
    private String paymentStatus; 
    private String transactionId;
    private String cardLastFourDigits;
    
    public Payment(String paymentId, Booking booking, double amount, LocalDateTime paymentDate,
                   String paymentMethod, String paymentStatus, String transactionId,
                   String cardLastFourDigits) {
        this.paymentId = paymentId;
        this.booking = booking;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.transactionId = transactionId;
        this.cardLastFourDigits = cardLastFourDigits;
    }
    public boolean processPayment() {
        
        System.out.println("Processing payment of $" + amount + " via " + paymentMethod);
        this.paymentStatus = "Completed";
        this.transactionId = "TXN" + System.currentTimeMillis(); // dummy transaction id
        return true;
    }

    
    public boolean verifyPayment() {
        
        return "Completed".equalsIgnoreCase(paymentStatus);
    }public Receipt generateReceipt() {
        return new Receipt(paymentId, amount, paymentDate, paymentMethod, transactionId);
    }

    
    public boolean refundPayment() {
        if ("Completed".equalsIgnoreCase(paymentStatus)) {
            System.out.println("Refunding payment of $" + amount);
            this.paymentStatus = "Refunded";
            return true;
        } else {
            System.out.println("Payment cannot be refunded. Status: " + paymentStatus);
            return false;
        }
    }
}
    