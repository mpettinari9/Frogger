package model;

import java.util.ArrayList;
import java.util.Random;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Game {
	//Costanti di gioco
	public static final int HEART_SPAWN_INTERVAL = 15; //Secondi tra spawn cuori
	public static final int[] OBJECT_OFFSETS = {0, 300, 600, 900, 1200}; //Posizioni spawn oggetti
    public static final int SPAWN_OFFSET = 50; // Offset extra spawn

	private Map map;
	private Frog frog;
	private Heart heart;
	private ArrayList<MovingObject> movingObjects;
	private Random rnd;
	
    private boolean objectsSpawned; //Flag oggetti già spawnati
    private boolean isHeartSpawned; //Flag cuore spawnato
    private int heartSpawnCycles; //Contatore spawn cuori
    private String death; //'Morte' formatto stringa
	
	private LocalDateTime startTime;
	private LocalDateTime lastHeartSpawnTime; //Ultimo spawn cuore
	
    // Contatori per alternare corsie
	private int carLaneCount, truckLaneCount, turtleLaneCount, trunkLaneCount;
	
	public Game(Map map) {
		this.startTime = LocalDateTime.now();
		this.map = map;
		this.rnd = new Random();
		this.movingObjects = new ArrayList<>();
		this.isHeartSpawned = false;
		this.heartSpawnCycles = 0;
		this.death = "";
		this.lastHeartSpawnTime = LocalDateTime.now();
	}
	
	//Getter e setter
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

	public Heart getHeart() {
		return heart;
	}

	public boolean isHeartSpawned() {
		return isHeartSpawned;
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
		return heartSpawnCycles;
	}

	//Movimenti rana con correzione posizione
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

	//Gestione oggetti dinamici
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

    //Spawn iniziale degli oggetti (1 sola volta)
    public void spawnMovingObject() {
        if (!objectsSpawned) {
            // Spawna 5 oggetti per ogni tipo 
            for (int i = 0; i < 5; i++) {
                spawnMovingObjectByType(MovingObjectType.CAR, OBJECT_OFFSETS[i]);
                spawnMovingObjectByType(MovingObjectType.TRUCK, OBJECT_OFFSETS[i]);
                spawnMovingObjectByType(MovingObjectType.TURTLE, OBJECT_OFFSETS[i]);
                spawnMovingObjectByType(MovingObjectType.TRUNK, OBJECT_OFFSETS[i]);
            }
            objectsSpawned = true;
        }
    }
    
    //Spawn oggetto per tipo
    private void spawnMovingObjectByType(MovingObjectType type, int offset) {
        int x, y;
        Direction direction;
        
        switch (type) {
        //Strada
        case CAR:
            y = (carLaneCount == 0) ? 500 : 440; //Corsie auto
            carLaneCount = (carLaneCount + 1) % 2; //Alterna corsie
            direction = Direction.RIGHT;
            x =  offset;
            break;
            
        case TRUCK:
            y = (truckLaneCount == 0) ? 390 : 340; //Corsie camion 
            truckLaneCount = (truckLaneCount + 1) % 2;
            direction = Direction.LEFT;
            x =  + SPAWN_OFFSET + offset;
            break;
            
        //Fiume    
        case TURTLE:
            y = (turtleLaneCount == 0) ? 105 : 225; //Corsie tartarughe
            turtleLaneCount = (turtleLaneCount + 1) % 2;
            direction = Direction.RIGHT;
            x = offset;
            break;
            
        case TRUNK:
            y = (trunkLaneCount == 0) ? 55 : 165;  //Corsie tronchi
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

	//Gestione collisioni tra Frog e MovingObject
    private void movingObjectCollision(MovingObject obj) {
        switch (obj.getMovingObjectType()) {
        case CAR:
        case TRUCK:
            if (this.frog.getLives() > 1) {
                //Se hai più di 1 vita: perdi una vita e resetti posizione
                this.frog.loseLife();
                this.frog.resetToInitialPosition();
                this.death = ""; //Continui a giocare
            } else {
                //Se hai solo 1 vita: muori
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

	//Verifica collisione con gli oggetti
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
    
    //Trasporto rana tramite un oggetto (tronchi/tartarughe)
    public void onWaterObject(MovingObject waterObj) {
    	
        if (waterObj.getDirection() == Direction.RIGHT) {
        	frog.setX(frog.getX() + waterObj.getSpeed());
        	
  
        } else {
        	frog.setX(frog.getX() - waterObj.getSpeed());
        }
        frog.updateHitBox();
    }
    
	public void setLastHeartSpawnTime(LocalDateTime time) {
	    this.lastHeartSpawnTime = time;
	}
	
	//Spawn cuore ogni EARN_LIFE_SPAWN_INTERVAL secondi
    public void heartSpawn() {
	    LocalDateTime now = LocalDateTime.now();
	    long secondsSinceLastSpawn = this.lastHeartSpawnTime.until(now, ChronoUnit.SECONDS);

	    if (!this.isHeartSpawned && secondsSinceLastSpawn >= HEART_SPAWN_INTERVAL) {
	    	int x = rnd.nextInt(0, map.getWidth() - Heart.getHeartWidth());
	    	int y = rnd.nextInt(0, map.getHeight() - Heart.getHeartHeight());

	        this.heart = new Heart(x, y);
	        this.isHeartSpawned = true;
	        this.heartSpawnCycles++;
	        this.lastHeartSpawnTime = now; // reset timer
	    }
	}
    
	//Verifica collisione con un cuore (vita rigenerata)
	public boolean checkHeartCollision() {
		 if (frog == null || heart == null || !isHeartSpawned) 
	            return false;
	        
	        if (frog.getHitBox().intersects(heart.getHitBox())) {
	            frog.resetLives(); //Rigenera tutte le vite (2)
	            isHeartSpawned = false; //Cuore scompare
	            heart = null;
	            return true;
	        }
	        return false;
	}
	
	//Calcolo durata partita formato orologio (modulo 60/24).
	private long getDurationSeconds(LocalDateTime time) {
		return this.startTime.until(time, ChronoUnit.SECONDS)%60;
	}
		
	private long getDurationMinutes(LocalDateTime time) {
		return this.startTime.until(time, ChronoUnit.MINUTES)%60;
	}

	private long getDurationHours(LocalDateTime time) {
		return this.startTime.until(time, ChronoUnit.HOURS)%24;
	}
		
	public long getMatchDurationSeconds() {
		return this.getDurationSeconds(LocalDateTime.now());
	}
		
	public long getMatchDurationMinutes() {
		return this.getDurationMinutes(LocalDateTime.now());
	}

	public long getMatchDurationHours() {
		return this.getDurationHours(LocalDateTime.now());
	}
	
	//Formattazione tempo partita come HH:MM:SS
	private String formatMatchTime() {
	    return String.format("%02d:%02d:%02d",
	        getMatchDurationHours(),
	        getMatchDurationMinutes(),
	        getMatchDurationSeconds()
	    );
	}
	
	//Verifica game over (vite <=0 o rana raggiunge obiettivo)
	public boolean checkGameOver() {
		if (frog == null) return false;
		
		boolean hasLost = frog.getLives() <= 0; 
		boolean hasWon = frog.getY() <= 0;	
		
		if ((hasLost || hasWon) && death.isEmpty() ) 
	        death = formatMatchTime();
	        
		return hasLost || hasWon;
	}
	
	//Ciclo principale di aggiornamento del gioco
	public void update() {
		spawnMovingObject();          //Spawn iniziale oggetti
        updateMovingObjects();        //Muove auto, camion, tronchi, tartarughe
        checkMovingObjectCollision(); //Controlla collisioni con veicoli
        checkWaterCollision();        //Controlla annegamento/trasporto acqua
        heartSpawn();              //Spawn cuore extra vita
        checkHeartCollision();        //Controlla raccolta cuore
        checkGameOver();              //Verifica fine partita
	}
    
}