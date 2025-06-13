package com.jeudepuzzle.controller;

import com.jeudepuzzle.model.DatabaseManager;
import com.jeudepuzzle.view.RegistrationView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;

public class RegistrationController {
    private RegistrationView registrationView;
    private DatabaseManager dbManager;

    // Basic email validation regex (can be here or in a shared utility/model)
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    );

    private boolean registeredSuccessfully = false; // State managed by Controller

    public RegistrationController(RegistrationView registrationView, DatabaseManager dbManager) {
        this.registrationView = registrationView;
        this.dbManager = dbManager;
        attachListeners();
    }

    private void attachListeners() {
        registrationView.getRegisterButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleRegister();
            }
        });

        registrationView.getCancelButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleCancel();
            }
        });
    }

    private void handleRegister() {
        String username = registrationView.getUsernameField().getText().trim();
        String password = new String(registrationView.getPasswordField().getPassword());
        String confirmPassword = new String(registrationView.getConfirmPasswordField().getPassword());
        String email = registrationView.getEmailField().getText().trim();

        // Validation Logic (Controller's responsibility)
        if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
            registrationView.showError("All fields are required.");
            return;
        }
        if (!password.equals(confirmPassword)) {
            registrationView.showError("Passwords do not match.");
            return;
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            registrationView.showError("Invalid email format.");
            return;
        }
        if (dbManager.userExists(username)) {
            registrationView.showError("Username already taken.");
            return;
        }
        if (dbManager.emailExists(email)) {
            registrationView.showError("Email already registered.");
            return;
        }

        // Interaction with Model (DatabaseManager)
        if (dbManager.addUser(username, password, email)) {
            registrationView.showMessage("Registration successful! You can now log in.");
            registeredSuccessfully = true;
            registrationView.closeDialog(); // Controller tells View to close
        } else {
            registrationView.showError("Registration failed. Please try again.");
        }
    }

    private void handleCancel() {
        registeredSuccessfully = false;
        registrationView.closeDialog(); // Controller tells View to close
    }

    // Method for the calling Controller (LoginController) to check the result
    public boolean wasRegistrationSuccessful() {
        return registeredSuccessfully;
    }

    // Method for the calling Controller to display the view
    public void showDialog() {
        registrationView.setVisible(true);
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
