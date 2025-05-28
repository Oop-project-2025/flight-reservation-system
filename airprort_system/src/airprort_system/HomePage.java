package airprort_system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.net.URL;

public class HomePage extends JPanel {
    public HomePage(CardLayout cardLayout, JPanel parent) {
        setLayout(new BorderLayout());
        
        // Background panel with image
        JPanel backgroundPanel = new JPanel() {
            private Image backgroundImage;
            private Image logoImage;
            
            {
                try {
                    // Updated resource paths assuming they are in the same package or accessible
                    URL bgUrl = getClass().getResource("kk.jfif"); // Assuming kk.jfif is available
                    URL logoUrl = getClass().getResource("bg-logo.png"); // Using the uploaded bg-logo.png
                    
                    if (bgUrl != null) backgroundImage = new ImageIcon(bgUrl).getImage();
                    else System.err.println("Error: kk.jfif not found in classpath.");

                    if (logoUrl != null) {
                        ImageIcon logoIcon = new ImageIcon(logoUrl);
                        logoImage = logoIcon.getImage().getScaledInstance(300, -1, Image.SCALE_SMOOTH);
                    } else {
                        System.err.println("Error: bg-logo.png not found in classpath.");
                    }
                } catch (Exception e) {
                    System.err.println("Error loading images: " + e.getMessage());
                }
            }
            
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                
                if (backgroundImage != null) {
                    g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                } else {
                    // Fallback if image not found
                    g2d.setColor(new Color(20, 30, 50));
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                }

                // Draw logo in the center top
                if (logoImage != null) {
                    int x = (getWidth() - logoImage.getWidth(this)) / 2;
                    int y = 30; // Some padding from the top
                    g2d.drawImage(logoImage, x, y, this);
                }
            }
        };
        backgroundPanel.setLayout(new BorderLayout());
        
        // Content panel for title and buttons
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setOpaque(false); // Make it transparent to show background
        
        // Title Label
        JLabel titleLabel = new JLabel("Your Gateway to Jupiter ");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 48));
        titleLabel.setForeground(new Color(255, 255, 255, 220)); // Slightly transparent white
        
        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 20));
        buttonPanel.setOpaque(false); // Make it transparent
        
        JButton loginButton = createStyledButton("LOGIN", new Color(0, 102, 204)); // Blue
        JButton registerButton = createStyledButton("REGISTER", new Color(34, 139, 34)); // Green
        
        loginButton.addActionListener((ActionEvent e) -> {
            cardLayout.show(parent, "LOGIN");
        });
        
        registerButton.addActionListener((ActionEvent e) -> {
            cardLayout.show(parent, "REGISTER");
        });
        
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        
        // Layout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(120, 0, 50, 0); // Top padding for title
        contentPanel.add(titleLabel, gbc);
        
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 0, 0); // No extra padding for buttons
        contentPanel.add(buttonPanel, gbc);
        
        backgroundPanel.add(contentPanel, BorderLayout.CENTER);
        add(backgroundPanel);
    }
    
    private JButton createStyledButton(String text, Color baseColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setPreferredSize(new Dimension(180, 50));
        button.setForeground(Color.WHITE);
        button.setBackground(baseColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 255, 255, 120), 1), // Light border
            BorderFactory.createEmptyBorder(10, 25, 10, 25)
        ));
        
        // Hover effect
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
}
