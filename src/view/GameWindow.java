package view;

import javax.swing.JFrame;

import model.Frog;

/*
 * Finestra principale del gioco.
 * Gestisce la navigazione tra i pannelli: titolo, gioco, risultati e tutorial.
 */
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
		
		
		this.titlePanel = new TitlePanel(width, height);
		this.resultPanel = new ResultPanel(width, height);
		this.tutorialPanel = new TutorialPanel(width, height);  
		
		this.add(this.titlePanel);

		this.setVisible(true);
		 this.gamePanel = new GamePanel(width,
		            height,
		            new String[] {"Frog"},
		            new int[] {Frog.DEFAULT_LIVES},
		            new int[] {Frog.DEFAULT_LIVES});
	      
		 this.gamePanel.setVisible(false);
	        this.add(gamePanel);

	        this.setVisible(true);

	}
	
	//  Mostra il pannello del titolo.
	public void showTitle() {
	    this.getContentPane().removeAll();
	    this.add(titlePanel);
	    this.refreshPanel();
	}

	// Mostra il pannello di gioco e richiede il focus per i tasti.
	public void showGame() {
	    this.getContentPane().removeAll();
	    this.add(gamePanel);
	    this.refreshPanel();
	    
	    if (gamePanel != null) {
	        gamePanel.requestFocusInWindow();
	    }
	}

	// Mostra il pannello dei risultati.
	public void showResults() {
	    this.getContentPane().removeAll();
	    this.add(resultPanel);
	    this.refreshPanel();
	}
	
	// Mostra il pannello del tutorial.
	public void showTutorial() {
        this.getContentPane().removeAll();
        this.add(tutorialPanel);
        this.refreshPanel();
    }
	
	//  Aggiorna la finestra rivalidando e ridisegnando il contenuto.
	public void refreshPanel() {
    	this.revalidate();
        this.repaint();
    }
	
	// metodi getter e setter
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