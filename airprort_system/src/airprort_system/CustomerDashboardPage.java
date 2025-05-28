package airprort_system;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.geom.RoundRectangle2D;
import java.sql.*;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.Vector;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class CustomerDashboardPage extends JPanel {
    private CardLayout cardLayout;
    private JPanel parentPanel;
    private Customer loggedInCustomer;

    private JLabel welcomeLabel;
    private JPanel contentPanel; 

    
    private JTextField searchFromField;
    private JTextField searchToField;
    private JFormattedTextField searchDepartureDateField;
    private JFormattedTextField searchReturnDateField; 
    private JComboBox<String> searchClassComboBox;
    private JSpinner searchPassengersSpinner;
    private JTable flightsTable;
    private DefaultTableModel flightsTableModel;
    private JButton bookFlightButton;

    
    private JLabel bookingFlightDetailsLabel;
    private JTextField passengerFirstNameField;
    private JTextField passengerLastNameField;
    private JFormattedTextField passengerDOBField;
    private JTextField passengerPassportField;
    private JTextField passengerNationalityField;
    private JComboBox<String> passengerGenderComboBox;
    private JCheckBox specialAssistanceCheckBox;
    private JTextField mealPreferenceField;
    private JTextField seatNumberField;
    private JComboBox<String> paymentMethodComboBox; 
    private JTextArea specialRequestsArea;
    private Flight selectedFlightForBooking;

    
    private int currentAnomalyCount = 0;
private StringBuilder currentAnalysisResult = new StringBuilder();
private final int DEEPFAKE_THRESHOLD_ANOMALY_COUNT = 5;


    
    private JTable myBookingsTable;
    private DefaultTableModel myBookingsTableModel;

    
    private JTextArea notificationArea;

    
    private JTextField profileUsernameField;
    private JTextField profileEmailField;
    private JPasswordField profilePasswordField;
    private JPasswordField profileConfirmPasswordField;
    private JTextField profilePhoneNumberField;
    private JTextField profileAddressField;
    private JTextField profilePassportNumberField;

    
    private JTable paymentMethodsTable;
    private DefaultTableModel paymentMethodsTableModel;
    private JTextField cardNumberField;
    private JTextField cardHolderNameField;
    private JFormattedTextField cardExpiryDateField;
    private JTextField cardCvvField;
    


    
    private JTextField refundBookingIdField;
    private JTextArea refundReasonArea;
    private JTextField refundMethodField;
    private JLabel refundConfirmationLabel;

    
    private JTextField rateFlightNumberField;
    private JSpinner rateStarsSpinner;
    private JTextArea rateReviewArea;
    private JComboBox<String> pastBookedFlightsComboBox;

private JTable bookingHistoryTable;
private DefaultTableModel bookingHistoryTableModel;



    private JTextField refundReasonField;

    public CustomerDashboardPage(CardLayout cardLayout, JPanel parentPanel) {
        this.cardLayout = cardLayout;
        this.parentPanel = parentPanel;

        setLayout(new BorderLayout());
        setName("CUSTOMER_DASHBOARD");
        setBackground(new Color(30, 40, 60));

        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(40, 50, 70));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        welcomeLabel = new JLabel("Welcome, Customer (Placeholder)");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setForeground(Color.WHITE);
        headerPanel.add(welcomeLabel, BorderLayout.WEST);

        JButton logoutButton = createStyledButton("Logout", new Color(204, 51, 51));
        logoutButton.addActionListener(e -> {
            
            loggedInCustomer = null; 
            cardLayout.show(parentPanel, "LOGIN"); 
        });
        headerPanel.add(logoutButton, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        
        JPanel mainContentPanel = new JPanel(new BorderLayout());
        mainContentPanel.setBackground(new Color(30, 40, 60));

        
        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(new Color(50, 60, 80));
        sidebarPanel.setBorder(new EmptyBorder(20, 10, 20, 10));
        sidebarPanel.setPreferredSize(new Dimension(200, getHeight()));

        JButton searchFlightBtn = createSidebarButton("Search Flights");
        searchFlightBtn.addActionListener(e -> showPanel("SEARCH_FLIGHTS"));
        JButton myBookingsBtn = createSidebarButton("My Bookings");
        myBookingsBtn.addActionListener(e -> showPanel("MY_BOOKINGS"));
        JButton bookingHistoryBtn = createSidebarButton("Booking History");
        bookingHistoryBtn.addActionListener(e -> showPanel("BOOKING_HISTORY"));
        JButton updateProfileBtn = createSidebarButton("Update Profile");
        updateProfileBtn.addActionListener(e -> showPanel("UPDATE_PROFILE"));
        JButton managePaymentBtn = createSidebarButton("Manage Payment");
        managePaymentBtn.addActionListener(e -> showPanel("MANAGE_PAYMENT"));
        JButton refundRequestBtn = createSidebarButton("Refund Request");
        refundRequestBtn.addActionListener(e -> showPanel("REFUND_REQUEST"));
        JButton rateFlightBtn = createSidebarButton("Rate Flight");
        rateFlightBtn.addActionListener(e -> showPanel("RATE_FLIGHT"));
        JButton notificationsBtn = createSidebarButton("Notifications");
        notificationsBtn.addActionListener(e -> showPanel("NOTIFICATIONS"));


        sidebarPanel.add(Box.createVerticalStrut(10));
        sidebarPanel.add(searchFlightBtn);
        sidebarPanel.add(Box.createVerticalStrut(10));
        sidebarPanel.add(myBookingsBtn);
        sidebarPanel.add(Box.createVerticalStrut(10));
        sidebarPanel.add(bookingHistoryBtn);
        sidebarPanel.add(Box.createVerticalStrut(10));
        sidebarPanel.add(updateProfileBtn);
        sidebarPanel.add(Box.createVerticalStrut(10));
        sidebarPanel.add(managePaymentBtn);
        sidebarPanel.add(Box.createVerticalStrut(10));
        sidebarPanel.add(refundRequestBtn);
        sidebarPanel.add(Box.createVerticalStrut(10));
        sidebarPanel.add(rateFlightBtn);
        sidebarPanel.add(Box.createVerticalStrut(10));
        sidebarPanel.add(notificationsBtn);
        sidebarPanel.add(Box.createVerticalGlue()); 


        mainContentPanel.add(sidebarPanel, BorderLayout.WEST);

        
        contentPanel = new JPanel(new CardLayout());
        contentPanel.setBackground(new Color(30, 40, 60));
        mainContentPanel.add(contentPanel, BorderLayout.CENTER);

        add(mainContentPanel, BorderLayout.CENTER);

        
        initSearchFlightsPanel();
        initBookingPagePanel();
        initMyBookingsPanel();
        initBookingHistoryPanel();
        initUpdateProfilePanel();
        initManagePaymentPanel();
        initRefundRequestPanel();
        initRateFlightPanel();
        initNotificationsPanel();

        
        showPanel("SEARCH_FLIGHTS");
    }

    /**
     * Sets the logged-in customer for the dashboard and refreshes UI elements.
     * Also checks for blacklist status and adds a notification if blacklisted.
     * @param customer The Customer object representing the logged-in user.
     */
    public void setLoggedInCustomer(Customer customer) {
        this.loggedInCustomer = customer;
        if (loggedInCustomer != null) {
            
            refreshLoggedInCustomerDetails();
            welcomeLabel.setText("Welcome, " + loggedInCustomer.getUsername() + "!");
            
            if (loggedInCustomer.isBlacklisted()) {
                this.addNotification("IMPORTANT: Your account has been blacklisted. Reason: " + loggedInCustomer.getBlacklistReason());
                JOptionPane.showMessageDialog(this, "Your account has been blacklisted. Reason: " + loggedInCustomer.getBlacklistReason(), "Account Blacklisted", JOptionPane.WARNING_MESSAGE);
            }
            if (isUserFlaggedAsDeepfake(loggedInCustomer.getUserID())) {
    this.addNotification("WARNING: Your account is flagged for suspicious activity and under review.");
    JOptionPane.showMessageDialog(this,
        "Your account is flagged for suspicious activity and is under review.",
        "Deepfake Alert",
        JOptionPane.WARNING_MESSAGE);
}

            
            populateProfileData();
            loadMyBookings();
            loadPaymentMethods(); 
            loadPastBookedFlights();
            loadNotifications(); 
        } else {
            welcomeLabel.setText("Welcome, Customer (Placeholder)");
        }
    }

    /**
     * Displays the specified panel within the main content area.
     * Also triggers data loading for the shown panel.
     * @param panelName The name of the panel to show.
     */
    private void showPanel(String panelName) {
        ((CardLayout) contentPanel.getLayout()).show(contentPanel, panelName);
        
        switch (panelName) {
            case "MY_BOOKINGS":
                loadMyBookings();
                break;
            case "BOOKING_HISTORY":
                loadBookingHistory("All"); 
                break;
            case "UPDATE_PROFILE":
                populateProfileData();
                break;
            case "MANAGE_PAYMENT":
                loadPaymentMethods(); 
                break;
            case "RATE_FLIGHT":
                loadPastBookedFlights();
                break;
            case "NOTIFICATIONS":
                loadNotifications();
                break;
            case "BOOK_FLIGHT":
                populatePaymentMethodComboBox(); 
                break;
        }
    }

    

    /**
     * Creates a styled JButton with a specified text and base color.
     * Includes hover effects.
     * @param text The text for the button.
     * @param baseColor The base background color of the button.
     * @return A styled JButton.
     */
    private JButton createStyledButton(String text, Color baseColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setPreferredSize(new Dimension(180, 40));
        button.setForeground(Color.WHITE);
        button.setBackground(baseColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 255, 255, 120), 1),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
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

    /**
     * Creates a styled JButton for the sidebar navigation.
     * Includes hover effects.
     * @param text The text for the button.
     * @return A styled sidebar JButton.
     */
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

    /**
     * Creates a common content panel with a title for various sections.
     * @param title The title of the panel.
     * @return A JPanel with a title and basic styling.
     */
    private JPanel createContentPanel(String title) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(30, 40, 60));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(new Color(200, 200, 200));
        panel.add(titleLabel, BorderLayout.NORTH);

        return panel;
    }

    /**
     * Creates a formatted text field for date input (YYYY-MM-DD).
     * @return A JFormattedTextField configured for date input.
     */
    private JFormattedTextField createFormattedDateField() {
        try {
            MaskFormatter dateFormatter = new MaskFormatter("####-##-##"); 
            dateFormatter.setPlaceholderCharacter('_');
            JFormattedTextField field = new JFormattedTextField(dateFormatter);
            field.setFont(new Font("Arial", Font.PLAIN, 14));
            field.setPreferredSize(new Dimension(150, 30));
            field.setBackground(new Color(60, 70, 90)); 
            field.setForeground(Color.WHITE);
            field.setCaretColor(Color.WHITE);
            return field;
        } catch (ParseException e) {
            e.printStackTrace();
            return new JFormattedTextField();
        }
    }

    /**
     * Creates a styled JLabel with white foreground and Arial font.
     * @param text The text for the label.
     * @return A styled JLabel.
     */
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        return label;
    }

    /**
     * Applies common styling to JTables.
     * @param table The JTable to style.
     */
    private void styleTable(JTable table) {
        table.setBackground(new Color(60, 70, 90));
        table.setForeground(Color.WHITE);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setSelectionBackground(new Color(51, 153, 204));
        table.setSelectionForeground(Color.WHITE);
        table.getTableHeader().setBackground(new Color(50, 60, 80));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(new Color(60, 70, 90)); 
    }


    

    /**
     * Initializes the panel for searching flights.
     */
    private void initSearchFlightsPanel() {
        JPanel panel = createContentPanel("Search Flights");
        panel.setName("SEARCH_FLIGHTS");

        JPanel searchFormPanel = new JPanel(new GridBagLayout());
        searchFormPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        
        gbc.gridx = 0;
        gbc.gridy = 0;
        searchFormPanel.add(createLabel("From:"), gbc);
        gbc.gridx = 1;
        searchFromField = new JTextField(10);
        searchFromField.setBackground(new Color(60, 70, 90));
        searchFromField.setForeground(Color.WHITE);
        searchFromField.setCaretColor(Color.WHITE);
        searchFormPanel.add(searchFromField, gbc);

        
        gbc.gridx = 2;
        searchFormPanel.add(createLabel("To:"), gbc);
        gbc.gridx = 3;
        searchToField = new JTextField(10);
        searchToField.setBackground(new Color(60, 70, 90));
        searchToField.setForeground(Color.WHITE);
        searchToField.setCaretColor(Color.WHITE);
        searchFormPanel.add(searchToField, gbc);

        
        gbc.gridx = 0;
        gbc.gridy = 1;
        searchFormPanel.add(createLabel("Departure Date (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        searchDepartureDateField = createFormattedDateField();
        searchFormPanel.add(searchDepartureDateField, gbc);

        
        gbc.gridx = 2;
        searchFormPanel.add(createLabel("Return Date (Optional):"), gbc);
        gbc.gridx = 3;
        searchReturnDateField = createFormattedDateField();
        searchFormPanel.add(searchReturnDateField, gbc);

        
        gbc.gridx = 0;
        gbc.gridy = 2;
        searchFormPanel.add(createLabel("Class:"), gbc);
        gbc.gridx = 1;
        searchClassComboBox = new JComboBox<>(new String[]{"Economy", "Business", "First Class"});
        searchClassComboBox.setBackground(new Color(60, 70, 90));
        searchClassComboBox.setForeground(Color.WHITE);
        searchFormPanel.add(searchClassComboBox, gbc);

        
        gbc.gridx = 2;
        gbc.gridy = 2;
        searchFormPanel.add(createLabel("Passengers:"), gbc);
        gbc.gridx = 3;
        searchPassengersSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        ((JSpinner.DefaultEditor) searchPassengersSpinner.getEditor()).getTextField().setBackground(new Color(60, 70, 90));
        ((JSpinner.DefaultEditor) searchPassengersSpinner.getEditor()).getTextField().setForeground(Color.WHITE);
        searchPassengersSpinner.setBackground(new Color(60, 70, 90));
        searchPassengersSpinner.setForeground(Color.WHITE);
        searchFormPanel.add(searchPassengersSpinner, gbc);

        
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 4;
        JButton searchButton = createStyledButton("Search Flights", new Color(51, 153, 204));
        searchButton.addActionListener(e -> searchFlights());
        searchFormPanel.add(searchButton, gbc);

        panel.add(searchFormPanel, BorderLayout.NORTH);

        
        flightsTableModel = new DefaultTableModel(new Object[]{"Flight Number", "Departure", "Arrival", "Date", "Time", "Aircraft", "Seats", "Status", "Fare (Economy)"}, 0);
        flightsTable = new JTable(flightsTableModel);
        styleTable(flightsTable);
        JScrollPane scrollPane = new JScrollPane(flightsTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        
        bookFlightButton = createStyledButton("Book Selected Flight", new Color(51, 204, 102));
        bookFlightButton.setEnabled(false); 
        bookFlightButton.addActionListener(e -> {
            int selectedRow = flightsTable.getSelectedRow();
            if (selectedRow != -1) {
                try {
                    String flightNumber = (String) flightsTableModel.getValueAt(selectedRow, 0);
                    selectedFlightForBooking = getFlightByNumber(flightNumber); 
                    if (selectedFlightForBooking != null) {
                        bookingFlightDetailsLabel.setText("Booking for Flight: " + selectedFlightForBooking.getFlightNumber() +
                                " (" + selectedFlightForBooking.getDepartureAirport().getAirportCode() + " to " +
                                selectedFlightForBooking.getArrivalAirport().getAirportCode() + ")");
                        showPanel("BOOK_FLIGHT");
                    } else {
                        JOptionPane.showMessageDialog(this, "Could not retrieve flight details.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error selecting flight: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a flight to book.", "No Flight Selected", JOptionPane.WARNING_MESSAGE);
            }
        });
        JPanel bookButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bookButtonPanel.setOpaque(false);
        bookButtonPanel.add(bookFlightButton);
        panel.add(bookButtonPanel, BorderLayout.SOUTH);

        flightsTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && flightsTable.getSelectedRow() != -1) {
                bookFlightButton.setEnabled(true);
            } else {
                bookFlightButton.setEnabled(false);
            }
        });

        contentPanel.add(panel, "SEARCH_FLIGHTS");
    }

    /**
     * Initializes the panel for booking a flight.
     */
    private void initBookingPagePanel() {
    JPanel panel = createContentPanel("Book Flight");
    panel.setName("BOOK_FLIGHT");

    JPanel formPanel = new JPanel(new GridBagLayout());
    formPanel.setOpaque(false); 
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(5, 5, 5, 5);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    bookingFlightDetailsLabel = createLabel("Booking for Flight: N/A");
    bookingFlightDetailsLabel.setFont(new Font("Arial", Font.BOLD, 18));
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 2;
    formPanel.add(bookingFlightDetailsLabel, gbc);

    
    gbc.gridy++;
    gbc.gridwidth = 1;
    formPanel.add(createLabel("Passenger First Name:"), gbc);
    gbc.gridx = 1;
    passengerFirstNameField = new JTextField(15);
    passengerFirstNameField.setBackground(new Color(60, 70, 90));
    passengerFirstNameField.setForeground(Color.WHITE);
    passengerFirstNameField.setCaretColor(Color.WHITE);
    formPanel.add(passengerFirstNameField, gbc);

    gbc.gridy++;
    gbc.gridx = 0;
    formPanel.add(createLabel("Passenger Last Name:"), gbc);
    gbc.gridx = 1;
    passengerLastNameField = new JTextField(15);
    passengerLastNameField.setBackground(new Color(60, 70, 90));
    passengerLastNameField.setForeground(Color.WHITE);
    passengerLastNameField.setCaretColor(Color.WHITE);
    formPanel.add(passengerLastNameField, gbc);

    gbc.gridy++;
    gbc.gridx = 0;
    formPanel.add(createLabel("Date of Birth (YYYY-MM-DD):"), gbc);
    gbc.gridx = 1;
    passengerDOBField = createFormattedDateField();
    formPanel.add(passengerDOBField, gbc);

    gbc.gridy++;
    gbc.gridx = 0;
    formPanel.add(createLabel("Passport Number:"), gbc);
    gbc.gridx = 1;
    passengerPassportField = new JTextField(15);
    passengerPassportField.setBackground(new Color(60, 70, 90));
    passengerPassportField.setForeground(Color.WHITE);
    passengerPassportField.setCaretColor(Color.WHITE);
    formPanel.add(passengerPassportField, gbc);

    gbc.gridy++;
    gbc.gridx = 0;
    formPanel.add(createLabel("Nationality:"), gbc);
    gbc.gridx = 1;
    passengerNationalityField = new JTextField(15);
    passengerNationalityField.setBackground(new Color(60, 70, 90));
    passengerNationalityField.setForeground(Color.WHITE);
    passengerNationalityField.setCaretColor(Color.WHITE);
    formPanel.add(passengerNationalityField, gbc);

    gbc.gridy++;
    gbc.gridx = 0;
    formPanel.add(createLabel("Gender:"), gbc);
    gbc.gridx = 1;
    passengerGenderComboBox = new JComboBox<>(new String[]{"Male", "Female", "Other"});
    passengerGenderComboBox.setBackground(new Color(60, 70, 90));
    passengerGenderComboBox.setForeground(Color.WHITE);
    formPanel.add(passengerGenderComboBox, gbc);

    gbc.gridy++;
    gbc.gridx = 0;
    specialAssistanceCheckBox = new JCheckBox("Special Assistance Required");
    specialAssistanceCheckBox.setOpaque(false);
    specialAssistanceCheckBox.setForeground(Color.WHITE);
    formPanel.add(specialAssistanceCheckBox, gbc);

    gbc.gridy++;
    gbc.gridx = 0;
    formPanel.add(createLabel("Meal Preference:"), gbc);
    gbc.gridx = 1;
    mealPreferenceField = new JTextField(15);
    mealPreferenceField.setBackground(new Color(60, 70, 90));
    mealPreferenceField.setForeground(Color.WHITE);
    mealPreferenceField.setCaretColor(Color.WHITE);
    formPanel.add(mealPreferenceField, gbc);

    gbc.gridy++;
    gbc.gridx = 0;
    formPanel.add(createLabel("Seat Number:"), gbc);
    gbc.gridx = 1;
    seatNumberField = new JTextField(15);
    seatNumberField.setBackground(new Color(60, 70, 90));
    seatNumberField.setForeground(Color.WHITE);
    seatNumberField.setCaretColor(Color.WHITE);
    formPanel.add(seatNumberField, gbc);

    
    gbc.gridy++;
    gbc.gridx = 0;
    formPanel.add(createLabel("Payment Method:"), gbc);
    gbc.gridx = 1;
    paymentMethodComboBox = new JComboBox<>(); 
    paymentMethodComboBox.setBackground(new Color(60, 70, 90));
    paymentMethodComboBox.setForeground(Color.WHITE);
    formPanel.add(paymentMethodComboBox, gbc);

    
    gbc.gridy++;
    gbc.gridx = 0;
    formPanel.add(createLabel("Special Requests:"), gbc);
    gbc.gridx = 1;
    specialRequestsArea = new JTextArea(3, 15);
    specialRequestsArea.setLineWrap(true);
    specialRequestsArea.setWrapStyleWord(true);
    specialRequestsArea.setBackground(new Color(60, 70, 90));
    specialRequestsArea.setForeground(Color.WHITE);
    specialRequestsArea.setCaretColor(Color.WHITE);
    JScrollPane specialRequestsScrollPane = new JScrollPane(specialRequestsArea);
    formPanel.add(specialRequestsScrollPane, gbc);

    
    gbc.gridy++;
    gbc.gridx = 0;
    gbc.gridwidth = 2;
    JButton confirmBookingButton = createStyledButton("Confirm Booking", new Color(51, 204, 102));
    confirmBookingButton.addActionListener(e -> confirmBooking());
    formPanel.add(confirmBookingButton, gbc);

    
    JScrollPane scrollPane = new JScrollPane(formPanel);
    scrollPane.getViewport().setBackground(new Color(30, 40, 60)); 
    scrollPane.setBorder(BorderFactory.createEmptyBorder()); 
    panel.add(scrollPane, BorderLayout.CENTER);

    contentPanel.add(panel, "BOOK_FLIGHT");
}

    /**
     * Populates the payment method combo box on the booking page with saved methods.
     */
    private void populatePaymentMethodComboBox() {
        paymentMethodComboBox.removeAllItems();
        if (loggedInCustomer == null) {
            return;
        }

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.connect();
            if (conn == null) return;

            String sql = "SELECT method_id, method_type, card_last_four_digits, is_default FROM payment_methods WHERE user_id = ? ORDER BY is_default DESC, created_at DESC";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, loggedInCustomer.getUserID());
            rs = pstmt.executeQuery();

            while (rs.next()) {
                String methodId = rs.getString("method_id");
                String methodType = rs.getString("method_type");
                String lastFour = rs.getString("card_last_four_digits");
                boolean isDefault = rs.getBoolean("is_default");
                String display = methodType + " (**** " + lastFour + ")" + (isDefault ? " (Default)" : "");
                paymentMethodComboBox.addItem(methodId + " - " + display); 
            }
            if (paymentMethodComboBox.getItemCount() == 0) {
                paymentMethodComboBox.addItem("No saved methods. Add one in 'Manage Payment'.");
                
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading payment methods for booking: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
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


    /**
     * Initializes the panel for viewing customer's current bookings.
     */
    private void initMyBookingsPanel() {
        JPanel panel = createContentPanel("My Bookings");
        panel.setName("MY_BOOKINGS");

        myBookingsTableModel = new DefaultTableModel(new Object[]{"Booking ID", "Flight Number", "Departure", "Arrival", "Date", "Status", "Total Price"}, 0);
        myBookingsTable = new JTable(myBookingsTableModel);
        styleTable(myBookingsTable);
        JScrollPane scrollPane = new JScrollPane(myBookingsTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setOpaque(false);

        JButton viewTicketButton = createStyledButton("View Ticket", new Color(102, 153, 204));
        viewTicketButton.addActionListener(e -> viewTicket());
        buttonPanel.add(viewTicketButton);

        JButton cancelBookingButton = createStyledButton("Cancel Booking", new Color(204, 102, 51));
        cancelBookingButton.addActionListener(e -> cancelBooking());
        buttonPanel.add(cancelBookingButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);
        
    JButton payNowButton = createStyledButton("Pay Now", new Color(76, 175, 80));
    payNowButton.addActionListener(e -> payForBooking());
    buttonPanel.add(payNowButton);
        contentPanel.add(panel, "MY_BOOKINGS");
    }

    /**
     * Initializes the panel for viewing customer's booking history with filtering.
     */
    private void initBookingHistoryPanel() {
    JPanel panel = createContentPanel("Booking History");
    panel.setName("BOOKING_HISTORY");

    JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
    filterPanel.setOpaque(false);
    filterPanel.add(createLabel("Filter by Status:"));
    JComboBox<String> statusFilterComboBox = new JComboBox<>(new String[]{"All", "Confirmed", "Cancelled", "Completed"});
    statusFilterComboBox.setBackground(new Color(60, 70, 90));
    statusFilterComboBox.setForeground(Color.WHITE);
    filterPanel.add(statusFilterComboBox);
    JButton applyFilterButton = createStyledButton("Apply Filter", new Color(102, 102, 204));
    applyFilterButton.addActionListener(e -> loadBookingHistory((String) statusFilterComboBox.getSelectedItem()));
    filterPanel.add(applyFilterButton);
    panel.add(filterPanel, BorderLayout.NORTH);

    
    bookingHistoryTableModel = new DefaultTableModel(new Object[]{"Booking ID", "Flight Number", "Departure", "Arrival", "Date", "Status", "Total Price"}, 0);
    bookingHistoryTable = new JTable(bookingHistoryTableModel);
    styleTable(bookingHistoryTable);
    JScrollPane scrollPane = new JScrollPane(bookingHistoryTable);
    panel.add(scrollPane, BorderLayout.CENTER);

    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
    buttonPanel.setOpaque(false);
    JButton viewTicketHistoryButton = createStyledButton("View Ticket", new Color(102, 153, 204));
    viewTicketHistoryButton.addActionListener(e -> {
        int selectedRow = bookingHistoryTable.getSelectedRow();
        if (selectedRow != -1) {
            String bookingId = (String) bookingHistoryTableModel.getValueAt(selectedRow, 0);
            viewTicket(bookingId);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a booking.", "No Booking Selected", JOptionPane.WARNING_MESSAGE);
        }
    });
    buttonPanel.add(viewTicketHistoryButton);

    JButton writeReviewButton = createStyledButton("Write Review", new Color(153, 102, 204));
    writeReviewButton.addActionListener(e -> {
        int selectedRow = bookingHistoryTable.getSelectedRow();
        if (selectedRow != -1) {
            String flightNumber = (String) bookingHistoryTableModel.getValueAt(selectedRow, 1);
            rateFlightNumberField.setText(flightNumber);
            showPanel("RATE_FLIGHT");
        } else {
            JOptionPane.showMessageDialog(this, "Please select a booking to review its flight.", "No Booking Selected", JOptionPane.WARNING_MESSAGE);
        }
    });
    buttonPanel.add(writeReviewButton);

    panel.add(buttonPanel, BorderLayout.SOUTH);

    contentPanel.add(panel, "BOOKING_HISTORY");
}

    /**
     * Initializes the panel for updating customer profile details and changing password.
     */
   private void initUpdateProfilePanel() {
    JPanel panel = createContentPanel("Update Profile");
    panel.setName("UPDATE_PROFILE");
    
    

    JPanel formPanel = new JPanel(new GridBagLayout());
    formPanel.setOpaque(false); 
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(5, 5, 5, 5);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    
    gbc.gridx = 0;
    gbc.gridy = 0;
    formPanel.add(createLabel("Username:"), gbc);
    gbc.gridx = 1;
    profileUsernameField = new JTextField(20);
    profileUsernameField.setBackground(new Color(60, 70, 90));
    profileUsernameField.setForeground(Color.WHITE);
    profileUsernameField.setCaretColor(Color.WHITE);
    formPanel.add(profileUsernameField, gbc);

    
    gbc.gridx = 0;
    gbc.gridy = 1;
    formPanel.add(createLabel("Email:"), gbc);
    gbc.gridx = 1;
    profileEmailField = new JTextField(20);
    profileEmailField.setEditable(false);
    profileEmailField.setBackground(new Color(60, 70, 90));
    profileEmailField.setForeground(Color.WHITE);
    formPanel.add(profileEmailField, gbc);

    
    gbc.gridx = 0;
    gbc.gridy = 2;
    formPanel.add(createLabel("Phone Number:"), gbc);
    gbc.gridx = 1;
    profilePhoneNumberField = new JTextField(20);
    profilePhoneNumberField.setBackground(new Color(60, 70, 90));
    profilePhoneNumberField.setForeground(Color.WHITE);
    profilePhoneNumberField.setCaretColor(Color.WHITE);
    formPanel.add(profilePhoneNumberField, gbc);

    
    gbc.gridx = 0;
    gbc.gridy = 3;
    formPanel.add(createLabel("Address:"), gbc);
    gbc.gridx = 1;
    profileAddressField = new JTextField(20);
    profileAddressField.setBackground(new Color(60, 70, 90));
    profileAddressField.setForeground(Color.WHITE);
    profileAddressField.setCaretColor(Color.WHITE);
    formPanel.add(profileAddressField, gbc);

    
    gbc.gridx = 0;
    gbc.gridy = 4;
    formPanel.add(createLabel("Passport Number:"), gbc);
    gbc.gridx = 1;
    profilePassportNumberField = new JTextField(20);
    profilePassportNumberField.setBackground(new Color(60, 70, 90));
    profilePassportNumberField.setForeground(Color.WHITE);
    profilePassportNumberField.setCaretColor(Color.WHITE);
    formPanel.add(profilePassportNumberField, gbc);

    
    gbc.gridx = 0;
    gbc.gridy = 5;
    gbc.gridwidth = 2;
    JButton editDetailsButton = createStyledButton("Save Personal Details", new Color(51, 153, 204));
    editDetailsButton.addActionListener(e -> updateProfileDetails());
    formPanel.add(editDetailsButton, gbc);

    
    gbc.gridy = 6;
    gbc.gridwidth = 2;
    formPanel.add(new JSeparator(), gbc); 

    gbc.gridy = 7;
    gbc.gridwidth = 1;
    formPanel.add(createLabel("New Password:"), gbc);
    gbc.gridx = 1;
    profilePasswordField = new JPasswordField(20);
    profilePasswordField.setBackground(new Color(60, 70, 90));
    profilePasswordField.setForeground(Color.WHITE);
    profilePasswordField.setCaretColor(Color.WHITE);
    formPanel.add(profilePasswordField, gbc);

    gbc.gridy = 8;
    gbc.gridx = 0;
    formPanel.add(createLabel("Confirm New Password:"), gbc);
    gbc.gridx = 1;
    profileConfirmPasswordField = new JPasswordField(20);
    profileConfirmPasswordField.setBackground(new Color(60, 70, 90));
    profileConfirmPasswordField.setForeground(Color.WHITE);
    profileConfirmPasswordField.setCaretColor(Color.WHITE);
    formPanel.add(profileConfirmPasswordField, gbc);

    gbc.gridy = 9;
    gbc.gridx = 0;
    gbc.gridwidth = 2;
    JButton changePasswordButton = createStyledButton("Change Password", new Color(204, 153, 51));
    changePasswordButton.addActionListener(e -> changePassword());
    formPanel.add(changePasswordButton, gbc);

    
    JScrollPane scrollPane = new JScrollPane(formPanel);
    
    scrollPane.getViewport().setBackground(new Color(30, 40, 60));
    
    scrollPane.setBorder(BorderFactory.createEmptyBorder());
    
    panel.add(scrollPane, BorderLayout.CENTER);
    contentPanel.add(panel, "UPDATE_PROFILE");
}

    /**
     * Initializes the panel for managing payment methods.
     */
    private void initManagePaymentPanel() {
    JPanel panel = createContentPanel("Manage Payment Methods");
    panel.setName("MANAGE_PAYMENT");

    
    paymentMethodsTableModel = new DefaultTableModel(
        new Object[]{"ID", "Type", "Last 4", "Expiry", "Holder Name", "Default"}, 0) { 
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    paymentMethodsTable = new JTable(paymentMethodsTableModel);
    styleTable(paymentMethodsTable);
    JScrollPane scrollPane = new JScrollPane(paymentMethodsTable);
    panel.add(scrollPane, BorderLayout.CENTER);

    
    JPanel formPanel = new JPanel(new GridBagLayout());
    formPanel.setOpaque(false);
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(5, 5, 5, 5);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    
    gbc.gridx = 0;
    gbc.gridy = 0;
    formPanel.add(createLabel("Card Number:"), gbc);
    gbc.gridx = 1;
    cardNumberField = new JTextField(20);
    cardNumberField.setBackground(new Color(60, 70, 90));
    cardNumberField.setForeground(Color.WHITE);
    cardNumberField.setCaretColor(Color.WHITE);
    formPanel.add(cardNumberField, gbc);

    
    gbc.gridx = 0;
    gbc.gridy = 1;
    formPanel.add(createLabel("Card Holder Name:"), gbc);
    gbc.gridx = 1;
    cardHolderNameField = new JTextField(20);
    cardHolderNameField.setBackground(new Color(60, 70, 90));
    cardHolderNameField.setForeground(Color.WHITE);
    cardHolderNameField.setCaretColor(Color.WHITE);
    formPanel.add(cardHolderNameField, gbc);

    
    gbc.gridx = 0;
    gbc.gridy = 2;
    formPanel.add(createLabel("Expiry Date (MM/YY):"), gbc);
    gbc.gridx = 1;
    try {
        MaskFormatter expiryFormatter = new MaskFormatter("##/##");
        expiryFormatter.setPlaceholderCharacter('_');
        cardExpiryDateField = new JFormattedTextField(expiryFormatter);
        cardExpiryDateField.setBackground(new Color(60, 70, 90));
        cardExpiryDateField.setForeground(Color.WHITE);
        cardExpiryDateField.setCaretColor(Color.WHITE);
    } catch (ParseException e) {
        cardExpiryDateField = new JFormattedTextField();
    }
    formPanel.add(cardExpiryDateField, gbc);

    
    gbc.gridx = 0;
    gbc.gridy = 3;
    formPanel.add(createLabel("CVV:"), gbc);
    gbc.gridx = 1;
    cardCvvField = new JTextField(4);
    cardCvvField.setBackground(new Color(60, 70, 90));
    cardCvvField.setForeground(Color.WHITE);
    cardCvvField.setCaretColor(Color.WHITE);
    formPanel.add(cardCvvField, gbc);

    
    gbc.gridx = 0;
    gbc.gridy = 4;
    formPanel.add(createLabel("Payment Type:"), gbc);
    gbc.gridx = 1;
    
    
    JComboBox<String> newPaymentMethodTypeComboBox = new JComboBox<>(new String[]{"Credit Card", "Debit Card", "PayPal"});
    newPaymentMethodTypeComboBox.setBackground(new Color(60, 70, 90));
    newPaymentMethodTypeComboBox.setForeground(Color.WHITE);
    formPanel.add(newPaymentMethodTypeComboBox, gbc);


    
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
    buttonPanel.setOpaque(false);

    JButton addCardButton = createStyledButton("Add Payment Method", new Color(51, 204, 102));
    addCardButton.addActionListener(e -> addPaymentMethod((String) newPaymentMethodTypeComboBox.getSelectedItem()));
    buttonPanel.add(addCardButton);

    JButton deleteCardButton = createStyledButton("Delete Selected", new Color(204, 51, 51));
    deleteCardButton.addActionListener(e -> deletePaymentMethod());
    buttonPanel.add(deleteCardButton);

    JButton setDefaultCardButton = createStyledButton("Set Default", new Color(102, 153, 204));
    setDefaultCardButton.addActionListener(e -> setDefaultPaymentMethod());
    buttonPanel.add(setDefaultCardButton);

    panel.add(formPanel, BorderLayout.NORTH);
    panel.add(buttonPanel, BorderLayout.SOUTH);
    contentPanel.add(panel, "MANAGE_PAYMENT");
}

    /**
     * Initializes the panel for submitting a refund request.
     */
    private void initRefundRequestPanel() {
    JPanel panel = createContentPanel("Refund Request");
    panel.setName("REFUND_REQUEST");

    JPanel formPanel = new JPanel(new GridBagLayout());
    formPanel.setOpaque(false);
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(5, 5, 5, 5);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    
    gbc.gridx = 0;
    gbc.gridy = 0;
    formPanel.add(createLabel("Booking ID:"), gbc);
    gbc.gridx = 1;
    refundBookingIdField = new JTextField(15);
    refundBookingIdField.setBackground(new Color(60, 70, 90));
    refundBookingIdField.setForeground(Color.WHITE);
    refundBookingIdField.setCaretColor(Color.WHITE);
    formPanel.add(refundBookingIdField, gbc);

    
    gbc.gridx = 0;
    gbc.gridy = 1;
    formPanel.add(createLabel("Refund Amount:"), gbc);
    gbc.gridx = 1;
    JTextField refundAmountField = new JTextField(15);
    refundAmountField.setBackground(new Color(60, 70, 90));
    refundAmountField.setForeground(Color.WHITE);
    refundAmountField.setCaretColor(Color.WHITE);
    
    refundAmountField.setDocument(new PlainDocument() {
        @Override
        public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
            if (str == null) return;
            String newStr = str.replaceAll("[^0-9.]", "");
            super.insertString(offs, newStr, a);
        }
    });
    formPanel.add(refundAmountField, gbc);

    
    gbc.gridx = 0;
    gbc.gridy = 2;
    formPanel.add(createLabel("Reason:"), gbc);
    gbc.gridx = 1;
    refundReasonField = new JTextField(15);
    refundReasonField.setBackground(new Color(60, 70, 90));
    refundReasonField.setForeground(Color.WHITE);
    refundReasonField.setCaretColor(Color.WHITE);
    formPanel.add(refundReasonField, gbc);

    
    gbc.gridx = 0;
    gbc.gridy = 3;
    gbc.gridwidth = 2;
    JButton submitRefundButton = createStyledButton("Submit Refund Request", new Color(204, 102, 51));
    submitRefundButton.addActionListener(e -> submitRefundRequest(refundAmountField));
    formPanel.add(submitRefundButton, gbc);

    
    gbc.gridx = 0;
    gbc.gridy = 4;
    refundConfirmationLabel = createLabel("");
    refundConfirmationLabel.setForeground(new Color(153, 204, 255));
    formPanel.add(refundConfirmationLabel, gbc);

    panel.add(formPanel, BorderLayout.NORTH);
    contentPanel.add(panel, "REFUND_REQUEST");
}

    /**
     * Initializes the panel for rating a flight and submitting feedback.
     */
    private void initRateFlightPanel() {
        JPanel panel = createContentPanel("Rate Flight / Submit Feedback");
        panel.setName("RATE_FLIGHT");

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(createLabel("Select Past Flight:"), gbc);
        gbc.gridx = 1;
        pastBookedFlightsComboBox = new JComboBox<>();
        pastBookedFlightsComboBox.setPreferredSize(new Dimension(200, 30));
        pastBookedFlightsComboBox.setBackground(new Color(60, 70, 90));
        pastBookedFlightsComboBox.setForeground(Color.WHITE);
        formPanel.add(pastBookedFlightsComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(createLabel("Flight Number (or select above):"), gbc);
        gbc.gridx = 1;
        rateFlightNumberField = new JTextField(15);
        rateFlightNumberField.setBackground(new Color(60, 70, 90));
        rateFlightNumberField.setForeground(Color.WHITE);
        rateFlightNumberField.setCaretColor(Color.WHITE);
        formPanel.add(rateFlightNumberField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(createLabel("Rating (1-5 Stars):"), gbc);
        gbc.gridx = 1;
        rateStarsSpinner = new JSpinner(new SpinnerNumberModel(3, 1, 5, 1));
        ((JSpinner.DefaultEditor) rateStarsSpinner.getEditor()).getTextField().setBackground(new Color(60, 70, 90));
        ((JSpinner.DefaultEditor) rateStarsSpinner.getEditor()).getTextField().setForeground(Color.WHITE);
        rateStarsSpinner.setBackground(new Color(60, 70, 90));
        rateStarsSpinner.setForeground(Color.WHITE);
        formPanel.add(rateStarsSpinner, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(createLabel("Write Review:"), gbc);
        gbc.gridx = 1;
        rateReviewArea = new JTextArea(5, 20);
        rateReviewArea.setLineWrap(true);
        rateReviewArea.setWrapStyleWord(true);
        rateReviewArea.setBackground(new Color(60, 70, 90));
        rateReviewArea.setForeground(Color.WHITE);
        rateReviewArea.setCaretColor(Color.WHITE);
        JScrollPane reviewScrollPane = new JScrollPane(rateReviewArea);
        formPanel.add(reviewScrollPane, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        JButton submitFeedbackButton = createStyledButton("Submit Feedback", new Color(153, 102, 204));
        submitFeedbackButton.addActionListener(e -> submitFeedback());
        formPanel.add(submitFeedbackButton, gbc);

        panel.add(formPanel, BorderLayout.NORTH);
        contentPanel.add(panel, "RATE_FLIGHT");
    }

    /**
     * Initializes the panel for displaying and managing notifications.
     */
    private void initNotificationsPanel() {
        JPanel panel = createContentPanel("Notifications");
        panel.setName("NOTIFICATIONS");

        notificationArea = new JTextArea();
        notificationArea.setEditable(false);
        notificationArea.setLineWrap(true);
        notificationArea.setWrapStyleWord(true);
        notificationArea.setBackground(new Color(60, 70, 90));
        notificationArea.setForeground(Color.WHITE);
        notificationArea.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(notificationArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        JButton markAllReadButton = createStyledButton("Mark All As Read", new Color(102, 153, 204));
        markAllReadButton.addActionListener(e -> markAllNotificationsRead());
        buttonPanel.add(markAllReadButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        contentPanel.add(panel, "NOTIFICATIONS");
    }


    

    /**
     * Refreshes the logged-in customer's details from the database, including
     * username, email, phone number, address, passport number, and blacklist status.
     * This ensures the in-memory Customer object is always up-to-date.
     * Uses: `users`, `customers`, `blacklist` tables.
     */
    private void refreshLoggedInCustomerDetails() {
        if (loggedInCustomer == null) {
            return;
        }

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.connect();
            if (conn == null) return;

            
            
            String sql = "SELECT u.username, u.email, u.phone_number, u.address, " +
                         "c.passport_number, " +
                         "bl.reason AS blacklist_reason " +
                         "FROM users u " +
                         "LEFT JOIN customers c ON u.user_id = c.user_id " +
                         "LEFT JOIN blacklist bl ON u.user_id = bl.user_id AND bl.is_active = TRUE " +
                         "WHERE u.user_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, loggedInCustomer.getUserID());
            rs = pstmt.executeQuery();

            if (rs.next()) {
                
                loggedInCustomer.setUsername(rs.getString("username"));
                loggedInCustomer.setEmail(rs.getString("email"));
                loggedInCustomer.setPhoneNumber(rs.getInt("phone_number"));
                loggedInCustomer.setAddress(rs.getString("address"));
                loggedInCustomer.setPassportNumber(rs.getString("passport_number"));

                String blacklistReason = rs.getString("blacklist_reason");
                if (blacklistReason != null && !blacklistReason.isEmpty()) {
                    
                    loggedInCustomer.blacklistUser("DB_BLACKLIST_ENTRY", blacklistReason);
                } else {
                    loggedInCustomer.removeFromBlacklist();
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error refreshing customer details: " + ex.getMessage());
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

    /**
     * Searches for flights based on user input and populates the flights table.
     * Uses: `flight`, `airport` tables.
     */
    private void searchFlights() {
    flightsTableModel.setRowCount(0); 
    String from = searchFromField.getText().trim();
    String to = searchToField.getText().trim();
    String departureDateStr = searchDepartureDateField.getText().trim();

    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;

    try {
        conn = DatabaseConnection.connect();
        if (conn == null) {
            JOptionPane.showMessageDialog(this, "Database connection failed.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        StringBuilder sql = new StringBuilder(
            "SELECT f.flight_num, da.airport_code AS departure_code, aa.airport_code AS arrival_code, " +
            "f.departure_time, f.arrival_time, f.aircraft_type, f.total_seats, f.fare_economy, f.status " +
            "FROM flight f " +
            "JOIN airport da ON f.departure_airport_id = da.airport_code " +
            "JOIN airport aa ON f.arrival_airport_id = aa.airport_code WHERE 1=1"
        );

        List<Object> params = new ArrayList<>();
        if (!from.isEmpty()) {
            sql.append(" AND da.airport_code = ?");
            params.add(from);
        }
        if (!to.isEmpty()) {
            sql.append(" AND aa.airport_code = ?");
            params.add(to);
        }
        if (!departureDateStr.isEmpty() && !departureDateStr.contains("_")) {
            sql.append(" AND DATE(f.departure_time) = ?");
            Date departureDate = new SimpleDateFormat("yyyy-MM-dd").parse(departureDateStr);
            params.add(new java.sql.Date(departureDate.getTime()));
        }

        pstmt = conn.prepareStatement(sql.toString());
        for (int i = 0; i < params.size(); i++) {
            pstmt.setObject(i + 1, params.get(i));
        }

        rs = pstmt.executeQuery();
        while (rs.next()) {
            flightsTableModel.addRow(new Object[]{
                rs.getString("flight_num"),
                rs.getString("departure_code"),
                rs.getString("arrival_code"),
                new SimpleDateFormat("yyyy-MM-dd").format(rs.getTimestamp("departure_time")),
                new SimpleDateFormat("HH:mm").format(rs.getTimestamp("departure_time")),
                rs.getString("aircraft_type"),
                rs.getInt("total_seats"),
                rs.getString("status"),
                rs.getDouble("fare_economy")
            });
        }

        if (flightsTableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No flights found for your search criteria.", "No Results", JOptionPane.INFORMATION_MESSAGE);
        }

    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Error searching flights: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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


    /**
     * Retrieves a full Flight object from the database using its flight number.
     * Uses: `flight`, `airport` tables.
     * @param flightNumber The unique flight number.
     * @return The Flight object, or null if not found or an error occurs.
     */
    private Flight getFlightByNumber(String flightNumber) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Flight flight = null;

        try {
            conn = DatabaseConnection.connect();
            if (conn == null) return null;

           String sql = "SELECT f.flight_id, f.flight_num, da.airport_code AS departure_code, da.name AS departure_name, " +
             "da.city AS departure_city, da.country AS departure_country, da.time_zone AS departure_timezone, " +
             "aa.airport_code AS arrival_code, aa.name AS arrival_name, aa.city AS arrival_city, " +
             "aa.country AS arrival_country, aa.time_zone AS arrival_timezone, " +
             "f.departure_time, f.arrival_time, f.aircraft_type, f.total_seats, " +
             "f.fare_economy, f.fare_business, f.fare_first_class, f.status " +
                   
             "FROM flight f " +
             "JOIN airport da ON f.departure_airport_id = da.airport_code " +
             "JOIN airport aa ON f.arrival_airport_id = aa.airport_code " +
             "WHERE f.flight_num = ?";
           pstmt = conn.prepareStatement(sql);
pstmt.setString(1, flightNumber);
rs = pstmt.executeQuery();

if (rs.next()) {
    String flightID = rs.getString("flight_id");
    String aircraftType = rs.getString("aircraft_type");
    int totalSeats = rs.getInt("total_seats");
    
    
    double fareEconomy = rs.getDouble("fare_economy");
    double fareBusiness = rs.getDouble("fare_business");
    double fareFirstClass = rs.getDouble("fare_first_class");
    
    String status = rs.getString("status");
    
    
    Airport departureAirport = new Airport(
            rs.getString("departure_code"),
            rs.getString("departure_name"),
            rs.getString("departure_city"),
            rs.getString("departure_country"),
            rs.getString("departure_timezone")
    );
    Airport arrivalAirport = new Airport(
            rs.getString("arrival_code"),
            rs.getString("arrival_name"),
            rs.getString("arrival_city"),
            rs.getString("arrival_country"),
            rs.getString("arrival_timezone")
    );

    
    Date departureTime = new Date(rs.getTimestamp("departure_time").getTime());
    Date arrivalTime = new Date(rs.getTimestamp("arrival_time").getTime());

    
    Map<String, Double> fareMap = new HashMap<>();
    fareMap.put("Economy", fareEconomy);
    fareMap.put("Business", fareBusiness);
    fareMap.put("First Class", fareFirstClass);

    flight = new Flight(
            flightID, 
            flightNumber, 
            departureAirport, 
            arrivalAirport,
            departureTime, 
            arrivalTime, 
            aircraftType, 
            totalSeats,
            fareMap, 
            status, 
            new ArrayList<>()  
    );
}
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error retrieving flight details: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
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
        return flight;
    }

    /**
     * Confirms the booking for the selected flight and passenger details.
     * Inserts data into `passenger`, `booking`, `ticket`, and `payment` tables.
     * Updates `flight` table (reduces total_seats).
     * Uses transactions for atomicity.
     */
    private void confirmBooking() {
        if (loggedInCustomer == null || selectedFlightForBooking == null) {
            JOptionPane.showMessageDialog(this, "Please log in and select a flight first.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        
        if (paymentMethodComboBox.getSelectedItem() == null || paymentMethodComboBox.getSelectedItem().toString().contains("No saved methods")) {
            JOptionPane.showMessageDialog(this, "Please add and select a payment method.", "Payment Method Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        
        String pFirstName = passengerFirstNameField.getText().trim();
        String pLastName = passengerLastNameField.getText().trim();
        String pDOBStr = passengerDOBField.getText().trim();
        String pPassport = passengerPassportField.getText().trim();
        String pNationality = passengerNationalityField.getText().trim();
        String pGender = (String) passengerGenderComboBox.getSelectedItem();
        boolean pSpecialAssistance = specialAssistanceCheckBox.isSelected();
        String pMealPreference = mealPreferenceField.getText().trim();
        String pSeatNumber = seatNumberField.getText().trim();
        String selectedPaymentMethodDisplay = (String) paymentMethodComboBox.getSelectedItem();
        String specialRequests = specialRequestsArea.getText().trim();

        if (pFirstName.isEmpty() || pLastName.isEmpty() || pDOBStr.isEmpty() || pPassport.isEmpty() || pNationality.isEmpty() || pSeatNumber.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all required passenger details and seat number.", "Missing Information", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (pDOBStr.contains("_")) {
            JOptionPane.showMessageDialog(this, "Please enter a valid Date of Birth (YYYY-MM-DD).", "Invalid Date", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Date pDOB;
        try {
            pDOB = new SimpleDateFormat("yyyy-MM-dd").parse(pDOBStr);
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(this, "Invalid Date of Birth format. Please use ISO-8601 (YYYY-MM-DD).", "Invalid Date", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Connection conn = null;
        PreparedStatement pstmtBooking = null;
        PreparedStatement pstmtPassenger = null;
        PreparedStatement pstmtTicket = null;
        PreparedStatement pstmtPayment = null;
        PreparedStatement pstmtFlightUpdate = null; 

        try {
            conn = DatabaseConnection.connect();
            if (conn == null) {
                JOptionPane.showMessageDialog(this, "Database connection failed.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            conn.setAutoCommit(false); 

            
            String passengerId = "PASS" + UUID.randomUUID().toString().substring(0, 8);
            Passengers newPassenger = new Passengers(); 
            newPassenger.setPassengerId(passengerId);
            newPassenger.setFirstName(pFirstName);
            newPassenger.setLastName(pLastName);
            newPassenger.setDateOfBirth(pDOB);
            newPassenger.setPassportNumber(pPassport);
            newPassenger.setNationality(pNationality);
            newPassenger.setGender(pGender);
            newPassenger.setSpecialAssistance(pSpecialAssistance);
            newPassenger.setMealPreference(pMealPreference);
            newPassenger.setSeatNumber(pSeatNumber); 

            String sqlInsertPassenger = "INSERT INTO passenger (passenger_id, first_name, last_name, date_of_birth, passport_number, nationality, gender, special_assistance, meal_preference) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            pstmtPassenger = conn.prepareStatement(sqlInsertPassenger);
            pstmtPassenger.setString(1, newPassenger.getPassengerId());
            pstmtPassenger.setString(2, newPassenger.getFirstName());
            pstmtPassenger.setString(3, newPassenger.getLastName());
            pstmtPassenger.setDate(4, new java.sql.Date(newPassenger.getDateOfBirth().getTime()));
            pstmtPassenger.setString(5, newPassenger.getPassportNumber());
            pstmtPassenger.setString(6, newPassenger.getNationality());
            pstmtPassenger.setString(7, newPassenger.getGender());
            pstmtPassenger.setBoolean(8, newPassenger.isSpecialAssistance());
            pstmtPassenger.setString(9, newPassenger.getMealPreference());
            pstmtPassenger.executeUpdate();

            
            String bookingId = "BOOK" + UUID.randomUUID().toString().substring(0, 8);
            Date bookingDate = new Date(); 
            String seatClass = (String) searchClassComboBox.getSelectedItem();
            double totalPrice = selectedFlightForBooking.getFare().getOrDefault(seatClass, 0.0); 
            if (totalPrice == 0.0) {
                JOptionPane.showMessageDialog(this, "Fare not defined for selected class.", "Error", JOptionPane.ERROR_MESSAGE);
                conn.rollback();
                return;
            }

            List<Passengers> passengersList = new ArrayList<>();
            passengersList.add(newPassenger);
            List<String> seatNumbersList = new ArrayList<>();
            seatNumbersList.add(pSeatNumber);
            List<String> specialRequestsList = new ArrayList<>();
            if (!specialRequests.isEmpty()) {
                specialRequestsList.add(specialRequests);
            }


            Booking newBooking = new Booking(
                    bookingId, loggedInCustomer, selectedFlightForBooking, passengersList, bookingDate,
                    seatNumbersList, seatClass, totalPrice, "Pending", "Confirmed", specialRequestsList
            );

            
            String sqlInsertBooking = "INSERT INTO booking (booking_id, customer_id, flight_id, booking_date, seat_class, total_price, payment_status, special_requests) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            pstmtBooking = conn.prepareStatement(sqlInsertBooking);
    pstmtBooking.setString(1, newBooking.getBookingID());
    pstmtBooking.setString(2, newBooking.getCustomer().getUserID());
    pstmtBooking.setString(3, newBooking.getFlight().getFlightID());
    pstmtBooking.setTimestamp(4, new Timestamp(newBooking.getBookingDate().getTime()));
    pstmtBooking.setString(5, newBooking.getSeatClass());  
    pstmtBooking.setDouble(6, newBooking.getTotalPrice());
    pstmtBooking.setString(7, newBooking.getPaymentStatus());
    pstmtBooking.setString(8, String.join(",", newBooking.getSpecialRequests()));
            pstmtBooking.executeUpdate();

            
            String ticketId = "TICKET" + UUID.randomUUID().toString().substring(0, 8);
            Ticket newTicket = new Ticket(
                    ticketId, newBooking, newPassenger, pSeatNumber,
                    selectedFlightForBooking.getDepartureTime(), "TBD", "Confirmed", null
            );
            newTicket.generateBarcode(); 

            String sqlInsertTicket = "INSERT INTO ticket (ticket_id, booking_id, passenger_id, seat_number, boarding_time, gate_number, ticket_status, barcode) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            pstmtTicket = conn.prepareStatement(sqlInsertTicket);
            pstmtTicket.setString(1, newTicket.getTicketID());
            pstmtTicket.setString(2, newTicket.getBooking().getBookingID());
            pstmtTicket.setString(3, newTicket.getPassenger().getPassengerId());
            pstmtTicket.setString(4, newTicket.getSeatNumber());
            pstmtTicket.setTimestamp(5, new Timestamp(newTicket.getBoardingTime().getTime()));
            pstmtTicket.setString(6, newTicket.getGateNumber());
            pstmtTicket.setString(7, newTicket.getTicketStatus());
            pstmtTicket.setString(8, newTicket.getBarcode());
            pstmtTicket.executeUpdate();

            
            
            String methodId = selectedPaymentMethodDisplay.substring(0, selectedPaymentMethodDisplay.indexOf(" - "));

            
            String paymentMethodType = null;
            String cardLastFourDigits = null;
            String transactionId = "TXN" + UUID.randomUUID().toString().substring(0, 8); 

            String sqlGetPaymentMethodDetails = "SELECT method_type, card_last_four_digits FROM payment_methods WHERE method_id = ?";
            try (PreparedStatement pstmtGetMethod = conn.prepareStatement(sqlGetPaymentMethodDetails)) {
                pstmtGetMethod.setString(1, methodId);
                try (ResultSet rsMethod = pstmtGetMethod.executeQuery()) {
                    if (rsMethod.next()) {
                        paymentMethodType = rsMethod.getString("method_type");
                        cardLastFourDigits = rsMethod.getString("card_last_four_digits");
                    } else {
                        JOptionPane.showMessageDialog(this, "Selected payment method not found.", "Payment Error", JOptionPane.ERROR_MESSAGE);
                        conn.rollback();
                        return;
                    }
                }
            }

            
            String paymentId = "PAY" + UUID.randomUUID().toString().substring(0, 8);
            Payment newPayment = new Payment(
                    paymentId, newBooking, newBooking.getTotalPrice(), LocalDateTime.now(),
                    paymentMethodType, "Pending", transactionId, cardLastFourDigits
            );


        String paymentStatus = "Pending";


            
            String sqlUpdateFlightSeats = "UPDATE flight SET total_seats = total_seats - 1 WHERE flight_id = ?";
            pstmtFlightUpdate = conn.prepareStatement(sqlUpdateFlightSeats);
            pstmtFlightUpdate.setString(1, selectedFlightForBooking.getFlightID());
            pstmtFlightUpdate.executeUpdate();
            selectedFlightForBooking.setTotalSeats(selectedFlightForBooking.getTotalSeats() - 1); 
logSuspiciousActivity("Booking confirmed for flight " + selectedFlightForBooking.getFlightNumber());
currentAnomalyCount++;
if (currentAnomalyCount >= DEEPFAKE_THRESHOLD_ANOMALY_COUNT) {
    addNotification("Your account has triggered multiple suspicious actions and is being monitored.");
}

            conn.commit(); 
            JOptionPane.showMessageDialog(this, "Flight booked and paid successfully! Booking ID: " + bookingId, "Booking Confirmed", JOptionPane.INFORMATION_MESSAGE);
            this.addNotification("New booking confirmed! Booking ID: " + bookingId);
            
            passengerFirstNameField.setText("");
            passengerLastNameField.setText("");
            passengerDOBField.setText("");
            passengerPassportField.setText("");
            passengerNationalityField.setText("");
            mealPreferenceField.setText("");
            seatNumberField.setText("");
            specialRequestsArea.setText("");
            showPanel("MY_BOOKINGS"); 
        } catch (SQLException ex) {
            try {
                if (conn != null) conn.rollback(); 
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            JOptionPane.showMessageDialog(this, "Error confirming booking: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } finally {
            try {
                if (pstmtPassenger != null) pstmtPassenger.close();
                if (pstmtBooking != null) pstmtBooking.close();
                if (pstmtTicket != null) pstmtTicket.close();
                if (pstmtPayment != null) pstmtPayment.close();
                if (pstmtFlightUpdate != null) pstmtFlightUpdate.close();
                if (conn != null) conn.setAutoCommit(true); 
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Loads upcoming confirmed bookings for the logged-in customer and displays them.
     * Uses: `booking`, `flight`, `airport` tables.
     */

    private void loadMyBookings() {
    myBookingsTableModel.setRowCount(0);
    if (loggedInCustomer == null) return;

    try (Connection conn = DatabaseConnection.connect()) {
        String sql = "SELECT b.booking_id, f.flight_num, " +
                     "da.airport_code AS departure_airport_code, " +
                     "aa.airport_code AS arrival_airport_code, " +
                     "f.departure_time, b.payment_status, b.total_price, " +
                     "CASE WHEN EXISTS (SELECT 1 FROM ticket WHERE booking_id = b.booking_id) THEN 'Yes' ELSE 'No' END as has_ticket " +
                     "FROM booking b JOIN flight f ON b.flight_id = f.flight_id " +
                     "JOIN airport da ON f.departure_airport_id = da.airport_code " + 
                     "JOIN airport aa ON f.arrival_airport_id = aa.airport_code " +   
                     "WHERE b.customer_id = ? ORDER BY f.departure_time";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, loggedInCustomer.getUserID());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                myBookingsTableModel.addRow(new Object[]{
                    rs.getString("booking_id"),
                    rs.getString("flight_num"),
                    rs.getString("departure_airport_code"),
                    rs.getString("arrival_airport_code"),
                    new SimpleDateFormat("yyyy-MM-dd HH:mm").format(rs.getTimestamp("departure_time")),
                    rs.getString("payment_status"),
                    rs.getDouble("total_price"),
                    rs.getString("has_ticket")
                });
            }
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error loading my bookings: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
    }
}

   
   private void viewTicket() {
    int selectedRow = myBookingsTable.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Please select a booking to view the ticket.", "No Booking Selected", JOptionPane.WARNING_MESSAGE);
        return;
    }

    String bookingId = (String) myBookingsTableModel.getValueAt(selectedRow, 0);
    viewTicket(bookingId);
}

private void viewTicket(String bookingId) {
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;

    try {
        conn = DatabaseConnection.connect();
        if (conn == null) return;

        String sql = "SELECT t.ticket_id, t.seat_number, t.boarding_time, t.gate_number, t.ticket_status, t.barcode, " +
                "p.first_name, p.last_name, p.passport_number, " +
                "f.flight_num, da.airport_code AS departure_code, da.name AS departure_name, da.city AS departure_city, " +
                "aa.airport_code AS arrival_code, aa.name AS arrival_name, aa.city AS arrival_city, " +
                "f.departure_time, f.arrival_time, f.aircraft_type, " +
                "b.seat_class, b.booking_date " +
                "FROM ticket t " +
                "JOIN booking b ON t.booking_id = b.booking_id " +
                "JOIN passenger p ON t.passenger_id = p.passenger_id " +
                "JOIN flight f ON b.flight_id = f.flight_id " +
                "JOIN airport da ON f.departure_airport_id = da.airport_code " +
                "JOIN airport aa ON f.arrival_airport_id = aa.airport_code " +
                "WHERE t.booking_id = ?";
        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, bookingId);
        rs = pstmt.executeQuery();

        if (rs.next()) {
            JPanel ticketPanel = new JPanel(new BorderLayout());
            ticketPanel.setBackground(new Color(245, 245, 245));
            ticketPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(200, 200, 200)),
                    BorderFactory.createEmptyBorder(20, 20, 20, 20)));
            ticketPanel.setPreferredSize(new Dimension(700, 400));

            JPanel headerPanel = new GradientPanel(new Color(30, 30, 30), new Color(70, 70, 70));
            headerPanel.setLayout(new BorderLayout());
            headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

            JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            logoPanel.setOpaque(false);

            try {
                ImageIcon logoIcon = new ImageIcon("premium_logo.png");
                Image logoImage = logoIcon.getImage().getScaledInstance(120, 60, Image.SCALE_SMOOTH);
                JLabel logoLabel = new JLabel(new ImageIcon(logoImage));
                logoPanel.add(logoLabel);
            } catch (Exception e) {
                JLabel logoLabel = new JLabel("SKYLINE PREMIUM");
                logoLabel.setFont(new Font("Montserrat", Font.BOLD, 24));
                logoLabel.setForeground(new Color(255, 215, 0));
                logoPanel.add(logoLabel);
            }

            JLabel boardingLabel = new JLabel("BOARDING PASS");
            boardingLabel.setFont(new Font("Montserrat", Font.BOLD, 28));
            boardingLabel.setForeground(Color.WHITE);
            boardingLabel.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 0));

            JPanel headerRight = new JPanel(new BorderLayout());
            headerRight.setOpaque(false);

            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM dd");
            Date departureTime = rs.getTimestamp("departure_time");

            JLabel flightLabel = new JLabel("FLIGHT " + rs.getString("flight_num") + " | " + dateFormat.format(departureTime));
            flightLabel.setFont(new Font("Montserrat", Font.PLAIN, 14));
            flightLabel.setForeground(new Color(200, 200, 200));
            flightLabel.setHorizontalAlignment(SwingConstants.RIGHT);

            headerRight.add(boardingLabel, BorderLayout.CENTER);
            headerRight.add(flightLabel, BorderLayout.SOUTH);

            headerPanel.add(logoPanel, BorderLayout.WEST);
            headerPanel.add(headerRight, BorderLayout.CENTER);

            ticketPanel.add(headerPanel, BorderLayout.NORTH);

            JPanel mainContent = new JPanel(new GridLayout(1, 2, 0, 0));

            JPanel leftPanel = new JPanel(new BorderLayout());
            leftPanel.setBackground(Color.WHITE);
            leftPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

            JLabel nameLabel = new JLabel(rs.getString("first_name") + " " + rs.getString("last_name"));
            nameLabel.setFont(new Font("Montserrat", Font.BOLD, 28));
            nameLabel.setForeground(new Color(50, 50, 50));
            nameLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

            JPanel routePanel = new JPanel(new GridLayout(2, 1, 0, 15));
            routePanel.setOpaque(false);

            JPanel departurePanel = createRoutePanel("✈️ DEPARTURE",
                    rs.getString("departure_code"),
                    rs.getString("departure_city"),
                    rs.getTimestamp("departure_time"));

            JPanel arrivalPanel = createRoutePanel("🛬 ARRIVAL",
                    rs.getString("arrival_code"),
                    rs.getString("arrival_city"),
                    rs.getTimestamp("arrival_time"));

            routePanel.add(departurePanel);
            routePanel.add(arrivalPanel);

            leftPanel.add(nameLabel, BorderLayout.NORTH);
            leftPanel.add(routePanel, BorderLayout.CENTER);

            JPanel rightPanel = new GradientPanel(new Color(40, 40, 40), new Color(70, 70, 70));
            rightPanel.setLayout(new BorderLayout());
            rightPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

            JPanel detailsPanel = new JPanel(new GridLayout(0, 1, 0, 15));
            detailsPanel.setOpaque(false);

            addPremiumDetail(detailsPanel, "SEAT", rs.getString("seat_number"), "💺");
            addPremiumDetail(detailsPanel, "CLASS", rs.getString("seat_class"), "✨");
            addPremiumDetail(detailsPanel, "GATE", rs.getString("gate_number"), "🚪");
            addPremiumDetail(detailsPanel, "BOARDING",
                    new SimpleDateFormat("HH:mm").format(rs.getTimestamp("boarding_time")), "⏱️");
            addPremiumDetail(detailsPanel, "AIRCRAFT", rs.getString("aircraft_type"), "✈️");
            addPremiumDetail(detailsPanel, "TICKET", rs.getString("ticket_id"), "🎫");

            JPanel codePanel = new JPanel(new BorderLayout(0, 10));
            codePanel.setOpaque(false);

            JPanel barcodeContainer = new JPanel(new BorderLayout());
            barcodeContainer.setBackground(new Color(60, 60, 60));
            barcodeContainer.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(100, 100, 100)),
                    BorderFactory.createEmptyBorder(15, 15, 15, 15)));

            JPanel barcodeLines = new JPanel(new GridLayout(1, 0, 1, 0));
            barcodeLines.setBackground(new Color(60, 60, 60));
            Random rand = new Random();
            for (int i = 0; i < 40; i++) {
                JPanel line = new JPanel();
                int height = rand.nextInt(30) + 30;
                line.setPreferredSize(new Dimension(rand.nextInt(3) + 1, height));
                line.setBackground(new Color(220, 220, 220));
                barcodeLines.add(line);
            }

            JLabel barcodeLabel = new JLabel(rs.getString("barcode"), SwingConstants.CENTER);
            barcodeLabel.setFont(new Font("Montserrat", Font.BOLD, 12));
            barcodeLabel.setForeground(new Color(200, 200, 200));

            barcodeContainer.add(barcodeLines, BorderLayout.CENTER);
            barcodeContainer.add(barcodeLabel, BorderLayout.SOUTH);

            codePanel.add(new JLabel("SCAN AT GATE", SwingConstants.CENTER), BorderLayout.NORTH);
            codePanel.add(barcodeContainer, BorderLayout.CENTER);

            rightPanel.add(detailsPanel, BorderLayout.CENTER);
            rightPanel.add(codePanel, BorderLayout.SOUTH);

            mainContent.add(leftPanel);
            mainContent.add(rightPanel);

            ticketPanel.add(mainContent, BorderLayout.CENTER);

            JPanel footerPanel = new JPanel(new BorderLayout());
            footerPanel.setBackground(new Color(245, 245, 245));
            footerPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

            JSeparator divider = new JSeparator();
            divider.setForeground(new Color(200, 200, 200));

            JLabel termsLabel = new JLabel("<html><div style='text-align: center; color: #666666; font-size: 10px;'>"
                    + "PLEASE ARRIVE AT THE GATE AT LEAST 30 MINUTES BEFORE DEPARTURE<br>"
                    + "VALID GOVERNMENT PHOTO ID REQUIRED • BAGGAGE POLICY APPLIES</div></html>");
            termsLabel.setFont(new Font("Montserrat", Font.PLAIN, 10));

            footerPanel.add(divider, BorderLayout.NORTH);
            footerPanel.add(termsLabel, BorderLayout.CENTER);

            JLabel companyNameLabel = new JLabel("JUPITER AIRWAYS", SwingConstants.LEFT);
            companyNameLabel.setFont(new Font("Segoe UI Black", Font.BOLD, 14));
            companyNameLabel.setForeground(new Color(255, 140, 0));
            companyNameLabel.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 0));

            footerPanel.add(companyNameLabel, BorderLayout.WEST);

            ticketPanel.add(footerPanel, BorderLayout.SOUTH);

            JDialog ticketDialog = new JDialog();
            ticketDialog.setTitle("Premium Boarding Pass - " + rs.getString("flight_num"));
            ticketDialog.setModal(true);

            ((JComponent) ticketDialog.getContentPane()).setBorder(
                    BorderFactory.createEmptyBorder(5, 5, 5, 5));

            ticketDialog.add(ticketPanel);
            ticketDialog.pack();
            ticketDialog.setLocationRelativeTo(this);

            ticketDialog.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Ticket not found for booking ID: " + bookingId,
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this,
                "Error viewing ticket: " + ex.getMessage(),
                "Database Error", JOptionPane.ERROR_MESSAGE);
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

private JPanel createRoutePanel(String label, String airportCode, String city, Timestamp time) {
    JPanel panel = new JPanel(new BorderLayout());
    panel.setOpaque(false);
    
    JLabel labelLabel = new JLabel(label);
    labelLabel.setFont(new Font("Montserrat", Font.BOLD, 12));
    labelLabel.setForeground(new Color(150, 150, 150));
    
    JPanel infoPanel = new JPanel(new GridLayout(2, 1, 0, 5));
    infoPanel.setOpaque(false);
    
    JLabel airportLabel = new JLabel(airportCode + " • " + city);
    airportLabel.setFont(new Font("Montserrat", Font.BOLD, 18));
    airportLabel.setForeground(new Color(70, 70, 70));
    
    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
    JLabel timeLabel = new JLabel(timeFormat.format(time));
    timeLabel.setFont(new Font("Montserrat", Font.PLAIN, 16));
    timeLabel.setForeground(new Color(100, 100, 100));
    
    infoPanel.add(airportLabel);
    infoPanel.add(timeLabel);
    
    panel.add(labelLabel, BorderLayout.NORTH);
    panel.add(infoPanel, BorderLayout.CENTER);
    
    return panel;
}

private void addPremiumDetail(JPanel panel, String label, String value, String emoji) {
    JPanel detailPanel = new JPanel(new BorderLayout());
    detailPanel.setOpaque(false);
    
    JLabel labelLabel = new JLabel(emoji + " " + label);
    labelLabel.setFont(new Font("Montserrat", Font.PLAIN, 12));
    labelLabel.setForeground(new Color(180, 180, 180));
    
    JLabel valueLabel = new JLabel(value);
    valueLabel.setFont(new Font("Montserrat", Font.BOLD, 16));
    valueLabel.setForeground(Color.WHITE);
    
    detailPanel.add(labelLabel, BorderLayout.NORTH);
    detailPanel.add(valueLabel, BorderLayout.CENTER);
    
    panel.add(detailPanel);
}


class GradientPanel extends JPanel {
    private Color color1;
    private Color color2;
    
    public GradientPanel(Color color1, Color color2) {
        this.color1 = color1;
        this.color2 = color2;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        GradientPaint gp = new GradientPaint(0, 0, color1, getWidth(), getHeight(), color2);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }
}
    
   private void cancelBooking() {
    int selectedRow = myBookingsTable.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, 
            "Please select a booking to cancel.", 
            "No Booking Selected", 
            JOptionPane.WARNING_MESSAGE);
        return;
    }

    String bookingId = myBookingsTableModel.getValueAt(selectedRow, 0).toString();
    Object statusObj = myBookingsTableModel.getValueAt(selectedRow, 5);
    String currentStatus = (statusObj != null) ? statusObj.toString() : "";

    if ("Cancelled".equalsIgnoreCase(currentStatus)) {
        JOptionPane.showMessageDialog(this, 
            "This booking is already cancelled.", 
            "Already Cancelled", 
            JOptionPane.INFORMATION_MESSAGE);
        return;
    }

    int confirm = JOptionPane.showConfirmDialog(this, 
        "Are you sure you want to cancel booking ID: " + bookingId + "?", 
        "Confirm Cancellation", 
        JOptionPane.YES_NO_OPTION);
    
    if (confirm == JOptionPane.YES_OPTION) {
        String reason = JOptionPane.showInputDialog(this, "Please enter a reason for cancellation:");
        if (reason == null || reason.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Cancellation requires a reason.", 
                "Missing Reason", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        Connection conn = null;
        try {
            conn = DatabaseConnection.connect();
            if (conn == null) return;
            conn.setAutoCommit(false);

            
            String flightId;
            try (PreparedStatement pstmt = conn.prepareStatement(
                "SELECT flight_id FROM booking WHERE booking_id = ?")) {
                pstmt.setString(1, bookingId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (!rs.next()) {
                        JOptionPane.showMessageDialog(this,
                            "Booking not found in database.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    flightId = rs.getString("flight_id");
                }
            }

            
            try (PreparedStatement pstmt = conn.prepareStatement(
                "UPDATE booking SET payment_status = 'Cancelled' " +
                "WHERE booking_id = ?")) {
                pstmt.setString(1, bookingId);
                pstmt.executeUpdate();
            }

            
            try (PreparedStatement pstmt = conn.prepareStatement(
                "UPDATE booking SET special_requests = " +
                "CONCAT(IFNULL(special_requests,''), ' [CANCELLED: ', ?, ']') " +
                "WHERE booking_id = ?")) {
                pstmt.setString(1, reason);
                pstmt.setString(2, bookingId);
                pstmt.executeUpdate();
            }

            
            try (PreparedStatement pstmt = conn.prepareStatement(
                "UPDATE ticket SET ticket_status = 'Cancelled' WHERE booking_id = ?")) {
                pstmt.setString(1, bookingId);
                pstmt.executeUpdate();
            }

            
            try (PreparedStatement pstmt = conn.prepareStatement(
                "UPDATE flight SET total_seats = total_seats + 1 WHERE flight_id = ?")) {
                pstmt.setString(1, flightId);
                pstmt.executeUpdate();
            }

            conn.commit();
            
            logSuspiciousActivity("Booking cancelled. ID: " + bookingId);
currentAnomalyCount++;
if (currentAnomalyCount >= DEEPFAKE_THRESHOLD_ANOMALY_COUNT) {
    addNotification("Your account has triggered multiple suspicious actions and is being monitored.");
}

            JOptionPane.showMessageDialog(this,
                "Booking cancelled successfully!",
                "Cancellation Complete",
                JOptionPane.INFORMATION_MESSAGE);
                
            
            loadMyBookings();
            loadBookingHistory("All");
            
        } catch (SQLException ex) {
            try { if (conn != null) conn.rollback(); } catch (SQLException e) {}
            JOptionPane.showMessageDialog(this,
                "Database error during cancellation:\n" + ex.getMessage(),
                "Cancellation Failed",
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } finally {
            try { 
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException ex) {}
        }
    }
}

    
    private void loadBookingHistory(String statusFilter) {
    bookingHistoryTableModel.setRowCount(0); 
    if (loggedInCustomer == null) {
        return;
    }

    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;

    try {
        conn = DatabaseConnection.connect();
        if (conn == null) {
            JOptionPane.showMessageDialog(this, "Database connection failed.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        StringBuilder sqlBuilder = new StringBuilder(
            "SELECT b.booking_id, f.flight_num, da.airport_code AS departure_code, aa.airport_code AS arrival_code, " +
            "f.departure_time, b.payment_status, b.total_price " +
            "FROM booking b " +
            "JOIN flight f ON b.flight_id = f.flight_id " +
            "JOIN airport da ON f.departure_airport_id = da.airport_code " +
            "JOIN airport aa ON f.arrival_airport_id = aa.airport_code " +
            "WHERE b.customer_id = ? "
        );

        if (!"All".equalsIgnoreCase(statusFilter)) {
            sqlBuilder.append(" AND b.payment_status = ?");
        }
        sqlBuilder.append(" ORDER BY f.departure_time DESC");

        pstmt = conn.prepareStatement(sqlBuilder.toString());
        pstmt.setString(1, loggedInCustomer.getUserID());
        if (!"All".equalsIgnoreCase(statusFilter)) {
            pstmt.setString(2, statusFilter);
        }
        rs = pstmt.executeQuery();

        while (rs.next()) {
            bookingHistoryTableModel.addRow(new Object[]{
                rs.getString("booking_id"),
                rs.getString("flight_num"),
                rs.getString("departure_code"),
                rs.getString("arrival_code"),
                new SimpleDateFormat("yyyy-MM-dd HH:mm").format(rs.getTimestamp("departure_time")),
                rs.getString("payment_status"),  
                rs.getDouble("total_price")
            });
        }
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Error loading booking history: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
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

   
    private void populateProfileData() {
        if (loggedInCustomer != null) {
            profileUsernameField.setText(loggedInCustomer.getUsername());
            profileEmailField.setText(loggedInCustomer.getEmail());
            profilePhoneNumberField.setText(String.valueOf(loggedInCustomer.getPhoneNumber()));
            profileAddressField.setText(loggedInCustomer.getAddress());
            
            profilePassportNumberField.setText(loggedInCustomer.getPassportNumber() != null ? loggedInCustomer.getPassportNumber() : "");
        }
    }

   
    private void updateProfileDetails() {
        if (loggedInCustomer == null) {
            JOptionPane.showMessageDialog(this, "No customer logged in.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String newUsername = profileUsernameField.getText().trim();
        String newPhoneNumberStr = profilePhoneNumberField.getText().trim();
        String newAddress = profileAddressField.getText().trim();
        String newPassportNumber = profilePassportNumberField.getText().trim(); 

        if (newUsername.isEmpty() || newPhoneNumberStr.isEmpty() || newAddress.isEmpty() || newPassportNumber.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all profile fields.", "Missing Information", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int newPhoneNumber;
        try {
            newPhoneNumber = Integer.parseInt(newPhoneNumberStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Phone number must be a valid number.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Connection conn = null;
        PreparedStatement pstmtUser = null;
        PreparedStatement pstmtCustomer = null;

        try {
            conn = DatabaseConnection.connect();
            if (conn == null) return;
            conn.setAutoCommit(false); 

            
            String sqlUser = "UPDATE users SET username = ?, phone_number = ?, address = ? WHERE user_id = ?";
            pstmtUser = conn.prepareStatement(sqlUser);
            pstmtUser.setString(1, newUsername);
            pstmtUser.setInt(2, newPhoneNumber);
            pstmtUser.setString(3, newAddress);
            pstmtUser.setString(4, loggedInCustomer.getUserID());
            pstmtUser.executeUpdate();

            
            String sqlCustomer = "UPDATE customers SET passport_number = ? WHERE customer_id = ?"; 
            pstmtCustomer = conn.prepareStatement(sqlCustomer);
            pstmtCustomer.setString(1, newPassportNumber);
            pstmtCustomer.setString(2, loggedInCustomer.getUserID()); 
            pstmtCustomer.executeUpdate();

            conn.commit(); 

            
            loggedInCustomer.setUsername(newUsername);
            loggedInCustomer.setPhoneNumber(newPhoneNumber);
            loggedInCustomer.setAddress(newAddress);
            loggedInCustomer.setPassportNumber(newPassportNumber); 
            welcomeLabel.setText("Welcome, " + loggedInCustomer.getUsername() + "!"); 
            JOptionPane.showMessageDialog(this, "Profile updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            try {
                if (conn != null) conn.rollback(); 
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            JOptionPane.showMessageDialog(this, "Error updating profile: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } finally {
            try {
                if (pstmtUser != null) pstmtUser.close();
                if (pstmtCustomer != null) pstmtCustomer.close();
                if (conn != null) conn.setAutoCommit(true); 
                if (conn != null) conn.close();
            }
            catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void changePassword() {
        if (loggedInCustomer == null) {
            JOptionPane.showMessageDialog(this, "No customer logged in.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String newPassword = new String(profilePasswordField.getPassword());
        String confirmPassword = new String(profileConfirmPasswordField.getPassword());

        if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both new password and confirm password.", "Missing Information", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!newPassword.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "New password and confirm password do not match.", "Password Mismatch", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (newPassword.length() < 6) { 
            JOptionPane.showMessageDialog(this, "Password must be at least 6 characters long.", "Weak Password", JOptionPane.WARNING_MESSAGE);
            return;
        }

        
        
        String newPasswordHash = newPassword; 

        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DatabaseConnection.connect();
            if (conn == null) return;

            String sql = "UPDATE users SET password_hash = ? WHERE user_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, newPasswordHash);
            pstmt.setString(2, loggedInCustomer.getUserID());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                loggedInCustomer.setPasswordHash(newPasswordHash); 
                JOptionPane.showMessageDialog(this, "Password changed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                profilePasswordField.setText("");
                profileConfirmPasswordField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to change password. User not found?", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error changing password: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
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

   
    private void addPaymentMethod(String paymentType) {
        
        String cardNumber = cardNumberField.getText().trim();
        String cardHolderName = cardHolderNameField.getText().trim();
        String expiryDate = cardExpiryDateField.getText().trim();
        String cvv = cardCvvField.getText().trim();

        
        if (cardNumber.isEmpty() || cardHolderName.isEmpty() ||
            expiryDate.isEmpty() || cvv.isEmpty() || expiryDate.contains("_")) {
            JOptionPane.showMessageDialog(this,
                "Please fill in all card details.",
                "Missing Information",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        
        if (!cardNumber.matches("\\d{13,19}")) {
            JOptionPane.showMessageDialog(this,
                "Card number must be 13-19 digits.",
                "Invalid Card Number",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        
        if (!expiryDate.matches("\\d{2}/\\d{2}")) {
            JOptionPane.showMessageDialog(this,
                "Please enter expiry date in MM/YY format.",
                "Invalid Expiry Date",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        
        if (!cvv.matches("\\d{3,4}")) {
            JOptionPane.showMessageDialog(this,
                "CVV must be 3 or 4 digits.",
                "Invalid CVV",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        
        String lastFourDigits = cardNumber.substring(cardNumber.length() - 4);
        String paymentMethodId = "PM-" + UUID.randomUUID().toString().substring(0, 8);

        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DatabaseConnection.connect();
            if (conn == null) return;
            conn.setAutoCommit(false); 

            
            boolean isFirstMethod = true;
            String checkSql = "SELECT COUNT(*) FROM payment_methods WHERE user_id = ?";
            try (PreparedStatement checkPstmt = conn.prepareStatement(checkSql)) {
                checkPstmt.setString(1, loggedInCustomer.getUserID());
                try (ResultSet rs = checkPstmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        isFirstMethod = false;
                    }
                }
            }

            
            String sql = "INSERT INTO payment_methods (method_id, user_id, method_type, card_last_four_digits, expiry_date, card_holder_name, is_default) VALUES (?, ?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, paymentMethodId);
            pstmt.setString(2, loggedInCustomer.getUserID());
            pstmt.setString(3, paymentType);
            pstmt.setString(4, lastFourDigits);
            pstmt.setString(5, expiryDate);
            pstmt.setString(6, cardHolderName);
            pstmt.setBoolean(7, isFirstMethod); 

            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                conn.commit();
                JOptionPane.showMessageDialog(this,
                    "Payment method saved successfully!\n" +
                    "Type: " + paymentType + "\n" +
                    "Last 4: " + lastFourDigits,
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                loadPaymentMethods(); 
                populatePaymentMethodComboBox(); 
            } else {
                conn.rollback();
                JOptionPane.showMessageDialog(this,
                    "Failed to save payment method.",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            try { if (conn != null) conn.rollback(); } catch (SQLException e) { e.printStackTrace(); }
            JOptionPane.showMessageDialog(this,
                "Failed to save payment method: " + ex.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.setAutoCommit(true);
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            
            cardNumberField.setText("");
            cardHolderNameField.setText("");
            cardExpiryDateField.setText("");
            cardCvvField.setText("");
        }
    }

    /**
     * Deletes a selected payment method from the `payment_methods` table.
     */
    private void deletePaymentMethod() {
        int selectedRow = paymentMethodsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select a payment method to delete.",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        String paymentMethodId = (String) paymentMethodsTableModel.getValueAt(selectedRow, 0);
        String lastFour = (String) paymentMethodsTableModel.getValueAt(selectedRow, 2);

        int confirm = JOptionPane.showConfirmDialog(this,
            "Delete payment method ending with " + lastFour + "?",
            "Confirm Deletion",
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            Connection conn = null;
            PreparedStatement pstmt = null;
            try {
                conn = DatabaseConnection.connect();
                if (conn == null) return;

                String sql = "DELETE FROM payment_methods WHERE method_id = ? AND user_id = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, paymentMethodId);
                pstmt.setString(2, loggedInCustomer.getUserID());

                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this,
                        "Payment method deleted successfully.",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                    loadPaymentMethods(); 
                    populatePaymentMethodComboBox(); 
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Failed to delete payment method. It might not exist or belong to you.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this,
                    "Database error during deletion: " + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
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

  
    private void setDefaultPaymentMethod() {
        int selectedRow = paymentMethodsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select a payment method to set as default.",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        String paymentMethodId = (String) paymentMethodsTableModel.getValueAt(selectedRow, 0);
        String lastFour = (String) paymentMethodsTableModel.getValueAt(selectedRow, 2);

        Connection conn = null;
        PreparedStatement pstmtReset = null;
        PreparedStatement pstmtSet = null;
        try {
            conn = DatabaseConnection.connect();
            if (conn == null) return;
            conn.setAutoCommit(false); 

            
            String sqlResetDefault = "UPDATE payment_methods SET is_default = FALSE WHERE user_id = ?";
            pstmtReset = conn.prepareStatement(sqlResetDefault);
            pstmtReset.setString(1, loggedInCustomer.getUserID());
            pstmtReset.executeUpdate();

            
            String sqlSetDefault = "UPDATE payment_methods SET is_default = TRUE WHERE method_id = ? AND user_id = ?";
            pstmtSet = conn.prepareStatement(sqlSetDefault);
            pstmtSet.setString(1, paymentMethodId);
            pstmtSet.setString(2, loggedInCustomer.getUserID());
            int rowsAffected = pstmtSet.executeUpdate();

            if (rowsAffected > 0) {
                conn.commit();
                JOptionPane.showMessageDialog(this,
                    "Payment method ending with " + lastFour + " set as default.",
                    "Default Set",
                    JOptionPane.INFORMATION_MESSAGE);
                loadPaymentMethods(); 
                populatePaymentMethodComboBox(); 
            } else {
                conn.rollback();
                JOptionPane.showMessageDialog(this,
                    "Failed to set default payment method. It might not exist or belong to you.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            try { if (conn != null) conn.rollback(); } catch (SQLException e) { e.printStackTrace(); }
            JOptionPane.showMessageDialog(this,
                "Database error setting default: " + ex.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } finally {
            try {
                if (pstmtReset != null) pstmtReset.close();
                if (pstmtSet != null) pstmtSet.close();
                if (conn != null) conn.setAutoCommit(true);
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    
    private void submitRefundRequest(JTextField refundAmountField) {
    String bookingId = refundBookingIdField.getText().trim();
    String reason = refundReasonField.getText().trim();
    String amountStr = refundAmountField.getText().trim();

    
    if (bookingId.isEmpty() || reason.isEmpty() || amountStr.isEmpty()) {
        JOptionPane.showMessageDialog(this, 
            "Please fill in all required fields.", 
            "Missing Information", 
            JOptionPane.WARNING_MESSAGE);
        return;
    }

    
    double amount;
    try {
        amount = Double.parseDouble(amountStr);
        if (amount <= 0) {
            throw new NumberFormatException();
        }
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, 
            "Please enter a valid positive number for amount.", 
            "Invalid Amount", 
            JOptionPane.WARNING_MESSAGE);
        return;
    }

    if (loggedInCustomer == null) {
        JOptionPane.showMessageDialog(this, 
            "No customer logged in.", 
            "Error", 
            JOptionPane.ERROR_MESSAGE);
        return;
    }

    Connection conn = null;
    PreparedStatement pstmtCheckBooking = null;
    PreparedStatement pstmtInsertRefund = null;
    ResultSet rs = null;

    try {
        conn = DatabaseConnection.connect();
        if (conn == null) return;

        
        String checkBookingSql = "SELECT total_price, payment_status FROM booking " +
                               "WHERE booking_id = ? AND customer_id = ?";
        pstmtCheckBooking = conn.prepareStatement(checkBookingSql);
        pstmtCheckBooking.setString(1, bookingId);
        pstmtCheckBooking.setString(2, loggedInCustomer.getUserID());
        rs = pstmtCheckBooking.executeQuery();

        if (!rs.next()) {
            JOptionPane.showMessageDialog(this, 
                "Booking ID not found or doesn't belong to your account.", 
                "Invalid Booking", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        double bookingTotal = rs.getDouble("total_price");
        String paymentStatus = rs.getString("payment_status");

        
        if (!"Completed".equalsIgnoreCase(paymentStatus)) {
            JOptionPane.showMessageDialog(this, 
                "Only completed bookings can be refunded. Current status: " + paymentStatus, 
                "Invalid Status", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        
        if (amount > bookingTotal) {
            JOptionPane.showMessageDialog(this, 
                String.format("Amount cannot exceed booking total (%.2f)", bookingTotal), 
                "Invalid Amount", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        
        String insertRefundSql = "INSERT INTO RefundRequests " +
                               "(booking_id, customer_id, Amount, RequestDate, Status, reason) " +
                               "VALUES (?, ?, ?, NOW(), 'Pending', ?)";
        pstmtInsertRefund = conn.prepareStatement(insertRefundSql);
        pstmtInsertRefund.setString(1, bookingId);
        pstmtInsertRefund.setString(2, loggedInCustomer.getUserID());
        pstmtInsertRefund.setDouble(3, amount);
        pstmtInsertRefund.setString(4, reason);

        int rowsAffected = pstmtInsertRefund.executeUpdate();
        if (rowsAffected > 0) {
            
            String updateBookingSql = "UPDATE booking SET payment_status = 'Refund Requested' " +
                                    "WHERE booking_id = ?";
            try (PreparedStatement pstmtUpdate = conn.prepareStatement(updateBookingSql)) {
                pstmtUpdate.setString(1, bookingId);
                pstmtUpdate.executeUpdate();
            }

            
            this.addNotification("Refund request submitted for Booking ID: " + bookingId + 
                               ". Amount: " + String.format("%.2f", amount) + 
                               ". Waiting for agent review.");

            
            refundConfirmationLabel.setText("<html>Refund request submitted!<br>" +
                                          "Booking: " + bookingId + "<br>" +
                                          "Amount: " + String.format("%.2f", amount) + "</html>");
            
            JOptionPane.showMessageDialog(this,
                "Refund request submitted successfully.",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
logSuspiciousActivity("Refund request submitted for booking ID: " + bookingId);
currentAnomalyCount++;
if (currentAnomalyCount >= DEEPFAKE_THRESHOLD_ANOMALY_COUNT) {
    addNotification("Your account has triggered multiple suspicious actions and is being monitored.");
}

            
            refundBookingIdField.setText("");
            refundReasonField.setText("");
            refundAmountField.setText("");
        }
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this,
            "Error processing refund request: " + ex.getMessage(),
            "Database Error",
            JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
    } finally {
        try {
            if (rs != null) rs.close();
            if (pstmtCheckBooking != null) pstmtCheckBooking.close();
            if (pstmtInsertRefund != null) pstmtInsertRefund.close();
            if (conn != null) conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}

    
    private void loadPastBookedFlights() {
        pastBookedFlightsComboBox.removeAllItems();
        if (loggedInCustomer == null) {
            return;
        }

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.connect();
            if (conn == null) return;

            
            String sql = "SELECT DISTINCT f.flight_num, f.departure_time " + 
                         "FROM booking b " +
                         "JOIN flight f ON b.flight_id = f.flight_id " +
                         "WHERE b.customer_id = ? AND f.departure_time < NOW() " +
                         "ORDER BY f.departure_time DESC";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, loggedInCustomer.getUserID());
            rs = pstmt.executeQuery();

            while (rs.next()) {
                String flightNumber = rs.getString("flight_num"); 
                Date departureTime = rs.getTimestamp("departure_time");
                pastBookedFlightsComboBox.addItem(flightNumber + " (" + new SimpleDateFormat("yyyy-MM-dd").format(departureTime) + ")");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading past flights: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
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

    
    private void submitFeedback() {
        String selectedFlightFromCombo = (String) pastBookedFlightsComboBox.getSelectedItem();
        String flightNumber = rateFlightNumberField.getText().trim();
        int rating = (Integer) rateStarsSpinner.getValue();
        String review = rateReviewArea.getText().trim();

        if (flightNumber.isEmpty() && selectedFlightFromCombo == null) {
            JOptionPane.showMessageDialog(this, "Please enter a flight number or select a past flight.", "Missing Information", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (review.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please write a review.", "Missing Information", JOptionPane.WARNING_MESSAGE);
            return;
        }

        
        if (selectedFlightFromCombo != null && !selectedFlightFromCombo.isEmpty()) {
            
            int endIndex = selectedFlightFromCombo.indexOf(" (");
            if (endIndex != -1) {
                flightNumber = selectedFlightFromCombo.substring(0, endIndex);
            } else {
                flightNumber = selectedFlightFromCombo; 
            }
        }


        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String flightId = null;

        try {
            conn = DatabaseConnection.connect();
            if (conn == null) return;

            
            String getFlightIdSql = "SELECT flight_id FROM flight WHERE flight_num = ?";
            pstmt = conn.prepareStatement(getFlightIdSql);
            pstmt.setString(1, flightNumber);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                flightId = rs.getString("flight_id");
            } else {
                JOptionPane.showMessageDialog(this, "Flight number not found.", "Invalid Flight", JOptionPane.ERROR_MESSAGE);
                return;
            }
            rs.close();
            pstmt.close();

            
            String feedbackId = "FEED" + UUID.randomUUID().toString().substring(0, 8);
            
            String sqlInsertFeedback = "INSERT INTO feedback (feedback_id, customer_id, flight_id, rating, comment, created_at) VALUES (?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sqlInsertFeedback);
            pstmt.setString(1, feedbackId);
            pstmt.setString(2, loggedInCustomer.getUserID()); 
            pstmt.setString(3, flightId);
            pstmt.setInt(4, rating);
            pstmt.setString(5, review);
            pstmt.setTimestamp(6, new Timestamp(System.currentTimeMillis()));

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Feedback submitted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                this.addNotification("New feedback submitted for Flight: " + flightNumber);
                
                rateFlightNumberField.setText("");
                rateStarsSpinner.setValue(3);
                rateReviewArea.setText("");
                pastBookedFlightsComboBox.setSelectedIndex(-1); 
            } else {
                JOptionPane.showMessageDialog(this, "Failed to submit feedback.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error submitting feedback: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
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


  
    public void addNotification(String message) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DatabaseConnection.connect();
            if (conn == null) return;

            String notificationId = "NOTIF" + UUID.randomUUID().toString().substring(0, 8);
            String sql = "INSERT INTO notifications (notification_id, user_id, message, notification_time, is_read, sender_role) VALUES (?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, notificationId);
            pstmt.setString(2, loggedInCustomer.getUserID());
            pstmt.setString(3, message);
            pstmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            pstmt.setBoolean(5, false); 
            pstmt.setString(6, "System"); 
            pstmt.executeUpdate();
            loadNotifications(); 
        } catch (SQLException ex) {
            System.err.println("Error adding notification: " + ex.getMessage());
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

   
    private void loadNotifications() {
        notificationArea.setText(""); 
        if (loggedInCustomer == null) {
            return;
        }

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.connect();
            if (conn == null) return;

            String sql = "SELECT message, notification_time, is_read FROM notifications WHERE user_id = ? ORDER BY notification_time DESC";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, loggedInCustomer.getUserID());
            rs = pstmt.executeQuery();

            while (rs.next()) {
                String message = rs.getString("message");
                Timestamp notificationTime = rs.getTimestamp("notification_time");
                boolean isRead = rs.getBoolean("is_read");

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String status = isRead ? "" : "[UNREAD] ";
                notificationArea.append(status + sdf.format(notificationTime) + ": " + message + "\n");
            }
        } catch (SQLException ex) {
            System.err.println("Error loading notifications: " + ex.getMessage());
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

    /**
     * Marks all unread notifications for the logged-in customer as read in the database.
     * Uses: `notifications` table.
     */
    private void markAllNotificationsRead() {
        if (loggedInCustomer == null) {
            JOptionPane.showMessageDialog(this, "No customer logged in.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DatabaseConnection.connect();
            if (conn == null) return;

            String sql = "UPDATE notifications SET is_read = TRUE WHERE user_id = ? AND is_read = FALSE";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, loggedInCustomer.getUserID());
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, rowsAffected + " notifications marked as read.", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadNotifications(); 
            } else {
                JOptionPane.showMessageDialog(this, "No unread notifications to mark as read.", "No Changes", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error marking notifications as read: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
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

   
    private void loadPaymentMethods() {
        paymentMethodsTableModel.setRowCount(0); 
        if (loggedInCustomer == null) {
            return;
        }

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.connect();
            if (conn == null) return;

            String sql = "SELECT method_id, method_type, card_last_four_digits, expiry_date, card_holder_name, is_default FROM payment_methods WHERE user_id = ? ORDER BY is_default DESC, created_at DESC";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, loggedInCustomer.getUserID());
            rs = pstmt.executeQuery();

            while (rs.next()) {
                String methodId = rs.getString("method_id");
                String methodType = rs.getString("method_type");
                String lastFour = rs.getString("card_last_four_digits");
                String expiry = rs.getString("expiry_date");
                String holderName = rs.getString("card_holder_name");
                boolean isDefault = rs.getBoolean("is_default");

                paymentMethodsTableModel.addRow(new Object[]{
                    methodId,
                    methodType,
                    lastFour,
                    expiry,
                    holderName,
                    isDefault ? "(Default)" : ""
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading payment methods: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
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
    
    
    private boolean processBookingPayment(Booking booking) {
        
        
        return false; 
    }  
    
    
private void payForBooking() {
    int selectedRow = myBookingsTable.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Please select a booking to pay for.", "No Selection", JOptionPane.WARNING_MESSAGE);
        return;
    }

    String bookingId = (String) myBookingsTableModel.getValueAt(selectedRow, 0);
    String currentStatus = (String) myBookingsTableModel.getValueAt(selectedRow, 5);
    double amount = (Double) myBookingsTableModel.getValueAt(selectedRow, 6);

    if ("Completed".equalsIgnoreCase(currentStatus)) {
        JOptionPane.showMessageDialog(this, "This booking is already paid.", "Already Paid", JOptionPane.INFORMATION_MESSAGE);
        return;
    }

    if (!"Pending".equalsIgnoreCase(currentStatus)) {
        JOptionPane.showMessageDialog(this, "This booking cannot be paid. Current status: " + currentStatus, "Invalid Status", JOptionPane.WARNING_MESSAGE);
        return;
    }

    try (Connection conn = DatabaseConnection.connect()) {
        if (conn == null) {
            JOptionPane.showMessageDialog(this, "Database connection failed.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        
        String sql = "SELECT method_id, method_type, card_last_four_digits FROM payment_methods WHERE user_id = ? ORDER BY is_default DESC, created_at DESC LIMIT 1";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, loggedInCustomer.getUserID());
            ResultSet rs = pstmt.executeQuery();

            if (!rs.next()) {
                JOptionPane.showMessageDialog(this, "No payment method found. Please add one in 'Manage Payment'.", "No Payment Method", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String methodType = rs.getString("method_type");
            String lastFour = rs.getString("card_last_four_digits");

            String paymentId = "PAY" + UUID.randomUUID().toString().substring(0, 8);
            String transactionId = "TXN" + UUID.randomUUID().toString().substring(0, 8);
            LocalDateTime now = LocalDateTime.now();

            conn.setAutoCommit(false);

            
            String insertPayment = "INSERT INTO payment (payment_id, booking_id, amount, payment_date, payment_method, payment_status, transaction_id, card_last_four_digits) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement insertStmt = conn.prepareStatement(insertPayment)) {
                insertStmt.setString(1, paymentId);
                insertStmt.setString(2, bookingId);
                insertStmt.setDouble(3, amount);
                insertStmt.setTimestamp(4, Timestamp.valueOf(now));
                insertStmt.setString(5, methodType);
                insertStmt.setString(6, "Completed");
                insertStmt.setString(7, transactionId);
                insertStmt.setString(8, lastFour);
                insertStmt.executeUpdate();
            }

            
            String updateBooking = "UPDATE booking SET payment_status = 'Completed' WHERE booking_id = ?";
            try (PreparedStatement updateStmt = conn.prepareStatement(updateBooking)) {
                updateStmt.setString(1, bookingId);
                updateStmt.executeUpdate();
            }

            conn.commit();
            myBookingsTableModel.setValueAt("Completed", selectedRow, 5);
            JOptionPane.showMessageDialog(this, "Payment completed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            this.addNotification("Payment completed for Booking ID: " + bookingId);

        }
    } catch (SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error processing payment: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
    }
}



private void generateTicket(String bookingId) {
    try (Connection conn = DatabaseConnection.connect()) {
        
        String sql = "SELECT b.*, f.flight_num, f.departure_time FROM booking b JOIN flight f ON b.flight_id = f.flight_id WHERE b.booking_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, bookingId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                
                String checkTicketSql = "SELECT COUNT(*) FROM ticket WHERE booking_id = ?";
                try (PreparedStatement checkTicketPstmt = conn.prepareStatement(checkTicketSql)) {
                    checkTicketPstmt.setString(1, bookingId);
                    try (ResultSet ticketCountRs = checkTicketPstmt.executeQuery()) {
                        if (ticketCountRs.next() && ticketCountRs.getInt(1) > 0) {
                            
                            String updateTicketStatusSql = "UPDATE ticket SET ticket_status = 'Confirmed' WHERE booking_id = ?";
                            try (PreparedStatement updateTicketPstmt = conn.prepareStatement(updateTicketStatusSql)) {
                                updateTicketPstmt.setString(1, bookingId);
                                updateTicketPstmt.executeUpdate();
                                System.out.println("Ticket status updated to Confirmed for booking: " + bookingId);
                            }
                            return; 
                        }
                    }
                }

                
                String ticketId = "TICKET-" + UUID.randomUUID().toString().substring(0,8);
                
                String passengerInfoSql = "SELECT p.passenger_id, t.seat_number FROM passenger p JOIN ticket t ON p.passenger_id = t.passenger_id WHERE t.booking_id = ?";
                String passengerId = null;
                String seatNumber = null;
                try(PreparedStatement pstmtPassengerInfo = conn.prepareStatement(passengerInfoSql)) {
                    pstmtPassengerInfo.setString(1, bookingId);
                    try(ResultSet rsPassengerInfo = pstmtPassengerInfo.executeQuery()) {
                        if (rsPassengerInfo.next()) {
                            passengerId = rsPassengerInfo.getString("passenger_id");
                            seatNumber = rsPassengerInfo.getString("seat_number");
                        }
                    }
                }
                
                if (passengerId == null || seatNumber == null) {
                    System.err.println("Could not find passenger or seat info for booking: " + bookingId);
                    return;
                }

                
                try (PreparedStatement pstmt2 = conn.prepareStatement(
                        "INSERT INTO ticket (ticket_id, booking_id, passenger_id, seat_number, " +
                        "boarding_time, gate_number, ticket_status, barcode) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {
                    pstmt2.setString(1, ticketId);
                    pstmt2.setString(2, bookingId);
                    pstmt2.setString(3, passengerId);
                    pstmt2.setString(4, seatNumber);
                    
                    Timestamp boardingTime = new Timestamp(rs.getTimestamp("departure_time").getTime() - (60 * 60 * 1000));
                    pstmt2.setTimestamp(5, boardingTime);
                    pstmt2.setString(6, "GATE" + (int)(Math.random() * 10 + 1)); 
                    pstmt2.setString(7, "Confirmed");
                    pstmt2.setString(8, "BAR-" + UUID.randomUUID().toString());
                    pstmt2.executeUpdate();
                    System.out.println("New ticket generated for booking: " + bookingId);
                }
            }
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
        System.err.println("Error generating ticket: " + ex.getMessage());
    }
}


private boolean isBookingEligibleForRefund(String bookingId) {
    if (loggedInCustomer == null || bookingId == null || bookingId.isEmpty()) {
        return false;
    }

    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    
    try {
        conn = DatabaseConnection.connect();
        if (conn == null) return false;
        
        String sql = "SELECT b.payment_status, " +
                     "(SELECT COUNT(*) FROM RefundRequests r WHERE r.booking_id = b.booking_id AND r.Status = 'Pending') as pending_refunds " +
                     "FROM booking b " +
                     "WHERE b.booking_id = ? AND b.customer_id = ?";
        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, bookingId);
        pstmt.setString(2, loggedInCustomer.getUserID());
        rs = pstmt.executeQuery();
        
        if (rs.next()) {
            String status = rs.getString("payment_status");
            int pendingRefunds = rs.getInt("pending_refunds");
            return "Completed".equalsIgnoreCase(status) && pendingRefunds == 0;
        }
    } catch (SQLException ex) {
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
    return false;
}
private void logSuspiciousActivity(String detail) {
    if (loggedInCustomer == null) return;
    try (Connection conn = DatabaseConnection.connect();
         PreparedStatement pstmt = conn.prepareStatement(
             "INSERT INTO deepfakesuspiciousactivity (log_id, user_id, activity_detail) VALUES (?, ?, ?)")) {
        pstmt.setString(1, "LOG" + UUID.randomUUID().toString().substring(0, 8));
        pstmt.setString(2, loggedInCustomer.getUserID());
        pstmt.setString(3, detail);
        pstmt.executeUpdate();
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
}
private boolean isUserFlaggedAsDeepfake(String userId) {
    try (Connection conn = DatabaseConnection.connect();
         PreparedStatement pstmt = conn.prepareStatement(
             "SELECT is_verified FROM deepfakeuser WHERE original_user_id = ?")) {
        pstmt.setString(1, userId);
        ResultSet rs = pstmt.executeQuery();
        return rs.next() && !rs.getBoolean("is_verified");
    } catch (SQLException ex) {
        ex.printStackTrace();
        return false;
    }
}

}
