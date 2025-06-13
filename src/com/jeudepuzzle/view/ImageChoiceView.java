package com.jeudepuzzle.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class ImageChoiceView extends JFrame {

    private JPanel imagePanel;
    private JButton backButton;

    // Your desired dark theme style constants
    private static final Color PRIMARY_COLOR = new Color(60, 70, 90);
    private static final Color SECONDARY_COLOR = new Color(80, 90, 110);
    private static final Color ACCENT_COLOR = new Color(255, 150, 50);
    private static final Color TEXT_COLOR = Color.WHITE;
    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 24);

    public ImageChoiceView() {
        setTitle("Choose Puzzle Image");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 550); // Increased height slightly for the button
        setLocationRelativeTo(null);

        // --- Main container with dark background ---
        JPanel mainPanel = new JPanel(new BorderLayout(10, 15)); // Gaps between components
        mainPanel.setBackground(PRIMARY_COLOR);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // --- Top panel to hold both title and back button ---
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false); // Make it transparent to show mainPanel's color

        JLabel titleLabel = new JLabel("Select an Image");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topPanel.add(titleLabel, BorderLayout.CENTER);

        // --- Back Button Creation and Styling ---
        backButton = new JButton("← Back");
        backButton.setFont(new Font("Arial", Font.PLAIN, 14));
        backButton.setForeground(Color.LIGHT_GRAY);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Style it to look like a subtle link
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setFocusPainted(false);
        backButton.setOpaque(false);
        
        // Add a hover effect for interactivity
        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                backButton.setForeground(ACCENT_COLOR); // Highlight on hover
            }
            @Override
            public void mouseExited(MouseEvent e) {
                backButton.setForeground(Color.LIGHT_GRAY); // Revert on exit
            }
        });

        // Add the button to the left side of the top panel
        topPanel.add(backButton, BorderLayout.WEST);

        // Add the combined top panel to the main frame
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // --- Image Panel Setup ---
        // This panel will be populated by the controller
        imagePanel = new JPanel(new GridLayout(0, 3, 15, 15)); // 3 columns, dynamic rows, with gaps
        imagePanel.setBackground(SECONDARY_COLOR);
        imagePanel.setBorder(new EmptyBorder(15, 15, 15, 15)); // Padding inside the image area

        // Put the image panel in a scroll pane in case of many images
        JScrollPane scrollPane = new JScrollPane(imagePanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(SECONDARY_COLOR);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Smoother scrolling

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Set the final content pane for the JFrame
        setContentPane(mainPanel);
    }

    /**
     * A safe method that takes fully prepared JLabels from the controller and displays them.
     * It simply clears the old content and adds the new components.
     *
     * @param labels The list of JLabels (with listeners already attached) to display.
     */
    public void displayImageLabels(List<JLabel> labels) {
        imagePanel.removeAll();
        for (JLabel label : labels) {
            imagePanel.add(label);
        }
        imagePanel.revalidate();
        imagePanel.repaint();
    }

    /**
     * Provides the controller with access to the back button so it can add its action listener.
     * This is the only public method the controller needs to make the button functional.
     *
     * @return The back button component.
     */
    public JButton getBackButton() {
        return backButton;
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