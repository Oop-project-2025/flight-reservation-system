package airprort_system;


import java.time.LocalDateTime;

class Receipt {
    private String paymentId;
    private double amount;
    private LocalDateTime paymentDate;
    private String paymentMethod;
    private String transactionId;

    public Receipt(String paymentId, double amount, LocalDateTime paymentDate,
                   String paymentMethod, String transactionId) {
        this.paymentId = paymentId;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.paymentMethod = paymentMethod;
        this.transactionId = transactionId;
    }

    public void displayReceipt() {
        System.out.println("=== Payment Receipt ===");
        System.out.println("Payment ID: " + paymentId);
        System.out.println("Amount: $" + amount);
        System.out.println("Date: " + paymentDate);
        System.out.println("Method: " + paymentMethod);
        System.out.println("Transaction ID: " + transactionId);
        System.out.println("========================");
    }
    
}