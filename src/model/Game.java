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

	    // Verifica collisione con gli oggetti mobili per tutte le rane in partita
	    public boolean checkMovingObjectCollision() {
	        boolean collisionFound = false;
	        for (int i = 0; i < frogs.length; i++) {
	            Frog frog = frogs[i];
	            if (frog == null || frog.isDead()) continue;

	            for (MovingObject obj : movingObjects[i]) {
	                if (frog.getHitBox().intersects(obj.getHitBox())) {
	                    movingObjectCollision(frog, i, obj);
	                    collisionFound = true;
	                    break;
	                }
	            }
	        }
	        return collisionFound;
	    }

	    // Verifica annegamento/trasporto su tronchi e tartarughe, per tutte le rane
	    public void checkWaterCollision() {
	        for (int i = 0; i < frogs.length; i++) {
	            checkWaterCollision(frogs[i], i);
	        }
	    }

	    private void checkWaterCollision(Frog frog, int frogIndex) {
	        if (frog == null || frog.getLives() <= 0) return;

	        if (frog.isInWaterArea()) {
	            boolean onPlatform = false;
	            MovingObject platformObject = null;

	            for (MovingObject obj : movingObjects[frogIndex]) {
	                MovingObjectType type = obj.getMovingObjectType();
	                if ((type == MovingObjectType.TURTLE || type == MovingObjectType.TRUNK)
	                        && frog.getHitBox().intersects(obj.getHitBox())) {
	                    onPlatform = true;
	                    platformObject = obj;
	                    break;
	                }
	            }

	            if (onPlatform && platformObject != null) {
	                onWaterObject(frog, platformObject);
	                frog.correctPosition();
	            } else {
	                if (frog.getLives() > 1) {
	                    frog.loseLife();
	                    frog.resetToInitialPosition();
	                    death[frogIndex] = "";
	                } else {
	                    frog.loseLife();
	                    frog.resetToInitialPosition();
	                    if (death[frogIndex].isEmpty()) {
	                        death[frogIndex] = "Drowned in water - " + formatMatchTime();
	                    }
	                }
	            }
	        }
	    }

	    public void onWaterObject(Frog frog, MovingObject waterObj) {
	        if (waterObj.getDirection() == Direction.RIGHT) {
	            frog.setX(frog.getX() + waterObj.getSpeed());
	        } else {
	            frog.setX(frog.getX() - waterObj.getSpeed());
	        }
	        frog.updateHitBox();
	    }

	    // --- Collisione generica con Collectible (cuore, insetto) ---
	    // Instrada il punteggio al giocatore corretto tramite indexOfFrog,
	    // dato che Collectible.onCollect riceve solo l'oggetto Frog.

	    public boolean checkCollectibleCollision(Collectible c, Frog frog) {
	        if (c == null || !c.isActive() || frog == null) return false;
	        if (frog.getHitBox().intersects(c.getHitBox())) {
	            c.onCollect(frog, this);
	            return true;
	        }
	        return false;
	    }
	    
	 // --- Spawn e controllo tane ---

	    // Controlla se una specifica rana ha raggiunto una propria tana libera
	    private boolean checkHomeSlotCollision(Frog frog, int frogIndex) {
	        if (frog == null) return false;

	        for (HomeSlot slot : homeSlots[frogIndex]) {
	            if (!slot.isOccupied() && frog.getHitBox().intersects(slot.getHitBox())) {
	                if (slot.hasInsect()) {
	                    slot.getInsect().onCollect(frog, this);
	                }
	                slot.setOccupied(true);
	                addScore(frogIndex, SLOT_POINTS);
	                frog.resetToInitialPosition();
	                return true;
	            }
	        }
	        return false;
	    }

	    // Controlla le tane per tutte le rane in partita
	    public boolean checkHomeSlotCollision() {
	        boolean any = false;
	        for (int i = 0; i < frogs.length; i++) {
	            if (checkHomeSlotCollision(frogs[i], i)) any = true;
	        }
	        return any;
	    }

	    // Verifica se tutte le 5 tane del giocatore all'indice "index" sono occupate
	    private boolean allSlotsOccupied(int index) {
	        for (HomeSlot slot : homeSlots[index]) {
	            if (!slot.isOccupied()) return false;
	        }
	        return true;
	    }
	    
	    // --- Spawn cuore (condiviso: il primo che lo raccoglie ottiene il beneficio) ---


	    // Genera un cuore indipendente per ciascun giocatore, all'interno della SUA metà schermo
	    // (così con 2 giocatori entrambi hanno la stessa possibilità di trovarne uno, e non capita
	    // che un cuore casuale finisca tutto dalla parte di un solo giocatore).
	    public void heartSpawn() {
	        LocalDateTime now = LocalDateTime.now();
	        int numberOfFrogs = frogs.length;
	        int sectionWidth = (numberOfFrogs == 1) ? map.getWidth() : map.getWidth() / numberOfFrogs;

	        for (int p = 0; p < numberOfFrogs; p++) {
	            long secondsSinceLastSpawn = this.lastHeartSpawnTime[p].until(now, ChronoUnit.SECONDS);

	            if (!this.isHeartSpawned[p] && secondsSinceLastSpawn >= HEART_SPAWN_INTERVAL) {
	                int xBase = p * sectionWidth;
	                int x = xBase + rnd.nextInt(0, sectionWidth - Heart.getHeartWidth());
	                int y = rnd.nextInt(0, map.getHeight() - Heart.getHeartHeight());
	                this.hearts[p] = new Heart(x, y);
	                this.isHeartSpawned[p] = true;
	                this.heartSpawnCycles[p]++;
	                this.lastHeartSpawnTime[p] = now;
	            }
	        }
	    }

	    // --- Spawn insetto in una tana libera casuale ---
	    // Distribuisce gli insetti tra le tane di TUTTI i giocatori (ognuno ha le proprie tane)

	    public void insectSpawn() {
	        LocalDateTime now = LocalDateTime.now();
	        long seconds = this.lastInsectSpawnTime.until(now, ChronoUnit.SECONDS);

	        boolean anyInsectActive = false;
	        for (HomeSlot[] playerSlots : homeSlots) {
	            for (HomeSlot slot : playerSlots) {
	                if (slot.hasInsect()) { anyInsectActive = true; break; }
	            }
	            if (anyInsectActive) break;
	        }

	        if (!anyInsectActive && seconds >= INSECT_SPAWN_INTERVAL) {
	            // Raccoglie tutte le tane libere senza insetto, di qualsiasi giocatore
	            ArrayList<HomeSlot> freeSlots = new ArrayList<>();
	            for (HomeSlot[] playerSlots : homeSlots) {
	                for (HomeSlot slot : playerSlots) {
	                    if (!slot.isOccupied() && !slot.hasInsect()) {
	                        freeSlots.add(slot);
	                    }
	                }
	            }
	            if (!freeSlots.isEmpty()) {
	                HomeSlot slot = freeSlots.get(rnd.nextInt(freeSlots.size()));
	                Insect insect = new Insect(slot.getX(), slot.getY(), 0);
	                slot.setInsect(insect);
	            }
	            this.lastInsectSpawnTime = now;
	        }

	        // Controlla scadenza insetti nelle tane di tutti i giocatori
	        for (HomeSlot[] playerSlots : homeSlots) {
	            for (HomeSlot slot : playerSlots) {
	                if (slot.hasInsect() && slot.getInsect().isExpired()) {
	                    slot.getInsect().deactivate();
	                    slot.setInsect(null);
	                    this.lastInsectSpawnTime = LocalDateTime.now();
	                }
	            }
	        }
	    }

	    // --- Controllo collisione cuore, per tutte le rane ---

	    // --- Controllo collisione cuore, per ciascun giocatore con il proprio cuore ---

	    public boolean checkHeartCollision() {
	        boolean any = false;
	        for (int p = 0; p < frogs.length; p++) {
	            if (hearts[p] == null || !isHeartSpawned[p]) continue;
	            Frog frog = frogs[p];
	            if (frog == null) continue;
	            if (checkCollectibleCollision(hearts[p], frog)) {
	                isHeartSpawned[p] = false;
	                hearts[p] = null;
	                any = true;
	            }
	        }
	        return any;
	    }

	    // --- Game over ---
	    // Verifica se una specifica rana ha concluso la propria gara (vinto occupando le 5 tane, o perso le vite)
	    private boolean checkFrogOver(Frog frog, int index) {
	        if (frog == null) return false;

	        boolean hasLost = frog.getLives() <= 0;
	        boolean hasWon = allSlotsOccupied(index);

	        if ((hasLost || hasWon) && death[index].isEmpty())
	            death[index] = formatMatchTime();

	        return hasLost || hasWon;
	    }

	    // La partita finisce quando TUTTE le rane hanno concluso la propria gara (vinto o perso)
	    public boolean checkGameOver() {
	        boolean allOver = true;
	        for (int i = 0; i < frogs.length; i++) {
	            allOver = allOver && checkFrogOver(frogs[i], i);
	        }
	        return allOver;
	    }

	    // Indica se la rana all'indice "index" ha vinto la propria gara (tutte le tane occupate)
	    public boolean hasFrogWon(int index) {
	        return allSlotsOccupied(index);
	    }

	    // --- Ciclo principale ---

	    public void update() {
	        spawnMovingObject();
	        updateMovingObjects();
	        checkMovingObjectCollision();
	        checkWaterCollision();
	        checkHomeSlotCollision();
	        heartSpawn();
	        checkHeartCollision();
	        insectSpawn();
	        checkGameOver();
	    }
	    
	    
	    // --- Timer partita ---

	    private long getDurationSeconds(LocalDateTime time) { return this.startTime.until(time, ChronoUnit.SECONDS) % 60; }
	    private long getDurationMinutes(LocalDateTime time) { return this.startTime.until(time, ChronoUnit.MINUTES) % 60; }
	    private long getDurationHours(LocalDateTime time) { return this.startTime.until(time, ChronoUnit.HOURS) % 24; }

	    public long getMatchDurationSeconds() { return this.getDurationSeconds(LocalDateTime.now()); }
	    public long getMatchDurationMinutes() { return this.getDurationMinutes(LocalDateTime.now()); }
	    public long getMatchDurationHours() { return this.getDurationHours(LocalDateTime.now()); }

	    private String formatMatchTime() {
	        return String.format("%02d:%02d:%02d",
	                getMatchDurationHours(),
	                getMatchDurationMinutes(),
	                getMatchDurationSeconds());
	    }
}
