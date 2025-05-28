package airprort_system;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class FlightReservationSystem {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Jupiter Airways");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 700);
            frame.setLocationRelativeTo(null);

            // Set window icon
            try {
                URL iconUrl = FlightReservationSystem.class.getResource("logo.jfif");
                if (iconUrl != null) {
                    frame.setIconImage(new ImageIcon(iconUrl).getImage());
                } else {
                    System.err.println("Error: logo.jfif not found in classpath.");
                }
            } catch (Exception e) {
                System.err.println("Error loading icon: " + e.getMessage());
            }

            // Create card layout container
            CardLayout cardLayout = new CardLayout();
            JPanel cardPanel = new JPanel(cardLayout);
            
            // Create pages
            HomePage homePage = new HomePage(cardLayout, cardPanel);
            LoginPage loginPage = new LoginPage(cardLayout, cardPanel);
            RegisterPage registerPage = new RegisterPage(cardLayout, cardPanel);
            
            // Add pages to card layout
            cardPanel.add(homePage, "HOME");
            cardPanel.add(loginPage, "LOGIN");
            cardPanel.add(registerPage, "REGISTER");

            
            CustomerDashboardPage customerDashboard = new CustomerDashboardPage(cardLayout, cardPanel);
            AgentDashboardPage agentDashboard = new AgentDashboardPage(cardLayout, cardPanel);
            AdminDashboardPage adminDashboard = new AdminDashboardPage(cardLayout, cardPanel);

            cardPanel.add(customerDashboard, "CUSTOMER_DASHBOARD");
            cardPanel.add(agentDashboard, "AGENT_DASHBOARD");
            cardPanel.add(adminDashboard, "ADMIN_DASHBOARD");
            
            frame.add(cardPanel);
            frame.setVisible(true);
        });
    }
}
