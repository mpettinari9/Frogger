package model;

//Tipi di oggetti mobili presenti nel gioco.
public enum MovingObjectType {
	CAR(55, 45),
	TRUCK(107, 45),
	TURTLE(180, 50),
	TRUNK(196, 50);
	
    private final int width;
    private final int height;

    MovingObjectType(int width, int height) {
        this.width = width;
        this.height = height;
    }

    //Getter
    public int getWidth() {
    	return width; 
    }
    
    public int getHeight() { 
    	return height; 
    }
}