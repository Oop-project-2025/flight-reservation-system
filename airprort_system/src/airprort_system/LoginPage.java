package airprort_system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Enumeration; // For ButtonGroup iteration
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID; // For generating unique IDs
import java.util.List; // For List
import java.util.ArrayList; // For ArrayList
import java.util.Arrays; // For Arrays.asList (if using comma-separated string)

public class LoginPage extends JPanel {
    private JTextField emailField;
    private JPasswordField passwordField;
    private ButtonGroup userTypeGroup;
    private CardLayout cardLayout; // Added for navigation
    private JPanel parentPanel; // Added for navigation

    public LoginPage(CardLayout cardLayout, JPanel parent) {
        this.cardLayout = cardLayout; // Initialize
        this.parentPanel = parent; // Initialize
        setLayout(new BorderLayout());
        
        // Main background panel
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
        formPanel.setOpaque(false); // Make it transparent to show background
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(40, 100, 40, 100)); // Padding
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 5, 10, 5); // Padding between components
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Title
        JLabel titleLabel = new JLabel("Login");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Span across two columns
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(titleLabel, gbc);
        
        // Email Field
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setForeground(Color.WHITE);
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(emailLabel, gbc);

        emailField = createStyledTextField();
        gbc.gridx = 1;
        formPanel.add(emailField, gbc);
        
        // Password Field
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(Color.WHITE);
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(passwordLabel, gbc);

        passwordField = createStyledPasswordField();
        gbc.gridx = 1;
        formPanel.add(passwordField, gbc);

        // User Type Radio Buttons
        JLabel userTypeLabel = new JLabel("Login As:");
        userTypeLabel.setForeground(Color.WHITE);
        userTypeLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(userTypeLabel, gbc);

        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        radioPanel.setOpaque(false);
        userTypeGroup = new ButtonGroup();

        JRadioButton customerRadio = createStyledRadioButton("Customer", "CUSTOMER");
        JRadioButton agentRadio = createStyledRadioButton("Agent", "AGENT");
        JRadioButton adminRadio = createStyledRadioButton("Admin", "ADMINISTRATOR");

        customerRadio.setSelected(true); // Default selection

        userTypeGroup.add(customerRadio);
        userTypeGroup.add(agentRadio);
        userTypeGroup.add(adminRadio);

        radioPanel.add(customerRadio);
        radioPanel.add(agentRadio);
        radioPanel.add(adminRadio);

        gbc.gridx = 1;
        formPanel.add(radioPanel, gbc);
        
        // Login Button
        JButton loginButton = createStyledButton("LOGIN", new Color(0, 153, 51)); // Green
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(30, 5, 10, 5); // More top padding for button
        formPanel.add(loginButton, gbc);
        
        loginButton.addActionListener(this::performLogin); // Use a separate method for logic
        
        // Back Button
        JButton backButton = createBackButton();
        gbc.gridy = 5;
        gbc.insets = new Insets(10, 5, 5, 5); // Padding for back button
        formPanel.add(backButton, gbc);
        
        backButton.addActionListener((ActionEvent e) -> {
            cardLayout.show(parentPanel, "HOME");
        });
        
        // Center the form panel
        backgroundPanel.add(formPanel, BorderLayout.CENTER);
        add(backgroundPanel);
    }

   
   
   
   private void performLogin(ActionEvent e) {
    String email = emailField.getText();
    String password = new String(passwordField.getPassword());
    String selectedRole = getSelectedUserType();

    if (email.isEmpty() || password.isEmpty() || selectedRole == null) {
        JOptionPane.showMessageDialog(this, "Please fill in all fields and select a user type.", "Login Error", JOptionPane.WARNING_MESSAGE);
        return;
    }

    User loggedInUser = null;
    String sql = "SELECT user_id, username, email, password_hash, role, phone_number, address, is_blacklisted FROM users WHERE email = ? AND role = ?";

    try (Connection conn = DatabaseConnection.connect();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        if (conn == null) {
            JOptionPane.showMessageDialog(this, "Database connection failed.", "Login Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        pstmt.setString(1, email);
        pstmt.setString(2, selectedRole);
        
        try (ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                String storedPasswordHash = rs.getString("password_hash");
                if (password.equals(storedPasswordHash)) {
                    // Update last login time
                    String updateLoginSQL = "UPDATE users SET last_login = ? WHERE user_id = ?";
                    try (PreparedStatement updateStmt = conn.prepareStatement(updateLoginSQL)) {
                        updateStmt.setTimestamp(1, new java.sql.Timestamp(System.currentTimeMillis()));
                        updateStmt.setString(2, rs.getString("user_id"));
                        updateStmt.executeUpdate();
                    }

                    String userID = rs.getString("user_id");
                    String username = rs.getString("username");
                    String userEmail = rs.getString("email");
                    int phoneNumber = rs.getInt("phone_number"); // Changed to String
                    String address = rs.getString("address");
                    boolean isBlacklisted = rs.getBoolean("is_blacklisted");
                    
                    if (isBlacklisted) {
                        int choice = JOptionPane.showConfirmDialog(this,
                            "Your account is currently blocked. Would you like to send a request to the admin to unblock it?",
                            "Account Blocked", JOptionPane.YES_NO_OPTION);

                        if (choice == JOptionPane.YES_OPTION) {
                            String notificationId = "NOTIF" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
                            String message = "Unblock request from user: " + email;
                            try (PreparedStatement notifyStmt = conn.prepareStatement(
                                "INSERT INTO notifications (notification_id, user_id, message, notification_time, sender_role) VALUES (?, ?, ?, NOW(), ?)")) {
                                notifyStmt.setString(1, notificationId);
                                notifyStmt.setString(2, rs.getString("user_id"));
                                notifyStmt.setString(3, message);
                                notifyStmt.setString(4, selectedRole);
                                notifyStmt.executeUpdate();
                                JOptionPane.showMessageDialog(this, "Request sent to admin. Please wait for review.", "Request Sent", JOptionPane.INFORMATION_MESSAGE);
                            }
                        }
                        return; // Prevent login
                    }

                    switch (selectedRole) {
                        case "CUSTOMER":
                            loggedInUser = new Customer(userID, username, userEmail, storedPasswordHash, selectedRole, phoneNumber, address);
                            break;
                        case "AGENT":
                            String agentSql = "SELECT agent_id, airline, commission_rate, access_level FROM agents WHERE user_id = ?";
                            try (PreparedStatement agentPstmt = conn.prepareStatement(agentSql)) {
                                agentPstmt.setString(1, userID);
                                try (ResultSet agentRs = agentPstmt.executeQuery()) {
                                    if (agentRs.next()) {
                                        String agentId = agentRs.getString("agent_id");
                                        String airline = agentRs.getString("airline");
                                        double commissionRate = agentRs.getDouble("commission_rate");
                                        int accessLevel = agentRs.getInt("access_level");
                                        loggedInUser = new Agent(agentId, airline, commissionRate, new java.util.ArrayList<>(), accessLevel, userID, username, userEmail, storedPasswordHash, selectedRole, phoneNumber, address);
                                    }
                                }
                            }
                            break;
                        case "ADMINISTRATOR":
                            String adminSql = "SELECT admin_id, department, permissions FROM administrators WHERE user_id = ?";
                            try (PreparedStatement adminPstmt = conn.prepareStatement(adminSql)) {
                                adminPstmt.setString(1, userID);
                                try (ResultSet adminRs = adminPstmt.executeQuery()) {
                                    if (adminRs.next()) {
                                        String adminId = adminRs.getString("admin_id");
                                        String department = adminRs.getString("department");
                                        String permissionsJson = adminRs.getString("permissions");
                                        List<String> permissions = new java.util.ArrayList<>();
                                        loggedInUser = new Administrator(adminId, department, permissions, userID, username, userEmail, storedPasswordHash, selectedRole, phoneNumber, address);
                                    }
                                }
                            }
                            break;
                    }

                    if (loggedInUser != null) {
                        JOptionPane.showMessageDialog(this, "Login Successful! Welcome, " + loggedInUser.getUsername(), "Success", JOptionPane.INFORMATION_MESSAGE);
                        // Navigate to appropriate dashboard
                        // Log login
                        try (PreparedStatement logStmt = conn.prepareStatement(
                            "INSERT INTO system_logs (log_id, user_id, action, log_type, log_time) VALUES (?, ?, ?, ?, NOW())")) {
                            logStmt.setString(1, "LOG" + UUID.randomUUID().toString().substring(0, 8));
                            logStmt.setString(2, rs.getString("user_id"));
                            logStmt.setString(3, "User logged in: " + email);
                            logStmt.setString(4, "LOGIN");
                            logStmt.executeUpdate();
                        }

                        switch (loggedInUser.getRole()) {
                            case "CUSTOMER":
                                CustomerDashboardPage customerDashboard = (CustomerDashboardPage) parentPanel.getComponent(3);
                                customerDashboard.setLoggedInCustomer((Customer) loggedInUser);
                                cardLayout.show(parentPanel, "CUSTOMER_DASHBOARD");
                                break;
                            case "AGENT":
                                AgentDashboardPage agentDashboard = (AgentDashboardPage) parentPanel.getComponent(4);
                                agentDashboard.setLoggedInAgent((Agent) loggedInUser);
                                cardLayout.show(parentPanel, "AGENT_DASHBOARD");
                                break;
                            case "ADMINISTRATOR":
                                AdminDashboardPage adminDashboard = (AdminDashboardPage) parentPanel.getComponent(5);
                                adminDashboard.setLoggedInAdmin((Administrator) loggedInUser);
                                cardLayout.show(parentPanel, "ADMIN_DASHBOARD");
                                break;
                        }
                        emailField.setText("");
                        passwordField.setText("");
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to load user details after login.", "Login Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid email or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid email or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        }
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Database error during login: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
    }
}
   

    private String getSelectedUserType() {
        for (Enumeration<AbstractButton> buttons = userTypeGroup.getElements(); buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();
            if (button.isSelected()) {
                return button.getActionCommand();
            }
        }
        return null;
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField(20);
        field.setFont(new Font("Arial", Font.PLAIN, 16));
        field.setPreferredSize(new Dimension(250, 35));
        field.setBackground(new Color(60, 60, 60)); // Darker background
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE); // Cursor color
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 100, 100), 1), // Subtle border
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

    private JRadioButton createStyledRadioButton(String text, String actionCommand) {
        JRadioButton radioButton = new JRadioButton(text);
        radioButton.setActionCommand(actionCommand); // Set action command for role
        radioButton.setForeground(Color.WHITE);
        radioButton.setOpaque(false); // Make background transparent
        radioButton.setFont(new Font("Arial", Font.PLAIN, 14));
        return radioButton;
    }
    
    private JButton createStyledButton(String text, Color baseColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setPreferredSize(new Dimension(200, 50));
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
        button.setContentAreaFilled(false); // Transparent background
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setForeground(new Color(150, 150, 255)); // Lighter blue on hover
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setForeground(Color.WHITE);
            }
        });
        return button;
    }
}
