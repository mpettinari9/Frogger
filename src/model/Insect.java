package model;

// Insetto che compare in una tana libera casuale.
// Assegna punti extra se la rana raggiunge quella tana.
// Scompare automaticamente dopo DURATION_SECONDS.
// Estende TimedCollectible perché ha un timer di scadenza automatica.
public class Insect extends TimedCollectible {

    public static final int WIDTH = 40;
    public static final int HEIGHT = 40;
    public static final int DURATION_SECONDS = 8; // Scompare dopo 8 secondi
    public static final int BONUS_POINTS = 300;   // Punti extra se raccolta

    private int slotIndex; // Indice della tana in cui si trova

    public Insect(int x, int y, int slotIndex) {
        super(x, y, WIDTH, HEIGHT, DURATION_SECONDS);
        this.slotIndex = slotIndex;
    }

    // Quando raccolta: aggiunge punti extra al punteggio della rana che la raccoglie
    @Override
    public void onCollect(Frog frog, Game game) {
        game.addScore(frog, BONUS_POINTS);
        this.deactivate();
    }

    public int getSlotIndex() {
        return slotIndex;
    }
}