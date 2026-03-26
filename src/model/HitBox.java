package model;

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
	
	//formula standard AABB (Axis-Aligned Bounding Box)
    public boolean intersects(HitBox other) {
        return this.x < other.x + other.width &&  //Controlla che il lato sinistro di A (rana) sia a sinistra del lato destro di B (movingObject).
               this.x + this.width > other.x &&   //Controlla che il lato destro di A sia a destra del lato sinistro di B.
               this.y < other.y + other.height && //Controlla che il lato superiore di A sia sopra il lato inferiore di B.
               this.y + this.height > other.y;    //Controlla che il lato inferiore di A sia sotto il lato superiore di B.
    }
    
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
