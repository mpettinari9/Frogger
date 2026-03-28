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


	
}