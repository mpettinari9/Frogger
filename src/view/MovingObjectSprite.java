package view;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

/*
 * Sprite degli oggetti mobili (auto, camion, tartarughe, tronchi).
 * Gestisce la posizione e l'immagine dell'oggetto a schermo.
 */
public class MovingObjectSprite extends JLabel {

	private boolean moving;
	
	// Crea lo sprite caricando l'immagine dal percorso indicato.
    public MovingObjectSprite(String imgPath) {
    	ImageIcon icon = new ImageIcon(imgPath);
        this.setIcon(icon);
    	this.setSize(icon.getIconWidth(), icon.getIconHeight());
        this.setOpaque(false);
        this.moving = false;
    }
    
    // Crea lo sprite e lo posiziona alle coordinate indicate.
    public MovingObjectSprite(String imgPath, int x, int y) {
    	this(imgPath);
        this.setLocation(x, y);
        this.setVisible(true);
    }

    // Aggiorna la posizione dello sprite a schermo.
    public void updatePosition(int x, int y) {
        this.setLocation(x, y);
    }

    // Aggiorna l'immagine dello sprite.
    public void updateSprite(String imgPath) {
        ImageIcon icon = new ImageIcon(imgPath);
        this.setIcon(icon);
        this.setSize(icon.getIconWidth(), icon.getIconHeight());
    }

    // Metodi setter
    public void setMoving(boolean moving) {
		this.moving = moving;
	}
	
    public void setSpriteActions(boolean moving) {
    	this.setMoving(moving);
    }
}

