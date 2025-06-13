package com.jeudepuzzle.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DifficultyChoiceView extends JFrame {

    private JButton easyButton;
    private JButton mediumButton;
    private JButton hardButton;
    private JButton backButton; // --- 1. DECLARE THE NEW BUTTON ---

    // Styling constants (matching LoginView.java)
    private static final Color PRIMARY_COLOR = new Color(70, 130, 180); // Steel Blue
    private static final Color SECONDARY_COLOR = new Color(240, 248, 255); // Alice Blue (light background)
    private static final Color ACCENT_COLOR = new Color(255, 165, 0); // Orange for accents
    private static final Font LABEL_FONT = new Font("Arial", Font.PLAIN, 14);
    private static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 14);
    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 28);

    public DifficultyChoiceView() {
        setTitle("Choose Puzzle Difficulty");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 350); // Increased height slightly for the new button
        setLocationRelativeTo(null); // Center on screen
        setLayout(new BorderLayout(10, 10)); // Main layout
        getContentPane().setBackground(SECONDARY_COLOR);

        initComponents();
    }

    private void initComponents() {
        // Title Panel
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(SECONDARY_COLOR);
        titlePanel.setBorder(new EmptyBorder(20, 0, 10, 0)); // Top padding

        JLabel titleLabel = new JLabel("Select Difficulty:");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(PRIMARY_COLOR);
        titlePanel.add(titleLabel);

        add(titlePanel, BorderLayout.NORTH);

        // Difficulty Options Panel
        JPanel optionsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20)); // FlowLayout with spacing
        optionsPanel.setBackground(SECONDARY_COLOR);
        optionsPanel.setBorder(new EmptyBorder(20, 20, 20, 20)); // Padding

        easyButton = createStyledButton("Easy", new Color(50, 205, 50)); // Lime Green
        mediumButton = createStyledButton("Medium", ACCENT_COLOR); // Orange
        hardButton = createStyledButton("Hard", new Color(220, 20, 60)); // Crimson

        optionsPanel.add(easyButton);
        optionsPanel.add(mediumButton);
        optionsPanel.add(hardButton);

        add(optionsPanel, BorderLayout.CENTER);

        // --- START OF NEW CODE ---
        // 2. CREATE AND ADD THE BACK BUTTON
        JPanel navigationPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // Align button to the right
        navigationPanel.setBackground(SECONDARY_COLOR);
        navigationPanel.setBorder(new EmptyBorder(0, 20, 10, 20)); // Add some padding

        // Create the back button using the same styling method but with a neutral color
        backButton = createStyledButton("Back", new Color(105, 105, 105)); // Dim Gray

        navigationPanel.add(backButton);

        // Add the navigation panel to the bottom of the frame
        add(navigationPanel, BorderLayout.SOUTH);
        // --- END OF NEW CODE ---
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bgColor.darker(), 1),
                BorderFactory.createEmptyBorder(10, 25, 10, 25) // Increased padding
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        // Add hover effect
        Color originalBg = button.getBackground();
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(originalBg.brighter());
            }
            public void mouseExited(MouseEvent evt) {
                button.setBackground(originalBg);
            }
        });
        return button;
    }

    // Getters for the controller
    public JButton getEasyButton() { return easyButton; }
    public JButton getMediumButton() { return mediumButton; }
    public JButton getHardButton() { return hardButton; }
    
    // --- 3. ADD A GETTER FOR THE NEW BUTTON ---
    public JButton getBackButton() {
        return backButton;
    }


    // Main method for testing (optional)
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new DifficultyChoiceView().setVisible(true);
        });
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