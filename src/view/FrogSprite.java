package view;

import javax.swing.ImageIcon;

public class FrogSprite extends Sprite {
	 private int walkFrame;

	    public FrogSprite(String[] walkUpPaths, String[] walkDownPaths, String[] walkLeftPaths, String[] walkRightPaths) {

	        super(walkDownPaths[0], walkDownPaths.length); 
	        this.walkFrame = 0;

	        // Carica animazioni
	        for (int i = 0; i < walkUpPaths.length; i++)
	            super.getUpAnimations()[i] = new ImageIcon(walkUpPaths[i]);

	        for (int i = 0; i < walkDownPaths.length; i++)
	            super.getDownAnimations()[i] = new ImageIcon(walkDownPaths[i]);

	        for (int i = 0; i < walkLeftPaths.length; i++)
	            super.getLeftAnimations()[i] = new ImageIcon(walkLeftPaths[i]);

	        for (int i = 0; i < walkRightPaths.length; i++)
	            super.getRightAnimations()[i] = new ImageIcon(walkRightPaths[i]);
	    }

	    public void setSpriteActions(boolean moving) {
	    	this.setMoving(moving);
	    }

}
