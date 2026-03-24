package view;

import javax.swing.JFrame;


public class GameWindow extends JFrame{
	
	public GameWindow(int width, int height) {
		this.setSize(width, height);
		this.setTitle("Frogger");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setLayout(null);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	
}