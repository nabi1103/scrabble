package de.tud.jsf.scrabble.model.player;

import java.util.ArrayList;

import de.tud.jsf.scrabble.ui.entity.Letter;

public class Player {
	
	private String name;
	private int score;
	private int id;
	private ArrayList<String> letters;
	
	public int getID() {
		return id;
	}
	
	public int getScore() {
		return score;
	}
	
	public String getName() {
		return name;
	}
	
	public ArrayList<String> getLetters() {
		return letters;
	}
	
	public Player(String name, int id) {
		score = 0;
		this.name = name;
		this.id = id;
		letters = new ArrayList<String>();
	}
	
	public void setScore(int score) {
		this.score = score;
	}

	public void addLetter(String to_add) {
		this.letters.add(to_add);
	}
	
	public void removeLetter(String l) {
		this.letters.remove(l);
	}
}
