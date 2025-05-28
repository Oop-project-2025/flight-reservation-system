package airprort_system;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp; 
import java.util.Vector; 

import com.google.gson.Gson; 
import com.google.gson.reflect.TypeToken; 
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type; 
import javax.swing.border.TitledBorder;

public class AdminDashboardPage extends JPanel {
    private CardLayout cardLayout;
    private JPanel parentPanel;
    private Administrator loggedInAdmin;

    private JLabel welcomeLabel;

    
    private JTextField flightNumberField;
    private JComboBox<String> departureAirportCodeComboBox;
    private JComboBox<String> arrivalAirportCodeComboBox;
    private JFormattedTextField departureTimeField; 
    private JFormattedTextField arrivalTimeField;   
    private JTextField aircraftTypeField;
    private JSpinner totalSeatsSpinner;
    private JTextField economyFareField;
    private JTextField businessFareField;
    private JTextField firstClassFareField;
    private JComboBox<String> flightStatusComboBox;
    private JTextField crewMembersField; 

    
    private JTextField searchFlightField;
    private JTable flightsTable;
    private DefaultTableModel flightsTableModel;
    private JButton editFlightButton;
    private JButton cancelFlightButton;
    private JButton rescheduleFlightButton;
    private JToggleButton notifyPassengersToggle;

    
    
    private List<String> airportCodes;
    
    private JTextField searchUserField;
    private JComboBox<String> searchUserTypeComboBox;
    private JTable usersTable;
    private DefaultTableModel usersTableModel;
    private JButton editUserProfileButton;
    private JButton resetUserPasswordButton;
    private JButton deactivateDeleteUserButton;
    private JButton addAgentButton; 

    
    private JTextField logDateField; 
    private JTextField logActionField; 
    private JTextField logUserField;
    private JTextArea logDisplayArea;
    private JButton exportLogsButton;
    private JButton errorTraceLogsButton;

    
    private JFormattedTextField reportStartDateField; 
    private JFormattedTextField reportEndDateField;   
    private JComboBox<String> reportTypeComboBox;
    private JTextArea reportDisplayArea;
    private JButton generateReportButton;
    private JButton exportReportButton; 

    
    private JTextField blockUserIdField;
    private JTextField blockReasonField;
    private JButton blockUserButton;
    private JCheckBox notifyUserCheckbox;
    private JCheckBox restrictAccountCheckbox;
    private JTextField unblockUserIdField; 
    private JButton unblockUserButton;     

    
    private JTextField fareMultiplierField;
    private JToggleButton maintenanceModeToggle;
    private JTextArea notificationTemplatesArea;
    private JButton saveSettingsButton;
    private JButton testSettingsButton;

    
    private JTextField searchAirportCodeField;
    private JTextField airportNameField;
    private JTextField airportCityField;
    private JTextField airportCountryField;
    private JTextField airportTimeZoneField;
    private JTextArea airportTerminalsArea; 
    private JTextArea airportFacilitiesArea; 
    private JButton addAirportButton;
    private JButton updateAirportButton;
    private JButton deleteAirportButton;

    
    private JTextField feedbackFlightFilterField;
    private JTextField feedbackCustomerFilterField;
    private JTextArea feedbackDisplayArea;
    private JButton flagAbusiveButton; 
    private JButton exportFeedbacksButton; 

    
    private JTextArea notificationDisplayArea; 
    private JButton markNotificationReadButton; 
    private JButton clearAllNotificationsButton; 

    
    private Gson gson;
    
    private JTable deepfakeUsersTable;
    private DefaultTableModel deepfakeUsersTableModel;
    private JTextArea activityDisplayArea;
    private Color baseColor;


    public AdminDashboardPage(CardLayout cardLayout, JPanel parentPanel) {
    this.cardLayout = cardLayout;
    this.parentPanel = parentPanel;
    this.loggedInAdmin = loggedInAdmin;

    
    loadAirportCodes(); 

    setLayout(new BorderLayout()); 
        this.gson = new Gson(); 

        setLayout(new BorderLayout());
        setName("ADMIN_DASHBOARD");

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(30, 40, 60));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(40, 50, 70));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        welcomeLabel = new JLabel("Welcome, Admin (Placeholder)"); 
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setForeground(Color.WHITE);
        headerPanel.add(welcomeLabel, BorderLayout.WEST);

        JButton logoutButton = createStyledButton("Logout");
        logoutButton.addActionListener(e -> {
            
            
            this.cardLayout.show(this.parentPanel, "HOME");
        });
        headerPanel.add(logoutButton, BorderLayout.EAST);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        
        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(new Color(50, 60, 80));
        sidebarPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        sidebarPanel.setPreferredSize(new Dimension(200, getHeight()));

        
        JPanel contentCards = new JPanel(new CardLayout()); 
        contentCards.setOpaque(false);

        
        addSidebarButton(sidebarPanel, "Add Flight", contentCards, "ADD_FLIGHT");
        addSidebarButton(sidebarPanel, "Manage Flights", contentCards, "MANAGE_FLIGHTS");
        addSidebarButton(sidebarPanel, "Manage Users", contentCards, "MANAGE_USERS");
        addSidebarButton(sidebarPanel, "View System Logs", contentCards, "VIEW_LOGS");
        addSidebarButton(sidebarPanel, "Generate Reports", contentCards, "GENERATE_REPORTS");
        addSidebarButton(sidebarPanel, "Block User", contentCards, "BLOCK_UNBLOCK_USER"); 
        addSidebarButton(sidebarPanel, "System Settings", contentCards, "SYSTEM_SETTINGS");
        addSidebarButton(sidebarPanel, "Manage Airports", contentCards, "MANAGE_AIRPORTS");
        addSidebarButton(sidebarPanel, "View Feedback", contentCards, "VIEW_FEEDBACK");
        addSidebarButton(sidebarPanel, "Deepfake Management", contentCards, "DEEPFAKE_MANAGEMENT");
        addSidebarButton(sidebarPanel, "Notifications", contentCards, "NOTIFICATIONS_PANEL"); 

        
        contentCards.add(createAddFlightPanel(), "ADD_FLIGHT");
        contentCards.add(createManageFlightsPanel(), "MANAGE_FLIGHTS");
        contentCards.add(createManageUsersPanel(), "MANAGE_USERS");
        contentCards.add(createViewLogsPanel(), "VIEW_LOGS");
        contentCards.add(createGenerateReportsPanel(), "GENERATE_REPORTS");
        contentCards.add(createBlockUnblockUserPanel(), "BLOCK_UNBLOCK_USER"); 
        contentCards.add(createSystemSettingsPanel(), "SYSTEM_SETTINGS");
        contentCards.add(createManageAirportsPanel(), "MANAGE_AIRPORTS");
        contentCards.add(createViewFeedbackPanel(), "VIEW_FEEDBACK");
        contentCards.add(createDeepfakeManagementPanel(), "DEEPFAKE_MANAGEMENT");
        contentCards.add(createNotificationPanel(), "NOTIFICATIONS_PANEL"); 

        mainPanel.add(sidebarPanel, BorderLayout.WEST);
        mainPanel.add(contentCards, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);

        
        populateAirportComboBoxes(departureAirportCodeComboBox, arrivalAirportCodeComboBox);
        
        ((CardLayout) contentCards.getLayout()).show(contentCards, "DEEPFAKE_MANAGEMENT");
    }

    public void setLoggedInAdmin(Administrator admin) {
        this.loggedInAdmin = admin;
        welcomeLabel.setText("Welcome, " + loggedInAdmin.getUsername());
    }

    private void addSidebarButton(JPanel sidebar, String text, JPanel contentCards, String cardName) {
        JButton button = createSidebarButton(text);
        button.addActionListener(e -> {
            ((CardLayout) contentCards.getLayout()).show(contentCards, cardName);
            
            if (cardName.equals("MANAGE_FLIGHTS")) {
                populateFlightsTable();
            } else if (cardName.equals("MANAGE_USERS")) {
                searchUsersForManagement();
            } else if (cardName.equals("VIEW_LOGS")) {
                viewSystemLogs();
            } else if (cardName.equals("VIEW_FEEDBACK")) {
                viewFeedbacks();
            } else if (cardName.equals("NOTIFICATIONS_PANEL")) { 
                loadNotificationsForAdmin();
            }
        });
        sidebar.add(Box.createVerticalStrut(10)); 
        sidebar.add(button);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setPreferredSize(new Dimension(180, 40)); 
        button.setForeground(Color.WHITE);
        button.setBackground(Color.DARK_GRAY);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 255, 255, 120), 1),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.GRAY); 
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.DARK_GRAY);
            }
        });
        return button;
    }

    private JButton createSidebarButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(50, 60, 80));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(180, 40));
        button.setMinimumSize(new Dimension(180, 40));
        button.setPreferredSize(new Dimension(180, 40));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(70, 80, 100));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(50, 60, 80));
            }
        });
        return button;
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField(20);
        field.setFont(new Font("Arial", Font.PLAIN, 16));
        field.setPreferredSize(new Dimension(200, 35));
        field.setBackground(new Color(60, 60, 60));
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 100, 100), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return field;
    }

    private JFormattedTextField createStyledFormattedTextField(String format) {
        JFormattedTextField field = null;
        try {
            MaskFormatter formatter = new MaskFormatter(format);
            formatter.setPlaceholderCharacter('_');
            field = new JFormattedTextField(formatter);
        } catch (ParseException e) {
            field = new JFormattedTextField(); 
            System.err.println("Error creating formatted text field: " + e.getMessage());
        }
        field.setColumns(10);
        field.setFont(new Font("Arial", Font.PLAIN, 16));
        field.setPreferredSize(new Dimension(200, 35));
        field.setBackground(new Color(60, 60, 60));
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 100, 100), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return field;
    }

    private JComboBox<String> createStyledComboBox() {
        JComboBox<String> comboBox = new JComboBox<>();
        comboBox.setFont(new Font("Arial", Font.PLAIN, 16));
        comboBox.setPreferredSize(new Dimension(200, 35));
        comboBox.setBackground(new Color(60, 60, 60));
        comboBox.setForeground(Color.WHITE);
        comboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JComponent comp = (JComponent) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                comp.setBackground(isSelected ? new Color(80, 80, 80) : new Color(60, 60, 60));
                comp.setForeground(Color.WHITE);
                return comp;
            }
        });
        return comboBox;
    }

    private JPanel createContentPanel(String title) {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(new Color(200, 200, 200));
        panel.add(titleLabel, BorderLayout.NORTH);

        return panel;
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        return formPanel;
    }

    private void addFormField(JPanel panel, JLabel label, JComponent component, GridBagConstraints gbc, int y) {
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 0, 5, 10);
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(5, 0, 5, 0);
        panel.add(component, gbc);
    }

    
    private JPanel createAddFlightPanel() {
    JPanel panel = createContentPanel("Add New Flight");

    JPanel mainFormPanel = new JPanel();
    mainFormPanel.setLayout(new BoxLayout(mainFormPanel, BoxLayout.Y_AXIS));
    mainFormPanel.setOpaque(false);
    mainFormPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    initializeFormFields();
    populateAirportComboBoxes(departureAirportCodeComboBox, arrivalAirportCodeComboBox);

    mainFormPanel.add(createCompactFlightDetailsSection());
    mainFormPanel.add(Box.createVerticalStrut(10));
    mainFormPanel.add(createCompactFareDetailsSection());
    mainFormPanel.add(Box.createVerticalStrut(10));
    mainFormPanel.add(createCompactCrewDetailsSection());

    JButton addFlightButton = createStyledButton("Add Flight");
    addFlightButton.addActionListener(this::addFlightAction);

    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    buttonPanel.setOpaque(false);
    buttonPanel.add(addFlightButton);

    panel.add(mainFormPanel, BorderLayout.CENTER);
    panel.add(buttonPanel, BorderLayout.SOUTH);

    return panel;
}

private JPanel createCompactFlightDetailsSection() {
    JPanel panel = createSectionPanel("Flight Information");
    GridBagConstraints gbc = createDefaultGBC();

    addFormField(panel, createStyledLabel("Flight Number:"), flightNumberField, gbc, 0, 0);
    addFormField(panel, createStyledLabel("Departure Airport:"), departureAirportCodeComboBox, gbc, 0, 1);
    addFormField(panel, createStyledLabel("Arrival Airport:"), arrivalAirportCodeComboBox, gbc, 1, 0);
    addFormField(panel, createStyledLabel("Departure Time:"), departureTimeField, gbc, 1, 1);
    addFormField(panel, createStyledLabel("Arrival Time:"), arrivalTimeField, gbc, 2, 0);
    addFormField(panel, createStyledLabel("Aircraft Type:"), aircraftTypeField, gbc, 2, 1);
    addFormField(panel, createStyledLabel("Total Seats:"), totalSeatsSpinner, gbc, 3, 0);
    addFormField(panel, createStyledLabel("Status:"), flightStatusComboBox, gbc, 3, 1);

    return panel;
}

private JPanel createCompactFareDetailsSection() {
    JPanel panel = createSectionPanel("Fare Details");
    GridBagConstraints gbc = createDefaultGBC();

    addFormField(panel, createStyledLabel("Economy Fare:"), economyFareField, gbc, 0, 0);
    addFormField(panel, createStyledLabel("Business Fare:"), businessFareField, gbc, 0, 1);
    addFormField(panel, createStyledLabel("First Class Fare:"), firstClassFareField, gbc, 1, 0);

    return panel;
}

private JPanel createCompactCrewDetailsSection() {
    JPanel panel = createSectionPanel("Crew Details");
    GridBagConstraints gbc = createDefaultGBC();

    addFormField(panel, createStyledLabel("Crew Members:"), crewMembersField, gbc, 0, 0);

    return panel;
}

private void addFormField(JPanel panel, JLabel label, JComponent field, GridBagConstraints gbc, int row, int col) {
    gbc.gridx = col * 2;
    gbc.gridy = row;
    panel.add(label, gbc);

    gbc.gridx = col * 2 + 1;
    panel.add(field, gbc);
}

private GridBagConstraints createDefaultGBC() {
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(4, 4, 4, 4);
    gbc.weightx = 0.5;
    return gbc;
}




private JPanel createSectionPanel(String title) {
    JPanel panel = new JPanel(new GridBagLayout());
    panel.setOpaque(false);
    panel.setBorder(BorderFactory.createTitledBorder(
        BorderFactory.createLineBorder(Color.GRAY), title,
        TitledBorder.LEFT, TitledBorder.TOP, 
        new Font("Arial", Font.BOLD, 14), Color.WHITE));
    return panel;
}

private void initializeFormFields() {
    flightNumberField = createStyledTextField();
    departureAirportCodeComboBox = createStyledComboBox();
    arrivalAirportCodeComboBox = createStyledComboBox();
    departureTimeField = createStyledFormattedTextField("####-##-## ##:##:##");
    arrivalTimeField = createStyledFormattedTextField("####-##-## ##:##:##");
    aircraftTypeField = createStyledTextField();
    totalSeatsSpinner = new JSpinner(new SpinnerNumberModel(100, 1, 500, 1));
    ((JSpinner.DefaultEditor) totalSeatsSpinner.getEditor()).getTextField().setBackground(new Color(60, 60, 60));
    ((JSpinner.DefaultEditor) totalSeatsSpinner.getEditor()).getTextField().setForeground(Color.WHITE);
    economyFareField = createStyledTextField();
    businessFareField = createStyledTextField();
    firstClassFareField = createStyledTextField();
    flightStatusComboBox = createStyledComboBox();
    flightStatusComboBox.addItem("Scheduled");
    flightStatusComboBox.addItem("Delayed");
    flightStatusComboBox.addItem("Cancelled");
    flightStatusComboBox.addItem("On Time");
    crewMembersField = createStyledTextField();
}

private JPanel createFlightDetailsSection() {
    JPanel panel = createSectionPanel("Flight Information");
    GridBagConstraints gbc = createDefaultGBC();

    addFormField(panel, createStyledLabel("Flight Number:"), flightNumberField, gbc, 0);
    addFormField(panel, createStyledLabel("Departure Airport:"), departureAirportCodeComboBox, gbc, 1);
    addFormField(panel, createStyledLabel("Arrival Airport:"), arrivalAirportCodeComboBox, gbc, 2);
    addFormField(panel, createStyledLabel("Departure Time:"), departureTimeField, gbc, 3);
    addFormField(panel, createStyledLabel("Arrival Time:"), arrivalTimeField, gbc, 4);
    addFormField(panel, createStyledLabel("Aircraft Type:"), aircraftTypeField, gbc, 5);
    addFormField(panel, createStyledLabel("Total Seats:"), totalSeatsSpinner, gbc, 6);
    addFormField(panel, createStyledLabel("Status:"), flightStatusComboBox, gbc, 7);

    return panel;
}

private JPanel createFareDetailsSection() {
    JPanel panel = createSectionPanel("Fare Details");
    GridBagConstraints gbc = createDefaultGBC();

    addFormField(panel, createStyledLabel("Economy Fare:"), economyFareField, gbc, 0);
    addFormField(panel, createStyledLabel("Business Fare:"), businessFareField, gbc, 1);
    addFormField(panel, createStyledLabel("First Class Fare:"), firstClassFareField, gbc, 2);

    return panel;
}

private JPanel createCrewDetailsSection() {
    JPanel panel = createSectionPanel("Crew Details");
    GridBagConstraints gbc = createDefaultGBC();

    addFormField(panel, createStyledLabel("Crew Members:"), crewMembersField, gbc, 0);

    return panel;
}




    private void populateAirportComboBoxes(JComboBox<String> depComboBox, JComboBox<String> arrComboBox) {
        depComboBox.removeAllItems();
        arrComboBox.removeAllItems();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConnection.connect();
            String sql = "SELECT airport_code FROM airport ORDER BY airport_code";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                String airportCode = rs.getString("airport_code");
                depComboBox.addItem(airportCode);
                arrComboBox.addItem(airportCode);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error fetching airport codes: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void addFlightAction(ActionEvent e) {
        String flightNumber = flightNumberField.getText().trim();
        String departureAirportCode = (String) departureAirportCodeComboBox.getSelectedItem();
        String arrivalAirportCode = (String) arrivalAirportCodeComboBox.getSelectedItem();
        String departureTimeString = departureTimeField.getText().trim();
        String arrivalTimeString = arrivalTimeField.getText().trim();
        String aircraftType = aircraftTypeField.getText().trim();
        int totalSeats = (Integer) totalSeatsSpinner.getValue();
        String economyFareStr = economyFareField.getText().trim();
        String businessFareStr = businessFareField.getText().trim();
        String firstFareStr = firstClassFareField.getText().trim();
        String crewMembersInput = crewMembersField.getText().trim();
        String status = (String) flightStatusComboBox.getSelectedItem();

        
        if (flightNumber.isEmpty() || departureAirportCode == null || arrivalAirportCode == null ||
            departureTimeString.isEmpty() || arrivalTimeString.isEmpty() ||
            aircraftType.isEmpty() || economyFareStr.isEmpty() || businessFareStr.isEmpty() ||
            firstFareStr.isEmpty() || status == null) {
            JOptionPane.showMessageDialog(this, "Please fill in all flight details.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (departureAirportCode.equals(arrivalAirportCode)) {
            JOptionPane.showMessageDialog(this, "Departure and Arrival airports cannot be the same.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Timestamp departureSqlTime;
        Timestamp arrivalSqlTime;
        try {
            
            if (!departureTimeString.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}")) {
                throw new ParseException("Invalid format", 0);
            }
            departureSqlTime = new Timestamp(dateTimeFormat.parse(departureTimeString).getTime());
            
            
            if (!arrivalTimeString.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}")) {
                throw new ParseException("Invalid format", 0);
            }
            arrivalSqlTime = new Timestamp(dateTimeFormat.parse(arrivalTimeString).getTime());
            
            if (departureSqlTime.after(arrivalSqlTime)) {
                JOptionPane.showMessageDialog(this, "Departure time cannot be after arrival time.", "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(this, "Invalid Date/Time format. Please use YYYY-MM-DD HH:MM:SS", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        
        double economyFare;
        double businessFare;
        double firstClassFare;
        try {
            economyFare = Double.parseDouble(economyFareStr);
            businessFare = Double.parseDouble(businessFareStr);
            firstClassFare = Double.parseDouble(firstFareStr);
            
            if (economyFare <= 0 || businessFare <= 0 || firstClassFare <= 0) {
                JOptionPane.showMessageDialog(this, "Fare amounts must be positive numbers.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid fare amount. Please enter valid numbers.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        
        String flightID = "FL" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        Connection conn = null;
        PreparedStatement pstmtFlight = null;
        PreparedStatement pstmtCrew = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConnection.connect();
            conn.setAutoCommit(false);

            
            String sqlFlight = "INSERT INTO flight (flight_id, flight_num, departure_airport_id, arrival_airport_id, " +
                               "departure_time, arrival_time, aircraft_type, total_seats, available_seats, status, " +
                               "fare_economy, fare_business, fare_first_class) " +
                               "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            pstmtFlight = conn.prepareStatement(sqlFlight);
            pstmtFlight.setString(1, flightID);
            pstmtFlight.setString(2, flightNumber);
            pstmtFlight.setString(3, departureAirportCode);
            pstmtFlight.setString(4, arrivalAirportCode);
            pstmtFlight.setTimestamp(5, departureSqlTime);
            pstmtFlight.setTimestamp(6, arrivalSqlTime);
            pstmtFlight.setString(7, aircraftType);
            pstmtFlight.setInt(8, totalSeats);
            pstmtFlight.setInt(9, totalSeats); 
            pstmtFlight.setString(10, status);
            pstmtFlight.setDouble(11, economyFare);
            pstmtFlight.setDouble(12, businessFare);
            pstmtFlight.setDouble(13, firstClassFare);
            pstmtFlight.executeUpdate();

            
            if (!crewMembersInput.isEmpty()) {
                List<String> crewMemberIds = Arrays.asList(crewMembersInput.split(",\\s*"));
                String sqlCrew = "INSERT INTO flight_flightcrew (flight_id, crew_member_id) VALUES (?, ?)";
                pstmtCrew = conn.prepareStatement(sqlCrew);
                
                
                String checkCrewSql = "SELECT COUNT(*) FROM crew_member WHERE crew_member_id = ?";
                PreparedStatement checkCrewStmt = conn.prepareStatement(checkCrewSql);
                
                for (String crewId : crewMemberIds) {
                    crewId = crewId.trim();
                    checkCrewStmt.setString(1, crewId);
                    rs = checkCrewStmt.executeQuery();
                    if (rs.next() && rs.getInt(1) > 0) {
                        pstmtCrew.setString(1, flightID);
                        pstmtCrew.setString(2, crewId);
                        pstmtCrew.addBatch();
                    } else {
                        JOptionPane.showMessageDialog(this, "Crew member ID " + crewId + " not found. Skipping.", "Warning", JOptionPane.WARNING_MESSAGE);
                    }
                    rs.close();
                }
                checkCrewStmt.close();
                pstmtCrew.executeBatch();
            }

            conn.commit();
            JOptionPane.showMessageDialog(this, "Flight added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            logSystemAction(loggedInAdmin != null ? loggedInAdmin.getAdminId() : null, "ADD_FLIGHT", "Added flight: " + flightNumber + " (" + flightID + ")");
            sendAdminNotification("New flight " + flightNumber + " added.", "System");

            
            flightNumberField.setText("");
            departureAirportCodeComboBox.setSelectedIndex(0);
            arrivalAirportCodeComboBox.setSelectedIndex(0);
            departureTimeField.setText("");
            arrivalTimeField.setText("");
            aircraftTypeField.setText("");
            totalSeatsSpinner.setValue(100);
            economyFareField.setText("");
            businessFareField.setText("");
            firstClassFareField.setText("");
            flightStatusComboBox.setSelectedIndex(0);
            crewMembersField.setText("");

        } catch (SQLException ex) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            JOptionPane.showMessageDialog(this, "Database error adding flight: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmtFlight != null) pstmtFlight.close();
                if (pstmtCrew != null) pstmtCrew.close();
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }


    
    private JPanel createManageFlightsPanel() {
        JPanel panel = createContentPanel("Manage Existing Flights");
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setOpaque(false);

        
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        searchPanel.setOpaque(false);
        searchPanel.add(createStyledLabel("Search Flight (ID or Number):"));
        searchFlightField = createStyledTextField();
        searchPanel.add(searchFlightField);
        JButton searchButton = createStyledButton("Search");
        searchButton.addActionListener(e -> searchFlightsForManagement());
        searchPanel.add(searchButton);
        contentPanel.add(searchPanel, BorderLayout.NORTH);

        
        flightsTableModel = new DefaultTableModel(new Object[]{"Flight ID", "Number", "Departure", "Arrival", "Dep. Time", "Arr. Time", "Aircraft", "Total Seats", "Available Seats", "Economy Fare", "Business Fare", "First Class Fare", "Status", "Crew Members"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        flightsTable = new JTable(flightsTableModel);
        flightsTable.setBackground(new Color(60, 60, 60));
        flightsTable.setForeground(Color.WHITE);
        flightsTable.setSelectionBackground(new Color(80, 80, 80));
        flightsTable.setSelectionForeground(Color.WHITE);
        flightsTable.getTableHeader().setBackground(new Color(40, 50, 70));
        flightsTable.getTableHeader().setForeground(Color.WHITE);
        flightsTable.setFillsViewportHeight(true); 
        JScrollPane scrollPane = new JScrollPane(flightsTable);
        scrollPane.getViewport().setBackground(new Color(60, 60, 60)); 
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        
        JPanel actionButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        actionButtonPanel.setOpaque(false);

        editFlightButton = createStyledButton("Edit Flight");
        editFlightButton.addActionListener(e -> editSelectedFlight());
        actionButtonPanel.add(editFlightButton);

        cancelFlightButton = createStyledButton("Cancel Flight");
        cancelFlightButton.addActionListener(e -> updateFlightStatusSelected("Cancelled")); 
        actionButtonPanel.add(cancelFlightButton);

        rescheduleFlightButton = createStyledButton("Reschedule Flight");
        rescheduleFlightButton.addActionListener(e -> rescheduleSelectedFlight());
        actionButtonPanel.add(rescheduleFlightButton);

        notifyPassengersToggle = new JToggleButton("Notify Passengers");
        notifyPassengersToggle.setFont(new Font("Arial", Font.BOLD, 16));
        notifyPassengersToggle.setPreferredSize(new Dimension(200, 40));
        notifyPassengersToggle.setForeground(Color.WHITE);
        notifyPassengersToggle.setBackground(new Color(102, 102, 204));
        notifyPassengersToggle.setFocusPainted(false);
        notifyPassengersToggle.addActionListener(e -> {
            if (notifyPassengersToggle.isSelected()) {
                JOptionPane.showMessageDialog(this, "Passengers will be notified of changes.", "Notification", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Passenger notification is off.", "Notification", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        actionButtonPanel.add(notifyPassengersToggle);

        contentPanel.add(actionButtonPanel, BorderLayout.SOUTH);
        panel.add(contentPanel, BorderLayout.CENTER);

        return panel;
    }

    private void populateFlightsTable() {
        flightsTableModel.setRowCount(0); 

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConnection.connect();
            String sql = "SELECT f.flight_id, f.flight_num, da.name AS departure_airport_name, aa.name AS arrival_airport_name, " +
                         "f.departure_time, f.arrival_time, f.aircraft_type, f.total_seats, f.available_seats, " +
                         "f.fare_economy, f.fare_business, f.fare_first_class, f.status " +
                         "FROM flight f " +
                         "JOIN airport da ON f.departure_airport_id = da.airport_code " +
                         "JOIN airport aa ON f.arrival_airport_id = aa.airport_code";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            SimpleDateFormat displayDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            while (rs.next()) {
                String flightID = rs.getString("flight_id");
                String flightNumber = rs.getString("flight_num"); 
                String departureAirportName = rs.getString("departure_airport_name");
                String arrivalAirportName = rs.getString("arrival_airport_name");
                String departureTime = displayDateFormat.format(rs.getTimestamp("departure_time"));
                String arrivalTime = displayDateFormat.format(rs.getTimestamp("arrival_time"));
                String aircraftType = rs.getString("aircraft_type");
                int totalSeats = rs.getInt("total_seats");
                int availableSeats = rs.getInt("available_seats");
                double economyFare = rs.getDouble("fare_economy");
                double businessFare = rs.getDouble("fare_business");
                double firstClassFare = rs.getDouble("fare_first_class");
                String status = rs.getString("status");

                
                List<String> crewMembersList = new ArrayList<>();
                String crewSql = "SELECT crew_member_id FROM flight_flightcrew WHERE flight_id = ?";
                try (PreparedStatement crewPstmt = conn.prepareStatement(crewSql)) {
                    crewPstmt.setString(1, flightID);
                    try (ResultSet crewRs = crewPstmt.executeQuery()) {
                        while (crewRs.next()) {
                            crewMembersList.add(crewRs.getString("crew_member_id"));
                        }
                    }
                }
                String crewMembersDisplay = crewMembersList.isEmpty() ? "N/A" : String.join(", ", crewMembersList);

                flightsTableModel.addRow(new Object[]{
                    flightID,
                    flightNumber,
                    departureAirportName,
                    arrivalAirportName,
                    departureTime,
                    arrivalTime,
                    aircraftType,
                    totalSeats,
                    availableSeats,
                    String.format("%.2f", economyFare),
                    String.format("%.2f", businessFare),
                    String.format("%.2f", firstClassFare),
                    status,
                    crewMembersDisplay
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading flights: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void searchFlightsForManagement() {
        String searchTerm = searchFlightField.getText().trim();
        flightsTableModel.setRowCount(0); 

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConnection.connect();
            String sql = "SELECT f.flight_id, f.flight_num, da.name AS departure_airport_name, aa.name AS arrival_airport_name, " +
                         "f.departure_time, f.arrival_time, f.aircraft_type, f.total_seats, f.available_seats, " +
                         "f.fare_economy, f.fare_business, f.fare_first_class, f.status " +
                         "FROM flight f " +
                         "JOIN airport da ON f.departure_airport_id = da.airport_code " +
                         "JOIN airport aa ON f.arrival_airport_id = aa.airport_code " +
                         "WHERE f.flight_id LIKE ? OR f.flight_num LIKE ?"; 
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "%" + searchTerm + "%");
            pstmt.setString(2, "%" + searchTerm + "%");
            rs = pstmt.executeQuery();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            while (rs.next()) {
                String flightID = rs.getString("flight_id");
                String flightNumber = rs.getString("flight_num"); 
                String departureAirportName = rs.getString("departure_airport_name");
                String arrivalAirportName = rs.getString("arrival_airport_name");
                String departureTime = sdf.format(rs.getTimestamp("departure_time"));
                String arrivalTime = sdf.format(rs.getTimestamp("arrival_time"));
                String aircraftType = rs.getString("aircraft_type");
                int totalSeats = rs.getInt("total_seats");
                int availableSeats = rs.getInt("available_seats");
                double economyFare = rs.getDouble("fare_economy");
                double businessFare = rs.getDouble("fare_business");
                double firstClassFare = rs.getDouble("fare_first_class");
                String status = rs.getString("status");

                
                List<String> crewMembersList = new ArrayList<>();
                String crewSql = "SELECT crew_member_id FROM flight_flightcrew WHERE flight_id = ?";
                try (PreparedStatement crewPstmt = conn.prepareStatement(crewSql)) {
                    crewPstmt.setString(1, flightID);
                    try (ResultSet crewRs = crewPstmt.executeQuery()) {
                        while (crewRs.next()) {
                            crewMembersList.add(crewRs.getString("crew_member_id"));
                        }
                    }
                }
                String crewMembersDisplay = crewMembersList.isEmpty() ? "N/A" : String.join(", ", crewMembersList);

                Vector<Object> row = new Vector<>();
                row.add(flightID);
                row.add(flightNumber);
                row.add(departureAirportName);
                row.add(arrivalAirportName);
                row.add(departureTime);
                row.add(arrivalTime);
                row.add(aircraftType);
                row.add(totalSeats);
                row.add(availableSeats);
                row.add(String.format("%.2f", economyFare));
                row.add(String.format("%.2f", businessFare));
                row.add(String.format("%.2f", firstClassFare));
                row.add(status);
                row.add(crewMembersDisplay);
                flightsTableModel.addRow(row);
            }
            if (flightsTableModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "No flights found matching your search.", "Search Result", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error during flight search: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void editSelectedFlight() {
    int selectedRow = flightsTable.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Please select a flight to edit.", "No Flight Selected", JOptionPane.WARNING_MESSAGE);
        return;
    }

    
    String flightID = flightsTable.getValueAt(selectedRow, 0).toString();
    String currentFlightNumber = flightsTable.getValueAt(selectedRow, 1).toString();
    String currentDepartureAirport = flightsTable.getValueAt(selectedRow, 2).toString();
    String currentArrivalAirport = flightsTable.getValueAt(selectedRow, 3).toString();
    String currentDepartureTime = flightsTable.getValueAt(selectedRow, 4).toString();
    String currentArrivalTime = flightsTable.getValueAt(selectedRow, 5).toString();
    String currentStatus = flightsTable.getValueAt(selectedRow, 6).toString();
    String currentAvailableSeats = flightsTable.getValueAt(selectedRow, 7).toString();
    String currentPrice = flightsTable.getValueAt(selectedRow, 8).toString();
    String currentCrewMembersJson = flightsTable.getValueAt(selectedRow, 9).toString();

    
    Gson gson = new Gson();
    Type type = new TypeToken<List<String>>() {}.getType();
    List<String> currentCrewMembers = gson.fromJson(currentCrewMembersJson, type);

    
    JDialog editDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Edit Flight", true);
    editDialog.setLayout(new GridBagLayout());
    editDialog.setSize(600, 500);
    editDialog.setLocationRelativeTo(this);

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(5, 5, 5, 5);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    
    gbc.gridx = 0;
    gbc.gridy = 0;
    editDialog.add(new JLabel("Flight Number:"), gbc);
    gbc.gridx = 1;
    JTextField editFlightNumberField = new JTextField(currentFlightNumber, 20);
    editDialog.add(editFlightNumberField, gbc);

    
    gbc.gridx = 0;
    gbc.gridy = 1;
    editDialog.add(new JLabel("Departure Airport Code:"), gbc);
    gbc.gridx = 1;
    JComboBox<String> editDepartureAirportCodeComboBox = new JComboBox<>(airportCodes.toArray(new String[0]));
    editDepartureAirportCodeComboBox.setSelectedItem(currentDepartureAirport);
    editDialog.add(editDepartureAirportCodeComboBox, gbc);

    
    gbc.gridx = 0;
    gbc.gridy = 2;
    editDialog.add(new JLabel("Arrival Airport Code:"), gbc);
    gbc.gridx = 1;
    JComboBox<String> editArrivalAirportCodeComboBox = new JComboBox<>(airportCodes.toArray(new String[0]));
    editArrivalAirportCodeComboBox.setSelectedItem(currentArrivalAirport);
    editDialog.add(editArrivalAirportCodeComboBox, gbc);

    
    gbc.gridx = 0;
    gbc.gridy = 3;
    editDialog.add(new JLabel("Departure Time (yyyy-MM-dd HH:mm:ss):"), gbc);
    gbc.gridx = 1;
    JTextField editDepartureTimeField = new JTextField(currentDepartureTime, 20);
    editDialog.add(editDepartureTimeField, gbc);

    
    gbc.gridx = 0;
    gbc.gridy = 4;
    editDialog.add(new JLabel("Arrival Time (yyyy-MM-dd HH:mm:ss):"), gbc);
    gbc.gridx = 1;
    JTextField editArrivalTimeField = new JTextField(currentArrivalTime, 20);
    editDialog.add(editArrivalTimeField, gbc);

    
    gbc.gridx = 0;
    gbc.gridy = 5;
    editDialog.add(new JLabel("Status:"), gbc);
    gbc.gridx = 1;
    String[] statuses = {"Scheduled", "Delayed", "Cancelled", "Active", "Landed"};
    JComboBox<String> editStatusComboBox = new JComboBox<>(statuses);
    editStatusComboBox.setSelectedItem(currentStatus);
    editDialog.add(editStatusComboBox, gbc);

    
    gbc.gridx = 0;
    gbc.gridy = 6;
    editDialog.add(new JLabel("Available Seats:"), gbc);
    gbc.gridx = 1;
    JTextField editAvailableSeatsField = new JTextField(currentAvailableSeats, 20);
    editDialog.add(editAvailableSeatsField, gbc);

    
    gbc.gridx = 0;
    gbc.gridy = 7;
    editDialog.add(new JLabel("Price:"), gbc);
    gbc.gridx = 1;
    JTextField editPriceField = new JTextField(currentPrice, 20);
    editDialog.add(editPriceField, gbc);

    
    gbc.gridx = 0;
    gbc.gridy = 8;
    editDialog.add(new JLabel("Crew Members (comma-separated IDs):"), gbc);
    gbc.gridx = 1;
    JTextField editCrewMembersField = new JTextField(String.join(", ", currentCrewMembers), 20);
    editDialog.add(editCrewMembersField, gbc);

    
    gbc.gridx = 0;
    gbc.gridy = 9;
    gbc.gridwidth = 2;
    JButton saveButton = new JButton("Save Changes");
    editDialog.add(saveButton, gbc);

    saveButton.addActionListener(e -> {
        
        String newFlightNumber = editFlightNumberField.getText();
        String newDepartureAirportCode = (String) editDepartureAirportCodeComboBox.getSelectedItem();
        String newArrivalAirportCode = (String) editArrivalAirportCodeComboBox.getSelectedItem();
        String newDepartureTime = editDepartureTimeField.getText();
        String newArrivalTime = editArrivalTimeField.getText();
        String newStatus = (String) editStatusComboBox.getSelectedItem();
        String newAvailableSeats = editAvailableSeatsField.getText();
        String newPrice = editPriceField.getText();
        List<String> newCrewMemberIds = Arrays.asList(editCrewMembersField.getText().split(","));

        Connection conn = null;
        PreparedStatement updateFlightStmt = null;
        PreparedStatement deleteCrewStmt = null;
        PreparedStatement insertCrewPstmt = null;
        PreparedStatement checkCrewStmt = null;

        try {
            conn = DatabaseConnection.connect();
            if (conn != null) {
                conn.setAutoCommit(false); 

                
                String updateFlightSql = "UPDATE Flight SET " +
                                         "flight_number = ?, " +
                                         "departure_airport_code = ?, " +
                                         "arrival_airport_code = ?, " +
                                         "departure_time = ?, " +
                                         "arrival_time = ?, " +
                                         "status = ?, " +
                                         "available_seats = ?, " +
                                         "price = ? " +
                                         "WHERE flight_id = ?";
                updateFlightStmt = conn.prepareStatement(updateFlightSql);
                updateFlightStmt.setString(1, newFlightNumber);
                updateFlightStmt.setString(2, newDepartureAirportCode);
                updateFlightStmt.setString(3, newArrivalAirportCode);
                
                
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                java.util.Date parsedDepartureDate = dateFormat.parse(newDepartureTime);
                java.sql.Timestamp sqlDepartureTime = new java.sql.Timestamp(parsedDepartureDate.getTime());
                updateFlightStmt.setTimestamp(4, sqlDepartureTime);

                java.util.Date parsedArrivalDate = dateFormat.parse(newArrivalTime);
                java.sql.Timestamp sqlArrivalTime = new java.sql.Timestamp(parsedArrivalDate.getTime());
                updateFlightStmt.setTimestamp(5, sqlArrivalTime);
                
                updateFlightStmt.setString(6, newStatus);
                updateFlightStmt.setInt(7, Integer.parseInt(newAvailableSeats));
                updateFlightStmt.setDouble(8, Double.parseDouble(newPrice));
                updateFlightStmt.setString(9, flightID);
                updateFlightStmt.executeUpdate();

                
                
                String deleteCrewSql = "DELETE FROM FlightCrew WHERE flight_id = ?";
                deleteCrewStmt = conn.prepareStatement(deleteCrewSql);
                deleteCrewStmt.setString(1, flightID);
                deleteCrewStmt.executeUpdate();

                
                String insertCrewSql = "INSERT INTO FlightCrew (flight_id, crew_id) VALUES (?, ?)";
                insertCrewPstmt = conn.prepareStatement(insertCrewSql);

                
                String checkCrewSql = "SELECT COUNT(*) FROM Crew WHERE crew_id = ?";
                checkCrewStmt = conn.prepareStatement(checkCrewSql);

                for (String crewId : newCrewMemberIds) {
                    crewId = crewId.trim();
                    if (crewId.isEmpty()) continue; 

                    checkCrewStmt.setString(1, crewId);
                    try (ResultSet crewCheckRs = checkCrewStmt.executeQuery()) { 
                        if (crewCheckRs.next() && crewCheckRs.getInt(1) > 0) {
                            insertCrewPstmt.setString(1, flightID);
                            insertCrewPstmt.setString(2, crewId);
                            insertCrewPstmt.addBatch();
                        } else {
                            JOptionPane.showMessageDialog(editDialog, "Crew member ID " + crewId + " not found. Skipping.", "Warning", JOptionPane.WARNING_MESSAGE);
                        }
                    }
                }
                insertCrewPstmt.executeBatch(); 

                conn.commit(); 
                JOptionPane.showMessageDialog(editDialog, "Flight and crew details updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                editDialog.dispose(); 
                loadFlights(); 
            }
        } catch (SQLException | ParseException ex) {
            if (conn != null) {
                try {
                    conn.rollback(); 
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            ex.printStackTrace();
            JOptionPane.showMessageDialog(editDialog, "Error updating flight: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (insertCrewPstmt != null) insertCrewPstmt.close();
                if (deleteCrewStmt != null) deleteCrewStmt.close();
                if (updateFlightStmt != null) updateFlightStmt.close();
                if (checkCrewStmt != null) checkCrewStmt.close();
                if (conn != null) {
                    conn.setAutoCommit(true); 
                    conn.close(); 
                }
            } catch (SQLException closeEx) {
                closeEx.printStackTrace();
            }
        }
    });

    editDialog.setVisible(true);
}

    private void updateFlightStatusSelected(String newStatus) {
        int selectedRow = flightsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a flight to update its status.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String flightID = (String) flightsTableModel.getValueAt(selectedRow, 0);

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to set flight " + flightID + " to " + newStatus + "?", "Confirm Status Change", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            Connection conn = null;
            PreparedStatement pstmt = null;
            try {
                conn = DatabaseConnection.connect();
                String sql = "UPDATE flight SET status = ? WHERE flight_id = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, newStatus);
                pstmt.setString(2, flightID);
                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Flight " + flightID + " status updated to " + newStatus + " successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    searchFlightsForManagement(); 
                    logSystemAction(loggedInAdmin != null ? loggedInAdmin.getAdminId() : null, "UPDATE_FLIGHT_STATUS", "Set flight " + flightID + " status to " + newStatus);
                    sendAdminNotification("Flight " + flightID + " status changed to " + newStatus + ".", "System");

                    if (notifyPassengersToggle.isSelected()) {
                        JOptionPane.showMessageDialog(this, "Passengers will be notified of flight " + flightID + " status change to " + newStatus, "Notification", JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update flight status.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            } finally {
                try {
                    if (pstmt != null) pstmt.close();
                    if (conn != null) conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private void rescheduleSelectedFlight() {
        int selectedRow = flightsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a flight to reschedule.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String flightID = (String) flightsTableModel.getValueAt(selectedRow, 0);
        String currentDepTime = (String) flightsTableModel.getValueAt(selectedRow, 4);
        String currentArrTime = (String) flightsTableModel.getValueAt(selectedRow, 5);

        
        JDialog rescheduleDialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Reschedule Flight", Dialog.ModalityType.APPLICATION_MODAL);
        rescheduleDialog.setLayout(new BorderLayout(10, 10));
        rescheduleDialog.setBackground(new Color(30, 40, 60));
        rescheduleDialog.setMinimumSize(new Dimension(400, 250));
        rescheduleDialog.setLocationRelativeTo(this);

        JPanel rescheduleFormPanel = new JPanel(new GridBagLayout());
        rescheduleFormPanel.setOpaque(false);
        rescheduleFormPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JFormattedTextField newDepartureTimeField = createStyledFormattedTextField("####-##-## ##:##:##");
        JFormattedTextField newArrivalTimeField = createStyledFormattedTextField("####-##-## ##:##:##");

        newDepartureTimeField.setText(currentDepTime);
        newArrivalTimeField.setText(currentArrTime);

        addFormField(rescheduleFormPanel, createStyledLabel("New Departure Time (YYYY-MM-DD HH:MM:SS):"), newDepartureTimeField, gbc, 0);
        addFormField(rescheduleFormPanel, createStyledLabel("New Arrival Time (YYYY-MM-DD HH:MM:SS):"), newArrivalTimeField, gbc, 1);

        JButton saveButton = createStyledButton("Save Reschedule");
        saveButton.addActionListener(evt -> {
            String newDepTimeStr = newDepartureTimeField.getText();
            String newArrTimeStr = newArrivalTimeField.getText();

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date newDepartureTime, newArrivalTime;
            try {
                newDepartureTime = dateFormat.parse(newDepTimeStr);
                newArrivalTime = dateFormat.parse(newArrTimeStr);
                if (newDepartureTime.after(newArrivalTime)) {
                    JOptionPane.showMessageDialog(rescheduleDialog, "New departure time cannot be after new arrival time.", "Input Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(rescheduleDialog, "Invalid date/time format. Use YYYY-MM-DD HH:MM:SS.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Connection conn = null;
            PreparedStatement pstmt = null;
            try {
                conn = DatabaseConnection.connect();
                String sql = "UPDATE flight SET departure_time = ?, arrival_time = ?, status = ? WHERE flight_id = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setTimestamp(1, new Timestamp(newDepartureTime.getTime()));
                pstmt.setTimestamp(2, new Timestamp(newArrivalTime.getTime()));
                pstmt.setString(3, "Rescheduled"); 
                pstmt.setString(4, flightID);

                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(rescheduleDialog, "Flight " + flightID + " rescheduled successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    rescheduleDialog.dispose();
                    searchFlightsForManagement(); 
                    logSystemAction(loggedInAdmin != null ? loggedInAdmin.getAdminId() : null, "RESCHEDULE_FLIGHT", "Rescheduled flight: " + flightID);
                    sendAdminNotification("Flight " + flightID + " rescheduled.", "System");

                    if (notifyPassengersToggle.isSelected()) {
                        JOptionPane.showMessageDialog(this, "Passengers will be notified of flight " + flightID + " rescheduling.", "Notification", JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(rescheduleDialog, "Failed to reschedule flight.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(rescheduleDialog, "Database error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            } finally {
                try {
                    if (pstmt != null) pstmt.close();
                    if (conn != null) conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        JPanel dialogButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        dialogButtonPanel.setOpaque(false);
        dialogButtonPanel.add(saveButton);
        rescheduleDialog.add(rescheduleFormPanel, BorderLayout.CENTER);
        rescheduleDialog.add(dialogButtonPanel, BorderLayout.SOUTH);
        rescheduleDialog.pack();
        rescheduleDialog.setVisible(true);
    }


    
    private JPanel createManageUsersPanel() {
        JPanel panel = createContentPanel("Manage Users");
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setOpaque(false);

        
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        searchPanel.setOpaque(false);
        searchPanel.add(createStyledLabel("Search User (ID/Username):"));
        searchUserField = createStyledTextField();
        searchPanel.add(searchUserField);
        searchPanel.add(createStyledLabel("User Role:"));
        searchUserTypeComboBox = createStyledComboBox();
        searchUserTypeComboBox.addItem("All");
        searchUserTypeComboBox.addItem("CUSTOMER");
        searchUserTypeComboBox.addItem("AGENT");
        searchUserTypeComboBox.addItem("ADMINISTRATOR");
        searchPanel.add(searchUserTypeComboBox);
        JButton searchButton = createStyledButton("Search");
        searchButton.addActionListener(e -> searchUsersForManagement());
        searchPanel.add(searchButton);
        contentPanel.add(searchPanel, BorderLayout.NORTH);

        
        usersTableModel = new DefaultTableModel(new Object[]{"User ID", "Username", "Email", "Phone", "Address", "Role", "Blacklisted", "Registration Date", "Last Login"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        usersTable = new JTable(usersTableModel);
        usersTable.setBackground(new Color(60, 60, 60));
        usersTable.setForeground(Color.WHITE);
        usersTable.setSelectionBackground(new Color(80, 80, 80));
        usersTable.setSelectionForeground(Color.WHITE);
        usersTable.getTableHeader().setBackground(new Color(40, 50, 70));
        usersTable.getTableHeader().setForeground(Color.WHITE);
        usersTable.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(usersTable);
        scrollPane.getViewport().setBackground(new Color(60, 60, 60));
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        
        JPanel actionButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        actionButtonPanel.setOpaque(false);

        editUserProfileButton = createStyledButton("Edit Profile");
        editUserProfileButton.addActionListener(e -> editSelectedUser());
        actionButtonPanel.add(editUserProfileButton);

        resetUserPasswordButton = createStyledButton("Reset Password");
        resetUserPasswordButton.addActionListener(e -> resetSelectedUserPassword());
        actionButtonPanel.add(resetUserPasswordButton);

        deactivateDeleteUserButton = createStyledButton("Delete User"); 
        deactivateDeleteUserButton.addActionListener(e -> deleteSelectedUser()); 
        actionButtonPanel.add(deactivateDeleteUserButton);

        addAgentButton = createStyledButton("Add Agent"); 
        addAgentButton.addActionListener(e -> addAgentAction());
        actionButtonPanel.add(addAgentButton);

        contentPanel.add(actionButtonPanel, BorderLayout.SOUTH);
        panel.add(contentPanel, BorderLayout.CENTER);

        return panel;
    }

    private void searchUsersForManagement() {
        String searchTerm = searchUserField.getText().trim();
        String userRoleFilter = (String) searchUserTypeComboBox.getSelectedItem();
        usersTableModel.setRowCount(0); 

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConnection.connect();
            String sql = "SELECT user_id, username, email, phone_number, address, registration_date, last_login, role, is_blacklisted FROM users WHERE (user_id LIKE ? OR username LIKE ?) ";
            if (!userRoleFilter.equals("All")) {
                sql += "AND role = ?";
            }
            sql += " ORDER BY username ASC"; 

            pstmt = conn.prepareStatement(sql);
            int paramIndex = 1;
            pstmt.setString(paramIndex++, "%" + searchTerm + "%");
            pstmt.setString(paramIndex++, "%" + searchTerm + "%");
            if (!userRoleFilter.equals("All")) {
                pstmt.setString(paramIndex++, userRoleFilter);
            }
            rs = pstmt.executeQuery();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getString("user_id"));
                row.add(rs.getString("username"));
                row.add(rs.getString("email"));
                row.add(rs.getString("phone_number"));
                row.add(rs.getString("address"));
                row.add(rs.getString("role"));
                row.add(rs.getBoolean("is_blacklisted") ? "Yes" : "No"); 
                row.add(rs.getTimestamp("registration_date") != null ? sdf.format(rs.getTimestamp("registration_date")) : "N/A");
                row.add(rs.getTimestamp("last_login") != null ? sdf.format(rs.getTimestamp("last_login")) : "N/A");
                usersTableModel.addRow(row);
            }
            if (usersTableModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "No users found matching your search criteria.", "Search Result", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error during user search: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void editSelectedUser() {
        int selectedRow = usersTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user to edit.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String userID = (String) usersTableModel.getValueAt(selectedRow, 0);

        JDialog editUserDialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Edit User Profile", Dialog.ModalityType.APPLICATION_MODAL);
        editUserDialog.setLayout(new BorderLayout(10, 10));
        editUserDialog.setBackground(new Color(30, 40, 60));
        editUserDialog.setMinimumSize(new Dimension(450, 450));
        editUserDialog.setLocationRelativeTo(this);

        JPanel editFormPanel = new JPanel(new GridBagLayout());
        editFormPanel.setOpaque(false);
        editFormPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField editUsernameField = createStyledTextField();
        JTextField editEmailField = createStyledTextField();
        JTextField editPhoneNumberField = createStyledTextField(); 
        JTextArea editAddressArea = new JTextArea(3, 20); 
        editAddressArea.setBackground(new Color(60, 60, 60));
        editAddressArea.setForeground(Color.WHITE);
        editAddressArea.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 1));
        JScrollPane addressScrollPane = new JScrollPane(editAddressArea);
        addressScrollPane.getViewport().setBackground(new Color(60, 60, 60));

        JComboBox<String> editRoleComboBox = createStyledComboBox();
        editRoleComboBox.addItem("CUSTOMER");
        editRoleComboBox.addItem("AGENT");
        editRoleComboBox.addItem("ADMINISTRATOR");

        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConnection.connect();
            String sql = "SELECT username, email, phone_number, address, role FROM users WHERE user_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, userID);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                editUsernameField.setText(rs.getString("username"));
                editEmailField.setText(rs.getString("email"));
                editPhoneNumberField.setText(rs.getString("phone_number"));
                editAddressArea.setText(rs.getString("address"));
                editRoleComboBox.setSelectedItem(rs.getString("role"));
            } else {
                JOptionPane.showMessageDialog(editUserDialog, "User not found for ID: " + userID, "Error", JOptionPane.ERROR_MESSAGE);
                editUserDialog.dispose();
                return;
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(editUserDialog, "Database error fetching user details: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
            editUserDialog.dispose();
            return;
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        addFormField(editFormPanel, createStyledLabel("Username:"), editUsernameField, gbc, 0);
        addFormField(editFormPanel, createStyledLabel("Email:"), editEmailField, gbc, 1);
        addFormField(editFormPanel, createStyledLabel("Phone Number:"), editPhoneNumberField, gbc, 2);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        editFormPanel.add(createStyledLabel("Address:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        editFormPanel.add(addressScrollPane, gbc);
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        addFormField(editFormPanel, createStyledLabel("Role:"), editRoleComboBox, gbc, 4); 

        JButton saveButton = createStyledButton("Save Changes");
        saveButton.addActionListener(evt -> {
            String newUsername = editUsernameField.getText().trim();
            String newEmail = editEmailField.getText().trim();
            String newPhoneNumber = editPhoneNumberField.getText().trim();
            String newAddress = editAddressArea.getText().trim();
            String newRole = (String) editRoleComboBox.getSelectedItem();

            if (newUsername.isEmpty() || newEmail.isEmpty() || newRole == null) {
                JOptionPane.showMessageDialog(editUserDialog, "Username, Email, and Role cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Connection updateConn = null;
            PreparedStatement updatePstmt = null;
            try {
                updateConn = DatabaseConnection.connect();
                String sql = "UPDATE users SET username=?, email=?, phone_number=?, address=?, role=? WHERE user_id=?";
                updatePstmt = updateConn.prepareStatement(sql);
                updatePstmt.setString(1, newUsername);
                updatePstmt.setString(2, newEmail);
                updatePstmt.setString(3, newPhoneNumber);
                updatePstmt.setString(4, newAddress);
                updatePstmt.setString(5, newRole);
                updatePstmt.setString(6, userID);

                int rowsAffected = updatePstmt.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(editUserDialog, "User profile updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    editUserDialog.dispose();
                    searchUsersForManagement(); 
                    logSystemAction(loggedInAdmin != null ? loggedInAdmin.getAdminId() : null, "UPDATE_USER_PROFILE", "Updated profile for user: " + newUsername + " (" + userID + ")");
                    sendAdminNotification("User " + newUsername + " profile updated.", "System");
                } else {
                    JOptionPane.showMessageDialog(editUserDialog, "Failed to update user profile.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(editUserDialog, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            } finally {
                try {
                    if (updatePstmt != null) updatePstmt.close();
                    if (updateConn != null) updateConn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        JPanel dialogButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        dialogButtonPanel.setOpaque(false);
        dialogButtonPanel.add(saveButton);
        editUserDialog.add(editFormPanel, BorderLayout.CENTER);
        editUserDialog.add(dialogButtonPanel, BorderLayout.SOUTH);
        editUserDialog.pack();
        editUserDialog.setVisible(true);
    }

    private void resetSelectedUserPassword() {
        int selectedRow = usersTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user to reset password.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String userID = (String) usersTableModel.getValueAt(selectedRow, 0);

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to reset password for user " + userID + "? This action cannot be undone.", "Confirm Password Reset", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            String newPasswordHash = "new_temp_password_hash"; 

            Connection conn = null;
            PreparedStatement pstmt = null;
            try {
                conn = DatabaseConnection.connect();
                String sql = "UPDATE users SET password_hash = ? WHERE user_id = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, newPasswordHash);
                pstmt.setString(2, userID);

                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Password for user " + userID + " has been reset to a temporary value. Please inform the user.", "Password Reset", JOptionPane.INFORMATION_MESSAGE);
                    logSystemAction(loggedInAdmin != null ? loggedInAdmin.getAdminId() : null, "RESET_USER_PASSWORD", "Reset password for user: " + userID);
                    sendAdminNotification("Password reset for user " + userID + ".", "System");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to reset password.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            } finally {
                try {
                    if (pstmt != null) pstmt.close();
                    if (conn != null) conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private void deleteSelectedUser() { 
        int selectedRow = usersTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String userID = (String) usersTableModel.getValueAt(selectedRow, 0);
        String username = (String) usersTableModel.getValueAt(selectedRow, 1);

        int confirmDelete = JOptionPane.showConfirmDialog(this, "Are you absolutely sure you want to DELETE user " + username + " (ID: " + userID + ")? This action is irreversible and will remove associated data (e.g., from customers/agents/administrators tables).", "Confirm Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirmDelete == JOptionPane.YES_OPTION) {
            Connection conn = null;
            PreparedStatement pstmt = null;
            try {
                conn = DatabaseConnection.connect();
                
                
                String sql = "DELETE FROM users WHERE user_id = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, userID);
                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "User " + username + " (ID: " + userID + ") deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    searchUsersForManagement(); 
                    logSystemAction(loggedInAdmin != null ? loggedInAdmin.getAdminId() : null, "DELETE_USER", "Deleted user: " + username + " (" + userID + ")");
                    sendAdminNotification("User " + username + " deleted.", "System");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete user. User might not exist or there's a database constraint issue.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            } finally {
                try {
                    if (pstmt != null) pstmt.close();
                    if (conn != null) conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    
    private void addAgentAction() {
        JDialog addAgentDialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Add New Agent", Dialog.ModalityType.APPLICATION_MODAL);
        addAgentDialog.setLayout(new BorderLayout(10, 10));
        addAgentDialog.setBackground(new Color(30, 40, 60));
        addAgentDialog.setMinimumSize(new Dimension(500, 500));
        addAgentDialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JTextField usernameField = createStyledTextField();
        JTextField emailField = createStyledTextField();
        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 16));
        passwordField.setPreferredSize(new Dimension(200, 35));
        passwordField.setBackground(new Color(60, 60, 60));
        passwordField.setForeground(Color.WHITE);
        passwordField.setCaretColor(Color.WHITE);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 100, 100), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        JTextField phoneNumberField = createStyledTextField();
        JTextArea addressArea = new JTextArea(3, 20);
        addressArea.setBackground(new Color(60, 60, 60));
        addressArea.setForeground(Color.WHITE);
        addressArea.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 1));
        JScrollPane addressScrollPane = new JScrollPane(addressArea);
        addressScrollPane.getViewport().setBackground(new Color(60, 60, 60));

        JTextField airlineField = createStyledTextField();
        JTextField commissionRateField = createStyledTextField();
        JSpinner accessLevelSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 5, 1)); 
        ((JSpinner.DefaultEditor) accessLevelSpinner.getEditor()).getTextField().setBackground(new Color(60, 60, 60));
        ((JSpinner.DefaultEditor) accessLevelSpinner.getEditor()).getTextField().setForeground(Color.WHITE);


        addFormField(formPanel, createStyledLabel("Username:"), usernameField, gbc, 0);
        addFormField(formPanel, createStyledLabel("Email:"), emailField, gbc, 1);
        addFormField(formPanel, createStyledLabel("Password:"), passwordField, gbc, 2);
        addFormField(formPanel, createStyledLabel("Phone Number:"), phoneNumberField, gbc, 3);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        formPanel.add(createStyledLabel("Address:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        formPanel.add(addressScrollPane, gbc);
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        addFormField(formPanel, createStyledLabel("Airline:"), airlineField, gbc, 5); 
        addFormField(formPanel, createStyledLabel("Commission Rate (%):"), commissionRateField, gbc, 6);
        addFormField(formPanel, createStyledLabel("Access Level:"), accessLevelSpinner, gbc, 7);

        JButton saveButton = createStyledButton("Add Agent");
        saveButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword()); 
            String phoneNumber = phoneNumberField.getText().trim();
            String address = addressArea.getText().trim();
            String airline = airlineField.getText().trim();
            String commissionRateStr = commissionRateField.getText().trim();
            int accessLevel = (Integer) accessLevelSpinner.getValue();

            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || airline.isEmpty() || commissionRateStr.isEmpty()) {
                JOptionPane.showMessageDialog(addAgentDialog, "Please fill in all required fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double commissionRate;
            try {
                commissionRate = Double.parseDouble(commissionRateStr);
                if (commissionRate < 0 || commissionRate > 100) {
                    JOptionPane.showMessageDialog(addAgentDialog, "Commission Rate must be between 0 and 100.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(addAgentDialog, "Invalid Commission Rate. Please enter a number.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            addAgentToDatabase(username, email, password, phoneNumber, address, airline, commissionRate, accessLevel, addAgentDialog);
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        buttonPanel.add(saveButton);

        addAgentDialog.add(formPanel, BorderLayout.CENTER);
        addAgentDialog.add(buttonPanel, BorderLayout.SOUTH);
        addAgentDialog.pack();
        addAgentDialog.setVisible(true);
    }

    private void addAgentToDatabase(String username, String email, String password, String phoneNumber, String address, String airline, double commissionRate, int accessLevel, JDialog dialog) {
        Connection conn = null;
        PreparedStatement pstmtUser = null;
        PreparedStatement pstmtAgent = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConnection.connect();
            conn.setAutoCommit(false); 

            
            String userID = UUID.randomUUID().toString(); 
            String sqlUser = "INSERT INTO users (user_id, username, password_hash, email, phone_number, address, registration_date, role, is_blacklisted) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            pstmtUser = conn.prepareStatement(sqlUser);
            pstmtUser.setString(1, userID);
            pstmtUser.setString(2, username);
            pstmtUser.setString(3, password); 
            pstmtUser.setString(4, email);
            pstmtUser.setString(5, phoneNumber);
            pstmtUser.setString(6, address);
            pstmtUser.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
            pstmtUser.setString(8, "AGENT");
            pstmtUser.setBoolean(9, false); 
            pstmtUser.executeUpdate();
            pstmtUser.close();

            
            String agentID = "AGT" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            String sqlAgent = "INSERT INTO agents (agent_id, user_id, airline, commission_rate, access_level) VALUES (?, ?, ?, ?, ?)";
            pstmtAgent = conn.prepareStatement(sqlAgent);
            pstmtAgent.setString(1, agentID);
            pstmtAgent.setString(2, userID);
            pstmtAgent.setString(3, airline);
            pstmtAgent.setDouble(4, commissionRate);
            pstmtAgent.setInt(5, accessLevel);
            pstmtAgent.executeUpdate();

            conn.commit(); 
            JOptionPane.showMessageDialog(dialog, "Agent " + username + " added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dialog.dispose();
            searchUsersForManagement(); 
            logSystemAction(loggedInAdmin != null ? loggedInAdmin.getAdminId() : null, "ADD_AGENT", "Added new agent: " + username + " (User ID: " + userID + ")");
            sendAdminNotification("New agent " + username + " added.", "System");

        } catch (SQLException ex) {
            try {
                if (conn != null) conn.rollback(); 
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            JOptionPane.showMessageDialog(dialog, "Database error adding agent: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmtUser != null) pstmtUser.close();
                if (pstmtAgent != null) pstmtAgent.close();
                if (conn != null) {
                    conn.setAutoCommit(true); 
                    conn.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }


    
    private JPanel createViewLogsPanel() {
    JPanel panel = createContentPanel("View System Logs");
    
    JPanel mainContentPanel = new JPanel();
    mainContentPanel.setLayout(new BoxLayout(mainContentPanel, BoxLayout.Y_AXIS));
    mainContentPanel.setOpaque(false);
    mainContentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

    
    mainContentPanel.add(createLogFiltersSection());
    mainContentPanel.add(Box.createVerticalStrut(15));
    
    
    mainContentPanel.add(createLogDisplaySection());
    mainContentPanel.add(Box.createVerticalStrut(15));
    
    
    JPanel actionButtonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
    actionButtonsPanel.setOpaque(false);
    
    exportLogsButton = createStyledButton("Export Logs");
    exportLogsButton.addActionListener(e -> exportSystemLogs());
    actionButtonsPanel.add(exportLogsButton);
    
    errorTraceLogsButton = createStyledButton("View Error Traces");
    errorTraceLogsButton.addActionListener(e -> viewErrorTraceLogs());
    actionButtonsPanel.add(errorTraceLogsButton);
    
    mainContentPanel.add(actionButtonsPanel);
    
    panel.add(mainContentPanel, BorderLayout.CENTER);
    return panel;
}

private JPanel createLogFiltersSection() {
    JPanel filterPanel = new JPanel();
    filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.X_AXIS));
    filterPanel.setOpaque(false);
    filterPanel.setBorder(BorderFactory.createTitledBorder("Filter Logs"));
    
    Font inputFont = new Font("Arial", Font.PLAIN, 14);
    Dimension fieldSize = new Dimension(200, 30);
    int gap = 15;
    
    
    MaskFormatter dateFormatter = null;
    try {
        dateFormatter = new MaskFormatter("####-##-##");
        dateFormatter.setPlaceholderCharacter('_');
    } catch (ParseException e) {
        e.printStackTrace();
    }
    
    
    JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
    datePanel.setOpaque(false);
    datePanel.add(createStyledLabel("Date:"));
    
    logDateField = new JFormattedTextField(dateFormatter);
    logDateField.setPreferredSize(fieldSize);
    logDateField.setFont(inputFont);
    logDateField.setColumns(10);
    styleTextField(logDateField);
    datePanel.add(logDateField);
    filterPanel.add(datePanel);
    
    filterPanel.add(Box.createHorizontalStrut(gap));
    
    
    JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
    actionPanel.setOpaque(false);
    actionPanel.add(createStyledLabel("Action:"));
    logActionField = new JTextField();
    logActionField.setPreferredSize(fieldSize);
    logActionField.setFont(inputFont);
    styleTextField(logActionField);
    actionPanel.add(logActionField);
    filterPanel.add(actionPanel);
    
    filterPanel.add(Box.createHorizontalStrut(gap));
    
    
    JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
    userPanel.setOpaque(false);
    userPanel.add(createStyledLabel("User ID:"));
    logUserField = new JTextField();
    logUserField.setPreferredSize(fieldSize);
    logUserField.setFont(inputFont);
    styleTextField(logUserField);
    userPanel.add(logUserField);
    filterPanel.add(userPanel);
    
    filterPanel.add(Box.createHorizontalStrut(gap));
    
    
    JButton filterButton = createStyledButton("Apply Filters");
    filterButton.setPreferredSize(new Dimension(150, 30));
    filterButton.setFont(inputFont);
    filterButton.addActionListener(e -> viewSystemLogs());
    filterPanel.add(filterButton);
    
    return filterPanel;
}

private JPanel createLogDisplaySection() {
    JPanel displayPanel = new JPanel(new BorderLayout());
    displayPanel.setOpaque(false);
    displayPanel.setBorder(BorderFactory.createTitledBorder("Log Entries"));
    
    logDisplayArea = new JTextArea();
    logDisplayArea.setEditable(false);
    logDisplayArea.setBackground(new Color(60, 60, 60));
    logDisplayArea.setForeground(Color.WHITE);
    logDisplayArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
    
    JScrollPane scrollPane = new JScrollPane(logDisplayArea);
    scrollPane.getViewport().setBackground(new Color(60, 60, 60));
    scrollPane.setPreferredSize(new Dimension(800, 400));
    
    displayPanel.add(scrollPane, BorderLayout.CENTER);
    return displayPanel;
}



    private void viewSystemLogs() {
        logDisplayArea.setText(""); 
        StringBuilder logs = new StringBuilder();
        logs.append("--- System Logs ---\n\n");

        String dateFilter = logDateField.getText().trim();
        String actionFilter = logActionField.getText().trim();
        String userFilter = logUserField.getText().trim();

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConnection.connect();
            String sql = "SELECT log_id, user_id, action, log_type, log_time FROM system_logs WHERE 1=1 ";
            if (!dateFilter.isEmpty()) {
                sql += "AND DATE(log_time) = ? ";
            }
            if (!actionFilter.isEmpty()) {
                sql += "AND action LIKE ? ";
            }
            if (!userFilter.isEmpty()) {
                sql += "AND user_id LIKE ? ";
            }
            sql += "ORDER BY log_time DESC LIMIT 200";

            pstmt = conn.prepareStatement(sql);
            int paramIndex = 1;
            if (!dateFilter.isEmpty()) {
                pstmt.setString(paramIndex++, dateFilter);
            }
            if (!actionFilter.isEmpty()) {
                pstmt.setString(paramIndex++, "%" + actionFilter + "%");
            }
            if (!userFilter.isEmpty()) {
                pstmt.setString(paramIndex++, "%" + userFilter + "%");
            }
            rs = pstmt.executeQuery();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            while (rs.next()) {
                logs.append("ID: ").append(rs.getString("log_id")).append("\n");
                logs.append("User: ").append(rs.getString("user_id")).append("\n");
                logs.append("Action: ").append(rs.getString("action")).append("\n");
                logs.append("Type: ").append(rs.getString("log_type")).append("\n");
                logs.append("Time: ").append(sdf.format(rs.getTimestamp("log_time"))).append("\n");
                logs.append("-----------------------------------\n");
            }

            if (logs.toString().equals("--- System Logs ---\n\n")) {
                logs.append("No logs found matching the criteria.");
            }

        } catch (SQLException ex) {
            logs.append("Error fetching logs: ").append(ex.getMessage()).append("\n");
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        logDisplayArea.setText(logs.toString());
    }

    private void exportSystemLogs() {
        File exportsDir = new File("exports");
        if (!exportsDir.exists()) {
            exportsDir.mkdir(); 
        }
        
        String fileName = "system_logs_" + System.currentTimeMillis() + ".txt";
        String filePath = "exports/" + fileName;
        
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM system_logs ORDER BY log_time DESC");
             ResultSet rs = pstmt.executeQuery();
             java.io.PrintWriter writer = new java.io.PrintWriter(filePath)) {

            while (rs.next()) {
                writer.println("[" + rs.getTimestamp("log_time") + "] "
                    + rs.getString("log_type") + " - "
                    + rs.getString("user_id") + ": "
                    + rs.getString("action"));
            }

            
            try (PreparedStatement insertFile = conn.prepareStatement(
                "INSERT INTO file_manager (file_path, file_type) VALUES (?, ?)")) {
                insertFile.setString(1, filePath);
                insertFile.setString(2, "log");
                insertFile.executeUpdate();
            }

            JOptionPane.showMessageDialog(this, "Logs exported to: " + filePath, "Export Successful", JOptionPane.INFORMATION_MESSAGE);
            logSystemAction(loggedInAdmin.getAdminId(), "EXPORT_LOGS", "Exported system logs to file: " + filePath);
            sendAdminNotification("System logs exported to " + filePath, "System");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error exporting logs: " + ex.getMessage(), "Export Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }


    private void viewErrorTraceLogs() {
        
        logActionField.setText("ERROR");
        viewSystemLogs();
    }
        

    
    private void logSystemAction(String userId, String action, String details) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DatabaseConnection.connect();
            if (conn == null) {
                System.err.println("Failed to get database connection for logging system action.");
                return;
            }

            if (userId == null || userId.trim().isEmpty()) {
                userId = "SYSTEM"; 
            }
            
            
            
            String safeDetails = (details != null && details.length() > 255) ? details.substring(0, 255) : details;
            String safeAction = (action != null && action.length() > 50) ? action.substring(0, 50) : action;

            String sql = "INSERT INTO system_logs (log_id, user_id, action, log_type, log_time) VALUES (?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "LOG" + UUID.randomUUID().toString().substring(0, 8).toUpperCase()); 
            
            if (userId == null) {
                pstmt.setNull(2, java.sql.Types.VARCHAR);
            } else {
                pstmt.setString(2, userId);
            }
            pstmt.setString(3, safeDetails); 
            pstmt.setString(4, safeAction); 
            pstmt.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("Error logging system action: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }


    
    private JPanel createGenerateReportsPanel() {
    JPanel panel = createContentPanel("Generate System Reports");
    
    JPanel mainContentPanel = new JPanel();
    mainContentPanel.setLayout(new BoxLayout(mainContentPanel, BoxLayout.Y_AXIS));
    mainContentPanel.setOpaque(false);
    mainContentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

    
    mainContentPanel.add(createReportOptionsSection());
    mainContentPanel.add(Box.createVerticalStrut(15));
    
    
    mainContentPanel.add(createReportDisplaySection());
    
    panel.add(mainContentPanel, BorderLayout.CENTER);
    return panel;
}

private JPanel createReportOptionsSection() {
    JPanel optionsPanel = new JPanel();
    optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.X_AXIS));
    optionsPanel.setOpaque(false);
    optionsPanel.setBorder(BorderFactory.createTitledBorder("Report Options"));
    
    Font inputFont = new Font("Arial", Font.PLAIN, 14);
    Dimension fieldSize = new Dimension(200, 30);
    int gap = 15;
    
    
    JPanel startDatePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
    startDatePanel.setOpaque(false);
    startDatePanel.add(createStyledLabel("Start Date:"));
    
    
    MaskFormatter dateFormatter = null;
    try {
        dateFormatter = new MaskFormatter("####-##-##");
        dateFormatter.setPlaceholderCharacter('_');
    } catch (ParseException e) {
        e.printStackTrace();
    }
    
    reportStartDateField = new JFormattedTextField(dateFormatter);
    reportStartDateField.setPreferredSize(fieldSize);
    reportStartDateField.setFont(inputFont);
    reportStartDateField.setColumns(10); 
    styleTextField(reportStartDateField); 
    
    startDatePanel.add(reportStartDateField);
    optionsPanel.add(startDatePanel);
    
    optionsPanel.add(Box.createHorizontalStrut(gap));
    
    
    JPanel endDatePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
    endDatePanel.setOpaque(false);
    endDatePanel.add(createStyledLabel("End Date:"));
    
    reportEndDateField = new JFormattedTextField(dateFormatter);
    reportEndDateField.setPreferredSize(fieldSize);
    reportEndDateField.setFont(inputFont);
    reportEndDateField.setColumns(10);
    styleTextField(reportEndDateField);
    
    endDatePanel.add(reportEndDateField);
    optionsPanel.add(endDatePanel);
    
    optionsPanel.add(Box.createHorizontalStrut(gap));
    
    
    JPanel typePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
    typePanel.setOpaque(false);
    typePanel.add(createStyledLabel("Report Type:"));
    reportTypeComboBox = createStyledComboBox();
    reportTypeComboBox.setPreferredSize(fieldSize);
    reportTypeComboBox.setFont(inputFont);
    reportTypeComboBox.addItem("Bookings");
    reportTypeComboBox.addItem("Cancellations");
    reportTypeComboBox.addItem("Revenue");
    reportTypeComboBox.addItem("Tickets");
    reportTypeComboBox.addItem("All");
    typePanel.add(reportTypeComboBox);
    optionsPanel.add(typePanel);
    
    optionsPanel.add(Box.createHorizontalStrut(gap));
    
    
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
    buttonPanel.setOpaque(false);
    
    generateReportButton = createStyledButton("Generate");
    generateReportButton.setPreferredSize(new Dimension(120, 30));
    generateReportButton.setFont(inputFont);
    generateReportButton.addActionListener(e -> generateSystemReport());
    buttonPanel.add(generateReportButton);
    
    exportReportButton = createStyledButton("Export");
    exportReportButton.setPreferredSize(new Dimension(120, 30));
    exportReportButton.setFont(inputFont);
    exportReportButton.addActionListener(e -> exportSystemReportAction());
    buttonPanel.add(exportReportButton);
    
    optionsPanel.add(buttonPanel);
    
    return optionsPanel;
}


private void styleTextField(JTextField textField) {
    textField.setBackground(new Color(240, 240, 240));
    textField.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(180, 180, 180)),
        BorderFactory.createEmptyBorder(5, 5, 5, 5)
    ));
}

private JPanel createReportDisplaySection() {
    JPanel displayPanel = new JPanel(new BorderLayout());
    displayPanel.setOpaque(false);
    displayPanel.setBorder(BorderFactory.createTitledBorder("Report Output"));
    
    reportDisplayArea = new JTextArea();
    reportDisplayArea.setEditable(false);
    reportDisplayArea.setBackground(new Color(60, 60, 60));
    reportDisplayArea.setForeground(Color.WHITE);
    reportDisplayArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
    
    JScrollPane scrollPane = new JScrollPane(reportDisplayArea);
    scrollPane.getViewport().setBackground(new Color(60, 60, 60));
    scrollPane.setPreferredSize(new Dimension(800, 400));
    
    displayPanel.add(scrollPane, BorderLayout.CENTER);
    return displayPanel;
}


    private void generateSystemReport() {
        reportDisplayArea.setText("");
        StringBuilder reportContent = new StringBuilder();
        reportContent.append("--- System Report ---\n\n");

        String startDateStr = reportStartDateField.getText().trim();
        String endDateStr = reportEndDateField.getText().trim();
        String reportType = (String) reportTypeComboBox.getSelectedItem();

        Date startDate = null;
        Date endDate = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        try {
            if (!startDateStr.isEmpty()) startDate = sdf.parse(startDateStr);
            if (!endDateStr.isEmpty()) endDate = sdf.parse(endDateStr);
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(this, "Invalid date format. Use YYYY-MM-DD.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        
        SystemReport report = loggedInAdmin.generateSystemReport();
        reportContent.append(report.toString()).append("\n\n");

        
        if (reportType.equals("Bookings") || reportType.equals("All")) {
            reportContent.append("--- Detailed Bookings ---\n");
            
            addBookingDetailsToReport(reportContent, startDate, endDate);
        }

        if (reportType.equals("Cancellations") || reportType.equals("All")) {
            reportContent.append("\n--- Detailed Cancellations ---\n");
            
            addCancellationDetailsToReport(reportContent, startDate, endDate);
        }

        if (reportType.equals("Tickets") || reportType.equals("All")) { 
            reportContent.append("\n--- Detailed Tickets ---\n");
            addTicketDetailsToReport(reportContent, startDate, endDate);
        }

        reportDisplayArea.setText(reportContent.toString());
        logSystemAction(loggedInAdmin != null ? loggedInAdmin.getAdminId() : null, 
                       "GENERATE_REPORT", "Generated " + reportType + " report");
    }

    private void addBookingDetailsToReport(StringBuilder reportContent, Date startDate, Date endDate) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConnection.connect();
            String sql = "SELECT b.booking_id, b.customer_id, b.flight_id, b.booking_date, " +
                         "b.total_price, b.payment_status, f.flight_num " +
                         "FROM booking b JOIN flight f ON b.flight_id = f.flight_id WHERE 1=1 ";
            
            if (startDate != null) {
                sql += "AND b.booking_date >= ? ";
            }
            if (endDate != null) {
                sql += "AND b.booking_date <= ? ";
            }
            sql += "ORDER BY b.booking_date DESC LIMIT 50";
            
            pstmt = conn.prepareStatement(sql);
            int paramIndex = 1;
            if (startDate != null) {
                pstmt.setTimestamp(paramIndex++, new Timestamp(startDate.getTime()));
            }
            if (endDate != null) {
                pstmt.setTimestamp(paramIndex++, new Timestamp(endDate.getTime()));
            }
            
            rs = pstmt.executeQuery();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            
            while (rs.next()) {
                reportContent.append("Booking ID: ").append(rs.getString("booking_id")).append("\n");
                reportContent.append("Customer ID: ").append(rs.getString("customer_id")).append("\n");
                reportContent.append("Flight: ").append(rs.getString("flight_num")).append(" (").append(rs.getString("flight_id")).append(")\n");
                reportContent.append("Date: ").append(dateFormat.format(rs.getTimestamp("booking_date"))).append("\n");
                reportContent.append("Price: $").append(rs.getDouble("total_price")).append("\n");
                reportContent.append("Status: ").append(rs.getString("payment_status")).append("\n");
                reportContent.append("--------------------\n");
            }
        } catch (SQLException ex) {
            reportContent.append("Error fetching booking details: ").append(ex.getMessage()).append("\n");
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void addCancellationDetailsToReport(StringBuilder reportContent, Date startDate, Date endDate) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConnection.connect();
            String sql = "SELECT b.booking_id, b.customer_id, b.flight_id, b.booking_date, " +
                         "b.total_price, r.reason, r.RequestDate " +
                         "FROM booking b JOIN refundrequests r ON b.booking_id = r.booking_id " +
                         "WHERE b.payment_status = 'Cancelled' OR b.payment_status = 'Refunded' ";
            
            if (startDate != null) {
                sql += "AND r.RequestDate >= ? ";
            }
            if (endDate != null) {
                sql += "AND r.RequestDate <= ? ";
            }
            sql += "ORDER BY r.RequestDate DESC LIMIT 50";
            
            pstmt = conn.prepareStatement(sql);
            int paramIndex = 1;
            if (startDate != null) {
                pstmt.setTimestamp(paramIndex++, new Timestamp(startDate.getTime()));
            }
            if (endDate != null) {
                pstmt.setTimestamp(paramIndex++, new Timestamp(endDate.getTime()));
            }
            
            rs = pstmt.executeQuery();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            
            while (rs.next()) {
                reportContent.append("Booking ID: ").append(rs.getString("booking_id")).append("\n");
                reportContent.append("Customer ID: ").append(rs.getString("customer_id")).append("\n");
                reportContent.append("Flight ID: ").append(rs.getString("flight_id")).append("\n");
                reportContent.append("Original Date: ").append(dateFormat.format(rs.getTimestamp("booking_date"))).append("\n");
                reportContent.append("Amount: $").append(rs.getDouble("total_price")).append("\n");
                reportContent.append("Cancellation Date: ").append(dateFormat.format(rs.getTimestamp("RequestDate"))).append("\n");
                reportContent.append("Reason: ").append(rs.getString("reason")).append("\n");
                reportContent.append("--------------------\n");
            }
        } catch (SQLException ex) {
            reportContent.append("Error fetching cancellation details: ").append(ex.getMessage()).append("\n");
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    
    private void addTicketDetailsToReport(StringBuilder reportContent, Date startDate, Date endDate) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConnection.connect();
            String sql = "SELECT t.ticket_id, t.seat_number, t.boarding_time, t.gate_number, t.ticket_status, " +
                         "b.booking_id, b.customer_id, f.flight_num, p.first_name, p.last_name " +
                         "FROM ticket t " +
                         "JOIN booking b ON t.booking_id = b.booking_id " +
                         "JOIN flight f ON b.flight_id = f.flight_id " +
                         "JOIN passenger p ON t.passenger_id = p.passenger_id " +
                         "WHERE 1=1 ";
            
            if (startDate != null) {
                sql += "AND t.boarding_time >= ? ";
            }
            if (endDate != null) {
                sql += "AND t.boarding_time <= ? ";
            }
            sql += "ORDER BY t.boarding_time DESC LIMIT 100";
            
            pstmt = conn.prepareStatement(sql);
            int paramIndex = 1;
            if (startDate != null) {
                pstmt.setTimestamp(paramIndex++, new Timestamp(startDate.getTime()));
            }
            if (endDate != null) {
                pstmt.setTimestamp(paramIndex++, new Timestamp(endDate.getTime()));
            }
            
            rs = pstmt.executeQuery();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            
            while (rs.next()) {
                reportContent.append("Ticket ID: ").append(rs.getString("ticket_id")).append("\n");
                reportContent.append("Booking ID: ").append(rs.getString("booking_id")).append("\n");
                reportContent.append("Flight Number: ").append(rs.getString("flight_num")).append("\n");
                reportContent.append("Passenger: ").append(rs.getString("first_name")).append(" ").append(rs.getString("last_name")).append("\n");
                reportContent.append("Seat: ").append(rs.getString("seat_number")).append("\n");
                reportContent.append("Boarding Time: ").append(dateFormat.format(rs.getTimestamp("boarding_time"))).append("\n");
                reportContent.append("Gate: ").append(rs.getString("gate_number")).append("\n");
                reportContent.append("Status: ").append(rs.getString("ticket_status")).append("\n");
                reportContent.append("--------------------\n");
            }
        } catch (SQLException ex) {
            reportContent.append("Error fetching ticket details: ").append(ex.getMessage()).append("\n");
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    
    private void exportSystemReportAction() {
        String reportContent = reportDisplayArea.getText();
        if (reportContent.isEmpty() || reportContent.equals("--- System Report ---\n\n")) {
            JOptionPane.showMessageDialog(this, "No report generated to export.", "Export Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String reportType = (String) reportTypeComboBox.getSelectedItem();
        String fileName = reportType.toLowerCase().replaceAll("\\s+", "_") + "_report_" + System.currentTimeMillis() + ".txt";
        saveReportToFileManager(reportContent, fileName, "report-" + reportType.toLowerCase().replaceAll("\\s+", "_"));
    }

    
    private void saveReportToFileManager(String content, String fileName, String fileType) {
        File exportsDir = new File("exports");
        if (!exportsDir.exists()) {
            exportsDir.mkdir();
        }
        
        String filePath = "exports/" + fileName;
        
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(content);

            Connection conn = null;
            PreparedStatement insertFile = null;
            try {
                conn = DatabaseConnection.connect();
                insertFile = conn.prepareStatement(
                    "INSERT INTO file_manager (file_path, file_type) VALUES (?, ?)");
                insertFile.setString(1, filePath);
                insertFile.setString(2, fileType);
                insertFile.executeUpdate();
            } finally {
                if (insertFile != null) insertFile.close();
                if (conn != null) conn.close();
            }

            JOptionPane.showMessageDialog(this, "Report exported to: " + filePath, "Export Successful", JOptionPane.INFORMATION_MESSAGE);
            logSystemAction(loggedInAdmin != null ? loggedInAdmin.getAdminId() : null, "EXPORT_REPORT", "Exported " + fileType + " report to " + filePath);
            sendAdminNotification("Report '" + fileType + "' exported to " + filePath, "System");

        } catch (IOException | SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error exporting report: " + ex.getMessage(), "Export Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }


    
    private JPanel createBlockUnblockUserPanel() {
        JPanel panel = createContentPanel("Block User");
        JPanel mainContentPanel = new JPanel();
        mainContentPanel.setLayout(new BoxLayout(mainContentPanel, BoxLayout.Y_AXIS));
        mainContentPanel.setOpaque(false);
        mainContentPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        
        JPanel blockUserSection = new JPanel(new GridBagLayout());
        blockUserSection.setOpaque(false);
        blockUserSection.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY), "Block User", 
            TitledBorder.LEFT, TitledBorder.TOP, 
            new Font("Arial", Font.BOLD, 16), Color.WHITE));
        GridBagConstraints gbcBlock = new GridBagConstraints();
        gbcBlock.fill = GridBagConstraints.HORIZONTAL;
        gbcBlock.insets = new Insets(5, 5, 5, 5);

        blockUserIdField = createStyledTextField();
        blockReasonField = createStyledTextField();
        blockUserButton = createStyledButton("Block User");
        notifyUserCheckbox = new JCheckBox("Notify User");
        notifyUserCheckbox.setForeground(Color.WHITE);
        notifyUserCheckbox.setOpaque(false);
        restrictAccountCheckbox = new JCheckBox("Restrict Account Actions");
        restrictAccountCheckbox.setForeground(Color.WHITE);
        restrictAccountCheckbox.setOpaque(false);

        addFormField(blockUserSection, createStyledLabel("User ID to Block:"), blockUserIdField, gbcBlock, 0);
        addFormField(blockUserSection, createStyledLabel("Reason for Blocking:"), blockReasonField, gbcBlock, 1);

        gbcBlock.gridx = 0;
        gbcBlock.gridy = 2;
        gbcBlock.gridwidth = 2;
        gbcBlock.anchor = GridBagConstraints.WEST;
        gbcBlock.insets = new Insets(10, 5, 5, 5);
        blockUserSection.add(notifyUserCheckbox, gbcBlock);

        gbcBlock.gridy = 3;
        blockUserSection.add(restrictAccountCheckbox, gbcBlock);

        JPanel blockButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        blockButtonPanel.setOpaque(false);
        blockButtonPanel.add(blockUserButton);
        gbcBlock.gridy = 4;
        gbcBlock.gridwidth = 2;
        blockUserSection.add(blockButtonPanel, gbcBlock);

        blockUserButton.addActionListener(this::blockUser);

        
        JPanel unblockUserSection = new JPanel(new GridBagLayout());
        unblockUserSection.setOpaque(false);
        unblockUserSection.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY), "Unblock User", 
            TitledBorder.LEFT, TitledBorder.TOP, 
            new Font("Arial", Font.BOLD, 16), Color.WHITE));
        GridBagConstraints gbcUnblock = new GridBagConstraints();
        gbcUnblock.fill = GridBagConstraints.HORIZONTAL;
        gbcUnblock.insets = new Insets(5, 5, 5, 5);

        unblockUserIdField = createStyledTextField();
        unblockUserButton = createStyledButton("Unblock User");

        addFormField(unblockUserSection, createStyledLabel("User ID to Unblock:"), unblockUserIdField, gbcUnblock, 0);

        JPanel unblockButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        unblockButtonPanel.setOpaque(false);
        unblockButtonPanel.add(unblockUserButton);
        gbcUnblock.gridy = 1;
        gbcUnblock.gridwidth = 2;
        unblockUserSection.add(unblockButtonPanel, gbcUnblock);

        unblockUserButton.addActionListener(this::unblockUser);

        mainContentPanel.add(blockUserSection);
        mainContentPanel.add(Box.createVerticalStrut(30)); 
        mainContentPanel.add(unblockUserSection);

        panel.add(mainContentPanel, BorderLayout.CENTER);

        return panel;
    }

    private void blockUser(ActionEvent e) {
        String userIdToBlock = blockUserIdField.getText().trim();
        String reason = blockReasonField.getText().trim();

        if (userIdToBlock.isEmpty() || reason.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter User ID and Reason.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConnection.connect();
            conn.setAutoCommit(false);

            
            String checkSql = "SELECT username, is_blacklisted FROM users WHERE user_id = ?";
            pstmt = conn.prepareStatement(checkSql);
            pstmt.setString(1, userIdToBlock);
            rs = pstmt.executeQuery();

            if (!rs.next()) {
                JOptionPane.showMessageDialog(this, "User with ID " + userIdToBlock + " not found.", "User Not Found", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean isAlreadyBlacklisted = rs.getBoolean("is_blacklisted");
            String username = rs.getString("username");
            rs.close();
            pstmt.close();

            if (isAlreadyBlacklisted) {
                JOptionPane.showMessageDialog(this, "User " + username + " is already blacklisted.", "Already Blacklisted", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            
            String blacklistId = "BL" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            String insertSql = "INSERT INTO blacklist (blacklist_id, user_id, reason, date_added, added_by, is_active) " +
                              "VALUES (?, ?, ?, CURDATE(), ?, 1)";
            pstmt = conn.prepareStatement(insertSql);
            pstmt.setString(1, blacklistId);
            pstmt.setString(2, userIdToBlock);
            pstmt.setString(3, reason);
            pstmt.setString(4, loggedInAdmin != null ? loggedInAdmin.getAdminId() : "SYSTEM");
            pstmt.executeUpdate();
            pstmt.close();

            
            String updateSql = "UPDATE users SET is_blacklisted = 1 WHERE user_id = ?";
            pstmt = conn.prepareStatement(updateSql);
            pstmt.setString(1, userIdToBlock);
            pstmt.executeUpdate();

            conn.commit();
            JOptionPane.showMessageDialog(this, "User " + username + " has been successfully blacklisted.", "User Blocked", JOptionPane.INFORMATION_MESSAGE);
            logSystemAction(loggedInAdmin != null ? loggedInAdmin.getAdminId() : null, 
                           "BLOCK_USER", "Blacklisted user: " + username + " (" + userIdToBlock + ")");
            sendAdminNotification("User " + username + " blacklisted. Reason: " + reason, "System");


            if (notifyUserCheckbox.isSelected()) {
                
                String notifySql = "INSERT INTO notifications (notification_id, user_id, message, notification_time, sender_role) " +
                                 "VALUES (?, ?, ?, NOW(), ?)";
                pstmt = conn.prepareStatement(notifySql);
                pstmt.setString(1, "NOTIF" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
                pstmt.setString(2, userIdToBlock);
                pstmt.setString(3, "Your account has been restricted. Reason: " + reason);
                pstmt.setString(4, "ADMINISTRATOR");
                pstmt.executeUpdate();
            }

            
            blockUserIdField.setText("");
            blockReasonField.setText("");
            notifyUserCheckbox.setSelected(false);
            restrictAccountCheckbox.setSelected(false);

        } catch (SQLException ex) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            JOptionPane.showMessageDialog(this, "Database error during user blocking: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void unblockUser(ActionEvent e) {
        String userIdToUnblock = unblockUserIdField.getText().trim();

        if (userIdToUnblock.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter User ID to unblock.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConnection.connect();
            conn.setAutoCommit(false);

            
            String checkSql = "SELECT username, is_blacklisted FROM users WHERE user_id = ?";
            pstmt = conn.prepareStatement(checkSql);
            pstmt.setString(1, userIdToUnblock);
            rs = pstmt.executeQuery();

            if (!rs.next()) {
                JOptionPane.showMessageDialog(this, "User with ID " + userIdToUnblock + " not found.", "User Not Found", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean isCurrentlyBlacklisted = rs.getBoolean("is_blacklisted");
            String username = rs.getString("username");
            rs.close();
            pstmt.close();

            if (!isCurrentlyBlacklisted) {
                JOptionPane.showMessageDialog(this, "User " + username + " is not currently blacklisted.", "Not Blacklisted", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            
            String updateBlacklistSql = "UPDATE blacklist SET is_active = 0 WHERE user_id = ? AND is_active = 1";
            pstmt = conn.prepareStatement(updateBlacklistSql);
            pstmt.setString(1, userIdToUnblock);
            pstmt.executeUpdate();
            pstmt.close();

            
            String updateUserSql = "UPDATE users SET is_blacklisted = 0 WHERE user_id = ?";
            pstmt = conn.prepareStatement(updateUserSql);
            pstmt.setString(1, userIdToUnblock);
            pstmt.executeUpdate();

            conn.commit();
            JOptionPane.showMessageDialog(this, "User " + username + " has been successfully unblocked.", "User Unblocked", JOptionPane.INFORMATION_MESSAGE);
            logSystemAction(loggedInAdmin != null ? loggedInAdmin.getAdminId() : null, 
                           "UNBLOCK_USER", "Unblocked user: " + username + " (" + userIdToUnblock + ")");
            sendAdminNotification("User " + username + " unblocked.", "System");

            
            unblockUserIdField.setText("");

        } catch (SQLException ex) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            JOptionPane.showMessageDialog(this, "Database error during user unblocking: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }


    
    private JPanel createSystemSettingsPanel() {
        JPanel panel = createContentPanel("Configure System Settings");
        JPanel formPanel = createFormPanel();
        GridBagConstraints gbc = new GridBagConstraints();

        fareMultiplierField = createStyledTextField();
        maintenanceModeToggle = new JToggleButton("Maintenance Mode (OFF)");
        maintenanceModeToggle.setFont(new Font("Arial", Font.BOLD, 16));
        maintenanceModeToggle.setPreferredSize(new Dimension(250, 40));
        maintenanceModeToggle.setForeground(Color.WHITE);
        maintenanceModeToggle.setBackground(new Color(102, 102, 204));
        maintenanceModeToggle.setFocusPainted(false);
        maintenanceModeToggle.addActionListener(e -> {
            if (maintenanceModeToggle.isSelected()) {
                maintenanceModeToggle.setText("Maintenance Mode (ON)");
                maintenanceModeToggle.setBackground(new Color(204, 51, 51));
            } else {
                maintenanceModeToggle.setText("Maintenance Mode (OFF)");
                maintenanceModeToggle.setBackground(new Color(102, 102, 204));
            }
        });
        notificationTemplatesArea = new JTextArea(10, 30);
        notificationTemplatesArea.setBackground(new Color(60, 60, 60));
        notificationTemplatesArea.setForeground(Color.WHITE);
        notificationTemplatesArea.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 1));
        JScrollPane scrollPane = new JScrollPane(notificationTemplatesArea);
        scrollPane.getViewport().setBackground(new Color(60, 60, 60));

        addFormField(formPanel, createStyledLabel("Fare Multiplier:"), fareMultiplierField, gbc, 0);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 0, 5, 0);
        formPanel.add(maintenanceModeToggle, gbc);

        gbc.gridy = 2;
        gbc.insets = new Insets(10, 0, 5, 0);
        formPanel.add(createStyledLabel("Notification Templates:"), gbc);

        gbc.gridy = 3;
        gbc.weighty = 1.0; 
        gbc.fill = GridBagConstraints.BOTH;
        formPanel.add(scrollPane, gbc);
        gbc.weighty = 0; 

        saveSettingsButton = createStyledButton("Save Settings");
        saveSettingsButton.addActionListener(e -> saveSystemSettings());
        testSettingsButton = createStyledButton("Test Settings");
        testSettingsButton.addActionListener(e -> testSystemSettings());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        buttonPanel.add(saveSettingsButton);
        buttonPanel.add(testSettingsButton);

        panel.add(formPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        
        loadSystemSettings();

        return panel;
    }

    private void loadSystemSettings() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConnection.connect();
            String sql = "SELECT setting_key, setting_value FROM system_settings";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            Map<String, String> settings = new HashMap<>();
            while (rs.next()) {
                settings.put(rs.getString("setting_key"), rs.getString("setting_value"));
            }

            fareMultiplierField.setText(settings.getOrDefault("fare_multiplier", "1.0"));
            boolean maintenanceMode = Boolean.parseBoolean(settings.getOrDefault("maintenance_mode", "false"));
            maintenanceModeToggle.setSelected(maintenanceMode);
            maintenanceModeToggle.setText(maintenanceMode ? "Maintenance Mode (ON)" : "Maintenance Mode (OFF)");
            maintenanceModeToggle.setBackground(maintenanceMode ? new Color(204, 51, 51) : new Color(102, 102, 204));
            notificationTemplatesArea.setText(settings.getOrDefault("notification_templates", "Flight Delayed: Your flight {flightNumber} is now delayed.\nBooking Confirmed: Your booking {bookingID} is confirmed."));

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading system settings: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void saveSystemSettings() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            double fareMultiplier = Double.parseDouble(fareMultiplierField.getText().trim());
            boolean maintenanceMode = maintenanceModeToggle.isSelected();
            String notificationTemplates = notificationTemplatesArea.getText();

            conn = DatabaseConnection.connect();
            conn.setAutoCommit(false); 

            
            String sqlFare = "INSERT INTO system_settings (setting_key, setting_value) VALUES (?, ?) ON DUPLICATE KEY UPDATE setting_value = ?";
            pstmt = conn.prepareStatement(sqlFare);
            pstmt.setString(1, "fare_multiplier");
            pstmt.setString(2, String.valueOf(fareMultiplier));
            pstmt.setString(3, String.valueOf(fareMultiplier));
            pstmt.executeUpdate();
            pstmt.close();

            
            String sqlMaintenance = "INSERT INTO system_settings (setting_key, setting_value) VALUES (?, ?) ON DUPLICATE KEY UPDATE setting_value = ?";
            pstmt = conn.prepareStatement(sqlMaintenance);
            pstmt.setString(1, "maintenance_mode");
            pstmt.setString(2, String.valueOf(maintenanceMode));
            pstmt.setString(3, String.valueOf(maintenanceMode));
            pstmt.executeUpdate();
            pstmt.close();

            
            String sqlNotifications = "INSERT INTO system_settings (setting_key, setting_value) VALUES (?, ?) ON DUPLICATE KEY UPDATE setting_value = ?";
            pstmt = conn.prepareStatement(sqlNotifications);
            pstmt.setString(1, "notification_templates");
            pstmt.setString(2, notificationTemplates);
            pstmt.setString(3, notificationTemplates);
            pstmt.executeUpdate();
            pstmt.close();

            conn.commit(); 
            JOptionPane.showMessageDialog(this, "System settings saved successfully!", "Settings Saved", JOptionPane.INFORMATION_MESSAGE);
            logSystemAction(loggedInAdmin != null ? loggedInAdmin.getAdminId() : null, "SAVE_SETTINGS", "Saved system settings.");
            sendAdminNotification("System settings updated.", "System");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid value for Fare Multiplier. Please enter a number.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            JOptionPane.showMessageDialog(this, "Database error saving settings: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.setAutoCommit(true);
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void testSystemSettings() {
        JOptionPane.showMessageDialog(this, "Testing system settings... (Functionality not fully implemented)", "Test Settings", JOptionPane.INFORMATION_MESSAGE);
        logSystemAction(loggedInAdmin != null ? loggedInAdmin.getAdminId() : null, "TEST_SETTINGS", "Admin tested system settings.");
    }


    
    private JPanel createManageAirportsPanel() {
        JPanel panel = createContentPanel("Manage Airports"); 
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setOpaque(false);

        
        JPanel airportDetailsPanel = new JPanel(new GridBagLayout());
        airportDetailsPanel.setOpaque(false);
        airportDetailsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "Airport Details", TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial", Font.BOLD, 16), Color.WHITE));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        searchAirportCodeField = createStyledTextField();
        airportNameField = createStyledTextField();
        airportCityField = createStyledTextField();
        airportCountryField = createStyledTextField();
        airportTimeZoneField = createStyledTextField();
        airportTerminalsArea = new JTextArea(3, 20);
        airportTerminalsArea.setBackground(new Color(60, 60, 60));
        airportTerminalsArea.setForeground(Color.WHITE);
        airportTerminalsArea.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 1));
        JScrollPane terminalsScrollPane = new JScrollPane(airportTerminalsArea);
        terminalsScrollPane.getViewport().setBackground(new Color(60, 60, 60));

        airportFacilitiesArea = new JTextArea(3, 20);
        airportFacilitiesArea.setBackground(new Color(60, 60, 60));
        airportFacilitiesArea.setForeground(Color.WHITE);
        airportFacilitiesArea.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 1));
        JScrollPane facilitiesScrollPane = new JScrollPane(airportFacilitiesArea);
        facilitiesScrollPane.getViewport().setBackground(new Color(60, 60, 60));


        addFormField(airportDetailsPanel, createStyledLabel("Airport Code (ID):"), searchAirportCodeField, gbc, 0);
        JButton loadAirportButton = createStyledButton("Load Airport");
        loadAirportButton.addActionListener(e -> loadAirportDetails(searchAirportCodeField.getText().trim()));
        gbc.gridx = 2;
        gbc.gridy = 0;
        airportDetailsPanel.add(loadAirportButton, gbc);


        addFormField(airportDetailsPanel, createStyledLabel("Airport Name:"), airportNameField, gbc, 1);
        addFormField(airportDetailsPanel, createStyledLabel("City:"), airportCityField, gbc, 2);
        addFormField(airportDetailsPanel, createStyledLabel("Country:"), airportCountryField, gbc, 3);
        addFormField(airportDetailsPanel, createStyledLabel("Time Zone:"), airportTimeZoneField, gbc, 4);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        airportDetailsPanel.add(createStyledLabel("Terminals (comma-separated):"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        airportDetailsPanel.add(terminalsScrollPane, gbc);
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 1;
        airportDetailsPanel.add(createStyledLabel("Facilities (comma-separated):"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        airportDetailsPanel.add(facilitiesScrollPane, gbc);
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;


        JPanel airportButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        airportButtonPanel.setOpaque(false);
        addAirportButton = createStyledButton("Add Airport");
        addAirportButton.addActionListener(e -> addAirport());
        updateAirportButton = createStyledButton("Update Airport");
        updateAirportButton.addActionListener(e -> updateAirport());
        deleteAirportButton = createStyledButton("Delete Airport");
        deleteAirportButton.addActionListener(e -> deleteAirport());
        airportButtonPanel.add(addAirportButton);
        airportButtonPanel.add(updateAirportButton);
        airportButtonPanel.add(deleteAirportButton);

        contentPanel.add(airportDetailsPanel, BorderLayout.CENTER);
        contentPanel.add(airportButtonPanel, BorderLayout.SOUTH);

        panel.add(contentPanel, BorderLayout.CENTER);
        return panel;
    }

    private void loadAirportDetails(String airportCode) {
        if (airportCode.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter an Airport Code to load.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConnection.connect();
            String sql = "SELECT name, city, country, time_zone, terminals, facilities FROM airport WHERE airport_code = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, airportCode);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                airportNameField.setText(rs.getString("name"));
                airportCityField.setText(rs.getString("city"));
                airportCountryField.setText(rs.getString("country"));
                airportTimeZoneField.setText(rs.getString("time_zone"));

                
                String terminalsJson = rs.getString("terminals");
                String facilitiesJson = rs.getString("facilities");

                Type listType = new TypeToken<List<String>>() {}.getType();

                List<String> terminalsList = (terminalsJson != null && !terminalsJson.isEmpty()) ?
                                            gson.fromJson(terminalsJson, listType) : new ArrayList<>();
                List<String> facilitiesList = (facilitiesJson != null && !facilitiesJson.isEmpty()) ?
                                             gson.fromJson(facilitiesJson, listType) : new ArrayList<>();

                airportTerminalsArea.setText(String.join(", ", terminalsList)); 
                airportFacilitiesArea.setText(String.join(", ", facilitiesList)); 

            } else {
                JOptionPane.showMessageDialog(this, "Airport with code " + airportCode + " not found.", "Not Found", JOptionPane.INFORMATION_MESSAGE);
                clearAirportFields();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error loading airport: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } catch (Exception jsonEx) { 
            JOptionPane.showMessageDialog(this, "Error parsing airport details (terminals/facilities): " + jsonEx.getMessage(), "Data Error", JOptionPane.ERROR_MESSAGE);
            jsonEx.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void addAirport() {
        String airportCode = searchAirportCodeField.getText().trim();
        String name = airportNameField.getText().trim();
        String city = airportCityField.getText().trim();
        String country = airportCountryField.getText().trim();
        String timeZone = airportTimeZoneField.getText().trim();
        List<String> terminals = Arrays.asList(airportTerminalsArea.getText().split(",\\s*"));
        List<String> facilities = Arrays.asList(airportFacilitiesArea.getText().split(",\\s*"));

        if (airportCode.isEmpty() || name.isEmpty() || city.isEmpty() || country.isEmpty() || timeZone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all airport details.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConnection.connect();
            
            String checkSql = "SELECT COUNT(*) FROM airport WHERE airport_code = ?";
            pstmt = conn.prepareStatement(checkSql);
            pstmt.setString(1, airportCode);
            rs = pstmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                JOptionPane.showMessageDialog(this, "Airport with code " + airportCode + " already exists. Use 'Update Airport' instead.", "Duplicate Entry", JOptionPane.WARNING_MESSAGE);
                return;
            }
            rs.close();
            pstmt.close();

            String sql = "INSERT INTO airport (airport_code, name, city, country, time_zone, terminals, facilities) VALUES (?, ?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, airportCode);
            pstmt.setString(2, name);
            pstmt.setString(3, city);
            pstmt.setString(4, country);
            pstmt.setString(5, timeZone);
            pstmt.setString(6, gson.toJson(terminals)); 
            pstmt.setString(7, gson.toJson(facilities)); 

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Airport added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearAirportFields();
                populateAirportComboBoxes(departureAirportCodeComboBox, arrivalAirportCodeComboBox); 
                logSystemAction(loggedInAdmin != null ? loggedInAdmin.getAdminId() : null, "ADD_AIRPORT", "Added airport: " + airportCode + " - " + name);
                sendAdminNotification("New airport " + airportCode + " added.", "System");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add airport.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error adding airport: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void updateAirport() {
        String airportCode = searchAirportCodeField.getText().trim();
        String name = airportNameField.getText().trim();
        String city = airportCityField.getText().trim();
        String country = airportCountryField.getText().trim();
        String timeZone = airportTimeZoneField.getText().trim();
        List<String> terminals = Arrays.asList(airportTerminalsArea.getText().split(",\\s*"));
        List<String> facilities = Arrays.asList(airportFacilitiesArea.getText().split(",\\s*"));

        if (airportCode.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter an Airport Code to update.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (name.isEmpty() || city.isEmpty() || country.isEmpty() || timeZone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please load and fill all airport details before updating.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DatabaseConnection.connect();
            String sql = "UPDATE airport SET name=?, city=?, country=?, time_zone=?, terminals=?, facilities=? WHERE airport_code=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setString(2, city);
            pstmt.setString(3, country);
            pstmt.setString(4, timeZone);
            pstmt.setString(5, gson.toJson(terminals)); 
            pstmt.setString(6, gson.toJson(facilities)); 
            pstmt.setString(7, airportCode);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Airport updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                populateAirportComboBoxes(departureAirportCodeComboBox, arrivalAirportCodeComboBox); 
                logSystemAction(loggedInAdmin != null ? loggedInAdmin.getAdminId() : null, "UPDATE_AIRPORT", "Updated airport: " + airportCode);
                sendAdminNotification("Airport " + airportCode + " updated.", "System");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update airport. Airport code might not exist.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error updating airport: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void deleteAirport() {
        String airportCode = searchAirportCodeField.getText().trim();
        if (airportCode.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter an Airport Code to delete.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to DELETE airport " + airportCode + "? This will also affect related flights.", "Confirm Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            Connection conn = null;
            PreparedStatement pstmt = null;
            try {
                conn = DatabaseConnection.connect();
                
                
                String sql = "DELETE FROM airport WHERE airport_code = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, airportCode);

                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Airport " + airportCode + " deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    clearAirportFields();
                    populateAirportComboBoxes(departureAirportCodeComboBox, arrivalAirportCodeComboBox); 
                    logSystemAction(loggedInAdmin != null ? loggedInAdmin.getAdminId() : null, "DELETE_AIRPORT", "Deleted airport: " + airportCode);
                    sendAdminNotification("Airport " + airportCode + " deleted.", "System");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete airport. Airport code might not exist.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Database error deleting airport: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            } finally {
                try {
                    if (pstmt != null) pstmt.close();
                    if (conn != null) conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private void clearAirportFields() {
        searchAirportCodeField.setText("");
        airportNameField.setText("");
        airportCityField.setText("");
        airportCountryField.setText("");
        airportTimeZoneField.setText("");
        airportTerminalsArea.setText("");
        airportFacilitiesArea.setText("");
    }


    
    private JPanel createViewFeedbackPanel() {
        JPanel panel = createContentPanel("View Feedbacks and Ratings");
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setOpaque(false);

        
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        filterPanel.setOpaque(false);
        filterPanel.add(createStyledLabel("Filter by Flight ID:"));
        feedbackFlightFilterField = createStyledTextField();
        filterPanel.add(feedbackFlightFilterField);
        filterPanel.add(createStyledLabel("Filter by Customer ID:"));
        feedbackCustomerFilterField = createStyledTextField();
        filterPanel.add(feedbackCustomerFilterField);
        JButton filterButton = createStyledButton("Apply Filters");
        filterButton.addActionListener(e -> viewFeedbacks());
        filterPanel.add(filterButton);
        contentPanel.add(filterPanel, BorderLayout.NORTH);

        
        feedbackDisplayArea = new JTextArea();
        feedbackDisplayArea.setEditable(false);
        feedbackDisplayArea.setBackground(new Color(60, 60, 60));
        feedbackDisplayArea.setForeground(Color.WHITE);
        feedbackDisplayArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(feedbackDisplayArea);
        scrollPane.getViewport().setBackground(new Color(60, 60, 60));
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        
        JPanel actionButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        actionButtonPanel.setOpaque(false);

        flagAbusiveButton = createStyledButton("Flag Abusive Feedback (Simulated)"); 
        flagAbusiveButton.addActionListener(e -> flagAbusiveFeedback());
        actionButtonPanel.add(flagAbusiveButton);

        exportFeedbacksButton = createStyledButton("Export Feedbacks"); 
        exportFeedbacksButton.addActionListener(e -> exportFeedbacksAction());
        actionButtonPanel.add(exportFeedbacksButton);

        contentPanel.add(actionButtonPanel, BorderLayout.SOUTH);
        panel.add(contentPanel, BorderLayout.CENTER);

        return panel;
    }

    private void viewFeedbacks() {
        feedbackDisplayArea.setText("");
        StringBuilder feedbacks = new StringBuilder();
        feedbacks.append("--- Customer Feedbacks & Ratings ---\n\n");

        String flightFilter = feedbackFlightFilterField.getText().trim();
        String customerFilter = feedbackCustomerFilterField.getText().trim();

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConnection.connect();
            String sql = "SELECT feedback_id, customer_id, flight_id, rating, comment, created_at FROM feedback WHERE 1=1 "; 
            if (!flightFilter.isEmpty()) {
                sql += "AND flight_id LIKE ? ";
            }
            if (!customerFilter.isEmpty()) {
                sql += "AND customer_id LIKE ? ";
            }
            sql += "ORDER BY created_at DESC LIMIT 200"; 

            pstmt = conn.prepareStatement(sql);
            int paramIndex = 1;
            if (!flightFilter.isEmpty()) {
                pstmt.setString(paramIndex++, "%" + flightFilter + "%");
            }
            if (!customerFilter.isEmpty()) {
                pstmt.setString(paramIndex++, "%" + customerFilter + "%");
            }
            rs = pstmt.executeQuery();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            while (rs.next()) {
                feedbacks.append("Feedback ID: ").append(rs.getString("feedback_id")).append("\n");
                feedbacks.append("Customer ID: ").append(rs.getString("customer_id")).append("\n");
                feedbacks.append("Flight ID: ").append(rs.getString("flight_id")).append("\n");
                feedbacks.append("Rating: ").append(rs.getInt("rating")).append(" stars\n");
                feedbacks.append("Comment: ").append(rs.getString("comment")).append("\n");
                feedbacks.append("Created At: ").append(sdf.format(rs.getTimestamp("created_at"))).append("\n"); 
                feedbacks.append("-----------------------------------\n");
            }

            if (feedbacks.toString().equals("--- Customer Feedbacks & Ratings ---\n\n")) {
                feedbacks.append("No feedbacks found matching the criteria.");
            }

        } catch (SQLException ex) {
            feedbacks.append("Error fetching feedbacks: ").append(ex.getMessage()).append("\n");
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        feedbackDisplayArea.setText(feedbacks.toString());
    }

    private void flagAbusiveFeedback() {
        
        
        JOptionPane.showMessageDialog(this, "Flagging abusive feedback... (This is a simulated action as 'is_abusive' column is not in the database schema)", "Flag Feedback", JOptionPane.INFORMATION_MESSAGE);
        System.out.println("Simulating flagging abusive feedback.");
        logSystemAction(loggedInAdmin != null ? loggedInAdmin.getAdminId() : null, "FLAG_FEEDBACK", "Simulated flagging of abusive feedback.");
    }

    
    private void exportFeedbacksAction() {
        String feedbackContent = feedbackDisplayArea.getText();
        if (feedbackContent.isEmpty() || feedbackContent.equals("--- Customer Feedbacks & Ratings ---\n\n")) {
            JOptionPane.showMessageDialog(this, "No feedbacks displayed to export.", "Export Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String fileName = "feedbacks_" + System.currentTimeMillis() + ".txt";
        saveReportToFileManager(feedbackContent, fileName, "feedback");
    }


    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 16));
        label.setForeground(new Color(180, 180, 180));
        return label;
    }
    
    private JPanel createDeepfakeManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(45, 45, 45));

        JLabel titleLabel = createTitleLabel("Deepfake Management"); 
        panel.add(titleLabel, BorderLayout.NORTH);

        
        String[] columnNames = {"User ID", "Username", "Detection Score", "Detection Date", "Is Verified"};
        deepfakeUsersTableModel = new DefaultTableModel(columnNames, 0);
        deepfakeUsersTable = new JTable(deepfakeUsersTableModel);
        deepfakeUsersTable.setBackground(new Color(60, 60, 60));
        deepfakeUsersTable.setForeground(Color.WHITE);
        deepfakeUsersTable.setSelectionBackground(new Color(80, 80, 80));
        deepfakeUsersTable.setSelectionForeground(Color.WHITE);
        deepfakeUsersTable.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(deepfakeUsersTable);
        scrollPane.getViewport().setBackground(new Color(60, 60, 60));
        scrollPane.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(70, 70, 70)), "Detected Deepfake Users",
            TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial", Font.BOLD, 14), Color.WHITE));
        panel.add(scrollPane, BorderLayout.CENTER);

        
        activityDisplayArea = new JTextArea(5, 40);
        activityDisplayArea.setEditable(false);
        activityDisplayArea.setBackground(new Color(30, 30, 30));
        activityDisplayArea.setForeground(Color.CYAN);
        activityDisplayArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        activityDisplayArea.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(70, 70, 70)), "Suspicious Activities Log",
            TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial", Font.BOLD, 14), Color.WHITE));
        JScrollPane activityScrollPane = new JScrollPane(activityDisplayArea);
        panel.add(activityScrollPane, BorderLayout.EAST);

        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(new Color(45, 45, 45));

        JButton verifyButton = createStyledButton("Verify User"); 
        verifyButton.addActionListener(e -> verifySelectedDeepfakeUser());
        buttonPanel.add(verifyButton);

        JButton restrictButton = createStyledButton("Restrict Account");
        restrictButton.addActionListener(e -> restrictSelectedDeepfakeUser());
        buttonPanel.add(restrictButton);

        JButton viewActivitiesButton = createStyledButton("View Activities");
        viewActivitiesButton.addActionListener(e -> viewSuspiciousActivitiesForSelectedUser());
        buttonPanel.add(viewActivitiesButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void loadDeepFakeUsers() {
        deepfakeUsersTableModel.setRowCount(0); 
        String sql = "SELECT df.original_user_id, u.username, df.detection_score, df.detection_date, df.is_verified " +
                     "FROM deepfakeuser df JOIN users u ON df.original_user_id = u.user_id";

        try (Connection conn = DatabaseConnection.connect(); 
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String userId = rs.getString("original_user_id");
                String username = rs.getString("username");
                double detectionScore = rs.getDouble("detection_score");
                Timestamp detectionTimestamp = rs.getTimestamp("detection_date");
                Date detectionDate = (detectionTimestamp != null) ? new Date(detectionTimestamp.getTime()) : null;
                boolean isVerified = rs.getBoolean("is_verified");

                deepfakeUsersTableModel.addRow(new Object[]{userId, username, detectionScore, detectionDate, isVerified});
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading deepfake users: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }


    private User getUserById(String userId) {
        
        String sql = "SELECT user_id, username, email, password_hash, role, phone_number, " +
                     "address, registration_date, last_login, is_blacklisted " +
                     "FROM users WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                SimpleUser user = new SimpleUser(); 
                user.setUserID(rs.getString("user_id"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setPasswordHash(rs.getString("password_hash"));
                user.setRole(rs.getString("role"));
                user.setPhoneNumber(rs.getInt("phone_number"));
                user.setAddress(rs.getString("address"));
                user.setRegestrationDate(rs.getTimestamp("registration_date"));
                user.setLastLogin(rs.getTimestamp("last_login"));
                user.setIsBlacklisted(rs.getBoolean("is_blacklisted"));


                return user;
            }
        } catch (SQLException ex) {
            System.err.println("Error fetching user: " + ex.getMessage());
            ex.printStackTrace();
        }
        return null;
    }


    private DeepFake.DeepFakeUser getDeepFakeUserFromDatabase(User user) {
        if (user == null) return null;

        String sql = "SELECT detection_score, detection_date, is_verified FROM deepfakeuser WHERE original_user_id = ?";
        try (Connection conn = DatabaseConnection.connect(); 
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getUserID());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                double detectionScore = rs.getDouble("detection_score");
                Date detectionDate = rs.getTimestamp("detection_date");
                boolean isVerified = rs.getBoolean("is_verified");
                
                
                return new DeepFake().new DeepFakeUser(user, detectionScore, detectionDate, new ArrayList<>(), isVerified);
            }
        } catch (SQLException ex) {
            System.err.println("Error fetching deepfake user details: " + ex.getMessage());
            ex.printStackTrace();
        }
        return null;
    }


    private void verifySelectedDeepfakeUser() {
        int selectedRow = deepfakeUsersTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a deepfake user from the table.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String userId = (String) deepfakeUsersTableModel.getValueAt(selectedRow, 0);
        User originalUser = getUserById(userId);

        if (originalUser == null) {
            JOptionPane.showMessageDialog(this, "Could not find user details for ID: " + userId, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        DeepFake.DeepFakeUser deepFakeUser = getDeepFakeUserFromDatabase(originalUser);

        if (deepFakeUser == null) {
            JOptionPane.showMessageDialog(this, "No deepfake record found for user: " + userId, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        
        deepFakeUser.setVerified(true); 
        System.out.println("DeepFakeUser object's verified status set to true in memory.");

        
        String sql = "UPDATE deepfakeuser SET is_verified = TRUE WHERE original_user_id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "User " + userId + " marked as verified in deepfake records.", "Success", JOptionPane.INFORMATION_MESSAGE);
            logSystemAction(loggedInAdmin != null ? loggedInAdmin.getAdminId() : null, "VERIFY_DEEPFAKE_USER", "Verified deepfake status for user: " + userId);
            sendAdminNotification("Deepfake user " + userId + " verified.", "System");
            loadDeepFakeUsers(); 
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error updating deepfake verification status in database: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
        
        
    }


    private void restrictSelectedDeepfakeUser() {
        int selectedRow = deepfakeUsersTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a deepfake user from the table.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String userId = (String) deepfakeUsersTableModel.getValueAt(selectedRow, 0);
        User originalUser = getUserById(userId);

        if (originalUser == null) {
            JOptionPane.showMessageDialog(this, "Could not find user details for ID: " + userId, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        DeepFake.DeepFakeUser deepFakeUser = getDeepFakeUserFromDatabase(originalUser);

        if (deepFakeUser == null) {
            JOptionPane.showMessageDialog(this, "No deepfake record found for user: " + userId, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean restricted = deepFakeUser.restrictAccount();

        if (restricted) {
            String sql = "UPDATE users SET is_blacklisted = TRUE WHERE user_id = ?";
            try (Connection conn = DatabaseConnection.connect(); 
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, userId);
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Account for user " + userId + " has been restricted (blacklisted).", "Success", JOptionPane.INFORMATION_MESSAGE);
                logSystemAction(loggedInAdmin != null ? loggedInAdmin.getAdminId() : null, "RESTRICT_DEEPFAKE_ACCOUNT", "Restricted account for deepfake user: " + userId);
                sendAdminNotification("Deepfake user " + userId + " account restricted.", "System");
                loadDeepFakeUsers();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error restricting user account: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Failed to restrict account for user " + userId + ".", "Restriction Failed", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void viewSuspiciousActivitiesForSelectedUser() {
        int selectedRow = deepfakeUsersTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a deepfake user from the table.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String userId = (String) deepfakeUsersTableModel.getValueAt(selectedRow, 0);
        activityDisplayArea.setText("Loading activities for " + userId + "...\n");

        String sql = "SELECT activity_detail, activity_time FROM deepfakesuspiciousactivity WHERE user_id = ? ORDER BY activity_time DESC";
        StringBuilder activities = new StringBuilder();

        try (Connection conn = DatabaseConnection.connect(); 
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            ResultSet rs = pstmt.executeQuery();

            if (!rs.isBeforeFirst()) {
                activities.append("No suspicious activities logged for this user.\n");
            } else {
                activities.append("Suspicious Activities for User ").append(userId).append(":\n\n");
                while (rs.next()) {
                    String detail = rs.getString("activity_detail");
                    Timestamp time = rs.getTimestamp("activity_time");
                    activities.append("- ").append(time.toString()).append(": ").append(detail).append("\n");
                }
            }
        } catch (SQLException ex) {
            activities.append("Error fetching suspicious activities: ").append(ex.getMessage()).append("\n");
            ex.printStackTrace();
        }
        activityDisplayArea.setText(activities.toString());
    }

    
    private static class SimpleUser extends User {
        
        
        public SimpleUser() {
            super("temp_id", "temp_username", "temp@example.com", "temp_password_hash", "temp_role", 0);
        }
        @Override
            public void accessDashBoared(){};

    }

    
    private JLabel createTitleLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER); 
        label.setFont(new Font("Arial", Font.BOLD, 24)); 
        label.setForeground(new Color(255, 200, 0)); 
        label.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0)); 
        return label;
    }

    
    private JPanel createNotificationPanel() {
        JPanel panel = createContentPanel("Notifications");
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setOpaque(false);

        notificationDisplayArea = new JTextArea();
        notificationDisplayArea.setEditable(false);
        notificationDisplayArea.setBackground(new Color(60, 60, 60));
        notificationDisplayArea.setForeground(Color.WHITE);
        notificationDisplayArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(notificationDisplayArea);
        scrollPane.getViewport().setBackground(new Color(60, 60, 60));
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setOpaque(false);

        markNotificationReadButton = createStyledButton("Mark Selected as Read");
        markNotificationReadButton.addActionListener(e -> markSelectedNotificationAsRead());
        buttonPanel.add(markNotificationReadButton);

        clearAllNotificationsButton = createStyledButton("Clear All Read Notifications");
        clearAllNotificationsButton.addActionListener(e -> clearAllReadNotifications());
        buttonPanel.add(clearAllNotificationsButton);

        contentPanel.add(buttonPanel, BorderLayout.SOUTH);
        panel.add(contentPanel, BorderLayout.CENTER);

        return panel;
    }

    
    private void loadNotificationsForAdmin() {
        notificationDisplayArea.setText("");
        StringBuilder notificationsText = new StringBuilder();
        notificationsText.append("--- Your Notifications ---\n\n");

        if (loggedInAdmin == null) {
            notificationsText.append("Admin not logged in. Cannot fetch notifications.");
            notificationDisplayArea.setText(notificationsText.toString());
            return;
        }

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConnection.connect();
            String sql = "SELECT notification_id, message, notification_time, is_read, sender_role FROM notifications WHERE user_id = ? ORDER BY notification_time DESC";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, loggedInAdmin.getUserID());
            rs = pstmt.executeQuery();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            while (rs.next()) {
                String notificationId = rs.getString("notification_id");
                String message = rs.getString("message");
                Timestamp time = rs.getTimestamp("notification_time");
                boolean isRead = rs.getBoolean("is_read");
                String senderRole = rs.getString("sender_role");

                notificationsText.append("ID: ").append(notificationId).append("\n");
                notificationsText.append("Time: ").append(sdf.format(time)).append("\n");
                notificationsText.append("Sender: ").append(senderRole).append("\n");
                notificationsText.append("Status: ").append(isRead ? "Read" : "Unread").append("\n");
                notificationsText.append("Message: ").append(message).append("\n");
                notificationsText.append("-----------------------------------\n");
            }

            if (notificationsText.toString().equals("--- Your Notifications ---\n\n")) {
                notificationsText.append("No notifications found.");
            }

        } catch (SQLException ex) {
            notificationsText.append("Error fetching notifications: ").append(ex.getMessage()).append("\n");
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        notificationDisplayArea.setText(notificationsText.toString());
    }

    
    private void markSelectedNotificationAsRead() {
        String selectedText = notificationDisplayArea.getSelectedText();
        if (selectedText == null || selectedText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a notification to mark as read.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        
        String notificationId = null;
        for (String line : selectedText.split("\n")) {
            if (line.startsWith("ID: ")) {
                notificationId = line.substring(4).trim();
                break;
            }
        }

        if (notificationId == null) {
            JOptionPane.showMessageDialog(this, "Could not find Notification ID in selected text.", "Parse Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DatabaseConnection.connect();
            String sql = "UPDATE notifications SET is_read = 1 WHERE notification_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, notificationId);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Notification " + notificationId + " marked as read.", "Success", JOptionPane.INFORMATION_MESSAGE);
                logSystemAction(loggedInAdmin != null ? loggedInAdmin.getAdminId() : null, "MARK_NOTIFICATION_READ", "Marked notification " + notificationId + " as read.");
                loadNotificationsForAdmin(); 
            } else {
                JOptionPane.showMessageDialog(this, "Failed to mark notification as read.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error marking notification as read: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    
    private void clearAllReadNotifications() {
        if (loggedInAdmin == null) {
            JOptionPane.showMessageDialog(this, "Admin not logged in. Cannot clear notifications.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to clear all READ notifications?", "Confirm Clear", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            Connection conn = null;
            PreparedStatement pstmt = null;
            try {
                conn = DatabaseConnection.connect();
                String sql = "DELETE FROM notifications WHERE user_id = ? AND is_read = 1";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, loggedInAdmin.getUserID());
                int rowsAffected = pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, rowsAffected + " read notifications cleared.", "Success", JOptionPane.INFORMATION_MESSAGE);
                logSystemAction(loggedInAdmin != null ? loggedInAdmin.getAdminId() : null, "CLEAR_READ_NOTIFICATIONS", "Cleared " + rowsAffected + " read notifications.");
                loadNotificationsForAdmin(); 
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Database error clearing notifications: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            } finally {
                try {
                    if (pstmt != null) pstmt.close();
                    if (conn != null) conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    
    private void sendAdminNotification(String message, String senderRole) {
        if (loggedInAdmin == null || loggedInAdmin.getUserID() == null) {
            System.err.println("Cannot send admin notification: loggedInAdmin or its UserID is null.");
            return;
        }

        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DatabaseConnection.connect();
            String sql = "INSERT INTO notifications (notification_id, user_id, message, notification_time, is_read, sender_role) VALUES (?, ?, ?, NOW(), ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "NOTIF" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
            pstmt.setString(2, loggedInAdmin.getUserID());
            pstmt.setString(3, message);
            pstmt.setBoolean(4, false); 
            pstmt.setString(5, senderRole);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("Error sending admin notification: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    private void loadAirportCodes() {
    airportCodes = new ArrayList<>(); 
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    try {
        conn = DatabaseConnection.connect();
        if (conn != null) {
            String sql = "SELECT airport_code FROM Airport"; 
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                airportCodes.add(rs.getString("airport_code"));
            }
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error loading airport codes: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
    } finally {
        try {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        } catch (SQLException closeEx) {
            closeEx.printStackTrace();
        }
    }
}
    
    private void loadFlights() {
    flightsTableModel.setRowCount(0); 
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    try {
        conn = DatabaseConnection.connect();
        if (conn != null) {
            String sql = "SELECT f.flight_id, f.flight_num, da.name AS departure_airport_name, aa.name AS arrival_airport_name, " +
                         "f.departure_time, f.arrival_time, f.aircraft_type, f.total_seats, f.available_seats, " +
                         "f.fare_economy, f.fare_business, f.fare_first_class, f.status " +
                         "FROM flight f " +
                         "JOIN airport da ON f.departure_airport_id = da.airport_code " +
                         "JOIN airport aa ON f.arrival_airport_id = aa.airport_code";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            SimpleDateFormat displayDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            while (rs.next()) {
                String flightID = rs.getString("flight_id");
                String flightNumber = rs.getString("flight_num");
                String departureAirportName = rs.getString("departure_airport_name");
                String arrivalAirportName = rs.getString("arrival_airport_name");
                String departureTime = displayDateFormat.format(rs.getTimestamp("departure_time"));
                String arrivalTime = displayDateFormat.format(rs.getTimestamp("arrival_time"));
                String aircraftType = rs.getString("aircraft_type");
                int totalSeats = rs.getInt("total_seats");
                int availableSeats = rs.getInt("available_seats");
                double economyFare = rs.getDouble("fare_economy");
                double businessFare = rs.getDouble("fare_business");
                double firstClassFare = rs.getDouble("fare_first_class");
                String status = rs.getString("status");

                
                List<String> crewMembersList = new ArrayList<>();
                String crewSql = "SELECT crew_member_id FROM flight_flightcrew WHERE flight_id = ?";
                try (PreparedStatement crewPstmt = conn.prepareStatement(crewSql)) {
                    crewPstmt.setString(1, flightID);
                    try (ResultSet crewRs = crewPstmt.executeQuery()) {
                        while (crewRs.next()) {
                            crewMembersList.add(crewRs.getString("crew_member_id"));
                        }
                    }
                }
                String crewMembersDisplay = crewMembersList.isEmpty() ? "N/A" : String.join(", ", crewMembersList);

                flightsTableModel.addRow(new Object[]{
                    flightID, flightNumber, departureAirportName, arrivalAirportName,
                    departureTime, arrivalTime, aircraftType, totalSeats, availableSeats,
                    String.format("%.2f", economyFare), String.format("%.2f", businessFare), String.format("%.2f", firstClassFare),
                    status, crewMembersDisplay
                });
            }
        }
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Error loading flights: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
    } finally {
        try {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
}
