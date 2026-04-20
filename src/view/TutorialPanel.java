package view;

import java.awt.*;
import javax.swing.*;

public class TutorialPanel extends JPanel {
    private JButton backButton;
    private ImageIcon tutorialImage;
    
    public TutorialPanel(int screenWidth, int screenHeight) {
        this.setSize(screenWidth, screenHeight);
        this.setLayout(null);
        
        this.tutorialImage = new ImageIcon("src/view/Asset/TutorialFrog.png");
        
        // Pulsante per tornare indietro
        this.backButton = new JButton("GO BACK");
        this.backButton.setBackground(Color.RED);
        this.backButton.setForeground(Color.WHITE);
        this.backButton.setFont(new Font("DIALOG", Font.BOLD, 24));
        this.backButton.setBounds(screenWidth/3, screenHeight*13/15, 
                                  screenWidth/3, screenHeight/15);
        
        this.add(this.backButton);
    }
    
    public JButton getBackButton() {
        return backButton;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
         if (tutorialImage != null) {
            g.drawImage(tutorialImage.getImage(), 0, 0, 
                       this.getWidth(), this.getHeight(), this);
        }
    }
}
