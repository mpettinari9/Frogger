package model;

//Rappresenta una posizione bidimensionale (x, y) nello spazio di gioco.
//Utilizzata per rana, oggetti mobili, cuori e hitbox.
public class Position {
	private int x;
	private int y;
	
	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}

	//Getter e setter
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	//Sposta la posizione di un dato offset
    public void move(double dx, double dy) {
    	this.x += dx;
    	this.y += dy;
    }
}
