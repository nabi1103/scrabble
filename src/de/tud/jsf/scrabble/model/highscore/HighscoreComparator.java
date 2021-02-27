package de.tud.jsf.scrabble.model.highscore;

import java.util.Comparator;




public class HighscoreComparator implements Comparator<Highscore>{

	@Override
	public int compare(Highscore h1, Highscore h2) {
		if (h1.getScore() == h2.getScore()) {
			if (h1.getRound() < h2.getRound()) return -1;
			if (h1.getRound() == h2.getRound()) return 0;
		}
		if (h1.getScore() > h2.getScore()) return -1;
		else return 1;
	}

}
