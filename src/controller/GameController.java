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
    }

}