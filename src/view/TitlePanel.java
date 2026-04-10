package view;

import java.awt.*;
import javax.swing.*;

public class TitlePanel extends JPanel {
	private JButton startButton;
	private JButton tutorialButton;
	private JTextField nameField;
	private JLabel nameLabel;
	
	public TitlePanel(int screenWidth, int screenHeight) {
		this.setSize(screenWidth, screenHeight);
		this.setLayout(null);
	
		this.startButton = new JButton("AVVIA PARTITA");
		this.startButton.setBackground(Color.green);
		this.startButton.setFont(new Font("DIALOG", Font.BOLD, 24));
		this.startButton.setBounds(screenWidth/3, screenHeight*6/10, screenWidth/3, screenHeight/10);
		this.add(this.startButton);
		
        this.nameLabel = new JLabel("Inserisci il tuo nome:");
        this.nameLabel.setFont(new Font("DIALOG", Font.BOLD, 24));
        this.nameLabel.setHorizontalAlignment(JLabel.CENTER);
        this.nameLabel.setBounds(screenWidth/3, screenHeight*4/10, screenWidth/3, screenHeight/10);
      	
		this.nameField = new JTextField();
		this.nameField.setBackground(Color.green);
		this.nameField.setHorizontalAlignment(JTextField.CENTER);
		this.nameField.setFont(new Font("DIALOG", Font.BOLD, 24));
		this.nameField.setBounds(screenHeight/3, screenHeight*5/10, screenWidth/3, screenHeight/10);
		
		this.tutorialButton = new JButton("TUTORIAL");
		this.tutorialButton.setBackground(Color.BLUE);
		this.tutorialButton.setFont(new Font("DIALOG", Font.BOLD, 24));
		this.tutorialButton.setBounds(screenHeight/3, screenHeight*8/10, screenWidth/3, screenHeight/10);
		
		this.add(nameLabel);
		this.add(nameField);
		this.add(tutorialButton);
	}
	
	public JButton getStartButton(){
		return startButton;
	}
	
	public JButton getTutorialButton() {
		return tutorialButton;
	}

	public JTextField getNameField() {
		return nameField;
	}
	
	public String getFrogName() {
        return nameField.getText().trim();
    }
	
	public JLabel getNameLabel() {
		return nameLabel;
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(new ImageIcon("src/view/Asset/sfondofrogger.png").getImage(), 0, 0, this.getWidth(), this.getHeight(), this);
	}
}
