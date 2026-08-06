package model;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

// Classe astratta per i Collectible che scompaiono automaticamente dopo un timeout.
// FemaleFrog e Insect la estendono. Heart invece implementa Collectible direttamente
// perché non ha un timer di scadenza.
public abstract class TimedCollectible implements Collectible {

    protected Position position;
    protected HitBox hitBox;
    protected boolean active;
    protected LocalDateTime spawnTime;
    protected int durationSeconds; // Secondi prima della scomparsa automatica

    public TimedCollectible(int x, int y, int width, int height, int durationSeconds) {
        this.position = new Position(x, y);
        this.hitBox = new HitBox(x, y, width, height);
        this.active = true;
        this.spawnTime = LocalDateTime.now();
        this.durationSeconds = durationSeconds;
    }

    // Verifica se il timer è scaduto
    public boolean isExpired() {
        return spawnTime.until(LocalDateTime.now(), ChronoUnit.SECONDS) >= durationSeconds;
    }

    // Disattiva l'oggetto (raccolto o scaduto)
    public void deactivate() {
        this.active = false;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public HitBox getHitBox() {
        return hitBox;
    }

    public int getX() { return position.getX(); }
    public int getY() { return position.getY(); }
}