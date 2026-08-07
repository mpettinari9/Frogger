package model;

// Rappresenta una delle 5 tane di arrivo in cima alla mappa.
// La rana deve occupare tutte e 5 per vincere la partita.
// Può contenere un Insect che assegna punti bonus.
public class HomeSlot {

    public static final int SLOT_WIDTH = 50;
    public static final int SLOT_HEIGHT = 40;

    private final int x;
    private final int y;
    private boolean occupied;
    private HitBox hitBox;
    private Insect insect; // Insetto presente nella tana (può essere null)

    public HomeSlot(int x, int y) {
        this.x = x;
        this.y = y;
        this.occupied = false;
        this.hitBox = new HitBox(x, y, SLOT_WIDTH, SLOT_HEIGHT);
        this.insect = null;
    }

    // Getter e setter
    public boolean isOccupied() { return occupied; }
    public void setOccupied(boolean occupied) { this.occupied = occupied; }

    public HitBox getHitBox() { return hitBox; }

    public int getX() { return x; }
    public int getY() { return y; }

    public Insect getInsect() { return insect; }
    public void setInsect(Insect insect) { this.insect = insect; }
    public boolean hasInsect() { return insect != null && insect.isActive(); }
}