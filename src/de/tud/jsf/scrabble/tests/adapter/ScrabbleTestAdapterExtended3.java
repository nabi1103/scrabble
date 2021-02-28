package de.tud.jsf.scrabble.tests.adapter;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.geom.Vector2f;

import de.tud.jsf.scrabble.ui.entity.DialogueButton;
import eea.engine.entity.Entity;
import eea.engine.entity.StateBasedEntityManager;

// TRADE
public class ScrabbleTestAdapterExtended3 extends ScrabbleTestAdapterExtended2 {
	
	public ScrabbleTestAdapterExtended3() {
		super();
	}
	
	/**
	 * Returns the position of the "Show/Hide" button, with whom the player can click on the show/hide the content of
	 * their inventory (only visually).
	 * If there are no such buttons, null is returned
	 * @return a Vector2f Object representing the position of the "Show/Hide" button in GameplayState, null if there none.
	 */
	
	public Vector2f getShowButtonPosition() {
		// TODO
		List<Entity> entities = new ArrayList<Entity>();
		if (scrabble != null) 
			entities = StateBasedEntityManager.getInstance().getEntitiesByState(getGameplayStateID());
		for (Entity e : entities) {
			if (e.getID() == "show_button") {
				return e.getPosition();
			}
		}
		return null;
	}
	
	/**
	 * Returns true, if the contents of the inventory at the point of this call are hidden from view.
	 * False otherwise.
	 * @return true if hidden, false otherwise
	 */
	
	public boolean areLettersHidden() {
		List<Entity> entities = new ArrayList<Entity>();
		if (scrabble != null) 
			entities = StateBasedEntityManager.getInstance().getEntitiesByState(getGameplayStateID());
		for (Entity e : entities) {
			if (e.getID() == "show_button") {
				if ((((DialogueButton)e).getType()) == "show") return true;
			}
		}
		return false;
	}
	


}
