package view;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class HeartSprite extends JLabel{
	
	private boolean gainedLife;
    
    public HeartSprite(String heartPath) {
        ImageIcon heartIcon = new ImageIcon(heartPath);
        this.setIcon(heartIcon);
        this.setOpaque(false);
        this.setSize(heartIcon.getIconWidth(), heartIcon.getIconHeight());
        
        this.gainedLife = false;
        this.setVisible(true);
    }

    public void gainLife() {
        this.gainedLife = true;
        this.setVisible(false);
    }
    
    public boolean hasGainedLife() {
        return gainedLife;
    }
}
