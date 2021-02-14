package de.tud.jsf.scrabble.ui.states;

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
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;
import eea.engine.entity.StateBasedEntityManager;
import eea.engine.event.ANDEvent;
import eea.engine.event.basicevents.MouseClickedEvent;
import eea.engine.event.basicevents.MouseEnteredEvent;


public class MainMenuState extends BasicGameState implements GameParameters{
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
    	if (!Launch.debug)background.addComponent(new ImageRenderComponent(new Image(MAINMENU)));
    	entityManager.addEntity(stateID, background);
    	
    	Entity new_Game_Entity = new Entity("newgame");
    	
    	new_Game_Entity.setPosition(new Vector2f(145, 190));
    	new_Game_Entity.setScale(0.3f);
    	new_Game_Entity.setSize(new Vector2f(400/3,200/3));
    	if (!Launch.debug)new_Game_Entity.addComponent(new ImageRenderComponent(new Image(DBOX)));
    	
    	ANDEvent mainEvents = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
    	Action new_Game_Action = new ChangeStateAction(Launch.PLAYER_SELECT_STATE);
    	mainEvents.addAction(new_Game_Action);
    	new_Game_Entity.addComponent(mainEvents);
    	
    	entityManager.addEntity(this.stateID, new_Game_Entity);
    	
    	Entity quit_Entity = new Entity("Quit");
    	
    	quit_Entity.setPosition(new Vector2f(145, 290));
    	quit_Entity.setScale(0.3f);
    	quit_Entity.setSize(new Vector2f(400/3,200/3));
    	if (!Launch.debug)quit_Entity.addComponent(new ImageRenderComponent(new Image(DBOX)));
    	
    	ANDEvent mainEvents_q = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
    	Action quit_Action = new QuitAction();
    	mainEvents_q.addAction(quit_Action);
    	quit_Entity.addComponent(mainEvents_q);
    	
    	entityManager.addEntity(this.stateID, quit_Entity);
    	Entity highscore = new Entity("highscore");
    	highscore.setPosition(new Vector2f(145, 390));
    	highscore.setScale(0.3f);
    	highscore.setSize(new Vector2f(400/3,200/3));
    	if (!Launch.debug) highscore.addComponent(new ImageRenderComponent(new Image(DBOX)));
    	
    	ANDEvent highscoreEvent= new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
    	Action goToHighscore = new ChangeStateAction(Launch.HIGHSCORE_STATE);
    	highscoreEvent.addAction(goToHighscore);
    	highscore.addComponent(highscoreEvent);
    	
    	entityManager.addEntity(stateID, highscore);
	}
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics graphic) throws SlickException {
		entityManager.renderEntities(container, game, graphic);
		graphic.setColor(new Color(0, 0, 0));
		
		int counter = 0;
		
		graphic.drawString("New game", 110, start_Position+counter*distance); counter++;
		graphic.drawString("Quit", 110, start_Position+counter*distance); counter++;
		graphic.drawString("Highscore", 110, start_Position+counter*distance); counter++;
		
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
