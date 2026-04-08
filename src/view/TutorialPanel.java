package view;

import java.awt.*;
import javax.swing.*;

public class TutorialPanel extends JPanel {
    private JButton backButton;
    
    public TutorialPanel(int screenWidth, int screenHeight) {
        this.setSize(screenWidth, screenHeight);
        this.setLayout(null);
        
        // Pulsante per tornare indietro
        this.backButton = new JButton("GO BACK");
        this.backButton.setFont(new Font("DIALOG", Font.BOLD, 24));
        this.backButton.setBounds(screenWidth/3, screenHeight*85/100, 
                                  screenWidth/3, screenHeight/10);
        
        this.add(this.backButton);
    }
    
    public JButton getBackButton() {
        return backButton;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(new ImageIcon("src/view/Asset/TutorialFrog.png").getImage(), 0, 0, this.getWidth(), this.getHeight(), this);
    }
}
