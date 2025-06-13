package com.jeudepuzzle.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.regex.Pattern; // Keep Pattern import for potential validation in View (though Controller is better)

public class RegistrationView extends JDialog { // Changed from JDialog to JDialog to be a view component
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextField emailField;
    private JButton registerButton;
    private JButton cancelButton;

    public RegistrationView(Frame owner) { // Removed DatabaseManager from constructor
        super(owner, "Register New User", true);
        initComponents();
        pack();
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Title Panel
        JLabel titleLabel = new JLabel("Create Your Account", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(titleLabel, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        confirmPasswordField = new JPasswordField(20);
        emailField = new JTextField(20);

        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; formPanel.add(usernameField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; formPanel.add(passwordField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; formPanel.add(new JLabel("Confirm Password:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; formPanel.add(confirmPasswordField, gbc);
        gbc.gridx = 0; gbc.gridy = 3; formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; formPanel.add(emailField, gbc);

        add(formPanel, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        registerButton = new JButton("Register");
        registerButton.setBackground(new Color(0, 153, 51)); // Green
        registerButton.setForeground(Color.WHITE);
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));

        cancelButton = new JButton("Cancel");
        cancelButton.setBackground(new Color(204, 0, 0)); // Red
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFont(new Font("Arial", Font.BOLD, 14));

        buttonPanel.add(registerButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Action Listeners are NOT added here in the View
    }

    // Getters for the controller
    public JTextField getUsernameField() { return usernameField; }
    public JPasswordField getPasswordField() { return passwordField; }
    public JPasswordField getConfirmPasswordField() { return confirmPasswordField; }
    public JTextField getEmailField() { return emailField; }
    public JButton getRegisterButton() { return registerButton; }
    public JButton getCancelButton() { return cancelButton; }

    // Method to display error messages (can be called by controller)
    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    // Method to display success messages (can be called by controller)
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }

    // Method to close the dialog (can be called by controller)
    public void closeDialog() {
        this.dispose();
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