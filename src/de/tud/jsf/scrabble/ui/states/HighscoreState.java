package de.tud.jsf.scrabble.ui.states;

import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import de.tud.jsf.scrabble.constants.GameParameters;
import de.tud.jsf.scrabble.model.highscore.Highscore;
import de.tud.jsf.scrabble.model.highscore.HighscoreList;
import eea.engine.action.Action;
import eea.engine.action.basicactions.ChangeStateAction;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;
import eea.engine.entity.StateBasedEntityManager;
import eea.engine.event.ANDEvent;
import eea.engine.event.basicevents.MouseClickedEvent;
import eea.engine.event.basicevents.MouseEnteredEvent;

public class HighscoreState extends BasicGameState implements GameParameters {
	private int stateID; 			
	private StateBasedEntityManager entityManager; 
	private HighscoreList highscoreList;
	
	
    
    HighscoreState(int sid){
    	stateID = sid;
    	entityManager = StateBasedEntityManager.getInstance();
    	
    	this.highscoreList = HighscoreList.getInstance();
    	highscoreList.load();
    }
    
    

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		// TODO Auto-generated method stub
		// Setup background
		setBackground();
		// Return to main menu button
		Entity back = new Entity("Back");
    	back.setPosition(new Vector2f(WINDOW_WIDTH-100,WINDOW_HEIGHT - 100));
    	back.setScale(0.3f);
    	back.addComponent(new ImageRenderComponent(new Image(DBOX)));    	
    	ANDEvent backToMenu = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
    	Action backToMenuAction = new ChangeStateAction(Launch.MAINMENU_STATE);
    	backToMenu.addAction(backToMenuAction);
    	back.addComponent(backToMenu);
    	entityManager.addEntity(stateID,back);
    	
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		// TODO Auto-generated method stub
		entityManager.renderEntities(container,game,g);
		List<Highscore> highscores = highscoreList.getHighscores();
		
		float x = 150;
		float y =275;
		
		g.drawString("Name", x, y - 50);
		g.drawString("Score", x + 225, y - 50);
		g.drawString("Round won", x + 385, y - 50);
		
		if (highscores.isEmpty()) g.drawString("No highscores found.",x+40,y+50);
		int i = 1;
		for (Highscore hsc : highscores) {
			// Position
			g.drawString(String.valueOf(i) + ".", x - 40, y);
			// Name
			g.drawString(hsc.getName() , x, y); 
			// Score
			g.drawString(((Integer)hsc.getScore()).toString(), x + 250, y);
			// Round
			g.drawString(((Integer)hsc.getRound()).toString(), x + 375, y);
			i++;
			y += 35;
		}
		g.drawString("Back", WINDOW_WIDTH-118,WINDOW_HEIGHT - 110);
		
		
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		entityManager.updateEntities(container, game, delta);
		
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return stateID;
	}
	
	private void setBackground() throws SlickException {
		Entity background = new Entity("background");	
    	background.setPosition(new Vector2f(480,360));
    	background.addComponent(new ImageRenderComponent(new Image(MAINMENU)));
    	entityManager.addEntity(stateID, background);
	}
	
	

}
