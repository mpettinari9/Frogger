package view;

import javax.swing.*;

public class Sprite extends JLabel{
	public static final int DELAY = 5; /* tempo di azione del frame in step logici / ms */
	
	private ImageIcon[] upAnimations; 	/* insieme dei frame per le animazioni verso l'alto */
	private ImageIcon[] downAnimations; /* insieme dei frame per le animazioni verso il basso */
	private ImageIcon[] leftAnimations; /* insieme dei frame per le animazioni verso sinistra */
	private ImageIcon[] rightAnimations;/* insieme dei frame per le animazione verso destra */
	private SpriteDirection direction; 	/* direzione dello sprite */
	private boolean moving; 			/* movimento dello sprite */
	
	protected Sprite(String baseImagePath, int numberOfFrames) {
		super(new ImageIcon(baseImagePath));
		this.upAnimations = new ImageIcon[numberOfFrames];
		this.downAnimations = new ImageIcon[numberOfFrames];
		this.leftAnimations = new ImageIcon[numberOfFrames];
		this.rightAnimations = new ImageIcon[numberOfFrames];
		this.direction = SpriteDirection.DOWN;
		this.moving = false;
	}

	protected ImageIcon[] getUpAnimations() {
		return this.upAnimations;
	}

	protected ImageIcon[] getDownAnimations(){
		return this.downAnimations;
	}

	protected ImageIcon[] getLeftAnimations(){
		return this.leftAnimations;
	}

	protected ImageIcon[] getRightAnimations(){
		return this.rightAnimations;
	}

	public SpriteDirection getDirection() {
		return this.direction;
	}

	public boolean isMoving() {
		return this.moving;
	}

	public void setDirection(SpriteDirection direction) {
		this.direction = direction;
	}

	public void setMoving(boolean moving) {
		this.moving = moving;
	}
	
}
