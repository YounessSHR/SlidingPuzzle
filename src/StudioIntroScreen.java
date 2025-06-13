import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

public class StudioIntroScreen {

    private JWindow splashWindow;

    public StudioIntroScreen(int durationMilliseconds, Runnable onSplashFinished) {
        showSplash(durationMilliseconds, onSplashFinished);
    }

    private void showSplash(int durationMilliseconds, Runnable onSplashFinished) {
        splashWindow = new JWindow();
        // Optional: Make background transparent if your logo has transparency
        // splashWindow.setBackground(new Color(0, 0, 0, 0));

        URL imageUrl = getClass().getResource("/logo.gif"); // Assumes logo.png is in src/

        if (imageUrl == null) {
            System.err.println("Error: Splash image 'logo.png' not found! " +
                               "Make sure it's in the 'src' folder (default package).");
            JLabel errorLabel = new JLabel("Studio Logo Here", SwingConstants.CENTER);
            errorLabel.setFont(new Font("Arial", Font.BOLD, 24));
            errorLabel.setOpaque(true);
            errorLabel.setBackground(Color.LIGHT_GRAY);
            errorLabel.setPreferredSize(new Dimension(400, 200));
            splashWindow.add(errorLabel);
        } else {
            ImageIcon imageIcon = new ImageIcon(imageUrl);
            JLabel imageLabel = new JLabel(imageIcon);
            splashWindow.add(imageLabel);
        }

        splashWindow.pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - splashWindow.getWidth()) / 2;
        int y = (screenSize.height - splashWindow.getHeight()) / 2;
        splashWindow.setLocation(x, y);
        splashWindow.setVisible(true);

        Timer timer = new Timer(durationMilliseconds, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                splashWindow.setVisible(false);
                splashWindow.dispose();
                if (onSplashFinished != null) {
                    onSplashFinished.run();
                }
            }
        });
        timer.setRepeats(false);
        timer.start();
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
}