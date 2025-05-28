
package airprort_system;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Vector;
import java.util.Date; 

public class AgentDashboardPage extends JPanel {

    
    private static class FlightDisplayItem {
        String flightId;
        String displayText;
        double fareEconomy;
        double fareBusiness;
        double fareFirstClass;
        int availableSeats; 

        FlightDisplayItem(String flightId, String displayText, double fareEconomy, double fareBusiness, double fareFirstClass, int availableSeats) {
            this.flightId = flightId;
            this.displayText = displayText;
            this.fareEconomy = fareEconomy;
            this.fareBusiness = fareBusiness;
            this.fareFirstClass = fareFirstClass;
            this.availableSeats = availableSeats;
        }

        @Override
        public String toString() {
            return displayText;
        }
    }

   
    private CardLayout cardLayout;
    private JPanel parentPanel; 
    private CardLayout contentCardLayout; 
    private JPanel contentCards; 
    private Agent loggedInAgent;
    private String currentAgentId = "";
    private double currentAgentCommissionRate = 0.0;
    private JLabel agentInfoLabel;

    
   
    private JComboBox<String> flightSearchCriteriaComboBox;
    private JTextField searchInputTextField;
    private JButton searchFlightsButton;
    private JTable flightsResultsTable;
    private DefaultTableModel flightsResultsTableModel;

    private JComboBox<FlightDisplayItem> flightSelectionComboBox;
    private JTextField customerSearchInput;
    private JButton searchCustomerButton;
    private JButton registerNewCustomerButton;
    private JLabel foundCustomerLabel;
    private JTable passengersTable;
    private DefaultTableModel passengersTableModel;
    private JButton addPassengerButton;
    private JButton removePassengerButton; 
    private JComboBox<String> seatClassComboBox;
    private JTextArea specialRequestsTextArea;
    private JRadioButton customerPaysRadioButton;
    private JRadioButton agentPaysRadioButton;
    private ButtonGroup paymentMethodGroup;
    private JLabel totalPriceLabel;
    private JLabel agentCommissionLabel;
    private JButton submitBookingButton;
    private JLabel availableSeatsLabel;

    
    private JComboBox<String> modifySearchCriteriaComboBox;
    private JTextField modifySearchInputTextField;
    private JButton modifySearchButton;
    private JLabel modifyBookingIdLabel;
    private JLabel modifyCustomerIdLabel;
    private JLabel modifyFlightIdLabel;
    private JComboBox<String> modifySeatClassComboBox;
    private JTextArea modifySpecialRequestsTextArea;
    private JLabel modifyCurrentPriceLabel;
    private JTextField modifyManualPriceTextField;
    private JButton modifyRecalculateButton;
    private JLabel modifyNewPriceLabel;
    private JButton updateBookingButton;
    private JTable modifyTicketsTable; 
    private DefaultTableModel modifyTicketsTableModel;
    private JButton saveTicketChangesButton; 

    
    private JComboBox<String> viewCustomerSearchCriteriaComboBox;
    private JTextField viewCustomerSearchInputTextField;
    private JButton viewCustomerSearchButton;
    private JTextArea customerProfileTextArea;
    private JTable customerBookingHistoryTable;
    private DefaultTableModel customerBookingHistoryTableModel;
    private JTable customerPaymentHistoryTable;
    private DefaultTableModel customerPaymentHistoryTableModel;
    private JButton blockCustomerButton;

    
    private JTextField reportBookingIdFilter;
    private JTextField reportCustomerIdFilter;
    private JTextField reportFlightIdFilter;
    private JSpinner reportStartDateSpinner;
    private JSpinner reportEndDateSpinner;
    private JButton generateReportButton;
    private JTable reportResultsTable;
    private DefaultTableModel reportResultsTableModel;
    private JButton exportReportButton;

    
    private JComboBox<String> discountSearchCriteriaComboBox;
    private JTextField discountSearchInputTextField;
    private JButton discountSearchButton;
    private JLabel discountBookingIdLabel;
    private JLabel discountCustomerIdLabel;
    private JLabel discountFlightIdLabel;
    private JLabel discountCurrentPriceLabel;
    private JComboBox<String> discountTypeComboBox;
    private JTextField discountValueTextField;
    private JTextArea discountReasonTextArea;
    private JButton discountPreviewButton;
    private JLabel discountNewPriceLabel;
    private JButton applyDiscountButton;

    
    private JTable refundRequestsTable;
    private DefaultTableModel refundRequestsTableModel;
    private JButton refreshRefundsButton;

    
    private String viewedCustomerId = null;
    private String selectedFlightIdForBooking = null;
    private String selectedCustomerIdForBooking = null;
    private double selectedFlightFareEconomy = 0.0;
    private double selectedFlightFareBusiness = 0.0;
    private double selectedFlightFareFirstClass = 0.0;
    private int selectedFlightAvailableSeats = 0; 
    private String currentBookingIdToModify = null;
    private double currentBookingPrice = 0.0;
    
    private double modifyFlightFareEconomy = 0.0;
    private double modifyFlightFareBusiness = 0.0;
    private double modifyFlightFareFirstClass = 0.0;
    private String currentBookingIdForDiscount = null;
    private String currentCustomerIdForDiscount = null;
    private double currentBookingPriceForDiscount = 0.0;

    
    public AgentDashboardPage(CardLayout cardLayout, JPanel parentPanel) {
        this.cardLayout = cardLayout;
        this.parentPanel = parentPanel;
        setLayout(new BorderLayout());
        setName("AGENT_DASHBOARD");
        setBackground(new Color(30, 40, 60));
        initComponentsWithStyle();
        addListeners();
        setInitialComponentStates();
        loadPendingRefundRequests();
        loadFlightsForBooking(); 
    }

    
    public void setLoggedInAgent(Agent agent) {
        this.loggedInAgent = agent;
        if (agent != null) {
            this.currentAgentId = agent.getAgentId();
            this.currentAgentCommissionRate = agent.getCommissionRate();
            agentInfoLabel.setText("Welcome, Agent: " + agent.getUsername() + " (ID: " + currentAgentId + ")");
            loadPendingRefundRequests();
        } else {
            this.currentAgentId = "";
            this.currentAgentCommissionRate = 0.0;
            agentInfoLabel.setText("Welcome, Agent: Not Logged In");
        }
    }

    
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(160, 35));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(70, 130, 180));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 255, 255, 120), 1),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(100, 160, 210));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(70, 130, 180));
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
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(200, 30));
        field.setBackground(new Color(60, 70, 90));
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 110, 130), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return field;
    }

    private JComboBox createStyledComboBox() { 
        JComboBox comboBox = new JComboBox<>();
        comboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        comboBox.setPreferredSize(new Dimension(200, 30));
        comboBox.setBackground(new Color(60, 70, 90));
        comboBox.setForeground(Color.WHITE);
        
        comboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JComponent comp = (JComponent) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                comp.setBackground(isSelected ? new Color(80, 90, 110) : new Color(60, 70, 90));
                comp.setForeground(Color.WHITE);
                if (value instanceof FlightDisplayItem) {
                    setText(((FlightDisplayItem) value).displayText);
                } else if (value != null) {
                    setText(value.toString());
                }
                return comp;
            }
        });
        return comboBox;
    }

    private JTextArea createStyledTextArea() {
        JTextArea area = new JTextArea(5, 20);
        area.setFont(new Font("Arial", Font.PLAIN, 14));
        area.setBackground(new Color(60, 70, 90));
        area.setForeground(Color.WHITE);
        area.setCaretColor(Color.WHITE);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 110, 130), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return area;
    }

    private JTable createStyledTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.setRowHeight(25);
        table.setBackground(new Color(60, 70, 90));
        table.setForeground(Color.WHITE);
        table.setGridColor(new Color(80, 90, 110));
        table.setSelectionBackground(new Color(70, 130, 180));
        table.setSelectionForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(50, 60, 80));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setReorderingAllowed(false);
        return table;
    }

    private JScrollPane createStyledScrollPane(Component view) {
        JScrollPane scrollPane = new JScrollPane(view);
        scrollPane.getViewport().setBackground(new Color(40, 50, 70));
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(80, 90, 110)));
        scrollPane.getVerticalScrollBar().setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override protected void configureScrollBarColors() { this.thumbColor = new Color(70, 130, 180); this.trackColor = new Color(50, 60, 80); }
            @Override protected JButton createDecreaseButton(int o) { return createZeroButton(); }
            @Override protected JButton createIncreaseButton(int o) { return createZeroButton(); }
            private JButton createZeroButton() { JButton b = new JButton(); Dimension d = new Dimension(0,0); b.setPreferredSize(d); b.setMinimumSize(d); b.setMaximumSize(d); return b; }
        });
         scrollPane.getHorizontalScrollBar().setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override protected void configureScrollBarColors() { this.thumbColor = new Color(70, 130, 180); this.trackColor = new Color(50, 60, 80); }
            @Override protected JButton createDecreaseButton(int o) { return createZeroButton(); }
            @Override protected JButton createIncreaseButton(int o) { return createZeroButton(); }
            private JButton createZeroButton() { JButton b = new JButton(); Dimension d = new Dimension(0,0); b.setPreferredSize(d); b.setMinimumSize(d); b.setMaximumSize(d); return b; }
        });
        return scrollPane;
    }

    private JPanel createContentPanel(String title) {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(new Color(40, 50, 70));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(80, 90, 110)), " " + title + " ",
                TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
                new Font("Arial", Font.BOLD, 16), Color.WHITE
            ),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        return panel;
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        label.setForeground(Color.WHITE);
        return label;
    }

    private JSpinner createStyledSpinner(SpinnerModel model) {
        JSpinner spinner = new JSpinner(model);
        spinner.setFont(new Font("Arial", Font.PLAIN, 14));
        spinner.setPreferredSize(new Dimension(120, 30)); 
        spinner.getEditor().getComponent(0).setBackground(new Color(60, 70, 90));
        spinner.getEditor().getComponent(0).setForeground(Color.WHITE);
        ((JSpinner.DefaultEditor)spinner.getEditor()).getTextField().setCaretColor(Color.WHITE);
        spinner.setBorder(BorderFactory.createLineBorder(new Color(100, 110, 130)));
        return spinner;
    }

    
    private void initComponentsWithStyle() {
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(40, 50, 70));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        agentInfoLabel = createStyledLabel("Welcome, Agent: Not Logged In");
        agentInfoLabel.setFont(new Font("Arial", Font.BOLD, 18));
        headerPanel.add(agentInfoLabel, BorderLayout.WEST);

        JButton logoutButton = createStyledButton("Logout");
        logoutButton.addActionListener(e -> {
            
            cardLayout.show(parentPanel, "LOGIN");
            setLoggedInAgent(null); 
        });
        headerPanel.add(logoutButton, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        
        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(new Color(50, 60, 80));
        sidebarPanel.setPreferredSize(new Dimension(200, 0));
        sidebarPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        String[] buttonLabels = {"Search Flights", "Create Booking", "Modify Booking", "View/Block Customer", "Generate Reports", "Apply Discount", "Refund Requests"};
        String[] cardNames = {"SEARCH_FLIGHTS", "CREATE_BOOKING", "MODIFY_BOOKING", "VIEW_BLOCK_CUSTOMER", "GENERATE_REPORTS", "APPLY_DISCOUNT", "REFUND_REQUESTS"};

        sidebarPanel.add(Box.createVerticalStrut(20));
        for (int i = 0; i < buttonLabels.length; i++) {
            JButton button = createSidebarButton(buttonLabels[i]);
            final String cardName = cardNames[i];
            button.addActionListener(e -> contentCardLayout.show(contentCards, cardName));
            sidebarPanel.add(button);
            sidebarPanel.add(Box.createVerticalStrut(10));
        }
        sidebarPanel.add(Box.createVerticalGlue()); 
        add(sidebarPanel, BorderLayout.WEST);

        
        contentCardLayout = new CardLayout();
        contentCards = new JPanel(contentCardLayout);
        contentCards.setBackground(new Color(40, 50, 70));

        
        contentCards.add(createSearchFlightsPanel(), "SEARCH_FLIGHTS");
        contentCards.add(createCreateBookingPanel(), "CREATE_BOOKING");
        contentCards.add(createModifyBookingPanel(), "MODIFY_BOOKING");
        contentCards.add(createViewBlockCustomerPanel(), "VIEW_BLOCK_CUSTOMER");
        contentCards.add(createGenerateReportsPanel(), "GENERATE_REPORTS");
        contentCards.add(createApplyDiscountPanel(), "APPLY_DISCOUNT");
        contentCards.add(createRefundRequestsPanel(), "REFUND_REQUESTS");

        add(contentCards, BorderLayout.CENTER);
        contentCardLayout.show(contentCards, "SEARCH_FLIGHTS"); 
    }

    

    private JPanel createSearchFlightsPanel() {
        JPanel panel = createContentPanel("Search Flights");
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topPanel.setOpaque(false);

        flightSearchCriteriaComboBox = createStyledComboBox();
        flightSearchCriteriaComboBox.addItem("Flight ID");
        flightSearchCriteriaComboBox.addItem("Flight Number");
        flightSearchCriteriaComboBox.addItem("Departure Airport Code");
        flightSearchCriteriaComboBox.addItem("Arrival Airport Code");
        flightSearchCriteriaComboBox.addItem("Departure Country");
        flightSearchCriteriaComboBox.addItem("Arrival Country");

        searchInputTextField = createStyledTextField();
        searchFlightsButton = createStyledButton("Search");

        topPanel.add(createStyledLabel("Search By:"));
        topPanel.add(flightSearchCriteriaComboBox);
        topPanel.add(createStyledLabel("Value:"));
        topPanel.add(searchInputTextField);
        topPanel.add(searchFlightsButton);

        panel.add(topPanel, BorderLayout.NORTH);

        flightsResultsTableModel = new DefaultTableModel(new String[]{"Flight ID", "Flight Num", "Dep Airport", "Arr Airport", "Dep Time", "Arr Time", "Aircraft", "Total Seats", "Available Seats", "Status", "Econ Fare", "Bus Fare", "First Fare"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        flightsResultsTable = createStyledTable(flightsResultsTableModel);
        panel.add(createStyledScrollPane(flightsResultsTable), BorderLayout.CENTER);

        return panel;
    }

    private JPanel createCreateBookingPanel() {
        JPanel panel = createContentPanel("Create Booking");
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        
        gbc.gridx = 0; gbc.gridy = 0; panel.add(createStyledLabel("Select Flight:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3; flightSelectionComboBox = (JComboBox<FlightDisplayItem>) createStyledComboBox(); panel.add(flightSelectionComboBox, gbc);
        gbc.gridx = 4; gbc.gridwidth = 1; availableSeatsLabel = createStyledLabel("Available Seats: -"); panel.add(availableSeatsLabel, gbc);

        
        gbc.gridx = 0; gbc.gridy = 1; panel.add(createStyledLabel("Search Customer (ID/Email/Phone):"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 1; customerSearchInput = createStyledTextField(); panel.add(customerSearchInput, gbc);
        gbc.gridx = 2; searchCustomerButton = createStyledButton("Search"); panel.add(searchCustomerButton, gbc);
        gbc.gridx = 3; registerNewCustomerButton = createStyledButton("Register New"); panel.add(registerNewCustomerButton, gbc);
        gbc.gridx = 4; foundCustomerLabel = createStyledLabel("Selected Customer: None"); panel.add(foundCustomerLabel, gbc);

        
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 5; gbc.fill = GridBagConstraints.BOTH; gbc.weightx = 1.0; gbc.weighty = 0.4;
        passengersTableModel = new DefaultTableModel(new String[]{"First Name", "Last Name", "DOB (YYYY-MM-DD)", "Passport No.", "Nationality", "Gender", "Meal Pref.", "Special Assist."}, 0);
        passengersTable = createStyledTable(passengersTableModel);
        panel.add(createStyledScrollPane(passengersTable), gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 0; gbc.weighty = 0;

        
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 1; addPassengerButton = createStyledButton("Add Passenger"); panel.add(addPassengerButton, gbc);
        gbc.gridx = 1; removePassengerButton = createStyledButton("Remove Selected"); panel.add(removePassengerButton, gbc);

        
        gbc.gridx = 0; gbc.gridy = 4; panel.add(createStyledLabel("Seat Class:"), gbc);
        gbc.gridx = 1; seatClassComboBox = (JComboBox<String>) createStyledComboBox();
        seatClassComboBox.addItem("Economy"); seatClassComboBox.addItem("Business"); seatClassComboBox.addItem("First Class");
        panel.add(seatClassComboBox, gbc);
        gbc.gridx = 2; panel.add(createStyledLabel("Special Requests:"), gbc);
        gbc.gridx = 3; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.BOTH; gbc.weighty = 0.1;
        specialRequestsTextArea = createStyledTextArea();
        panel.add(createStyledScrollPane(specialRequestsTextArea), gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weighty = 0;

        
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 1; panel.add(createStyledLabel("Payment By:"), gbc);
        customerPaysRadioButton = new JRadioButton("Customer"); customerPaysRadioButton.setSelected(true); customerPaysRadioButton.setForeground(Color.WHITE); customerPaysRadioButton.setOpaque(false);
        agentPaysRadioButton = new JRadioButton("Agent (Commission)"); agentPaysRadioButton.setForeground(Color.WHITE); agentPaysRadioButton.setOpaque(false);
        paymentMethodGroup = new ButtonGroup(); paymentMethodGroup.add(customerPaysRadioButton); paymentMethodGroup.add(agentPaysRadioButton);
        JPanel paymentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); paymentPanel.setOpaque(false);
        paymentPanel.add(customerPaysRadioButton); paymentPanel.add(agentPaysRadioButton);
        gbc.gridx = 1; gbc.gridwidth = 1; panel.add(paymentPanel, gbc);
        gbc.gridx = 2; totalPriceLabel = createStyledLabel("Total Price: $0.00"); panel.add(totalPriceLabel, gbc);
        gbc.gridx = 3; agentCommissionLabel = createStyledLabel("Agent Commission: $0.00"); panel.add(agentCommissionLabel, gbc);

        
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 5; gbc.anchor = GridBagConstraints.CENTER;
        submitBookingButton = createStyledButton("Submit Booking / Add to Waitlist");
        panel.add(submitBookingButton, gbc);

        return panel;
    }

    private JPanel createModifyBookingPanel() {
        JPanel panel = createContentPanel("Modify Booking");
        panel.setLayout(new BorderLayout(15, 15));

        
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topPanel.setOpaque(false);
        modifySearchCriteriaComboBox = (JComboBox<String>) createStyledComboBox();
        modifySearchCriteriaComboBox.addItem("Booking ID");
        modifySearchCriteriaComboBox.addItem("Customer ID");
        modifySearchCriteriaComboBox.addItem("Flight ID");
        modifySearchInputTextField = createStyledTextField();
        modifySearchButton = createStyledButton("Search Booking");
        topPanel.add(createStyledLabel("Search By:"));
        topPanel.add(modifySearchCriteriaComboBox);
        topPanel.add(createStyledLabel("Value:"));
        topPanel.add(modifySearchInputTextField);
        topPanel.add(modifySearchButton);
        panel.add(topPanel, BorderLayout.NORTH);

        
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        
        gbc.gridx = 0; gbc.gridy = 0; modifyBookingIdLabel = createStyledLabel("Booking ID: -"); centerPanel.add(modifyBookingIdLabel, gbc);
        gbc.gridx = 1; modifyCustomerIdLabel = createStyledLabel("Customer ID: -"); centerPanel.add(modifyCustomerIdLabel, gbc);
        gbc.gridx = 2; modifyFlightIdLabel = createStyledLabel("Flight ID: -"); centerPanel.add(modifyFlightIdLabel, gbc);

        
        gbc.gridx = 0; gbc.gridy = 1; centerPanel.add(createStyledLabel("Seat Class:"), gbc);
        gbc.gridx = 1; modifySeatClassComboBox = (JComboBox<String>) createStyledComboBox();
        modifySeatClassComboBox.addItem("Economy"); modifySeatClassComboBox.addItem("Business"); modifySeatClassComboBox.addItem("First Class");
        centerPanel.add(modifySeatClassComboBox, gbc);
        gbc.gridx = 2; centerPanel.add(createStyledLabel("Special Requests:"), gbc);
        gbc.gridx = 3; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.BOTH; gbc.weighty = 0.1;
        modifySpecialRequestsTextArea = createStyledTextArea();
        centerPanel.add(createStyledScrollPane(modifySpecialRequestsTextArea), gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weighty = 0; gbc.gridwidth = 1;

        
        gbc.gridx = 0; gbc.gridy = 2; modifyCurrentPriceLabel = createStyledLabel("Current Price: $0.00"); centerPanel.add(modifyCurrentPriceLabel, gbc);
        gbc.gridx = 1; centerPanel.add(createStyledLabel("Manual Price Override:"), gbc);
        gbc.gridx = 2; modifyManualPriceTextField = createStyledTextField(); centerPanel.add(modifyManualPriceTextField, gbc);
        gbc.gridx = 3; modifyRecalculateButton = createStyledButton("Recalculate Price"); centerPanel.add(modifyRecalculateButton, gbc);
        gbc.gridx = 4; modifyNewPriceLabel = createStyledLabel("New Price: $0.00"); centerPanel.add(modifyNewPriceLabel, gbc);

        
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 5; gbc.fill = GridBagConstraints.BOTH; gbc.weightx = 1.0; gbc.weighty = 0.4;
        modifyTicketsTableModel = new DefaultTableModel(new String[]{"Ticket ID", "Passenger ID", "Seat Number", "Status"}, 0);
        modifyTicketsTable = createStyledTable(modifyTicketsTableModel);
        
        modifyTicketsTable.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(createStyledTextField()));
        modifyTicketsTable.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(createStyledComboBox())); 
        centerPanel.add(createStyledScrollPane(modifyTicketsTable), gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 0; gbc.weighty = 0;

        
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        updateBookingButton = createStyledButton("Update Booking Details"); centerPanel.add(updateBookingButton, gbc);
        gbc.gridx = 2; gbc.gridwidth = 3;
        saveTicketChangesButton = createStyledButton("Save Ticket Changes"); centerPanel.add(saveTicketChangesButton, gbc);

        panel.add(centerPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createViewBlockCustomerPanel() {
        JPanel panel = createContentPanel("View / Block Customer");
        panel.setLayout(new BorderLayout(15, 15));

        
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topPanel.setOpaque(false);
        viewCustomerSearchCriteriaComboBox = (JComboBox<String>) createStyledComboBox();
        viewCustomerSearchCriteriaComboBox.addItem("Customer ID");
        viewCustomerSearchCriteriaComboBox.addItem("Email");
        viewCustomerSearchCriteriaComboBox.addItem("Phone");
        viewCustomerSearchCriteriaComboBox.addItem("Passport No.");
        viewCustomerSearchInputTextField = createStyledTextField();
        viewCustomerSearchButton = createStyledButton("Search Customer");
        topPanel.add(createStyledLabel("Search By:"));
        topPanel.add(viewCustomerSearchCriteriaComboBox);
        topPanel.add(createStyledLabel("Value:"));
        topPanel.add(viewCustomerSearchInputTextField);
        topPanel.add(viewCustomerSearchButton);
        panel.add(topPanel, BorderLayout.NORTH);

        
        JSplitPane centerSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        centerSplitPane.setOpaque(false);
        centerSplitPane.setBorder(null);
        centerSplitPane.setResizeWeight(0.3);

        
        JPanel profilePanel = new JPanel(new BorderLayout(5, 5));
        profilePanel.setOpaque(false);
        profilePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(80, 90, 110)), " Customer Profile ", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Arial", Font.BOLD, 13), Color.WHITE));
        customerProfileTextArea = createStyledTextArea();
        customerProfileTextArea.setEditable(false);
        profilePanel.add(createStyledScrollPane(customerProfileTextArea), BorderLayout.CENTER);
        blockCustomerButton = createStyledButton("Request Block Customer");
        blockCustomerButton.setEnabled(false); 
        profilePanel.add(blockCustomerButton, BorderLayout.SOUTH);
        centerSplitPane.setTopComponent(profilePanel);

        
        JSplitPane historySplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        historySplitPane.setOpaque(false);
        historySplitPane.setBorder(null);
        historySplitPane.setResizeWeight(0.5);

        
        customerBookingHistoryTableModel = new DefaultTableModel(new String[]{"Booking ID", "Flight ID", "Date", "Class", "Price", "Status"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        customerBookingHistoryTable = createStyledTable(customerBookingHistoryTableModel);
        JPanel bookingHistoryPanel = new JPanel(new BorderLayout());
        bookingHistoryPanel.setOpaque(false);
        bookingHistoryPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(80, 90, 110)), " Booking History ", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Arial", Font.BOLD, 13), Color.WHITE));
        bookingHistoryPanel.add(createStyledScrollPane(customerBookingHistoryTable), BorderLayout.CENTER);
        historySplitPane.setLeftComponent(bookingHistoryPanel);

        
        customerPaymentHistoryTableModel = new DefaultTableModel(new String[]{"Payment ID", "Booking ID", "Amount", "Date", "Method", "Status"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        customerPaymentHistoryTable = createStyledTable(customerPaymentHistoryTableModel);
        JPanel paymentHistoryPanel = new JPanel(new BorderLayout());
        paymentHistoryPanel.setOpaque(false);
        paymentHistoryPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(80, 90, 110)), " Payment History ", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Arial", Font.BOLD, 13), Color.WHITE));
        paymentHistoryPanel.add(createStyledScrollPane(customerPaymentHistoryTable), BorderLayout.CENTER);
        historySplitPane.setRightComponent(paymentHistoryPanel);

        centerSplitPane.setBottomComponent(historySplitPane);
        panel.add(centerSplitPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createGenerateReportsPanel() {
        JPanel panel = createContentPanel("Generate Booking Reports");
        panel.setLayout(new BorderLayout(15, 15));

        
        JPanel topPanel = new JPanel(new GridBagLayout());
        topPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0; topPanel.add(createStyledLabel("Booking ID:"), gbc);
        gbc.gridx = 1; reportBookingIdFilter = createStyledTextField(); topPanel.add(reportBookingIdFilter, gbc);
        gbc.gridx = 2; topPanel.add(createStyledLabel("Customer ID:"), gbc);
        gbc.gridx = 3; reportCustomerIdFilter = createStyledTextField(); topPanel.add(reportCustomerIdFilter, gbc);
        gbc.gridx = 4; topPanel.add(createStyledLabel("Flight ID:"), gbc);
        gbc.gridx = 5; reportFlightIdFilter = createStyledTextField(); topPanel.add(reportFlightIdFilter, gbc);

        gbc.gridx = 0; gbc.gridy = 1; topPanel.add(createStyledLabel("Start Date:"), gbc);
        gbc.gridx = 1; reportStartDateSpinner = createStyledSpinner(new SpinnerDateModel()); reportStartDateSpinner.setEditor(new JSpinner.DateEditor(reportStartDateSpinner, "yyyy-MM-dd")); topPanel.add(reportStartDateSpinner, gbc);
        gbc.gridx = 2; topPanel.add(createStyledLabel("End Date:"), gbc);
        gbc.gridx = 3; reportEndDateSpinner = createStyledSpinner(new SpinnerDateModel()); reportEndDateSpinner.setEditor(new JSpinner.DateEditor(reportEndDateSpinner, "yyyy-MM-dd")); topPanel.add(reportEndDateSpinner, gbc);
        gbc.gridx = 4; gbc.gridwidth = 2; generateReportButton = createStyledButton("Generate Report"); topPanel.add(generateReportButton, gbc);

        panel.add(topPanel, BorderLayout.NORTH);

        
        reportResultsTableModel = new DefaultTableModel(new String[]{"Booking ID", "Cust ID", "Flight ID", "Date", "Class", "Price", "Status", "Agent ID"}, 0) {
             @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        reportResultsTable = createStyledTable(reportResultsTableModel);
        panel.add(createStyledScrollPane(reportResultsTable), BorderLayout.CENTER);

        
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setOpaque(false);
        exportReportButton = createStyledButton("Export Report (TXT)");
        bottomPanel.add(exportReportButton);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createApplyDiscountPanel() {
        JPanel panel = createContentPanel("Apply Discount");
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        
        gbc.gridx = 0; gbc.gridy = 0; panel.add(createStyledLabel("Search Booking By:"), gbc);
        gbc.gridx = 1; discountSearchCriteriaComboBox = (JComboBox<String>) createStyledComboBox();
        discountSearchCriteriaComboBox.addItem("Booking ID");
        panel.add(discountSearchCriteriaComboBox, gbc);
        gbc.gridx = 2; panel.add(createStyledLabel("Value:"), gbc);
        gbc.gridx = 3; discountSearchInputTextField = createStyledTextField(); panel.add(discountSearchInputTextField, gbc);
        gbc.gridx = 4; discountSearchButton = createStyledButton("Search"); panel.add(discountSearchButton, gbc);

        
        gbc.gridx = 0; gbc.gridy = 1; discountBookingIdLabel = createStyledLabel("Booking ID: -"); panel.add(discountBookingIdLabel, gbc);
        gbc.gridx = 1; discountCustomerIdLabel = createStyledLabel("Customer ID: -"); panel.add(discountCustomerIdLabel, gbc);
        gbc.gridx = 2; discountFlightIdLabel = createStyledLabel("Flight ID: -"); panel.add(discountFlightIdLabel, gbc);
        gbc.gridx = 3; gbc.gridwidth = 2; discountCurrentPriceLabel = createStyledLabel("Current Price: $0.00"); panel.add(discountCurrentPriceLabel, gbc);
        gbc.gridwidth = 1;

        
        gbc.gridx = 0; gbc.gridy = 2; panel.add(createStyledLabel("Discount Type:"), gbc);
        gbc.gridx = 1; discountTypeComboBox = (JComboBox<String>) createStyledComboBox();
        discountTypeComboBox.addItem("Percentage"); discountTypeComboBox.addItem("Fixed Amount");
        panel.add(discountTypeComboBox, gbc);
        gbc.gridx = 2; panel.add(createStyledLabel("Value (% or $):"), gbc);
        gbc.gridx = 3; discountValueTextField = createStyledTextField(); panel.add(discountValueTextField, gbc);

        
        gbc.gridx = 0; gbc.gridy = 3; panel.add(createStyledLabel("Reason:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 4; gbc.fill = GridBagConstraints.BOTH; gbc.weighty = 0.1;
        discountReasonTextArea = createStyledTextArea();
        panel.add(createStyledScrollPane(discountReasonTextArea), gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weighty = 0; gbc.gridwidth = 1;

        
        gbc.gridx = 0; gbc.gridy = 4; discountPreviewButton = createStyledButton("Preview Discount"); panel.add(discountPreviewButton, gbc);
        gbc.gridx = 1; discountNewPriceLabel = createStyledLabel("New Price: $0.00"); panel.add(discountNewPriceLabel, gbc);
        gbc.gridx = 2; gbc.gridwidth = 3; gbc.anchor = GridBagConstraints.EAST;
        applyDiscountButton = createStyledButton("Apply Discount");
        panel.add(applyDiscountButton, gbc);

        return panel;
    }

    private JPanel createRefundRequestsPanel() {
        JPanel panel = createContentPanel("Process Refund Requests");
        panel.setLayout(new BorderLayout(15, 15));

        
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.setOpaque(false);
        refreshRefundsButton = createStyledButton("Refresh List");
        topPanel.add(refreshRefundsButton);
        panel.add(topPanel, BorderLayout.NORTH);

        
        refundRequestsTableModel = new DefaultTableModel(new String[]{"Booking ID", "Cust ID", "Flight ID", "Price", "Request Date", "Reason", "Action"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return column == 6; } 
        };
        refundRequestsTable = createStyledTable(refundRequestsTableModel);

        
        TableColumn actionColumn = refundRequestsTable.getColumnModel().getColumn(6);
        actionColumn.setCellRenderer(new ButtonRenderer("Approve Refund"));
        actionColumn.setCellEditor(new ButtonEditor(new JCheckBox(), "Approve Refund", this::approveRefundAction));

        panel.add(createStyledScrollPane(refundRequestsTable), BorderLayout.CENTER);

        return panel;
    }

    
    private void setInitialComponentStates() {
        
        flightSelectionComboBox.setEnabled(false);
        submitBookingButton.setEnabled(false);
        totalPriceLabel.setText("Total Price: $0.00");
        agentCommissionLabel.setText("Agent Commission: $0.00");
        availableSeatsLabel.setText("Available Seats: -");

        
        modifySeatClassComboBox.setEnabled(false);
        modifySpecialRequestsTextArea.setEnabled(false);
        modifyManualPriceTextField.setEnabled(false);
        modifyRecalculateButton.setEnabled(false);
        updateBookingButton.setEnabled(false);
        saveTicketChangesButton.setEnabled(false);

        
        blockCustomerButton.setEnabled(false);

        
        discountTypeComboBox.setEnabled(false);
        discountValueTextField.setEnabled(false);
        discountReasonTextArea.setEnabled(false);
        discountPreviewButton.setEnabled(false);
        applyDiscountButton.setEnabled(false);
    }

    
    private void addListeners() {
        
        searchFlightsButton.addActionListener(e -> searchFlights());

        
        flightSelectionComboBox.addActionListener(this::handleFlightSelection);
        searchCustomerButton.addActionListener(e -> searchCustomerForBooking());
        registerNewCustomerButton.addActionListener(e -> openRegisterNewCustomerDialog());
        addPassengerButton.addActionListener(e -> addPassengerRow());
        removePassengerButton.addActionListener(e -> removeSelectedPassengerRow());
        seatClassComboBox.addActionListener(e -> calculateTotalPrice());
        customerPaysRadioButton.addActionListener(e -> calculateTotalPrice());
        agentPaysRadioButton.addActionListener(e -> calculateTotalPrice());
        submitBookingButton.addActionListener(e -> submitNewBookingOrWaitlist());

        
        modifySearchButton.addActionListener(e -> searchBookingToModify());
        modifyRecalculateButton.addActionListener(e -> recalculateModifyPrice());
        updateBookingButton.addActionListener(e -> updateBookingDetails());
        saveTicketChangesButton.addActionListener(e -> saveTicketChanges());

        
        viewCustomerSearchButton.addActionListener(e -> searchCustomerToView());
        blockCustomerButton.addActionListener(e -> requestBlockCustomer());

        
        generateReportButton.addActionListener(e -> generateBookingReport());
        exportReportButton.addActionListener(e -> exportReportToTxt());

        
        discountSearchButton.addActionListener(e -> searchBookingForDiscount());
        discountPreviewButton.addActionListener(e -> previewDiscount());
        applyDiscountButton.addActionListener(e -> applyDiscount());

        
        refreshRefundsButton.addActionListener(e -> loadPendingRefundRequests());
    }

    

    
    private void searchFlights() {
    String criteria = (String) flightSearchCriteriaComboBox.getSelectedItem();
    String value = searchInputTextField.getText().trim();
    flightsResultsTableModel.setRowCount(0); 

    if (value.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please enter a search value.", "Input Error", JOptionPane.WARNING_MESSAGE);
        return;
    }

    String sql = "SELECT f.flight_id, f.flight_num, dep.name as dep_name, f.departure_airport_id, " +
                 "arr.name as arr_name, f.arrival_airport_id, " +
                 "f.departure_time, f.arrival_time, f.aircraft_type, f.total_seats, f.available_seats, f.status, " +
                 "f.fare_economy, f.fare_business, f.fare_first_class " +
                 "FROM flight f " +
                 "JOIN airport dep ON f.departure_airport_id = dep.airport_code " +
                 "JOIN airport arr ON f.arrival_airport_id = arr.airport_code ";

    String whereClause = "";
    switch (criteria) {
        case "Flight ID": whereClause = "WHERE f.flight_id LIKE ?"; break;
        case "Flight Number": whereClause = "WHERE f.flight_num LIKE ?"; break;
        case "Departure Airport Code": whereClause = "WHERE f.departure_airport_id LIKE ?"; break;
        case "Arrival Airport Code": whereClause = "WHERE f.arrival_airport_id LIKE ?"; break;
        case "Departure Country": whereClause = "WHERE dep.country LIKE ?"; break;
        case "Arrival Country": whereClause = "WHERE arr.country LIKE ?"; break;
        default: JOptionPane.showMessageDialog(this, "Invalid search criteria.", "Error", JOptionPane.ERROR_MESSAGE); return;
    }
    sql += whereClause + " ORDER BY f.departure_time ASC";

    Connection conn = DatabaseConnection.connect();
    if (conn == null) return;

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setString(1, "%" + value + "%");
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            Vector<Object> row = new Vector<>();
            row.add(rs.getString("flight_id"));
            row.add(rs.getString("flight_num"));
            row.add(rs.getString("dep_name") + " (" + rs.getString("departure_airport_id") + ")");
            row.add(rs.getString("arr_name") + " (" + rs.getString("arrival_airport_id") + ")");
            row.add(rs.getTimestamp("departure_time"));
            row.add(rs.getTimestamp("arrival_time"));
            row.add(rs.getString("aircraft_type"));
            row.add(rs.getInt("total_seats"));
            row.add(rs.getInt("available_seats"));
            row.add(rs.getString("status"));
            row.add(String.format("%.2f", rs.getDouble("fare_economy")));
            row.add(String.format("%.2f", rs.getDouble("fare_business")));
            row.add(String.format("%.2f", rs.getDouble("fare_first_class")));
            flightsResultsTableModel.addRow(row);
        }
        if (flightsResultsTableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No flights found matching your criteria.", "Search Results", JOptionPane.INFORMATION_MESSAGE);
        }
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Error searching flights: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
    } finally {
        closeConnection(conn);
    }
}

    
    private void loadFlightsForBooking() {
        flightSelectionComboBox.removeAllItems();
        flightSelectionComboBox.setEnabled(false);
        flightSelectionComboBox.addItem(new FlightDisplayItem(null, "Loading flights...", 0, 0, 0, 0));

        
        String sql = "SELECT f.flight_id, f.flight_num, dep.name as dep_name, arr.name as arr_name, f.departure_time, " +
                     "f.fare_economy, f.fare_business, f.fare_first_class, f.available_seats " +
                     "FROM flight f " +
                     "JOIN airport dep ON f.departure_airport_id = dep.airport_code " +
                     "JOIN airport arr ON f.arrival_airport_id = arr.airport_code " +
                     "WHERE f.status = \'On Time\' AND f.departure_time > NOW() " +
                     "ORDER BY f.departure_time ASC";

        Connection conn = DatabaseConnection.connect();
        if (conn == null) {
            flightSelectionComboBox.removeAllItems();
            flightSelectionComboBox.addItem(new FlightDisplayItem(null, "Error loading flights (DB)", 0, 0, 0, 0));
            return;
        }

        
        SwingWorker<List<FlightDisplayItem>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<FlightDisplayItem> doInBackground() throws Exception {
                List<FlightDisplayItem> flightItems = new ArrayList<>();
                try (PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        String flightId = rs.getString("flight_id");
                        String flightNum = rs.getString("flight_num");
                        String depName = rs.getString("dep_name");
                        String arrName = rs.getString("arr_name");
                        Timestamp depTime = rs.getTimestamp("departure_time");
                        int availableSeats = rs.getInt("available_seats"); 
                        double econFare = rs.getDouble("fare_economy");
                        double busFare = rs.getDouble("fare_business");
                        double firstFare = rs.getDouble("fare_first_class");

                        String displayText = String.format("%s (%s -> %s) | %s | %d seats left",
                                flightNum, depName, arrName, depTime, availableSeats);
                        flightItems.add(new FlightDisplayItem(flightId, displayText, econFare, busFare, firstFare, availableSeats));
                    }
                } finally {
                    closeConnection(conn);
                }
                return flightItems;
            }

            @Override
            protected void done() {
                try {
                    List<FlightDisplayItem> items = get();
                    flightSelectionComboBox.removeAllItems();
                    if (items.isEmpty()) {
                        flightSelectionComboBox.addItem(new FlightDisplayItem(null, "No available flights found", 0, 0, 0, 0));
                        flightSelectionComboBox.setEnabled(false);
                    } else {
                        flightSelectionComboBox.addItem(new FlightDisplayItem(null, "-- Select a Flight --", 0, 0, 0, 0)); 
                        for (FlightDisplayItem item : items) {
                            flightSelectionComboBox.addItem(item);
                        }
                        flightSelectionComboBox.setEnabled(true);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    flightSelectionComboBox.removeAllItems();
                    flightSelectionComboBox.addItem(new FlightDisplayItem(null, "Error loading flights (Exec)", 0, 0, 0, 0));
                    flightSelectionComboBox.setEnabled(false);
                }
            }
        };
        worker.execute();
    }

    private void handleFlightSelection(ActionEvent e) {
        Object selectedItem = flightSelectionComboBox.getSelectedItem();
        if (selectedItem instanceof FlightDisplayItem) {
            FlightDisplayItem item = (FlightDisplayItem) selectedItem;
            if (item.flightId != null) {
                selectedFlightIdForBooking = item.flightId;
                selectedFlightFareEconomy = item.fareEconomy;
                selectedFlightFareBusiness = item.fareBusiness;
                selectedFlightFareFirstClass = item.fareFirstClass;
                selectedFlightAvailableSeats = item.availableSeats; 
                availableSeatsLabel.setText("Available Seats: " + selectedFlightAvailableSeats);
                calculateTotalPrice();
                submitBookingButton.setEnabled(selectedCustomerIdForBooking != null); 
            } else {
                
                selectedFlightIdForBooking = null;
                selectedFlightAvailableSeats = 0;
                availableSeatsLabel.setText("Available Seats: -");
                calculateTotalPrice(); 
                submitBookingButton.setEnabled(false);
            }
        } else {
            
            selectedFlightIdForBooking = null;
            selectedFlightAvailableSeats = 0;
            availableSeatsLabel.setText("Available Seats: -");
            calculateTotalPrice();
            submitBookingButton.setEnabled(false);
        }
    }

   private void searchCustomerForBooking() {
    String searchTerm = customerSearchInput.getText().trim();
    if (searchTerm.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please enter Customer ID, Email, or Phone.", "Input Error", JOptionPane.WARNING_MESSAGE);
        return;
    }

    
    String sql = "SELECT c.customer_id, u.username, u.email " +
                 "FROM customers c JOIN users u ON c.user_id = u.user_id " +
                 "WHERE c.customer_id = ? OR u.email = ? OR u.phone_number = ?";

    Connection conn = DatabaseConnection.connect();
    if (conn == null) return;

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setString(1, searchTerm);
        pstmt.setString(2, searchTerm);
        pstmt.setString(3, searchTerm);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            selectedCustomerIdForBooking = rs.getString("customer_id");
            String username = rs.getString("username");
            String email = rs.getString("email");
            foundCustomerLabel.setText("Selected Customer: " + username + " (" + selectedCustomerIdForBooking + ")");
            submitBookingButton.setEnabled(selectedFlightIdForBooking != null); 
        } else {
            selectedCustomerIdForBooking = null;
            foundCustomerLabel.setText("Selected Customer: Not Found");
            submitBookingButton.setEnabled(false);
            JOptionPane.showMessageDialog(this, "Customer not found. Please register or try a different search term.", "Search Result", JOptionPane.INFORMATION_MESSAGE);
        }
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Error searching customer: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
        selectedCustomerIdForBooking = null;
        foundCustomerLabel.setText("Selected Customer: Error");
        submitBookingButton.setEnabled(false);
    } finally {
        closeConnection(conn);
    }
}

    private void openRegisterNewCustomerDialog() {
        
        
        
        String firstName = JOptionPane.showInputDialog(this, "Enter First Name:");
        String lastName = JOptionPane.showInputDialog(this, "Enter Last Name:");
        String email = JOptionPane.showInputDialog(this, "Enter Email:");
        String phone = JOptionPane.showInputDialog(this, "Enter Phone:");
        String password = JOptionPane.showInputDialog(this, "Enter Password:"); 

        if (firstName != null && !firstName.trim().isEmpty() &&
            lastName != null && !lastName.trim().isEmpty() &&
            email != null && !email.trim().isEmpty() &&
            phone != null && !phone.trim().isEmpty() &&
            password != null && !password.isEmpty()) {

            
            String sqlUser = "INSERT INTO users (first_name, last_name, email, phone_number, password, role) VALUES (?, ?, ?, ?, ?, \'Customer\')";
            

            Connection conn = DatabaseConnection.connect();
            if (conn == null) return;

            try (PreparedStatement pstmtUser = conn.prepareStatement(sqlUser, Statement.RETURN_GENERATED_KEYS)) {
                
                
                String hashedPassword = password; 

                pstmtUser.setString(1, firstName);
                pstmtUser.setString(2, lastName);
                pstmtUser.setString(3, email);
                pstmtUser.setString(4, phone);
                pstmtUser.setString(5, hashedPassword);
                int affectedRows = pstmtUser.executeUpdate();

                if (affectedRows > 0) {
                    
                    
                    
                    
                    JOptionPane.showMessageDialog(this, "Customer registered successfully (simulated). Please search again to select.", "Registration Success", JOptionPane.INFORMATION_MESSAGE);
                    customerSearchInput.setText(email); 
                    searchCustomerForBooking(); 
                } else {
                    JOptionPane.showMessageDialog(this, "Registration failed.", "Registration Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error registering customer: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            } finally {
                closeConnection(conn);
            }
        } else {
            JOptionPane.showMessageDialog(this, "All fields are required for registration.", "Input Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void addPassengerRow() {
        passengersTableModel.addRow(new Object[]{"", "", "", "", "", "Male", "", false});
        calculateTotalPrice(); 
    }

    private void removeSelectedPassengerRow() {
        int selectedRow = passengersTable.getSelectedRow();
        if (selectedRow >= 0) {
            passengersTableModel.removeRow(selectedRow);
            calculateTotalPrice(); 
        } else {
            JOptionPane.showMessageDialog(this, "Please select a passenger row to remove.", "Selection Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void calculateTotalPrice() {
        if (selectedFlightIdForBooking == null) {
            totalPriceLabel.setText("Total Price: $0.00");
            agentCommissionLabel.setText("Agent Commission: $0.00");
            return;
        }

        String selectedClass = (String) seatClassComboBox.getSelectedItem();
        double basePricePerPassenger = 0.0;
        switch (selectedClass) {
            case "Economy": basePricePerPassenger = selectedFlightFareEconomy; break;
            case "Business": basePricePerPassenger = selectedFlightFareBusiness; break;
            case "First Class": basePricePerPassenger = selectedFlightFareFirstClass; break;
        }

        int passengerCount = passengersTableModel.getRowCount();
        if (passengerCount == 0) passengerCount = 1; 

        double totalPrice = basePricePerPassenger * passengerCount;
        totalPriceLabel.setText(String.format("Total Price: $%.2f", totalPrice));

        double commission = 0.0;
        if (agentPaysRadioButton.isSelected()) {
            commission = totalPrice * (currentAgentCommissionRate / 100.0);
        }
        agentCommissionLabel.setText(String.format("Agent Commission: $%.2f", commission));
    }

    private void submitNewBookingOrWaitlist() {
        if (selectedFlightIdForBooking == null || selectedCustomerIdForBooking == null) {
            JOptionPane.showMessageDialog(this, "Please select a flight and a customer first.", "Booking Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int passengerCount = passengersTableModel.getRowCount();
        if (passengerCount == 0) {
            JOptionPane.showMessageDialog(this, "Please add at least one passenger.", "Booking Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        
        List<Passengers> passengers = new ArrayList<>();
        for (int i = 0; i < passengerCount; i++) {
            String firstName = (String) passengersTableModel.getValueAt(i, 0);
            String lastName = (String) passengersTableModel.getValueAt(i, 1);
            String dobStr = (String) passengersTableModel.getValueAt(i, 2);
            String passport = (String) passengersTableModel.getValueAt(i, 3);
            String nationality = (String) passengersTableModel.getValueAt(i, 4);
            String gender = (String) passengersTableModel.getValueAt(i, 5);
            String mealPref = (String) passengersTableModel.getValueAt(i, 6);
            Boolean specialAssist = (Boolean) passengersTableModel.getValueAt(i, 7);

            if (firstName == null || firstName.trim().isEmpty() || lastName == null || lastName.trim().isEmpty() || dobStr == null || dobStr.trim().isEmpty() || passport == null || passport.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Passenger First Name, Last Name, DOB, and Passport are required (Row " + (i + 1) + ").", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            
            Passengers p = new Passengers();
            p.setFirstName(firstName.trim());
            p.setLastName(lastName.trim());
            
            p.setPassportNumber(passport.trim());
            p.setNationality(nationality != null ? nationality.trim() : "");
            p.setGender(gender != null ? gender : "Other");
            p.setMealPreference(mealPref != null ? mealPref.trim() : "");
            p.setSpecialAssistance(specialAssist != null && specialAssist);
            passengers.add(p);
        }

        
        if (passengerCount <= selectedFlightAvailableSeats) {
            proceedWithBooking(passengers);
        } else {
            int choice = JOptionPane.showConfirmDialog(this,
                    "Not enough seats available (" + selectedFlightAvailableSeats + " left). Add customer to waitlist for this flight?",
                    "Flight Full - Add to Waitlist?",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (choice == JOptionPane.YES_OPTION) {
                addToWaitlist(selectedFlightIdForBooking, selectedCustomerIdForBooking);
            }
        }
    }

    private void proceedWithBooking(List<Passengers> passengers) {
    String bookingId = "BOOK" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    String seatClass = (String) seatClassComboBox.getSelectedItem();
    String specialRequests = specialRequestsTextArea.getText().trim();
    String paymentStatus = "Pending";
    double totalPrice = calculateCurrentBookingPrice();

    Connection conn = DatabaseConnection.connect();
    if (conn == null) return;

    try {
        conn.setAutoCommit(false);

        
        String bookingSql = "INSERT INTO booking (booking_id, customer_id, flight_id, booking_date, seat_class, total_price, payment_status, special_requests) VALUES (?, ?, ?, NOW(), ?, ?, ?, ?)";
        try (PreparedStatement pstmtBooking = conn.prepareStatement(bookingSql)) {
            pstmtBooking.setString(1, bookingId);
            pstmtBooking.setString(2, selectedCustomerIdForBooking);
            pstmtBooking.setString(3, selectedFlightIdForBooking);
            pstmtBooking.setString(4, seatClass);
            pstmtBooking.setDouble(5, totalPrice);
            pstmtBooking.setString(6, paymentStatus);
            pstmtBooking.setString(7, specialRequests);
            pstmtBooking.executeUpdate();
        }

        
        String passengerSql = "INSERT INTO passenger (passenger_id, first_name, last_name, date_of_birth, passport_number, nationality, gender, special_assistance, meal_preference) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String ticketSql = "INSERT INTO ticket (ticket_id, booking_id, passenger_id, seat_number, ticket_status) VALUES (?, ?, ?, ?, ?)";

        for (Passengers p : passengers) {
            
            String passengerId = "PASS" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            p.setPassengerId(passengerId);

            
            try (PreparedStatement pstmtPassenger = conn.prepareStatement(passengerSql)) {
                pstmtPassenger.setString(1, passengerId);
                pstmtPassenger.setString(2, p.getFirstName());
                pstmtPassenger.setString(3, p.getLastName());
                pstmtPassenger.setDate(4, new java.sql.Date(p.getDateOfBirth().getTime()));
                pstmtPassenger.setString(5, p.getPassportNumber());
                pstmtPassenger.setString(6, p.getNationality());
                pstmtPassenger.setString(7, p.getGender());
                pstmtPassenger.setBoolean(8, p.isSpecialAssistance());
                pstmtPassenger.setString(9, p.getMealPreference());
                pstmtPassenger.executeUpdate();
            }

            
            String ticketId = "TKT" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            try (PreparedStatement pstmtTicket = conn.prepareStatement(ticketSql)) {
                pstmtTicket.setString(1, ticketId);
                pstmtTicket.setString(2, bookingId);
                pstmtTicket.setString(3, passengerId); 
                pstmtTicket.setString(4, null); 
                pstmtTicket.setString(5, "Confirmed");
                pstmtTicket.executeUpdate();
            }
        }

        
        String updateSeatsSql = "UPDATE flight SET available_seats = available_seats - ? WHERE flight_id = ? AND available_seats >= ?";
        try (PreparedStatement pstmtSeats = conn.prepareStatement(updateSeatsSql)) {
            pstmtSeats.setInt(1, passengers.size());
            pstmtSeats.setString(2, selectedFlightIdForBooking);
            pstmtSeats.setInt(3, passengers.size());
            int rowsAffected = pstmtSeats.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Failed to reserve seats. Not enough available or flight changed.");
            }
        }

        
        if (customerPaysRadioButton.isSelected()) {
            String paymentId = "PAY" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            String paymentSql = "INSERT INTO payment (payment_id, booking_id, amount, payment_date, payment_method, payment_status, transaction_id) VALUES (?, ?, ?, NOW(), ?, ?, ?)";
            try (PreparedStatement pstmtPayment = conn.prepareStatement(paymentSql)) {
                pstmtPayment.setString(1, paymentId);
                pstmtPayment.setString(2, bookingId);
                pstmtPayment.setDouble(3, totalPrice);
                pstmtPayment.setString(4, "Credit Card");
                pstmtPayment.setString(5, "Completed");
                pstmtPayment.setString(6, "TXN" + UUID.randomUUID().toString().substring(0, 10));
                pstmtPayment.executeUpdate();
            }
            
            String updateBookingStatusSql = "UPDATE booking SET payment_status = 'Completed' WHERE booking_id = ?";
            try (PreparedStatement pstmtUpdateBooking = conn.prepareStatement(updateBookingStatusSql)) {
                pstmtUpdateBooking.setString(1, bookingId);
                pstmtUpdateBooking.executeUpdate();
            }
            paymentStatus = "Completed";
        }

        conn.commit();
        JOptionPane.showMessageDialog(this, "Booking successful! Booking ID: " + bookingId + ", Status: " + paymentStatus, "Booking Confirmation", JOptionPane.INFORMATION_MESSAGE);

        
        sendNotification(selectedCustomerIdForBooking, "New booking confirmed! Booking ID: " + bookingId, "System");
        if (paymentStatus.equals("Completed")) {
            sendNotification(selectedCustomerIdForBooking, "Payment completed for Booking ID: " + bookingId, "System");
        }

        resetCreateBookingForm();
        loadFlightsForBooking();

    } catch (SQLException ex) {
        try { conn.rollback(); } catch (SQLException rollbackEx) { System.err.println("Rollback failed: " + rollbackEx.getMessage()); }
        JOptionPane.showMessageDialog(this, "Error creating booking: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
    } finally {
        try { conn.setAutoCommit(true); } catch (SQLException ex) { /* ignore */ }
        closeConnection(conn);
    }
}

    private double calculateCurrentBookingPrice() {
        
        String priceText = totalPriceLabel.getText().replace("Total Price: $", "");
        try {
            return Double.parseDouble(priceText);
        } catch (NumberFormatException e) {
            return 0.0; 
        }
    }

    private void addToWaitlist(String flightId, String customerId) {
        
        String checkSql = "SELECT wc.waitlist_id FROM waitlist w JOIN waitlist_customers wc ON w.waitlist_id = wc.waitlist_id WHERE w.flight_id = ? AND wc.customer_id = ?";
        Connection conn = DatabaseConnection.connect();
        if (conn == null) return;

        try {
            boolean alreadyOnWaitlist = false;
            try (PreparedStatement pstmtCheck = conn.prepareStatement(checkSql)) {
                pstmtCheck.setString(1, flightId);
                pstmtCheck.setString(2, customerId);
                ResultSet rsCheck = pstmtCheck.executeQuery();
                if (rsCheck.next()) {
                    alreadyOnWaitlist = true;
                }
            }

            if (alreadyOnWaitlist) {
                JOptionPane.showMessageDialog(this, "Customer is already on the waitlist for this flight.", "Waitlist Info", JOptionPane.INFORMATION_MESSAGE);
                return; 
            }

            
            conn.setAutoCommit(false);
            String waitlistId = null;
            String findWaitlistSql = "SELECT waitlist_id FROM waitlist WHERE flight_id = ?";
            String insertWaitlistSql = "INSERT INTO waitlist (waitlist_id, flight_id, date_added) VALUES (?, ?, NOW())";
            String insertCustomerSql = "INSERT INTO waitlist_customers (waitlist_id, customer_id, status) VALUES (?, ?, \'Pending\')";

            
            try (PreparedStatement pstmtFind = conn.prepareStatement(findWaitlistSql)) {
                pstmtFind.setString(1, flightId);
                ResultSet rsFind = pstmtFind.executeQuery();
                if (rsFind.next()) {
                    waitlistId = rsFind.getString("waitlist_id");
                } else {
                    waitlistId = "WL" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
                    try (PreparedStatement pstmtInsertWl = conn.prepareStatement(insertWaitlistSql)) {
                        pstmtInsertWl.setString(1, waitlistId);
                        pstmtInsertWl.setString(2, flightId);
                        pstmtInsertWl.executeUpdate();
                    }
                }
            }

            
            try (PreparedStatement pstmtInsertCust = conn.prepareStatement(insertCustomerSql)) {
                pstmtInsertCust.setString(1, waitlistId);
                pstmtInsertCust.setString(2, customerId);
                pstmtInsertCust.executeUpdate();
            }

            conn.commit();
            JOptionPane.showMessageDialog(this, "Customer added to waitlist successfully for flight " + flightId + ". Waitlist ID: " + waitlistId, "Waitlist Success", JOptionPane.INFORMATION_MESSAGE);
            sendNotification(customerId, "You have been added to the waitlist for flight " + flightId + ". We will notify you if a seat becomes available.", "System");
            resetCreateBookingForm();

        } catch (SQLException ex) {
            try { conn.rollback(); } catch (SQLException rollbackEx) { System.err.println("Rollback failed: " + rollbackEx.getMessage()); }
            JOptionPane.showMessageDialog(this, "Error adding customer to waitlist: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } finally {
            try { conn.setAutoCommit(true); } catch (SQLException ex) { /* ignore */ }
            closeConnection(conn);
        }
    }

    private void resetCreateBookingForm() {
        flightSelectionComboBox.setSelectedIndex(0); 
        customerSearchInput.setText("");
        foundCustomerLabel.setText("Selected Customer: None");
        passengersTableModel.setRowCount(0); 
        addPassengerRow(); 
        seatClassComboBox.setSelectedIndex(0);
        specialRequestsTextArea.setText("");
        customerPaysRadioButton.setSelected(true);
        totalPriceLabel.setText("Total Price: $0.00");
        agentCommissionLabel.setText("Agent Commission: $0.00");
        availableSeatsLabel.setText("Available Seats: -");
        selectedFlightIdForBooking = null;
        selectedCustomerIdForBooking = null;
        selectedFlightAvailableSeats = 0;
        submitBookingButton.setEnabled(false);
    }

    
    private void searchBookingToModify() {
        String criteria = (String) modifySearchCriteriaComboBox.getSelectedItem();
        String value = modifySearchInputTextField.getText().trim();
        clearModifyBookingForm();

        if (value.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a search value.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String sql = "SELECT b.booking_id, b.customer_id, b.flight_id, b.seat_class, b.total_price, b.payment_status, b.special_requests, " +
                     "f.fare_economy, f.fare_business, f.fare_first_class " +
                     "FROM booking b JOIN flight f ON b.flight_id = f.flight_id ";
        String whereClause = "";

        switch (criteria) {
            case "Booking ID": whereClause = "WHERE b.booking_id = ?"; break;
            case "Customer ID": whereClause = "WHERE b.customer_id = ?"; break; 
            case "Flight ID": whereClause = "WHERE b.flight_id = ?"; break; 
            default: JOptionPane.showMessageDialog(this, "Invalid search criteria.", "Error", JOptionPane.ERROR_MESSAGE); return;
        }
        sql += whereClause;

        Connection conn = DatabaseConnection.connect();
        if (conn == null) return;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, value);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                
                
                currentBookingIdToModify = rs.getString("booking_id");
                String customerId = rs.getString("customer_id");
                String flightId = rs.getString("flight_id");
                String seatClass = rs.getString("seat_class");
                currentBookingPrice = rs.getDouble("total_price");
                String specialRequests = rs.getString("special_requests");
                modifyFlightFareEconomy = rs.getDouble("fare_economy");
                modifyFlightFareBusiness = rs.getDouble("fare_business");
                modifyFlightFareFirstClass = rs.getDouble("fare_first_class");

                modifyBookingIdLabel.setText("Booking ID: " + currentBookingIdToModify);
                modifyCustomerIdLabel.setText("Customer ID: " + customerId);
                modifyFlightIdLabel.setText("Flight ID: " + flightId);
                modifySeatClassComboBox.setSelectedItem(seatClass);
                modifySpecialRequestsTextArea.setText(specialRequests != null ? specialRequests : "");
                modifyCurrentPriceLabel.setText(String.format("Current Price: $%.2f", currentBookingPrice));
                modifyNewPriceLabel.setText("New Price: -");
                modifyManualPriceTextField.setText(""); 

                
                modifySeatClassComboBox.setEnabled(true);
                modifySpecialRequestsTextArea.setEnabled(true);
                modifyManualPriceTextField.setEnabled(true);
                modifyRecalculateButton.setEnabled(true);
                updateBookingButton.setEnabled(true);
                saveTicketChangesButton.setEnabled(true);

                
                loadTicketsForBooking(conn, currentBookingIdToModify);

                if (rs.next()) {
                    
                    JOptionPane.showMessageDialog(this, "Multiple bookings found. Displaying the first one. Refine search if needed.", "Multiple Results", JOptionPane.INFORMATION_MESSAGE);
                }

            } else {
                JOptionPane.showMessageDialog(this, "No booking found matching your criteria.", "Search Result", JOptionPane.INFORMATION_MESSAGE);
                clearModifyBookingForm();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error searching booking: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
            clearModifyBookingForm();
        } finally {
            closeConnection(conn);
        }
    }

    private void clearModifyBookingForm() {
        currentBookingIdToModify = null;
        modifyBookingIdLabel.setText("Booking ID: -");
        modifyCustomerIdLabel.setText("Customer ID: -");
        modifyFlightIdLabel.setText("Flight ID: -");
        modifySeatClassComboBox.setSelectedIndex(0);
        modifySpecialRequestsTextArea.setText("");
        modifyCurrentPriceLabel.setText("Current Price: $0.00");
        modifyNewPriceLabel.setText("New Price: -");
        modifyManualPriceTextField.setText("");
        modifyTicketsTableModel.setRowCount(0); 

        
        modifySeatClassComboBox.setEnabled(false);
        modifySpecialRequestsTextArea.setEnabled(false);
        modifyManualPriceTextField.setEnabled(false);
        modifyRecalculateButton.setEnabled(false);
        updateBookingButton.setEnabled(false);
        saveTicketChangesButton.setEnabled(false);
    }

    private void loadTicketsForBooking(Connection conn, String bookingId) {
        String sql = "SELECT ticket_id, passenger_id, seat_number, ticket_status FROM ticket WHERE booking_id = ?";
        modifyTicketsTableModel.setRowCount(0); 

        boolean closeConnHere = false;
        if (conn == null) {
            conn = DatabaseConnection.connect();
            if (conn == null) {
                 System.err.println("Error loading tickets: Database connection failed.");
                 JOptionPane.showMessageDialog(this, "Database connection failed while loading tickets.", "DB Error", JOptionPane.ERROR_MESSAGE);
                 return;
            }
            closeConnHere = true;
        }

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, bookingId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getString("ticket_id"));
                row.add(rs.getString("passenger_id")); 
                row.add(rs.getString("seat_number"));
                row.add(rs.getString("ticket_status"));
                modifyTicketsTableModel.addRow(row);
            }
        } catch (SQLException ex) {
            System.err.println("Error loading tickets for booking " + bookingId + ": " + ex.getMessage());
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading associated tickets: " + ex.getMessage(), "Ticket Load Error", JOptionPane.WARNING_MESSAGE);
        } finally {
            if (closeConnHere) {
                closeConnection(conn);
            }
        }
    }

    private void recalculateModifyPrice() {
        if (currentBookingIdToModify == null) return;

        String manualPriceStr = modifyManualPriceTextField.getText().trim();
        double newPrice;

        if (!manualPriceStr.isEmpty()) {
            try {
                newPrice = Double.parseDouble(manualPriceStr);
                if (newPrice < 0) throw new NumberFormatException("Price cannot be negative");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid manual price entered. Please enter a valid positive number.", "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
        } else {
            
            String selectedClass = (String) modifySeatClassComboBox.getSelectedItem();
            double basePricePerPassenger = 0.0;
            switch (selectedClass) {
                case "Economy": basePricePerPassenger = modifyFlightFareEconomy; break;
                case "Business": basePricePerPassenger = modifyFlightFareBusiness; break;
                case "First Class": basePricePerPassenger = modifyFlightFareFirstClass; break;
            }
            int passengerCount = modifyTicketsTableModel.getRowCount(); 
            newPrice = basePricePerPassenger * passengerCount;
        }

        modifyNewPriceLabel.setText(String.format("New Price: $%.2f", newPrice));
    }

    private void updateBookingDetails() {
        if (currentBookingIdToModify == null) {
            JOptionPane.showMessageDialog(this, "No booking selected to update.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        
        recalculateModifyPrice();
        String newPriceStr = modifyNewPriceLabel.getText().replace("New Price: $", "");
        double newPrice;
        try {
            newPrice = Double.parseDouble(newPriceStr);
        } catch (NumberFormatException | NullPointerException e) {
            JOptionPane.showMessageDialog(this, "Could not determine the new price. Please recalculate.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String newSeatClass = (String) modifySeatClassComboBox.getSelectedItem();
        String newSpecialRequests = modifySpecialRequestsTextArea.getText().trim();

        String sql = "UPDATE booking SET seat_class = ?, total_price = ?, special_requests = ? WHERE booking_id = ?";

        Connection conn = DatabaseConnection.connect();
        if (conn == null) return;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newSeatClass);
            pstmt.setDouble(2, newPrice);
            pstmt.setString(3, newSpecialRequests);
            pstmt.setString(4, currentBookingIdToModify);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Booking details updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                
                currentBookingPrice = newPrice;
                modifyCurrentPriceLabel.setText(String.format("Current Price: $%.2f", currentBookingPrice));
                modifyNewPriceLabel.setText("New Price: -"); 
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update booking details.", "Update Failed", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error updating booking: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } finally {
            closeConnection(conn);
        }
    }

    private void saveTicketChanges() {
        if (currentBookingIdToModify == null) {
            JOptionPane.showMessageDialog(this, "No booking selected.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (modifyTicketsTable.isEditing()) {
            modifyTicketsTable.getCellEditor().stopCellEditing();
        }

        int rowCount = modifyTicketsTableModel.getRowCount();
        if (rowCount == 0) {
            JOptionPane.showMessageDialog(this, "No tickets found for this booking to update.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String sql = "UPDATE ticket SET seat_number = ?, ticket_status = ? WHERE ticket_id = ?";
        Connection conn = DatabaseConnection.connect();
        if (conn == null) return;

        int updatedCount = 0;
        int failedCount = 0;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < rowCount; i++) {
                String ticketId = (String) modifyTicketsTableModel.getValueAt(i, 0);
                String seatNumber = (String) modifyTicketsTableModel.getValueAt(i, 2);
                String status = (String) modifyTicketsTableModel.getValueAt(i, 3);

                
                if (status == null || status.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Ticket status cannot be empty (Row " + (i + 1) + ").", "Input Error", JOptionPane.WARNING_MESSAGE);
                    failedCount++;
                    continue; 
                }

                pstmt.setString(1, (seatNumber != null && !seatNumber.trim().isEmpty()) ? seatNumber.trim() : null); 
                pstmt.setString(2, status.trim());
                pstmt.setString(3, ticketId);
                pstmt.addBatch();
            }

            if (failedCount == 0) { 
                int[] results = pstmt.executeBatch();
                for (int result : results) {
                    if (result >= 0) { 
                        updatedCount++;
                    } else if (result == Statement.EXECUTE_FAILED) {
                        failedCount++;
                    }
                }
            }

            if (failedCount > 0) {
                JOptionPane.showMessageDialog(this, "Successfully updated " + updatedCount + " tickets. Failed to update/validate " + failedCount + " tickets.", "Partial Success/Validation Error", JOptionPane.WARNING_MESSAGE);
            } else if (updatedCount > 0) {
                JOptionPane.showMessageDialog(this, "All " + updatedCount + " ticket(s) updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "No tickets were updated (no changes detected or validation failed).", "Info", JOptionPane.INFORMATION_MESSAGE);
            }

            
            loadTicketsForBooking(conn, currentBookingIdToModify);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error saving ticket changes: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } finally {
            closeConnection(conn);
        }
    }

    
   private void searchCustomerToView() {
    String criteria = (String) viewCustomerSearchCriteriaComboBox.getSelectedItem();
    String value = viewCustomerSearchInputTextField.getText().trim();
    clearViewCustomerPanel();

    if (value.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please enter a search value.", "Input Error", JOptionPane.WARNING_MESSAGE);
        return;
    }

    
    String sqlUser = "SELECT u.user_id, u.username, u.email, u.phone_number, u.registration_date, u.role, " +
                     "c.passport_number, c.frequent_flyer_number, c.preferences, bl.blacklist_id, bl.reason as block_reason " +
                     "FROM users u LEFT JOIN customers c ON u.user_id = c.customer_id " +
                     "LEFT JOIN blacklist bl ON u.user_id = bl.user_id AND bl.is_active = 1 ";

    String whereClause = "";
    switch (criteria) {
        case "Customer ID": whereClause = "WHERE u.user_id = ? AND u.role = 'Customer' "; break;
        case "Email": whereClause = "WHERE u.email = ? AND u.role = 'Customer' "; break;
        case "Phone": whereClause = "WHERE u.phone_number = ? AND u.role = 'Customer' "; break;
        case "Passport No.": whereClause = "WHERE c.passport_number = ? AND u.role = 'Customer' "; break;
        default: JOptionPane.showMessageDialog(this, "Invalid search criteria.", "Error", JOptionPane.ERROR_MESSAGE); return;
    }
    sqlUser += whereClause;

    Connection conn = DatabaseConnection.connect();
    if (conn == null) return;

    try (PreparedStatement pstmt = conn.prepareStatement(sqlUser)) {
        pstmt.setString(1, value);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            viewedCustomerId = rs.getString("user_id");
            StringBuilder profile = new StringBuilder();
            profile.append("Customer ID: ").append(viewedCustomerId).append("\n");
            profile.append("Username: ").append(rs.getString("username")).append("\n");
            profile.append("Email: ").append(rs.getString("email")).append("\n");
            profile.append("Phone: ").append(rs.getString("phone_number")).append("\n");
            profile.append("Registration Date: ").append(rs.getTimestamp("registration_date")).append("\n");
            profile.append("Passport No: ").append(rs.getString("passport_number") != null ? rs.getString("passport_number") : "N/A").append("\n");
            profile.append("Frequent Flyer: ").append(rs.getString("frequent_flyer_number") != null ? rs.getString("frequent_flyer_number") : "N/A").append("\n");
            profile.append("Preferences: ").append(rs.getString("preferences") != null ? rs.getString("preferences") : "N/A").append("\n");

            String blacklistId = rs.getString("blacklist_id");
            if (blacklistId != null) {
                profile.append("\n--- CURRENTLY BLOCKED ---").append("\n");
                profile.append("Reason: ").append(rs.getString("block_reason")).append("\n");
                blockCustomerButton.setText("Request Unblock Customer");
                blockCustomerButton.setEnabled(true);
            } else {
                profile.append("\nStatus: Active").append("\n");
                blockCustomerButton.setText("Request Block Customer");
                blockCustomerButton.setEnabled(true);
            }

            customerProfileTextArea.setText(profile.toString());

            
            loadCustomerBookingHistory(conn, viewedCustomerId);
            loadCustomerPaymentHistory(conn, viewedCustomerId);

        } else {
            JOptionPane.showMessageDialog(this, "No customer found matching your criteria.", "Search Result", JOptionPane.INFORMATION_MESSAGE);
            clearViewCustomerPanel();
        }
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Error searching customer: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
        clearViewCustomerPanel();
    } finally {
        closeConnection(conn);
    }
}

    private void clearViewCustomerPanel() {
        viewedCustomerId = null;
        customerProfileTextArea.setText("");
        customerBookingHistoryTableModel.setRowCount(0);
        customerPaymentHistoryTableModel.setRowCount(0);
        blockCustomerButton.setText("Request Block Customer");
        blockCustomerButton.setEnabled(false);
    }

    private void loadCustomerBookingHistory(Connection conn, String customerId) {
        String sql = "SELECT booking_id, flight_id, booking_date, seat_class, total_price, payment_status FROM booking WHERE customer_id = ? ORDER BY booking_date DESC";
        customerBookingHistoryTableModel.setRowCount(0);
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, customerId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getString("booking_id"));
                row.add(rs.getString("flight_id"));
                row.add(rs.getTimestamp("booking_date"));
                row.add(rs.getString("seat_class"));
                row.add(String.format("%.2f", rs.getDouble("total_price")));
                row.add(rs.getString("payment_status"));
                customerBookingHistoryTableModel.addRow(row);
            }
        } catch (SQLException ex) {
            System.err.println("Error loading booking history: " + ex.getMessage());
            
        }
    }

    private void loadCustomerPaymentHistory(Connection conn, String customerId) {
        String sql = "SELECT p.payment_id, p.booking_id, p.amount, p.payment_date, p.payment_method, p.payment_status " +
                     "FROM payment p JOIN booking b ON p.booking_id = b.booking_id " +
                     "WHERE b.customer_id = ? ORDER BY p.payment_date DESC";
        customerPaymentHistoryTableModel.setRowCount(0);
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, customerId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getString("payment_id"));
                row.add(rs.getString("booking_id"));
                row.add(String.format("%.2f", rs.getDouble("amount")));
                row.add(rs.getTimestamp("payment_date"));
                row.add(rs.getString("payment_method"));
                row.add(rs.getString("payment_status"));
                customerPaymentHistoryTableModel.addRow(row);
            }
        } catch (SQLException ex) {
            System.err.println("Error loading payment history: " + ex.getMessage());
            
        }
    }

   private void requestBlockCustomer() {
    if (viewedCustomerId == null) return;

    
    String message = "Block request for customer: " + viewedCustomerId;
    
    
    insertSimpleNotification(viewedCustomerId, message, "BlockRequest");
    
    JOptionPane.showMessageDialog(this, 
        "Block request recorded", 
        "Request Sent", 
        JOptionPane.INFORMATION_MESSAGE);
}

private void insertSimpleNotification(String userId, String message, String type) {
    String sql = "INSERT INTO notifications (notification_id, user_id, message, notification_time, sender_role) " +
                 "VALUES (?, ?, ?, NOW(), ?)";
    
    Connection conn = DatabaseConnection.connect();
    if (conn == null) return;

    try {
        
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("SET FOREIGN_KEY_CHECKS=0");
        }

        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "NOTIF" + UUID.randomUUID().toString().substring(0, 10));
            pstmt.setString(2, userId);
            pstmt.setString(3, message);
            pstmt.setString(4, type);
            pstmt.executeUpdate();
        }

        
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("SET FOREIGN_KEY_CHECKS=1");
        }
    } catch (SQLException ex) {
        System.err.println("Error inserting notification: " + ex.getMessage());
    } finally {
        closeConnection(conn);
    }
}
    
    private void generateBookingReport() {
    String bookingId = reportBookingIdFilter.getText().trim();
    String customerId = reportCustomerIdFilter.getText().trim();
    String flightId = reportFlightIdFilter.getText().trim();
    Date startDate = (Date) reportStartDateSpinner.getValue();
    Date endDate = (Date) reportEndDateSpinner.getValue();
    reportResultsTableModel.setRowCount(0);

    // Updated SQL to match exact schema - removed payment join as it's not needed for basic booking info
    StringBuilder sqlBuilder = new StringBuilder("SELECT booking_id, customer_id, flight_id, booking_date, seat_class, total_price, payment_status, special_requests " +
                                               "FROM booking WHERE 1=1 "); 
    List<Object> params = new ArrayList<>();

    if (!bookingId.isEmpty()) {
        sqlBuilder.append(" AND booking_id LIKE ?");
        params.add("%" + bookingId + "%");
    }
    if (!customerId.isEmpty()) {
        sqlBuilder.append(" AND customer_id LIKE ?");
        params.add("%" + customerId + "%");
    }
    if (!flightId.isEmpty()) {
        sqlBuilder.append(" AND flight_id LIKE ?");
        params.add("%" + flightId + "%");
    }
    if (startDate != null) {
        sqlBuilder.append(" AND booking_date >= ?");
        params.add(new Timestamp(startDate.getTime()));
    }
    if (endDate != null) {
        long oneDay = 24 * 60 * 60 * 1000;
        sqlBuilder.append(" AND booking_date < ?");
        params.add(new Timestamp(endDate.getTime() + oneDay));
    }
    sqlBuilder.append(" ORDER BY booking_date DESC");

    Connection conn = DatabaseConnection.connect();
    if (conn == null) return;

    try (PreparedStatement pstmt = conn.prepareStatement(sqlBuilder.toString())) {
        for (int i = 0; i < params.size(); i++) {
            pstmt.setObject(i + 1, params.get(i));
        }
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            Vector<Object> row = new Vector<>();
            row.add(rs.getString("booking_id"));
            row.add(rs.getString("customer_id"));
            row.add(rs.getString("flight_id"));
            row.add(rs.getTimestamp("booking_date"));
            row.add(rs.getString("seat_class"));
            row.add(String.format("%.2f", rs.getDouble("total_price")));
            row.add(rs.getString("payment_status"));
            row.add(rs.getString("special_requests")); // Added special_requests from schema
            reportResultsTableModel.addRow(row);
        }
        if (reportResultsTableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No bookings found matching the criteria.", "Report Results", JOptionPane.INFORMATION_MESSAGE);
        }
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Error generating report: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
    } finally {
        closeConnection(conn);
    }
}

    private void exportReportToTxt() {
        if (reportResultsTableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No report data to export.", "Export Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Report As TXT");
        fileChooser.setSelectedFile(new java.io.File("BookingReport_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".txt"));
        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            java.io.File fileToSave = fileChooser.getSelectedFile();
            String filePath = fileToSave.getAbsolutePath();

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                
                for (int i = 0; i < reportResultsTableModel.getColumnCount(); i++) {
                    writer.write(String.format("%-20s", reportResultsTableModel.getColumnName(i))); 
                }
                writer.newLine();
                writer.write("-".repeat(reportResultsTableModel.getColumnCount() * 20));
                writer.newLine();

                
                for (int row = 0; row < reportResultsTableModel.getRowCount(); row++) {
                    for (int col = 0; col < reportResultsTableModel.getColumnCount(); col++) {
                        Object value = reportResultsTableModel.getValueAt(row, col);
                        writer.write(String.format("%-20s", value != null ? value.toString() : ""));
                    }
                    writer.newLine();
                }
                writer.flush();
                JOptionPane.showMessageDialog(this, "Report exported successfully to: " + filePath, "Export Success", JOptionPane.INFORMATION_MESSAGE);

                
                saveFilePathToManager(filePath, "TXT_Report");

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error exporting report: " + ex.getMessage(), "Export Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    private void saveFilePathToManager(String filePath, String fileType) {
        String sql = "INSERT INTO file_manager (file_path, file_type) VALUES (?, ?)";
        Connection conn = DatabaseConnection.connect();
        if (conn == null) {
            System.err.println("Failed to save file path to manager: DB connection failed.");
            return;
        }
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, filePath);
            pstmt.setString(2, fileType);
            pstmt.executeUpdate();
            System.out.println("Report path saved to file_manager: " + filePath);
        } catch (SQLException ex) {
            System.err.println("Error saving file path to file_manager: " + ex.getMessage());
            
        } finally {
            closeConnection(conn);
        }
    }

    
    private void searchBookingForDiscount() {
        String criteria = (String) discountSearchCriteriaComboBox.getSelectedItem(); 
        String value = discountSearchInputTextField.getText().trim();
        clearDiscountForm();

        if (value.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a Booking ID.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String sql = "SELECT booking_id, customer_id, flight_id, total_price, payment_status FROM booking WHERE booking_id = ?";

        Connection conn = DatabaseConnection.connect();
        if (conn == null) return;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, value);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                currentBookingIdForDiscount = rs.getString("booking_id");
                currentCustomerIdForDiscount = rs.getString("customer_id");
                String flightId = rs.getString("flight_id");
                currentBookingPriceForDiscount = rs.getDouble("total_price");
                String status = rs.getString("payment_status");

                if ("Completed".equals(status) || "Cancelled".equals(status) || "Refunded".equals(status)) {
                    JOptionPane.showMessageDialog(this, "Cannot apply discount to a booking that is already " + status + ".", "Discount Error", JOptionPane.WARNING_MESSAGE);
                    clearDiscountForm();
                    return;
                }

                discountBookingIdLabel.setText("Booking ID: " + currentBookingIdForDiscount);
                discountCustomerIdLabel.setText("Customer ID: " + currentCustomerIdForDiscount);
                discountFlightIdLabel.setText("Flight ID: " + flightId);
                discountCurrentPriceLabel.setText(String.format("Current Price: $%.2f", currentBookingPriceForDiscount));

                
                discountTypeComboBox.setEnabled(true);
                discountValueTextField.setEnabled(true);
                discountReasonTextArea.setEnabled(true);
                discountPreviewButton.setEnabled(true);
                applyDiscountButton.setEnabled(true);

            } else {
                JOptionPane.showMessageDialog(this, "Booking ID not found.", "Search Result", JOptionPane.INFORMATION_MESSAGE);
                clearDiscountForm();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error searching booking: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
            clearDiscountForm();
        } finally {
            closeConnection(conn);
        }
    }

    private void clearDiscountForm() {
        currentBookingIdForDiscount = null;
        currentCustomerIdForDiscount = null;
        currentBookingPriceForDiscount = 0.0;
        discountBookingIdLabel.setText("Booking ID: -");
        discountCustomerIdLabel.setText("Customer ID: -");
        discountFlightIdLabel.setText("Flight ID: -");
        discountCurrentPriceLabel.setText("Current Price: $0.00");
        discountNewPriceLabel.setText("New Price: $0.00");
        discountValueTextField.setText("");
        discountReasonTextArea.setText("");

        
        discountTypeComboBox.setEnabled(false);
        discountValueTextField.setEnabled(false);
        discountReasonTextArea.setEnabled(false);
        discountPreviewButton.setEnabled(false);
        applyDiscountButton.setEnabled(false);
    }

    private void previewDiscount() {
        if (currentBookingIdForDiscount == null) return;

        String type = (String) discountTypeComboBox.getSelectedItem();
        String valueStr = discountValueTextField.getText().trim();
        double value;

        try {
            value = Double.parseDouble(valueStr);
            if (value < 0) throw new NumberFormatException("Discount value cannot be negative");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid discount value. Please enter a positive number.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double newPrice = currentBookingPriceForDiscount;
        if ("Percentage".equals(type)) {
            if (value > 100) {
                JOptionPane.showMessageDialog(this, "Percentage discount cannot exceed 100%.", "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            newPrice = currentBookingPriceForDiscount * (1 - (value / 100.0));
        } else { 
            if (value > currentBookingPriceForDiscount) {
                JOptionPane.showMessageDialog(this, "Fixed discount cannot exceed the current price.", "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            newPrice = currentBookingPriceForDiscount - value;
        }

        discountNewPriceLabel.setText(String.format("New Price: $%.2f", newPrice));
    }

    private void applyDiscount() {
        if (currentBookingIdForDiscount == null) return;

        
        previewDiscount();
        String newPriceStr = discountNewPriceLabel.getText().replace("New Price: $", "");
        double newPrice;
        try {
            newPrice = Double.parseDouble(newPriceStr);
        } catch (NumberFormatException | NullPointerException e) {
            JOptionPane.showMessageDialog(this, "Could not determine the new price. Please preview again.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String reason = discountReasonTextArea.getText().trim();
        if (reason.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please provide a reason for the discount.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String discountType = (String) discountTypeComboBox.getSelectedItem();
        double discountValue = Double.parseDouble(discountValueTextField.getText().trim());
        String discountId = "DISC" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        Connection conn = DatabaseConnection.connect();
        if (conn == null) return;

        try {
            conn.setAutoCommit(false);

            
            String updateBookingSql = "UPDATE booking SET total_price = ? WHERE booking_id = ?";
            try (PreparedStatement pstmtUpdate = conn.prepareStatement(updateBookingSql)) {
                pstmtUpdate.setDouble(1, newPrice);
                pstmtUpdate.setString(2, currentBookingIdForDiscount);
                pstmtUpdate.executeUpdate();
            }

            
            String insertDiscountSql = "INSERT INTO discounts (discount_id, booking_id, discount_type, discount_value, reason) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement pstmtInsert = conn.prepareStatement(insertDiscountSql)) {
                pstmtInsert.setString(1, discountId);
                pstmtInsert.setString(2, currentBookingIdForDiscount);
                pstmtInsert.setString(3, discountType);
                pstmtInsert.setDouble(4, discountValue);
                pstmtInsert.setString(5, reason);
                pstmtInsert.executeUpdate();
            }

            conn.commit();
            JOptionPane.showMessageDialog(this, "Discount applied successfully! New price: $" + String.format("%.2f", newPrice), "Success", JOptionPane.INFORMATION_MESSAGE);

            
            sendNotification(currentCustomerIdForDiscount, "A discount has been applied to your booking " + currentBookingIdForDiscount + ". New total: $" + String.format("%.2f", newPrice), "System");

            clearDiscountForm(); 

        } catch (SQLException ex) {
            try { conn.rollback(); } catch (SQLException rollbackEx) { System.err.println("Rollback failed: " + rollbackEx.getMessage()); }
            JOptionPane.showMessageDialog(this, "Error applying discount: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } finally {
            try { conn.setAutoCommit(true); } catch (SQLException ex) { /* ignore */ }
            closeConnection(conn);
        }
    }

    
    private void loadPendingRefundRequests() {
        refundRequestsTableModel.setRowCount(0);
        String sql = "SELECT b.booking_id, b.customer_id, b.flight_id, b.total_price, p.payment_date as request_date, b.special_requests " +
                     "FROM booking b JOIN payment p ON b.booking_id = p.booking_id " +
                     "WHERE b.payment_status = \'Refund Requested\' ORDER BY p.payment_date ASC";

        Connection conn = DatabaseConnection.connect();
        if (conn == null) return;

        try (PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getString("booking_id"));
                row.add(rs.getString("customer_id"));
                row.add(rs.getString("flight_id"));
                row.add(String.format("%.2f", rs.getDouble("total_price")));
                row.add(rs.getTimestamp("request_date"));
                
                String requests = rs.getString("special_requests");
                String reason = "N/A";
                if (requests != null && requests.contains("Refund Reason:")) {
                    try {
                        reason = requests.substring(requests.indexOf("Refund Reason:") + 14).trim();
                    } catch (Exception ignored) {}
                }
                row.add(reason);
                row.add("Approve Refund"); 
                refundRequestsTableModel.addRow(row);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading refund requests: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } finally {
            closeConnection(conn);
        }
    }

    private void approveRefundAction(int row) {
        String bookingId = (String) refundRequestsTableModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to approve the refund for Booking ID: " + bookingId + "?",
                "Confirm Refund Approval",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            approveRefund(bookingId);
        }
    }

    private void approveRefund(String bookingId) {
        String updateBookingSql = "UPDATE booking SET payment_status = \'Refunded\' WHERE booking_id = ? AND payment_status = \'Refund Requested\'";
        String updatePaymentSql = "UPDATE payment SET payment_status = \'Refunded\' WHERE booking_id = ? AND payment_status = \'Completed\'"; 
        String getCustomerIdSql = "SELECT customer_id FROM booking WHERE booking_id = ?";

        Connection conn = DatabaseConnection.connect();
        if (conn == null) return;

        String customerId = null;
        try {
            conn.setAutoCommit(false);

            
            try (PreparedStatement pstmtCust = conn.prepareStatement(getCustomerIdSql)) {
                pstmtCust.setString(1, bookingId);
                ResultSet rsCust = pstmtCust.executeQuery();
                if (rsCust.next()) {
                    customerId = rsCust.getString("customer_id");
                }
            }

            
            try (PreparedStatement pstmtBooking = conn.prepareStatement(updateBookingSql)) {
                pstmtBooking.setString(1, bookingId);
                int bookingRowsAffected = pstmtBooking.executeUpdate();
                if (bookingRowsAffected == 0) {
                    throw new SQLException("Failed to update booking status. Status might have changed.");
                }
            }

            
            try (PreparedStatement pstmtPayment = conn.prepareStatement(updatePaymentSql)) {
                pstmtPayment.setString(1, bookingId);
                pstmtPayment.executeUpdate(); 
            }

            

            
            conn.commit();
            JOptionPane.showMessageDialog(this, "Refund approved successfully for Booking ID: " + bookingId, "Refund Approved", JOptionPane.INFORMATION_MESSAGE);

            
            if (customerId != null) {
                sendNotification(customerId, "Your refund request for booking " + bookingId + " has been approved.", "System");
            }

            
            loadPendingRefundRequests();

        } catch (SQLException ex) {
            try { conn.rollback(); } catch (SQLException rollbackEx) { System.err.println("Rollback failed: " + rollbackEx.getMessage()); }
            JOptionPane.showMessageDialog(this, "Error approving refund: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } finally {
            try { conn.setAutoCommit(true); } catch (SQLException ex) { /* ignore */ }
            closeConnection(conn);
        }
    }

    

    private boolean sendNotification(String userId, String message, String senderRole) {
        String notificationId = "NOTIF" + UUID.randomUUID().toString().substring(0, 10);
        String sql = "INSERT INTO notifications (notification_id, user_id, message, notification_time, sender_role) VALUES (?, ?, ?, NOW(), ?)";
        Connection conn = DatabaseConnection.connect();
        if (conn == null) return false;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, notificationId);
            pstmt.setString(2, userId);
            pstmt.setString(3, message);
            pstmt.setString(4, senderRole);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            System.err.println("Error sending notification: " + ex.getMessage());
            ex.printStackTrace();
            return false;
        } finally {
            closeConnection(conn);
        }
    }

    private void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException ex) {
                System.err.println("Error closing connection: " + ex.getMessage());
            }
        }
    }

    
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer(String text) {
            setOpaque(true);
            setForeground(Color.WHITE);
            setBackground(new Color(70, 130, 180)); 
            setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
            setText(text);
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private String label;
        private boolean isPushed;
        private int editedRow;
        private java.util.function.IntConsumer action;

        public ButtonEditor(JCheckBox checkBox, String text, java.util.function.IntConsumer action) {
            super(checkBox);
            this.action = action;
            button = new JButton();
            button.setOpaque(true);
            button.setForeground(Color.WHITE);
            button.setBackground(new Color(70, 130, 180));
            button.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
            button.addActionListener(e -> fireEditingStopped());
            label = text;
            button.setText(label);
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            button.setText((value == null) ? "" : value.toString());
            editedRow = row;
            isPushed = true;
            return button;
        }

        public Object getCellEditorValue() {
            if (isPushed) {
                action.accept(editedRow);
            }
            isPushed = false;
            return label; 
        }

        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }

        @Override
        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }
    }
}

