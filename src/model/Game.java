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

    public static final int INSECT_SPAWN_INTERVAL = 15;  // Secondi tra spawn insetti
    public static final int SLOT_POINTS = 100; // Punti per ogni tana occupata
    public static final int NUM_SLOTS = 5;     // Tane da occupare per vincere (per ciascun giocatore)

    private Map map;
    private Frog[] frogs;               // Una rana per ciascun giocatore (1 o 2)
    private ArrayList<MovingObject>[] movingObjects; // Ostacoli separati per giocatore
    private Random rnd;
    private HomeSlot[][] homeSlots;     // homeSlots[i] = le 5 tane del giocatore i (gara: tane separate)

    // Collectible: il cuore è indipendente per ciascun giocatore, spawnato nella sua metà schermo
    private Heart[] hearts;

    // Stato
    private boolean objectsSpawned;
    private boolean[] isHeartSpawned;
    private int[] heartSpawnCycles;
    private String[] death; // Una "morte"/risultato formattato per ciascun giocatore
    private int[] scores;   // Punteggio separato per ciascun giocatore

    // Timer
    private LocalDateTime startTime;
    private LocalDateTime[] lastHeartSpawnTime;
    private LocalDateTime lastInsectSpawnTime;

    // Contatori per alternare corsie
	private int carLaneCount, truckLaneCount, turtleLaneCount, trunkLaneCount;
	
	 public Game(Map map, int numberOfFrogs) {
	        this.startTime = LocalDateTime.now();
	        this.map = map;
	        this.rnd = new Random();

	        this.hearts = new Heart[numberOfFrogs];
	        this.isHeartSpawned = new boolean[numberOfFrogs];
	        this.heartSpawnCycles = new int[numberOfFrogs];
	        this.lastHeartSpawnTime = new LocalDateTime[numberOfFrogs];
	        LocalDateTime now0 = LocalDateTime.now();
	        for (int i = 0; i < numberOfFrogs; i++) {
	            this.lastHeartSpawnTime[i] = now0;
	        }

	        this.lastInsectSpawnTime = LocalDateTime.now();

	        this.frogs = new Frog[numberOfFrogs];
	        this.death = new String[numberOfFrogs];
	        this.scores = new int[numberOfFrogs];
	        @SuppressWarnings("unchecked")
	        ArrayList<MovingObject>[] tempMoving = new ArrayList[numberOfFrogs];
	        for (int i = 0; i < numberOfFrogs; i++) {
	            tempMoving[i] = new ArrayList<>();
	        }
	        this.movingObjects = tempMoving;
	        for (int i = 0; i < numberOfFrogs; i++) {
	            this.death[i] = "";
	            this.scores[i] = 0;
	        }

	        initHomeSlots(numberOfFrogs); // Inizializza le 5 tane per ciascun giocatore
	    }

	private void initHomeSlots(int numberOfFrogs) {
		// TODO Auto-generated method stub
		
	}
}
