package view;

import java.awt.*;
import javax.swing.*;

/*
 * Pannello dei risultati mostrato a fine partita.
 * Mostra il risultato della partita e il pulsante per ricominciare.
 */
public class ResultPanel extends JPanel {
	private JLabel frogResultTextP1; // Risultato del Giocatore 1
	private JLabel frogResultTextP2; // Risultato del Giocatore 2 (visibile solo in partite a 2 giocatori)
	private JButton toTitlePanelButton;

	// Palette colori in linea con il gioco
	private static final Color VERDE_RANA   = new Color(0, 200, 0);
	private static final Color GIALLO_TESTO = new Color(255, 220, 0);
	private static final Color BLU_BOTTONE  = new Color(30, 30, 220);
	private static final Color BIANCO       = Color.WHITE;
	private static final Color PANNELLO_BG  = new Color(0, 0, 80, 200); // blu semitrasparente

	public ResultPanel(int screenWidth, int screenHeight) {
		this.setSize(screenWidth, screenHeight);
		this.setLayout(null);

		// --- Riga risultato Giocatore 1 ---
		this.frogResultTextP1 = new JLabel("");
		this.frogResultTextP1.setForeground(VERDE_RANA);
		this.frogResultTextP1.setFont(new Font("DIALOG", Font.BOLD, 20));
		this.frogResultTextP1.setHorizontalAlignment(JLabel.CENTER);
		this.frogResultTextP1.setVerticalAlignment(JLabel.CENTER);
		// Posizionata sotto il titolo, sopra il bottone
		this.frogResultTextP1.setBounds(screenWidth / 10, screenHeight * 48 / 100, screenWidth * 8 / 10, screenHeight / 12);

		// --- Riga risultato Giocatore 2 ---
		this.frogResultTextP2 = new JLabel("");
		this.frogResultTextP2.setForeground(GIALLO_TESTO);
		this.frogResultTextP2.setFont(new Font("DIALOG", Font.BOLD, 20));
		this.frogResultTextP2.setHorizontalAlignment(JLabel.CENTER);
		this.frogResultTextP2.setVerticalAlignment(JLabel.CENTER);
		// Sotto la riga del giocatore 1, ancora sopra il bottone
		this.frogResultTextP2.setBounds(screenWidth / 10, screenHeight * 57 / 100, screenWidth * 8 / 10, screenHeight / 12);
		this.frogResultTextP2.setVisible(false);
	    
		
		// --- Bottone Ricomincia: in fondo, non si sovrappone alle label ---
		this.toTitlePanelButton = new JButton("RICOMINCIA");
		this.toTitlePanelButton.setBackground(BLU_BOTTONE);
		this.toTitlePanelButton.setForeground(BIANCO);
		this.toTitlePanelButton.setFont(new Font("DIALOG", Font.BOLD, 24));
		this.toTitlePanelButton.setFocusPainted(false);
		this.toTitlePanelButton.setBorderPainted(false);
		this.toTitlePanelButton.setBounds(screenWidth / 3, screenHeight * 72 / 100, screenWidth / 3, screenHeight / 10);

		this.add(this.frogResultTextP1);
		this.add(this.frogResultTextP2);
		this.add(this.toTitlePanelButton);
	}

			

	// Imposta il risultato del Giocatore 1
	public void setFrogResultTextP1(String text) {
		this.frogResultTextP1.setText(text);
	}

	// Imposta il risultato del Giocatore 2; nasconde la riga se vuota (partita a 1 giocatore)
	public void setFrogResultTextP2(String text) {
		this.frogResultTextP2.setText(text);
		this.frogResultTextP2.setVisible(!text.isEmpty());
	}

	public JButton getToTitlePanelButton() {
		return toTitlePanelButton;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		// Sfondo immagine
		g.drawImage(new ImageIcon("src/view/Asset/sfondofrogger.png").getImage(), 0, 0, this.getWidth(), this.getHeight(), this);
		// Rettangolo semitrasparente dietro le info per migliorare la leggibilità
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(PANNELLO_BG);
		g2.fillRoundRect(screenWidth() / 10, screenHeight() * 42 / 100,
			screenWidth() * 8 / 10, screenHeight() * 36 / 100, 20, 20);
	}

	// Helper per leggere dimensioni attuali del pannello
	private int screenWidth()  { return getWidth();  }
	private int screenHeight() { return getHeight(); }
}
