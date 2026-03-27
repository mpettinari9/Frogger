package model;

public class MovingObject {
	public static final int START_X_LEFT = -200;
    public static final int START_X_RIGHT = 1280;
    public static final int BASE_SPEED = 2;

    private final MovingObjectType type;
    private Direction direction;  
    private Position position;
    private final Size size;
    private HitBox hitBox;  
    private final int screenWidth;
    
    public MovingObject(Position position, MovingObjectType type, Map map, int spawnOffset) {
        this.position = position;
        this.type = type;
        this.screenWidth = map.getWidth();      
        this.size = new Size(type.getWidth(), type.getHeight());       
        this.hitBox = new HitBox(position.getX(), position.getY(), size.getWidth(), size.getHeight());
    }  

    public HitBox getHitBox() {
        return hitBox;
    }

    public Position getPosition() {
        return position;
    }

    public Size getSize() {
        return size;
    }

    public MovingObjectType getMovingObjectType() {
        return type;
    }

    public Direction getDirection() {
    	return direction;
    }
    
    public void setDirection(Direction direction) {
    	this.direction = direction;
    }
    

    public int getSpeed() {
    	return BASE_SPEED;
    }
}

