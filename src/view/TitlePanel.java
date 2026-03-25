package view;

import java.awt.*;
import javax.swing.*;

public class TitlePanel extends JPanel {
	private JButton startButton;
	
	public TitlePanel(int screenWidth, int screenHeight) {
		this.setSize(screenWidth, screenHeight);
		this.setLayout(null);
	
		this.startButton = new JButton("AVVIA PARTITA");
		this.startButton.setBackground(Color.green);
		this.startButton.setFont(new Font("DIALOG", Font.BOLD, 24));
		this.startButton.setBounds(screenWidth/3, screenHeight*6/10, screenWidth/3, screenHeight/10);
		this.add(this.startButton);
	}
	
	public JButton getStartButton(){
		return startButton;
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(new ImageIcon("src/view/Asset/sfondofrogger.png").getImage(), 0, 0, this.getWidth(), this.getHeight(), this);
	}
}
