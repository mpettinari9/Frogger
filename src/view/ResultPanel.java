package view;

import java.awt.*;
import javax.swing.*;

public class ResultPanel extends JPanel {
	private JLabel title;
	private JLabel frogResultText;
	private JButton toTitlePanelButton; 
	
	public ResultPanel(int screenWidth, int screenHeight) {
		this.setSize(screenWidth, screenHeight);
		this.setLayout(null);
	
		this.title = new JLabel("");
		this.frogResultText = new JLabel("");
		
		this.title.setBounds(0, screenHeight/5, screenWidth, screenHeight/5);
		this.title.setFont(new Font("DIALOG", Font.BOLD, 60));
		this.title.setForeground(Color.WHITE);
		this.title.setHorizontalAlignment(JLabel.CENTER);
		
		this.frogResultText.setBackground(Color.gray);
		this.frogResultText.setHorizontalAlignment(JLabel.CENTER);
		this.frogResultText.setVerticalAlignment(JLabel.CENTER);
		this.frogResultText.setBounds(0, screenHeight*2/5, screenWidth, screenHeight/5);
		
		this.toTitlePanelButton = new JButton("RICOMINCIA");
		this.toTitlePanelButton.setBackground(Color.blue);
		this.toTitlePanelButton.setFont(new Font("DIALOG", Font.BOLD,24));
		this.toTitlePanelButton.setBounds(screenWidth/3, screenHeight*6/10, screenWidth/3, screenHeight/10);
		
		this.add(this.title);
		this.add(this.frogResultText);
		this.add(this.toTitlePanelButton);
	}
	
	public void setTitle(String titleText) {
	    this.title.setText(titleText);
	}

	public void setFrogResultText(String text) {
		this.frogResultText.setText(text);	}
	
	private void setResultText(JLabel resultText, String result) {
		resultText.setText(result);
	}
	
	public JButton getToTitlePanelButton() {
		return toTitlePanelButton;
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(new ImageIcon("src/view/Asset/sfondofrogger.png").getImage(), 0, 0, this.getWidth(), this.getHeight(), this);
	}
}