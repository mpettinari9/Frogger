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

    // Inizializza modello, vista e thread di gioco con il nome inserito dal giocatore
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
        gameModel = new Game(map);

        // Crea rana con posizione e dimensioni iniziali 
        Frog frog = new Frog(frogName, Direction.UP, new Size(FROG_SIZE, FROG_SIZE),
                             new Position(startX, startY), map, 2);
        gameModel.setFrog(frog);
 
        // Crea pannello di gioco e collega listener tastiera
        GamePanel gamePanel = new GamePanel(SCREEN_WIDTH, SCREEN_HEIGHT, frog.getName(),
                                            frog.getLives(), frog.getMaxLives());
        gamePanel.setFrogSprite(startX, startY);
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
                 moveFrog(); // Legge input e muove rana
                
                gameModel.update(); // Aggiorna stato del modello (oggetti, collisioni, cuore)
                
                // Sincronizza la vista con il modello
                updateFrogSprite();
                updateMovingObjects();
                updateHeart();
                updateFrog();

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
        ArrayList<MovingObject> modelObjects = gameModel.getMovingObjects();
        ArrayList<MovingObjectSprite> viewSprites = gameWindow.getGamePanel().getMovingObjectSprites();

        // Aggiunge sprite per oggetti non ancora presenti nella vista
        for (int i = viewSprites.size(); i < modelObjects.size(); i++) {
            MovingObject obj = modelObjects.get(i);
            gameWindow.getGamePanel().addMovingObject(
                modelToViewMovingObjectTypeConverter(obj.getMovingObjectType()),
                obj.getPosition().getX(), 
                obj.getPosition().getY()
            );
            viewSprites.get(i).setSpriteActions(true);
        }

        // Aggiorna posizione di ogni sprite esistente
        for (int i = 0; i < Math.min(viewSprites.size(), modelObjects.size()); i++) {
            MovingObject obj = modelObjects.get(i);
            viewSprites.get(i).setBounds(
                obj.getPosition().getX(),
                obj.getPosition().getY(),
                viewSprites.get(i).getWidth(),
                viewSprites.get(i).getHeight()
            );
        }
    }

    // Legge i tasti premuti e invia i comandi di movimento al modello.
    // Disattiva lo sprite se la rana è morta.
    private void moveFrog() {
        Frog frog = gameModel.getFrog();
        if (frog.getLives() <= 0) {
            gameWindow.getGamePanel().getFrogSprite().setSpriteActions(false);
            return;
        }

        boolean isMoving = false;
        
        if (pressedKeys.contains(KeyEvent.VK_UP)) {
            gameModel.moveFrogUp();
            isMoving = true;
        } 
        if (pressedKeys.contains(KeyEvent.VK_LEFT)) {
            gameModel.moveFrogLeft();
            isMoving = true;
        } 
        if (pressedKeys.contains(KeyEvent.VK_DOWN)) {
            gameModel.moveFrogDown();
            isMoving = true;
        } 
        if (pressedKeys.contains(KeyEvent.VK_RIGHT)) {
            gameModel.moveFrogRight();
            isMoving = true;
        }
        
        gameWindow.getGamePanel().getFrogSprite().setSpriteActions(isMoving);
    }
    
    // Aggiunge o rimuove lo sprite del cuore in base allo stato del modello
    private void updateHeart() {
        if (gameModel.isHeartSpawned() && gameWindow.getGamePanel().getHeartSprite() == null) {
            Heart heart = gameModel.getHeart();
            gameWindow.getGamePanel().addHeart(heart.getX(), heart.getY());
        } else if (!gameModel.isHeartSpawned() && gameWindow.getGamePanel().getHeartSprite() != null) {
            gameWindow.getGamePanel().removeHeart();
        }
    }
    
    // Mostra il pannello dei risultati con nome rana e tempo di partita
    private void showGameOver() {
        Frog frog = gameModel.getFrog();
        gameWindow.getResultPanel().setFrogResultText(
            frog.getName() + ": Tempo " + gameModel.getDeath()
        );
        gameWindow.showResults();
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
    
    // Aggiorna posizione e direzione dello sprite della rana, poi lancia l'animazione
    private void updateFrogSprite() {
        Frog frog = gameModel.getFrog();
        if (frog == null || gameWindow.getGamePanel().getFrogSprite() == null) 
            return;

        gameWindow.getGamePanel().getFrogSprite().setBounds(
            frog.getPosition().getX(), 
            frog.getPosition().getY(), 
            FROG_SIZE, 
            FROG_SIZE
        );
        gameWindow.getGamePanel().getFrogSprite().setDirection(
            modelToViewDirectionConverter(frog.getDirection())
        );
        gameWindow.getGamePanel().getFrogSprite().performAnimation();
    }

    // Aggiorna la HUD del pannello di gioco: posizione rana, vite, posizioni oggetti e timer
    private void updateFrog() {
        Frog frog = gameModel.getFrog();
        gameWindow.getGamePanel().updateGameWindow(
            frog.getPosition().getX(),
            frog.getPosition().getY(),
            modelToViewDirectionConverter(frog.getDirection()),
            frog.getLives(),
            frog.getMaxLives(),
            gameModel.getMovingObjects().stream()
                .map(o -> o.getPosition().getX())
                .collect(Collectors.toCollection(ArrayList::new)),
            gameModel.getMovingObjects().stream()
                .map(o -> o.getPosition().getY())
                .collect(Collectors.toCollection(ArrayList::new)),
            gameModel.getMatchDurationHours(),
            gameModel.getMatchDurationMinutes(),
            gameModel.getMatchDurationSeconds()
        );
    }
}