package com.jeudepuzzle.controller;

import com.jeudepuzzle.model.DatabaseManager;
import com.jeudepuzzle.model.PuzzleModel;
import com.jeudepuzzle.model.User;
import com.jeudepuzzle.view.GameView;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.sql.SQLException;

public class GameController {
    private User user;
    private String category;
    public static String imageName;
    private String difficulty;
    private GameView gameView;
    private PuzzleModel puzzleModel;
    private DatabaseManager dbManager;
    private Timer gameTimer;
    private long startTime;
    private long elapsedTimeSeconds;
    private ScreenTransitionListener screenTransitionListener;

    public GameController(User user, String category, String imageName, String difficulty, DatabaseManager dbManager, ScreenTransitionListener screenTransitionListener) {
        this.user = user;
        this.category = category;
        GameController.imageName = imageName;
        this.difficulty = difficulty;
        this.dbManager = dbManager;
        this.screenTransitionListener = screenTransitionListener;
        initComponents();
    }

    private void initComponents() {
        gameView = new GameView();
        BufferedImage originalImage = loadImage(category, imageName);

        if (originalImage == null) {
            JOptionPane.showMessageDialog(null, "Failed to load image for the puzzle.", "Image Loading Error", JOptionPane.ERROR_MESSAGE);
            if (screenTransitionListener != null) {
                screenTransitionListener.requestScreenTransition("ImageChoice");
            }
            disposeCurrentFrame();
            return;
        }

        puzzleModel = new PuzzleModel(originalImage, difficulty);
        int gridSize = puzzleModel.getGridSize();
        gameView.setPuzzleGridLayout(new GridLayout(gridSize, gridSize, 2, 2));
        updateGameViewDisplay();

        int preferredWidth = originalImage.getWidth() + 40;
        int preferredHeight = originalImage.getHeight() + 160;

        SwingUtilities.invokeLater(() -> {
            JFrame gameFrame = (JFrame) SwingUtilities.getWindowAncestor(gameView);
            if (gameFrame != null) {
                gameFrame.getContentPane().setPreferredSize(new Dimension(preferredWidth, preferredHeight));
                gameFrame.pack();
                gameFrame.setLocationRelativeTo(null);
            }
        });

        setupInputListeners();

        elapsedTimeSeconds = 0;
        startTime = System.currentTimeMillis();
        gameTimer = new Timer(1000, e -> {
            elapsedTimeSeconds++;
            gameView.updateTimerDisplay(elapsedTimeSeconds);
        });
        gameTimer.start();
    }

    /**
     * Handles the win condition. Plays a sound, stops the timer, saves the score,
     * and then triggers the win animation sequence in the GameView.
     */
    private void handleWin() {
        playSound("/Win Sound.wav");

        if (gameTimer != null && gameTimer.isRunning()) {
            gameTimer.stop();
        }

        // Calculate and save the score, and get the score value back.
        int finalScore = saveFinalScore();

        // Delegate the entire visual sequence to the GameView.
        // This is much cleaner and keeps the Controller from manipulating the View directly.
        gameView.showWinSplashThenOverlay(finalScore);
    }

    /**
     * Calculates the final score, saves it to the database, and returns the calculated score.
     * @return The final calculated score.
     */
    private int saveFinalScore() {
        int difficultyMultiplier = getDifficultyMultiplier(difficulty);
        int finalScore = Math.max(100, 2000 - (int) elapsedTimeSeconds * 15 + (difficultyMultiplier * 700));

        if (user != null && dbManager != null) {
            boolean saveSuccess = dbManager.saveUserScore(user.getId(), finalScore, category, imageName, difficulty);
            updateUserScoreInDatabase(user.getUsername(), finalScore); // Use this method to update score
            if (!saveSuccess) {
                // This message can appear over the animation, which is acceptable for an error.
                JOptionPane.showMessageDialog(null, "Failed to save score.", "Score Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        return finalScore;
    }

    /**
     * Updates the user's score in the database.
     *
     * @param username The username of the user.
     * @param finalScore The final score to add to the user's current score.
     */
    private void updateUserScoreInDatabase(String username, int finalScore) {
        try {
            String sql = "UPDATE useresultat SET score = score + ? WHERE username = ?";
            try (java.sql.Connection conn = dbManager.getConnection();
                 java.sql.PreparedStatement query = conn.prepareStatement(sql)) {
                query.setInt(1, finalScore);
                query.setString(2, username);
                int rowsAffected = query.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Score updated successfully for user: " + username);
                } else {
                    System.out.println("No user found with username: " + username);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error updating score in database: " + e.getMessage());
            e.printStackTrace();
            // Consider showing an error message to the user.
            JOptionPane.showMessageDialog(null, "Error updating score in database.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }



    // --- ALL OTHER METHODS BELOW ARE UNCHANGED ---

    private BufferedImage loadImage(String category, String imageName) {
        String imagePath = "/" + category + "/" + imageName;
        try {
            URL imageUrl = getClass().getResource(imagePath);
            if (imageUrl == null) {
                System.err.println("Cannot find image resource at: " + imagePath);
                return null;
            }
            return javax.imageio.ImageIO.read(imageUrl);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void setupInputListeners() {
        gameView.getPuzzleGridPanel().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Component clickedComponent = e.getComponent().getComponentAt(e.getPoint());
                if (clickedComponent instanceof JPanel clickedPiecePanel) {
                    JPanel puzzleGrid = (JPanel) clickedPiecePanel.getParent();
                    Component[] components = puzzleGrid.getComponents();
                    int clickedIndex = -1;
                    for (int i = 0; i < components.length; i++) {
                        if (components[i] == clickedPiecePanel) {
                            clickedIndex = i;
                            break;
                        }
                    }
                    if (clickedIndex != -1) {
                        int gridSize = puzzleModel.getGridSize();
                        int clickedRow = clickedIndex / gridSize;
                        int clickedCol = clickedIndex % gridSize;
                        handlePieceClick(clickedRow, clickedCol);
                    }
                }
            }
        });
        gameView.getBackButton().addActionListener(e -> handleBackButton());
    }

    private void handlePieceClick(int clickedRow, int clickedCol) {
        boolean moved = puzzleModel.attemptMove(clickedRow, clickedCol);
        if (moved) {
            updateGameViewDisplay();
            if (puzzleModel.isSolved()) {
                handleWin();
            }
        }
    }

    private void handleBackButton() {
        if (gameTimer != null) gameTimer.stop();
        int choice = JOptionPane.showConfirmDialog(gameView, "Are you sure you want to return to the category selection?\nYour current progress will be lost.", "Confirm Exit", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (choice == JOptionPane.YES_OPTION) {
            disposeCurrentFrame();
            if (screenTransitionListener != null) {
                screenTransitionListener.requestScreenTransition("CategoryChoice");
            }
        } else {
            if (gameTimer != null) gameTimer.start();
        }
    }

    private void updateGameViewDisplay() {
        gameView.updatePuzzleDisplay(puzzleModel.getPiecePanels());
        gameView.revalidate();
        gameView.repaint();
    }

    private void playSound(String soundFilePath) {
        String correctedPath = soundFilePath.replace(" ", "%20");
        try {
            URL soundURL = getClass().getResource(correctedPath);
            if (soundURL == null) soundURL = getClass().getResource(soundFilePath);
            if (soundURL == null) {
                System.err.println("Cannot find sound file at: " + soundFilePath);
                return;
            }
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundURL);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getDifficultyMultiplier(String difficulty) {
        return switch (difficulty) {
            case "Easy" -> 1;
            case "Medium" -> 2;
            case "Hard" -> 3;
            default -> 1;
        };
    }

    private void disposeCurrentFrame() {
        JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(gameView);
        if (currentFrame != null) {
            currentFrame.dispose();
        }
    }

    public GameView getGameView() {
        return gameView;
    }
}