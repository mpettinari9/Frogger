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

    private int performSprite(SpriteDirection direction, int frame) {
        ImageIcon[] animation;
        switch (direction) {
            case UP:    animation = super.getUpAnimations(); break;
            case DOWN:  animation = super.getDownAnimations(); break;
            case LEFT:  animation = super.getLeftAnimations(); break;
            default:    animation = super.getRightAnimations(); break;
        }

        int totFrames = animation.length; 
        int index = (frame / Sprite.DELAY) % totFrames;
        super.setIcon(animation[index]);
        return totFrames;
    }

    public void performAnimation() {
        if (super.isMoving()) {
            // avanza l’animazione
            this.walkFrame = (this.walkFrame + 1) %
                    (this.performSprite(super.getDirection(), this.walkFrame) * Sprite.DELAY);
        } else {
            // se non si sta muovendo, frame 0
            this.walkFrame = 0;
            this.performSprite(super.getDirection(), 0);
        }
    }
}
