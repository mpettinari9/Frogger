package view;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

/*
 * Sprite del cuore che appare durante la partita.
 * Se raccolto dalla rana, ripristina le vite al massimo.
 */
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

    // Nasconde il cuore e segna la vita come guadagnata.
    public void gainLife() {
        this.gainedLife = true;
        this.setVisible(false);
    }
    
    public boolean hasGainedLife() {
        return gainedLife;
    }
}
