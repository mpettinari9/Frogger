package view;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class MovingObjectSprite extends JLabel {

	private boolean moving;
	
    public MovingObjectSprite(String imgPath) {
    	ImageIcon icon = new ImageIcon(imgPath);
        this.setIcon(icon);
    	this.setSize(icon.getIconWidth(), icon.getIconHeight());
        this.setOpaque(false);
        this.moving = false;
    }
    
    public MovingObjectSprite(String imgPath, int x, int y) {
    	this(imgPath);
        this.setLocation(x, y);
        this.setVisible(true);
    }

    public void updatePosition(int x, int y) {
        this.setLocation(x, y);
    }

    public void updateSprite(String imgPath) {
        ImageIcon icon = new ImageIcon(imgPath);
        this.setIcon(icon);
        this.setSize(icon.getIconWidth(), icon.getIconHeight());
    }

    public void setMoving(boolean moving) {
		this.moving = moving;
	}
	
    public void setSpriteActions(boolean moving) {
    	this.setMoving(moving);
    }
}

