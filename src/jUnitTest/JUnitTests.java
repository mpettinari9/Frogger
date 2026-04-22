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
	
	public void testMoveDown() {
		int initialY = frog.getY();
		frog.moveDown();
		assertTrue(frog.getY() > initialY);
		assertEquals(Direction.DOWN, frog.getDirection());
	}
	
	public void testMoveLeft() {
		int initialX = frog.getX();
		frog.moveLeft();
		assertTrue(frog.getX() < initialX);
		assertEquals(Direction.LEFT, frog.getDirection());
	}
	
	public void testMoveRight() {
		int initialX = frog.getX();
		frog.moveRight();
		assertTrue(frog.getX() > initialX);
		assertEquals(Direction.RIGHT, frog.getDirection());
	}
	
	public void testHitBoxIntersects() {
		HitBox a = new HitBox(0, 0, 50, 50);
		HitBox b = new HitBox(25, 25, 50, 50);
		assertTrue(a.intersects(b));
	}
	
	public void testHitBoxNoIntersect() {
		HitBox a = new HitBox(0, 0, 50, 50);
		HitBox b = new HitBox(100, 100, 50, 50);
		assertFalse(a.intersects(b));
	}
	
	public void testHitBoxAdjacentNoIntersect() {
		HitBox a = new HitBox(0, 0, 50, 50);
		HitBox b = new HitBox(50, 0, 50, 50);
		assertFalse(a.intersects(b));
	}
	
	@Test
	public void testLoseLife() {
		int initialLives = frog.getLives();
		frog.loseLife();
		assertEquals(initialLives -1, frog.getLives());
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
}
