package com.jeudepuzzle.view;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

public class CategoryChoiceView extends JFrame {

    // MODIFIED: From JButton to JPanel to hold the new components
    private JPanel animalsPanel;
    private JPanel naturePanel;
    private JPanel vehiclesPanel;
    private JPanel foodPanel;
    private JPanel sportsPanel;
    private JButton backButton;

    // Styling constants
    private static final Color PRIMARY_COLOR = new Color(70, 130, 180);
    private static final Color SECONDARY_COLOR = new Color(240, 248, 255);
    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 28);
    // NEW: Font for the text on the category panels
    private static final Font CATEGORY_FONT = new Font("Arial", Font.BOLD, 22);

    public CategoryChoiceView() {
        setTitle("Puzzle World - Choose Puzzle Category");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 750); // Adjusted size for larger panels
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        JPanel contentPane = new JPanel(new BorderLayout(10, 20)); // Increased vertical gap
        contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        contentPane.setBackground(SECONDARY_COLOR);

        JLabel titleLabel = new JLabel("Select a Category", SwingConstants.CENTER);
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(PRIMARY_COLOR);
        contentPane.add(titleLabel, BorderLayout.NORTH);

        // MODIFIED: The main panel will now hold our new image panels
        JPanel categoriesPanel = new JPanel(new GridLayout(0, 2, 20, 20)); // 2 columns, with gaps
        categoriesPanel.setBackground(SECONDARY_COLOR);

        // --- IMPORTANT ---
        // You must create a 'resources/previews/' folder in your project root
        // and place images like 'animals.jpg', 'nature.jpg', etc., inside it.
        animalsPanel = createCategoryPanel("Animals", "resources/Animals/squirrel.jpg");
        naturePanel = createCategoryPanel("Nature", "resources/Nature/forest night.jpg");
        vehiclesPanel = createCategoryPanel("Vehicles", "resources/Vehicles/Lada.jpg");
        foodPanel = createCategoryPanel("Food", "resources/Food/Pancake.jpg ");
        sportsPanel = createCategoryPanel("Sports", "resources/sports/Cricket.jpg");
        
        // A placeholder panel in case of an odd number of categories
        JPanel placeholderPanel = new JPanel();
        placeholderPanel.setOpaque(false);

        categoriesPanel.add(animalsPanel);
        categoriesPanel.add(naturePanel);
        categoriesPanel.add(vehiclesPanel);
        categoriesPanel.add(foodPanel);
        categoriesPanel.add(sportsPanel);
        categoriesPanel.add(placeholderPanel); // Keeps alignment nice

        contentPane.add(categoriesPanel, BorderLayout.CENTER);

        // Back button remains at the bottom
        JPanel navigationPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        navigationPanel.setBackground(SECONDARY_COLOR);
        backButton = new JButton("← Back to Login");
        // Simplified styling for the back button to look like a link
        backButton.setForeground(Color.GRAY);
        backButton.setFont(new Font("Arial", Font.PLAIN, 14));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setFocusPainted(false);
        navigationPanel.add(backButton);
        contentPane.add(navigationPanel, BorderLayout.SOUTH);

        setContentPane(contentPane);
    }

    // NEW: Helper method to create each category panel
    private JPanel createCategoryPanel(String categoryName, String imagePath) {
        // Load the background image
        Image backgroundImage = null;
        try {
            backgroundImage = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            System.err.println("Could not load image at path: " + imagePath);
            // Use a plain color as a fallback
        }
        final Image finalImage = backgroundImage;

        // Create a custom JPanel that will draw the image, overlay, and text
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // 1. Draw the background image, scaled to fill the panel
                if (finalImage != null) {
                    g.drawImage(finalImage, 0, 0, this.getWidth(), this.getHeight(), this);
                } else {
                    g.setColor(Color.DARK_GRAY);
                    g.fillRect(0, 0, getWidth(), getHeight());
                }

                // 2. Draw the semi-transparent overlay ("blur" effect)
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setColor(new Color(0, 0, 0, 0.5f)); // 50% transparent black
                g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
                
                // 3. Draw the category name on top, centered
                g2d.setFont(CATEGORY_FONT);
                g2d.setColor(Color.WHITE);
                FontMetrics fm = g2d.getFontMetrics();
                int textWidth = fm.stringWidth(categoryName);
                int textHeight = fm.getAscent();
                int x = (getWidth() - textWidth) / 2;
                int y = (getHeight() + textHeight) / 2;
                g2d.drawString(categoryName, x, y);
                
                g2d.dispose();
            }
        };

        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panel.setPreferredSize(new Dimension(250, 120)); // Set a fixed size for the panels

        // Add a hover effect directly in the view (visual only)
        final Border defaultBorder = BorderFactory.createEmptyBorder(3,3,3,3);
        final Border hoverBorder = BorderFactory.createLineBorder(PRIMARY_COLOR, 3);
        panel.setBorder(defaultBorder);

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                panel.setBorder(hoverBorder);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                panel.setBorder(defaultBorder);
            }
        });

        return panel;
    }

    // MODIFIED: Getters now return JPanels instead of JButtons
    public JPanel getAnimalsPanel() { return animalsPanel; }
    public JPanel getNaturePanel() { return naturePanel; }
    public JPanel getVehiclesPanel() { return vehiclesPanel; }
    public JPanel getFoodPanel() { return foodPanel; }
    public JPanel getSportsPanel() { return sportsPanel; }
    public JButton getBackButton() { return backButton; }
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