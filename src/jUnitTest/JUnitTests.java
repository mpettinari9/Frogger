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
	
}
