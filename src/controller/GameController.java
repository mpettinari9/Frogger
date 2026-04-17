package controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.ArrayList;

import model.*;
import view.*;

public class GameController implements KeyListener, Runnable {
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
    private int startX = SCREEN_WIDTH / 2 - FROG_SIZE / 2;
    private int startY= (SCREEN_HEIGHT * 82 / 100) - FROG_SIZE / 2;    

    public GameController() {
        this.gameWindow = new GameWindow(SCREEN_WIDTH, SCREEN_HEIGHT);
        this.pressedKeys = new HashSet<>();
        
        this.gameWindow.addKeyListener(this);
        this.gameWindow.setFocusable(true);
        this.gameWindow.requestFocusInWindow();

        this.gameWindow.getTitlePanel().getStartButton().addActionListener(e -> startGame());

        this.gameWindow.getTitlePanel().getTutorialButton().addActionListener(e -> {
            gameWindow.showTutorial();
        });

        this.gameWindow.getTutorialPanel().getBackButton().addActionListener(e -> {
            gameWindow.showTitle();
        });
        
        this.gameWindow.getResultPanel().getToTitlePanelButton().addActionListener(e -> {
            gameRunning = false;
            gameModel = null;
            pressedKeys.clear();
            
            gameWindow.showTitle();
        });
    
    }

    private void startGame() {
        String frogName = gameWindow.getTitlePanel().getFrogName();
        
        if (frogName.isEmpty()) {
            frogName = "Frog";
        }
        
        Map map = new Map(SCREEN_WIDTH, SCREEN_HEIGHT, RIVER_TOP, RIVER_BOTTOM);
        gameModel = new Game(map);

        Frog frog = new Frog(frogName, Direction.UP, new Size(FROG_SIZE, FROG_SIZE),
                             new Position(startX, startY), map, 2);
        gameModel.setFrog(frog);

        GamePanel gamePanel = new GamePanel(SCREEN_WIDTH, SCREEN_HEIGHT, frog.getName(),
                                            frog.getLives(), frog.getMaxLives());
        gamePanel.setFrogSprite(startX, startY);
        gamePanel.addKeyListener(this);
        gamePanel.setFocusable(true);

        gameWindow.setGamePanel(gamePanel);
        gameWindow.showGame();
        gamePanel.requestFocusInWindow();

        if (gameThread == null || !gameThread.isAlive()) {
            gameThread = new Thread(this);
            gameThread.start();
        }
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        pressedKeys.add(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        pressedKeys.remove(e.getKeyCode());
    }

    @Override
    public void run() {
        long drawInterval = 1000 / FPS;
        gameRunning = true;
        
        while (gameRunning) {
            long startTime = System.currentTimeMillis();

            if (gameModel != null) {
                 moveFrog();
                
               // gameModel.update();
                
                updateFrogSprite();
                updateMovingObjects();
                updateHeart();
                updateFrog();

//                if (gameModel.checkGameOver()) {
//                    showGameOver();
//                    break;
//                }
            }

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

	private void updateFrog() {
		// TODO Auto-generated method stub
		
	}

    private MovingObjectTypeSprite modelToViewMovingObjectTypeConverter(MovingObjectType type) {
        switch (type) {
            case CAR: return MovingObjectTypeSprite.CAR;
            case TRUCK: return MovingObjectTypeSprite.TRUCK;
            case TRUNK: return MovingObjectTypeSprite.TRUNK;
            case TURTLE: return MovingObjectTypeSprite.TURTLE;
            default: return MovingObjectTypeSprite.CAR;
        }
    }
    
	private void updateMovingObjects() {
        ArrayList<MovingObject> modelObjects = gameModel.getMovingObjects();
        ArrayList<MovingObjectSprite> viewSprites = gameWindow.getGamePanel().getMovingObjectSprites();

        // Aggiungo nuovi oggetti usando coordinate dall'angolo
        for (int i = viewSprites.size(); i < modelObjects.size(); i++) {
            MovingObject obj = modelObjects.get(i);
            gameWindow.getGamePanel().addMovingObject(
                modelToViewMovingObjectTypeConverter(obj.getMovingObjectType()),
                obj.getPosition().getX(), 
                obj.getPosition().getY()
            );
            viewSprites.get(i).setSpriteActions(true);
        }

        // Aggiorno posizioni usando coordinate dall'angolo
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

	private void updateFrogSprite() {
		// TODO Auto-generated method stub
		
	}

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
    
    private void updateHeart() {
        if (gameModel.isEarnLifeSpawned() && gameWindow.getGamePanel().getHeartSprite() == null) {
            EarnLife heart = gameModel.getEarnLife();
            gameWindow.getGamePanel().addHeart(heart.getX(), heart.getY());
        } else if (!gameModel.isEarnLifeSpawned() && gameWindow.getGamePanel().getHeartSprite() != null) {
            gameWindow.getGamePanel().removeHeart();
        }
    }

}