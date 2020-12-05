package de.tud.jsf.scrabble.ui.entity;

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import de.tud.jsf.scrabble.constants.GameParameters;

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
	int mult;
	Vector2f pos;
	String type; // 'blank', 'dword', 'dletter', 'tword', 'tletter'
	ArrayList<String> neighbors = new ArrayList<>(); 

	public Tile(String entityID, String type, Vector2f pos) {
		super(entityID);
		
		if ( !(type.equals("blank") || type.equals("dword") || type.equals("dletter") || type.equals("tword") || type.equals("tletter")) ) {
			throw new IllegalArgumentException("Type can only be 'blank', 'dword', 'dletter', 'tword' or 'tletter'");
		}
		
		this.id = entityID;
		this.type = type;
		this.pos = pos;
		
		this.setMultiplier();
		this.setVisible(true);
		this.setPosition(pos);
		this.setScale(TILE_SCALE_FACTOR);
		this.setNeighbors();
		
		this.addImageComponent();
		
		//Test
		//this.addTestEvent();
	}
	
	public String getTileID() {
		return id;
	}
	
	public int getMultiplier() {
		return mult;
	}
	
	public Vector2f getPosition() {
		return pos;
	}
	
	public String getType() {
		return type;
	}
		
	public ArrayList<String> getNeighbors() {
		return neighbors;
	}
	
	public void setNeighbors() {
		int x = Integer.parseInt(id.split("_")[0]);
		int y = Integer.parseInt(id.split("_")[1]);
		
		int x_left = x - 1;
		int x_right = x + 1;
		int y_up = y - 1;
		int y_down = y + 1;
		
		ArrayList<Integer> coorX = new ArrayList<>();
		ArrayList<Integer> coorY = new ArrayList<>();
		
		if (x_left >= 0) coorX.add(x_left);
		if (x_right < BOARDSIZE) coorX.add(x_right);
		
		if (y_up >= 0) coorY.add(y_up);
		if (y_down < BOARDSIZE) coorY.add(y_down);
		
		for (int i = 0; i < coorX.size(); i++) {
			neighbors.add(coorX.get(i).toString() + "_" + Integer.toString(y));
	     } 
		
		for (int i = 0; i < coorY.size(); i++) {
			neighbors.add(Integer.toString(x) + "_" + coorY.get(i).toString());
	     }
	}
	
	public void setMultiplier() {
		switch (this.getType()) {
		case "blank":
			this.mult = 1;
			break;
		case "dword":
			this.mult = 2;
			break;
		case "dletter":
			this.mult = 2;
			break;
		case "tword":
			this.mult = 3;
			break;
		case "tletter":
			this.mult = 3;
			break;
		}
	}
	
	public Event addTestEvent() {
    	ANDEvent click = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		click.addAction(new Action() {
			@Override
			public void update(GameContainer gc, StateBasedGame sbg, int arg, Component c) {
				System.out.println(getTileID());
				for (String s : neighbors) {
					System.out.print(s + " ");
				}
				System.out.println("________________");
			}
		});
		this.addComponent(click);
		return click;
	}
	
	public ImageRenderComponent addImageComponent() {
		ImageRenderComponent image = null;
		
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

		}
		return image;
	}

}
