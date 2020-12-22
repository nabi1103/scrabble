package de.tud.jsf.scrabble.ui.entity;

import java.util.ArrayList;

public class Word {
	String value;
	int score;
	ArrayList<Tile> tiles;
	
	public Word(String value, int score, ArrayList<Tile> tiles) {
		this.value = value;
		this.score = score;
		this.tiles = tiles;
	}
	
	public void setValue(String word) {
		this.value = word;
	}
	
	public void setScore(int score) {
		this.score = score;
	}
	
	public void setTiles(ArrayList<Tile> tiles) {
		this.tiles = tiles;
	}
	
	public ArrayList<Tile> getTiles() {
		return tiles;
	}
	
	public String getValue() {
		return value;
	}
	
	public int getScore() {
		return score;
	}
}
