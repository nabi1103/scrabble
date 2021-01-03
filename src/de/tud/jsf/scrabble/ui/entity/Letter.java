package de.tud.jsf.scrabble.ui.entity;

import org.newdawn.slick.geom.Vector2f;

import java.util.Set;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;
import de.tud.jsf.scrabble.constants.GameParameters;


public class Letter extends Entity implements GameParameters{
	
	String id;
	char value;
	int score;
	Vector2f pos;
	String tile_id;
	
	public Letter(String entityID, char letter, Vector2f pos) {
		super(entityID);
		
		Set<Character> alphabet = Set.of('_','a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z');
		
		if(!alphabet.contains(letter)) {
			throw new IllegalArgumentException("Can only be '_' or a letter in the English alphabet");
		}
		
		this.value = letter;
		this.pos = pos;
		this.id = entityID;
		
		this.setPosition(pos);
		this.addImageComponent();
		this.setScale(LETTER_SCALE_FACTOR);
		this.setVisible(true);
	}
	
	public Vector2f getPosition() {
		return pos;
	}
	
	public char getValue() {
		return value;
	}
	
	public ImageRenderComponent addImageComponent() {
		ImageRenderComponent image = null;
		
		for (String path : LETTERS) {
			char image_id;
			image_id = path.charAt(30);
			if(this.value == Character.toLowerCase(image_id)) {
				try {
					image = new ImageRenderComponent(new Image(path));
					this.addComponent(image);
				} catch (SlickException e) {
					System.err.println("Cannot find file " + path);
					e.printStackTrace();
				}
				return image;
			}
		}
		return image;
	}


}
