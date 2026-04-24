package model;

//Rappresenta la mappa di gioco con le sue dimensioni e l'area del fiume.
//Il fiume è definito da un intervallo verticale (riverTop, riverBottom).
public class Map{
	private final Size size;
    private final int riverTop;     // Coordinata Y superiore del fiume (inizio acqua)
    private final int riverBottom;  // Coordinata Y inferiore del fiume (fine acqua)
	
    //Costruttore con oggetto Size
	public Map(Size size, int riverTop, int riverBottom) {
		this.size = size;
		this.riverTop = riverTop;
		this.riverBottom = riverBottom;
	}
	
	//Costruttore con larghezza e altezza esplicite
	public Map(int width, int height, int riverTop, int riverBottom) {
		this(new Size(width, height), riverTop, riverBottom);
	}
	
	//Getter
	public int getWidth() {
	    return size.getWidth();
	}

	public int getHeight() {
	    return size.getHeight();
	}

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
