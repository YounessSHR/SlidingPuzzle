package com.jeudepuzzle.controller;

import com.jeudepuzzle.model.DatabaseManager;
import com.jeudepuzzle.model.User;
import com.jeudepuzzle.view.CategoryChoiceView;
import com.jeudepuzzle.view.DifficultyChoiceView;
import com.jeudepuzzle.view.ImageChoiceView;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImageChoiceController {

    private final ImageChoiceView imageChoiceView;
    private final User user;
    private final String selectedCategory;
    private final DatabaseManager dbManager;
    private final CategoryChoiceView categoryChoiceView;

    public ImageChoiceController(ImageChoiceView imageChoiceView, User user, String selectedCategory, DatabaseManager dbManager, CategoryChoiceView categoryChoiceView) {
        this.imageChoiceView = imageChoiceView;
        this.user = user;
        this.selectedCategory = selectedCategory;
        this.dbManager = dbManager;
        this.categoryChoiceView = categoryChoiceView;
        this.imageChoiceView.setTitle("Category: " + selectedCategory);

        initializeViewAndListeners();
    }

    private void initializeViewAndListeners() {
        // Attach listener to the static back button first
        imageChoiceView.getBackButton().addActionListener(e -> handleGoBack());

        // Load images and create fully interactive labels
        List<JLabel> imageLabels = loadAndCreateImageLabels(selectedCategory);

        // Tell the view to display the already-prepared labels
        imageChoiceView.displayImageLabels(imageLabels);
    }

    private void handleImageSelection(String imageName) {
        System.out.println("Image selected: " + imageName);
        DifficultyChoiceView difficultyChoiceView = new DifficultyChoiceView();
        DifficultyChoiceController difficultyController = new DifficultyChoiceController(
            difficultyChoiceView, user, selectedCategory, imageName, dbManager, this.imageChoiceView
        );
        difficultyController.showView();
        imageChoiceView.setVisible(false);
    }

    private void handleGoBack() {
        categoryChoiceView.setVisible(true);
        imageChoiceView.dispose();
    }
    
    // *** CORE FIX IS HERE ***
    // This method now creates the list of fully-functional JLabels.
    private List<JLabel> loadAndCreateImageLabels(String category) {
        System.out.println("Loading images and creating labels for category: " + category);
        List<JLabel> labels = new ArrayList<>();
        File categoryDir = new File("resources/" + category);

        if (categoryDir.exists() && categoryDir.isDirectory()) {
            File[] imageFiles = categoryDir.listFiles((dir, name) ->
                    name.toLowerCase().endsWith(".jpg") || name.toLowerCase().endsWith(".png"));

            if (imageFiles != null) {
                for (File imageFile : imageFiles) {
                    try {
                        // Create a single, complete JLabel with its listener attached
                        Image originalImage = ImageIO.read(imageFile);
                        Image scaledImage = originalImage.getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                        JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
                        
                        // Configure the label's appearance
                        imageLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
                        imageLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
                        
                        // Store the image name directly in the label
                        imageLabel.putClientProperty("imageName", imageFile.getName());
                        
                        // ATTACH THE LISTENER RIGHT NOW - This is the robust way
                        imageLabel.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseClicked(MouseEvent e) {
                                String imageName = (String) ((JLabel)e.getSource()).getClientProperty("imageName");
                                handleImageSelection(imageName);
                            }
                        });
                        
                        labels.add(imageLabel);

                    } catch (IOException e) {
                        System.err.println("Error loading image: " + imageFile.getName() + " - " + e.getMessage());
                    }
                }
            }
        }
        return labels;
    }

    public void showView() {
        imageChoiceView.setVisible(true);
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