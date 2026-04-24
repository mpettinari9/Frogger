package model;

//Riquadro di collisione rettangolare allineato agli assi (AABB).
//Utilizzato per rilevare intersezioni tra rana, oggetti mobili e cuori.
public class HitBox {
	private int x;
	private int y;
	private int width;
	private int height;
	
	public HitBox(int x, int y,int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	//Verifica se questa hitbox si interseca con un'altra
    public boolean intersects(HitBox other) {
        //Controlla se il lato sinistro di this è a sinistra del lato destro di other
        //e se il lato destro di this è a destra del lato sinistro di other,
        //analogamente per l'asse Y.
        return this.x < other.x + other.width &&  
               this.x + this.width > other.x &&  
               this.y < other.y + other.height && 
               this.y + this.height > other.y;    
    }
    
    //Getter e setter
    public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}
}
