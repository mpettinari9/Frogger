package controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;

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

    public GameController() {
        this.gameWindow = new GameWindow(SCREEN_WIDTH, SCREEN_HEIGHT);
        this.pressedKeys = new HashSet<>();
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

	private void updateHeart() {
		// TODO Auto-generated method stub
		
	}

	private void updateMovingObjects() {
		// TODO Auto-generated method stub
		
	}

	private void updateFrogSprite() {
		// TODO Auto-generated method stub
		
	}

	private void moveFrog() {
		// TODO Auto-generated method stub
		
	}

}