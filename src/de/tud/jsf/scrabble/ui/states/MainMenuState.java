package de.tud.jsf.scrabble.ui.states;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import eea.engine.action.Action;
import eea.engine.action.basicactions.ChangeStateInitAction;
import eea.engine.action.basicactions.QuitAction;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;
import eea.engine.entity.StateBasedEntityManager;
import eea.engine.event.ANDEvent;
import eea.engine.event.basicevents.MouseClickedEvent;
import eea.engine.event.basicevents.MouseEnteredEvent;


public class MainMenuState extends BasicGameState {
	private int stateID; 			
	private StateBasedEntityManager entityManager; 	//
	
	private final int distance = 100;
    private final int start_Position = 180;
    
    MainMenuState(int sid){
    	stateID = sid;
    	entityManager = StateBasedEntityManager.getInstance();
    }
    
    
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
    	Entity background = new Entity("menu");	
    	background.setPosition(new Vector2f(480,360));
    	background.addComponent(new ImageRenderComponent(new Image("/assets/menu.png")));
    	    	
    	entityManager.addEntity(stateID, background);
    	
    	String new_Game = "New Game";
    	Entity new_Game_Entity = new Entity(new_Game);
    	
    	new_Game_Entity.setPosition(new Vector2f(218, 190));
    	new_Game_Entity.setScale(0.28f);
    	new_Game_Entity.addComponent(new ImageRenderComponent(new Image("assets/entry.png")));
    	
    	ANDEvent mainEvents = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
    	Action new_Game_Action = new ChangeStateInitAction(Launch.GAMEPLAY_STATE);
    	mainEvents.addAction(new_Game_Action);
    	new_Game_Entity.addComponent(mainEvents);
    	
    	entityManager.addEntity(this.stateID, new_Game_Entity);
    	
    	Entity quit_Entity = new Entity("Quit");
    	
    	quit_Entity.setPosition(new Vector2f(218, 290));
    	quit_Entity.setScale(0.28f);
    	quit_Entity.addComponent(new ImageRenderComponent(new Image("assets/entry.png")));
    	
    	ANDEvent mainEvents_q = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
    	Action quit_Action = new QuitAction();
    	mainEvents_q.addAction(quit_Action);
    	quit_Entity.addComponent(mainEvents_q);
    	
    	entityManager.addEntity(this.stateID, quit_Entity);
	}
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics graphic) throws SlickException {
		entityManager.renderEntities(container, game, graphic);
		
		int counter = 0;
		
		graphic.drawString("New Game", 110, start_Position+counter*distance); counter++;
		graphic.drawString("Quit", 110, start_Position+counter*distance); counter++;
		
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
