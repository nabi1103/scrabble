package de.tud.jsf.scrabble.ui.states;

import java.util.LinkedList;
import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import de.tud.jsf.scrabble.constants.GameParameters;
import eea.engine.action.Action;
import eea.engine.action.basicactions.ChangeStateAction;
import eea.engine.action.basicactions.QuitAction;
import eea.engine.component.Component;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;
import eea.engine.entity.StateBasedEntityManager;
import eea.engine.event.ANDEvent;
import eea.engine.event.basicevents.MouseClickedEvent;
import eea.engine.event.basicevents.MouseEnteredEvent;

import de.tud.jsf.scrabble.model.player.*;
import de.tud.jsf.scrabble.ui.entity.ChangeNameButton;
import de.tud.jsf.scrabble.ui.entity.DialogueButton;


public class PlayerSelectState extends BasicGameState implements GameParameters{
	private int stateID; 			
	private StateBasedEntityManager entityManager; 
	
	int numberOfPlayers;
	private String statusBar;
	
	private final int distance = 100;
    private final int start_Position = 180;
    
    private final int CENTERX = WINDOW_WIDTH / 2;
    private final int CENTERY = WINDOW_HEIGHT / 2;
    private final int DISTANCE_BETWEEN_BUTTONS = 75;
    
    ChangeNameButton[] playerBoxes;
//    Entity start;
//    Entity back;
    
    PlayerSelectState(int sid){
    	stateID = sid;
    	entityManager = StateBasedEntityManager.getInstance();
    }
    
    public void selectPlayer(ChangeNameButton playerSelectBox) {
    	String name = playerSelectBox.getName();
    	if (name == "") { // This box is empty, add new player
    		numberOfPlayers++;
    		name = "PLAYER";
    	}
    	else {
    		numberOfPlayers --;
    		name = "";
    	}
    	playerSelectBox.setName(name);
    }
    public boolean initPlayers() {
    	Players.resetPlayers();

    	if (numberOfPlayers <= 1) {
    		statusBar = " Not enough players to start the game!";
    		return false;
    	}
    	for (int i = 0; i < numberOfPlayers ; i ++) {
    		Players.addPlayer(new Player("" + (i + 1), i));
    	}
    	
    	Players.initFirstPlayer(); 
    	return true;
    }

    
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		// Init variables
		numberOfPlayers = 0;
		statusBar = "";
		playerBoxes = new ChangeNameButton[4];
    	Entity background = new Entity("background");	
    	background.setPosition(new Vector2f(480,360));
    	if (!Launch.debug) background.addComponent(new ImageRenderComponent(new Image(MAINMENU)));
    	entityManager.addEntity(stateID, background);
    	
    	// Start the game
//    	start = new Entity("Start");
//    	start.setPosition(new Vector2f(CENTERX - DISTANCE_BETWEEN_BUTTONS,WINDOW_HEIGHT - 100));
//    	start.setScale(0.2f);
//    	start.setSize(new Vector2f(400/2,200/2));
//    	if (!Launch.debug) start.addComponent(new ImageRenderComponent(new Image(DBOX)));  
    	
    	DialogueButton start = new DialogueButton("start_button", new Vector2f(CENTERX - DISTANCE_BETWEEN_BUTTONS,WINDOW_HEIGHT - 100), "start");
    	
    	ANDEvent startEvent = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
    	startEvent.addAction(new Action() {
			@Override
			public void update(GameContainer arg0, StateBasedGame arg1, int arg2, Component arg3) {
				if (initPlayers()) {
					arg1.enterState(arg1.getState(GAMEPLAY_STATE).getID());
					((GameplayState)arg1.getState(GAMEPLAY_STATE)).setLastStateID(stateID);
				}
			}				
		});
    	start.addComponent(startEvent);
    	start.addImageComponent();
    	
    	// Return to main menu   	
//    	back = new Entity("Back");
//    	back.setPosition(new Vector2f(CENTERX + DISTANCE_BETWEEN_BUTTONS,WINDOW_HEIGHT - 100));
//    	back.setScale(0.2f);
//    	back.setSize(new Vector2f(400/2,200/2));
//    	if (!Launch.debug) back.addComponent(new ImageRenderComponent(new Image(DBOX)));    
    	
    	DialogueButton back = new DialogueButton("back_button", new Vector2f(CENTERX + DISTANCE_BETWEEN_BUTTONS,WINDOW_HEIGHT - 100), "back");
    	
    	ANDEvent backToMenu = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
    	Action backToMenuAction = new ChangeStateAction(Launch.MAINMENU_STATE);
    	Action resetState = new Action() {
			@Override
			public void update(GameContainer arg0, StateBasedGame arg1, int arg2, Component arg3) {
				numberOfPlayers = 0;
				statusBar = "";
				for (int i = 0 ; i < playerBoxes.length ; i ++) playerBoxes[i].setName("");
			}    		
    	};
    	backToMenu.addAction(backToMenuAction);
    	backToMenu.addAction(resetState);
    	
    	back.addComponent(backToMenu);
    	back.addImageComponent();
    	
    	// Player boxes
    	
    	for (int i = 0 ; i < playerBoxes.length ; i ++) {		
    		playerBoxes[i] = new ChangeNameButton("PlayerSelect",CENTERX, CENTERY - 50 +i*80, 0.3f, "");
    		playerBoxes[i].setSize(new Vector2f(400/3,200/3));
        	ANDEvent selectPlayerEvent = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());        
        	selectPlayerEvent.addAction(createPlayerSelectAction(i));
        	playerBoxes[i].addComponent(selectPlayerEvent);
        	entityManager.addEntity(stateID, playerBoxes[i]);
    	}    	    	
    	entityManager.addEntity(stateID, start);
    	entityManager.addEntity(stateID, back);
    	    	
    	
	}
	private Action createPlayerSelectAction(int i) {
		return new Action() {
			@Override
			public void update(GameContainer arg0, StateBasedGame arg1, int arg2, Component arg3) {
				selectPlayer(playerBoxes[i]);
			}			
		};
	}
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics graphic) throws SlickException {
		entityManager.renderEntities(container, game, graphic);
		graphic.setColor(new Color(0, 0, 0));
//		graphic.drawString("Start", start.getPosition().getX() - 17,start.getPosition().getY() - 10);
//		graphic.drawString("Back", back.getPosition().getX() - 15,back.getPosition().getY() - 10);
		for (int i = 0; i < playerBoxes.length ; i ++) {
			graphic.drawString(playerBoxes[i].getName(),playerBoxes[i].getPosition().getX() - 25, playerBoxes[i].getPosition().getY() - 10);
		}	
//		graphic.drawString("Players: " + numberOfPlayers, back.getPosition().getX(),back.getPosition().getY() + 70);
		graphic.setColor(new Color(255,0,0));
//		graphic.drawString(statusBar, back.getPosition().getX(),back.getPosition().getY() + 50);
	}
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		entityManager.updateEntities(container, game, delta);

	}
	
	@Override
	public int getID() {
		return stateID;
	}


}
