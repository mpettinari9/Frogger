package view;

import java.awt.*;
import javax.swing.*;

import model.Difficulty;

/*
 * Pannello della schermata iniziale.
 * Permette al giocatore di inserire il proprio nome e avviare la partita.
 */
public class TitlePanel extends JPanel {
	private JButton startButton;
	private JButton tutorialButton;
	private JTextField nameField1;
	private JTextField nameField2;
	private JLabel nameLabel1;
	private JLabel nameLabel2;
	// Bottoni per selezionare 1 o 2 giocatori (sostituiscono lo slider)
	private JToggleButton btn1Player;
	private JToggleButton btn2Players;
	private ButtonGroup playerGroup;
	// Modalità "vs CPU": checkbox e selettore di difficoltà (sostituiscono il Giocatore 2 quando attivi)
	private JCheckBox chkVsCPU;
	private JLabel difficultyLabel;
	private JToggleButton btnEasy;
	private JToggleButton btnMedium;
	private JToggleButton btnHard;
	private ButtonGroup difficultyGroup;

	// Palette colori in linea con il gioco
	private static final Color BLU_SCURO   = new Color(0, 0, 139);
	private static final Color VERDE_RANA  = new Color(0, 200, 0);
	private static final Color GIALLO_TESTO = new Color(255, 220, 0);
	private static final Color BIANCO   = Color.WHITE; 
	

}
