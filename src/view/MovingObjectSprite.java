package view;

import javax.swing.ImageIcon;

public class MovingObjectSprite extends Sprite {
	public MovingObjectSprite(String imagePath) {
		super(imagePath,1);
		this.setIcon(new ImageIcon(imagePath));
	}
	
	public void updatePosition(int x, int y) {
		this.setBounds(x, y, this.getWidth(), this.getHeight());
	}
	
	public void setSpriteActions(boolean moving) {
		this.setMoving(moving);
	}
}
