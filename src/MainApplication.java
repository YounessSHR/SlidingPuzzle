import javax.swing.SwingUtilities;
import com.jeudepuzzle.model.DatabaseManager;

public class MainApplication {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Affiche l'écran d'intro et ensuite...
            new StudioIntroScreen(17000, () -> {
                // Initialise le gestionnaire de base de données
                DatabaseManager dbManager = new DatabaseManager();

                // Affiche le menu principal
                PuzzleWorldMenuPage menuPage = new PuzzleWorldMenuPage(dbManager);
                menuPage.setVisible(true);
            });
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