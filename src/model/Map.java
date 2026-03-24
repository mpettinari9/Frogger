package model;

public class Map{
	private final Size size;
	private final int riverTop;
	private final int riverBottom;
	
	public Map(Size size, int riverTop, int riverBottom) {
		this.size = size;
		this.riverTop = riverTop;
		this.riverBottom = riverBottom;
	}
	
	public Map(int width, int height, int riverTop, int riverBottom) {
		this(new Size(width, height), riverTop, riverBottom);
	}
	
	public int getWidth() {
	    return size.getWidth();
	}

	public int getHeight() {
	    return size.getHeight();
	}

	// getter per acqua
    public int getRiverTop() {
        return riverTop;
    }

    public int getRiverBottom() {
        return riverBottom;
    }

	public Size getSize() {
		return size;
	}
}
