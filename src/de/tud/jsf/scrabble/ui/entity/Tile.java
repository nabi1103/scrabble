package de.tud.jsf.scrabble.ui.entity;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import de.tud.jsf.scrabble.constants.GameParameters;
import de.tud.jsf.scrabble.ui.states.Launch;
import eea.engine.action.Action;
import eea.engine.component.Component;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;
import eea.engine.event.ANDEvent;
import eea.engine.event.Event;
import eea.engine.event.basicevents.MouseClickedEvent;
import eea.engine.event.basicevents.MouseEnteredEvent;

public class Tile extends Entity implements GameParameters {
	
	String id;
	int letter_multiplier;
	int word_multiplier;
	Vector2f pos;
	String type; // 'blank', 'dword', 'dletter', 'tword', 'tletter', 'star'

	public Tile(String entityID, String type, Vector2f pos) {
		super(entityID);
		
		if ( !(type.equals("blank") || type.equals("dword") || type.equals("dletter") || type.equals("tword") || type.equals("tletter") || type.equals("star")) ) {
			throw new IllegalArgumentException("Type can only be 'blank', 'dword', 'dletter', 'tword' or 'tletter'");
		}
		
		this.id = entityID;
		this.type = type;
		this.pos = pos;
		
		this.setLetterMultiplier();
		this.setVisible(true);
		
		this.setPosition(pos);
		this.setScale(TILE_SCALE_FACTOR);
		this.setSize(new Vector2f(TILE_WIDTH,TILE_HEIGHT));
		
		this.setLetterMultiplier();
		this.setWordMultiplier();
		
		this.addImageComponent();
		
//		this.addTestEvent();
	}
	
	public int getLetterMultiplier() {
		return letter_multiplier;
	}
	
	public int getWordMultiplier() {
		return word_multiplier;
	}
	
	public Vector2f getPosition() {
		return pos;
	}
	
	public String getType() {
		return type;
	}
		
	
	public void setLetterMultiplier() {
		if (this.getType().equals("blank") || this.getType().equals("dword") || this.getType().equals("tword") || this.getType().equals("star")) {
			this.letter_multiplier = 1;
		}
		else if (this.getType().equals("dletter")) {
			this.letter_multiplier = 2;
		}
		else if (this.getType().equals("tletter")) {
			this.letter_multiplier = 3;
		}
	}
	
	public void setWordMultiplier() {
		if (this.getType().equals("blank") || this.getType().equals("dletter") || this.getType().equals("tletter")) {
			this.word_multiplier = 1;
		}
		else if (this.getType().equals("dword") || this.getType().equals("star")) {
			this.word_multiplier = 2;
		}
		else if (this.getType().equals("tword")) {
			this.word_multiplier = 3;
		}
	}
	
	public void clearMultiplier() {
		this.word_multiplier = 1;
		this.letter_multiplier = 1;
	}
	
	// Testing only
	public Event addTestEvent() {
    	ANDEvent click = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		click.addAction(new Action() {
			@Override
			public void update(GameContainer gc, StateBasedGame sbg, int arg, Component c) {
				System.out.println(getPosition());
			}
		});
		this.addComponent(click);
		return click;
	}
	
	public ImageRenderComponent addImageComponent() {
		ImageRenderComponent image = null;
		if (!Launch.debug)
		switch (this.getType()) {
		case "blank":
			try {
				image = new ImageRenderComponent(new Image(BLANK_TILE));
				this.addComponent(image);
			} catch (SlickException e) {
				System.err.println("Cannot find file " + BLANK_TILE);
				e.printStackTrace();
			}
			break;
		case "dletter":
			try {
				image = new ImageRenderComponent(new Image(CYAN_TILE));
				this.addComponent(image);
			} catch (SlickException e) {
				System.err.println("Cannot find file " + CYAN_TILE);
				e.printStackTrace();
			}
			break;

		case "dword":
			try {
				image = new ImageRenderComponent(new Image(PINK_TILE));
				this.addComponent(image);
			} catch (SlickException e) {
				System.err.println("Cannot find file " + PINK_TILE);
				e.printStackTrace();
			}
			break;

		case "tletter":
			try {
				image = new ImageRenderComponent(new Image(BLUE_TILE));
				this.addComponent(image);
			} catch (SlickException e) {
				System.err.println("Cannot find file " + BLUE_TILE);
				e.printStackTrace();
			}
			break;

		case "tword":
			try {
				image = new ImageRenderComponent(new Image(RED_TILE));
				this.addComponent(image);
			} catch (SlickException e) {
				System.err.println("Cannot find file " + RED_TILE);
				e.printStackTrace();
			}
			break;
		
		case "star":
			try {
				image = new ImageRenderComponent(new Image(STAR_TILE));
				this.addComponent(image);
			} catch (SlickException e) {
				System.err.println("Cannot find file " + STAR_TILE);
				e.printStackTrace();
			}
			break;	
			
		}
		return image;
	}

}
