package model;

//Cuore che il giocatore può raccogliere per ottenere una vita extra.
//Viene spawnato casualmente sulla mappa ogni 15 secondi.
public class Heart implements Collectible {
	//Dimensioni fisse del cuore (60x60 pixel)
	public static final int HEART_WIDTH = 60;
    public static final int HEART_HEIGHT = 60;
    //1 vita concessa
	public static final int HEART_VALUE = 1;

	private final Position position; //Posizione sulla mappa
	private HitBox hitBox; //Rilevamento collisione con rana
	private boolean active;
	
	//Costruttore con oggetto posizione
	public Heart(Position position) {
		this.position = position;	
		this.hitBox = new HitBox(position.getX(), position.getY(), HEART_WIDTH, HEART_HEIGHT); 
		this.active = true;
	}
	
	//Costruttore con coordinate
	public Heart(int x, int y) {
		this(new Position(x,y));
	}
	
	@Override
	public void onCollect(Frog frog, Game game) {
	    frog.resetLives();
	    this.active = false;
    }

	@Override
	public boolean isActive() {
	    return active;
	}

    @Override
	public HitBox getHitBox() {
	    return hitBox;
	}
	    
	//Getter
	public Position getPosition() {
		return position;
	}
	
	public int getX() {
		return position.getX();
	}
	
	public int getY() {
		return position.getY();
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
