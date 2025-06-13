package com.jeudepuzzle.controller;

import com.jeudepuzzle.model.DatabaseManager;
import com.jeudepuzzle.model.User;
import com.jeudepuzzle.view.DifficultyChoiceView;
import com.jeudepuzzle.view.GameView;
import com.jeudepuzzle.view.ImageChoiceView; // Import de la vue pour retour en arrière

import javax.swing.JFrame;

/**
 * Contrôleur pour la sélection de difficulté du jeu Puzzle
 */
public class DifficultyChoiceController {
    private DifficultyChoiceView difficultyChoiceView; // Vue pour choisir la difficulté
    private User user; // Utilisateur actuel
    private String selectedCategory; // Catégorie d'image sélectionnée
    private String selectedImageName; // Nom de l'image sélectionnée
    private DatabaseManager dbManager; // Gestionnaire de base de données
    private ImageChoiceView imageChoiceView; // Référence vers la vue précédente (choix d'image)

    /**
     * Constructeur du contrôleur de sélection de difficulté
     * @param difficultyChoiceView Vue de sélection de difficulté
     * @param user Utilisateur connecté
     * @param selectedCategory Catégorie sélectionnée
     * @param selectedImageName Nom de l'image sélectionnée
     * @param dbManager Gestionnaire de base de données
     * @param imageChoiceView Vue de sélection d'image (pour le retour)
     */
    public DifficultyChoiceController(DifficultyChoiceView difficultyChoiceView, User user, 
                                    String selectedCategory, String selectedImageName, 
                                    DatabaseManager dbManager, ImageChoiceView imageChoiceView) {
        this.difficultyChoiceView = difficultyChoiceView;
        this.user = user;
        this.selectedCategory = selectedCategory;
        this.selectedImageName = selectedImageName;
        this.dbManager = dbManager;
        this.imageChoiceView = imageChoiceView; // Initialisation de la vue précédente
        attachListeners(); // Attache les écouteurs d'événements
    }

    /**
     * Gère la sélection d'une difficulté
     * @param difficulty Niveau de difficulté choisi (Easy/Medium/Hard)
     */
    private void handleDifficultySelection(String difficulty) {
        // Crée un nouveau contrôleur de jeu avec les paramètres sélectionnés
        GameController gameController = new GameController(user, selectedCategory, 
                                                         selectedImageName, difficulty, 
                                                         dbManager, null);

        // Crée et configure la fenêtre de jeu
        JFrame gameFrame = new JFrame("Puzzle World - " + selectedCategory + " - " + difficulty);
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setSize(480, 580);
        gameFrame.setLocationRelativeTo(null); // Centre la fenêtre
        gameFrame.getContentPane().add(gameController.getGameView());
        gameFrame.setVisible(true);

        difficultyChoiceView.dispose(); // Ferme la vue de sélection de difficulté
    }

    /**
     * Gère l'action du bouton retour
     */
    private void handleGoBack() {
        imageChoiceView.setVisible(true); // Affiche à nouveau l'écran de choix d'image
        difficultyChoiceView.dispose();   // Ferme la fenêtre actuelle de choix de difficulté
    }

    /**
     * Attache les écouteurs d'événements aux composants de la vue
     */
    private void attachListeners() {
        // Ecouteurs pour les boutons de difficulté
        difficultyChoiceView.getEasyButton().addActionListener(e -> handleDifficultySelection("Easy"));
        difficultyChoiceView.getMediumButton().addActionListener(e -> handleDifficultySelection("Medium"));
        difficultyChoiceView.getHardButton().addActionListener(e -> handleDifficultySelection("Hard"));

        // Ecouteur pour le bouton retour
        difficultyChoiceView.getBackButton().addActionListener(e -> handleGoBack());
    }

    /**
     * Affiche la vue de sélection de difficulté
     */
    public void showView() {
        difficultyChoiceView.setVisible(true);
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