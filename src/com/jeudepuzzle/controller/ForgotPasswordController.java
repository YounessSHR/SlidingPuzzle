package com.jeudepuzzle.controller;

import com.jeudepuzzle.model.DatabaseManager;
import com.jeudepuzzle.view.ForgotPasswordView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Contrôleur pour la fonctionnalité "Mot de passe oublié"
 * Gère les interactions entre la vue et le modèle pour la récupération du mot de passe
 */
public class ForgotPasswordController {
    private ForgotPasswordView forgotPasswordView; // Vue pour la récupération de mot de passe
    private DatabaseManager dbManager; // Gestionnaire d'accès à la base de données

    /**
     * Constructeur du contrôleur
     * @param forgotPasswordView La vue associée
     * @param dbManager Le gestionnaire de base de données
     */
    public ForgotPasswordController(ForgotPasswordView forgotPasswordView, DatabaseManager dbManager) {
        this.forgotPasswordView = forgotPasswordView;
        this.dbManager = dbManager;
        attachListeners(); // Attache les écouteurs d'événements
    }

    /**
     * Attache les écouteurs d'événements aux composants de la vue
     */
    private void attachListeners() {
        // Ecouteur pour le bouton de soumission
        forgotPasswordView.getSubmitButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSubmit();
            }
        });

        // Ecouteur pour le bouton d'annulation
        forgotPasswordView.getCancelButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleCancel();
            }
        });
    }

    /**
     * Gère l'action de soumission du formulaire
     */
    private void handleSubmit() {
        // Récupère et nettoie l'email saisi
        String email = forgotPasswordView.getEmailField().getText().trim();

        // Validation de l'email
        if (email.isEmpty()) {
            forgotPasswordView.showMessage("Veuillez saisir votre adresse email.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Interaction avec le modèle (DatabaseManager) pour obtenir l'indice de mot de passe
        String message = dbManager.getPasswordHintByEmail(email);

        // Affichage du résultat à l'utilisateur via la vue
        forgotPasswordView.showMessage(message, "Récupération de mot de passe", JOptionPane.INFORMATION_MESSAGE);

        // Fermeture de la boîte de dialogue via la vue
        forgotPasswordView.closeDialog();
    }

    /**
     * Gère l'action d'annulation
     */
    private void handleCancel() {
        // Demande à la vue de fermer la boîte de dialogue
        forgotPasswordView.closeDialog();
    }

    /**
     * Affiche la boîte de dialogue de récupération de mot de passe
     * Méthode appelée par le contrôleur parent (LoginController)
     */
    public void showDialog() {
        forgotPasswordView.setVisible(true);
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