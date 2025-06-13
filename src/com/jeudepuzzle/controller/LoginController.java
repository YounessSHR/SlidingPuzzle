package com.jeudepuzzle.controller;

import javax.swing.*;
import com.jeudepuzzle.model.DatabaseManager;
import com.jeudepuzzle.model.User;
import com.jeudepuzzle.view.CategoryChoiceView;
import com.jeudepuzzle.view.ForgotPasswordView;
import com.jeudepuzzle.view.LoginView;
import com.jeudepuzzle.view.RegistrationView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginController {
    private LoginView loginView;
    private DatabaseManager dbManager;

    public LoginController(LoginView loginView, DatabaseManager dbManager) {
        this.loginView = loginView;
        this.dbManager = dbManager;

        // Add action listeners to the buttons
        this.loginView.getLoginButton().addActionListener(e -> handleLogin());
        this.loginView.getRegisterButton().addActionListener(e -> handleRegister());
        this.loginView.getForgotPasswordButton().addActionListener(e -> handleForgotPassword());
        this.loginView.getGuestButton().addActionListener(e -> handleGuestLogin());
        this.loginView.getAboutButton().addActionListener(e -> handleAbout());
    }

    private void handleLogin() {
        String username = loginView.getUsernameField().getText();
        String password = new String(loginView.getPasswordField().getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            loginView.showMessage("Username and Password cannot be empty.", "Login Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        User user = dbManager.authenticateUser(username, password);
        if (user != null) {
            loginView.showMessage("Login Successful! Welcome " + user.getUsername() + "\nScore: " + user.getScore(), "Success", JOptionPane.INFORMATION_MESSAGE);
            CategoryChoiceView categoryChoiceView = new CategoryChoiceView();
            // MODIFIED: Pass the LoginView instance to the controller
            CategoryChoiceController categoryChoiceController = new CategoryChoiceController(categoryChoiceView, user, dbManager, this.loginView);
            categoryChoiceView.setVisible(true);
            // MODIFIED: Hide the login window instead of disposing it
            loginView.setVisible(false); 
        } else {
            loginView.showMessage("Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleRegister() {
        RegistrationView registrationView = new RegistrationView(loginView);
        RegistrationController registrationController = new RegistrationController(registrationView, dbManager);
        registrationController.showDialog();
    }

    private void handleForgotPassword() {
        ForgotPasswordView forgotPasswordView = new ForgotPasswordView(loginView);
        ForgotPasswordController forgotPasswordController = new ForgotPasswordController(forgotPasswordView, dbManager);
        forgotPasswordController.showDialog();
    }

    private void handleGuestLogin() {
        loginView.showMessage("Continuing as Guest will not calculate your progress", "Guest Mode", JOptionPane.INFORMATION_MESSAGE);
        CategoryChoiceView categoryChoiceView = new CategoryChoiceView();
        // MODIFIED: Pass the LoginView instance to the controller
        CategoryChoiceController categoryChoiceController = new CategoryChoiceController(categoryChoiceView, null, dbManager, this.loginView);
        categoryChoiceView.setVisible(true);
        // MODIFIED: Hide the login window instead of disposing it
        loginView.setVisible(false);
    }

    private void handleAbout() {
        String aboutMessage = "PuzzleWorld\n\n" +
                              "Developed by:\n" +
                              "- Abdelali ITTAS\n" +
                              "- Abdelouahab Mjahdi\n" +
                              "- Youness SAHRAOUI\n" +
                              "- Mohammed MAATAOUI BELABBES\n" +
                              "\n\n" +
                              "Version: beta\n" +
                              "(c) 2025";
        loginView.showMessage(aboutMessage, "About Puzzle World", JOptionPane.INFORMATION_MESSAGE);
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

