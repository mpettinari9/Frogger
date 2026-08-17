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
	}
}