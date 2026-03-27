package model;

public enum MovingObjectType {
    CAR(80, 50),
    TRUCK(120, 50),
    TURTLE(80, 50),
    TRUNK(120, 50);

    private final int width;
    private final int height;

    MovingObjectType(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
    	return width; 
    }
    
    public int getHeight() { 
    	return height; 
    }
}