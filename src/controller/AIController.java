package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import model.Difficulty;
import model.Direction;
import model.Frog;
import model.Game;
import model.HomeSlot;
import model.MovingObject;
import model.MovingObjectType;

/* Controlla la rana della CPU al posto della tastiera.
 * Ogni tot frame (in base alla difficoltà) decide una direzione da seguire, cercando di
 * raggiungere la tana libera più vicina.
 *
 * Sulla strada, la sicurezza di una mossa non è più stimata da un "gap" istantaneo con l'ostacolo
 * più vicino: viene invece VERIFICATA simulando in avanti (auto e camion si muovono a velocità
 * costante e nota, quindi la loro posizione futura è prevedibile con esattezza) se quella mossa
 * porterebbe a uno scontro entro un breve orizzonte di tempo. Questo perché ragionare un ostacolo
 * alla volta, riassunto in un singolo numero, si è dimostrato inaffidabile in tutti i casi in cui
 * la rana ha più di una minaccia rilevante insieme (es. una corsia sopra e una sotto): un numero
 * solo non basta a distinguere una minaccia che si sta davvero avvicinando da una che sembra
 * "vicina" solo per un attimo senza esserlo davvero. */
public class AIController {
	private static final int ENTER_MARGIN = 10;  // Anticipo verticale per l'allineamento in acqua (tartaruga/tronco)
	private static final int ALIGN_TOLERANCE = 10; // Scarto massimo in X per considerarsi allineati alla tana
	private static final int MOVE_STEP = 2;       // Corrisponde a Frog.MOVE_STEP: quanti pixel avanza la rana ad ogni passo
	// Quanti frame nel futuro simulare per giudicare una mossa sicura. Deve restare breve: non
	// serve garantire che una posizione sia libera per sempre (su una strada trafficata non lo è
	// mai), solo che non porti a uno scontro nell'immediato. Un orizzonte troppo lungo farebbe
	// sembrare "avanzare" quasi sempre peggio di "restare ferma o muoversi di lato", dato che
	// avanzare a lungo attraverserebbe comunque prima o poi una corsia trafficata, mentre restare
	// ferma o spostarsi in orizzontale non tocca mai una corsia diversa da quella attuale: dato che
	// la scelta viene comunque rivalutata da capo ad ogni frame, basta vedere abbastanza vicino da
	// accorgersi in tempo di un ostacolo in arrivo, non l'intero attraversamento in un colpo solo.
	private static final int HORIZON_FRAMES = 25;

	private final Game game;
	private final int frogIndex;
	private final Difficulty difficulty;
	private final Random rnd;

	private int frameCounter;

	public AIController(Game game, int frogIndex, Difficulty difficulty) {
		this.game = game;
		this.frogIndex = frogIndex;
		this.difficulty = difficulty;
		this.rnd = game.getRandomNumberGenerator();
	}

	public boolean update() {
		// TODO Auto-generated method stub
		return false;
	}

}
