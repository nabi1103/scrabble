package de.tud.jsf.scrabble.ui.states;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Random;

import org.newdawn.slick.Color;
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
import de.tud.jsf.scrabble.ui.entity.Lexicon;
import de.tud.jsf.scrabble.ui.entity.DialogueBox;
import de.tud.jsf.scrabble.ui.entity.DialogueButton;
import de.tud.jsf.scrabble.ui.entity.Word;
import de.tud.jsf.scrabble.model.player.*;
public class GameplayState extends BasicGameState implements GameParameters{
	
	private int stateID; 				
	private StateBasedEntityManager entityManager;
	
	// Gameplay variables
	private char[][] char_grid = new char[BOARDSIZE][BOARDSIZE];
	private Board b = new Board(new Vector2f(BOARD_START_X,BOARD_START_y));
	private Tile[][] tiles = b.buildBoard();
	private Lexicon lexicon = new Lexicon();
	private static ArrayList<Word> current_words = new ArrayList<>();
	
	private Player[] players;
	private int numberOfPlayers = 2;
	private Player currentPlayer;
	
	// Functional variables
	private boolean move_letter = false;
	private Letter tmp_letter = null;
	private boolean clickable = true;
	
	//String UI
	private static String most_recent_horizontal_word = "";
	private static int most_recent_horizontal_score = 0;
	
	private static String most_recent_vertical_word = "";
	private static int most_recent_vertical_score = 0;
	
	private static int total_score = 0;
	private static int turn = 0;
	
	private static String commit_button_generic_text = "";
	private static String undo_button_generic_text = "";
	private static String warning_button_generic_text = "";
	
	// Test
	
	// Undo button
	private List<Entity> turn_begin_state = new ArrayList<>();
	private int turn_begin_score = 0;
	private char[][] turn_begin_char_grid = new char[BOARDSIZE][BOARDSIZE];
	
	//
	private int numberOfColumns = 0;
	private int numberOfRows = 0;
	private int lineX = -1;
	private int lineY = -1;
	
	GameplayState(int sid ) {
        this.stateID = sid;
        entityManager = StateBasedEntityManager.getInstance();
     }
	
	int debug_size = -1;
     
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		// Init player
		players = new Player[numberOfPlayers];
		for (int i = 0 ; i < numberOfPlayers ; i ++) {
			players[i] = new Player("" + i + 1);
		}
		// Randomize first player to go
		currentPlayer = players[new Random().nextInt(numberOfPlayers-1) + 1];
		
		System.out.println("INIT");
		
		// Create new BACKGROUND entity & add to manager
		Entity background = new Entity("background");
		background.setPosition(new Vector2f(WINDOW_WIDTH / 2, WINDOW_HEIGHT / 2));
		background.addComponent(new ImageRenderComponent(new Image("assets/background.png")));
		entityManager.addEntity(stateID, background);
		
		for (int i = 0 ; i < BOARDSIZE ; i ++) {
			for (int j = 0 ; j < BOARDSIZE ; j ++) {
				entityManager.addEntity(stateID, tiles[i][j]);
				moveLetterToBoardTile(tiles[i][j]);
			}
		}
		// Create letters
		
		// Set<Character> alphabet = Set.of('a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z', '_');
		
		for(int i = 0 ; i < 7; i++) {
			char c = ' ';
			if (i % 3 ==  0) {
				c = 'b';
			}
			if (i % 3 ==  1) {
				c = 'a';
			}
			if (i % 3 ==  2) {
				c = 'g';
			}
			
			if (i == 6) {
				c = 's';
			}
			Vector2f tv = new Vector2f(800, 410 + 50*i);
			Letter tmp = new Letter("letter_" + Integer.toString(i), c, tv);
			moveLetterToBoardLetter(tmp);
			entityManager.addEntity(stateID, tmp);
		}
		
		// Interactive buttons
		// Commit button (PLAY)
		
		Vector2f commit_pos = new Vector2f(750, 120);
		DialogueButton commit = new DialogueButton("commit_button", commit_pos, "commit");
		commit.addImageComponent();
		entityManager.addEntity(stateID, commit);
		triggerCommitDialogueBox(commit);
		// Undo button (RECALL)
		Vector2f undo_pos = new Vector2f(860, 120);
		DialogueButton undo = new DialogueButton("undo_button", undo_pos, "undo");
		undo.addImageComponent();
		entityManager.addEntity(stateID, undo);
		triggerUndoDialogueBox(undo);
		// Play button
		/*
		Vector2f play_pos = new Vector2f(750,120);
		DialogueButton play = new DialogueButton("play_button", play_pos, "commit");
		play.addImageComponent();
		entityManager.addEntity(stateID, play);*/
		// TODO: Challenge Button
		// TODO: Exchange Button
		// TODO: Show letters Button
		
		
		// Always the last thing to do
		
		turn_begin_state.clear();
		
		for (Entity e :entityManager.getEntitiesByState(stateID) ) {
			turn_begin_state.add(e);
		}
		
	}
	// PLAY BUTTON
	public void triggerPlay(DialogueButton button) {
	ArrayList<Entity> tmp_rmv = new ArrayList<>();
	ANDEvent clickEvent = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
	Action clickOnButton = new Action() {
		@Override
		public void update(GameContainer arg0, StateBasedGame arg1, int arg2, Component arg3) {
			DialogueBox box = button.getDialogueBox();
			
			// Update UI elements accordingly
			if (turn == 0 && getEntityByPos(new Vector2f(380,380)).size() < 2) {
				
				entityManager.clearEntitiesFromState(stateID);
				
				most_recent_horizontal_word = "";
				most_recent_horizontal_score = 0;
				
				most_recent_vertical_word = "";
				most_recent_vertical_score = 0;
				
				current_words.clear();
				
				entityManager.removeEntity(stateID, box.getButtons()[1]);
				entityManager.removeEntity(stateID, box);
				entityManager.removeEntity(stateID, button);
				
				for(Entity e : turn_begin_state) {
					entityManager.addEntity(stateID, e);
				} 

				for(int i = 0; i < 15; i++) {
					for(int j = 0; j < 15; j++) {
						char_grid[i][j] = turn_begin_char_grid[i][j];
					}
				}
				
				warning_button_generic_text = "The first turn must use the centermost tile!";
				commit_button_generic_text = "";
				clickable = true;
				
				return;
			}
			
			lineX = -1;
			lineY = -1;
			
			turn = turn + 1;
			warning_button_generic_text = "";
			
			total_score = total_score + current_words.stream().mapToInt((w) -> w.getScore()).sum();
			turn_begin_score = total_score;
			
			most_recent_horizontal_word = "";
			most_recent_horizontal_score = 0;
			
			most_recent_vertical_word = "";
			most_recent_vertical_score = 0;
			
			current_words.forEach((w)-> {w.getTiles().forEach((t) -> {t.clearMultiplier();});});
			current_words.clear();
		
			// Remove dialogue box
			entityManager.removeEntity(stateID, box.getButtons()[1]);
			entityManager.removeEntity(stateID, box);
			entityManager.removeEntity(stateID, button);
			
			// Restore overlapping entities
			for (Entity e : tmp_rmv) {
				entityManager.addEntity(stateID, e);
			}
			
			turn_begin_state.clear();
			
			for (Entity e :entityManager.getEntitiesByState(stateID) ) {
				turn_begin_state.add(e);
			}
			
			for(int i = 0; i < 15; i++) {
				for(int j = 0; j < 15; j++) {
					turn_begin_char_grid[i][j] = char_grid[i][j];
				}
			}
			
			commit_button_generic_text = "";
			clickable = true;
			
		
		}
	};
	clickEvent.addAction(clickOnButton);
	button.addComponent(clickEvent);
	
	}
	
	// "Commit" dialogue box event functions
	
	public void triggerCommitDialogueBox(DialogueButton button) {
		ArrayList<Entity> tmp_rmv = new ArrayList<>();
		
		ANDEvent clickEvent = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		Action clickOnButton = new Action() {
			@Override
			public void update(GameContainer arg0, StateBasedGame arg1, int arg2, Component arg3) {
				if(!clickable) return;
				Vector2f t = new Vector2f(DBOX_START_X, DBOX_START_Y);
				DialogueBox dbox_test = new DialogueBox("commit_dialogue_box", t, 50);
				
				// Get and temporarily remove overlapping entities
				
				for (Entity e : getEntityByPos(new Vector2f (340, 420))) {
					if(!tmp_rmv.contains(e)) tmp_rmv.add(e);
				}
				
				for (Entity e : getEntityByPos(new Vector2f (460, 420))) {
					if(!tmp_rmv.contains(e)) tmp_rmv.add(e);
				}
				
				for (Entity e : tmp_rmv) {
					entityManager.removeEntity(stateID, e);
				}
				
				// Add dialogue box
				
				entityManager.addEntity(stateID, dbox_test);
				
				for (DialogueButton b : dbox_test.getButtons()) {
					entityManager.addEntity(stateID, b);
					if(b.getID().contains("confirm")) {
						destroyCommitDialogueBoxConfirmButton(b, tmp_rmv);
					}
					
					if(b.getID().contains("cancel")) {
						destroyDialogueBoxCancelButton(b, tmp_rmv);
					}
				}

				commit_button_generic_text = "Add the words found to total score\nand end this turn?\n\nPoints earned this turn: " + current_words.stream().mapToInt((w) -> w.getScore()).sum();
				clickable = false;
			}
		};
		clickEvent.addAction(clickOnButton);
		button.addComponent(clickEvent);
	}
	
	public void destroyDialogueBoxCancelButton(DialogueButton button, ArrayList<Entity> tmp_rmv) { 	// Can be used for all dialogue boxes
		ANDEvent clickEvent = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		Action clickOnButton = new Action() {

			@Override
			public void update(GameContainer arg0, StateBasedGame arg1, int arg2, Component arg3) {
				DialogueBox box = button.getDialogueBox();
				entityManager.removeEntity(stateID, box.getButtons()[0]);
				entityManager.removeEntity(stateID, box);
				entityManager.removeEntity(stateID, button);
				
				for (Entity e : tmp_rmv) {
					entityManager.addEntity(stateID, e);
				}
				clickable = true;
				commit_button_generic_text = "";
				undo_button_generic_text = "";
				warning_button_generic_text = "";
			}
		};
		
		clickEvent.addAction(clickOnButton);
		button.addComponent(clickEvent);
	}
	
	public void destroyCommitDialogueBoxConfirmButton(DialogueButton button, ArrayList<Entity> tmp_rmv) {
		ANDEvent clickEvent = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		Action clickOnButton = new Action() {
			@Override
			public void update(GameContainer arg0, StateBasedGame arg1, int arg2, Component arg3) {
				DialogueBox box = button.getDialogueBox();
				
				// Update UI elements accordingly
				if (turn == 0 && getEntityByPos(new Vector2f(380,380)).size() < 2) {
					
					entityManager.clearEntitiesFromState(stateID);
					
					most_recent_horizontal_word = "";
					most_recent_horizontal_score = 0;
					
					most_recent_vertical_word = "";
					most_recent_vertical_score = 0;
					
					current_words.clear();
					
					entityManager.removeEntity(stateID, box.getButtons()[1]);
					entityManager.removeEntity(stateID, box);
					entityManager.removeEntity(stateID, button);
					
					for(Entity e : turn_begin_state) {
						entityManager.addEntity(stateID, e);
					} 

					for(int i = 0; i < 15; i++) {
						for(int j = 0; j < 15; j++) {
							char_grid[i][j] = turn_begin_char_grid[i][j];
						}
					}
					
					warning_button_generic_text = "The first turn must use the centermost tile!";
					commit_button_generic_text = "";
					clickable = true;
					
					return;
				}
				
				lineX = -1;
				lineY = -1;
				
				turn = turn + 1;
				warning_button_generic_text = "";
				
				total_score = total_score + current_words.stream().mapToInt((w) -> w.getScore()).sum();
				turn_begin_score = total_score;
				
				most_recent_horizontal_word = "";
				most_recent_horizontal_score = 0;
				
				most_recent_vertical_word = "";
				most_recent_vertical_score = 0;
				
				current_words.forEach((w)-> {w.getTiles().forEach((t) -> {t.clearMultiplier();});});
				current_words.clear();
			
				// Remove dialogue box
				entityManager.removeEntity(stateID, box.getButtons()[1]);
				entityManager.removeEntity(stateID, box);
				entityManager.removeEntity(stateID, button);
				
				// Restore overlapping entities
				for (Entity e : tmp_rmv) {
					entityManager.addEntity(stateID, e);
				}
				
				turn_begin_state.clear();
				
				for (Entity e :entityManager.getEntitiesByState(stateID) ) {
					turn_begin_state.add(e);
				}
				
				for(int i = 0; i < 15; i++) {
					for(int j = 0; j < 15; j++) {
						turn_begin_char_grid[i][j] = char_grid[i][j];
					}
				}
				
				commit_button_generic_text = "";
				clickable = true;
				
			}
		};
		
		clickEvent.addAction(clickOnButton);
		button.addComponent(clickEvent);
	}
	
	// Undo dialogue box
	
	public void triggerUndoDialogueBox(DialogueButton button) {
		ArrayList<Entity> tmp_rmv = new ArrayList<>();
		
		ANDEvent clickEvent = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		Action clickOnButton = new Action() {
			@Override
			public void update(GameContainer arg0, StateBasedGame arg1, int arg2, Component arg3) {
				if(!clickable) return;
				Vector2f t = new Vector2f(DBOX_START_X, DBOX_START_Y);
				DialogueBox dbox_test = new DialogueBox("undo_dialoguebox", t, 50);
				
				// Get and temporarily remove overlapping entities
				
				for (Entity e : getEntityByPos(new Vector2f (340, 420))) {
					if(!tmp_rmv.contains(e)) tmp_rmv.add(e);
				}
				
				for (Entity e : getEntityByPos(new Vector2f (460, 420))) {
					if(!tmp_rmv.contains(e)) tmp_rmv.add(e);
				}
				
				for (Entity e : tmp_rmv) {
					entityManager.removeEntity(stateID, e);
				}
				
				// Add dialogue box
				
				entityManager.addEntity(stateID, dbox_test);
				
				for (DialogueButton b : dbox_test.getButtons()) {
					entityManager.addEntity(stateID, b);
					if(b.getID().contains("confirm")) {
						destroyUndoDialogueBoxConfirmButton(b, tmp_rmv);
					}
					
					if(b.getID().contains("cancel")) {
						destroyDialogueBoxCancelButton(b, tmp_rmv);
					}
				}

				undo_button_generic_text = "Remove changes and return\nto the beginning of the turn?";
				clickable = false;
			}
		};
		clickEvent.addAction(clickOnButton);
		button.addComponent(clickEvent);		
	}
	
	public void destroyUndoDialogueBoxConfirmButton(DialogueButton button, ArrayList<Entity>tmp_rmv) {
		ANDEvent clickEvent = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		Action clickOnButton = new Action() {
			@Override
			public void update(GameContainer arg0, StateBasedGame arg1, int arg2, Component arg3) {
				DialogueBox box = button.getDialogueBox();
				
				// Update UI elements accordingly
				total_score = turn_begin_score;
				
				entityManager.clearEntitiesFromState(stateID);
			
				most_recent_horizontal_word = "";
				most_recent_horizontal_score = 0;
				
				most_recent_vertical_word = "";
				most_recent_vertical_score = 0;
				
				current_words.clear();
				
				// Remove dialogue box
				entityManager.removeEntity(stateID, box.getButtons()[1]);
				entityManager.removeEntity(stateID, box);
				entityManager.removeEntity(stateID, button);
				
				// Restore old entities
				for(Entity e : turn_begin_state) {
					entityManager.addEntity(stateID, e);
				} 

				for(int i = 0; i < 15; i++) {
					for(int j = 0; j < 15; j++) {
						char_grid[i][j] = turn_begin_char_grid[i][j];
					}
				}
				
				clickable = true;
				undo_button_generic_text = "";
			}
		};
		
		clickEvent.addAction(clickOnButton);
		button.addComponent(clickEvent);
		
	}
	
	// Warning dialogue box
	
	// Moving letters function
	
	public void moveLetterToBoardLetter(Letter l) {
		ANDEvent clickEvent = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		Action clickOnLetter = new Action() {
			@Override
			public void update(GameContainer arg0, StateBasedGame arg1, int arg2, Component arg3) {
				if(!clickable) return;
				if (move_letter) {
					tmp_letter = l;
					return;
				}
				move_letter = true;
				tmp_letter = l;
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
				if(!clickable) return;
				if (!move_letter) return;
				if(getEntityByPos(t.getPosition()).size() > 1) return;
				
				int x = Integer.parseInt(t.getID().split("_")[0]);
				int y = Integer.parseInt(t.getID().split("_")[1]);
				
				
				if(turn > 0) {
					char left = '\u0000';
					char right = '\u0000';
					char up = '\u0000';
					char down = '\u0000';
					if (y != 0) {
						left = char_grid[x][y - 1];
					}
					
					if (y != BOARDSIZE - 1) {
						right = char_grid[x][y + 1];
					}
					
					if (x != 0) {
						up = char_grid[x - 1][y];
					}
					
					if (x != BOARDSIZE - 1) {
						down = char_grid[x + 1][y];
					}
				
					if (up == '\u0000' && down == '\u0000' && left == '\u0000' && right == '\u0000') {
						move_letter = false;
						return;
					}					
					
				}
				if (lineX == -1) {
					numberOfRows = 1;
					lineX = x;
				}
				if (lineY == -1) {
					numberOfColumns = 1;
					lineY = y;
				}
				int tempX = numberOfRows;
				int tempY = numberOfColumns;
				if (x != lineX) numberOfRows ++;
				if (y != lineY) numberOfColumns ++;
				
				if (numberOfRows > 1 && numberOfColumns > 1) {
					numberOfRows = tempX;
					numberOfColumns = tempY;
					move_letter = false;
					return;
				}
												
				Letter new_letter = new Letter(tmp_letter.getID(), tmp_letter.getValue(), t.getPosition());
				
				char_grid[x][y] = new_letter.getValue();
				
				entityManager.removeEntity(stateID, entityManager.getEntity(stateID, tmp_letter.getID()));
				entityManager.addEntity(stateID, new_letter);
				
				move_letter = false;
				tmp_letter = null;
				
				// TEST
			
				System.out.println("current grid:");

				for(int i1 = 0; i1 < 15; i1++) {
					for(int j1 = 0; j1 < 15; j1++) {
						System.out.print("[" + char_grid[i1][j1] + "]");
					}
					System.out.println(" ");
				}
				
				// TEST 
				
				Word[] curr_words = getWord(x,y);
				//System.out.println(curr_words[0].getValue());
				//System.out.println(curr_words[1].getValue());
				
				most_recent_horizontal_word = curr_words[0].getValue();
				most_recent_horizontal_score = curr_words[0].getScore();
				addToCurrentWord(curr_words[0]);
				
				most_recent_vertical_word = curr_words[1].getValue();
				most_recent_vertical_score = curr_words[1].getScore();
				addToCurrentWord(curr_words[1]);
//				for (Tile t : curr_words[1].getTiles()) {
//					System.out.print(t.getID() + " ");
//				}
			}
		};
		clickEvent.addAction(clickOnLetter);
		t.addComponent(clickEvent);
	}
	
	// Help functions
	
	public ArrayList<Entity> getEntityByPos(Vector2f pos) {
		ArrayList<Entity> result = new ArrayList<>();
		
		for (Entity e : entityManager.getEntitiesByState(stateID)) {
			if (e.getPosition().getX() == pos.getX() && e.getPosition().getY() == pos.getY() && !result.contains(e)) {
				result.add(e);
			}
			
		}
		return result;
	}
	
	public boolean isNewWord(Word current_word, Word new_word) {
		ArrayList<Tile> current_tiles = current_word.getTiles();
		ArrayList<Tile> new_tiles = new_word.getTiles();
		
		
		boolean result = true;
		
		for (Tile t : current_tiles) {
			result = result && new_tiles.contains(t);
		}
		
		/*
		boolean[] result = {false, false};
		boolean extend = true;
	
		for (Tile t : current_tiles) {
			extend = extend && new_tiles.contains(t);
			if (!new_tiles.contains(t)) {
				result[0] = true;
			}
		}
		result[1] = extend;
		return result;
		
		 */
		return result;
	}
	
	public void addToCurrentWord(Word new_word) {
		if(current_words.isEmpty()) {
			current_words.add(new_word);
			return;
		}
		for (Iterator<Word> it = current_words.iterator(); it.hasNext();) {
			Word w = it.next();
			if (isNewWord(w, new_word)) {
				it.remove();
			}
		}
		if (new_word.getValue().length() > 1) current_words.add(new_word);
		new_word.getTiles().forEach((t) -> {t.clearMultiplier();});
		
		/*
		boolean completely_new = true;
		boolean extension = false;
		
		for (Word w : current_words) {
			 completely_new = completely_new && isNewWord(w, new_word)[0];
			 if(isNewWord(w, new_word)[1]){
				 extension = true;
				 current_words.remove(w);
				 current_words.add(new_word);
//				 new_word.getTiles().forEach((t) -> {t.clearMultiplier();});
				 break;
			 }
		}
		if(completely_new && !extension ) { // && lexicon.check(new_word.getValue())) {
			current_words.add(new_word);
			
//			new_word.getTiles().forEach((t) -> {t.clearMultiplier();});
		}
		else {
			System.out.println(new_word.getValue() + " is not added");
		}
		*/
		current_words.forEach((w) -> {System.out.println(w.getValue());});
	}
		
	public Word[] getWord(int i, int j) {
		char curr_char = char_grid[i][j];
		
		String left = "";
		String right = "";
		String word = "";
		int word_score = 0;
		
		Word[] result = new Word[2];
		
		int score = getLetterScore(curr_char) * tiles[i][j].getLetterMultiplier();
		int word_multiplier = tiles[i][j].getWordMultiplier();
		ArrayList<Tile> word_tiles_h = new ArrayList<Tile>();
		word_tiles_h.add(tiles[i][j]);
		ArrayList<Tile> word_tiles_v = new ArrayList<Tile>();
		word_tiles_v.add(tiles[i][j]);
		
		// Horizontal
		
		int y = j - 1;
		while (y >= 0) {		// Left
			char next_char = char_grid[i][y];
			if (next_char == '\u0000') {
				break;			
			}
			left = next_char + left;
			score = score + getLetterScore(next_char) * tiles[i][y].getLetterMultiplier();
			word_multiplier = word_multiplier * tiles[i][y].getWordMultiplier();
			word_tiles_h.add(tiles[i][y]);
			y --;
		}
		y = j + 1;
		while (y < BOARDSIZE) {  // Right
			char next_char = char_grid[i][y];
			if (char_grid[i][y] == '\u0000') break;
			right = right + next_char;
			score = score + getLetterScore(next_char) * tiles[i][y].getLetterMultiplier();
			word_multiplier = word_multiplier * tiles[i][y].getWordMultiplier();
			word_tiles_h.add(tiles[i][y]);
			y ++;
		}
		
		word = left + curr_char + right;
		
		//if (lexicon.check(word)) {
			word_score = score * word_multiplier;
		//} 
		
		word_tiles_h.sort(new Comparator<Tile>() {

			@Override
			public int compare(Tile t0, Tile t1) {
				return Integer.compare(Integer.parseInt(t0.getID().split("_")[1]), Integer.parseInt(t1.getID().split("_")[1]));
			}
		});
		
		result[0] = new Word(word, word_score, word_tiles_h);
		
		
		// Reset
		score = getLetterScore(curr_char) * tiles[i][j].getLetterMultiplier();
		word_multiplier = tiles[i][j].getWordMultiplier();
		left = "";
		right = "";
		word = "";
		word_score = 0;
		
		// Vertical
		
		int x = i - 1;
		while (x >= 0) {   // Up
			char next_char = char_grid[x][j];
			if (next_char == '\u0000') {
				break;			
			}
			left = next_char + left;
			score = score + getLetterScore(next_char) * tiles[x][j].getLetterMultiplier();
			word_multiplier = word_multiplier * tiles[x][j].getWordMultiplier();
			word_tiles_v.add(tiles[x][j]);
			x --;
		}
		x = i + 1;    
		while (x < BOARDSIZE) {	// Down
			char next_char = char_grid[x][j];
			if (next_char == '\u0000') break;
			right = right + next_char;
			score = score + getLetterScore(next_char) * tiles[x][j].getLetterMultiplier();
			word_multiplier = word_multiplier * tiles[x][j].getWordMultiplier();
			word_tiles_v.add(tiles[x][j]);
			x ++;
		}
		
		word = left + curr_char + right;
		
		//if (lexicon.check(word)) {
			word_score = score * word_multiplier;
		//}
		
		word_tiles_v.sort(new Comparator<Tile>() {

			@Override
			public int compare(Tile t0, Tile t1) {
				return Integer.compare(Integer.parseInt(t0.getID().split("_")[0]), Integer.parseInt(t1.getID().split("_")[0]));
			}
		});
		
		
		result[1] = new Word(word, word_score, word_tiles_v);

		return result;
	}
	
	public int getLetterScore(char c) {
		Set<Character> blank = Set.of('_');
		
		if (blank.contains(c)) {
			return 0;
		} 
		
		Set<Character> one = Set.of('a','e','i','n','o','r','s','t','u','l');
		
		if (one.contains(c)) {
			return 1;
		}
		Set<Character> two = Set.of('g','d');
		
		if (two.contains(c)) {
			return 2;
		}
		
		Set<Character> three = Set.of('b','c','m','p');
		
		if (three.contains(c)) {
			return 3;
		}
		
		Set<Character> four = Set.of('w','f','y','h','v');
		
		if (four.contains(c)) {
			return 4;
		}
		
		Set<Character> five = Set.of('k');
		
		if (five.contains(c)) {
			return 5;
		}
		
		Set<Character> eight = Set.of('j','x');
		
		if (eight.contains(c)) {
			return 8;
		}
		
		Set<Character> ten = Set.of('q','z');
		
		if (ten.contains(c)) {
			return 10;
		}
		
		return -1;
	}
	
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics graphic) throws SlickException {
		entityManager.renderEntities(container, game, graphic);
		
		// Top UI
		
		graphic.drawString("Total Score: " + total_score, 100, 50);
		graphic.drawString("Turn: " + (turn + 1), 600, 50);
		
		// Right UI
		graphic.drawString("Horizontal: " + most_recent_horizontal_word, 700, 180);
		graphic.drawString("Score: " + most_recent_horizontal_score, 700, 200);
		graphic.drawString("Vertical: " + most_recent_vertical_word, 700, 240);
		graphic.drawString("Score: " + most_recent_vertical_score, 700, 260);
		int word_spacing = 20;
		
		for(int i = 0 ; i < current_words.size(); i ++) {
			graphic.drawString(current_words.get(i).getValue() + ", " + current_words.get(i).getScore()  , 700, 280 + i * word_spacing);
		}
		
		// Dialogue box strings
		graphic.drawString(commit_button_generic_text, 250, 300);
		graphic.drawString(undo_button_generic_text, 250, 300);
		graphic.drawString(warning_button_generic_text, 200, 30);
		
		Color b = new Color(0, 0, 0);
		graphic.setColor(b);
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
