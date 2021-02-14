package de.tud.jsf.scrabble.model.highscore;
import java.io.Serializable;
public class Highscore implements Serializable {
	String name;
	int score;
	int round;
	private static final long serialVersionUID = 8175891823171504140L;
	
	public Highscore(String name, int score, int round) {
		this.name = name;
		this.score = score;
		this.round = round;
	}
	
	public String getName() {
		return name;
	}
	
	public int getScore() {
		return score;
	}
	
	public int getRound() {
		return round;
	}
	
	@Override
	public String toString() {
		return name + score + round;
	}

}
