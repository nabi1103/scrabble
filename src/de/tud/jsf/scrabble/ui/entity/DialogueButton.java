package de.tud.jsf.scrabble.ui.entity;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import de.tud.jsf.scrabble.constants.GameParameters;
import eea.engine.component.render.ImageRenderComponent;

import eea.engine.entity.Entity;

public class DialogueButton extends Entity implements GameParameters{
	String id;
	Vector2f pos;
	String type; // "confirm", "cancel"
	DialogueBox parent;

	public DialogueButton(String entityID, Vector2f startPos, String type) {
		super(entityID);
		
		this.id = entityID;
		this.pos = startPos;
		this.type = type;
		
		this.setVisible(true);
		this.setPosition(pos);
		
		this.setSize(new Vector2f(38, 36));
	}
	
	public void addDialogueBox(DialogueBox b) {
		this.parent = b;
	}
	
	public String getType() {
		return type;
	}
	
	public DialogueBox getDialogueBox() {
		return parent;
	}
	
	public ImageRenderComponent addImageComponent() {
		ImageRenderComponent image = null;
		
		switch (this.getType()) {
		case "confirm":
			try {
				image = new ImageRenderComponent(new Image(DBOX_YES));
				this.addComponent(image);
			} catch (SlickException e) {
				System.err.println("Cannot find file " + DBOX_YES);
				e.printStackTrace();
			}
			break;
		case "cancel":
			try {
				image = new ImageRenderComponent(new Image(DBOX_NO));
				this.addComponent(image);
			} catch (SlickException e) {
				System.err.println("Cannot find file " + DBOX_NO);
				e.printStackTrace();
			}
			break;
		
		case "commit":
			try {
				image = new ImageRenderComponent(new Image(COMMIT_BUTTON));
				this.addComponent(image);
			} catch (SlickException e) {
				System.err.println("Cannot find file " + COMMIT_BUTTON);
				e.printStackTrace();
			}
			break;
			
		case "undo":
			try {
				image = new ImageRenderComponent(new Image(UNDO_BUTTON));
				this.addComponent(image);
			} catch (SlickException e) {
				System.err.println("Cannot find file " + UNDO_BUTTON);
				e.printStackTrace();
			}
			break;
			
		case "check":
			try {
				image = new ImageRenderComponent(new Image(CHECK_BUTTON));
				this.addComponent(image);
			} catch (SlickException e) {
				System.err.println("Cannot find file " + CHECK_BUTTON);
				e.printStackTrace();
			}
			break;
			
		case "trade":
			try {
				image = new ImageRenderComponent(new Image(TRADE_BUTTON));
				this.addComponent(image);
			} catch (SlickException e) {
				System.err.println("Cannot find file " + TRADE_BUTTON);
				e.printStackTrace();
			}
			break;
			
		}
		
		
		
		return image; 
	}
}
