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
import de.tud.jsf.scrabble.ui.entity.DialogueButton;
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
	
	// MAIN MENU SECTION
	
	public Vector2f getNewgameButtonPosition() {
		List<Entity> entities = new ArrayList<Entity>();
		if (scrabble != null) 
			entities = StateBasedEntityManager.getInstance().getEntitiesByState(getMainMenuStateID());
		for (Entity e : entities) {
			if (e.getID() == "new_game_button") {
				return e.getPosition();
			}
		}
		return null;
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
	
	public Vector2f getShowButtonPosition() {
		// TODO
		List<Entity> entities = new ArrayList<Entity>();
		if (scrabble != null) 
			entities = StateBasedEntityManager.getInstance().getEntitiesByState(getGameplayStateID());
		for (Entity e : entities) {
			if (e.getID() == "show_button") {
				return e.getPosition();
			}
		}
		return null;
	}
	
	public boolean areLettersHidden() {
		List<Entity> entities = new ArrayList<Entity>();
		if (scrabble != null) 
			entities = StateBasedEntityManager.getInstance().getEntitiesByState(getGameplayStateID());
		for (Entity e : entities) {
			if (e.getID() == "show_button") {
				if ((((DialogueButton)e).getType()) == "show") return true;
			}
		}
		return false;
	}	
	
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
				char letter = ((Letter) e).getValue();
				if (!((Letter) e).isOnBoard() && !isLetterBlank(letter)) res.add(e.getPosition());
			}
		}
		return res;
	}
	
	public int getRemainingLettersInBag() {
		return GameplayState.bag_of_letters.size();
	}
	
	
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
	
	public boolean isLetterBlank(char letter) {
		// TODO
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
	
	
	public Vector2f getStartButtonPosition() {
		List<Entity> entities = new ArrayList<Entity>();
		if (scrabble != null) 
			entities = StateBasedEntityManager.getInstance().getEntitiesByState(getPlayerSelectStateID());
		for (Entity e : entities) {
			if (e.getID() == "start_button") {
				return e.getPosition();
			}
		}
		return null;
	}

	
	public Vector2f getBackButtonPosition() {
		List<Entity> entities = new ArrayList<Entity>();
		if (scrabble != null) 
			entities = StateBasedEntityManager.getInstance().getEntitiesByState(getPlayerSelectStateID());
		for (Entity e : entities) {
			if (e.getID() == "back_button") {
				return e.getPosition();
			}
		}
		return null;
	}

	
	public List<Vector2f> getPlayerSelectFieldsPosition() {
		ArrayList<Vector2f> result = new ArrayList<Vector2f>();
		List<Entity> entities = new ArrayList<Entity>();
		if (scrabble != null) 
			entities = StateBasedEntityManager.getInstance().getEntitiesByState(getPlayerSelectStateID());
		for (Entity e : entities) {
			if (e.getID() == "PlayerSelect") {
				result.add(e.getPosition());
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
				break;
			}
		}
	}
	
	
	
	
		
	

}
