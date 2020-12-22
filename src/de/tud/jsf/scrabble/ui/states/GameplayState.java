package de.tud.jsf.scrabble.ui.states;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Set;

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

public class GameplayState extends BasicGameState implements GameParameters{
	
	private int stateID; 				
	private StateBasedEntityManager entityManager;
	
	// Gameplay variables
	private char[][] char_grid = new char[BOARDSIZE][BOARDSIZE];
	private Board b = new Board(new Vector2f(BOARD_START_X,BOARD_START_y));
	private Tile[][] tiles = b.buildBoard();
	private Lexicon lexicon = new Lexicon();
	private static ArrayList<Word> current_words = new ArrayList<>();
	
	// Functional variables
	private boolean move_letter = false;
	private Letter tmp_letter = null;
	private boolean clickable = true;
	
	//String UI
	private static String most_recent_horizontal_word = "";
	private static int most_recent_horizontal_score = 0;
	
	private static String most_recent_vertical_word = "";
	private static int most_recent_vertical_score = 0;
	
	
	GameplayState(int sid ) {
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
		
		for (int i = 0 ; i < BOARDSIZE ; i ++) {
			for (int j = 0 ; j < BOARDSIZE ; j ++) {
				StateBasedEntityManager.getInstance().addEntity(stateID, tiles[i][j]);
				moveLetterToBoardTile(tiles[i][j]);
			}
		}
		// Create LETTER
		
		Set<Character> alphabet = Set.of('a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z', '_');
		
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
			Vector2f tv = new Vector2f(800, 400 + 50*i);
			Letter tmp = new Letter("letter_" + Integer.toString(i), c, tv);
			moveLetterToBoardLetter(tmp);
			StateBasedEntityManager.getInstance().addEntity(stateID, tmp);
		}
		
		// Dialogue boxes
		
		Vector2f tv = new Vector2f(800, 360);
		DialogueButton confirm = new DialogueButton("nyaa_test", tv, "confirm");
		confirm.addImageComponent();
		StateBasedEntityManager.getInstance().addEntity(stateID, confirm);
		triggerDialogueBox(confirm);
		
		// Test
//		ArrayList<Tile> w1t = new ArrayList<>();
//		w1t.add(tiles[0][1]);
//		w1t.add(tiles[0][2]);
//		w1t.add(tiles[0][3]);
//		
//		ArrayList<Tile> w2t = new ArrayList<>();
//		w2t.add(tiles[0][1]);
//		w2t.add(tiles[1][1]);
//		w2t.add(tiles[2][1]);
//		//w2t.add(tiles[0][2]);
//		//w2t.add(tiles[0][3]);
//		
//		Word w1 = new Word("test_1", 0, w1t);
//		Word w2 = new Word("test_2", 0, w2t);
//		
//		System.out.println(isNewWord(w1, w2));
		
		/*
		Vector2f t = new Vector2f(400, 360);
		DialogueBox dbox_test = new DialogueBox("db_test", t, 50);
		
		StateBasedEntityManager.getInstance().addEntity(stateID, dbox_test);
		
		for (DialogueButton b : dbox_test.getButtons()) {
			StateBasedEntityManager.getInstance().addEntity(stateID, b);
			if(b.getID().contains("cancel")) {
				destroyDialogueBox(b);
			}
		}	
		*/
	}
	
	// Gameplay Actions
	
	public void triggerDialogueBox(DialogueButton button) {
		ArrayList<Entity> tmp_rmv = new ArrayList<>();
		ANDEvent clickEvent = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		Action clickOnButton = new Action() {
			@Override
			public void update(GameContainer arg0, StateBasedGame arg1, int arg2, Component arg3) {
				Vector2f t = new Vector2f(DBOX_START_X, DBOX_START_Y);
				DialogueBox dbox_test = new DialogueBox("db_test", t, 50);
				
				for (Entity e : getEntityByPos(new Vector2f (340, 420))) {
					tmp_rmv.add(e);
				}
				
				for (Entity e : getEntityByPos(new Vector2f (460, 420))) {
					tmp_rmv.add(e);
				}
				
				for (Entity e : tmp_rmv) {
					StateBasedEntityManager.getInstance().removeEntity(stateID, e);
				}
				StateBasedEntityManager.getInstance().addEntity(stateID, dbox_test);
				for (DialogueButton b : dbox_test.getButtons()) {
					StateBasedEntityManager.getInstance().addEntity(stateID, b);
					if(b.getID().contains("cancel")) {
						destroyDialogueBoxCancelButton(b, tmp_rmv);
					}
				}
				
				clickable = false;
			}
		};
		clickEvent.addAction(clickOnButton);
		button.addComponent(clickEvent);
	}
	
	public void destroyDialogueBoxCancelButton(DialogueButton button, ArrayList<Entity> tmp_rmv) {
		ANDEvent clickEvent = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		Action clickOnButton = new Action() {
			@Override
			public void update(GameContainer arg0, StateBasedGame arg1, int arg2, Component arg3) {
				DialogueBox box = button.getDialogueBox();
				StateBasedEntityManager.getInstance().removeEntity(stateID, box.getButtons()[0]);
				StateBasedEntityManager.getInstance().removeEntity(stateID, box);
				StateBasedEntityManager.getInstance().removeEntity(stateID, button);
				
				for (Entity e : tmp_rmv) {
					StateBasedEntityManager.getInstance().addEntity(stateID, e);
				}
				clickable = true;
			}
		};
		clickEvent.addAction(clickOnButton);
		button.addComponent(clickEvent);
	}
	
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
				if(!clickable) return;
				if (!move_letter) return;				
				Letter new_letter = new Letter(tmp_letter.getID(), tmp_letter.getValue(), t.getPosition());
				
				int x = Integer.parseInt(t.getID().split("_")[0]);
				int y = Integer.parseInt(t.getID().split("_")[1]);
				char_grid[x][y] = new_letter.getValue();
				
				StateBasedEntityManager.getInstance().removeEntity(stateID, entityManager.getEntity(stateID, tmp_letter.getID()));
				StateBasedEntityManager.getInstance().addEntity(stateID, new_letter);
				
				move_letter = false;
				tmp_letter = null;
				
				System.out.println("current grid:");

				for(int i1 = 0; i1 < 15; i1++) {
					for(int j1 = 0; j1 < 15; j1++) {
						System.out.print("[" + char_grid[i1][j1] + "]");
					}
					System.out.println(" ");
				}
				
				Word[] curr_words = getWord(x,y);
				
				most_recent_horizontal_word = curr_words[0].getValue();
				most_recent_horizontal_score = curr_words[0].getScore();
				
				if(most_recent_horizontal_score > 0) addToCurrentWord(curr_words[0]);
//				for (Tile t : curr_words[0].getTiles()) {
//					System.out.print(t.getID() + " ");
//				}
//				System.out.println("");
				most_recent_vertical_word = curr_words[1].getValue();
				most_recent_vertical_score = curr_words[1].getScore();
				if(most_recent_vertical_score > 0) addToCurrentWord(curr_words[1]);
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
		
		for (Entity e : StateBasedEntityManager.getInstance().getEntitiesByState(stateID)) {
			if (e.getPosition().getX() == pos.getX() && e.getPosition().getY() == pos.getY()) {
				result.add(e);
			}
		}
		return result;
	}
	
	public boolean[] isNewWord(Word current_word, Word new_word) {
		ArrayList<Tile> current_tiles = current_word.getTiles();
		ArrayList<Tile> new_tiles = new_word.getTiles();
		
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
	}
	
	public void addToCurrentWord(Word new_word) {
		boolean completely_new = true;
		for (Word w : current_words) {
			System.out.println(isNewWord(w, new_word)[0]);
			System.out.println(isNewWord(w, new_word)[1]);
			 if(!isNewWord(w, new_word)[0]) {
				 completely_new = false;
			 }  
			 if (isNewWord(w, new_word)[1]){
				 System.out.println("here");
				 current_words.remove(w);
				 current_words.add(new_word);
			 }
		}
		if(completely_new) current_words.add(new_word);
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
		ArrayList<Tile> word_tiles_v = new ArrayList<>();
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
		
		if (lexicon.check(word)) {
			word_score = score * word_multiplier;
		} 
		
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
		
		if (lexicon.check(word)) {
			word_score = score * word_multiplier;
		}
		
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
		
		graphic.drawString("Horizontal: " + most_recent_horizontal_word, 700, 180);
		graphic.drawString("Score: " + most_recent_horizontal_score, 700, 200);
		graphic.drawString("Vertical: " + most_recent_vertical_word, 700, 240);
		graphic.drawString("Score: " + most_recent_vertical_score, 700, 260);
		int margin = 20;
		
		for(int i = 0 ; i < current_words.size(); i ++) {
			graphic.drawString(current_words.get(i).getValue(), 700, 280 + i * margin);
		}
		
		
		
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
