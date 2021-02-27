package de.tud.jsf.scrabble.tests.adapter;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.geom.Vector2f;

import de.tud.jsf.scrabble.model.highscore.Highscore;
import de.tud.jsf.scrabble.model.highscore.HighscoreList;
import de.tud.jsf.scrabble.ui.entity.DialogueButton;
import de.tud.jsf.scrabble.ui.entity.Lexicon;
import eea.engine.entity.Entity;
import eea.engine.entity.StateBasedEntityManager;


// HIGHSCORE, SHOW/HIDE, GAMEWINNING
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
		// TODO
		HighscoreList.getInstance().addHighscore(new Highscore(name,score,round));
	}
	
	public void resetHighscore() {
		// TODO
		HighscoreList.getInstance().getHighscores().clear();
	}
	
	public String getPlayerNameInHighscore(int pos) {
		// TODO
		return HighscoreList.getInstance().getHighscores().get(pos).getName();
	}
	
	public int getScoreOfPlayerInHighscore(int pos) {
		// TODO
		return HighscoreList.getInstance().getHighscores().get(pos).getScore();
	}
	
	public int getRoundOfPlayerInHighscore(int pos) {
		// TODO
		return HighscoreList.getInstance().getHighscores().get(pos).getRound();
	}
	
	public int getHighscoreListSize() {
		// TODO
		return HighscoreList.getInstance().getHighscores().size();
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
