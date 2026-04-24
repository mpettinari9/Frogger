package model;

//Rappresenta le dimensioni bidimensionali (larghezza e altezza).
//Utilizzata per mappe, hitbox e dimensioni degli oggetti di gioco.
public class Size {
	private int width;
	private int height;
	
	public Size(int width, int height) {
		this.width = width;
		this.height = height;
	}

	//Getter e setter
	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
}
