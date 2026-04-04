package view;

import java.awt.*;
import javax.swing.*;

public class ResultPanel extends JPanel {
	private JLabel frogResultLabel;
	private JButton toTitlePanelButton; // da implementare!!!!
	
	public ResultPanel(int screenWidth, int screenHeight) {
		this.setSize(screenWidth, screenHeight);
		this.setLayout(null);
		this.setBackground(Color.black);
	
		this.frogResultLabel = new JLabel("");
		this.frogResultLabel.setForeground(Color.white);
		this.frogResultLabel.setFont(new Font("DIALOG",Font.BOLD, 25));
		this.frogResultLabel.setHorizontalAlignment(SwingConstants.CENTER);
		this.frogResultLabel.setBounds(0, screenHeight/3, screenWidth, 50);
		this.add(frogResultLabel);
		
		this.toTitlePanelButton = new JButton("TORNA AL MENU'");
		this.toTitlePanelButton.setFont(new Font("DIALOG", Font.BOLD, 24));
		this.toTitlePanelButton.setBounds(screenWidth / 3, screenHeight * 8/10, screenWidth /3, screenHeight / 10);
		this.add(toTitlePanelButton);
		
	}
	public JButton getToTitlePanelButton() {
		return toTitlePanelButton;
	}

	public void setFrogResultText(String text) {
		this.frogResultLabel.setText(text);	}
	
	

}
