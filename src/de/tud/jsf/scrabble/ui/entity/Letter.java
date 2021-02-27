package de.tud.jsf.scrabble.ui.entity;

import org.newdawn.slick.geom.Vector2f;

import java.util.Set;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;
import de.tud.jsf.scrabble.constants.GameParameters;
import de.tud.jsf.scrabble.ui.states.Launch;
import eea.engine.action.Action;

public class Letter extends Entity implements GameParameters {

	String id;
	char value;
	int score;
	int rotation;
	String tile_id;
	ImageRenderComponent image;
	Action move_direction;
	float speed;

	boolean onBoard;

	public Letter(String entityID, char letter, Vector2f pos, boolean onBoard) {
		super(entityID);

		Set<Character> alphabet = Set.of('_', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o',
				'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z');

		if (!alphabet.contains(letter)) {
			throw new IllegalArgumentException("Can only be '_' or a letter in the English alphabet");
		}

		this.value = letter;
		this.id = entityID;
		this.onBoard = onBoard;

		if (!Launch.debug)
			this.addImageComponent();
		this.setScale(LETTER_SCALE_FACTOR);
		this.setVisible(true);
		this.setSize(new Vector2f(256 * LETTER_SCALE_FACTOR, 256 * LETTER_SCALE_FACTOR));
		this.setPosition(pos);
	}

	public boolean isOnBoard() {
		return onBoard;
	}

	public char getValue() {
		return value;
	}
	
	public void setIntRotation(int r) {
		this.rotation = r;
	}
	
	public int getIntRotation() {
		return this.rotation;
	}
	
	public void setSpeed(float r) {
		this.speed = r;
	}
	
	public float getSpeed() {
		return this.speed;
	}

	public ImageRenderComponent addImageComponent() {
		image = null;

		for (String path : LETTERS) {
			char image_id;
			image_id = path.charAt(30);
			if (this.value == Character.toLowerCase(image_id)) {
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

	public ImageRenderComponent addBlankImageComponent(char c) {
		this.removeComponent(image);

		for (String path : LETTERS) {
			char image_id;
			image_id = path.charAt(30);
			if (c == Character.toLowerCase(image_id)) {
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
