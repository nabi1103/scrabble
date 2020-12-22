package de.tud.jsf.scrabble.ui.entity;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import de.tud.jsf.scrabble.constants.GameParameters;

import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;

public class DialogueBox extends Entity implements GameParameters{
	
	String id;
	Vector2f pos;
	float offset;
	DialogueButton[] buttons = new DialogueButton[2];

	public DialogueBox(String entityID, Vector2f startPos, float offset) {
		super(entityID);
		
		this.id = entityID;
		this.pos = startPos;
		this.offset = offset;
		
		this.setPosition(pos);
		this.setVisible(true);
		
		this.addButton();
		this.addImageComponent();
		
	}
	
	public void addButton() {
		float start_X = this.getPosition().getX();
		float start_Y = this.getPosition().getY();
		
		DialogueButton confirm = new DialogueButton(this.getID() + "_confirm", new Vector2f(start_X - this.getOffset(), start_Y + this.getOffset()), "confirm");
		confirm.addDialogueBox(this);
		DialogueButton cancel = new DialogueButton(this.getID() + "_cancel", new Vector2f(start_X + this.getOffset(), start_Y + this.getOffset()), "cancel");
		cancel.addDialogueBox(this);
				
		buttons[0] = confirm;
		buttons[1] = cancel;
	}
	
	public DialogueButton[] getButtons() {
		return buttons;
	}
	
	public float getOffset() {
		return offset;
	}
	
	public ImageRenderComponent addImageComponent() {
		ImageRenderComponent image = null;
		
		try {
			image = new ImageRenderComponent(new Image(DBOX));
			this.addComponent(image);
		} catch (SlickException e) {
			System.err.println("Cannot find file " + DBOX);
			e.printStackTrace();
		}
		
		for (DialogueButton b : this.getButtons()) {
			b.addImageComponent();
		}
		
		return image; 
	}
}
