import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import com.jeudepuzzle.controller.LoginController;
import com.jeudepuzzle.model.DatabaseManager;
import com.jeudepuzzle.view.LoginView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.sound.sampled.*;

public class PuzzleWorldMenuPage extends JFrame {

    private Image logoImage, podiumImage, musicOnImage, musicOffImage; // Images
    private ImageIcon musicOnIconScaled, musicOffIconScaled;
    private JLabel musicControlLabel;
    private Clip musicClip;
    private boolean isMusicPlaying = true;
    private DatabaseManager dbManager;

    private static final String WINDOW_TITLE = "Puzzle World";
    private static final int WINDOW_WIDTH = 800, WINDOW_HEIGHT = 600;
    private static final String LOGO_IMAGE_PATH = "/logo.png", MUSIC_FILE_PATH = "/Background.wav", PODIUM_ICON_PATH = "/podium.png", MUSIC_ON_ICON_PATH = "/music_on.png", MUSIC_OFF_ICON_PATH = "/music_off.png";

    private static final Color COLOR_BACKGROUND_DARK = new Color(15, 18, 28), COLOR_TEXT_LIGHT = new Color(220, 220, 225), COLOR_PRIMARY_ACCENT = new Color(0, 150, 255), COLOR_PRIMARY_ACCENT_HOVER = new Color(50, 170, 255), COLOR_SECONDARY_ACCENT = new Color(80, 85, 100), COLOR_SECONDARY_ACCENT_HOVER = new Color(100, 105, 120), COLOR_ICON_HOVER_GLOW = new Color(255, 255, 255, 60);
    private static final Font FONT_MAIN_BUTTON = new Font("Segoe UI", Font.BOLD, 22), FONT_TABLE_HEADER = new Font("Segoe UI", Font.BOLD, 16), FONT_TABLE_CELL = new Font("Segoe UI", Font.PLAIN, 14);


    public PuzzleWorldMenuPage(DatabaseManager dbManager) {
        this.dbManager = dbManager;
        setTitle(WINDOW_TITLE); setSize(WINDOW_WIDTH, WINDOW_HEIGHT); setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); setLocationRelativeTo(null); setResizable(false);
        loadResources();

        BackgroundPanel mainPanel = new BackgroundPanel(WINDOW_WIDTH, WINDOW_HEIGHT); // Fond étoilé
        setContentPane(mainPanel);
        mainPanel.setLayout(new GridBagLayout());        
        GridBagConstraints gbc = new GridBagConstraints();

        if (musicOnImage != null && musicOffImage != null) {
            int MUSIC_ICON_SIZE = 35;
            musicOnIconScaled = new ImageIcon(musicOnImage.getScaledInstance(MUSIC_ICON_SIZE, MUSIC_ICON_SIZE, Image.SCALE_SMOOTH));
            musicOffIconScaled = new ImageIcon(musicOffImage.getScaledInstance(MUSIC_ICON_SIZE, MUSIC_ICON_SIZE, Image.SCALE_SMOOTH));
            musicControlLabel = new JLabel(isMusicPlaying ? musicOnIconScaled : musicOffIconScaled);
            musicControlLabel.setToolTipText("Mute/Unmute Music"); musicControlLabel.setCursor(new Cursor(Cursor.HAND_CURSOR)); musicControlLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); musicControlLabel.addMouseListener(new ModernIconHoverAdapter(musicControlLabel));
            musicControlLabel.addMouseListener(new MouseAdapter() { @Override public void mouseClicked(MouseEvent e) { toggleMusic(); } });
            GridBagConstraints musicGbc = new GridBagConstraints();
            musicGbc.gridx = 1; musicGbc.gridy = 0; musicGbc.anchor = GridBagConstraints.NORTHEAST; musicGbc.insets = new Insets(10, 0, 0, 10);
            mainPanel.add(musicControlLabel, musicGbc);
        }

        if (logoImage != null) {
            JLabel logoLabel = new JLabel(new ImageIcon(logoImage.getScaledInstance(350, -1, Image.SCALE_SMOOTH)));
            gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; gbc.weighty = 0.4; gbc.anchor = GridBagConstraints.CENTER; gbc.insets = new Insets(30, 0, 20, 0);
            mainPanel.add(logoLabel, gbc);
        }

        if (podiumImage != null) {
            JLabel podiumLabel = new JLabel(new ImageIcon(podiumImage.getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
            podiumLabel.setToolTipText("View Leaderboard"); podiumLabel.setCursor(new Cursor(Cursor.HAND_CURSOR)); podiumLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); podiumLabel.addMouseListener(new ModernIconHoverAdapter(podiumLabel));
            podiumLabel.addMouseListener(new MouseAdapter() { @Override public void mouseClicked(MouseEvent e) { handleClassementsAction(); } });
            gbc.gridy = 1; gbc.weighty = 0.15; gbc.anchor = GridBagConstraints.CENTER; gbc.fill = GridBagConstraints.NONE; gbc.insets = new Insets(10, 0, 20, 0);
            mainPanel.add(podiumLabel, gbc);
        }

        JPanel buttonContainer = new JPanel(new GridBagLayout());
        buttonContainer.setOpaque(false);
        GridBagConstraints btnGbc = new GridBagConstraints();
        btnGbc.fill = GridBagConstraints.BOTH; btnGbc.insets = new Insets(10, 25, 10, 25);
        JButton playButton = createModernButton("PLAY", COLOR_PRIMARY_ACCENT, COLOR_PRIMARY_ACCENT_HOVER);
        playButton.addActionListener(e -> handlePlayAction());
        btnGbc.gridx = 0; btnGbc.gridy = 0; btnGbc.ipadx = 100; btnGbc.ipady = 20;
        buttonContainer.add(playButton, btnGbc);
        JButton quitButton = createModernButton("QUIT", COLOR_SECONDARY_ACCENT, COLOR_SECONDARY_ACCENT_HOVER);
        quitButton.addActionListener(e -> handleQuitAction());
        btnGbc.gridx = 1; btnGbc.gridy = 0; btnGbc.ipadx = 100; btnGbc.ipady = 20;
        buttonContainer.add(quitButton, btnGbc);
        gbc.gridy = 2; gbc.weighty = 0.25; gbc.anchor = GridBagConstraints.PAGE_START;
        mainPanel.add(buttonContainer, gbc);

        gbc.gridy = 3; gbc.weighty = 0.20; gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(Box.createVerticalGlue(), gbc);

        playMusic();
    }

    private static class Star {
        float x, y, speed, radius;
    }

    class BackgroundPanel extends JPanel implements ActionListener {
        private final List<Star> stars;
        private final Timer animationTimer;
        private final Random random = new Random();
        private static final int NUM_STARS = 250, ANIMATION_DELAY = 16;

        public BackgroundPanel(int width, int height) {
            this.stars = new ArrayList<>();
            initializeStars(width, height);

            this.animationTimer = new Timer(ANIMATION_DELAY, this); // Timer pour les étoiles
            this.animationTimer.start();
        }

        private void initializeStars(int width, int height) {
            for (int i = 0; i < NUM_STARS; i++) {
                Star star = new Star();
                star.x = random.nextInt(width); star.y = random.nextInt(height);
                star.radius = random.nextFloat() * 2 + 1;
                star.speed = star.radius * 0.2f;
                stars.add(star);
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            g2d.setColor(COLOR_BACKGROUND_DARK); // Fond noir
            g2d.fillRect(0, 0, getWidth(), getHeight());

            for (Star star : stars) {
                int alpha = (int) (150 * (star.radius / 3.0f)) + 50;
                g2d.setColor(new Color(220, 220, 255, alpha));
                g2d.fillOval((int)star.x, (int)star.y, (int)star.radius, (int)star.radius);
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            for (Star star : stars) {
                star.y += star.speed;

                if (star.y > getHeight()) {
                    star.y = 0; star.x = random.nextInt(getWidth()); // Reset si hors écran
                }
            }
            repaint(); // Redessine
        }

        @Override
        public void removeNotify() { super.removeNotify(); animationTimer.stop(); } // Arrete le timer
    }

    private JButton createModernButton(String text, Color bgColor, Color hoverColor) {
        JButton button = new JButton(text) { @Override protected void paintComponent(Graphics g) { Graphics2D g2 = (Graphics2D) g.create(); g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); if (getModel().isRollover()) g2.setColor(hoverColor); else g2.setColor(bgColor); g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 30, 30)); super.paintComponent(g); g2.dispose(); } };
        button.setFont(FONT_MAIN_BUTTON); button.setForeground(COLOR_TEXT_LIGHT); button.setFocusPainted(false); button.setBorderPainted(false); button.setContentAreaFilled(false); button.setOpaque(false);
        return button;
    }

    private void handleClassementsAction() {
        String sql = "SELECT username, score FROM useresultat ORDER BY score DESC LIMIT ?"; List<Object[]> scoreDataList = new ArrayList<>();
        try (Connection conn = dbManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) 
        { pstmt.setInt(1, 10); 
        try (ResultSet rs = pstmt.executeQuery()) { 
        	while (rs.next()) scoreDataList.add(new Object[]{rs.getString("username"), rs.getInt("score")}); } } catch (SQLException ex) 
        { JOptionPane.showMessageDialog(this, "Erreur scores: " + ex.getMessage(), "Erreur BDD", JOptionPane.ERROR_MESSAGE); 
        return; 
        }
        JDialog dialog = new JDialog(this, "Leaderboard - Top 10", true); 
        dialog.setSize(450, 400); dialog.setLocationRelativeTo(this); 
        dialog.getContentPane().setBackground(COLOR_BACKGROUND_DARK); 
        dialog.setLayout(new BorderLayout(0, 15)); 
        dialog.getRootPane().setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        String[] columnNames = {"Rank", "Player", "Score"}; Object[][] data = new Object[scoreDataList.size()][3]; 
        for (int i = 0; i < scoreDataList.size(); i++) {
        	data[i][0] = " #" + (i + 1); data[i][1] = scoreDataList.get(i)[0]; data[i][2] = scoreDataList.get(i)[1]; }
        JTable table = new JTable(new DefaultTableModel(data, columnNames) { 
        	@Override public boolean isCellEditable(int r, int c) { return false; } 
        	});
        table.setFont(FONT_TABLE_CELL); table.setForeground(COLOR_TEXT_LIGHT); table.setBackground(COLOR_SECONDARY_ACCENT); table.setRowHeight(30); table.setGridColor(new Color(60, 63, 65)); table.setIntercellSpacing(new Dimension(0, 1));
        JTableHeader header = table.getTableHeader(); header.setFont(FONT_TABLE_HEADER); header.setBackground(COLOR_PRIMARY_ACCENT); header.setForeground(Color.WHITE); header.setReorderingAllowed(false); header.setBorder(null);
        JScrollPane scrollPane = new JScrollPane(table); scrollPane.setBorder(BorderFactory.createLineBorder(COLOR_SECONDARY_ACCENT, 2)); scrollPane.getViewport().setBackground(COLOR_BACKGROUND_DARK); dialog.add(scrollPane, BorderLayout.CENTER);
        JButton closeButton = createModernButton("Fermer", COLOR_SECONDARY_ACCENT, COLOR_SECONDARY_ACCENT_HOVER); closeButton.addActionListener(e -> dialog.dispose()); closeButton.setPreferredSize(new Dimension(120, 40));
        JPanel bottomPanel = new JPanel(); bottomPanel.setOpaque(false); bottomPanel.add(closeButton); dialog.add(bottomPanel, BorderLayout.SOUTH); dialog.setVisible(true);
    }

    private class ModernIconHoverAdapter extends MouseAdapter {
        private final JLabel label; private final Border hoverBorder = BorderFactory.createMatteBorder(2, 2, 2, 2, COLOR_ICON_HOVER_GLOW); private final Border defaultBorder;
        public ModernIconHoverAdapter(JLabel label) { this.label = label; this.defaultBorder = label.getBorder(); }
        @Override public void mouseEntered(MouseEvent e) { label.setBorder(hoverBorder); }
        @Override public void mouseExited(MouseEvent e) { label.setBorder(defaultBorder); }
    }

    private void loadResources() {
        try { logoImage = ImageIO.read(getClass().getResource(LOGO_IMAGE_PATH)); } catch (Exception e) { System.err.println("Erreur logo: " + e.getMessage()); }
        try { podiumImage = ImageIO.read(getClass().getResource(PODIUM_ICON_PATH)); } catch (Exception e) { System.err.println("Erreur podium: " + e.getMessage()); }
        try { musicOnImage = ImageIO.read(getClass().getResource(MUSIC_ON_ICON_PATH)); } catch (Exception e) { System.err.println("Erreur musique ON: " + e.getMessage()); }
        try { musicOffImage = ImageIO.read(getClass().getResource(MUSIC_OFF_ICON_PATH)); } catch (Exception e) { System.err.println("Erreur musique OFF: " + e.getMessage()); }
        try { URL musicURL = getClass().getResource(MUSIC_FILE_PATH); if (musicURL != null) { AudioInputStream audioStream = AudioSystem.getAudioInputStream(musicURL); musicClip = AudioSystem.getClip(); musicClip.open(audioStream); } } catch (Exception e) { System.err.println("Erreur audio: " + e.getMessage()); }
    }

    private void toggleMusic() { if (musicClip == null) return; isMusicPlaying = !isMusicPlaying; if (isMusicPlaying) { musicClip.loop(Clip.LOOP_CONTINUOUSLY); if (musicControlLabel != null) musicControlLabel.setIcon(musicOnIconScaled); } else { musicClip.stop(); if (musicControlLabel != null) musicControlLabel.setIcon(musicOffIconScaled); } }
    private void playMusic() { if (musicClip != null && musicClip.isOpen() && isMusicPlaying) { musicClip.setFramePosition(0); musicClip.loop(Clip.LOOP_CONTINUOUSLY); } }
    private void stopMusicAndCloseClip() { if (musicClip != null) { if (musicClip.isRunning()) musicClip.stop(); if (musicClip.isOpen()) musicClip.close(); } isMusicPlaying = false; }

    @Override public void dispose() { stopMusicAndCloseClip(); super.dispose(); }

    private void handlePlayAction() { stopMusicAndCloseClip(); LoginView loginView = new LoginView(); new LoginController(loginView, dbManager); loginView.setVisible(true); this.dispose(); }
    private void handleQuitAction() { stopMusicAndCloseClip(); System.exit(0); }
    
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