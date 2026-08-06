package model;

// Interfaccia comune per tutti gli oggetti raccoglibili dalla rana.
// Implementata da Heart e Insect.
public interface Collectible {

    // Restituisce la hitbox dell'oggetto per il rilevamento collisioni
    HitBox getHitBox();

    // Indica se l'oggetto è attualmente presente sulla mappa
    boolean isActive();

    // Effetto applicato alla rana quando viene raccolto
    void onCollect(Frog frog, Game game);
}