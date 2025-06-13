package com.jeudepuzzle.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.List;
import com.jeudepuzzle.controller.GameController;

public class GameView extends JLayeredPane {

    // Styling constants
    private static final Color PRIMARY_COLOR = new Color(70, 130, 180);
    private static final Color SECONDARY_COLOR = new Color(240, 248, 255);
    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 28);
    private static final Font SUBTITLE_FONT = new Font("Arial", Font.ITALIC, 18);
    private static final Font UI_FONT = new Font("Arial", Font.PLAIN, 16);
    private static final Font WIN_TITLE_FONT = new Font("Arial", Font.BOLD, 48);
    private static final Font WIN_SCORE_FONT = new Font("Arial", Font.PLAIN, 24);
    private static final Font WIN_BUTTON_FONT = new Font("Arial", Font.BOLD, 18);

    private JPanel mainPanel;
    private JPanel winOverlayPanel;
    private JPanel puzzleGridPanel;
    private JLabel gameTitleLabel, gameTitleLabel2, timerLabel;
    private JButton backButton;
    private JLabel winTitleLabel, winScoreLabel;
    private JButton playAgainButton, returnToMenuButton;

    // Panel for the temporary "You Win!" splash image
    private JPanel splashWinPanel;

    // To hold the pre-loaded images
    private ImageIcon blurBackgroundIcon;
    private ImageIcon winnerSplashIcon;

    public GameView() {
        // Load all static resources needed for the view
        loadResources();

        mainPanel = new JPanel();
        mainPanel.setBackground(SECONDARY_COLOR);
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        mainPanel.setBounds(0, 0, 800, 800);

        setupMainPanel();
        createWinOverlay();
        createSplashWinPanel(); // Create the new splash panel

        // Add panels to the layered pane in their respective layers
        this.add(mainPanel, JLayeredPane.DEFAULT_LAYER); // Game itself
        this.add(winOverlayPanel, JLayeredPane.MODAL_LAYER); // Final score screen
        this.add(splashWinPanel, JLayeredPane.POPUP_LAYER);  // Temporary winner image (highest layer)

        this.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                // Ensure all panels resize with the window
                mainPanel.setSize(getSize());
                winOverlayPanel.setSize(getSize());
                splashWinPanel.setSize(getSize());
                revalidate();
                repaint();
            }
        });
    }

    /**
     * Loads the static image resources needed for the view.
     */
    private void loadResources() {
        // Load the blur background for the final score screen
        String blurImagePath = "/win_blur.png";
        URL blurImageUrl = getClass().getResource(blurImagePath);
        if (blurImageUrl != null) {
            this.blurBackgroundIcon = new ImageIcon(blurImageUrl);
        } else {
            System.err.println("Could not find the win screen image at: " + blurImagePath);
        }

        // Load the temporary winner splash image
        String winnerImagePath = "/winner.png";
        URL winnerImageUrl = getClass().getResource(winnerImagePath);
        if (winnerImageUrl != null) {
            this.winnerSplashIcon = new ImageIcon(winnerImageUrl);
        } else {
            System.err.println("Could not find the winner splash image at: " + winnerImagePath);
        }
    }

    private void setupMainPanel() {
        JPanel titleContainer = new JPanel();
        titleContainer.setBackground(SECONDARY_COLOR);
        titleContainer.setLayout(new BoxLayout(titleContainer, BoxLayout.Y_AXIS));

        gameTitleLabel = new JLabel("Puzzle World", JLabel.CENTER);
        gameTitleLabel.setFont(TITLE_FONT);
        gameTitleLabel.setForeground(PRIMARY_COLOR);
        gameTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        String cleanImageName = getCleanImageName(GameController.imageName);
        gameTitleLabel2 = new JLabel(cleanImageName, JLabel.CENTER);
        gameTitleLabel2.setFont(SUBTITLE_FONT);
        gameTitleLabel2.setForeground(PRIMARY_COLOR.darker());
        gameTitleLabel2.setAlignmentX(Component.CENTER_ALIGNMENT);

        titleContainer.add(gameTitleLabel);
        titleContainer.add(Box.createRigidArea(new Dimension(0, 5)));
        titleContainer.add(gameTitleLabel2);
        mainPanel.add(titleContainer, BorderLayout.NORTH);

        puzzleGridPanel = new JPanel();
        puzzleGridPanel.setBackground(SECONDARY_COLOR);
        mainPanel.add(puzzleGridPanel, BorderLayout.CENTER);

        JPanel southPanel = new JPanel(new BorderLayout(10, 0));
        southPanel.setBackground(SECONDARY_COLOR);
        southPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        backButton = new JButton("Back to Categories");
        backButton.setFont(UI_FONT);
        southPanel.add(backButton, BorderLayout.WEST);
        timerLabel = new JLabel("Time: 0s", JLabel.RIGHT);
        timerLabel.setFont(UI_FONT);
        southPanel.add(timerLabel, BorderLayout.EAST);
        mainPanel.add(southPanel, BorderLayout.SOUTH);
    }

    private void createWinOverlay() {
        winOverlayPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (blurBackgroundIcon != null) {
                    g.drawImage(blurBackgroundIcon.getImage(), 0, 0, this.getWidth(), this.getHeight(), this);
                } else {
                    g.setColor(new Color(50, 50, 50, 200));
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        winOverlayPanel.setLayout(new GridBagLayout());
        winOverlayPanel.setVisible(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.gridx = 0;

        winTitleLabel = new JLabel("You Win!");
        winTitleLabel.setFont(WIN_TITLE_FONT);
        winTitleLabel.setForeground(Color.WHITE);
        gbc.gridy = 0;
        winOverlayPanel.add(winTitleLabel, gbc);

        winScoreLabel = new JLabel("Your Score: 0");
        winScoreLabel.setFont(WIN_SCORE_FONT);
        winScoreLabel.setForeground(Color.WHITE);
        gbc.gridy = 1;
        winOverlayPanel.add(winScoreLabel, gbc);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        buttonPanel.setOpaque(false);

        playAgainButton = new JButton("Play Again");
        playAgainButton.setFont(WIN_BUTTON_FONT);

        returnToMenuButton = new JButton("Return to Menu");
        returnToMenuButton.setFont(WIN_BUTTON_FONT);

        buttonPanel.add(playAgainButton);
        buttonPanel.add(returnToMenuButton);

        gbc.gridy = 2;
        gbc.insets = new Insets(30, 15, 15, 15);
        winOverlayPanel.add(buttonPanel, gbc);
    }

    /**
     * Creates the panel that will briefly display the "winner.png" image.
     */
    private void createSplashWinPanel() {
        splashWinPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (winnerSplashIcon != null) {
                    // Fill background with black for a cinematic effect
                    g.setColor(Color.BLACK);
                    g.fillRect(0, 0, getWidth(), getHeight());
                    // Draw the image centered in the panel
                    int x = (getWidth() - winnerSplashIcon.getIconWidth()) / 2;
                    int y = (getHeight() - winnerSplashIcon.getIconHeight()) / 2;
                    g.drawImage(winnerSplashIcon.getImage(), x, y, this);
                }
            }
        };
        splashWinPanel.setBounds(0, 0, 800, 800);
        splashWinPanel.setVisible(false); // Initially hidden
    }

    /**
     * Shows the final win screen with score and buttons.
     * @param finalScore The score to display.
     */
    public void showWinScreen(int finalScore) {
        winScoreLabel.setText("Your Score: " + finalScore);
        winOverlayPanel.setVisible(true);
        this.moveToFront(winOverlayPanel);
        this.repaint();
    }

    /**
     * Shows a temporary splash image, waits 2 seconds, then shows the final win overlay.
     * @param finalScore The score to pass to the final overlay.
     */
    public void showWinSplashThenOverlay(int finalScore) {
        // Fallback: If the splash image didn't load, just show the final screen.
        if (winnerSplashIcon == null) {
            showWinScreen(finalScore);
            return;
        }

        // 1. Make the splash image panel visible on top of everything.
        splashWinPanel.setVisible(true);
        this.moveToFront(splashWinPanel);
        this.repaint();

        // 2. Create a Swing Timer that will fire once after 2000 milliseconds (2 seconds).
        Timer splashTimer = new Timer(2000, e -> {
            // 3. When the timer fires, hide the splash panel.
            splashWinPanel.setVisible(false);

            // 4. Then, show the final win overlay with the calculated score.
            showWinScreen(finalScore);
        });

        splashTimer.setRepeats(false); // Make sure the timer only runs one time.
        splashTimer.start(); // Start the 2-second countdown.
    }

    public void updatePuzzleDisplay(List<JPanel> piecePanels) {
        puzzleGridPanel.removeAll();
        for (JPanel piecePanel : piecePanels) puzzleGridPanel.add(piecePanel);
        puzzleGridPanel.revalidate();
        puzzleGridPanel.repaint();
    }

    private String getCleanImageName(String fullName) {
        if (fullName == null || fullName.isEmpty()) return "";
        int dotIndex = fullName.lastIndexOf('.');
        return (dotIndex > 0) ? fullName.substring(0, dotIndex) : fullName;
    }

    public void updateTimerDisplay(long elapsedTimeSeconds) { timerLabel.setText("Time: " + elapsedTimeSeconds + "s"); }
    public void setPuzzleGridLayout(LayoutManager layout) { puzzleGridPanel.setLayout(layout); puzzleGridPanel.removeAll(); }
    public JPanel getPuzzleGridPanel() { return puzzleGridPanel; }
    public JButton getBackButton() { return backButton; }
    public JButton getPlayAgainButton() { return playAgainButton; }
    public JButton getReturnToMenuButton() { return returnToMenuButton; }
}