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
	
	public TitlePanel(int screenWidth, int screenHeight) {
		this.setSize(screenWidth, screenHeight);
		this.setLayout(null);

		// --- Etichetta "Giocatori" ---
		JLabel playersLabel = new JLabel("NUMERO DI GIOCATORI:");
		playersLabel.setFont(new Font("DIALOG", Font.BOLD, 18));
		playersLabel.setForeground(GIALLO_TESTO);
		playersLabel.setHorizontalAlignment(JLabel.CENTER);
		playersLabel.setBounds(screenWidth / 3, screenHeight * 33 / 100, screenWidth / 3, screenHeight / 20);

		// --- Bottoni toggle 1 / 2 giocatori ---
		int btnW = screenWidth / 7;
		int btnH = screenHeight / 14;
		int btnY = screenHeight * 39 / 100;
		int centerX = screenWidth / 2;

		this.btn1Player = new JToggleButton("1 GIOCATORE");
		styleToggle(btn1Player, true);
		btn1Player.setBounds(centerX - btnW - 8, btnY, btnW, btnH);

		this.btn2Players = new JToggleButton("2 GIOCATORI");
		styleToggle(btn2Players, false);
		btn2Players.setBounds(centerX + 8, btnY, btnW, btnH);

		this.playerGroup = new ButtonGroup();
		playerGroup.add(btn1Player);
		playerGroup.add(btn2Players);
		btn1Player.setSelected(true);

		// Aggiorna visibilità campi nome al cambio selezione
		btn1Player.addActionListener(e -> updatePlayerFields(false));
		btn2Players.addActionListener(e -> updatePlayerFields(true));

		// --- Campo nome Giocatore 1 ---
		// Con 1 giocatore usa le freccette; con 2 giocatori usa WASD (sta nella metà sinistra dello schermo)
		this.nameLabel1 = new JLabel("NOME GIOCATORE 1 (Freccette):");
		styleLabelField(nameLabel1);
		nameLabel1.setBounds(screenWidth / 3, screenHeight * 51 / 100, screenWidth / 3, screenHeight / 20);

		this.nameField1 = new JTextField();
		styleTextField(nameField1);
		nameField1.setBounds(screenWidth / 3, screenHeight * 57 / 100, screenWidth / 3, screenHeight / 20);

		// --- Campo nome Giocatore 2 (metà destra dello schermo, usa le freccette) ---
		this.nameLabel2 = new JLabel("NOME GIOCATORE 2 (Freccette):");
		styleLabelField(nameLabel2);
		nameLabel2.setBounds(screenWidth / 3, screenHeight * 64 / 100, screenWidth / 3, screenHeight / 20);
		nameLabel2.setVisible(false);

		this.nameField2 = new JTextField();
		styleTextField(nameField2);
		nameField2.setBounds(screenWidth / 3, screenHeight * 70 / 100, screenWidth / 3, screenHeight / 20);
		nameField2.setVisible(false);
	}

	private void styleToggle(JToggleButton btn1Player2, boolean b) {
		// TODO Auto-generated method stub
		
	}

	private Object updatePlayerFields(boolean b) {
		// TODO Auto-generated method stub
		return null;
	}

	private void styleTextField(JTextField nameField22) {
		// TODO Auto-generated method stub
		
	}

	private void styleLabelField(JLabel nameLabel12) {
		// TODO Auto-generated method stub
		
	}
}
