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


// HIGHSCORE, CHALLENGE
public class ScrabbleTestAdapterExtended2 extends ScrabbleTestAdapterExtended1 {
	
	public ScrabbleTestAdapterExtended2() {
		super();
	}
	
	/**
	 * Returns the position of the "Check" button, with which a player can check
	 * for the validity of all the words that are played by the player before them.
	 * Returns null if there are no such buttons.
	 * @return position of "Check" button
	 */
	
	public Vector2f getCheckButtonPosition() {
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
	
	/**
	 * Checks if a word, represented by the String parameter, is a legal word according to the Lexicon.
	 * @param word word to check
	 * @return true if word is legal, false otherwise
	 */
	public boolean isWordLegal(String word) {
		// TODO
		Lexicon lex = new Lexicon();
		return lex.check(word);
	}
	
	/**
	 * Adds a highscore to the list of highscores to be shown with the given parameters
	 * @param name name of player
	 * @param score score of player
	 * @param round round the player won
	 */
	
	public void addHighscore(String name, int score , int round) {
		// TODO
		HighscoreList.getInstance().addHighscore(new Highscore(name,score,round));
	}
	
	/**
	 * Resets the highscore list. The highscore list should be empty after this method is called.
	 */
	
	public void resetHighscore() {
		// TODO
		HighscoreList.getInstance().getHighscores().clear();
	}
	
	/**
	 * Returns the name of the player of the entry in the highscore lists at the given position in the list.
	 * @param pos position of the highscore entry in list
	 * @return name of player at the aforementioned entry
	 */
	
	public String getPlayerNameInHighscore(int pos) {
		// TODO
		return HighscoreList.getInstance().getHighscores().get(pos).getName();
	}
	
	/**
	 * Returns the score of the player of the entry in the highscore lists at the given position in the list.
	 * @param pos position of the highscore entry in list
	 * @return score of player at the aforementioned entry
	 */
	
	public int getScoreOfPlayerInHighscore(int pos) {
		// TODO
		return HighscoreList.getInstance().getHighscores().get(pos).getScore();
	}
	
	/**
	 * Returns the round that the player won of the entry in the highscore lists at the given position in the list.
	 * @param pos position of the highscore entry in list
	 * @return round that the player won at the aforementioned entry
	 */
	
	public int getRoundOfPlayerInHighscore(int pos) {
		// TODO
		return HighscoreList.getInstance().getHighscores().get(pos).getRound();
	}
	
	/**
	 * Returns the number of highscore entries that are currently being shown.
	 * @return number of highscore entries available
	 */
	
	public int getHighscoreListSize() {
		// TODO
		return HighscoreList.getInstance().getHighscores().size();
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
