package model;

// Rana femmina che compare su un tronco casuale dopo un intervallo di tempo.
// Assegna punti bonus se raccolta. Scompare automaticamente dopo DURATION_SECONDS.
// Estende TimedCollectible perché ha un timer di scadenza automatica.
public class FemaleFrog extends TimedCollectible {

    public static final int WIDTH = 50;
    public static final int HEIGHT = 50;
    public static final int DURATION_SECONDS = 10; // Scompare dopo 10 secondi
    public static final int BONUS_POINTS = 200;    // Punti bonus se raccolta

    public FemaleFrog(int x, int y) {
        super(x, y, WIDTH, HEIGHT, DURATION_SECONDS);
    }

    // Quando raccolta: aggiunge punti bonus al punteggio della rana che la raccoglie
    @Override
    public void onCollect(Frog frog, Game game) {
        game.addScore(frog, BONUS_POINTS);
        this.deactivate();
    }
}