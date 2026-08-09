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

	// Inizializza le tane di ciascun giocatore, distribuite uniformemente in cima alla mappa.
	    // Con 1 giocatore: 5 tane su tutta la larghezza (come nell'originale single-player).
	    // Con 2 giocatori: 5 tane sulla metà sinistra (Giocatore 1) e 5 sulla metà destra (Giocatore 2),
	    // così le corsie di ciascuno restano ben distinte e non si accavallano visivamente.
	    private void initHomeSlots(int numberOfFrogs) {
	        int slotY = 10; // allineato alla posizione visiva delle tane a schermo
	        homeSlots = new HomeSlot[numberOfFrogs][NUM_SLOTS];

	        for (int p = 0; p < numberOfFrogs; p++) {
	            int sectionWidth = map.getWidth() / numberOfFrogs;
	            int sectionStart = (p * sectionWidth) - 15;
	            int spacing = (sectionWidth / NUM_SLOTS) + 5;

	            for (int i = 0; i < NUM_SLOTS; i++) {
	                int slotX = sectionStart + i * spacing + (spacing - HomeSlot.SLOT_WIDTH) / 2;
	                homeSlots[p][i] = new HomeSlot(slotX, slotY);
	            }
	        }
	    }

	    // --- Getter e setter ---

	    public Map getMap() { return map; }

	    public Frog[] getFrogs() { return frogs; }
	    public void setFrog(int index, Frog frog) { this.frogs[index] = frog; }
	    public int getNumberOfFrogs() { return this.frogs.length; }

	    public ArrayList<MovingObject> getMovingObjects(int playerIndex) { return movingObjects[playerIndex]; }
	    // Retrocompatibilità single player
	    public ArrayList<MovingObject> getMovingObjects() { return movingObjects[0]; }
	    public Heart getHeart(int playerIndex) { return hearts[playerIndex]; }
	    public boolean isHeartSpawned(int playerIndex) { return isHeartSpawned[playerIndex]; }
	    public String getDeath(int index) { return death[index]; }
	    public String[] getDeath() { return death; }
	    public Random getRandomNumberGenerator() { return rnd; }
	    public LocalDateTime getStartTime() { return startTime; }
	    public int getEarnLifeSpawnCycles(int playerIndex) { return heartSpawnCycles[playerIndex]; }

	    // Restituisce le 5 tane del giocatore all'indice "index"
	    public HomeSlot[] getHomeSlots(int index) { return homeSlots[index]; }
	    public HomeSlot[][] getHomeSlots() { return homeSlots; }

	    public int getScore(int index) { return scores[index]; }
	    public int[] getScores() { return scores; }

	    // Aggiunge punti al punteggio del giocatore all'indice "frogIndex" (chiamato da Collectible.onCollect)
	    public void addScore(int frogIndex, int points) { this.scores[frogIndex] += points; }

	    // Overload comodo per i Collectible (Heart, Insect), che conoscono solo
	    // l'oggetto Frog che ha raccolto, non il suo indice. Effettua la lookup internamente.
	    public void addScore(Frog frog, int points) {
	        int index = indexOfFrog(frog);
	        if (index >= 0) {
	            this.scores[index] += points;
	        }
	    }
	    
	 // Trova l'indice della rana passata come parametro nell'array frogs.
	    // Usato per instradare il punteggio al giocatore giusto quando un Collectible
	    // (che riceve solo l'oggetto Frog, non l'indice) viene raccolto.
	    private int indexOfFrog(Frog frog) {
	        for (int i = 0; i < frogs.length; i++) {
	            if (frogs[i] == frog) return i;
	        }
	        return -1;
	    }

	    // --- Movimento rana ---
	    // Richiedono la rana su cui agire, per supportare più rane in contemporanea

	    public void moveFrogUp(Frog frog) { frog.moveUp(); frog.correctPosition(); }
	    public void moveFrogDown(Frog frog) { frog.moveDown(); frog.correctPosition(); }
	    public void moveFrogLeft(Frog frog) { frog.moveLeft(); frog.correctPosition(); }
	    public void moveFrogRight(Frog frog) { frog.moveRight(); frog.correctPosition(); }

	    // --- Gestione oggetti mobili (condivisi tra tutti i giocatori) ---

	    public void addMovingObject(int playerIndex, MovingObject obj) { movingObjects[playerIndex].add(obj); }
	    public void addMovingObject(MovingObject obj) { movingObjects[0].add(obj); }
	    public void removeMovingObject(int playerIndex, MovingObject obj) { movingObjects[playerIndex].remove(obj); }
	    public void removeMovingObject(MovingObject obj) { movingObjects[0].remove(obj); }

	    public void updateMovingObjects() {
	        for (ArrayList<MovingObject> playerObjects : movingObjects) {
	            for (MovingObject obj : playerObjects) {
	                obj.updatePosition();
	            }
	        }
	    }

	    public void spawnMovingObject() {
	        if (!objectsSpawned) {
	            for (int p = 0; p < frogs.length; p++) {
	                // Per il giocatore p la mappa è la metà corrispondente
	                // Con 1 giocatore: intera larghezza. Con 2: metà ciascuno.
	                int sectionWidth = (frogs.length == 1) ? map.getWidth() : map.getWidth() / frogs.length;
	                int xBase = p * sectionWidth;
	                // Spawn 5 oggetti per tipo per ciascun giocatore
	                // Con 2 giocatori la sezione è metà schermo: 3 oggetti per tipo bastano
	                // Con 1 giocatore si usa l'intera larghezza: 5 oggetti come prima
	                int numObjects = (frogs.length == 1) ? 5 : 3;
	                for (int i = 0; i < numObjects; i++) {
	                    int offsetInSection = (i * sectionWidth) / numObjects;
	                    spawnMovingObjectByType(p, MovingObjectType.CAR, xBase + offsetInSection, sectionWidth);
	                    spawnMovingObjectByType(p, MovingObjectType.TRUCK, xBase + offsetInSection, sectionWidth);
	                    spawnMovingObjectByType(p, MovingObjectType.TURTLE, xBase + offsetInSection, sectionWidth);
	                    spawnMovingObjectByType(p, MovingObjectType.TRUNK, xBase + offsetInSection, sectionWidth);
	                }
	            }
	            objectsSpawned = true;
	        }
	    }

	    private void spawnMovingObjectByType(int playerIndex, MovingObjectType type, int xStart, int sectionWidth) {
	        int x, y;
	        Direction direction;

	        switch (type) {
	            case CAR:
	                y = (carLaneCount == 0) ? 500 : 440;
	                carLaneCount = (carLaneCount + 1) % 2;
	                direction = Direction.RIGHT;
	                x = xStart;
	                break;
	            case TRUCK:
	                y = (truckLaneCount == 0) ? 390 : 340;
	                truckLaneCount = (truckLaneCount + 1) % 2;
	                direction = Direction.LEFT;
	                x = xStart + SPAWN_OFFSET;
	                break;
	            case TURTLE:
	                y = (turtleLaneCount == 0) ? 105 : 225;
	                turtleLaneCount = (turtleLaneCount + 1) % 2;
	                direction = Direction.RIGHT;
	                x = xStart;
	                break;
	            case TRUNK:
	                y = (trunkLaneCount == 0) ? 55 : 165;
	                trunkLaneCount = (trunkLaneCount + 1) % 2;
	                direction = Direction.LEFT;
	                x = xStart + SPAWN_OFFSET;
	                break;
	            default:
	                return;
	        }

	        // Usa la mappa intera ma con un offset per confinare gli oggetti nella sezione del giocatore
	        MovingObject obj = new MovingObject(x, y, type, map, xStart);
	        obj.setDirection(direction);
	        obj.setSectionBounds(playerIndex * sectionWidth, playerIndex * sectionWidth + sectionWidth);
	        movingObjects[playerIndex].add(obj);
	    }

	    // --- Collisioni con oggetti mobili ---

	    private void movingObjectCollision(Frog frog, int frogIndex, MovingObject obj) {
	        switch (obj.getMovingObjectType()) {
	            case CAR:
	            case TRUCK:
	                if (frog.getLives() > 1) {
	                    frog.loseLife();
	                    frog.resetToInitialPosition();
	                  
	                } else {
	                    frog.loseLife();
	                    frog.resetToInitialPosition();
	                }
	                break;
	            case TURTLE:
	            case TRUNK:
	                break;
	        }
	    }

}
