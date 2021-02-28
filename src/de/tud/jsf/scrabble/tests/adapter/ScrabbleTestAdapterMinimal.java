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
	
	/**
	 * Returns the ID of MainMenuState, the state when starting the game
	 * @return ID of MainMenuState as registered in StateBasedEntityManager
	 */
	
	public int getMainMenuStateID() {
		// TODO
		return GameParameters.MAINMENU_STATE;
	}
	
	/**
	 * Returns the ID of GameplayState, the main playing state with the board
	 * @return ID of GameplaySate as registered in StateBasedEntityManager 
	 */
	
	public int getGameplayStateID() {
		// TODO
		return GameParameters.GAMEPLAY_STATE;
	}
	/**
	 * Returns the ID of PlayerSelectState, the state in which the number of players are decided before
	 * starting the game.
	 * @return ID of PlayerSelectState
	 */
	
	public int getPlayerSelectStateID() {
		// TODO
		return GameParameters.PLAYER_SELECT_STATE;
	}
	
	// MAIN MENU SECTION
	
	/**
	 * Returns the position of the "New game" button, i.e clicking this button the player can go to the player selecting screen.
	 * If there are no such buttons, null is returned.
	 * @return A Vector2f Object representing the position of the "New game" button in MainMenuState
	 */
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
	
	// GAME PLAY SECTION
	/**
	 * Returns the current number of players in the game
	 * @return current number of players
	 */
	public int getCurrentNumberOfPlayers() {
		// TODO
		return Players.getNumberOfPlayers();
	}
	
	/**
	 * Returns the current round the game is on. By default when the game is started, the round started as 1.
	 * @return current round of the game
	 */
	
	public int getCurrentRound() {
		// TODO
		return ((GameplayState) scrabble.getState(getGameplayStateID())).getCurrentTurn() + 1;
	}
	
	/**
	 * Returns the name of the player that can make a move on the board. The names of the player should be unique to them.
	 * @return name of current player
	 */
	
	public String getNameOfCurrentPlayer() {
		// TODO
		return Players.currentPlayer.getName();
	}
	
	/**
	 * Returns the current score of the player that can make a move on the board.
	 * This score should be unchanged throughout their turn (since only after they finish their turn their new score is calculated)
	 * @return score of current player
	 */
	
	public int getScoreOfCurrentPlayer() {
		// TODO
		return Players.currentPlayer.getScore();
	}
	
	/**
	 * Returns a mapping of player name - their current score.
	 * The number of keys in this mapping should be equal to the number of players in the game. (since their names must be unique)
	 * If the mapping contains no key, null should be returned.
	 * @return mapping of player name - score, otherwise null
	 */
	
	public Map<String,Integer> getPlayerData() {
		HashMap<String,Integer> playerData = new HashMap<>();
		for (Player p : Players.getPlayers()) {
			playerData.put(p.getName() , p.getScore());
		}
		return (playerData.isEmpty() ? null : playerData);
	}
	
	/**
	 * Returns a list of all player names in the game. The list's length should be equal to the number of players in the game.
	 * @return list of players names.
	 */
	
	public List<String> getPlayerNames() {
		List<String> res = new ArrayList<String>();
		for (Player p : Players.getPlayers()) {
			res.add(p.getName());
		}
		return res;
	}
	
	/**
	 * Returns the name of the player that goes after the current player as a String.
	 * @return name of next player
	 */
	
	public String getNameOfNextPlayer() {
		return Players.getPlayers().get((Players.currentPlayer.getID() + 1) % Players.getNumberOfPlayers()).getName();
	}
	
	/**
	 * Returns the score of the player that goes after the current player.
	 * @return score of next player
	 */
	
	public int getScoreOfNextPlayer() {
		return Players.getPlayers().get((Players.currentPlayer.getID() + 1) % Players.getNumberOfPlayers()).getScore();
	}
	
	/**
	 * Returns the position of the "Play" button, with whom the player can click on to finalize their turn, providing
	 * their move was a valid move.
	 * If there are no such buttons, null is returned.
	 * @return a Vector2f Object representing the position of the "Play" button in GameplayState
	 */
	
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
	
	/**
	 * Returns the position of the "Undo" button, with whom the player can click on
	 * to reset the game board to the start of their round.
	 * Returns null if there are no such buttons.
	 * @return a Vector2f object representing the position of the "Undo" button in GameplayState, null if there
	 * are none.
	 */
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
	
	/**
	 * Returns the number of letters in current player's inventory.
	 * @return number of letters in inventory
	 */
	
	public int getCurrentInventorySize() {
		// TODO
		return Players.currentPlayer.getLetters().size();
	}
	
	/**
	 * Returns the playing board as a 2-dimensional array of char.
	 * Each cell in this matrix represents a grid on the board.
	 * If the grid currently has no letter placed on it, the cell is empty: more concrete, the cell contains the value '\u0000'
	 * If there is a letter placed on a grid, the cell contains the value of that letter as a char.
	 * For example, grid (row 1, column 1) will be represented by array[0][0].
	 * If the grid contains a blank letter, the cell of the array will contains the letter that the blank is set to, but in uppercase.
	 * @return the playing board as a char-matrix
	 */
		
	public char[][] getBoard() {
		// TODO
		return ((GameplayState) scrabble.getState(getGameplayStateID())).getCharGrid();
	}
	
	/**
	 * Returns the position of a grid on the board, specified by its row and column.
	 * If the rows or columns parameter exceed the constraints of the board, null is returned.
	 * @param rows row the grid is in
	 * @param column column the grid is in
	 * @return
	 */
	
	public Vector2f getFieldPosition(int row, int column) {
		if (row > 15 || column > 15) return null;
		return ((GameplayState) scrabble.getState(getGameplayStateID())).getTiles()[row-1][column-1].getPosition();
	}
	
	/**
	 * Returns a list of all the positions of the letters that is currently in the current player's inventory.
	 * In other words, the letters that can still be placed on the board.
	 * If the inventory is empty, an empty list should be returned.
	 * @return list of the letters' positions
	 */
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
	
	/**
	 * Returns a list of all the positions of the non-blank letters that is currently in the current player's inventory.
	 * In other words, the letters that can still be placed on the board.
	 * If the inventory is empty or there are only blank letters left, an empty list should be returned.
	 * @return list of the non-blank letters' positions
	 */
	
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
	
	/**
	 * Returns the number of letters that has yet to be hand out the players, also the size of the pool of letters.
	 * @return number of letters left in the bag
	 */
	
	public int getRemainingLettersInBag() {
		return GameplayState.bag_of_letters.size();
	}
	
	/**
	 * Returns the letter at the position given by the parameter as a char.
	 * Usually the position is a element of getLettersInInventoryPosition().
	 * But if at the position there are no letters, then the empty char ('\u0000') should be returned.
	 * @param pos position of letter
	 * @return char representing that letter
	 */
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
	
	/**
	 * Checks if the char representing the letter is indeed the char representing the blank letter.
	 * @param letter char to check
	 * @return true if this char represents the blank letter, false otherwise
	 */
	
	public boolean isLetterBlank(char letter) {
		// TODO
		if (letter == '_') return true;
		return false;
	}
	
	/**
	 * Get the score of the letter. Score of letters are defined according to the rules.
	 * @param letter the char representing the letter on the board
	 * @return score of that letters
	 */
	
	public int getLetterScore(char letter) {
		return ((GameplayState) scrabble.getState(getGameplayStateID())).getLetterScore(letter); 
	}
	
	
	
	// PLAYER SELECT SECTION
	
	/**
	 * Returns the position of the "Start" button in PlayerSelectState, which finalizes the
	 * player selecting process and moves to playing the game.
	 * If there are no such buttons, null should be returned.
	 * @return position of "Start" button in PlayerSelectState
	 */
	
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
	
	/**
	 * Returns the position of the "Back" button in PlayerSelectState, which cancels
	 * the player selecting process and returns to main menu
	 * If there are no such buttons, null should be returned.
	 * @return position of "Back" button in PlayerSelectState
	 */

	
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

	/**
	 * Returns all the positions of the boxes that allow clicking on to set if there are players as a list.
	 * @return a list containing all the player selecting boxes. 
	 */
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
