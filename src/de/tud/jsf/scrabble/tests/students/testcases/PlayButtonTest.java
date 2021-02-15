package de.tud.jsf.scrabble.tests.students.testcases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.newdawn.slick.Input;

import de.tud.jsf.scrabble.tests.adapter.ScrabbleTestAdapterMinimal;



public class PlayButtonTest {
	
	ScrabbleTestAdapterMinimal adapter;
	int x;
	int y;
	@Before
	public void setup() {
		adapter = new ScrabbleTestAdapterMinimal();
	}
	@After
	public void finish() {
		adapter.stopGame();
	}
	
	public void enteringGameplayState() {
		assertTrue(adapter.getStateBasedGame().getCurrentStateID() == adapter.getMainMenuStateID());
		adapter.handleMousePressed(adapter.getNewgameButtonX(),adapter.getNewgameButtonY(),0,0);
		assertTrue(adapter.getStateBasedGame().getCurrentStateID() == adapter.getPlayerSelectStateID());
		for (int i = 0 ; i < adapter.getPlayerSelectFieldsX().size() ; i ++) {
			int x = adapter.getPlayerSelectFieldsX().get(i);
			int y = adapter.getPlayerSelectFieldsY().get(i);
			adapter.handleMousePressed(x,y,0,0);
		}
		adapter.handleMousePressed(adapter.getStartButtonX(),adapter.getStartButtonY(),0,0);
		assertTrue(adapter.getStateBasedGame().getCurrentStateID() == adapter.getGameplayStateID());
		assertTrue(adapter.getCurrentNumberOfPlayers() == 4);
		x = adapter.getPlayButtonX();
		y = adapter.getPlayButtonY();
		
		
	}
	// Test starting score	
	//@Test
	public void testScore() {
		// TUTOR
		adapter.initGame();
		enteringGameplayState();
		Map<String,Integer> playerData = adapter.getPlayerData();
		for (String name : adapter.getPlayerNames()) {
			assertTrue(playerData.get(name) == 0);
		}
		adapter.stopGame();
		
	}
	
	// Invalid move test	
	//@Test
	public void testInvalidMove1() {
		// Not placing on middle field
		// STUDENT
		int testRow = 8;
		int testColumn = 9;
		adapter.initGame();
		enteringGameplayState();
		int oldScore = adapter.getScoreOfCurrentPlayer();
		String oldName = adapter.getNameOfCurrentPlayer();
		int oldTurn = adapter.getCurrentRound();
		adapter.handleMousePressed(adapter.getLettersInInventoryX().get(0),adapter.getLettersInInventoryY().get(0),0,0);
		adapter.handleMousePressed(adapter.getBoardX(testColumn),adapter.getBoardY(testRow),0,0);
		adapter.handleMousePressed(x,y,0,0);
		assertTrue(oldName == adapter.getNameOfCurrentPlayer());
		assertTrue(oldScore == adapter.getScoreOfCurrentPlayer());
		assertTrue(oldTurn == adapter.getCurrentRound());
		adapter.stopGame();
		
	}
	
	//@Test
	public void testInvalidMove2() {
		// Horizontal/Vertical placing
		// STUDENT
		int testRow = 8;
		int testColumn = 9;
		adapter.initGame();
		enteringGameplayState();
		int oldScore = adapter.getScoreOfCurrentPlayer();
		String oldName = adapter.getNameOfCurrentPlayer();
		int oldTurn = adapter.getCurrentRound();
		adapter.handleMousePressed(adapter.getLettersInInventoryX().get(0),adapter.getLettersInInventoryY().get(0),0,0);
		adapter.handleMousePressed(adapter.getBoardX(testColumn),adapter.getBoardY(testRow),0,0);
		adapter.handleMousePressed(adapter.getLettersInInventoryX().get(1),adapter.getLettersInInventoryY().get(1),0,0);
		adapter.handleMousePressed(adapter.getBoardX(testColumn),adapter.getBoardY(testRow+1),0,0);
		adapter.handleMousePressed(adapter.getLettersInInventoryX().get(2),adapter.getLettersInInventoryY().get(2),0,0);
		adapter.handleMousePressed(adapter.getBoardX(testColumn+1),adapter.getBoardY(testRow),0,0);
		adapter.handleMousePressed(x,y,0,0);
		assertTrue(oldName == adapter.getNameOfCurrentPlayer());
		assertTrue(oldScore == adapter.getScoreOfCurrentPlayer());
		assertTrue(oldTurn == adapter.getCurrentRound());
		adapter.stopGame();
	}
	
	//@Test
	public void testInvalidMove3() {
		// TUTOR
		// Pass then not place a letter on the middle field
		int testRow = 8;
		int testColumn = 9;
		adapter.initGame();
		enteringGameplayState();
		int nextScore = adapter.getScoreOfNextPlayer();
		String nextName = adapter.getNameOfNextPlayer();
		int nextTurn = adapter.getCurrentRound() + 1;
		adapter.handleMousePressed(x,y,0,0);
		assertTrue(nextName == adapter.getNameOfCurrentPlayer());
		assertTrue(nextScore == adapter.getScoreOfCurrentPlayer());
		assertTrue(nextTurn == adapter.getCurrentRound());
		adapter.handleMousePressed(adapter.getLettersInInventoryX().get(0),adapter.getLettersInInventoryY().get(0),0,0);
		adapter.handleMousePressed(adapter.getBoardX(testColumn),adapter.getBoardY(testRow),0,0);
		adapter.handleMousePressed(x,y,0,0);
		assertTrue(nextName == adapter.getNameOfCurrentPlayer());
		assertTrue(nextScore == adapter.getScoreOfCurrentPlayer());
		assertTrue(nextTurn == adapter.getCurrentRound());
		adapter.stopGame();
	}
	
	@Test
	public void testInvalidMove4() {
		// STUDENT
		// Scoring of 1 letter
		int testRow = 8;
		int testColumn = 8;
		adapter.initGame();
		enteringGameplayState();
		int oldScore = adapter.getScoreOfCurrentPlayer();
		String oldName = adapter.getNameOfCurrentPlayer();
		int oldTurn = adapter.getCurrentRound();
		adapter.handleMousePressed(adapter.getLettersInInventoryX().get(0),adapter.getLettersInInventoryY().get(0),0,0);
		adapter.handleMousePressed(adapter.getBoardX(testColumn),adapter.getBoardY(testRow),0,0);
		adapter.handleMousePressed(x,y,0,0);
		
		assertTrue(oldName == adapter.getNameOfCurrentPlayer());
		assertTrue(oldScore == adapter.getScoreOfCurrentPlayer());
		assertTrue(oldTurn == adapter.getCurrentRound());
		adapter.stopGame();
	}
	
	// Pass test
	//@Test
	public void testPass1() {
		// STUDENT
		adapter.initGame();
		enteringGameplayState();
		int nextScore = adapter.getScoreOfNextPlayer();
		String nextName = adapter.getNameOfNextPlayer();
		int nextTurn = adapter.getCurrentRound() + 1;
		adapter.handleMousePressed(x,y,0,0);
		assertTrue(nextName == adapter.getNameOfCurrentPlayer());
		assertTrue(nextScore == adapter.getScoreOfCurrentPlayer());
		assertTrue(nextTurn == adapter.getCurrentRound());
		adapter.stopGame();
	}
	
	
	// Scoring test
	@Test
	public void testScoring1() {
		// STUDENT
		// Scoring of word with length 2 without multiplier
		int testRow = 8;
		int testColumn = 8;
		adapter.initGame();
		enteringGameplayState();
	
		int oldScore = adapter.getScoreOfCurrentPlayer();
		String oldName = adapter.getNameOfCurrentPlayer();
		String nextName = adapter.getNameOfNextPlayer();
		int nextTurn = adapter.getCurrentRound() + 1;
		adapter.testing();
		int val1 = adapter.getLetterScore(adapter.getLetter(adapter.getLettersInInventoryX().get(0),adapter.getLettersInInventoryY().get(0)));
		int val2 = adapter.getLetterScore(adapter.getLetter(adapter.getLettersInInventoryX().get(1),adapter.getLettersInInventoryY().get(1)));
		adapter.handleMousePressed(adapter.getLettersInInventoryX().get(0),adapter.getLettersInInventoryY().get(0),0,0);
		adapter.handleMousePressed(adapter.getBoardX(testColumn),adapter.getBoardY(testRow),0,0);
		adapter.handleMousePressed(adapter.getLettersInInventoryX().get(1),adapter.getLettersInInventoryY().get(1),0,0);
		adapter.handleMousePressed(adapter.getBoardX(testColumn+1),adapter.getBoardY(testRow),0,0);
		adapter.handleMousePressed(x,y,0,0);
		adapter.testing();
		Map<String,Integer> playerData = adapter.getPlayerData();
		assertTrue(nextTurn == adapter.getCurrentRound());
		assertTrue(nextName == adapter.getNameOfCurrentPlayer());
		assertTrue("Score is not calculated correctly!\n"+
		"Expected:" + (val1+val2) + "\nGet:" + playerData.get(oldName),oldScore==playerData.get(oldName)-val1-val2);
		adapter.stopGame();
	}
	
	
	
	
}
