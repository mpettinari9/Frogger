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

    public void setFrogSprite(int x, int y) {
        if (this.frogSprite == null) {
            String[] walkUp = {"src/view/Asset/frogup1.png", "src/view/Asset/frogup2.png"};
            String[] walkDown = {"src/view/Asset/frogdown1.png", "src/view/Asset/frogdown2.png"};
            String[] walkLeft = {"src/view/Asset/frogleft1.png", "src/view/Asset/frogleft2.png"};
            String[] walkRight = {"src/view/Asset/frogright1.png", "src/view/Asset/frogright2.png"};

            this.frogSprite = new FrogSprite(walkUp, walkDown, walkLeft, walkRight);
            // x e y sono già dall'angolo
            this.frogSprite.setBounds(x, y, 60, 60);
            this.gamePanel.add(this.frogSprite);
        }
    }
    
    public void updateGameWindow(int frogX, int frogY, SpriteDirection frogDirection, 
            					 int frogLife, int frogMaxLife,
            					 ArrayList<Integer> movingX, ArrayList<Integer> movingY, 
            					 long hours, long minutes, long seconds) {
		if (frogSprite != null) {
			// frogX e frogY sono già dall'angolo
			frogSprite.setBounds(frogX, frogY, frogSprite.getWidth(), frogSprite.getHeight());
		}
		
		livesBar.setValue(frogLife);
		livesBar.setMaximum(frogMaxLife);
		livesBar.setString(frogLife + "/" + frogMaxLife);
		
		timeLabel.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
		
		// Aggiorna oggetti in movimento usando coordinate dall'angolo
		for (int i = 0; i < movingObjectSprites.size(); i++) {
			if (i < movingX.size() && i < movingY.size()) {
				// movingX.get(i) e movingY.get(i) sono già dall'angolo
				movingObjectSprites.get(i).updatePosition(movingX.get(i), movingY.get(i));
			}
		}
	
		gamePanel.repaint();
    }
    
    // Aggiunge oggetto usando coordinate dall'angolo
    public void addMovingObject(MovingObjectTypeSprite type, int x, int y) {
        MovingObjectSprite obj = new MovingObjectSprite(
            "src/view/Asset/" + type.toString().toLowerCase() + ".png"
        );
        // x e y sono già dall'angolo
        obj.setBounds(x, y, obj.getIcon().getIconWidth(), obj.getIcon().getIconHeight());
        movingObjectSprites.add(obj);
        gamePanel.add(obj);
    }
    
    // Aggiunge cuore usando coordinate dall'angolo
    public void addHeart(int x, int y) {
        if (heartSprite == null) {
            heartSprite = new HeartSprite("src/view/Asset/heart.png");
            int size = 60;
            // x e y sono già dall'angolo
            heartSprite.setBounds(x, y, size, size);
            gamePanel.add(heartSprite);
        }
    }

    public void removeHeart() {
        if (heartSprite != null) {
            gamePanel.remove(heartSprite);
            heartSprite = null;
            gamePanel.repaint();
        }
    }
    
    
    
}