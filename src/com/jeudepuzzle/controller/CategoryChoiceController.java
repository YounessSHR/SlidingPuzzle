package com.jeudepuzzle.controller;

import com.jeudepuzzle.model.DatabaseManager;
import com.jeudepuzzle.model.User;
import com.jeudepuzzle.view.CategoryChoiceView;
import com.jeudepuzzle.view.ImageChoiceView;
import com.jeudepuzzle.view.LoginView;

// NEW: Import classes needed for mouse events and JPanels
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;

public class CategoryChoiceController {
    private CategoryChoiceView categoryChoiceView;
    private User user;
    private DatabaseManager dbManager;
    private LoginView loginView;

    public CategoryChoiceController(CategoryChoiceView categoryChoiceView, User user, DatabaseManager dbManager, LoginView loginView) {
        this.categoryChoiceView = categoryChoiceView;
        this.user = user;
        this.dbManager = dbManager;
        this.loginView = loginView;
        attachListeners();
    }
    

    private void handleCategorySelection(String categoryName) {
    	
        System.out.println("Category selected: " + categoryName);

        ImageChoiceView imageChoiceView = new ImageChoiceView();
        
        ImageChoiceController imageChoiceController = new ImageChoiceController(
            imageChoiceView,
            user,
            categoryName,
            dbManager,
            this.categoryChoiceView 
        );
        
        imageChoiceController.showView();
        
        categoryChoiceView.setVisible(false);
    }

    private void handleGoBack() {
        loginView.setVisible(true);
        categoryChoiceView.dispose();
    }
    
    // *** THIS METHOD IS THE FIX ***
    // It now listens for MOUSE CLICKS on JPanels instead of ACTIONS on JButtons.
    private void attachListeners() {
        // Create a single, reusable mouse listener for all category panels.
        MouseAdapter categoryClickListener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Get the specific panel that was clicked
                JPanel sourcePanel = (JPanel) e.getSource();
                
                // Check which panel it was and call the handler with the correct category name
                if (sourcePanel == categoryChoiceView.getAnimalsPanel()) {
                    handleCategorySelection("Animals");
                } else if (sourcePanel == categoryChoiceView.getNaturePanel()) {
                    handleCategorySelection("Nature");
                } else if (sourcePanel == categoryChoiceView.getVehiclesPanel()) {
                    handleCategorySelection("Vehicles");
                } else if (sourcePanel == categoryChoiceView.getFoodPanel()) {
                    handleCategorySelection("Food");
                } else if (sourcePanel == categoryChoiceView.getSportsPanel()) {
                    handleCategorySelection("Sports");
                }
            }
        };

        // Add the same listener to all five category panels
        categoryChoiceView.getAnimalsPanel().addMouseListener(categoryClickListener);
        categoryChoiceView.getNaturePanel().addMouseListener(categoryClickListener);
        categoryChoiceView.getVehiclesPanel().addMouseListener(categoryClickListener);
        categoryChoiceView.getFoodPanel().addMouseListener(categoryClickListener);
        categoryChoiceView.getSportsPanel().addMouseListener(categoryClickListener);

        // The back button is still a JButton, so its listener remains the same
        categoryChoiceView.getBackButton().addActionListener(e -> handleGoBack());
    }

    public void showView() {
        categoryChoiceView.setVisible(true);
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