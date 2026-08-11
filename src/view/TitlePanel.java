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
		// --- Checkbox "gioca contro la CPU" (visibile solo con 2 giocatori) ---
		this.chkVsCPU = new JCheckBox("GIOCA CONTRO IL COMPUTER");
		chkVsCPU.setFont(new Font("DIALOG", Font.BOLD, 16));
		chkVsCPU.setForeground(GIALLO_TESTO);
		chkVsCPU.setHorizontalAlignment(SwingConstants.CENTER);
		chkVsCPU.setOpaque(false);
		chkVsCPU.setFocusPainted(false);
		chkVsCPU.setBounds(screenWidth / 3, screenHeight * 45 / 100, screenWidth / 3, screenHeight / 20);
		chkVsCPU.setVisible(false);
		chkVsCPU.addActionListener(e -> updateVsCpuFields());

		// --- Selettore difficoltà CPU (sostituisce nome e campo del Giocatore 2 quando la CPU è attiva) ---
		this.difficultyLabel = new JLabel("DIFFICOLTÀ CPU:");
		styleLabelField(difficultyLabel);
		difficultyLabel.setBounds(screenWidth / 3, screenHeight * 64 / 100, screenWidth / 3, screenHeight / 20);
		difficultyLabel.setVisible(false);

		int diffBtnW = screenWidth / 10;
		int diffBtnY = screenHeight * 70 / 100;
		int diffStartX = screenWidth / 3;

		this.btnEasy = new JToggleButton("FACILE");
		this.btnMedium = new JToggleButton("MEDIO");
		this.btnHard = new JToggleButton("DIFFICILE");
		styleToggle(btnEasy, false);
		styleToggle(btnMedium, true);
		styleToggle(btnHard, false);
		btnEasy.setBounds(diffStartX, diffBtnY, diffBtnW, screenHeight / 20);
		btnMedium.setBounds(diffStartX + diffBtnW + 6, diffBtnY, diffBtnW, screenHeight / 20);
		btnHard.setBounds(diffStartX + 2 * (diffBtnW + 6), diffBtnY, diffBtnW, screenHeight / 20);
		btnEasy.setVisible(false);
		btnMedium.setVisible(false);
		btnHard.setVisible(false);

		this.difficultyGroup = new ButtonGroup();
		difficultyGroup.add(btnEasy);
		difficultyGroup.add(btnMedium);
		difficultyGroup.add(btnHard);
		btnMedium.setSelected(true);

		// --- Bottone Tutorial ---
		this.tutorialButton = new JButton("TUTORIAL");
		this.tutorialButton.setBackground(Color.BLUE);
		this.tutorialButton.setForeground(BIANCO);
		this.tutorialButton.setFont(new Font("DIALOG", Font.BOLD, 22));
		this.tutorialButton.setFocusPainted(false);
		this.tutorialButton.setBorderPainted(false);
		this.tutorialButton.setBounds(screenWidth / 3, screenHeight * 78 / 100, screenWidth / 3, screenHeight / 11);

		// --- Bottone Avvia ---
		this.startButton = new JButton("AVVIA PARTITA");
		this.startButton.setBackground(VERDE_RANA);
		this.startButton.setForeground(Color.BLACK);
		this.startButton.setFont(new Font("DIALOG", Font.BOLD, 22));
		this.startButton.setFocusPainted(false);
		this.startButton.setBorderPainted(false);
		this.startButton.setBounds(screenWidth / 3, screenHeight * 88 / 100, screenWidth / 3, screenHeight / 11);

		this.add(playersLabel);
		this.add(btn1Player);
		this.add(btn2Players);
		this.add(this.nameLabel1);
		this.add(this.nameField1);
		this.add(this.nameLabel2);
		this.add(this.nameField2);
		this.add(this.chkVsCPU);
		this.add(this.difficultyLabel);
		this.add(this.btnEasy);
		this.add(this.btnMedium);
		this.add(this.btnHard);
		this.add(this.tutorialButton);
		this.add(this.startButton);
	}
		
	private Object updateVsCpuFields() {
		// TODO Auto-generated method stub
		return null;
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
	
	// Metodi getter e setter
	public JButton getStartButton() {
		return startButton;
	}

	public JButton getTutorialButton() {
		return tutorialButton;
	}

	// Restituisce il numero di giocatori scelto (1 o 2)
	public int getNumberOfPlayers() {
		return btn2Players.isSelected() ? 2 : 1;
	}

	// Compatibilità con eventuali listener esistenti sul vecchio slider
	public JSlider getNumberOfPlayersSelector() {
		return null;
	}

	// Indica se la partita a 2 giocatori è contro la CPU invece che contro un secondo umano
	public boolean isVsCPU() {
		return btn2Players.isSelected() && chkVsCPU.isSelected();
	}

	// Restituisce la difficoltà scelta per la CPU
	public Difficulty getDifficulty() {
		if (btnEasy.isSelected()) return Difficulty.EASY;
		if (btnHard.isSelected()) return Difficulty.HARD;
		return Difficulty.MEDIUM;
	}

	// Restituisce il campo di testo del nome per il giocatore all'indice "index" (0 o 1)
	public JTextField getNameField(int index) {
		return (index == 0) ? nameField1 : nameField2;
	}

	// Restituisce il nome inserito per il giocatore all'indice "index" (0 o 1)
	public String getFrogName(int index) {
		return (index == 0) ? nameField1.getText().trim() : nameField2.getText().trim();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(new ImageIcon("src/view/Asset/sfondofrogger.png").getImage(), 0, 0, this.getWidth(), this.getHeight(), this);
	}
}
