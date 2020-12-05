package de.tud.jsf.scrabble.ui.states;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import eea.engine.action.Action;
import eea.engine.component.Component;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;
import eea.engine.entity.StateBasedEntityManager;
import eea.engine.event.ANDEvent;
import eea.engine.event.basicevents.MouseClickedEvent;
import eea.engine.event.basicevents.MouseEnteredEvent;
import de.tud.jsf.scrabble.constants.GameParameters;
import de.tud.jsf.scrabble.ui.entity.Board;
import de.tud.jsf.scrabble.ui.entity.Tile;
import de.tud.jsf.scrabble.ui.entity.Letter;

public class GameplayState extends BasicGameState implements GameParameters{
	
	private int stateID; 				
	private StateBasedEntityManager entityManager;
	private boolean move_letter = false;
	private Letter tmp_letter = null;

	GameplayState( int sid ) {
        this.stateID = sid;
        entityManager = StateBasedEntityManager.getInstance();
     }
     
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		// Create new BACKGROUND entity & add to manager
		Entity background = new Entity("background");
		background.setPosition(new Vector2f(WINDOW_WIDTH / 2, WINDOW_HEIGHT / 2));
		background.addComponent(new ImageRenderComponent(new Image("assets/background.png")));
		StateBasedEntityManager.getInstance().addEntity(stateID, background);
		
		
		// Create new BOARD as TILEs and add to entity manager
		Board b = new Board(new Vector2f(BOARD_START_X,BOARD_START_y));
		Tile[][] tiles = b.buildBoard();
		for (int i = 0 ; i < BOARDSIZE ; i ++) {
			for (int j = 0 ; j < BOARDSIZE ; j ++) {
				StateBasedEntityManager.getInstance().addEntity(stateID, tiles[i][j]);
				moveLetterToBoardTile(tiles[i][j]);
			}
		}
		
		// Create LETTER
		Vector2f tv = new Vector2f(800, 650);
		Letter test = new Letter("test", '_', tv);
		moveLetterToBoardLetter(test);
		StateBasedEntityManager.getInstance().addEntity(stateID, test);
		
		Vector2f tv1 = new Vector2f(800, 700);
		Letter test1 = new Letter("test", 'a', tv1);
		moveLetterToBoardLetter(test1);
		StateBasedEntityManager.getInstance().addEntity(stateID, test1);
	
	}
	
	public void moveLetterToBoardLetter(Letter l) {
		ANDEvent clickEvent = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		Action clickOnLetter = new Action() {
			@Override
			public void update(GameContainer arg0, StateBasedGame arg1, int arg2, Component arg3) {
				if (move_letter) return;
				move_letter = true;
				tmp_letter = l;
				System.out.println("updating...");
			}
		};
		clickEvent.addAction(clickOnLetter);
		l.addComponent(clickEvent);
	}
	
	public void moveLetterToBoardTile(Tile t) {
		ANDEvent clickEvent = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		Action clickOnLetter = new Action() {
			@Override
			public void update(GameContainer arg0, StateBasedGame arg1, int arg2, Component arg3) {
				if (!move_letter) return;
				
				System.out.println("moving...");
				
				Letter new_letter = new Letter(tmp_letter.getID(), tmp_letter.getValue(), t.getPosition());
				StateBasedEntityManager.getInstance().removeEntity(stateID, entityManager.getEntity(stateID, tmp_letter.getID()));
				StateBasedEntityManager.getInstance().addEntity(stateID, new_letter);
				
				move_letter = false;
				tmp_letter = null;
				
				System.out.println("moving done!");
			}
		};
		clickEvent.addAction(clickOnLetter);
		t.addComponent(clickEvent);
	}
		
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics graphic) throws SlickException {
		entityManager.renderEntities(container, game, graphic);
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
