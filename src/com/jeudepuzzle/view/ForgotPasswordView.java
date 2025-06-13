package com.jeudepuzzle.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ForgotPasswordView extends JDialog {

    private JTextField emailField;
    private JButton submitButton;
    private JButton cancelButton;

    public ForgotPasswordView(Frame owner) { // Removed DatabaseManager from constructor
        super(owner, "Forgot Password", true);
        initComponents();
        pack();
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Title Panel
        JLabel titleLabel = new JLabel("Password Recovery", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(titleLabel, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        emailField = new JTextField(25);
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Enter your Email:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        formPanel.add(emailField, gbc);

        add(formPanel, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        submitButton = new JButton("Submit");
        submitButton.setBackground(new Color(0, 122, 204)); // Blue
        submitButton.setForeground(Color.WHITE);

        cancelButton = new JButton("Cancel");
        cancelButton.setBackground(new Color(204, 0, 0)); // Red
        cancelButton.setForeground(Color.WHITE);

        buttonPanel.add(submitButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Action Listeners are NOT added here in the View
    }

    // Getters for the controller
    public JTextField getEmailField() { return emailField; }
    public JButton getSubmitButton() { return submitButton; }
    public JButton getCancelButton() { return cancelButton; }

    // Method for the controller to display messages
    public void showMessage(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }

    // Method for the controller to close the dialog
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