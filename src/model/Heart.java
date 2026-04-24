package model;


public class Heart {
	public static final int HEART_WIDTH = 60;
    public static final int HEART_HEIGHT = 60;
	public static final int HEART_VALUE = 1;

	private final Position position;
	private HitBox hitBox;
	
	public Heart(Position position) {
		this.position = position;	
		this.hitBox = new HitBox(position.getX(), position.getY(), HEART_WIDTH, HEART_HEIGHT); 
	}
	
	public Heart(int x, int y) {
		this(new Position(x,y));
	}
	
	public Position getPosition() {
		return position;
	}
	
	public int getX() {
		return position.getX();
	}
	
	public int getY() {
		return position.getY();
	}
	
	public HitBox getHitBox() {
        return hitBox;
    }
	
	public static int getHeartValue() {
		return HEART_VALUE;
	}

	public static int getHeartWidth() {
		return HEART_WIDTH;
	}

	public static int getHeartHeight() {
		return HEART_HEIGHT;
	}

}
