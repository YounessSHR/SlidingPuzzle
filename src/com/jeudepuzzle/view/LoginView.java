package com.jeudepuzzle.view;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.imageio.ImageIO;

public class LoginView extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton, registerButton, forgotPasswordButton, guestButton, aboutButton;

    // =========================================================================
    // ===       MODERN STYLE GUIDE (MATCHING THE MAIN MENU)                 ===
    // =========================================================================
    private static final Color COLOR_BACKGROUND_DARK = new Color(15, 18, 28);
    private static final Color COLOR_TEXT_LIGHT = new Color(220, 220, 225);
    private static final Color COLOR_PRIMARY_ACCENT = new Color(0, 150, 255);
    private static final Color COLOR_PRIMARY_ACCENT_HOVER = new Color(50, 170, 255);
    private static final Color COLOR_SECONDARY_ACCENT = new Color(255, 99, 71); // A vibrant orange/tomato for Register
    private static final Color COLOR_SECONDARY_ACCENT_HOVER = new Color(255, 120, 90);
    private static final Color COLOR_TERTIARY_ACCENT = new Color(80, 85, 100); // Muted gray for guest/about
    private static final Color COLOR_TERTIARY_ACCENT_HOVER = new Color(100, 105, 120);

    private static final Font FONT_LABEL = new Font("Segoe UI", Font.BOLD, 15);
    private static final Font FONT_BUTTON = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font FONT_TITLE = new Font("Segoe UI Semibold", Font.BOLD, 28);
    private static final Font FONT_FIELD = new Font("Segoe UI", Font.PLAIN, 16);
    // =========================================================================


    public LoginView() {
        setTitle("Puzzle World - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 650);
        setLocationRelativeTo(null);
        setResizable(false);

        // Set the animated starfield panel as the main content pane
        BackgroundPanel backgroundPanel = new BackgroundPanel(500, 650);
        backgroundPanel.setLayout(new BorderLayout(10, 10));
        setContentPane(backgroundPanel);

        initComponents();
    }

    private void initComponents() {
        // The main layout remains BorderLayout, but on the transparent panels

        // --- Top Panel for Logo (now transparent) ---
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        logoPanel.setOpaque(false); // Make transparent
        logoPanel.setBorder(new EmptyBorder(20, 0, 10, 0));
        try {
            URL logoUrl = getClass().getClassLoader().getResource("logo.png");
            if (logoUrl != null) {
                ImageIcon originalIcon = new ImageIcon(ImageIO.read(logoUrl));
                Image scaledImage = originalIcon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
                logoPanel.add(new JLabel(new ImageIcon(scaledImage)));
            }
        } catch (IOException e) { e.printStackTrace(); }
        // Add logoPanel to the North of the main BackgroundPanel
        getContentPane().add(logoPanel, BorderLayout.NORTH);


        // --- Center Panel for Login Form (now transparent) ---
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false); // Make transparent
        centerPanel.setBorder(new EmptyBorder(20, 50, 20, 50));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 8, 10, 8); // Increased vertical spacing
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Player Login");
        titleLabel.setFont(FONT_TITLE);
        titleLabel.setForeground(COLOR_TEXT_LIGHT); // Use light text
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        centerPanel.add(titleLabel, gbc);
        gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.WEST;

        // *** MODIFICATION: Added trailing spaces to the label text to make it longer ***
        JLabel userLabel = new JLabel("Username:  ");
        userLabel.setFont(FONT_LABEL);
        userLabel.setForeground(COLOR_TEXT_LIGHT); // Use light text
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.weightx = 0.0; // Ensure the label column doesn't expand
        centerPanel.add(userLabel, gbc);

        usernameField = createStyledTextField();
        gbc.gridx = 1; gbc.gridy = 1;
        gbc.weightx = 1.0; // Allow the text field column to expand and fill space
        centerPanel.add(usernameField, gbc);

        // *** MODIFICATION: Added trailing spaces to the label text to make it longer ***
        JLabel passLabel = new JLabel("Password:  ");
        passLabel.setFont(FONT_LABEL);
        passLabel.setForeground(COLOR_TEXT_LIGHT); // Use light text
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.weightx = 0.0; // Ensure the label column doesn't expand
        centerPanel.add(passLabel, gbc);

        passwordField = createStyledPasswordField();
        gbc.gridx = 1; gbc.gridy = 2;
        gbc.weightx = 1.0; // Allow the text field column to expand and fill space
        centerPanel.add(passwordField, gbc);

        // Reset weightx so it doesn't affect the buttons
        gbc.weightx = 0.0;

        // Buttons in the center form
        loginButton = createModernButton("Login", COLOR_PRIMARY_ACCENT, COLOR_PRIMARY_ACCENT_HOVER);
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.ipady = 10;
        centerPanel.add(loginButton, gbc);

        registerButton = createModernButton("Register", COLOR_SECONDARY_ACCENT, COLOR_SECONDARY_ACCENT_HOVER);
        gbc.gridx = 0; gbc.gridy = 4;
        centerPanel.add(registerButton, gbc);

        forgotPasswordButton = new JButton("Forgot Password?");
        forgotPasswordButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        forgotPasswordButton.setForeground(COLOR_PRIMARY_ACCENT); // Use accent color
        forgotPasswordButton.setBorderPainted(false);
        forgotPasswordButton.setContentAreaFilled(false);
        forgotPasswordButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2; gbc.ipady = 0; gbc.anchor = GridBagConstraints.CENTER;
        centerPanel.add(forgotPasswordButton, gbc);

        getContentPane().add(centerPanel, BorderLayout.CENTER);

        // --- Bottom Panel for Guest and About (now transparent) ---
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setOpaque(false); // Make transparent
        bottomPanel.setBorder(new EmptyBorder(10, 20, 20, 20));

        guestButton = createModernButton("Continue as Guest", COLOR_TERTIARY_ACCENT, COLOR_TERTIARY_ACCENT_HOVER);
        guestButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        bottomPanel.add(guestButton, BorderLayout.CENTER);

        aboutButton = createModernButton("About", COLOR_TERTIARY_ACCENT, COLOR_TERTIARY_ACCENT_HOVER);
        aboutButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        bottomPanel.add(aboutButton, BorderLayout.EAST);

        getContentPane().add(bottomPanel, BorderLayout.SOUTH);
    }

    private JButton createModernButton(String text, Color bgColor, Color hoverColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isRollover()) g2.setColor(hoverColor); else g2.setColor(bgColor);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20)); // Slightly less round
                super.paintComponent(g);
                g2.dispose();
            }
        };
        button.setFont(FONT_BUTTON);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void styleTextField(JTextField field) {
        field.setFont(FONT_FIELD);
        field.setBackground(new Color(30, 35, 45)); // Dark field background
        field.setForeground(COLOR_TEXT_LIGHT); // Light text
        field.setCaretColor(COLOR_PRIMARY_ACCENT); // Blinking cursor color
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, COLOR_TERTIARY_ACCENT), // Bottom line border
                BorderFactory.createEmptyBorder(5, 5, 5, 5) // Inner padding
        ));
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField(20);
        styleTextField(field);
        return field;
    }

    private JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField(20);
        styleTextField(field);
        return field;
    }

    // --- Getter methods (unchanged) ---
    public JTextField getUsernameField() { return usernameField; }
    public JPasswordField getPasswordField() { return passwordField; }
    public JButton getLoginButton() { return loginButton; }
    public JButton getRegisterButton() { return registerButton; }
    public JButton getForgotPasswordButton() { return forgotPasswordButton; }
    public JButton getGuestButton() { return guestButton; }
    public JButton getAboutButton() { return aboutButton; }
    public void showMessage(String message, String title, int messageType) { JOptionPane.showMessageDialog(this, message, title, messageType); }


    // =========================================================================
    // ===       ANIMATED STARFIELD BACKGROUND (COPIED FROM MENU)            ===
    // =========================================================================
    private static class Star { float x, y, speed, radius; }

    class BackgroundPanel extends JPanel implements ActionListener {
        private final List<Star> stars;
        private final Timer animationTimer;
        private final Random random = new Random();
        private static final int NUM_STARS = 200; // Fewer stars for a smaller window
        private static final int ANIMATION_DELAY = 16; // ~60 FPS

        public BackgroundPanel(int width, int height) {
            this.stars = new ArrayList<>();
            initializeStars(width, height);
            this.animationTimer = new Timer(ANIMATION_DELAY, this);
            this.animationTimer.start();
        }

        private void initializeStars(int width, int height) {
            for (int i = 0; i < NUM_STARS; i++) {
                Star star = new Star();
                star.x = random.nextInt(width);
                star.y = random.nextInt(height);
                star.radius = random.nextFloat() * 1.5f + 1; // Slightly smaller stars
                star.speed = star.radius * 0.15f;
                stars.add(star);
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(COLOR_BACKGROUND_DARK);
            g2d.fillRect(0, 0, getWidth(), getHeight());
            for (Star star : stars) {
                int alpha = (int) (150 * (star.radius / 2.5f)) + 50;
                g2d.setColor(new Color(220, 220, 255, alpha));
                g2d.fillOval((int)star.x, (int)star.y, (int)star.radius, (int)star.radius);
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            for (Star star : stars) {
                star.y += star.speed;
                if (star.y > getHeight()) {
                    star.y = 0;
                    star.x = random.nextInt(getWidth());
                }
            }
            repaint();
        }

        @Override
        public void removeNotify() {
            super.removeNotify();
            animationTimer.stop();
        }
    }
}
/**
 *  ************************************************ PuzzleWorld ************************************************
 *
 *  Développé par:
 *   - Abdelali ITTAS
 *   - Abdelouahab Mjahdi
 *   - Youness SAHRAOUI
 *   - Mohammed MAATAOUI BELABBES
 *
 *  Version: bêta
 *  (c) 2025
 *
 *  ************************************************************************************************************
 */