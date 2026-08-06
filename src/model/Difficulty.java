package model;

// Livelli di difficoltà per la rana controllata dalla CPU.
// reactionInterval: ogni quanti frame la CPU rivaluta la propria strategia (più alto = più lenta a pianificare)
// safeGap: margine di sicurezza orizzontale con cui la CPU "gonfia" auto e camion quando simula in
//          avanti se una mossa è sicura: un margine più ampio le fa scartare una mossa quando
//          l'ostacolo è ancora lontano, invece di aspettare che sia quasi addosso, quindi qui è la
//          CPU più brava a schivare ad avere il margine maggiore
// mistakePercent: probabilità (0-100) di scegliere una mossa casuale invece di quella corretta
public enum Difficulty {
	EASY(12, 55, 25),
	MEDIUM(6, 70, 10),
	HARD(2, 90, 2);

	private final int reactionInterval;
	private final int safeGap;
	private final int mistakePercent;

	Difficulty(int reactionInterval, int safeGap, int mistakePercent) {
		this.reactionInterval = reactionInterval;
		this.safeGap = safeGap;
		this.mistakePercent = mistakePercent;
	}

	//Getter
	public int getReactionInterval() {
		return reactionInterval;
	}

	public int getSafeGap() {
		return safeGap;
	}

	public int getMistakePercent() {
		return mistakePercent;
	}
}
