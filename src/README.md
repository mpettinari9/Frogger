# Frogger - Progetto Esame

Implementazione del classico gioco Frogger in Java con architettura MVC.

## Come eseguire:
1. Apri il progetto in Eclipse
2. Esegui 'launcher/Launcher.java'

## Strutture 
- 'model/' 		- logica di gioco (Frog, Game, MovingObject, ...)
- 'view/'       - interfaccia grafica (GamePanel, GameWindow, Sprite, ...)
- 'controller/' - gestione input utente e coordinamento model - view (GameController)
- 'jUnitTest/'  - test JUnit

## Controlli
- Frecce direzionali per muovere la rana
- Raggiungi il bordo superiore per vincere
- Evita auto e camion, usa tronchi e tartarughe per attraversare il fiume