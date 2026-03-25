package model;

public class Frog {
	public static final int DEFAULT_LIVES = 2;
	public static final double MOVE_STEP = 2;
	
	private String name;
	private Direction direction;
	private final Size size;
	private Position position;
	private final Map map;
	private int lives;
	private int maxLives;
	private final int screenwidth;
	private final int screenHeight;
	
	public Frog(String name, Direction direction, Size size, Position position, Map map, int lives) {
		this.name = name;
		this.direction = direction;
		this.size = size;
		this.position = position;
		this.map = map;
		this.lives = lives;
		this.maxLives = DEFAULT_LIVES;
		this.screenHeight = map.getHeight();
		this.screenwidth = map.getWidth();
	}

	public String getName() {
		return name;
	}

	public Direction getDirection() {
		return direction;
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
	
	public int getWidth() {
		return size.getWidth();
	}
	
	public int getHeight() {
		return size.getHeight();
	}
	
	public int getLives() {
		return lives;
	}

	public int getMaxLives() {
		return maxLives;
	}
	
	public Size getSize() {
		return size;
	}
	
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setDirection(Direction direction) {
		this.direction = direction;
	}
	
	public void setPosition(Position position) {
		this.position = position;
	}
	
	public void setLives(int lives) {
		this.lives = lives;
	}

	public void setX(int x) {
		this.position.setX(x);
	}
	
	public void setY(int y) {
		this.position.setY(y);
	}
	

}
