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

	// Chiamato una volta per frame dal game loop.
		// Restituisce true se la rana si sta muovendo (usato dalla vista per l'animazione).
		public boolean update() {
			Frog frog = game.getFrogs()[frogIndex];
			if (frog == null || frog.isDead() || game.hasFrogWon(frogIndex)) {
				return false;
			}

			// L'errore casuale della difficoltà resta scandito dal tempo di reazione: rappresenta
			// un'esitazione strategica occasionale, non va rivalutato ad ogni frame. La sicurezza vera
			// e propria (sotto) invece è sempre rivalutata ad ogni frame, altrimenti la si scoprirebbe
			// troppo tardi.
			frameCounter++;
			boolean mistakeNow = false;
			if (frameCounter >= difficulty.getReactionInterval()) {
				frameCounter = 0;
				mistakeNow = rnd.nextInt(100) < difficulty.getMistakePercent();
			}

			Direction direction = mistakeNow ? randomDirection()
					: frog.isInWaterArea() ? findWaterDirection(frog) : findRoadDirection(frog);

			applyMove(frog, direction);
			return direction != null;
		}

		// --- Strada ---

		// Sceglie la mossa da fare sulla strada: parte dalla mossa "ideale" per avvicinarsi alla tana
		// (allinearsi in orizzontale, poi avanzare) e la scarta solo se la simulazione mostra che
		// porterebbe a passare del tempo in territorio pericoloso. Prova quindi le alternative in
		// ordine di preferenza; se nessuna è completamente libera da rischi, sceglie quella che ne
		// accumula di meno (nessuna lo è mai del tutto per sempre in una corsia trafficata: l'obiettivo
		// qui è la scelta migliore nell'immediato, non una garanzia assoluta).
		private Direction findRoadDirection(Frog frog) {
			List<int[]> threats = nearbyThreats(frog);
			Direction[] candidates = buildCandidates(desiredDirection(frog));

			Direction best = null;
			int bestDanger = Integer.MAX_VALUE;
			for (Direction candidate : candidates) {
				int danger = countDangerSteps(frog, candidate, threats);
				if (danger == 0) {
					return candidate;
				}
				if (danger < bestDanger) {
					bestDanger = danger;
					best = candidate;
				}
			}
			return best;
		}
		
		// Ordine dei tentativi: prima la mossa desiderata, poi restare ferma, infine scivolare di
		// lato. Restare ferma viene prima dello scivolamento laterale perché, mosse in orizzontale
		// che non toccano la corsia attuale, risultano quasi sempre "sicure per tutto l'orizzonte"
		// anche quando non serve muoversi affatto: se venissero provate prima, la rana vagherebbe
		// di lato ogni volta che avanzare non è *subito* possibile, invece di aspettare il momento
		// giusto. Lo scivolamento laterale resta comunque disponibile come ultima risorsa, per i casi
		// in cui anche restare ferma non è sicuro (schiacciata da una minaccia sulla propria corsia).
		// Retrocedere (DOWN) non è mai un'opzione.
		private Direction[] buildCandidates(Direction desired) {
			if (desired == Direction.UP) {
				return new Direction[] { Direction.UP, null, Direction.LEFT, Direction.RIGHT };
			}
			Direction other = (desired == Direction.LEFT) ? Direction.RIGHT : Direction.LEFT;
			return new Direction[] { desired, Direction.UP, null, other };
		}
		
		// Simula la mossa data per "horizon" frame, muovendo la rana passo passo e le minacce alla
		// loro velocità nota, e conta per quanti di quei passi la rana si troverebbe in territorio
		// pericoloso (sovrapposta, col margine della difficoltà, a un'auto/camion). Conta l'intero
		// orizzonte invece di fermarsi al primo passo pericoloso: se la rana è già a ridosso di una
		// minaccia, il primo passo può risultare "in pericolo" per QUALSIASI mossa candidata (non si
		// è ancora avuto il tempo di divergere), e fermarsi lì renderebbe il confronto tra le mosse
		// arbitrario invece che informativo. Guardando l'intero orizzonte, una mossa che si allontana
		// davvero accumula molti meno passi pericolosi di una che resta bloccata al fianco della
		// minaccia, anche se il primissimo passo sembra ugualmente rischioso per entrambe.
		private int countDangerSteps(Frog frog, Direction move, List<int[]> threats) {
			if (threats.isEmpty()) {
				return 0;
			}
			int fx = frog.getX();
			int fy = frog.getY();
			int fw = frog.getWidth();
			int fh = frog.getHeight();
			int minX = frog.getMinX();
			int maxX = frog.getMaxX();
			int danger = 0;

			for (int step = 1; step <= HORIZON_FRAMES; step++) {
				if (move == Direction.UP) {
					fy -= MOVE_STEP;
				} else if (move == Direction.LEFT) {
					fx = Math.max(minX, fx - MOVE_STEP);
				} else if (move == Direction.RIGHT) {
					fx = Math.min(maxX, fx + MOVE_STEP);
				}
				// null (restare ferma): nessuno spostamento.

				for (int[] threat : threats) {
					int oy = threat[1];
					int ow = threat[2];
					int oh = threat[3];
					int vx = threat[4];
					int ox = threat[0] + vx * step;
					// Margine di sicurezza orizzontale della difficoltà: più è cauta, più "estende" il
					// bordo d'attacco dell'ostacolo (quello nella direzione in cui si muove), reagendo
					// quando l'ostacolo è ancora a distanza di sicurezza invece che al contatto vero e
					// proprio. Solo il bordo d'attacco conta: quello opposto non si sta avvicinando.
					int pad = difficulty.getSafeGap();
					if (vx > 0) {
						ow += pad; // si muove verso destra: estende il bordo destro
					} else {
						ox -= pad; // si muove verso sinistra: estende il bordo sinistro
						ow += pad;
					}
					if (fx < ox + ow && fx + fw > ox && fy < oy + oh && fy + fh > oy) {
						danger++;
						break; // un ostacolo alla volta basta per contare questo passo come pericoloso
					}
				}
			}
			return danger;
		}

		// La mossa che la rana vorrebbe fare per raggiungere la tana, ignorando del tutto la
		// sicurezza (quella la verifica solo findRoadDirection): si allinea in orizzontale alla tana
		// libera più vicina, poi avanza.
		private Direction desiredDirection(Frog frog) {
			HomeSlot target = nearestFreeSlot(frog);
			if (target == null) {
				return Direction.UP;
			}
			int targetCenterX = target.getX() + HomeSlot.SLOT_WIDTH / 2;
			if (frog.getCenterX() < targetCenterX - ALIGN_TOLERANCE) {
				return Direction.RIGHT;
			}
			if (frog.getCenterX() > targetCenterX + ALIGN_TOLERANCE) {
				return Direction.LEFT;
			}
			return Direction.UP;
		}

		private HomeSlot nearestFreeSlot(Frog frog) {
			HomeSlot target = null;
			int nearestDistance = Integer.MAX_VALUE;
			for (HomeSlot slot : game.getHomeSlots(frogIndex)) {
				if (slot.isOccupied()) {
					continue;
				}
				int distance = Math.abs(slot.getX() - frog.getX());
				if (distance < nearestDistance) {
					target = slot;
					nearestDistance = distance;
				}
			}
			return target;
		}

		// Istantanea di auto/camion presenti (posizione, dimensione, velocità con segno): calcolata
		// una sola volta per frame e riusata per tutte le mosse candidate, invece di rileggere lo
		// stato del gioco ad ogni simulazione.
		private List<int[]> nearbyThreats(Frog frog) {
			List<int[]> threats = new ArrayList<>();
			for (MovingObject obj : game.getMovingObjects(frogIndex)) {
				MovingObjectType type = obj.getMovingObjectType();
				if (type != MovingObjectType.CAR && type != MovingObjectType.TRUCK) {
					continue;
				}
				int vx = (obj.getDirection() == Direction.RIGHT) ? obj.getSpeed() : -obj.getSpeed();
				threats.add(new int[] { obj.getPosition().getX(), obj.getPosition().getY(),
						obj.getSize().getWidth(), obj.getSize().getHeight(), vx });
			}
			return threats;
		}
		

		// --- Acqua ---

		// Restare a galla non è mai lasciato al caso: un errore qui significa annegare all'istante.
		private Direction findWaterDirection(Frog frog) {
			if (!isOnPlatform(frog)) {
				return findSwimDirection(frog);
			}
			// Sopra una zattera l'allineamento orizzontale con la tana non conta: la corrente la
			// sposta comunque, quindi cercare di correggerlo significherebbe solo remare contro la
			// corrente all'infinito, restando bloccata invece di avanzare. Meglio continuare su.
			return Direction.UP;
		}

		// Verifica se la rana è attualmente sopra una tartaruga o un tronco
		private boolean isOnPlatform(Frog frog) {
			for (MovingObject obj : game.getMovingObjects(frogIndex)) {
				MovingObjectType type = obj.getMovingObjectType();
				if ((type == MovingObjectType.TURTLE || type == MovingObjectType.TRUNK)
						&& frog.getHitBox().intersects(obj.getHitBox())) {
					return true;
				}
			}
			return false;
		}

		// Cerca la tartaruga/tronco più vicino nella stessa corsia e si dirige verso di esso
		private Direction findSwimDirection(Frog frog) {
			MovingObject nearest = null;
			int nearestDistance = Integer.MAX_VALUE;

			for (MovingObject obj : game.getMovingObjects(frogIndex)) {
				MovingObjectType type = obj.getMovingObjectType();
				if (type != MovingObjectType.TURTLE && type != MovingObjectType.TRUNK) {
					continue;
				}
				if (!sharesLane(frog, obj)) {
					continue;
				}

				int distance = Math.abs(obj.getPosition().getX() - frog.getX());
				if (distance < nearestDistance) {
					nearest = obj;
					nearestDistance = distance;
				}
			}

			if (nearest == null) {
				return null;
			}
			return (nearest.getPosition().getX() > frog.getX()) ? Direction.RIGHT : Direction.LEFT;
		}
		
		// Vero se le fasce verticali di rana e ostacolo si sovrappongono (con un piccolo anticipo).
		private boolean sharesLane(Frog frog, MovingObject obj) {
			int frogTop = frog.getY() - ENTER_MARGIN;
			int frogBottom = frog.getY() + frog.getHeight() + ENTER_MARGIN;
			int objTop = obj.getPosition().getY();
			int objBottom = objTop + obj.getSize().getHeight();
			return frogTop < objBottom && frogBottom > objTop;
		}

		// --- Comune ---

		// Un "errore" della difficoltà è un'incertezza nell'allineamento (va verso il lato sbagliato),
		// non una mossa suicida: non deve mai poter bypassare i controlli di sicurezza su strada o
		// acqua scegliendo UP/DOWN alla cieca, altrimenti capiterebbe prima o poi di annegare o
		// finire sotto un'auto proprio per un tiro a caso sfortunato.
		private Direction randomDirection() {
			return rnd.nextBoolean() ? Direction.LEFT : Direction.RIGHT;
		}

		private void applyMove(Frog frog, Direction direction) {
			if (direction == null) {
				return;
			}
			// Nessuna mossa può farla entrare in acqua se in quel preciso istante non c'è davvero
			// un tronco o una tartaruga ad aspettarla: controllato sui dati più freschi possibile,
			// appena prima del passo vero e proprio, non con una previsione a lungo raggio che
			// potrebbe non essere più valida quando ci arriva davvero.
			if (direction == Direction.UP && wouldEnterWaterUnsafely(frog)) {
				return;
			}
			switch (direction) {
				case UP:    game.moveFrogUp(frog);    break;
				case DOWN:  game.moveFrogDown(frog);  break;
				case LEFT:  game.moveFrogLeft(frog);  break;
				case RIGHT: game.moveFrogRight(frog); break;
			}
		}
		
		
		
		
		
		
		
	
		// Vero se, facendo subito un passo in su, la rana si troverebbe in acqua senza un tronco o
		// una tartaruga proprio sotto di sé in quel preciso istante.
		private boolean wouldEnterWaterUnsafely(Frog frog) {
			int nextTop = frog.getY() - MOVE_STEP;
			int nextBottom = nextTop + frog.getHeight();
			int nextCenterY = nextTop + frog.getHeight() / 2;
			int riverTop = game.getMap().getRiverTop();
			int riverBottom = game.getMap().getRiverBottom();

			if (nextCenterY < riverTop || nextCenterY > riverBottom) {
				return false; // il prossimo passo non la porta comunque in acqua
			}

			int frogLeft = frog.getX();
			int frogRight = frogLeft + frog.getWidth();
			for (MovingObject obj : game.getMovingObjects(frogIndex)) {
				MovingObjectType type = obj.getMovingObjectType();
				if (type != MovingObjectType.TURTLE && type != MovingObjectType.TRUNK) {
					continue;
				}
				int objTop = obj.getPosition().getY();
				int objBottom = objTop + obj.getSize().getHeight();
				if (nextTop >= objBottom || nextBottom <= objTop) {
					continue;
				}
				int objLeft = obj.getPosition().getX();
				int objRight = objLeft + obj.getSize().getWidth();
				if (frogLeft < objRight && frogRight > objLeft) {
					return false; // c'è davvero una zattera proprio lì
				}
			}
			return true;
		}

}
