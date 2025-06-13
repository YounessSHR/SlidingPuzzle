package com.jeudepuzzle.model;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class PuzzleModel {

    // Client property key to store the original position
    public static final String ORIGINAL_POSITION_PROPERTY = "originalPosition";

    private BufferedImage originalImage;
    private int gridSize; // e.g., 3 for 3x3, 4 for 4x4
    private int emptyPieceRow; // Row of the empty piece
    private int emptyPieceCol; // Column of the empty piece

    private List<BufferedImage> puzzlePieces; // List of individual puzzle piece images
    private List<BufferedImage> currentPieceOrder; // Represents the shuffled order

    public PuzzleModel(BufferedImage originalImage, String difficulty) {
        this.originalImage = originalImage;
        setGridSize(difficulty);
        this.puzzlePieces = new ArrayList<>();
        this.currentPieceOrder = new ArrayList<>();
        initializePuzzle();
    }

    private void setGridSize(String difficulty) {
        switch (difficulty.toLowerCase()) {
            case "easy":
                this.gridSize = 3;
                break;
            case "medium":
                this.gridSize = 4;
                break;
            case "hard":
                this.gridSize = 5;
                break;
            default:
                // Default to Easy if difficulty is not recognized
                this.gridSize = 3;
                System.err.println("Unknown difficulty: " + difficulty + ". Defaulting to Easy (3x3).");
        }
    }

    private void initializePuzzle() {
        // Create a placeholder for the \"empty\" piece.
        // For simplicity, we\'ll just use null in the list.

        // Calculate piece dimensions
        int pieceWidth = originalImage.getWidth() / gridSize;
        int pieceHeight = originalImage.getHeight() / gridSize;

        // Divide the image into pieces
        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                BufferedImage piece = originalImage.getSubimage(
                        col * pieceWidth,
                        row * pieceHeight,
                        pieceWidth,
                        pieceHeight
                );
                puzzlePieces.add(piece);
                currentPieceOrder.add(piece); // Initially in correct order

                // Store the original row and column
                int originalRow = row;
                int originalCol = col;
                // We can store the original position as a Point for the piece
                // For the empty piece, we will handle it separately.
            }
        }

        // Replace the last piece with null to represent the empty space
        BufferedImage lastPiece = currentPieceOrder.remove(currentPieceOrder.size() - 1);
        currentPieceOrder.add(null); // Represents the empty space
        emptyPieceRow = gridSize - 1;
        emptyPieceCol = gridSize - 1;

        shufflePuzzle();
    }

    private void shufflePuzzle() {
        // Create a list of original indices (0 to gridSize*gridSize - 1)
        List<Integer> originalIndices = new ArrayList<>();
        for (int i = 0; i < gridSize * gridSize; i++) {
            originalIndices.add(i);
        }

        // Remove the index for the empty piece from the list of indices to shuffle
        // Assuming the empty piece is initially at the last position (gridSize*gridSize - 1)
        originalIndices.remove(originalIndices.size() - 1);

        Random random = new Random();

        // Declare pieceOrderForSolvabilityCheck outside the loop
        List<Integer> pieceOrderForSolvabilityCheck;
        int currentEmptySpaceRow; // Also declare currentEmptySpaceRow outside if needed in the loop condition

        // Shuffle until solvable
        do {
            // Shuffle the original indices
            Collections.shuffle(originalIndices, random);

            // Reconstruct the current piece order (including the empty space)
            // based on the shuffled original indices
            currentPieceOrder.clear();
            for (Integer originalIndex : originalIndices) {
                currentPieceOrder.add(puzzlePieces.get(originalIndex));
            }
            // Add the empty space (null) back to the end of the list
            currentPieceOrder.add(null);

            // Update the empty piece position (since we always place it at the end for shuffling)
            emptyPieceRow = gridSize - 1;
            emptyPieceCol = gridSize - 1;

            // Create a list of original indices including the empty space for the solvability check
            pieceOrderForSolvabilityCheck = new ArrayList<>(originalIndices);
            pieceOrderForSolvabilityCheck.add(gridSize * gridSize - 1); // Add the original index of the empty space

            // For solvability check, we need the row of the *current* empty space position
            currentEmptySpaceRow = emptyPieceRow; // This is already tracked


        } while (!isSolvable(pieceOrderForSolvabilityCheck, gridSize, currentEmptySpaceRow)); // Use emptyPieceRow for solvability check

        // Now currentPieceOrder holds a solvable arrangement of pieces
        // You might need to adjust how you visually represent the empty space
        // in your GameView based on the emptyPieceRow and emptyPieceCol
    }

    // Method to convert 1D index to 2D coordinates
    private Point getCoordinates(int index) {
        return new Point(index / gridSize, index % gridSize);
    }

    // Method to convert 2D coordinates to 1D index
    private int getIndex(int row, int col) {
        return row * gridSize + col;
    }

    public List<JPanel> getPiecePanels() {
        List<JPanel> piecePanels = new ArrayList<>();
        int pieceWidth = originalImage.getWidth() / gridSize;
        int pieceHeight = originalImage.getHeight() / gridSize;

        for (int i = 0; i < currentPieceOrder.size(); i++) {
            BufferedImage pieceImage = currentPieceOrder.get(i);

            JPanel piecePanel = new JPanel(new BorderLayout());
            // Use a FlowLayout or similar for the piece panel to handle centering/spacing if needed
            piecePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0)); // Example: Center image in panel

            if (pieceImage != null) { // Check if it's not the empty piece
                ImageIcon imageIcon = new ImageIcon(pieceImage);
                JLabel pieceLabel = new JLabel(imageIcon);
                piecePanel.add(pieceLabel);

                // Find the original index of this piece in the puzzlePieces list
                int originalIndex = puzzlePieces.indexOf(pieceImage);
                // Store the original row and column as a client property
                int originalRow = originalIndex / gridSize;
                int originalCol = originalIndex % gridSize;
                piecePanel.putClientProperty(ORIGINAL_POSITION_PROPERTY, new Point(originalRow, originalCol));

            } else {
                // This is the empty piece - display an empty panel
                // You might want a visual indicator for the empty space
                piecePanel.setBackground(Color.LIGHT_GRAY); // Example: Light gray background
                piecePanel.setPreferredSize(new Dimension(pieceWidth, pieceHeight)); // Give it size
                 piecePanel.putClientProperty(ORIGINAL_POSITION_PROPERTY, null); // Empty piece doesn\'t have an original image position in the puzzlePieces list
            }

            piecePanels.add(piecePanel);
        }
        return piecePanels;
    }

    public int getGridSize() {
        return gridSize;
    }

    // Method to attempt to move the piece at (clickedRow, clickedCol)
    public boolean attemptMove(int clickedRow, int clickedCol) {
        // Check if the clicked position is valid
        if (clickedRow < 0 || clickedRow >= gridSize || clickedCol < 0 || clickedCol >= gridSize) {
            return false; // Invalid click
        }

        int clickedIndex = getIndex(clickedRow, clickedCol);
        int emptyIndex = getIndex(emptyPieceRow, emptyPieceCol);

        // Check if the clicked piece is adjacent to the empty piece
        if (isAdjacent(clickedRow, clickedCol, emptyPieceRow, emptyPieceCol)) {
            // Swap the clicked piece with the empty piece
            Collections.swap(currentPieceOrder, clickedIndex, emptyIndex);

            // Update the empty piece\'s position
            emptyPieceRow = clickedRow;
            emptyPieceCol = clickedCol;

            return true; // Move successful
        }

        return false; // Not adjacent, move not possible
    }

    // Helper method to check if two positions are adjacent (horizontally or vertically)
    private boolean isAdjacent(int row1, int col1, int row2, int col2) {
        if (row1 == row2) { // Same row
            return Math.abs(col1 - col2) == 1; // Check if columns are next to each other
        } else if (col1 == col2) { // Same column
            return Math.abs(row1 - row2) == 1; // Check if rows are next to each other
        }
        return false; // Not in the same row or column, not adjacent
    }
    
    private int countInversions(List<Integer> pieceOrder) {
        int inversions = 0;
        int n = pieceOrder.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                // Assuming pieceOrder contains the correct index of the piece at each position
                if (pieceOrder.get(i) > pieceOrder.get(j)) {
                    inversions++;
                }
            }
        }
        return inversions;
    }
    
    private boolean isSolvable(List<Integer> pieceOrder, int gridSize, int emptySpaceRow) {
        int inversions = countInversions(pieceOrder);

        if (gridSize % 2 != 0) { // Odd grid size
            return inversions % 2 == 0;
        } else { // Even grid size
            // Count rows from the bottom, starting at 1
            int rowFromBottom = gridSize - emptySpaceRow;
            if (rowFromBottom % 2 != 0) { // Empty space on an odd row from bottom
                return inversions % 2 != 0;
            } else { // Empty space on an even row from bottom
                return inversions % 2 == 0;
            }
        }
    }

    // Method to check if the puzzle is solved
    public boolean isSolved() {
        // Iterate through the current piece order
        for (int i = 0; i < currentPieceOrder.size(); i++) {
            BufferedImage pieceImage = currentPieceOrder.get(i);

            if (pieceImage != null) { // Check if it's not the empty piece
                // Find the original index of this piece in the puzzlePieces list
                int originalIndex = puzzlePieces.indexOf(pieceImage);

                // Calculate the correct row and column for this piece in the solved state
                int correctRow = originalIndex / gridSize;
                int correctCol = originalIndex % gridSize;

                // Calculate the current row and column of the piece in the shuffled order
                int currentRow = i / gridSize;
                int currentCol = i % gridSize;

                // If the current position does not match the correct original position, the puzzle is not solved
                if (currentRow != correctRow || currentCol != correctCol) {
                    return false;
                }
            } else { // This is the empty piece
                // Check if the empty piece is in the correct final position (bottom-right)
                int emptyPieceCurrentRow = i / gridSize;
                int emptyPieceCurrentCol = i % gridSize;

                if (emptyPieceCurrentRow != gridSize - 1 || emptyPieceCurrentCol != gridSize - 1) {
                    return false;
                }
            }
        }

        // If we've checked all pieces and the empty space, and they are all in their correct positions, the puzzle is solved
        return true;
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