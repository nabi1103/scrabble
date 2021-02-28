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
	
	public int getTradeStateID() {
		return GameParameters.CE_STATE;
	}
	
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
	
	public int getNumberOfLettersInTradeZone() {
		// TODO
		return ((GameplayState) scrabble.getState(getGameplayStateID())).getTradedLetterSize(); 
	}
	
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
	
	public boolean minigameEnded() {
		if (getTradeStateID() != getStateBasedGame().getCurrentStateID()) {
			return false;
		}
		return ((CEState) scrabble.getState(getTradeStateID())).getReturnClickable();
	}
	
	public boolean isRandomTradeActivated() {
		// TODO
		return !((GameplayState) scrabble.getState(getGameplayStateID())).getAdvancedTrade(); 
	}
	
	public boolean isMinigameTradeActivated() {
		// TODO
		return ((GameplayState) scrabble.getState(getGameplayStateID())).getAdvancedTrade(); 
	}
	
	public boolean isTradeZoneEmpty() {
		return (getNumberOfLettersInTradeZone() == 0);
	}
	
	public List<Character> getLettersInTradeZone() {
		ArrayList<Character> res = new ArrayList<Character>();
		for (String letter : ((GameplayState) scrabble.getState(getGameplayStateID())).getTradedLetter()) {
			res.add(letter.charAt(0));
		}
		return res;
		
	}
	
	public int getNumberOfLettersWonInMinigame() {
		return ((CEState) scrabble.getState(getTradeStateID())).getTakenLettersSize();
	}
	
	

}
