package de.tud.jsf.scrabble.ui.states;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
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

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import eea.engine.action.Action;
import eea.engine.action.basicactions.ChangeStateAction;
import eea.engine.action.basicactions.ChangeStateInitAction;
import eea.engine.component.Component;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;
import eea.engine.entity.StateBasedEntityManager;
import eea.engine.event.ANDEvent;
import eea.engine.event.Event;
import eea.engine.event.OREvent;
import eea.engine.event.basicevents.KeyDownEvent;
import eea.engine.event.basicevents.KeyPressedEvent;
import eea.engine.event.basicevents.LeavingScreenEvent;
import eea.engine.event.basicevents.LoopEvent;
import eea.engine.event.basicevents.MouseClickedEvent;
import eea.engine.event.basicevents.MouseEnteredEvent;
import de.tud.jsf.scrabble.constants.GameParameters;
import de.tud.jsf.scrabble.ui.entity.Board;
import de.tud.jsf.scrabble.ui.entity.Tile;
import de.tud.jsf.scrabble.ui.entity.Letter;
import de.tud.jsf.scrabble.ui.entity.Lexicon;
import de.tud.jsf.scrabble.ui.entity.DialogueButton;
import de.tud.jsf.scrabble.ui.entity.Word;
import de.tud.jsf.scrabble.model.highscore.Highscore;
import de.tud.jsf.scrabble.model.highscore.HighscoreList;
import de.tud.jsf.scrabble.model.player.*;

public class GameplayState extends BasicGameState implements GameParameters {

	private int stateID;
	private int lastStateID;
	private StateBasedEntityManager entityManager;

	// Gameplay variables
	private char[][] char_grid = new char[BOARDSIZE][BOARDSIZE];
	private Board b = new Board(new Vector2f(BOARD_START_X, BOARD_START_Y));
	private Tile[][] tiles = b.buildBoard();
	private Lexicon lexicon = new Lexicon();
	private static ArrayList<Word> current_words = new ArrayList<>();
	private int consecutivePasses;

	// Player data
	//static Player[] players;
	//private int numberOfPlayers = Players.getNumberOfPlayers();
	//private Player currentPlayer;

	// Functional variables
	private boolean move_letter = false;
	private Letter tmp_letter = null;
	private boolean clickable = true;

	// String UI
	private String displayPlayerID;

	private static int turn = 0;

	private static String warning_text ;
	private static String status_text;
	private static String current_total_score;

	// Letter distribution variables
	public static ArrayList<String> bag_of_letters = new ArrayList<String>();
	private ArrayList<Letter> current_display_letters = new ArrayList<Letter>();
	private ArrayList<Letter> current_display_traded_letters = new ArrayList<Letter>();
	private ArrayList<Letter> used_letters = new ArrayList<Letter>();

	// Undo button
	private List<Entity> turn_begin_state = new ArrayList<>();
	private char[][] turn_begin_char_grid = new char[BOARDSIZE][BOARDSIZE];

	private ArrayList<Tile> newTilesThisTurn = new ArrayList<>();

	// Check button
	private boolean checkable = true;
	private Player last_player;
	private int last_player_added_point;
	private List<Entity> last_player_turn_begin_state = new ArrayList<>();
	private ArrayList<Word> last_player_added_word = new ArrayList<>();
	private ArrayList<Letter> last_player_used_letters = new ArrayList<Letter>();
	private char[][] last_player_turn_begin_char_grid = new char[BOARDSIZE][BOARDSIZE];

	// State safe
	private static boolean new_game = true;

	// Trade button
	Vector2f trade_pos = new Vector2f(880, 180);
	private DialogueButton trade = new DialogueButton("trade_button", trade_pos, "trade");

	private ArrayList<String> traded_letter = new ArrayList<String>();
	private boolean trading = false; // If currently trading -> can't put new tiles to the board
	
	DialogueButton check;
	Entity background;

	GameplayState(int sid) {
		this.stateID = sid;
		entityManager = StateBasedEntityManager.getInstance();
	}

	public int getCurrentTurn() {
		return turn;
	}
	
	public char[][] getCharGrid() {
		return char_grid;
	}
	
	public Tile[][] getTiles() {
		return tiles;
	}
	
	
	
	
	public int getLastStateID() {
		return lastStateID;
	}
	
	public void setLastStateID(int a) {
		lastStateID = a;
	}

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {	
		initNewGame();		
	}

	public void initNewGame() throws SlickException {
		// Init variables
		warning_text = "";
		status_text = "";
		current_total_score = "";
		// Initialize background
		background = new Entity("background");
		background.setPosition(new Vector2f(WINDOW_WIDTH / 2, WINDOW_HEIGHT / 2));
		if (!Launch.debug)background.addComponent(new ImageRenderComponent(new Image(BACKGROUND)));
		entityManager.addEntity(stateID, background);
		
		// Create board
		for (int i = 0; i < BOARDSIZE; i++) {
			for (int j = 0; j < BOARDSIZE; j++) {
				entityManager.addEntity(stateID, tiles[i][j]);
				moveLetterToBoardTile(tiles[i][j]);
			}
		}
		// Initialize player
		//players = Players.getPlayers().toArray(new Player[0]);
		//for (int i = 0; i < numberOfPlayers; i++) {
		//	players[i] = new Player("" + (i + 1), i);
		//}
		// Randomize first player to go
		//currentPlayer = players[new Random().nextInt(numberOfPlayers)];
		//displayPlayerID = currentPlayer.getName();

		// Initialize letter bag
		bag_of_letters = initLetter();
		
		//for (Player p : players) {
			//for (int i = 0; i < PLAYER_INVENTORY_SIZE; i++) {
				//String to_add = bag_of_letters.get(new Random().nextInt(bag_of_letters.size()));
				//p.addLetter(to_add);
				//bag_of_letters.remove(to_add);
			//}
		//}

		// Initialize trade zone
		Entity trade_zone = new Entity("trade_zone");
		trade_zone.setPosition(new Vector2f(825, 600));
		trade_zone.setScale(0.5f);
		trade_zone.setSize(new Vector2f(DBOX_WIDTH * 0.5f , DBOX_HEIGHT*0.5f));
		if (!Launch.debug)trade_zone.addComponent(new ImageRenderComponent(new Image(DBOX)));
		moveLetterToTradeZone(trade_zone);
		entityManager.addEntity(stateID, trade_zone);

		// Initialize buttons
		// Play button
		Vector2f play_pos = new Vector2f(770, 120);
		DialogueButton play = new DialogueButton("play_button", play_pos, "commit");
		if (!Launch.debug) play.addImageComponent();
		entityManager.addEntity(stateID, play);
		triggerPlay(play);

		// Undo button
		Vector2f undo_pos = new Vector2f(880, 120);
		DialogueButton undo = new DialogueButton("undo_button", undo_pos, "undo");
		if (!Launch.debug) undo.addImageComponent();
		entityManager.addEntity(stateID, undo);
		triggerUndo(undo);

		// Check button
		Vector2f check_pos = new Vector2f(770, 180);
		check = new DialogueButton("check_button", check_pos, "check");
		if (!Launch.debug) check.addImageComponent();
		entityManager.addEntity(stateID, check);
		triggerCheck(check);
		check.setVisible(false);

		// Trade button
		if (!Launch.debug) trade.addImageComponent();
		entityManager.addEntity(stateID, trade);
		triggerTrade(trade);

		// TEST button

		Vector2f test_pos = new Vector2f(900, 240);
		DialogueButton test = new DialogueButton("test_button", test_pos, "undo");
		if (!Launch.debug) test.addComponent(new ImageRenderComponent(new Image("assets/scrabble/ui/grey_boxCross.png")));
		test.setSize(new Vector2f(38,36));
		entityManager.addEntity(stateID, test);
		triggerUndo(undo);

		ANDEvent test_event = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		test_event.addAction(new Action() {
			@Override
			public void update(GameContainer arg0, StateBasedGame arg1, int arg2, Component arg3) {
				System.out.println("Current turn" + turn);
				System.out.println("Current player: " + displayPlayerID);
				for (Player p : Players.getPlayers()) {
					System.out.println("Player " + p.getName() + "(" + p.getID() + ")" + ": " + p.getScore());
					System.out.println(p.getLetters());
					System.out.println("--------------------------------");
				}
				System.out.println("Total entity count: " + entityManager.getEntitiesByState(stateID).size());
				System.out.println("Current grid:");

				for(int i1 = 0; i1 < 15; i1++) {
					for(int j1 = 0; j1 < 15; j1++) {
						System.out.print("[" + char_grid[i1][j1] + "]");
					}
					System.out.println(" ");
				}
				System.out.println("******************************");
			}
		});
		test.addComponent(test_event);
		entityManager.addEntity(stateID, test);
		

		// TODO: Show letters Button

		// Always the last thing to do
		turn_begin_state.clear();
		for (Entity e : entityManager.getEntitiesByState(stateID)) {
			turn_begin_state.add(e);
			last_player_turn_begin_state.add(e);
		}
		new_game = false;
	}
	@Override
	public void enter(GameContainer container, StateBasedGame game) {
		if (lastStateID == PLAYER_SELECT_STATE) {
			// Initialize player inventories
			for (Player p : Players.getPlayers()) {
				for (int i = 0; i < PLAYER_INVENTORY_SIZE; i++) {
					String to_add = bag_of_letters.get(new Random().nextInt(bag_of_letters.size()));
					p.addLetter(to_add);
					GameplayState.bag_of_letters.remove(to_add);
				}
			}
			// Render inventory for first player
			renderPlayerLetter(Players.currentPlayer);
			// Render starting player
			displayPlayerID = Players.currentPlayer.getName();
			lastStateID = -1;
			// Initialize variables
			consecutivePasses = 0;
		}
				
	}
	
	
	// PLAY BUTTON
	public void triggerPlay(DialogueButton button) {
		ANDEvent clickEvent = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		Action clickOnButton = new Action() {
			@Override
			public void update(GameContainer arg0, StateBasedGame arg1, int arg2, Component arg3) {
				// Check if the rules for the first turn is not broken; if that is the case, the
				// play is invalid and the player must redo their moves
				if (!Launch.debug) System.out.println("Play is clicked");
				if (trading) {
					warning_text = "You can not commit while there are letters in the trade zone";
					return;
				}
				if (newTilesThisTurn.isEmpty()) { // PASS
					consecutivePasses ++;
					nextTurn();
					return;
				}

				if (getEntityByPos(new Vector2f(380, 380)).size() < 2) {
					warning_text = "The first turn must use the centermost tile!";
					return;
				}

				/*if (turn == 0) {
					for (Word w : current_words) {
						if (!lexicon.check(w.getValue()) || w.getValue().length() < 2) {
							warning_text = "The first played word must be correct and contain at least 2 characters";
							return;
						}
					}
				}*/
				if (current_words.isEmpty()) {
					warning_text = "Each word must contain at least 2 characters";
					return;
				}				
				for (Word w : current_words) { // REDUNDANT
					if (w.getValue().length() < 2) {
						warning_text = "Each word must contain at least 2 characters";
						return;
					}
				}

				// Check if all the new tiles placed are in the same horizontal/vertical line
				int currentX = -1; // Init value = -1
				int currentY = -1;
				int numberOfRows = 0;
				int numberOfColumns = 0;
				for (Tile t : newTilesThisTurn) {
					// Turn coordinates into integer
					int x = Integer.parseInt(t.getID().split("_")[0]);
					int y = Integer.parseInt(t.getID().split("_")[1]);
					if (x != currentX) {
						numberOfRows++;
						if (currentX == -1)
							currentX = x;
					}
					if (y != currentY) {
						numberOfColumns++;
						if (currentY == -1)
							currentY = y;
					}
				}
				if (numberOfRows > 1 && numberOfColumns > 1) {
					warning_text = "The new tiles are not placed in the same horizontal / vertical line!";
					return;
				}
				consecutivePasses = 0;
				nextTurn();
			}
		};
		clickEvent.addAction(clickOnButton);
		button.addComponent(clickEvent);
	}

	// UNDO BUTTON
	public void triggerUndo(DialogueButton button) {
		ANDEvent clickEvent = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		Action clickOnButton = new Action() {
			@Override
			public void update(GameContainer arg0, StateBasedGame arg1, int arg2, Component arg3) {
				undo();
			}
		};

		clickEvent.addAction(clickOnButton);
		button.addComponent(clickEvent);

	}

	// CHECK BUTTON
	public void triggerCheck(DialogueButton button) {
		ANDEvent clickEvent = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		Action clickOnButton = new Action() {

			@Override
			public void update(GameContainer arg0, StateBasedGame arg1, int arg2, Component arg3) {
				if (last_player_added_word.isEmpty()) 
					return;
				undo();
				for (Iterator<Word> it = last_player_added_word.iterator(); it.hasNext();) {
					Word w = it.next();
					if (!lexicon.check(w.getValue()) && checkable) {
						entityManager.clearEntitiesFromState(stateID);
						turn_begin_state.clear();

						last_player.setScore(last_player.getScore() - last_player_added_point);
						warning_text = "Check complete. " + w.getValue() + " was not accepted. " + last_player.getName()
								+ "'s score has been reduced to " + last_player.getScore() + ".";
						for(Letter l : last_player_used_letters) {
							last_player.addLetter(l.getID());
						}

						for (Entity e : last_player_turn_begin_state) {
							entityManager.addEntity(stateID, e);
							turn_begin_state.add(e);
						}

						for (int i = 0; i < BOARDSIZE; i++) {
							for (int j = 0; j < BOARDSIZE; j++) {
								char_grid[i][j] = last_player_turn_begin_char_grid[i][j];
								turn_begin_char_grid[i][j] = last_player_turn_begin_char_grid[i][j];
							}
						}

						renderPlayerLetter(Players.currentPlayer);
						checkable = false;
						last_player_added_word.clear();
						check.setVisible(false);
						return;
					} else {
						warning_text = "Check complete. No illegal word found. Turn skipped.";
					}
				}
				nextTurn();
				/*
				 * last_player_added_word.forEach((w) -> { if(!lexicon.check(w.getValue()) &&
				 * checkable) { entityManager.clearEntitiesFromState(stateID);
				 * turn_begin_state.clear();
				 * 
				 * last_player.setScore(last_player.getScore() - last_player_added_point);
				 * warning_text = "Check complete. " + w.getValue() + " was not accepted. " +
				 * last_player.getName() +"'s score has been reduced to " +
				 * last_player.getScore();
				 * 
				 * for(Entity e : last_player_turn_begin_state) {
				 * entityManager.addEntity(stateID, e); turn_begin_state.add(e); }
				 * 
				 * for(int i = 0; i < 15; i++) { for(int j = 0; j < 15; j++) { char_grid[i][j] =
				 * last_player_turn_begin_char_grid[i][j]; turn_begin_char_grid[i][j] =
				 * last_player_turn_begin_char_grid[i][j]; } }
				 * 
				 * renderPlayerLetters(currentPlayer);
				 * 
				 * checkable = false; return; } else { warning_text =
				 * "Check complete. No illegal words found! Current player must skip this turn."
				 * ; nextTurn(); } });
				 */
			}

		};
		clickEvent.addAction(clickOnButton);
		button.addComponent(clickEvent);
	}

	// TRADE BUTTON
	public void triggerTrade(DialogueButton button) {
		Action new_Game_Action = new ChangeStateAction(Launch.CE_STATE);
		ANDEvent clickEvent = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		button.removeComponent(clickEvent);
		Action clickOnButton = new Action() {
			@Override
			public void update(GameContainer arg0, StateBasedGame arg1, int arg2, Component arg3) {
				if (traded_letter.isEmpty()) {
					warning_text = "No letters to trade. Add at least one letter to trade zone.";
					clickEvent.removeAction(new_Game_Action);
					button.removeComponent(clickEvent);
					return;
				}
				trade();
			}
		};
		clickEvent.addAction(clickOnButton);
		clickEvent.addAction(new_Game_Action);
		button.addComponent(clickEvent);
	}

	// Moving letters function
	public void moveLetterToBoardLetter(Letter l) {
		ANDEvent clickEvent = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		Action clickOnLetter = new Action() {
			@Override
			public void update(GameContainer arg0, StateBasedGame arg1, int arg2, Component arg3) {
				status_text = ("Picked " + l.getValue());
				if (!clickable)
					return;
				warning_text = "";
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
				if (!clickable)
					return;
				if (!move_letter)
					return;
				if (trading)
					return;
				if (getEntityByPos(t.getPosition()).size() > 1)
					return; // Placing letter on a bereits gelegte Tile will not work

				// Turn coordiantes into integer
				int x = Integer.parseInt(t.getID().split("_")[0]);
				int y = Integer.parseInt(t.getID().split("_")[1]);

				if (turn >= 0) { // Redundant IF block
					boolean isBoardEmpty = true;
					// Check if board is empty
					for (int i = 0; i < char_grid.length; i++) {
						for (int j = 0; j < char_grid.length; j++) { // Assume board is always squared
							if (char_grid[i][j] != '\u0000') {
								isBoardEmpty = false;
							}
						}
					}
					if (!isBoardEmpty) {
						// Check if the tile has any adjacent tiles; if not then it is an illegal move
						char left = '\u0000';
						char right = '\u0000';
						char up = '\u0000';
						char down = '\u0000';
						if (y != 0)
							left = char_grid[x][y - 1];
						if (y != BOARDSIZE - 1)
							right = char_grid[x][y + 1];
						if (x != 0)
							up = char_grid[x - 1][y];
						if (x != BOARDSIZE - 1)
							down = char_grid[x + 1][y];
						if (up == '\u0000' && down == '\u0000' && left == '\u0000' && right == '\u0000') {
							move_letter = false;
							return;
						}
					}
				}
				/*
				 * // Check if all the new letters are on the same horizontal / vertical line if
				 * (lineX == -1) { numberOfRows = 1; lineX = x; } if (lineY == -1) {
				 * numberOfColumns = 1; lineY = y; } int tempX = numberOfRows; int tempY =
				 * numberOfColumns; if (x != lineX) numberOfRows ++; if (y != lineY)
				 * numberOfColumns ++;
				 * 
				 * if (numberOfRows > 1 && numberOfColumns > 1) { numberOfRows = tempX;
				 * numberOfColumns = tempY; move_letter = false; return; }
				 */
				// Since this tile can be placed, add it to the list
				newTilesThisTurn.add(t);

				Letter new_letter = new Letter(tmp_letter.getID(), tmp_letter.getValue(), t.getPosition());

				used_letters.add(new_letter);
				char value = new_letter.getValue();
				Character return_value = '\u0000';
				
				if(value == '_') {
					Character[] options = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h',
							'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
					return_value = (Character)JOptionPane.showInputDialog(null, "Choose a value for the wildcard letter", " ", JOptionPane.QUESTION_MESSAGE, null, options, options[2]);
					
					if (return_value == null) {
						move_letter = false;
						tmp_letter = null;
						return;
					} else {
						value = return_value;
					}
				}
				
				char_grid[x][y] = value;

				entityManager.removeEntity(stateID, entityManager.getEntity(stateID, tmp_letter.getID()));
				entityManager.addEntity(stateID, new_letter);

				move_letter = false;
				tmp_letter = null;

				// TEST
//				System.out.println("current grid:");
//
//				for(int i1 = 0; i1 < 15; i1++) {
//					for(int j1 = 0; j1 < 15; j1++) {
//						System.out.print("[" + char_grid[i1][j1] + "]");
//					}
//					System.out.println(" ");
//				}
				// TEST

				Word[] curr_words = getWord(x, y);

				addToCurrentWord(curr_words[0]);
				addToCurrentWord(curr_words[1]);
				int temp = 0;
				temp = current_words.stream().mapToInt((w) -> w.getScore()).sum();
				current_total_score = (temp > 0 ? temp + "" : "" );
			}
		};
		clickEvent.addAction(clickOnLetter);
		t.addComponent(clickEvent);
	}

	public void moveLetterToTradeZone(Entity z) {
		ANDEvent clickEvent = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		Action clickOnLetter = new Action() {
			@Override
			public void update(GameContainer arg0, StateBasedGame arg1, int arg2, Component arg3) {
				if (!clickable)
					return;
				if (!move_letter)
					return;
				if (newTilesThisTurn.size() > 0) {
					warning_text = "Can't trade if tiles are played in this turn, has to UNDO first";
					return;
				}

				triggerTrade(trade);

				traded_letter.add(tmp_letter.getID());
				Players.currentPlayer.removeLetter(tmp_letter.getID());
				entityManager.removeEntity(stateID, entityManager.getEntity(stateID, tmp_letter.getID()));
				renderTradedLetter();

				move_letter = false;
				tmp_letter = null;
				trading = true;
			}
		};
		clickEvent.addAction(clickOnLetter);
		z.addComponent(clickEvent);
	}

	// Misc functions
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

		return result;
	}

	public void addToCurrentWord(Word new_word) {
		if (current_words.isEmpty()) {
			current_words.add(new_word);
			return;
		}
		for (Iterator<Word> it = current_words.iterator(); it.hasNext();) {
			Word w = it.next();
			if (isNewWord(w, new_word)) {
				it.remove();
			}
		}
		if (new_word.getValue().length() > 1)
			current_words.add(new_word);
		new_word.getTiles().forEach((t) -> {
			t.clearMultiplier();
		});

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
		while (y >= 0) { // Left
			char next_char = char_grid[i][y];
			if (next_char == '\u0000') {
				break;
			}
			left = next_char + left;
			score = score + getLetterScore(next_char) * tiles[i][y].getLetterMultiplier();
			word_multiplier = word_multiplier * tiles[i][y].getWordMultiplier();
			word_tiles_h.add(tiles[i][y]);
			y--;
		}
		y = j + 1;
		while (y < BOARDSIZE) { // Right
			char next_char = char_grid[i][y];
			if (char_grid[i][y] == '\u0000')
				break;
			right = right + next_char;
			score = score + getLetterScore(next_char) * tiles[i][y].getLetterMultiplier();
			word_multiplier = word_multiplier * tiles[i][y].getWordMultiplier();
			word_tiles_h.add(tiles[i][y]);
			y++;
		}

		word = left + curr_char + right;

		// if (lexicon.check(word)) {
		word_score = score * word_multiplier;
		// }

		word_tiles_h.sort(new Comparator<Tile>() {

			@Override
			public int compare(Tile t0, Tile t1) {
				return Integer.compare(Integer.parseInt(t0.getID().split("_")[1]),
						Integer.parseInt(t1.getID().split("_")[1]));
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
		while (x >= 0) { // Up
			char next_char = char_grid[x][j];
			if (next_char == '\u0000') {
				break;
			}
			left = next_char + left;
			score = score + getLetterScore(next_char) * tiles[x][j].getLetterMultiplier();
			word_multiplier = word_multiplier * tiles[x][j].getWordMultiplier();
			word_tiles_v.add(tiles[x][j]);
			x--;
		}
		x = i + 1;
		while (x < BOARDSIZE) { // Down
			char next_char = char_grid[x][j];
			if (next_char == '\u0000')
				break;
			right = right + next_char;
			score = score + getLetterScore(next_char) * tiles[x][j].getLetterMultiplier();
			word_multiplier = word_multiplier * tiles[x][j].getWordMultiplier();
			word_tiles_v.add(tiles[x][j]);
			x++;
		}

		word = left + curr_char + right;

		// if (lexicon.check(word)) {
		word_score = score * word_multiplier;
		// }

		word_tiles_v.sort(new Comparator<Tile>() {

			@Override
			public int compare(Tile t0, Tile t1) {
				return Integer.compare(Integer.parseInt(t0.getID().split("_")[0]),
						Integer.parseInt(t1.getID().split("_")[0]));
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

		Set<Character> one = Set.of('a', 'e', 'i', 'n', 'o', 'r', 's', 't', 'u', 'l');

		if (one.contains(c)) {
			return 1;
		}
		Set<Character> two = Set.of('g', 'd');

		if (two.contains(c)) {
			return 2;
		}

		Set<Character> three = Set.of('b', 'c', 'm', 'p');

		if (three.contains(c)) {
			return 3;
		}

		Set<Character> four = Set.of('w', 'f', 'y', 'h', 'v');

		if (four.contains(c)) {
			return 4;
		}

		Set<Character> five = Set.of('k');

		if (five.contains(c)) {
			return 5;
		}

		Set<Character> eight = Set.of('j', 'x');

		if (eight.contains(c)) {
			return 8;
		}

		Set<Character> ten = Set.of('q', 'z');

		if (ten.contains(c)) {
			return 10;
		}

		return -1;
	}

	public ArrayList<String> initLetter() {
		ArrayList<String> result = new ArrayList<String>();
		HashMap<Character, Integer> letter_map = new HashMap<Character, Integer>();

		letter_map.put('a', 9);
		letter_map.put('b', 2);
		letter_map.put('c', 2);
		letter_map.put('d', 4);
		letter_map.put('e', 12);
		letter_map.put('f', 2);
		letter_map.put('g', 3);
		letter_map.put('h', 2);
		letter_map.put('i', 9);
		letter_map.put('j', 1);
		letter_map.put('k', 1);
		letter_map.put('l', 4);
		letter_map.put('m', 2);
		letter_map.put('n', 6);
		letter_map.put('o', 8);
		letter_map.put('p', 2);
		letter_map.put('q', 1);
		letter_map.put('r', 6);
		letter_map.put('s', 4);
		letter_map.put('t', 6);
		letter_map.put('u', 4);
		letter_map.put('v', 2);
		letter_map.put('x', 1);
		letter_map.put('y', 2);
		letter_map.put('z', 1);
		letter_map.put('w', 2);
		letter_map.put('_', 2);

		for (char c : letter_map.keySet()) {
			for (int i = 0; i < letter_map.get(c); i++) {
				// Letter tmp = new Letter("letter_" + c + "_" + Integer.toString(i), c, dummy);
				result.add(c + "_" + i);
			}
		}
		return result;
	}

	public void renderPlayerLetter(Player p) {
		current_display_letters.clear();
		p.getLetters().sort(new Comparator<String>() {
			ArrayList<Character> order = new ArrayList<Character>(Arrays.asList('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h',
					'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '_'));

			@Override
			public int compare(String arg0, String arg1) {
				return order.indexOf(arg0.charAt(0)) - order.indexOf(arg1.charAt(0));
			}
		});

		for (int i = 0; i < p.getLetters().size(); i++) {
			Vector2f tv;
			if (i < 4) {
				tv = new Vector2f(750 + 50 * i, 400);
			} else {
				tv = new Vector2f(920 - 50 * (7 - i), 450);
			}
			Letter l = new Letter(p.getLetters().get(i), p.getLetters().get(i).charAt(0), tv);
			moveLetterToBoardLetter(l);
			entityManager.addEntity(stateID, l);
			current_display_letters.add(l);
		}
	}

	public void renderTradedLetter() {
		current_display_traded_letters.forEach((l) -> {
			entityManager.removeEntity(stateID, l);
		});
		current_display_traded_letters.clear();
		traded_letter.sort(new Comparator<String>() {
			ArrayList<Character> order = new ArrayList<Character>(Arrays.asList('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h',
					'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '_'));

			@Override
			public int compare(String arg0, String arg1) {
				return order.indexOf(arg0.charAt(0)) - order.indexOf(arg1.charAt(0));
			}
		});

		for (int i = 0; i < traded_letter.size(); i++) {
			Vector2f tv;
			if (i < 4) {
				tv = new Vector2f(750 + 50 * i, 575);
			} else {
				tv = new Vector2f(920 - 50 * (7 - i), 625);
			}
			Letter l = new Letter(traded_letter.get(i), traded_letter.get(i).charAt(0), tv);
			current_display_traded_letters.add(l);
			entityManager.addEntity(stateID, l);
		}
	}
	
	public void gameFinish() {
		boolean emptyInventory = false;
		for (Player p : Players.getPlayers()) {
			if (p.getLetters().isEmpty()) emptyInventory = true;
		}
		// Ending condition fulfilled
		if (consecutivePasses >= 2*Players.getNumberOfPlayers()|| emptyInventory) {
			ArrayList<Player> sortedPlayers = new ArrayList<Player>();
			for (Player p : Players.getPlayers()) sortedPlayers.add(p);
			sortedPlayers.sort(new Comparator<Player>() {
				@Override
				public int compare(Player p1, Player p2) {
					return Integer.compare(p2.getScore(),p1.getScore());
				}								
			});
			String winningMessage = "Player " + sortedPlayers.get(0).getName() + " wins!\n\n" + "Score of the players:\n";
			Highscore h = new Highscore(sortedPlayers.get(0).getName() , sortedPlayers.get(0).getScore(), turn-1);
			HighscoreList.getInstance().addHighscore(h);
			for (Player p : sortedPlayers) {
				
				winningMessage += "Player " + p.getName() + ": " + p.getScore() +"\n";	
			}
						
			JFrame frame = new JFrame("");
			JOptionPane.showMessageDialog(frame, winningMessage,"Game end!",1);
			System.out.println("END!");
		
			Entity dummy = new Entity("Dummy");
			try {
				dummy.addComponent(new ImageRenderComponent(new Image(DBOX))); 
			}
			catch (Exception exc) {
				exc.printStackTrace();
			}
			
			ANDEvent event = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
			

			
		/*dummy.addComponent(new Event("Ending") {
				@Override
				protected boolean performAction(GameContainer arg0, StateBasedGame arg1, int arg2) {
					// TODO Auto-generated method stub
					return true;
				}
				
			});*/
			/*event.addAction(new Action() {
				@Override
				public void update(GameContainer arg0, StateBasedGame arg1, int arg2, Component arg3) {
					arg1.enterState(MAINMENU_STATE);
					try {
						new_game = true;
						StateBasedEntityManager.getInstance().clearEntitiesFromState(stateID);
						arg0.initGL();
						arg0.reinit();
					}
					catch (SlickException exc) {
						exc.printStackTrace();
					}
										
				}
			});*/
			//new_game = true;
			event.addAction(new ChangeStateInitAction(MAINMENU_STATE));
			dummy.addComponent(event);
			entityManager.addEntity(stateID,dummy);						
		}
		 
	}

	public void nextTurn() {	
		Player currentPlayer = Players.currentPlayer;
		// Increase total turn count
		if (trading && turn == 0) {
			turn = turn - 1;
			System.out.println(turn);
		}

		turn++;

		// Update score for current player
		int newScore = currentPlayer.getScore() + current_words.stream().mapToInt((w) -> w.getScore()).sum();
		currentPlayer.setScore(newScore);
		last_player_added_point = current_words.stream().mapToInt((w) -> w.getScore()).sum();
		used_letters.forEach((l) -> {
			last_player_used_letters.add(l);
			Players.currentPlayer.removeLetter(l.getID());
		});
		
		// Reset trading
		trading = false;
		traded_letter.clear();

		// Reset parameter for check
		checkable = true;
		last_player_added_word.clear();
		last_player_turn_begin_state.clear();

		// Remove currently displayed letters from the current player
		current_display_letters.forEach((l) -> {
			entityManager.removeEntity(stateID, l);
		});

		// Remove currently displayed letters in the trade zone
		current_display_traded_letters.forEach((l) -> {
			entityManager.removeEntity(stateID, l);
		});

		// Save current player's state (for checking)
		last_player = currentPlayer;
		current_words.forEach((w) -> {
			last_player_added_word.add(w);
		});
		if (last_player_added_word.isEmpty()) 
			check.setVisible(false); 
		else check.setVisible(true);	

		// In case current player has committed an illegal word, remove all of his
		// letters
		for (Entity e : turn_begin_state) {
			last_player_turn_begin_state.add(e);
		}

		// Save the internal char grid
		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 15; j++) {
				last_player_turn_begin_char_grid[i][j] = turn_begin_char_grid[i][j];
			}
		}
		// Go to next player
		currentPlayer = Players.nextPlayer();
		displayPlayerID = currentPlayer.getName();

		// Add letters to next player if he doesn't have enough
		while (currentPlayer.getLetters().size() < PLAYER_INVENTORY_SIZE) {
			String to_add = bag_of_letters.get(new Random().nextInt(bag_of_letters.size()));
			currentPlayer.addLetter(to_add);
			bag_of_letters.remove(to_add);
		}

		// Clear multiplier (?)

		// Clear store arrays
		current_words.clear();
		newTilesThisTurn.clear();

		// Update state and char_grid
		turn_begin_state.clear();

		for (Entity e : entityManager.getEntitiesByState(stateID)) {
			turn_begin_state.add(e);
		}

		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 15; j++) {
				turn_begin_char_grid[i][j] = char_grid[i][j];
			}
		}
		renderPlayerLetter(currentPlayer);
		renderTradedLetter();
		
		gameFinish();
	}

	public void undo() {
		entityManager.clearEntitiesFromState(stateID);

		current_words.clear();
		used_letters.clear();
		// Undo trade parameters
		trading = false;
		traded_letter.forEach((l) -> {
			Players.currentPlayer.addLetter(l);
		}); // Change the order of letters but overall still correct
		traded_letter.clear();

		newTilesThisTurn.clear();
		warning_text = "";

		// Restore old entities
		for (Entity e : turn_begin_state) {
			entityManager.addEntity(stateID, e);
		}

		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 15; j++) {
				char_grid[i][j] = turn_begin_char_grid[i][j];
			}
		}
		renderPlayerLetter(Players.currentPlayer);
		clickable = true;
	}

	public void trade() {
		CEState.play_clickable = true;
		CEState.limit = traded_letter.size();
		//CEState.current_player = Players.currentPlayer.getID();
		CEState.warning_text = "Click the START button to begin the minigame. Number of letters to get: " + traded_letter.size();

		traded_letter.forEach((l) -> {
			bag_of_letters.add(l);
		});

		nextTurn();
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics graphic) throws SlickException {
		entityManager.renderEntities(container, game, graphic);

		// Top UI
		graphic.setColor(new Color(0, 0, 0));
		graphic.drawString("Current Turn: " + (turn + 1), 750, 50);

		// Right UI
//		int word_spacing = 20;
//		for (int i = 0; i < current_words.size(); i++) {
//			graphic.drawString(current_words.get(i).getValue(), 700, 280 + i * word_spacing);
//		}

		/*
		player_0_name = players[0].getName();
		player_0_score = players[0].getScore();

		player_1_name = players[1].getName();
		player_1_score = players[1].getScore();

		player_2_name = players[2].getName();
		player_2_score = players[2].getScore();

		player_3_name = players[3].getName();
		player_3_score = players[3].getScore();*/

		//String[] players_ui = { player_0_name, player_1_name, player_2_name, player_3_name };
		//int[] players_ui_score = { player_0_score, player_1_score, player_2_score, player_3_score };
		
		String[] players_ui = new String[Players.getNumberOfPlayers()];
		int[] players_ui_score = new int[Players.getNumberOfPlayers()];
		for (int i = 0 ; i < Players.getNumberOfPlayers(); i++) {
			players_ui[i] = Players.getPlayers().get(i).getName();
			players_ui_score[i] = Players.getPlayers().get(i).getScore();
		}

		for (int i = 0; i < players_ui.length; i++) {
			if (players_ui[i].equals(displayPlayerID)) {
				graphic.setColor(new Color(255, 0, 0));
				graphic.drawString("Player " + players_ui[i] + ": " + players_ui_score[i], 90 + 160 * i, 50);
			} else {
				graphic.setColor(new Color(0, 0, 0));
				graphic.drawString("Player " + players_ui[i] + ": " + players_ui_score[i], 90 + 160 * i, 50);
			}
		}
		graphic.setColor(new Color(0, 0, 0));
		graphic.drawString(status_text,750,350);
		
		graphic.drawString("Current Score: " + current_total_score,700,300);

		graphic.setColor(new Color(255, 0, 0));
		graphic.drawString(warning_text, 90, 10);
		
		
		

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
