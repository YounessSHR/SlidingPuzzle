package com.jeudepuzzle.model;

import java.sql.*; // Pour la base de données

/**
 * Gère la connexion et les opérations avec la base de données du jeu.
 */
public class DatabaseManager {

    // Infos de connexion à la base MySQL
    private static final String DB_URL = "jdbc:mysql://localhost:3306/puzzle_game_db?zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=UTC";
    private static final String DB_USER = "root"; // Utilisateur BDD
    private static final String DB_PASSWORD = ""; // Mot de passe BDD

    /**
     * Constructeur : charge le pilote MySQL et initialise la BDD.
     */
    public DatabaseManager() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Charge le pilote
        } catch (ClassNotFoundException e) {
            System.err.println("Pilote MySQL JDBC non trouvé."); // Erreur si pilote absent
            e.printStackTrace();
        }
        initDatabase(); // Crée les tables si besoin
    }

    /**
     * Ouvre une connexion à la base de données.
     * @return Connexion à la BDD.
     * @throws SQLException Si la connexion échoue.
     */
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD); // Connexion
    }

    /**
     * Met à jour le score d\'un utilisateur.
     * @param username Nom de l\'utilisateur.
     * @param newScore Nouveau score.
     * @return true si succès, false sinon.\r\n     */
    public void updateUserScore(String username, int newScore) {
        String sql = "UPDATE useresultat SET score = ? WHERE username = ?"; // Requête SQL
        try (Connection conn = getConnection(); // Auto-fermeture de la connexion
             PreparedStatement query = conn.prepareStatement(sql)) {
            query.setInt(1, newScore);    // Remplace le 1er \"?\"
            query.setString(2, username); // Remplace le 2ème \"?\"
        } catch (SQLException e) {
            e.printStackTrace();
           
        }
    }
    
    /**
     * Saves the user's score for a completed puzzle.
     *
     * @param userId      The ID of the user.
     * @param score       The score achieved.
     * @param category    The puzzle category.
     * @param imageName   The name of the image used for the puzzle.
     * @param difficulty  The difficulty level of the puzzle.
     * @return true if the score was saved successfully, false otherwise.
     */
    public boolean saveUserScore(int userId, int score, String category, String imageName, String difficulty) {
        String sql = "INSERT INTO game_results (user_id, score, category, image_name, difficulty) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement query = conn.prepareStatement(sql)) {

            query.setInt(1, userId);
            query.setInt(2, score);
            query.setString(3, category);
            query.setString(4, imageName);
            query.setString(5, difficulty);

            int rowsAffected = query.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error saving user score: " + e.getMessage());
            return false;
        }
    }

    /**
     * Crée la table des utilisateurs si elle n\'existe pas.\r\n     */
    private void initDatabase() {
        String createUserTableSQL = "CREATE TABLE IF NOT EXISTS useresultat (" +
                                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                                    "username VARCHAR(50) NOT NULL UNIQUE," +
                                    "password_hash VARCHAR(255) NOT NULL," +
                                    "email VARCHAR(100) NOT NULL UNIQUE," +
                                    "score INT DEFAULT 0 NOT NULL" +
                                    ");";

        String createGameResultsTableSQL = "CREATE TABLE IF NOT EXISTS game_results (" +
                                           "id INT AUTO_INCREMENT PRIMARY KEY," +
                                           "user_id INT," +
                                           "score INT," +
                                           "category VARCHAR(50)," +
                                           "image_name VARCHAR(100)," +
                                           "difficulty VARCHAR(20)," +
                                           "game_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                                           "FOREIGN KEY (user_id) REFERENCES useresultat(id)" +
                                           ");"; // <-- Make sure the closing parenthesis and semicolon are there

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createUserTableSQL);
            stmt.execute(createGameResultsTableSQL); // Execute the creation of the new table
            System.out.println("BDD initialisée (tables useresultat, game_results).");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Ajoute un nouvel utilisateur.
     * @param username Nom d\'utilisateur.
     * @param password Mot de passe (sera haché).
     * @param email Email.
     * @return true si succès, false sinon.\r\n     */
    public boolean addUser(String username, String password, String email) {
        if (userExists(username) || emailExists(email)) { // Vérifie doublons
            System.err.println("Utilisateur ou email déjà existant.");
            return false;
        }
        String hashedPassword = PasswordUtils.hashPassword(password); // Hache le mot de passe
        String sql = "INSERT INTO useresultat (username, password_hash, email, score) VALUES (?, ?, ?, 0)"; // SQL d\'insertion
        try (Connection conn = getConnection();
             PreparedStatement query = conn.prepareStatement(sql)) {
            query.setString(1, username);
            query.setString(2, hashedPassword);
            query.setString(3, email);
            return query.executeUpdate() > 0; // Exécute et vérifie succès
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Vérifie les identifiants d\'un utilisateur.
     * @param username Nom d\'utilisateur.
     * @param password Mot de passe.
     * @return Objet User si authentifié, null sinon.\r\n     */
    public User authenticateUser(String username, String password) {
        String sql = "SELECT id, username, password_hash, email, score FROM useresultat WHERE username = ?"; // SQL de sélection
        try (Connection conn = getConnection();
             PreparedStatement query = conn.prepareStatement(sql)) {
            query.setString(1, username);
            ResultSet resultat = query.executeQuery(); // Exécute la requête
            if (resultat.next()) { // Si utilisateur trouvé
                String storedHash = resultat.getString("password_hash"); // Récupère le hash stocké
                if (PasswordUtils.verifyPassword(password, storedHash)) { // Vérifie le mot de passe
                    return new User( // Crée et retourne l\'objet User
                        resultat.getInt("id"),
                        resultat.getString("username"),
                        resultat.getString("email"),
                        resultat.getInt("score")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Échec de l\'authentification
    }

    /**
     * Vérifie si un nom d\'utilisateur existe.
     * @param username Nom d\'utilisateur à vérifier.
     * @return true si existe, false sinon.\r\n     */
    public boolean userExists(String username) {
        String sql = "SELECT COUNT(*) FROM useresultat WHERE username = ?"; // Compte les utilisateurs avec ce nom
        try (Connection conn = getConnection();
             PreparedStatement query = conn.prepareStatement(sql)) {
            query.setString(1, username);
            ResultSet resultat = query.executeQuery();
            if (resultat.next()) {
                return resultat.getInt(1) > 0; // True si le compte est > 0
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Vérifie si un email existe.
     * @param email Email à vérifier.
     * @return true si existe, false sinon.\r\n     */
    public boolean emailExists(String email) {
        String sql = "SELECT COUNT(*) FROM useresultat WHERE email = ?"; // Compte les utilisateurs avec cet email
        try (Connection conn = getConnection();
        		PreparedStatement query = conn.prepareStatement(sql)) {
            query.setString(1, email);
            ResultSet resultat = query.executeQuery();
            if (resultat.next()) {
                return resultat.getInt(1) > 0; // True si le compte est > 0
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Fournit un message pour la récupération de mot de passe (simplifié).
     * @param email Email de l\'utilisateur.
     * @return Message d\'information.\r\n     */
    public String getPasswordHintByEmail(String email) {
        String sql = "SELECT username FROM useresultat WHERE email = ?"; // Vérifie si l\'email est enregistré
        try (Connection conn = getConnection();
             PreparedStatement query = conn.prepareStatement(sql)) {
            query.setString(1, email);
            ResultSet resultat = query.executeQuery();
            if (resultat.next()) { // Si email trouvé
                return "Des instructions de récupération seraient envoyées à cet email.";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Si cet email est enregistré, des instructions seront envoyées."; // Message par défaut
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