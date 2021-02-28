package de.tud.jsf.scrabble.tests.adapter;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.geom.Vector2f;

import de.tud.jsf.scrabble.constants.GameParameters;
import de.tud.jsf.scrabble.ui.states.CEState;
import de.tud.jsf.scrabble.ui.states.GameplayState;
import eea.engine.entity.Entity;
import eea.engine.entity.StateBasedEntityManager;

// CHALLENGE
public class ScrabbleTestAdapterExtended1 extends ScrabbleTestAdapterMinimal {
	
	public ScrabbleTestAdapterExtended1() {
		super();
	}
	
	/**
	 * Returns the ID of the state in which the minigame is played, as specified by StateBasedEntityManager.
	 * @return ID of minigame state
	 */
	
	public int getTradeStateID() {
		return GameParameters.CE_STATE;
	}
	
	/**
	 * Returns the position of the "Trade" button, which either setups the player for the minigame or
	 * just do the randomly exchaning process.
	 * If there are no such letters, null is returned.
	 * @return position of "Trade" button as a Vector2f or null if there are no such buttons
	 */
	
	public Vector2f getTradeButtonPosition() {
		// TODO
		List<Entity> entities = new ArrayList<Entity>();
		if (scrabble != null) 
			entities = StateBasedEntityManager.getInstance().getEntitiesByState(getGameplayStateID());
		for (Entity e : entities) {
			if (e.getID() == "trade_button") {
				return e.getPosition();
			}
		}
		return null;
	}
	
	/**
	 * Returns the position of the trade zone, which the player can put letters in for a trade.
	 * Null if there are no trade zone available.
	 * @return Vector2f object representing the position of the tradezone, null if there are no such thing.
	 */
	
	public Vector2f getTradeZonePosition() {
		// TODO
		List<Entity> entities = new ArrayList<Entity>();
		if (scrabble != null) 
			entities = StateBasedEntityManager.getInstance().getEntitiesByState(getGameplayStateID());
		for (Entity e : entities) {
			if (e.getID() == "trade_zone") {
				return e.getPosition();
			}
		}
		return null;
	}
	/**
	 * Returns the number of letters that are currently being placed in the trade zone.
	 * @return number of letters that will be traded away.
	 */
	
	public int getNumberOfLettersInTradeZone() {
		// TODO
		return ((GameplayState) scrabble.getState(getGameplayStateID())).getTradedLetterSize(); 
	}
	
	/**
	 * Returns the position of the "Start" button in the minigame, with which the player
	 * click on it to start playing the minigame.
	 * Returns null if there are no such buttons.
	 * @return Vector2f representing the position of button, null if there are no such buttons.
	 */
	
	public Vector2f getMinigameStartButtonPosition() {
		// TODO
		List<Entity> entities = new ArrayList<Entity>();
		if (scrabble != null) 
			entities = StateBasedEntityManager.getInstance().getEntitiesByState(getTradeStateID());
		for (Entity e : entities) {
			if (e.getID() == "play_button") {
				return e.getPosition();
			}
		}
		return null;
	}
	
	/**
	 * Returns the position of the "Return" button in the minigame, with which the player
	 * clicks on after finishing the game to return to the playing board so that the game
	 * can proceeds further.
	 * Null if there are no such buttons.
	 * @return Vector2f representing the position of "Return" button, null if there are none.
	 */
	
	public Vector2f getMinigameReturnButtonPosition() {
		// TODO
		List<Entity> entities = new ArrayList<Entity>();
		if (scrabble != null) 
			entities = StateBasedEntityManager.getInstance().getEntitiesByState(getTradeStateID());
		for (Entity e : entities) {
			if (e.getID() == "return_button") {
				return e.getPosition();
			}
		}
		return null;
	}
	
	/**
	 * Returns true if the minigame has ended, false otherwise.
	 * @return true if ended, false otherwise
	 */
	
	public boolean minigameEnded() {
		if (getTradeStateID() != getStateBasedGame().getCurrentStateID()) {
			return false;
		}
		return ((CEState) scrabble.getState(getTradeStateID())).getReturnClickable();
	}
	
	/**
	 * Returns true if random trading is activated.
	 * @return true if random trading is activated, false otherwise
	 */
	
	public boolean isRandomTradeActivated() {
		// TODO
		return !((GameplayState) scrabble.getState(getGameplayStateID())).getAdvancedTrade(); 
	}
	
	/**
	 * return true if trading with minigame is activated.
	 * @return true if minigame trade is activated, false otherwise
	 */
	
	public boolean isMinigameTradeActivated() {
		// TODO
		return ((GameplayState) scrabble.getState(getGameplayStateID())).getAdvancedTrade(); 
	}
	
	/**
	 * Returns true if there are no letters in the trading zone.
	 * @return true if trading zone empty, false otherwise
	 */
	
	public boolean isTradeZoneEmpty() {
		return (getNumberOfLettersInTradeZone() == 0);
	}
	
	/**
	 * Returns all the letters that are currently in the trade zone a list of characters.
	 * Each char represents a letter in the trading zone.
	 * @return list of chars in the trading zone
	 */
	
	public List<Character> getLettersInTradeZone() {
		ArrayList<Character> res = new ArrayList<Character>();
		for (String letter : ((GameplayState) scrabble.getState(getGameplayStateID())).getTradedLetter()) {
			res.add(letter.charAt(0));
		}
		return res;
		
	}
	
	/**
	 * Returns the number of letters that the player currently has caught in the minigame.
	 * @return the number of letters the player has won.
	 */
	
	public int getNumberOfLettersWonInMinigame() {
		return ((CEState) scrabble.getState(getTradeStateID())).getTakenLettersSize();
	}
	
	

}
