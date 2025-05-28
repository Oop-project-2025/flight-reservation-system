package airprort_system;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class FlightReservationSystem {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Your Gateway to Jupiter");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 700);
            frame.setLocationRelativeTo(null);

            try {
                URL iconUrl = FlightReservationSystem.class.getResource("bg-logo.png");
                if (iconUrl != null) {
                    frame.setIconImage(new ImageIcon(iconUrl).getImage());
                } else {
                    System.err.println("Error: logo not found in classpath.");
                }
            } catch (Exception e) {
                System.err.println("Error loading icon: " + e.getMessage());
            }

            Image backgroundImage = null;
            try {
                URL imageUrl = FlightReservationSystem.class.getResource("kk.jfif.jpg");
                if (imageUrl != null) {
                    backgroundImage = new ImageIcon(imageUrl).getImage();
                } else {
                    System.err.println("Error: Background image not found at background.jpg");
                }
            } catch (Exception e) {
                System.err.println("Error loading background image: " + e.getMessage());
                e.printStackTrace();
            }

            final Image finalBackgroundImage = backgroundImage;
            JPanel contentPaneWithBackground = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    if (finalBackgroundImage != null) {
                        g.drawImage(finalBackgroundImage, 0, 0, getWidth(), getHeight(), this);
                    }
                }
            };
            contentPaneWithBackground.setLayout(new BorderLayout());

            CardLayout cardLayout = new CardLayout();
            JPanel cardPanel = new JPanel(cardLayout);
            cardPanel.setOpaque(false);
            
            HomePage homePage = new HomePage(cardLayout, cardPanel);
            LoginPage loginPage = new LoginPage(cardLayout, cardPanel);
            RegisterPage registerPage = new RegisterPage(cardLayout, cardPanel);
            
            cardPanel.add(homePage, "HOME");
            cardPanel.add(loginPage, "LOGIN");
            cardPanel.add(registerPage, "REGISTER");

            CustomerDashboardPage customerDashboard = new CustomerDashboardPage(cardLayout, cardPanel);
            AgentDashboardPage agentDashboard = new AgentDashboardPage(cardLayout, cardPanel);
            AdminDashboardPage adminDashboard = new AdminDashboardPage(cardLayout, cardPanel);

            cardPanel.add(customerDashboard, "CUSTOMER_DASHBOARD");
            cardPanel.add(agentDashboard, "AGENT_DASHBOARD");
            cardPanel.add(adminDashboard, "ADMIN_DASHBOARD");
            
            contentPaneWithBackground.add(cardPanel, BorderLayout.CENTER);

            frame.setContentPane(contentPaneWithBackground);
            frame.setVisible(true);
        });
    }
}