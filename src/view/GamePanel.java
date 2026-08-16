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
        this.infoPanel.setBounds(0, gamePanelHeight, screenWidth, screenHeight - gamePanelHeight);
        this.infoPanel.setLayout(null);

        this.timeLabel = new JLabel("00:00:00");
        this.timeLabel.setBounds(0, 0, screenWidth, 30);
        this.timeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        this.timeLabel.setForeground(Color.WHITE);
        this.infoPanel.add(timeLabel);

        this.livesBars = new JProgressBar[numberOfPlayers];
        this.scoreLabels = new JLabel[numberOfPlayers];
        this.frogSprites = new FrogSprite[numberOfPlayers];

        int topOffset = -30; // margine superiore per alzare i widget nella HUD
        int columnWidth = screenWidth / numberOfPlayers;
        for (int i = 0; i < numberOfPlayers; i++) {
            JLabel nameLabel = new JLabel(playersNames[i]);
            nameLabel.setBounds(columnWidth * i, this.timeLabel.getHeight() + topOffset, columnWidth, this.infoPanel.getHeight() / 7);
            nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
            nameLabel.setForeground(Color.WHITE);
            this.infoPanel.add(nameLabel);

            this.livesBars[i] = new JProgressBar();
            this.livesBars[i].setMaximum(playersLifeMaxValues[i]);
            this.livesBars[i].setValue(playersLifeValues[i]);
            this.livesBars[i].setStringPainted(true);
            this.livesBars[i].setString(playersLifeValues[i] + "/" + playersLifeMaxValues[i]);
            this.livesBars[i].setForeground(Color.RED);
            this.livesBars[i].setBounds((columnWidth * i) + 20, nameLabel.getY() + nameLabel.getHeight(),
                    columnWidth - 40, this.infoPanel.getHeight() / 7);
            this.infoPanel.add(this.livesBars[i]);

            this.scoreLabels[i] = new JLabel("Punteggio: 0");
            this.scoreLabels[i].setBounds((columnWidth * i) + 20,
                    this.livesBars[i].getY() + this.livesBars[i].getHeight() + 4,
                    columnWidth - 40, this.infoPanel.getHeight() / 7);
            this.scoreLabels[i].setHorizontalAlignment(SwingConstants.CENTER);
            this.scoreLabels[i].setFont(new Font("DIALOG", Font.BOLD, 16));
            this.scoreLabels[i].setForeground(Color.WHITE);
            this.infoPanel.add(this.scoreLabels[i]);
        }


    
    
    }
}