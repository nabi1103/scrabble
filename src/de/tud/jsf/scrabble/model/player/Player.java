package de.tud.jsf.scrabble.model.player;

public class Player {
	
	private String name;
	private int score;
	private int id;
	
	public int getID() {
		return id;
	}
	
	public int getScore() {
		return score;
	}
	
	public String getName() {
		return name;
	}
	
	public Player(String name, int id) {
		score = 0;
		this.name = name;
		this.id = id;
	}
	
	public void setScore(int score) {
		this.score = score;
	}
}
