package airprort_system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID; // For generating unique IDs

public class RegisterPage extends JPanel {
    private JTextField usernameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextField phoneNumberField;
    private CardLayout cardLayout; // Added for navigation
    private JPanel parentPanel; // Added for navigation

    public RegisterPage(CardLayout cardLayout, JPanel parent) {
        this.cardLayout = cardLayout; // Initialize
        this.parentPanel = parent; // Initialize
        setLayout(new BorderLayout());
        
        // Background panel
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(20, 30, 50)); // Dark blue background
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        backgroundPanel.setLayout(new BorderLayout());
        
        // Form panel
        JPanel formPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(new Color(0, 0, 0, 180)); // Semi-transparent black
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30); // Rounded corners
            }
        };
        formPanel.setOpaque(false); // Make it transparent
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(40, 100, 40, 100)); // Padding
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 5, 10, 5); // Padding between components
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Title
        JLabel titleLabel = new JLabel("Register");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(titleLabel, gbc);
        
        // Username Field
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(Color.WHITE);
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(usernameLabel, gbc);
        usernameField = createStyledTextField(""); gbc.gridx = 1; gbc.gridy = 1; formPanel.add(usernameField, gbc);

        // Email Field
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setForeground(Color.WHITE);
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 0; gbc.gridy = 2; formPanel.add(emailLabel, gbc);
        emailField = createStyledTextField(""); gbc.gridx = 1; gbc.gridy = 2; formPanel.add(emailField, gbc);
        
        // Password Field
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(Color.WHITE);
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 0; gbc.gridy = 3; formPanel.add(passwordLabel, gbc);
        passwordField = createStyledPasswordField(); gbc.gridx = 1; gbc.gridy = 3; formPanel.add(passwordField, gbc);
        
        // Confirm Password Field
        JLabel confirmLabel = new JLabel("Confirm Password:");
        confirmLabel.setForeground(Color.WHITE);
        confirmLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 0; gbc.gridy = 4; formPanel.add(confirmLabel, gbc);
        confirmPasswordField = createStyledPasswordField(); gbc.gridx = 1; gbc.gridy = 4; formPanel.add(confirmPasswordField, gbc);

        // Phone Number Field
        JLabel phoneLabel = new JLabel("Phone Number:");
        phoneLabel.setForeground(Color.WHITE);
        phoneLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 0; gbc.gridy = 5; formPanel.add(phoneLabel, gbc);
        phoneNumberField = createStyledTextField(""); gbc.gridx = 1; gbc.gridy = 5; formPanel.add(phoneNumberField, gbc);
        
        // Register Button
        JButton registerButton = createStyledButton("REGISTER", new Color(34, 139, 34)); // Green
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER; gbc.insets = new Insets(30, 5, 10, 5);
        formPanel.add(registerButton, gbc);
        
        registerButton.addActionListener(this::performRegistration); // Use a separate method for logic
        
        // Back Button
        JButton backButton = createBackButton();
        gbc.gridy = 7;
        gbc.insets = new Insets(10, 5, 5, 5);
        formPanel.add(backButton, gbc);
        
        backButton.addActionListener((ActionEvent e) -> {
            cardLayout.show(parentPanel, "HOME");
        });
        
        // Center the form panel
        backgroundPanel.add(formPanel, BorderLayout.CENTER);
        add(backgroundPanel);
    }

    private void performRegistration(ActionEvent e) {
    String username = usernameField.getText();
    String email = emailField.getText();
    String password = new String(passwordField.getPassword());
    String confirmPassword = new String(confirmPasswordField.getPassword());
    String phoneNumberStr = phoneNumberField.getText();

    if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || phoneNumberStr.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Registration Error", JOptionPane.WARNING_MESSAGE);
        return;
    }

    if (!password.equals(confirmPassword)) {
        JOptionPane.showMessageDialog(this, "Passwords do not match.", "Registration Error", JOptionPane.WARNING_MESSAGE);
        return;
    }

    int phoneNumber;
    try {
        phoneNumber = Integer.parseInt(phoneNumberStr);
    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this, "Invalid phone number format. Please enter digits only.", "Registration Error", JOptionPane.WARNING_MESSAGE);
        return;
    }

    String userID = UUID.randomUUID().toString();

    // Database insertion logic
    Connection conn = null;
    try {
        conn = DatabaseConnection.connect();
        if (conn == null) {
            JOptionPane.showMessageDialog(this, "Database connection failed.", "Registration Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if email already exists
        String checkEmailSQL = "SELECT COUNT(*) FROM users WHERE email = ?";
        try (PreparedStatement checkPstmt = conn.prepareStatement(checkEmailSQL)) {
            checkPstmt.setString(1, email);
            try (ResultSet rs = checkPstmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    JOptionPane.showMessageDialog(this, "Registration failed: Email already exists.", "Registration Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }
        }

        conn.setAutoCommit(false); // Start transaction

        // Insert into users table with registration date
        String insertUserSQL = "INSERT INTO users (user_id, username, email, password_hash, role, phone_number, address, registration_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmtUser = conn.prepareStatement(insertUserSQL)) {
            pstmtUser.setString(1, userID);
            pstmtUser.setString(2, username);
            pstmtUser.setString(3, email);
            pstmtUser.setString(4, password);
            pstmtUser.setString(5, "CUSTOMER");
            pstmtUser.setInt(6, phoneNumber);
            pstmtUser.setString(7, "");
            pstmtUser.setTimestamp(8, new java.sql.Timestamp(System.currentTimeMillis())); // Set registration date
            pstmtUser.executeUpdate();
        }

        // Insert into customers table
        String insertCustomerSQL = "INSERT INTO customers (customer_id, user_id) VALUES (?, ?)";
        try (PreparedStatement pstmtCustomer = conn.prepareStatement(insertCustomerSQL)) {
            pstmtCustomer.setString(1, userID);
            pstmtCustomer.setString(2, userID);
            pstmtCustomer.executeUpdate();
        }

        conn.commit(); // Commit transaction
        JOptionPane.showMessageDialog(this, "Registration Successful! You can now log in.", "Success", JOptionPane.INFORMATION_MESSAGE);
        // Log registration
try (PreparedStatement logStmt = conn.prepareStatement(
    "INSERT INTO system_logs (log_id, user_id, action, log_type, log_time) VALUES (?, ?, ?, ?, NOW())")) {
    logStmt.setString(1, "LOG" + UUID.randomUUID().toString().substring(0, 8));
    logStmt.setString(2, userID);
    logStmt.setString(3, "User registered with email: " + email);
    logStmt.setString(4, "REGISTER");
    logStmt.executeUpdate();
}

        // Clear fields after successful registration
        usernameField.setText("");
        emailField.setText("");
        passwordField.setText("");
        confirmPasswordField.setText("");
        phoneNumberField.setText("");
        cardLayout.show(parentPanel, "LOGIN"); // Go to login page

    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Database error during registration: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
        if (conn != null) {
            try {
                conn.rollback(); // Rollback transaction on error
            } catch (SQLException rollbackEx) {
                System.err.println("Rollback failed: " + rollbackEx.getMessage());
            }
        }
    } finally {
        if (conn != null) {
            try {
                conn.close(); // Close connection
            } catch (SQLException closeEx) {
                System.err.println("Error closing connection: " + closeEx.getMessage());
            }
        }
    }
}
    
    private JTextField createStyledTextField(String initialText) {
        JTextField field = new JTextField(initialText);
        field.setFont(new Font("Arial", Font.PLAIN, 16));
        field.setPreferredSize(new Dimension(250, 35));
        field.setBackground(new Color(60, 60, 60));
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 100, 100), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return field;
    }
    
    private JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField(20);
        field.setFont(new Font("Arial", Font.PLAIN, 16));
        field.setPreferredSize(new Dimension(250, 35));
        field.setBackground(new Color(60, 60, 60));
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 100, 100), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return field;
    }
    
    private JButton createStyledButton(String text, Color baseColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setPreferredSize(new Dimension(250, 50));
        button.setForeground(Color.WHITE);
        button.setBackground(baseColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(baseColor.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(baseColor);
            }
        });
        
        return button;
    }
    
    private JButton createBackButton() {
        JButton button = new JButton("BACK");
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setForeground(Color.WHITE);
        button.setContentAreaFilled(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setForeground(new Color(150, 150, 255));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setForeground(Color.WHITE);
            }
        });
        return button;
    }
}
