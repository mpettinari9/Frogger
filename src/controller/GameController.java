package controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.ArrayList;

import model.*;
import view.*;

/* Controlla il flusso di gioco: gestisce input da tastiera, aggiorna il modello
 * e sincronizza la vista ad ogni frame. Implementa il game loop a 60 FPS.
 * Supporta 1 o 2 giocatori in locale: con 2 giocatori, Giocatore 1 (sinistra) = WASD, Giocatore 2 (destra) = Freccette.
 * Con 2 giocatori si gioca in modalità GARA: ciascuno ha le proprie 5 tane,
 * il proprio punteggio e le proprie vite; vince chi occupa prima tutte le tane. */

public class GameController implements KeyListener, Runnable {
	// Costanti di configurazione schermo e gioco
	public static final int SCREEN_WIDTH = 1280;
    public static final int SCREEN_HEIGHT = 720;
    public static final int FPS = 60;
    public static final int FROG_SIZE = 50;
    public static final int RIVER_TOP = 40;
    public static final int RIVER_BOTTOM = 290;

    private Game gameModel;
    private GameWindow gameWindow;
    private HashSet<Integer> pressedKeys;
    private Thread gameThread;
    private volatile boolean gameRunning = false;
    private int numberOfPlayers; // 1 o 2 giocatori
    private boolean vsCPU; // true se il giocatore 2 è controllato dalla CPU
    private AIController aiController; // rana della CPU, attivo solo se vsCPU è true
    // Posizioni iniziali delle rane: se 1 giocatore, al centro; se 2, affiancate
    private int[] startX;
    private int startY = (SCREEN_HEIGHT * 82 / 100) - FROG_SIZE / 2;

    // Costruttore: inizializza finestra, listener tastiera e pulsanti di navigazione
    public GameController() {
        this.gameWindow = new GameWindow(SCREEN_WIDTH, SCREEN_HEIGHT);
        this.pressedKeys = new HashSet<>();
        
        this.gameWindow.addKeyListener(this);
        this.gameWindow.setFocusable(true);
        this.gameWindow.requestFocusInWindow();

        // Avvia partita dal pannello titolo
        this.gameWindow.getTitlePanel().getStartButton().addActionListener(e -> startGame());

        // Mostra il tutorial
        this.gameWindow.getTitlePanel().getTutorialButton().addActionListener(e -> {
            gameWindow.showTutorial();
        });

        // Torna al titolo dal tutorial
        this.gameWindow.getTutorialPanel().getBackButton().addActionListener(e -> {
            gameWindow.showTitle();
        });
        
        // Torna al titolo dai risultati, resettando lo stato di gioco
        this.gameWindow.getResultPanel().getToTitlePanelButton().addActionListener(e -> {
            gameRunning = false;
            gameModel = null;
            pressedKeys.clear();
            
            gameWindow.showTitle();
        });
    
    }

    // Inizializza modello, vista e thread di gioco con i nomi inseriti dai giocatori
    private void startGame() {
        this.numberOfPlayers = this.gameWindow.getTitlePanel().getNumberOfPlayers();
        this.vsCPU = this.gameWindow.getTitlePanel().isVsCPU();
        Difficulty difficulty = this.gameWindow.getTitlePanel().getDifficulty();

        String[] frogNames = new String[numberOfPlayers];
        frogNames[0] = this.gameWindow.getTitlePanel().getFrogName(0);
        if (frogNames[0].isEmpty()) {
            frogNames[0] = "Frog 1";
        }
        if (numberOfPlayers == 2) {
            if (vsCPU) {
                frogNames[1] = "CPU (" + difficultyLabel(difficulty) + ")";
            } else {
                frogNames[1] = this.gameWindow.getTitlePanel().getFrogName(1);
                if (frogNames[1].isEmpty()) {
                    frogNames[1] = "Frog 2";
                }
            }
        }
        
        // Crea mappa e modello
        Map map = new Map(SCREEN_WIDTH, SCREEN_HEIGHT, RIVER_TOP, RIVER_BOTTOM);
        gameModel = new Game(map, numberOfPlayers);
        aiController = vsCPU ? new AIController(gameModel, 1, difficulty) : null;

        // Calcola le posizioni X iniziali: 1 giocatore al centro, 2 giocatori affiancati
        this.startX = new int[numberOfPlayers];
        if (numberOfPlayers == 1) {
            startX[0] = SCREEN_WIDTH / 2 - FROG_SIZE / 2;
        } else {
            startX[0] = SCREEN_WIDTH / 4 - FROG_SIZE / 2;
            startX[1] = (SCREEN_WIDTH * 3 / 4) - FROG_SIZE / 2;
        }

        int[] lifeValues = new int[numberOfPlayers];
        int[] lifeMaxValues = new int[numberOfPlayers];

        // Crea ciascuna rana con posizione e dimensioni iniziali
        for (int i = 0; i < numberOfPlayers; i++) {
            Frog frog = new Frog(frogNames[i], Direction.UP, new Size(FROG_SIZE, FROG_SIZE),
                    new Position(startX[i], startY), map, 2);
            // In modalità 2 giocatori, confina ogni rana nella propria metà dello schermo
            if (numberOfPlayers == 2) {
                int halfWidth = SCREEN_WIDTH / 2;
                frog.setXBounds(i * halfWidth, (i + 1) * halfWidth);
            }
            gameModel.setFrog(i, frog);
            lifeValues[i] = frog.getLives();
            lifeMaxValues[i] = frog.getMaxLives();
        }

        // Crea pannello di gioco e collega listener tastiera
        GamePanel gamePanel = new GamePanel(SCREEN_WIDTH, SCREEN_HEIGHT, frogNames, lifeValues, lifeMaxValues);
       
        // Crea lo sprite per ciascuna rana: ognuna usa il proprio set di asset dedicato
        // Giocatore 1: frogup1/2, frogdown1/2, frogleft1/2, frogright1/2
        // Giocatore 2: frogup21/22, frogdown21/22, frogleft21/22, frogright21/22
        for (int i = 0; i < numberOfPlayers; i++) {
            String[] walkUp, walkDown, walkLeft, walkRight;
            if (i == 0) {
                walkUp = new String[] {"src/view/Asset/frogup1.png", "src/view/Asset/frogup2.png"};
                walkDown = new String[] {"src/view/Asset/frogdown1.png", "src/view/Asset/frogdown2.png"};
                walkLeft = new String[] {"src/view/Asset/frogleft1.png", "src/view/Asset/frogleft2.png"};
                walkRight = new String[] {"src/view/Asset/frogright1.png", "src/view/Asset/frogright2.png"};
            } else {
                walkUp = new String[] {"src/view/Asset/frogup21.png", "src/view/Asset/frogup22.png"};
                walkDown = new String[] {"src/view/Asset/frogdown21.png", "src/view/Asset/frogdown22.png"};
                walkLeft = new String[] {"src/view/Asset/frogleft21.png", "src/view/Asset/frogleft22.png"};
                walkRight = new String[] {"src/view/Asset/frogright21.png", "src/view/Asset/frogright22.png"};
            }
            gamePanel.setFrogSprite(i, startX[i], startY, walkUp, walkDown, walkLeft, walkRight);
        }

        // Crea gli sprite delle tane per ciascun giocatore, in base alle tane create dal modello
        int[][] slotXs = new int[numberOfPlayers][];
        int[][] slotYs = new int[numberOfPlayers][];
        for (int p = 0; p < numberOfPlayers; p++) {
            HomeSlot[] slots = gameModel.getHomeSlots(p);
            slotXs[p] = new int[slots.length];
            slotYs[p] = new int[slots.length];
            for (int i = 0; i < slots.length; i++) {
                slotXs[p][i] = slots[i].getX();
                slotYs[p][i] = slots[i].getY();
            }
        }
        gamePanel.initHomeSlotSprites(slotXs, slotYs, HomeSlot.SLOT_WIDTH, HomeSlot.SLOT_HEIGHT);

        gamePanel.addKeyListener(this);
        gamePanel.setFocusable(true);

        gameWindow.setGamePanel(gamePanel);
        gameWindow.showGame();
        gamePanel.requestFocusInWindow();

        // Avvia il thread solo se non è già attivo
        if (gameThread == null || !gameThread.isAlive()) {
            gameThread = new Thread(this);
            gameThread.start();
        }
    }

	@Override
    public void keyTyped(KeyEvent e) {
    }
    
    // Registra il tasto premuto nel set dei tasti attivi
    @Override
    public void keyPressed(KeyEvent e) {
        pressedKeys.add(e.getKeyCode());
    }

    // Rimuove il tasto rilasciato dal set dei tasti attivi
    @Override
    public void keyReleased(KeyEvent e) {
        pressedKeys.remove(e.getKeyCode());
    }

    // Game loop principale: aggiorna modello e vista a 60 FPS
    @Override
    public void run() {
        long drawInterval = 1000 / FPS;
        gameRunning = true;
        
        while (gameRunning) {
            long startTime = System.currentTimeMillis();

            if (gameModel != null) {
                 moveFrogs(); // Legge input e muove rana
                
                gameModel.update(); // Aggiorna stato del modello (oggetti, collisioni, cuore)
               
                // Sincronizza la vista con il modello
                updateFrogSprites();
                updateMovingObjects();
                updateHeart();
                updateInsects();
                updateHomeSlots();
                updateFrogs();

                if (gameModel.checkGameOver()) {
                    showGameOver();
                    break;
                }
            }

            // Pausa residua per mantenere il frame rate costante
            long elapsed = System.currentTimeMillis() - startTime;
            long sleepTime = drawInterval - elapsed;
            if (sleepTime > 0) {
                try { 
                    Thread.sleep(sleepTime); 
                } catch (InterruptedException e) { 
                    e.printStackTrace(); 
                }
            }
        }
    }

	// Converte MovingObjectType (modello) in MovingObjectTypeSprite (vista)
    private MovingObjectTypeSprite modelToViewMovingObjectTypeConverter(MovingObjectType type) {
        switch (type) {
            case CAR: return MovingObjectTypeSprite.CAR;
            case TRUCK: return MovingObjectTypeSprite.TRUCK;
            case TRUNK: return MovingObjectTypeSprite.TRUNK;
            case TURTLE: return MovingObjectTypeSprite.TURTLE;
            default: return MovingObjectTypeSprite.CAR;
        }
    }
    
    // Sincronizza gli sprite degli oggetti mobili con le posizioni del modello.
    // Aggiunge nuovi sprite se il modello ha più oggetti della vista.
    private void updateMovingObjects() {
        for (int p = 0; p < numberOfPlayers; p++) {
            ArrayList<MovingObject> modelObjects = gameModel.getMovingObjects(p);
            ArrayList<MovingObjectSprite> viewSprites = gameWindow.getGamePanel().getMovingObjectSprites(p);

            for (int i = viewSprites.size(); i < modelObjects.size(); i++) {
                MovingObject obj = modelObjects.get(i);
                gameWindow.getGamePanel().addMovingObject(
                        p,
                        modelToViewMovingObjectTypeConverter(obj.getMovingObjectType()),
                        obj.getPosition().getX(),
                        obj.getPosition().getY()
                );
            }
            
        for (int i = 0; i < Math.min(viewSprites.size(), modelObjects.size()); i++) {
            MovingObject obj = modelObjects.get(i);
            int panelWidth = SCREEN_WIDTH / numberOfPlayers;
            int localX = (numberOfPlayers == 1) ? obj.getPosition().getX() : obj.getPosition().getX() - p * panelWidth;
            viewSprites.get(i).setBounds(
                    localX,
                    obj.getPosition().getY(),
                    viewSprites.get(i).getWidth(),
                    viewSprites.get(i).getHeight()
            		);
        	}
        }
    }

 // Legge i tasti premuti e invia i comandi di movimento al modello, per ciascuna rana.
    // Giocatore 1 (indice 0, metà SINISTRA dello schermo): WASD (tasti a sinistra sulla tastiera).
    // Giocatore 2 (indice 1, metà DESTRA dello schermo, se presente): freccette (tasti a destra sulla tastiera).
    // L'abbinamento è scelto così che la posizione dei tasti sulla tastiera corrisponda
    // alla posizione del giocatore sullo schermo.
    private void moveFrogs() {
        if (numberOfPlayers == 2 && vsCPU) {
            // Contro la CPU: l'unico umano usa le freccette, la rana 2 è guidata dall'IA
            moveFrog(0, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT);
            boolean aiMoving = aiController.update();
            gameWindow.getGamePanel().getFrogSprite(1).setSpriteActions(aiMoving);
        } else if (numberOfPlayers == 2) {
            moveFrog(0, KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D);
            moveFrog(1, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT);
        } else {
            // Con 1 solo giocatore si usano le freccette, più immediate
            moveFrog(0, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT);
        }
    }
  
	// Converte la difficoltà scelta in un'etichetta leggibile per il nome della CPU
    private String difficultyLabel(Difficulty difficulty) {
        switch (difficulty) {
            case EASY: return "Facile";
            case HARD: return "Difficile";
            default: return "Medio";
        }
    }
    
    // Gestisce il movimento di una singola rana in base ai tasti assegnati.
    // Una rana che ha già vinto (tutte le tane occupate) o è morta non si muove più.
    private void moveFrog(int frogIndex, int upKey, int downKey, int leftKey, int rightKey) {
        Frog frog = gameModel.getFrogs()[frogIndex];
        if (frog.getLives() <= 0 || gameModel.hasFrogWon(frogIndex)) {
            gameWindow.getGamePanel().getFrogSprite(frogIndex).setSpriteActions(false);
            return;
        }

        boolean isMoving = false;

        if (pressedKeys.contains(upKey)) {
            gameModel.moveFrogUp(frog);
            isMoving = true;
        }
        if (pressedKeys.contains(leftKey)) {
            gameModel.moveFrogLeft(frog);
            isMoving = true;
        }
        if (pressedKeys.contains(downKey)) {
            gameModel.moveFrogDown(frog);
            isMoving = true;
        }
        if (pressedKeys.contains(rightKey)) {
            gameModel.moveFrogRight(frog);
            isMoving = true;
        }

        gameWindow.getGamePanel().getFrogSprite(frogIndex).setSpriteActions(isMoving);
    }




    // Aggiunge o rimuove lo sprite del cuore per ciascun giocatore in base allo stato del modello
    private void updateHeart() {
        for (int p = 0; p < numberOfPlayers; p++) {
            if (gameModel.isHeartSpawned(p) && gameWindow.getGamePanel().getHeartSprite(p) == null) {
                Heart heart = gameModel.getHeart(p);
                gameWindow.getGamePanel().addHeart(p, heart.getX(), heart.getY());
            } else if (!gameModel.isHeartSpawned(p) && gameWindow.getGamePanel().getHeartSprite(p) != null) {
                gameWindow.getGamePanel().removeHeart(p);
            }
        }
    }

    // Sincronizza gli sprite degli insetti nelle tane di tutti i giocatori
    private void updateInsects() {
        GamePanel panel = gameWindow.getGamePanel();
        HomeSlot[][] allSlots = gameModel.getHomeSlots();

        for (int p = 0; p < allSlots.length; p++) {
            HomeSlot[] slots = allSlots[p];
            for (int i = 0; i < slots.length; i++) {
                HomeSlot slot = slots[i];
                if (slot.hasInsect() && panel.getInsectSprite(p, i) == null) {
                    panel.addInsect(p, i, slot.getX(), slot.getY());
                } else if (!slot.hasInsect() && panel.getInsectSprite(p, i) != null) {
                    panel.removeInsect(p, i);
                }
            }
        }
    }
    
    // Aggiorna lo stato visivo (libera/occupata) di tutte le tane di tutti i giocatori
    private void updateHomeSlots() {
        GamePanel panel = gameWindow.getGamePanel();
        HomeSlot[][] allSlots = gameModel.getHomeSlots();

        for (int p = 0; p < allSlots.length; p++) {
            HomeSlot[] slots = allSlots[p];
            for (int i = 0; i < slots.length; i++) {
                panel.updateHomeSlot(p, i, slots[i].isOccupied());
            }
        }
    }

    // Mostra il pannello dei risultati con nome, esito (vinto/perso), tempo e punteggio per ciascun giocatore
    private void showGameOver() {
        Frog[] frogs = gameModel.getFrogs();

        gameWindow.getResultPanel().setFrogResultTextP1(buildResultText(0));

        if (frogs.length == 2) {
            gameWindow.getResultPanel().setFrogResultTextP2(buildResultText(1));
        } else {
            gameWindow.getResultPanel().setFrogResultTextP2("");
        }

        gameWindow.showResults();
    }


    // Costruisce la stringa di risultato per il giocatore all'indice "index":
    // nome, esito (vinto/perso), tempo di gioco e punteggio finale
    private String buildResultText(int index) {
        Frog[] frogs = gameModel.getFrogs();
        String esito = gameModel.hasFrogWon(index) ? "VINTO" : "PERSO";
        return frogs[index].getName() + ": Hai " + esito
                + " - Tempo " + gameModel.getDeath(index)
                + " - Punteggio " + gameModel.getScore(index);
    }

    // Converte Direction (modello) in SpriteDirection (vista)
    private SpriteDirection modelToViewDirectionConverter(Direction dir) {
        switch (dir) {
            case UP: return SpriteDirection.UP;
            case DOWN: return SpriteDirection.DOWN;
            case LEFT: return SpriteDirection.LEFT;
            default: return SpriteDirection.RIGHT;
        }
    }

    // Aggiorna posizione e direzione dello sprite di ciascuna rana, poi lancia l'animazione
    private void updateFrogSprites() {
        Frog[] frogs = gameModel.getFrogs();
        int panelWidth = SCREEN_WIDTH / numberOfPlayers;
        for (int i = 0; i < frogs.length; i++) {
            Frog frog = frogs[i];
            FrogSprite sprite = gameWindow.getGamePanel().getFrogSprite(i);
            if (frog == null || sprite == null) continue;

            int localX = (numberOfPlayers == 1) ? frog.getPosition().getX()
                                                 : frog.getPosition().getX() - i * panelWidth;
            sprite.setBounds(
                    localX,
                    frog.getPosition().getY(),
                    FROG_SIZE,
                    FROG_SIZE
            );
            sprite.setDirection(
                    modelToViewDirectionConverter(frog.getDirection())
            );
            sprite.performAnimation();
        }
    }

    // Aggiorna la HUD del pannello di gioco per ciascuna rana
    private void updateFrogs() {
        Frog[] frogs = gameModel.getFrogs();

        for (int i = 0; i < frogs.length; i++) {
            Frog frog = frogs[i];
            ArrayList<Integer> movingX = gameModel.getMovingObjects(i).stream()
                    .map(o -> o.getPosition().getX())
                    .collect(Collectors.toCollection(ArrayList::new));
            ArrayList<Integer> movingY = gameModel.getMovingObjects(i).stream()
                    .map(o -> o.getPosition().getY())
                    .collect(Collectors.toCollection(ArrayList::new));

            gameWindow.getGamePanel().updateGameWindow(
                    i,
                    frog.getPosition().getX(),
                    frog.getPosition().getY(),
                    modelToViewDirectionConverter(frog.getDirection()),
                    frog.getLives(),
                    frog.getMaxLives(),
                    movingX,
                    movingY,
                    gameModel.getMatchDurationHours(),
                    gameModel.getMatchDurationMinutes(),
                    gameModel.getMatchDurationSeconds(),
                    gameModel.getScore(i)
            );
        }
    }
}