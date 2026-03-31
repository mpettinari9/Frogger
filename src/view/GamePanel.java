package view;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GamePanel extends JPanel {
    private JPanel gamePanel;
    private JPanel infoPanel;
    private FrogSprite frogSprite; 
    private ArrayList<MovingObjectSprite> movingObjectSprites;
    private HeartSprite heartSprite;
    private JProgressBar livesBar;
    private JLabel timeLabel;

    public GamePanel(int screenWidth, int screenHeight, String frogName, int frogLifeValue, int frogLifeMaxValue) {
        this.setSize(screenWidth, screenHeight);
        this.setLayout(null);

        this.gamePanel = new JPanel() {
	        @Override
	        protected void paintComponent(Graphics g) {
	            super.paintComponent(g);
	            g.drawImage(new ImageIcon("src/view/Asset/sfondogioco1.png").getImage(),
	                        0, 0, this.getWidth(), this.getHeight(), this);
	        }
	    };
        this.gamePanel.setBounds(0, 0, screenWidth, (screenHeight * 85) / 100);
        this.gamePanel.setLayout(null);

        this.infoPanel = new JPanel() {
	        @Override
	        protected void paintComponent(Graphics g) {
	            super.paintComponent(g);
	            g.drawImage(new ImageIcon("src/view/Asset/sfondoInfo.png").getImage(),
	                        0, 0, this.getWidth(), this.getHeight(), this);
	        }
	    };
        this.infoPanel.setBounds(0, this.gamePanel.getHeight(), screenWidth, screenHeight - this.gamePanel.getHeight());
        this.infoPanel.setLayout(null);

        this.timeLabel = new JLabel("00:00:00");
        this.timeLabel.setBounds(0, 0, this.infoPanel.getWidth(), 30);
        this.timeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        this.infoPanel.add(timeLabel);
        
        JLabel nameLabel = new JLabel(frogName);
        nameLabel.setBounds(0, this.timeLabel.getHeight(), this.infoPanel.getWidth(), this.infoPanel.getHeight() / 7);
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        this.infoPanel.add(nameLabel);

        this.livesBar = new JProgressBar();
        this.livesBar.setMaximum(frogLifeMaxValue);
        this.livesBar.setValue(frogLifeValue);
        this.livesBar.setStringPainted(true);
        this.livesBar.setString(frogLifeValue + "/" + frogLifeMaxValue);
        this.livesBar.setForeground(Color.RED);
        this.livesBar.setBounds(20, nameLabel.getY() + nameLabel.getHeight(), 
                                this.infoPanel.getWidth() - 40, this.infoPanel.getHeight() / 7);
        this.infoPanel.add(this.livesBar);

        this.movingObjectSprites = new ArrayList<>();
        this.heartSprite = null;

        this.add(this.gamePanel);
        this.add(this.infoPanel);
    }

    public JPanel getGamePanel() {
        return gamePanel;
    }
    
    public JPanel getInfoPanel() {
        return infoPanel;
    }
    
    public FrogSprite getFrogSprite() { 
        return frogSprite; 
    }
    

    public HeartSprite getHeartSprite() {
        return heartSprite;
    }
    
    public ArrayList<MovingObjectSprite> getMovingObjectSprites() { 
        return movingObjectSprites; 
    }

}