package de.tud.jsf.scrabble.tests.adapter;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.geom.Vector2f;

import de.tud.jsf.scrabble.ui.entity.Lexicon;
import eea.engine.entity.Entity;
import eea.engine.entity.StateBasedEntityManager;


// CHALLENGE, HIGHSCORE, SHOW/HIDE
public class ScrabbleTestAdapterExtended1 extends ScrabbleTestAdapterMinimal {
	
	public ScrabbleTestAdapterExtended1() {
		super();
	}
	
	public Vector2f getChallengeButtonPosition() {
		// TODO
		List<Entity> entities = new ArrayList<Entity>();
		if (scrabble != null) 
			entities = StateBasedEntityManager.getInstance().getEntitiesByState(getGameplayStateID());
		for (Entity e : entities) {
			if (e.getID() == "check_button") {
				return e.getPosition();
			}
		}
		return null;
	}
	
	public boolean isWordLegal(String word) {
		// TODO
		Lexicon lex = new Lexicon();
		return lex.check(word);
	}
	
	public void addHighscore(String name, int score , int round) {
		
	}
	
	public void resetHighscore() {
		
	}
	
	public String getPlayerNameInHighscore(int pos) {
		return null;
	}
	
	public int getScoreOfPlayerInHighscore(int pos) {
		return -1;
	}
	
	public int getHighscoreCount() {
		return -1;
	}
	
	
	
	
	
	
	
	
}
