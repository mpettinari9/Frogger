package model;

//Oggetto mobile del gioco (auto, camion, tartaruga, tronco).
//Si muove orizzontalmente da sinistra a destra o viceversa, e si riposiziona quando esce dallo schermo.
public class MovingObject {
    // Coordinate di reset quando l'oggetto esce dallo schermo
    public static final int START_X_LEFT = -200;   // Reset per oggetti che vanno a destra
    public static final int START_X_RIGHT = 1280;  // Reset per oggetti che vanno a sinistra
    public static final int BASE_SPEED = 2;        // Velocità costante di movimento

    private final MovingObjectType type;
    private Direction direction;  
    private Position position;
    private final Size size;
    private HitBox hitBox;  
    private final int screenWidth;
    
    //Costruttore con oggetto Position
    public MovingObject(Position position, MovingObjectType type, Map map, int spawnOffset) {
        this.position = position;
        this.type = type;
        this.screenWidth = map.getWidth();      
        this.size = new Size(type.getWidth(), type.getHeight());       
        this.hitBox = new HitBox(position.getX(), position.getY(), size.getWidth(), size.getHeight());
    }  

    //Costruttore con coordinate x, y
    public MovingObject(int x, int y, MovingObjectType type, Map map, int spawnOffset) {
        this(new Position(x, y), type, map, spawnOffset);
    }

    //Getter e setter
	public HitBox getHitBox() {
        return hitBox;
    }

    public Position getPosition() {
        return position;
    }
    
    public void setPosition(Position position) {
        this.position = position;
        updateHitBox();
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
    
    //Sincronizza l'hitbox con posizione e dimensioni correnti
    private void updateHitBox() {
        this.hitBox.setX(this.position.getX());
        this.hitBox.setY(this.position.getY()); 
        this.hitBox.setWidth(this.size.getWidth()); 
        this.hitBox.setHeight(this.size.getHeight()); 
    }
    
    //Aggiorna la posizione in base alla direzione.
    //Se esce dallo schermo, viene riposizionato all'inizio opposto.
    public void updatePosition() {
        if (direction == Direction.RIGHT) {
            position.setX(position.getX() + BASE_SPEED);
        } else {
            position.setX(position.getX() - BASE_SPEED);
        }
        updateHitBox();

        if (isOutOfBounds()) {
            resetPositionAtStart();
        }
    }

    //Verifica se l'oggetto è completamente uscito dallo schermo
    private boolean isOutOfBounds() {
        if (direction == Direction.RIGHT) {
            return position.getX() > screenWidth;
        } else {
            return position.getX() + size.getWidth() < 0;
        }
    }
    
    //Riposiziona l'oggetto all'inizio del percorso (ciclo continuo)
    private void resetPositionAtStart() {
        if (direction == Direction.RIGHT) {
            position.setX(START_X_LEFT );
        } else {
            position.setX(START_X_RIGHT);
        }
        updateHitBox();
    }
}

