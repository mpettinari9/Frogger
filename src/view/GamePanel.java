package view;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/*
 * Pannello principale di gioco.
 * Contiene l'area di gioco (gamePanel) e la barra informazioni (infoPanel)
 * con vite, nome giocatore e tempo di partita.
 */
public class GamePanel extends JPanel {
	private JPanel gamePanel;
    private JPanel[] playerPanels;
    private JPanel infoPanel;
    private FrogSprite[] frogSprites;
    private ArrayList<MovingObjectSprite>[] movingObjectSprites;
    private HeartSprite[] heartSprites;
    private JLabel[][] insectSprites;
    private HomeSlotSprite[][] homeSlotSprites;
    private JProgressBar[] livesBars;
    private JLabel[] scoreLabels;
    private JLabel timeLabel;
    private int numberOfPlayers;
    private int screenWidth;
    private int playerPanelWidth;

    private static final String[] BACKGROUNDS = {
        "src/view/Asset/background(1).png",
        "src/view/Asset/background(1).png"
    };
    
    @SuppressWarnings("unchecked")
    public GamePanel(int screenWidth, int screenHeight, String[] playersNames, int[] playersLifeValues, int[] playersLifeMaxValues) {
        this.numberOfPlayers = playersNames.length;
        this.screenWidth = screenWidth;
        this.playerPanelWidth = screenWidth / numberOfPlayers;
        this.setSize(screenWidth, screenHeight);
        this.setLayout(null);

        int gamePanelHeight = (screenHeight * 85) / 100;

        this.gamePanel = new JPanel();
        this.gamePanel.setBounds(0, 0, screenWidth, gamePanelHeight);
        this.gamePanel.setLayout(null);
        this.gamePanel.setOpaque(false);

        this.playerPanels = new JPanel[numberOfPlayers];
        for (int p = 0; p < numberOfPlayers; p++) {
            final String bgPath = BACKGROUNDS[Math.min(p, BACKGROUNDS.length - 1)];
            JPanel panel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.drawImage(new ImageIcon(bgPath).getImage(),
                            0, 0, this.getWidth(), this.getHeight(), this);
                }
            };
            panel.setBounds(p * playerPanelWidth, 0, playerPanelWidth, gamePanelHeight);
            panel.setLayout(null);
            playerPanels[p] = panel;
            gamePanel.add(panel);
        }
        
        // Linea divisoria bianca tra i due schermi - aggiunta per ultima per stare sopra i playerPanels
        if (numberOfPlayers == 2) {
            JPanel divider = new JPanel();
            divider.setBounds(playerPanelWidth - 2, 0, 4, gamePanelHeight);
            divider.setBackground(Color.WHITE);
            gamePanel.add(divider);
            gamePanel.setComponentZOrder(divider, 0); // portala in primo piano
        }

        // --- Barra info ---
        this.infoPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(new ImageIcon("src/view/Asset/sfondoInfo.png").getImage(),
                        0, 0, this.getWidth(), this.getHeight(), this);
            }
        };

    
    
    }
}