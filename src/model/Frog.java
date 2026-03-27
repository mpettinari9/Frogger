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
	private final int screenWidth;
	private final int screenHeight;
	private HitBox hitBox;
	private Position initialPosition;
	private Direction initialDirection;
	
	public Frog(String name, Direction direction, Size size, Position position, Map map, int lives) {
		this.name = name;
		this.direction = direction;
		this.size = size;
		this.position = position;
		this.map = map;
		this.lives = lives;
		this.maxLives = DEFAULT_LIVES;
		this.screenHeight = map.getHeight();
		this.screenWidth = map.getWidth();
		this.hitBox = new HitBox(position.getX(), position.getY(), size.getWidth(), size.getHeight());
		this.initialPosition = new Position(position.getX(), position.getY());
		this.initialDirection = direction;
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
	
	public HitBox getHitBox() {
        return this.hitBox;
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
	
    public void moveUp() {
        this.position.move(0, -MOVE_STEP);
        this.direction = Direction.UP;
        updateHitBox();
    }

    public void moveDown() {
        this.position.move(0, MOVE_STEP);
        this.direction = Direction.DOWN; 
        updateHitBox();
    }

    public void moveRight() {
        this.position.move(MOVE_STEP, 0);
        this.direction = Direction.RIGHT; 
        updateHitBox();
    }

    public void moveLeft() {
        this.position.move(-MOVE_STEP, 0);
        this.direction = Direction.LEFT; 
        updateHitBox();
    }
    
    public void updateHitBox() {
        this.hitBox.setX(this.position.getX());
        this.hitBox.setY(this.position.getY()); 
        this.hitBox.setWidth(this.size.getWidth()); 
        this.hitBox.setHeight(this.size.getHeight()); 
     }
    
    public void loseLife() {
    	this.lives--;
    }
    
    public void resetLives() {
    	this.lives = DEFAULT_LIVES;
    }
    
    public boolean isDead() {
    	return this.lives <= 0;
    }
    
    public boolean isOutOfBounds() {
        int centerX = position.getX() + size.getWidth() / 2;
        int centerY = position.getY() + size.getHeight() / 2;
        
        return centerX < 0 || 
               centerX > this.screenWidth ||
               centerY < 0 || 
               centerY > this.screenHeight;
    }

    public void correctPosition() {
        int centerX = position.getX() + size.getWidth() / 2;
        int centerY = position.getY() + size.getHeight() / 2;
        
        if (centerX < 0) {
            position.setX(-size.getWidth() / 2);
        }
        if (centerX > this.screenWidth) {
            position.setX(this.screenWidth - size.getWidth() / 2);
        }
        if (centerY < 0) {
            position.setY(-size.getHeight() / 2);
        }
        if (centerY > this.screenHeight) {
            position.setY(this.screenHeight - size.getHeight() / 2);
        }
        updateHitBox();
    }
    
    public boolean isInWaterArea() {
    	int centerY = this.position.getY() + this.size.getHeight() / 2;
        return centerY >= map.getRiverTop() && centerY <= map.getRiverBottom();   }
    
    // Metodo per resettare la posizione iniziale
    public void resetToInitialPosition() {
        this.position.setX(initialPosition.getX());
        this.position.setY(initialPosition.getY()); 
        this.direction = initialDirection;
        updateHitBox();
    }
    
    public int getCenterX() {
        return this.position.getX() + this.size.getWidth() / 2;
    }

    public int getCenterY() {
        return this.position.getY() + this.size.getHeight() / 2;
    }

}
