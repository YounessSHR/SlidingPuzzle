package com.jeudepuzzle.model;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class PasswordUtils {

 /** 
  * Algorithme de hashage 256 utilise pour secutise le mot de passe d'utilisateur
  * 
  * */
	
    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            // This should ideally not happen with SHA-256
            throw new RuntimeException("Failed to hash password", e);
        }
    }

    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        String newHash = hashPassword(plainPassword);
        return newHash.equals(hashedPassword);
    }
    /**
     * *************************************************PuzzleWorld\n\n" +
                              "Developed by:\n" +
                              "- Abdelali ITTAS\n" +
                              "- Abdelouahab Mjahdi\n" +
                              "- Youness SAHRAOUI\n" +
                              "- Mohammed MAATAOUI BELABBES\n" +
                              "\n\n" +
                              "Version: beta\n" +
                              "(c) 2025";*****************************************************************************/
}