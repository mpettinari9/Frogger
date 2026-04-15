package model;

import java.util.ArrayList;
import java.util.Random;
import java.time.LocalDateTime;

public class Game {
	public static final int EARN_LIFE_SPAWN_INTERVAL = 15; 
	public static final int[] OBJECT_OFFSETS = {0, 300, 600, 900,1200};
    public static final int SPAWN_OFFSET = 50;

	private Map map;
	private Frog frog;
	private EarnLife earnLife;
	private ArrayList<MovingObject> movingObjects;
	private Random rnd;
	
    private boolean objectsSpawned;
	private boolean isEarnLifeSpawned;
	private int earnLifeSpawnCycles;
	private String death; //morte formato stringa
	
	private LocalDateTime startTime;
	private LocalDateTime lastEarnLifeSpawnTime; 
	
	private int carLaneCount, truckLaneCount, turtleLaneCount, trunkLaneCount;
	
	public Game(Map map) {
		this.startTime = LocalDateTime.now();
		this.map = map;
		this.rnd = new Random();
		this.movingObjects = new ArrayList<>();
		this.isEarnLifeSpawned = false;
		this.earnLifeSpawnCycles = 0;
		this.death = "";
		this.lastEarnLifeSpawnTime = LocalDateTime.now();
	}

	public Map getMap() {
		return map;
	}

	public Frog getFrog() {
		return frog;
	}
	
	public void setFrog(Frog frog) {
	    this.frog = frog;
	}
	
    public ArrayList<MovingObject> getMovingObjects() {
        return movingObjects;
    }

	public EarnLife getEarnLife() {
		return earnLife;
	}

	public boolean isEarnLifeSpawned() {
		return isEarnLifeSpawned;
	}
	
	public String getDeath() {
		return death;
	}
	
	public Random getRandomNumberGenerator() {
		return rnd;
	}
	
	public LocalDateTime getStartTime() {
		return startTime;
	}

	public int getEarnLifeSpawnCycles() {
		return earnLifeSpawnCycles;
	}

	public void moveFrogUp() {
		frog.moveUp();
	    frog.correctPosition();
	}

	public void moveFrogDown() {
		frog.moveDown();
	    frog.correctPosition();
	}

	public void moveFrogLeft() {
		frog.moveLeft();
	    frog.correctPosition();
	}

	public void moveFrogRight() {
		frog.moveRight();
	    frog.correctPosition();
	}

    public void addMovingObject(MovingObject obj) {
        movingObjects.add(obj);
    }

    public void removeMovingObject(MovingObject obj) {
        movingObjects.remove(obj);
    }

    public void updateMovingObjects() {
        for (MovingObject obj : movingObjects) {
            obj.updatePosition();
        }
    }

    public void spawnMovingObject() {
        if (!objectsSpawned) {
            // Spawna 5 oggetti per ogni tipo (3 per corsia)
            for (int i = 0; i < 5; i++) {
                spawnMovingObjectByType(MovingObjectType.CAR, OBJECT_OFFSETS[i]);
                spawnMovingObjectByType(MovingObjectType.TRUCK, OBJECT_OFFSETS[i]);
                spawnMovingObjectByType(MovingObjectType.TURTLE, OBJECT_OFFSETS[i]);
                spawnMovingObjectByType(MovingObjectType.TRUNK, OBJECT_OFFSETS[i]);
            }
            objectsSpawned = true;
        }
    }
    
    private void spawnMovingObjectByType(MovingObjectType type, int offset) {
        int x, y;
        Direction direction;
        
        switch (type) {
        case CAR:
            // STRADA: corsie basse
            y = (carLaneCount == 0) ? 500 : 440;
            carLaneCount = (carLaneCount + 1) % 2;
            direction = Direction.RIGHT;
            x =  offset;
            break;
            
        case TRUCK:
            // STRADA: corsie alte (ma SOTTO la zona safe!)
            y = (truckLaneCount == 0) ? 390 : 340;
            truckLaneCount = (truckLaneCount + 1) % 2;
            direction = Direction.LEFT;
            x =  + SPAWN_OFFSET + offset;
            break;
            
        case TURTLE:
            // FIUME: corsie medie
            y = (turtleLaneCount == 0) ? 105 : 225;
            turtleLaneCount = (turtleLaneCount + 1) % 2;
            direction = Direction.RIGHT;
            x = offset;
            break;
            
        case TRUNK:
            // FIUME: corsie estreme (LONTANO dal confine 230!)
            y = (trunkLaneCount == 0) ? 55 : 165;  //
            trunkLaneCount = (trunkLaneCount + 1) % 2;
            direction = Direction.LEFT;
            x =  + SPAWN_OFFSET + offset;
            break;
            
        default:
            return;
        }
        
        MovingObject obj = new MovingObject(x, y, type, map, offset);
        obj.setDirection(direction); 
        movingObjects.add(obj);    
    }

	// Metodo per la logica della collisione tra Frog e MovingObject
    private void movingObjectCollision(MovingObject obj) {
        switch (obj.getMovingObjectType()) {
        case CAR:
        case TRUCK:
            if (this.frog.getLives() > 1) {
                // Hai più di 1 vita: perdi una vita e resetti
                this.frog.loseLife();
                this.frog.resetToInitialPosition();
                this.death = ""; // Continui a giocare
            } else {
                // Hai solo 1 vita: muori
                this.frog.loseLife();
                this.frog.resetToInitialPosition();
                this.death = "Hit by " + obj.getMovingObjectType().name().toLowerCase() + " - " + formatMatchTime();
            }
            break;
            
        case TURTLE:
        case TRUNK:
           
            break;
        }
    }
    
    private String formatMatchTime() {
		// TODO Auto-generated method stub
		return null;
	}

	// Metodo per la verifica della collisione con gli oggetti
    public boolean checkMovingObjectCollision() {
        if (frog == null) 
        	return false;
        
        for (MovingObject obj : movingObjects) {
            if (frog.getHitBox().intersects(obj.getHitBox())) {
                movingObjectCollision(obj);
                return true;
            }
        }
        return false;
    }
    
    // Metodo per essere trasportato da un oggetto (tronchi/tartarughe)
    public void onWaterObject(MovingObject waterObj) {
    	
    }
    
    // Se la rana è in acqua senza piattafroma, muore.
    public void checkWaterCollision() {
        if (frog == null || frog.getLives() <= 0) return; 
        
        if (frog.isInWaterArea()) {
            boolean onPlatform = false;
            MovingObject platformObject = null;
            
            for (MovingObject obj : movingObjects) {
                MovingObjectType type = obj.getMovingObjectType();
                if ((type == MovingObjectType.TURTLE || type == MovingObjectType.TRUNK) 
                    && frog.getHitBox().intersects(obj.getHitBox())) {
                    onPlatform = true;
                    platformObject = obj;
                    break;
                }
            }
            
            if (onPlatform && platformObject != null) {
                onWaterObject(platformObject);
                frog.correctPosition();
            } else {
                if (frog.getLives() > 1) {
                    frog.loseLife();
                    frog.resetToInitialPosition();
                    death = "";
                } else {
                    frog.loseLife();
                    frog.resetToInitialPosition();
                    if (death.isEmpty()) {
                        death = "Drowned in water - " + formatMatchTime();
                    }
                }
            }
        }
    }
    
}