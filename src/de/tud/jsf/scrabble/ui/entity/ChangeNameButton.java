package de.tud.jsf.scrabble.ui.entity;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import de.tud.jsf.scrabble.constants.GameParameters;
import de.tud.jsf.scrabble.ui.states.Launch;
import eea.engine.component.render.ImageRenderComponent;

import eea.engine.entity.Entity;

/**
 * A button that have its own description/name field.
 *
 */

public class ChangeNameButton extends Entity implements GameParameters{
	String id;
	String name;

	public ChangeNameButton(String entityID, int x, int y, float scale,String name) {
		super(entityID);
		
		this.id = entityID;
		this.setPosition(new Vector2f(x,y));

		this.setVisible(true);
		
		setScale(scale);
		this.name = name;
		if (!Launch.debug) {
			try {
				addImageComponent();
			}
			catch (SlickException exc) {
				System.err.println("Entity image cannot be loaded!");
			}
		}
				
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	
	public void addImageComponent() throws SlickException {				
		addComponent(new ImageRenderComponent(new Image(DBOX)));
	}
}
