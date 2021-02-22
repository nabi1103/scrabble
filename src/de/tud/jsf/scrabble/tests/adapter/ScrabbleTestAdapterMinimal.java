package de.tud.jsf.scrabble.tests.adapter;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import de.tud.jsf.scrabble.ui.states.Launch;
import eea.engine.action.Action;
import eea.engine.entity.Entity;
import eea.engine.entity.StateBasedEntityManager;
import eea.engine.test.TestAppGameContainer;
import de.tud.jsf.scrabble.ui.entity.Letter;
import de.tud.jsf.scrabble.ui.states.GameplayState;

// REMOVE THESE IMPORTS

import de.tud.jsf.scrabble.constants.GameParameters;
import de.tud.jsf.scrabble.model.player.Player;
import de.tud.jsf.scrabble.model.player.Players;


// PLAYER SELECT, PLACING LETTERS, UNDO, PLAY
public class ScrabbleTestAdapterMinimal {
	
	Launch scrabble;
	TestAppGameContainer app;
	
	public ScrabbleTestAdapterMinimal() {
		super();
		scrabble = null;
	}
	
	public StateBasedGame getStateBasedGame() {
		return scrabble;
	}
	
	public void initGame() {
		if (System.getProperty("os.name").toLowerCase().contains("windows")) {
    		System.setProperty("org.lwjgl.librarypath",System.getProperty("user.dir") + "/native/windows");
		} else if (System.getProperty("os.name").toLowerCase().contains("mac")) {
    		System.setProperty("org.lwjgl.librarypath",System.getProperty("user.dir") + "/native/macosx");
    	} else {
    		System.setProperty("org.lwjgl.librarypath",System.getProperty("user.dir") + "/native/" +System.getProperty("os.name").toLowerCase());
    	}
		System.setProperty("org.lwjgl.opengl.Display.allowSoftwareOpenGL", "true");
		
		scrabble = new Launch(true);
		
		try {
			app = new TestAppGameContainer(scrabble);
			app.start(0);
		} 
		catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	public void stopGame() {
		if (app != null) {
			app.exit();
			app.destroy();
		}
		StateBasedEntityManager.getInstance().clearAllStates();
	}
	
	public void runGame(int ms) {
		if (scrabble != null && app != null) {
			try {
				app.updateGame(ms);
			} catch (SlickException e) {
				e.printStackTrace();
			}
		}
	}
	
	public int getMainMenuStateID() {
		// TODO
		return GameParameters.MAINMENU_STATE;
	}
	
	public int getNewgameButtonX() {
		// TODO
		List<Entity> entities = new ArrayList<Entity>();
		if (scrabble != null) 
			entities = StateBasedEntityManager.getInstance().getEntitiesByState(getMainMenuStateID());
		for (Entity e : entities) {
			if (e.getID() == "newgame") {
				return (int)e.getPosition().getX();
			}
		}
		return -1;
	}
	
	public int getNewgameButtonY() {
		// TODO
		List<Entity> entities = new ArrayList<Entity>();
		if (scrabble != null) 
			entities = StateBasedEntityManager.getInstance().getEntitiesByState(getMainMenuStateID());
		for (Entity e : entities) {
			if (e.getID() == "newgame") {
				return (int)e.getPosition().getY();
			}
		}
		return -1;
	}
	public int getGameplayStateID() {
		// TODO
		return GameParameters.GAMEPLAY_STATE;
	}
	
	public int getCurrentNumberOfPlayers() {
		// TODO
		return Players.getNumberOfPlayers();
	}
	
	public int getCurrentRound() {
		// TODO
		return ((GameplayState) scrabble.getState(getGameplayStateID())).getCurrentTurn() + 1;
	}
	
	public String getNameOfCurrentPlayer() {
		// TODO
		return Players.currentPlayer.getName();
	}
	
	public int getScoreOfCurrentPlayer() {
		// TODO
		return Players.currentPlayer.getScore();
	}
	
	public Map<String,Integer> getPlayerData() {
		HashMap<String,Integer> playerData = new HashMap<>();
		for (Player p : Players.getPlayers()) {
			playerData.put(p.getName() , p.getScore());
		}
		return playerData;
	}
	
	public List<String> getPlayerNames() {
		List<String> res = new ArrayList<String>();
		for (Player p : Players.getPlayers()) {
			res.add(p.getName());
		}
		return res;
	}
	
	public String getNameOfNextPlayer() {
		return Players.getPlayers().get((Players.currentPlayer.getID() + 1) % Players.getNumberOfPlayers()).getName();
	}
	
	public int getScoreOfNextPlayer() {
		return Players.getPlayers().get((Players.currentPlayer.getID() + 1) % Players.getNumberOfPlayers()).getScore();
	}
	
	public Vector2f getPlayButtonPosition() {
		List<Entity> entities = new ArrayList<Entity>();
		if (scrabble != null) 
			entities = StateBasedEntityManager.getInstance().getEntitiesByState(getGameplayStateID());
		for (Entity e : entities) {
			if (e.getID() == "play_button") {
				return e.getPosition();
			}
		}
		return null;
	}
	
	/*public int getPlayButtonX() {
		List<Entity> entities = new ArrayList<Entity>();
		if (scrabble != null) 
			entities = StateBasedEntityManager.getInstance().getEntitiesByState(getGameplayStateID());
		for (Entity e : entities) {
			if (e.getID() == "play_button") {
				return (int) e.getPosition().getX();
			}
		}
		return -1;
	}
	
	public int getPlayButtonY() {
		List<Entity> entities = new ArrayList<Entity>();
		if (scrabble != null) 
			entities = StateBasedEntityManager.getInstance().getEntitiesByState(getGameplayStateID());
		for (Entity e : entities) {
			if (e.getID() == "play_button") {
				return (int) e.getPosition().getY();
			}
		}
		return -1;
	}*/
	
	public Vector2f getUndoButtonPosition() {
		List<Entity> entities = new ArrayList<Entity>();
		if (scrabble != null) 
			entities = StateBasedEntityManager.getInstance().getEntitiesByState(getGameplayStateID());
		for (Entity e : entities) {
			if (e.getID() == "undo_button") {
				return e.getPosition();
			}
		}
		return null;
	}
	
	
	public int getCurrentInventorySize() {
		// TODO
		return Players.currentPlayer.getLetters().size();
	}
		
	public char[][] getBoard() {
		// TODO
		return ((GameplayState) scrabble.getState(getGameplayStateID())).getCharGrid();
	}
	
	public Vector2f getFieldPosition(int rows, int column) {
		return ((GameplayState) scrabble.getState(getGameplayStateID())).getTiles()[rows-1][column-1].getPosition();
	}
	
	/*public int getBoardY(int rows) {
		return (int) ((GameplayState) scrabble.getState(getGameplayStateID())).getTiles()[rows-1][0].getPosition().getY();
	}
	
	public int getBoardX(int column) {
		return (int) ((GameplayState) scrabble.getState(getGameplayStateID())).getTiles()[0][column-1].getPosition().getX();
	}*/
	
	public List<Vector2f> getLettersInInventoryPosition() {
		List<Entity> entities = new ArrayList<Entity>();
		List<Vector2f> res = new ArrayList<Vector2f>();
		if (scrabble != null) 
			entities = StateBasedEntityManager.getInstance().getEntitiesByState(getGameplayStateID());
		for (Entity e : entities) {
			if (e instanceof Letter) {
				if (! ((Letter) e).isOnBoard()) res.add(e.getPosition());
			}
		}
		return res;
	}
	
	public List<Vector2f> getNonBlankLettersInInventoryPosition() {
		List<Entity> entities = new ArrayList<Entity>();
		List<Vector2f> res = new ArrayList<Vector2f>();
		if (scrabble != null) 
			entities = StateBasedEntityManager.getInstance().getEntitiesByState(getGameplayStateID());
		for (Entity e : entities) {
			if (e instanceof Letter) {
				if (!((Letter) e).isOnBoard() && !isLetterBlank(e.getPosition())) res.add(e.getPosition());
			}
		}
		return res;
	}
	
	/*public List<Integer> getLettersInInventoryX() {
		List<Entity> entities = new ArrayList<Entity>();
		List<Integer> letters = new ArrayList<Integer>();
		if (scrabble != null) 
			entities = StateBasedEntityManager.getInstance().getEntitiesByState(getGameplayStateID());
		for (Entity e : entities) {
			if (e instanceof Letter) {
				letters.add((int) e.getPosition().getX());
			}
		}
		return letters;
	}
	
	public List<Integer> getLettersInInventoryY() {
		List<Entity> entities = new ArrayList<Entity>();
		List<Integer> letters = new ArrayList<Integer>();
		if (scrabble != null) 
			entities = StateBasedEntityManager.getInstance().getEntitiesByState(getGameplayStateID());
		for (Entity e : entities) {
			if (e instanceof Letter) {
				letters.add((int) e.getPosition().getY());
			}
		}
		return letters;
	}*/
	
	public char getLetter(Vector2f pos) {
		List<Entity> entities = new ArrayList<Entity>();
		if (scrabble != null) 
			entities = StateBasedEntityManager.getInstance().getEntitiesByState(getGameplayStateID());
		for (Entity e : entities) {
			if (e.getPosition().getX() == pos.getX()
					&& e.getPosition().getY() == pos.getY() && e instanceof Letter) 
				return ((Letter)e).getValue();
		}
		return '\u0000';
	}
	
	public boolean isLetterBlank(Vector2f pos) {
		// DELETE THIS?
		char letter = getLetter(pos);
		if (letter == '_') return true;
		return false;
	}
	

	
	public int getLetterScore(char letter) {
		return ((GameplayState) scrabble.getState(getGameplayStateID())).getLetterScore(letter); 
	}
	
	// PLAYER SELECT SECTION
	public int getPlayerSelectStateID() {
		// TODO
		return GameParameters.PLAYER_SELECT_STATE;
	}
	public int getStartButtonX() {
		// TODO
		List<Entity> entities = new ArrayList<Entity>();
		if (scrabble != null) 
			entities = StateBasedEntityManager.getInstance().getEntitiesByState(getPlayerSelectStateID());
		for (Entity e : entities) {
			if (e.getID() == "Start") {
				return (int)e.getPosition().getX();
			}
		}
		return -1;
	}
	
	public int getStartButtonY() {
		// TODO
		List<Entity> entities = new ArrayList<Entity>();
		if (scrabble != null) 
			entities = StateBasedEntityManager.getInstance().getEntitiesByState(getPlayerSelectStateID());
		for (Entity e : entities) {
			if (e.getID() == "Start") {
				return (int)e.getPosition().getY();
			}
		}
		return -1;
	}
	
	public int getBackButtonX() {
		// TODO
		List<Entity> entities = new ArrayList<Entity>();
		if (scrabble != null) 
			entities = StateBasedEntityManager.getInstance().getEntitiesByState(getPlayerSelectStateID());
		for (Entity e : entities) {
			if (e.getID() == "Back") {
				return (int)e.getPosition().getX();
			}
		}
		return -1;
	}
	
	public int getBackButtonY() {
		// TODO
		List<Entity> entities = new ArrayList<Entity>();
		if (scrabble != null) 
			entities = StateBasedEntityManager.getInstance().getEntitiesByState(getPlayerSelectStateID());
		for (Entity e : entities) {
			if (e.getID() == "Back") {
				return (int)e.getPosition().getY();
			}
		}
		return -1;
	}
	
	public List<Integer> getPlayerSelectFieldsX() {
		// TODO
		ArrayList<Integer> result = new ArrayList<Integer>();
		List<Entity> entities = new ArrayList<Entity>();
		if (scrabble != null) 
			entities = StateBasedEntityManager.getInstance().getEntitiesByState(getPlayerSelectStateID());
		for (Entity e : entities) {
			if (e.getID() == "PlayerSelect") {
				result.add( (int)e.getPosition().getX());
			}
		}
		return result;
	}
	
	public List<Integer> getPlayerSelectFieldsY() {
		ArrayList<Integer> result = new ArrayList<Integer>();
		List<Entity> entities = new ArrayList<Entity>();
		if (scrabble != null) 
			entities = StateBasedEntityManager.getInstance().getEntitiesByState(getPlayerSelectStateID());
		for (Entity e : entities) {
			if (e.getID() == "PlayerSelect") {
				result.add( (int)e.getPosition().getY());
			}
		}
		return result;
	}
	
	public void handleMousePressed(int x , int y,int delta,int input) {
		if (scrabble != null && app != null) {
			app.getTestInput().setMouseX(x);
			app.getTestInput().setMouseY(y);
			app.getTestInput().setMouseButtonPressed(input);
			try {
				app.updateGame(delta);
			} catch (SlickException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void handleMousePressed(Vector2f pos,int delta,int input) {
		if (scrabble != null && app != null && pos!= null) {
			app.getTestInput().setMouseX((int)pos.getX());
			app.getTestInput().setMouseY((int)pos.getY());
			app.getTestInput().setMouseButtonPressed(input);
			try {
				app.updateGame(delta);
			} catch (SlickException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void testing() {
		// REMOVE THIS
		System.out.println("TESTING..");
		List<Entity> entities = new ArrayList<Entity>();
		if (scrabble != null) 
			entities = StateBasedEntityManager.getInstance().getEntitiesByState(getGameplayStateID());
		for (Entity e : entities) {
			if (e.getID() == "test_button") {
				int x = (int)e.getPosition().getX();
				int y = (int)e.getPosition().getY();
				handleMousePressed(x,y,0,0);
			}
		}
	}
	
	
	
	
		
	

}
