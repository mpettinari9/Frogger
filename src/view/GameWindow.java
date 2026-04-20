package view;

import javax.swing.JFrame;

import model.Frog;


public class GameWindow extends JFrame{
	private TitlePanel titlePanel; /* Title Screen */
	private GamePanel gamePanel; /* Schermata di gioco */
	private ResultPanel resultPanel; /* Schermata dei risultati */
	private TutorialPanel tutorialPanel; 
	
	
	public GameWindow(int width, int height) {
		this.setSize(width, height);
		this.setTitle("Frogger");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setLayout(null);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		
		this.titlePanel = new TitlePanel(width, height);
		this.resultPanel = new ResultPanel(width, height);
		this.tutorialPanel = new TutorialPanel(width, height);  
		
		this.add(this.titlePanel);
		
		this.setVisible(true);
		 this.gamePanel = new GamePanel(width,
		            height,
		            "Frog",
		            Frog.DEFAULT_LIVES,
		            Frog.DEFAULT_LIVES);
	      
		 this.gamePanel.setVisible(false);
	        this.add(gamePanel);

	        this.setVisible(true);

	}
	public void showTitle() {
	    this.getContentPane().removeAll();
	    this.add(titlePanel);
	    this.refreshPanel();
	}

	public void showGame() {
	    this.getContentPane().removeAll();
	    this.add(gamePanel);
	    this.refreshPanel();
	    
	    if (gamePanel != null) {
	        gamePanel.requestFocusInWindow();
	    }
	}

	public void showResults() {
	    this.getContentPane().removeAll();
	    this.add(resultPanel);
	    this.refreshPanel();
	}
	
	public void showTutorial() {
        this.getContentPane().removeAll();
        this.add(tutorialPanel);
        this.refreshPanel();
    }
	
	public void refreshPanel() {
    	this.revalidate();
        this.repaint();
    }
	
	/* metodi getter della classe "GameWindow" */
	public TitlePanel getTitlePanel() {
		return this.titlePanel;
	}
	
	public GamePanel getGamePanel() {
		return this.gamePanel;
	}
	
	public void setGamePanel(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
	}
	
	public ResultPanel getResultPanel() {
		return this.resultPanel;
	}
	
	public TutorialPanel getTutorialPanel() {
        return this.tutorialPanel;
    }

}