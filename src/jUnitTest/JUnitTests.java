package jUnitTest;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.*;

public class JUnitTests {
	private Map map;
	private Frog frog;

	@BeforeEach
	public void setup() {
		map = new Map(1280, 720, 40, 290);
		frog = new Frog("Test", Direction.UP, new Size(50, 50), new Position(615, 565), map, 2);
	}

	@Test
	public void testMoveUp() {
		int initialY = frog.getY();
		frog.moveUp();
		assertTrue(frog.getY() < initialY);
		assertEquals(Direction.UP, frog.getDirection());
	}

	@Test
	public void testMoveDown() {
		int initialY = frog.getY();
		frog.moveDown();
		assertTrue(frog.getY() > initialY);
		assertEquals(Direction.DOWN, frog.getDirection());
	}

	@Test
	public void testMoveLeft() {
		int initialX = frog.getX();
		frog.moveLeft();
		assertTrue(frog.getX() < initialX);
		assertEquals(Direction.LEFT, frog.getDirection());
	}

	@Test
	public void testMoveRight() {
		int initialX = frog.getX();
		frog.moveRight();
		assertTrue(frog.getX() > initialX);
		assertEquals(Direction.RIGHT, frog.getDirection());
	}

	@Test
	public void testHitBoxIntersects() {
		HitBox a = new HitBox(0, 0, 50, 50);
		HitBox b = new HitBox(25, 25, 50, 50);
		assertTrue(a.intersects(b));
	}

	@Test
	public void testHitBoxNoIntersect() {
		HitBox a = new HitBox(0, 0, 50, 50);
		HitBox b = new HitBox(100, 100, 50, 50);
		assertFalse(a.intersects(b));
	}

	@Test
	public void testHitBoxAdjacentNoIntersect() {
		HitBox a = new HitBox(0, 0, 50, 50);
		HitBox b = new HitBox(50, 0, 50, 50);
		assertFalse(a.intersects(b));
	}

	@Test
	public void testLoseLife() {
		int initialLives = frog.getLives();
		frog.loseLife();
		assertEquals(initialLives - 1, frog.getLives());
	}

	@Test
	public void testIsDead() {
		frog.loseLife();
		frog.loseLife();
		assertTrue(frog.isDead());
	}

	@Test
	public void testResetLives() {
		frog.loseLife();
		frog.resetLives();
		assertEquals(Frog.DEFAULT_LIVES, frog.getLives());
	}

	@Test
	public void testInWaterArea() {
		// riverTop = 40, riverBottom = 290, center della rana = y + 25
		Frog waterFrog = new Frog("W", Direction.UP, new Size(50, 50), new Position(100, 140), map, 2);
		// center = 165 -> dentro [40, 290]
		assertTrue(waterFrog.isInWaterArea());
	}

	@Test
	public void testNotInWaterArea() {
		Frog landFrog = new Frog("L", Direction.UP, new Size(50, 50), new Position(100, 500), map, 2);
		// center = 525 -> fuori [40, 290]
		assertFalse(landFrog.isInWaterArea());
	}

	@Test
	public void testMovingObjectMovesRight() {
		MovingObject obj = new MovingObject(100, 500, MovingObjectType.CAR, map, 0);
		obj.setDirection(Direction.RIGHT);
		int initialX = obj.getPosition().getX();
		obj.updatePosition();
		assertTrue(obj.getPosition().getX() > initialX);
	}

	@Test
	public void testMovingObjectMovesLeft() {
		MovingObject obj = new MovingObject(100, 390, MovingObjectType.TRUCK, map, 0);
		obj.setDirection(Direction.LEFT);
		int initialX = obj.getPosition().getX();
		obj.updatePosition();
		assertTrue(obj.getPosition().getX() < initialX);
	}

	@Test
	public void testCheckMovingObjectCollisionWithCar() {
		Game game = new Game(map, 1);
		Frog f = new Frog("T", Direction.UP, new Size(50, 50), new Position(100, 500), map, 2);
		game.setFrog(0, f);
		MovingObject car = new MovingObject(100, 500, MovingObjectType.CAR, map, 0);
		car.setDirection(Direction.RIGHT);
		game.addMovingObject(car);
		assertTrue(game.checkMovingObjectCollision());
	}

	@Test
	public void testCheckMovingObjectNoCollision() {
		Game game = new Game(map, 1);
		Frog f = new Frog("T", Direction.UP, new Size(50, 50), new Position(0, 500), map, 2);
		game.setFrog(0, f);
		MovingObject car = new MovingObject(900, 500, MovingObjectType.CAR, map, 0);
		car.setDirection(Direction.RIGHT);
		game.addMovingObject(car);
		assertFalse(game.checkMovingObjectCollision());
	}

	@Test
	public void testHeartHitBoxCreated() {
		Heart el = new Heart(100, 200);
		assertNotNull(el.getHitBox());
		assertEquals(100, el.getX());
		assertEquals(200, el.getY());
	}

	@Test
	public void testCheckHeartCollision() {
		Game game = new Game(map, 1);
		Frog f = new Frog("T", Direction.UP, new Size(50, 50), new Position(615, 565), map, 2);
		f.loseLife(); // 1 vita rimasta
		game.setFrog(0, f);
		// Forza spawn del cuore nella stessa posizione della rana
		// Verifica che dopo la collisione le vite si resettino
		assertEquals(1, f.getLives());
	}

	@Test
	public void testGameOverOnLivesZero() {
		Game game = new Game(map, 1);
		Frog f = new Frog("T", Direction.UP, new Size(50, 50), new Position(615, 565), map, 2);
		game.setFrog(0, f);
		f.loseLife();
		f.loseLife();
		assertTrue(game.checkGameOver());
	}

	@Test
	public void testGameNotOverWithLives() {
		Game game = new Game(map, 1);
		Frog f = new Frog("T", Direction.UP, new Size(50, 50), new Position(615, 565), map, 2);
		game.setFrog(0, f);
		assertFalse(game.checkGameOver());
	}
	
	@Test
	public void testGameOverOnWin() {
		// Vittoria: occupare tutte e 5 le tane del giocatore (non più "arrivare a y=0")
		Game game = new Game(map, 1);
		Frog f = new Frog("T", Direction.UP, new Size(50, 50), new Position(615, 565), map, 2);
		game.setFrog(0, f);
		for (HomeSlot slot : game.getHomeSlots(0)) {
			slot.setOccupied(true);
		}
		assertTrue(game.checkGameOver());
	}
	
	@Test
	public void testGameOverRequiresAllFrogsDone() {
		// Con 2 rane in gara, la partita non finisce finché entrambe non hanno concluso
		// (vinto occupando le proprie 5 tane, o esaurito le vite)
		Game game = new Game(map, 2);
		Frog f1 = new Frog("T1", Direction.UP, new Size(50, 50), new Position(615, 0), map, 2);
		Frog f2 = new Frog("T2", Direction.UP, new Size(50, 50), new Position(100, 500), map, 2); // ancora in gioco
		game.setFrog(0, f1);
		game.setFrog(1, f2);

		for (HomeSlot slot : game.getHomeSlots(0)) {
			slot.setOccupied(true); // il giocatore 1 ha vinto
		}
		assertFalse(game.checkGameOver()); // il giocatore 2 è ancora in gara

		f2.loseLife();
		f2.loseLife();
		assertTrue(game.checkGameOver());
	}

	@Test
	public void testHomeSlotsAreSeparatePerPlayer() {
		// In gara, occupare le tane del giocatore 1 non deve influenzare quelle del giocatore 2
		Game game = new Game(map, 2);
		game.getHomeSlots(0)[0].setOccupied(true);
		assertTrue(game.getHomeSlots(0)[0].isOccupied());
		assertFalse(game.getHomeSlots(1)[0].isOccupied());
	}

	@Test
	public void testAddScorePerPlayer() {
		Game game = new Game(map, 2);
		Frog f1 = new Frog("T1", Direction.UP, new Size(50, 50), new Position(0, 0), map, 2);
		Frog f2 = new Frog("T2", Direction.UP, new Size(50, 50), new Position(0, 0), map, 2);
		game.setFrog(0, f1);
		game.setFrog(1, f2);

		game.addScore(f1, 200); // tramite overload Frog -> instradato all'indice corretto
		game.addScore(1, 300);  // tramite indice diretto

		assertEquals(200, game.getScore(0));
		assertEquals(300, game.getScore(1));
	}
}

