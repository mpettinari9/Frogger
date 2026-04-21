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
}
