package de.tud.jsf.scrabble.tests.students.testcases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;

import de.tud.jsf.scrabble.tests.adapter.ScrabbleTestAdapterMinimal;

public class PlayButtonTest {
	
	ScrabbleTestAdapterMinimal adapter;
	@Before
	public void setup() {
		adapter = new ScrabbleTestAdapterMinimal();
	}
	@After
	public void finish() {
		adapter.stopGame();
	}
	
	public void enteringGameplayState() {		
		assertTrue("Wrong state",adapter.getStateBasedGame().getCurrentStateID() == adapter.getMainMenuStateID());
		adapter.handleMousePressed(adapter.getNewgameButtonPosition(),0,0);
		assertTrue(adapter.getStateBasedGame().getCurrentStateID() == adapter.getPlayerSelectStateID());
		for (int i = 0 ; i < adapter.getPlayerSelectFieldsPosition().size() ; i ++) {
			adapter.handleMousePressed(adapter.getPlayerSelectFieldsPosition().get(i),0,0);
		}
		adapter.handleMousePressed(adapter.getStartButtonPosition(),0,0);
		assertTrue("Wrong state",adapter.getStateBasedGame().getCurrentStateID() == adapter.getGameplayStateID());
		assertTrue("Wrong number of players",adapter.getCurrentNumberOfPlayers() == 4);		
	}
	
	@Test
	public void testGetLetterScore() {
		// STUDENT
		adapter.initGame();
		assertEquals("Letter 'a' should have value " + 1, 1,adapter.getLetterScore('a'));
		assertEquals("Letter 'e' should have value " + 1, 1,adapter.getLetterScore('e'));
		assertEquals("Letter 'i' should have value " + 1, 1,adapter.getLetterScore('i'));
		assertEquals("Letter 'o' should have value " + 1, 1,adapter.getLetterScore('o'));
		assertEquals("Letter 'n' should have value " + 1, 1,adapter.getLetterScore('n'));
		assertEquals("Letter 'r' should have value " + 1, 1,adapter.getLetterScore('r'));
		assertEquals("Letter 't' should have value " + 1, 1,adapter.getLetterScore('t'));
		assertEquals("Letter 'l' should have value " + 1, 1,adapter.getLetterScore('l'));
		assertEquals("Letter 's' should have value " + 1, 1,adapter.getLetterScore('s'));
		assertEquals("Letter 'u' should have value " + 1, 1,adapter.getLetterScore('u'));
		
		assertEquals("Letter 'd' should have value" + 2, 2,adapter.getLetterScore('d'));
		assertEquals("Letter 'g' should have value" + 2, 2,adapter.getLetterScore('g'));
		assertEquals("Letter 'b' should have value" + 3, 3,adapter.getLetterScore('b'));
		assertEquals("Letter 'c' should have value" + 3, 3,adapter.getLetterScore('c'));
		assertEquals("Letter 'm' should have value" + 3, 3,adapter.getLetterScore('m'));
		assertEquals("Letter 'p' should have value" + 3, 3,adapter.getLetterScore('p'));
		assertEquals("Letter 'f' should have value" + 4, 4,adapter.getLetterScore('f'));
		assertEquals("Letter 'h' should have value" + 4, 4,adapter.getLetterScore('h'));
		assertEquals("Letter 'v' should have value" + 4, 4,adapter.getLetterScore('v'));
		assertEquals("Letter 'w' should have value" + 4, 4,adapter.getLetterScore('w'));
		assertEquals("Letter 'y' should have value" + 4, 4,adapter.getLetterScore('y'));
		assertEquals("Letter 'k' should have value" + 5, 5,adapter.getLetterScore('k'));
		assertEquals("Letter 'j' should have value" + 8, 8,adapter.getLetterScore('j'));
		assertEquals("Letter 'x' should have value" + 8, 8,adapter.getLetterScore('x'));
		assertEquals("Letter 'q' should have value" + 10, 10,adapter.getLetterScore('q'));
		assertEquals("Letter 'z' should have value" + 10, 10,adapter.getLetterScore('z'));
		
		
	}
	// Test starting score	
	@Test
	public void testStartingScore() {
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
	@Test
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
		Vector2f letterPos = adapter.getLettersInInventoryPosition().get(0);
		Vector2f fieldPos = adapter.getFieldPosition(testRow,testColumn);
		adapter.handleMousePressed(letterPos,0,0);
		adapter.handleMousePressed(fieldPos,0,0);
		adapter.handleMousePressed(adapter.getPlayButtonPosition(),0,0);
		assertTrue("Player making an invalid move cannot move on!",oldName == adapter.getNameOfCurrentPlayer());
		assertTrue("Player making an invalid move cannot move on!",oldScore == adapter.getScoreOfCurrentPlayer());
		assertTrue("Player making an invalid move cannot move on!",oldTurn == adapter.getCurrentRound());
		adapter.stopGame();
		
	}
	
	@Test
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
		List<Vector2f> inventory = adapter.getLettersInInventoryPosition();
		Vector2f letterPos = inventory.get(0);
		Vector2f fieldPos1 = adapter.getFieldPosition(testRow,testColumn);
		Vector2f fieldPos2 = adapter.getFieldPosition(testRow+1,testColumn);
		Vector2f fieldPos3= adapter.getFieldPosition(testRow,testColumn+1);
		adapter.handleMousePressed(letterPos,0,0);
		adapter.handleMousePressed(fieldPos1,0,0);
		inventory = adapter.getLettersInInventoryPosition();
		letterPos = inventory.get(0);
		adapter.handleMousePressed(letterPos,0,0);
		adapter.handleMousePressed(fieldPos2,0,0);
		inventory = adapter.getLettersInInventoryPosition();
		letterPos = inventory.get(0);
		adapter.handleMousePressed(letterPos,0,0);
		adapter.handleMousePressed(fieldPos3,0,0);
		adapter.handleMousePressed(adapter.getPlayButtonPosition(),0,0);
		assertTrue("Player making an invalid move cannot move on!",oldName == adapter.getNameOfCurrentPlayer());
		assertTrue("Player making an invalid move cannot move on!",oldScore == adapter.getScoreOfCurrentPlayer());
		assertTrue("Player making an invalid move cannot move on!",oldTurn == adapter.getCurrentRound());
		adapter.stopGame();
	}
	
	@Test
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
		List<Vector2f> inventory = adapter.getLettersInInventoryPosition();
		Vector2f letterPos = inventory.get(0);
		Vector2f fieldPos = adapter.getFieldPosition(testRow,testColumn);
		adapter.handleMousePressed(adapter.getPlayButtonPosition(),0,0);
		assertTrue("Pass not working!",nextName == adapter.getNameOfCurrentPlayer());
		assertTrue("Pass not working!",nextScore == adapter.getScoreOfCurrentPlayer());
		assertTrue("Pass not working!",nextTurn == adapter.getCurrentRound());
		adapter.handleMousePressed(letterPos,0,0);
		adapter.handleMousePressed(fieldPos,0,0);
		adapter.handleMousePressed(adapter.getPlayButtonPosition(),0,0);
		assertTrue("Player making an invalid move cannot move on!",nextName == adapter.getNameOfCurrentPlayer());
		assertTrue("Player making an invalid move cannot move on!",nextScore == adapter.getScoreOfCurrentPlayer());
		assertTrue("Player making an invalid move cannot move on!",nextTurn == adapter.getCurrentRound());
		adapter.stopGame();
	}
	
	@Test
	public void testInvalidMove4() {
		// STUDENT
		// Placing only 1 letter at start
		int testRow = 8;
		int testColumn = 8;
		adapter.initGame();
		enteringGameplayState();
		int oldScore = adapter.getScoreOfCurrentPlayer();
		String oldName = adapter.getNameOfCurrentPlayer();
		int oldTurn = adapter.getCurrentRound();
		List<Vector2f> inventory = adapter.getLettersInInventoryPosition();
		Vector2f letterPos = inventory.get(0);
		Vector2f fieldPos = adapter.getFieldPosition(testRow,testColumn);
		adapter.handleMousePressed(letterPos,0,0);
		adapter.handleMousePressed(fieldPos,0,0);
		adapter.handleMousePressed(adapter.getPlayButtonPosition(),0,0);		
		assertTrue("Player making an invalid move cannot move on!",oldName == adapter.getNameOfCurrentPlayer());
		assertTrue("Player making an invalid move cannot move on!",oldScore == adapter.getScoreOfCurrentPlayer());
		assertTrue("Player making an invalid move cannot move on!",oldTurn == adapter.getCurrentRound());
		adapter.stopGame();
	}
	
	// Pass test
	@Test
	public void testPass1() {
		// STUDENT
		adapter.initGame();
		enteringGameplayState();
		int nextScore = adapter.getScoreOfNextPlayer();
		String nextName = adapter.getNameOfNextPlayer();
		int nextTurn = adapter.getCurrentRound() + 1;
		adapter.handleMousePressed(adapter.getPlayButtonPosition(),0,0);
		assertTrue("Wrong player name!",nextName == adapter.getNameOfCurrentPlayer());
		assertTrue("Wrong player score!",nextScore == adapter.getScoreOfCurrentPlayer());
		assertTrue("Wrong turn count!",nextTurn == adapter.getCurrentRound());
		adapter.stopGame();
	}
	
	
	// Scoring test
	@Test
	public void testScoring0() {
		// STUDENT
		// Scoring of word with length 2 with star multiplier (2x word)
		int testRow = 8;
		int testColumn = 8;
		adapter.initGame();
		enteringGameplayState();
	
		int oldScore = adapter.getScoreOfCurrentPlayer();
		String oldName = adapter.getNameOfCurrentPlayer();
		String nextName = adapter.getNameOfNextPlayer();
		int nextTurn = adapter.getCurrentRound() + 1;
		for (int i = 0 ; i <2 ; i ++) {
			adapter.handleMousePressed(adapter.getLettersInInventoryPosition().get(0),0,0);
			adapter.handleMousePressed(adapter.getFieldPosition(testRow,testColumn+i),0,0);
		}
		adapter.handleMousePressed(adapter.getPlayButtonPosition(),0,0);

		int val1 = adapter.getLetterScore(adapter.getBoard()[testRow-1][testColumn-1]);
		int val2 = adapter.getLetterScore(adapter.getBoard()[testRow-1][testColumn]);
		Map<String,Integer> playerData = adapter.getPlayerData();
		assertTrue("Wrong turn count!",nextTurn == adapter.getCurrentRound());
		assertTrue("Wrong player name!",nextName == adapter.getNameOfCurrentPlayer());
		assertTrue("Score is not calculated correctly!\n"+
		"Expected:" + (val1+val2)*2 + "\nGet:" + playerData.get(oldName),oldScore==playerData.get(oldName)-(val1+val2)*2);
		adapter.stopGame();
	}
	@Test
	public void testScoring1() {
		// STUDENT
		// Scoring without multipliers
		int testRow = 8;
		int testColumn = 8;
		adapter.initGame();
		enteringGameplayState();
		
		for (int i = 0 ; i <2 ; i ++) {
			adapter.handleMousePressed(adapter.getLettersInInventoryPosition().get(0),0,0);
			adapter.handleMousePressed(adapter.getFieldPosition(testRow,testColumn+i),0,0);
		}
		adapter.handleMousePressed(adapter.getPlayButtonPosition(),0,0);	
		int oldScore = adapter.getScoreOfCurrentPlayer();
		String oldName = adapter.getNameOfCurrentPlayer();
		String nextName = adapter.getNameOfNextPlayer();
		int nextTurn = adapter.getCurrentRound() + 1;
		testColumn = 10;
		for (int i = 0 ; i <2 ; i ++) {
			adapter.handleMousePressed(adapter.getLettersInInventoryPosition().get(0),0,0);
			adapter.handleMousePressed(adapter.getFieldPosition(testRow+i,testColumn),0,0);
		}
		int val = adapter.getLetterScore(adapter.getBoard()[testRow-1][testColumn-1])*2
				+ adapter.getLetterScore(adapter.getBoard()[testRow][testColumn-1])
				+ adapter.getLetterScore(adapter.getBoard()[7][7])
				+ adapter.getLetterScore(adapter.getBoard()[7][8]);
		adapter.handleMousePressed(adapter.getPlayButtonPosition(),0,0);
		Map<String,Integer> playerData = adapter.getPlayerData();
		assertTrue("Wrong turn count!",nextTurn == adapter.getCurrentRound());
		assertTrue("Wrong player name!",nextName == adapter.getNameOfCurrentPlayer());
		assertTrue("Score is not calculated correctly!\n"+
		"Expected:" + (val) + "\nGet:" + playerData.get(oldName),oldScore==playerData.get(oldName)-(val));
		adapter.stopGame();
	}
	@Test
	public void testScoring2() {
		// STUDENT
		// Scoring with multiplier 2x letter
		int testRow = 8;
		int testColumn = 8;
		adapter.initGame();
		enteringGameplayState();
		
		for (int i = 0 ; i <2 ; i ++) {
			adapter.handleMousePressed(adapter.getLettersInInventoryPosition().get(0),0,0);
			adapter.handleMousePressed(adapter.getFieldPosition(testRow,testColumn+i),0,0);
		}
		adapter.handleMousePressed(adapter.getPlayButtonPosition(),0,0);
		
		int oldScore = adapter.getScoreOfCurrentPlayer();
		String oldName = adapter.getNameOfCurrentPlayer();
		String nextName = adapter.getNameOfNextPlayer();
		int nextTurn = adapter.getCurrentRound() + 1;
		testColumn = 9;
		for (int i = 0 ; i < 2 ; i ++) {
			adapter.handleMousePressed(adapter.getLettersInInventoryPosition().get(0),0,0);
			adapter.handleMousePressed(adapter.getFieldPosition(testRow-1 + i*2,testColumn),0,0);
		}
		int val = adapter.getLetterScore(adapter.getBoard()[testRow-2][testColumn-1])*2
				+ adapter.getLetterScore(adapter.getBoard()[testRow-1][testColumn-1])
				+ adapter.getLetterScore(adapter.getBoard()[testRow][testColumn-1])*2;
		adapter.handleMousePressed(adapter.getPlayButtonPosition(),0,0);
		Map<String,Integer> playerData = adapter.getPlayerData();
		assertTrue("Wrong player name!",nextTurn == adapter.getCurrentRound());
		assertTrue("Wrong turn count!",nextName == adapter.getNameOfCurrentPlayer());
		assertTrue("Score is not calculated correctly!\n"+
		"Expected:" + (val) + "\nGet:" + playerData.get(oldName),oldScore==playerData.get(oldName)-(val));
		adapter.stopGame();
	}
		
	@Test
	public void testScoring3() {
		// STUDENT
		// Scoring of word multiplier 2x word
		int testRow = 8;
		int testColumn = 8;
		adapter.initGame();
		enteringGameplayState();
	

		List<Vector2f> inventory = adapter.getLettersInInventoryPosition();
		Vector2f letterPos = inventory.get(0);
		
		Vector2f fieldPos = adapter.getFieldPosition(testRow,testColumn);
		adapter.handleMousePressed(letterPos,0,0);
		adapter.handleMousePressed(fieldPos,0,0);
		letterPos = adapter.getLettersInInventoryPosition().get(0);
		fieldPos = adapter.getFieldPosition(testRow,testColumn+1);
		adapter.handleMousePressed(letterPos,0,0);
		adapter.handleMousePressed(fieldPos,0,0);
		letterPos = adapter.getLettersInInventoryPosition().get(0);
		fieldPos = adapter.getFieldPosition(testRow,testColumn+2);
		adapter.handleMousePressed(letterPos,0,0);
		adapter.handleMousePressed(fieldPos,0,0);
		letterPos = adapter.getLettersInInventoryPosition().get(0);
		fieldPos = adapter.getFieldPosition(testRow,testColumn+3);
		adapter.handleMousePressed(letterPos,0,0);
		adapter.handleMousePressed(fieldPos,0,0);
		adapter.handleMousePressed(adapter.getPlayButtonPosition(),0,0);

		int oldScore = adapter.getScoreOfCurrentPlayer();
		String oldName = adapter.getNameOfCurrentPlayer();
		String nextName = adapter.getNameOfNextPlayer();
		int nextTurn = adapter.getCurrentRound() + 1;
		testColumn = 11;
		testRow = 7;
		letterPos = adapter.getLettersInInventoryPosition().get(0);
		fieldPos = adapter.getFieldPosition(testRow,testColumn);
		adapter.handleMousePressed(letterPos,0,0);
		adapter.handleMousePressed(fieldPos,0,0);
		letterPos = adapter.getLettersInInventoryPosition().get(0);
		fieldPos = adapter.getFieldPosition(testRow-1,testColumn);
		adapter.handleMousePressed(letterPos,0,0);
		adapter.handleMousePressed(fieldPos,0,0);
		letterPos = adapter.getLettersInInventoryPosition().get(0);
		fieldPos = adapter.getFieldPosition(testRow-2,testColumn);
		adapter.handleMousePressed(letterPos,0,0);
		adapter.handleMousePressed(fieldPos,0,0);
		int val = adapter.getLetterScore(adapter.getBoard()[testRow-1][testColumn-1]) 
				+ adapter.getLetterScore(adapter.getBoard()[testRow-2][testColumn-1])
				+ adapter.getLetterScore(adapter.getBoard()[testRow-3][testColumn-1])
				+ adapter.getLetterScore(adapter.getBoard()[testRow][testColumn-1]);
		adapter.handleMousePressed(adapter.getPlayButtonPosition(),0,0);
		Map<String,Integer> playerData = adapter.getPlayerData();
		assertTrue("Wrong turn!",nextTurn == adapter.getCurrentRound());
		assertTrue("Wrong player name!",nextName == adapter.getNameOfCurrentPlayer());
		assertTrue("Score is not calculated correctly!\n"+
		"Expected:" + val*2 + "\nGet:" + playerData.get(oldName),oldScore==playerData.get(oldName)-val*2);
		adapter.stopGame();
	}
	@Test
	public void testScoring4() {
		// TUTOR
		// Scoring of Bingo + 2x Star Multiplier + 2x letter 		
		int testRow = 8;
		int testColumn = 8;
		adapter.initGame();
		enteringGameplayState();
		int oldScore = adapter.getScoreOfCurrentPlayer();
		String oldName = adapter.getNameOfCurrentPlayer();
		String nextName = adapter.getNameOfNextPlayer();
		int nextTurn = adapter.getCurrentRound() + 1;
		for (int i = 0 ; i < 7 ; i ++) {
			List<Vector2f> inventory = adapter.getLettersInInventoryPosition();
			Vector2f letterPos = inventory.get(0);		
			Vector2f fieldPos = adapter.getFieldPosition(testRow,testColumn+i);
			adapter.handleMousePressed(letterPos,0,0);
			adapter.handleMousePressed(fieldPos,0,0);
		}	
		adapter.handleMousePressed(adapter.getPlayButtonPosition(),0,0);
		int val = adapter.getLetterScore(adapter.getBoard()[testRow-1][testColumn-1]) 
				+ adapter.getLetterScore(adapter.getBoard()[testRow-1][testColumn])
				+ adapter.getLetterScore(adapter.getBoard()[testRow-1][testColumn+1])
				+ adapter.getLetterScore(adapter.getBoard()[testRow-1][testColumn+2])
				+ adapter.getLetterScore(adapter.getBoard()[testRow-1][testColumn+3])*2
				+ adapter.getLetterScore(adapter.getBoard()[testRow-1][testColumn+4])
				+ adapter.getLetterScore(adapter.getBoard()[testRow-1][testColumn+5]);
		Map<String,Integer> playerData = adapter.getPlayerData();
		assertTrue("Wrong turn!",nextTurn == adapter.getCurrentRound());
		assertTrue("Wrong player name!",nextName == adapter.getNameOfCurrentPlayer());
		assertTrue("Score is not calculated correctly!\n"+
		"Expected:" + (val*2 + 50) + "\nGet:" + playerData.get(oldName),oldScore==playerData.get(oldName)-(val*2 + 50));
		adapter.stopGame();
	}

	
	@Test
	public void testScoring5() {
		// TUTOR
		// Scoring of multipliers 2x word + 2x word
		int testRow = 8;
		int testColumn = 8;
		adapter.initGame();
		enteringGameplayState();
		
		for (int i = 0 ; i < 4 ; i ++) {
			adapter.handleMousePressed(adapter.getLettersInInventoryPosition().get(0),0,0);
			adapter.handleMousePressed(adapter.getFieldPosition(testRow,testColumn+i),0,0);
		}
		adapter.handleMousePressed(adapter.getPlayButtonPosition(),0,0);

		int oldScore = adapter.getScoreOfCurrentPlayer();
		String oldName = adapter.getNameOfCurrentPlayer();
		String nextName = adapter.getNameOfNextPlayer();
		int nextTurn = adapter.getCurrentRound() + 1;
		testColumn = 11;
		adapter.handleMousePressed(adapter.getLettersInInventoryPosition().get(0),0,0);
		adapter.handleMousePressed(adapter.getFieldPosition(testRow-1,testColumn),0,0);
		adapter.handleMousePressed(adapter.getLettersInInventoryPosition().get(0),0,0);
		adapter.handleMousePressed(adapter.getFieldPosition(testRow+1,testColumn),0,0);
		adapter.handleMousePressed(adapter.getLettersInInventoryPosition().get(0),0,0);
		adapter.handleMousePressed(adapter.getFieldPosition(testRow-2,testColumn),0,0);
		adapter.handleMousePressed(adapter.getLettersInInventoryPosition().get(0),0,0);
		adapter.handleMousePressed(adapter.getFieldPosition(testRow+2,testColumn),0,0);
		adapter.handleMousePressed(adapter.getLettersInInventoryPosition().get(0),0,0);
		adapter.handleMousePressed(adapter.getFieldPosition(testRow-3,testColumn),0,0);
		adapter.handleMousePressed(adapter.getLettersInInventoryPosition().get(0),0,0);
		adapter.handleMousePressed(adapter.getFieldPosition(testRow+3,testColumn),0,0);
		adapter.handleMousePressed(adapter.getLettersInInventoryPosition().get(0),0,0);
		adapter.handleMousePressed(adapter.getPlayButtonPosition(),0,0);
		int val = adapter.getLetterScore(adapter.getBoard()[testRow-4][testColumn-1]) 
				+ adapter.getLetterScore(adapter.getBoard()[testRow-3][testColumn-1])
				+ adapter.getLetterScore(adapter.getBoard()[testRow-2][testColumn-1])				
				+ adapter.getLetterScore(adapter.getBoard()[testRow-1][testColumn-1])
				+ adapter.getLetterScore(adapter.getBoard()[testRow][testColumn-1])
				+ adapter.getLetterScore(adapter.getBoard()[testRow+1][testColumn-1])
				+ adapter.getLetterScore(adapter.getBoard()[testRow+2][testColumn-1]);
		Map<String,Integer> playerData = adapter.getPlayerData();
		assertTrue("Wrong turn!",nextTurn == adapter.getCurrentRound());
		assertTrue("Wrong player name!",nextName == adapter.getNameOfCurrentPlayer());
		assertTrue("Score is not calculated correctly!\n"+
		"Expected:" + (val*4) + "\nGet:" + playerData.get(oldName),oldScore==playerData.get(oldName)-val*4);
		adapter.stopGame();
	}
	
	@Test
	public void testScoring6() {
		// TUTOR
		// Scoring of 3x word + multiplier elimination
		int testRow = 8;
		int testColumn = 8;
		adapter.initGame();
		enteringGameplayState();
		
		for (int i = 0 ; i < 7 ; i ++) {
			List<Vector2f> inventory = adapter.getLettersInInventoryPosition();
			Vector2f letterPos = inventory.get(0);		
			Vector2f fieldPos = adapter.getFieldPosition(testRow,testColumn+i);
			adapter.handleMousePressed(letterPos,0,0);
			adapter.handleMousePressed(fieldPos,0,0);
		}	
		adapter.handleMousePressed(adapter.getPlayButtonPosition(),0,0);

		int oldScore = adapter.getScoreOfCurrentPlayer();
		String oldName = adapter.getNameOfCurrentPlayer();
		String nextName = adapter.getNameOfNextPlayer();
		int nextTurn = adapter.getCurrentRound() + 1;
		adapter.handleMousePressed(adapter.getLettersInInventoryPosition().get(0),0,0);
		adapter.handleMousePressed(adapter.getFieldPosition(8,15),0,0);
		adapter.handleMousePressed(adapter.getPlayButtonPosition(),0,0);
		int val = adapter.getLetterScore(adapter.getBoard()[testRow-1][testColumn-1]) 
				+ adapter.getLetterScore(adapter.getBoard()[testRow-1][testColumn])
				+ adapter.getLetterScore(adapter.getBoard()[testRow-1][testColumn+1])				
				+ adapter.getLetterScore(adapter.getBoard()[testRow-1][testColumn+2])
				+ adapter.getLetterScore(adapter.getBoard()[testRow-1][testColumn+3])
				+ adapter.getLetterScore(adapter.getBoard()[testRow-1][testColumn+4])
				+ adapter.getLetterScore(adapter.getBoard()[testRow-1][testColumn+5])
				+ adapter.getLetterScore(adapter.getBoard()[testRow-1][testColumn+6]);
		Map<String,Integer> playerData = adapter.getPlayerData();
		assertTrue("Wrong turn!",nextTurn == adapter.getCurrentRound());
		assertTrue("Wrong player name!",nextName == adapter.getNameOfCurrentPlayer());
		assertTrue("Score is not calculated correctly!\n"+
		"Expected:" + (val*3) + "\nGet:" + playerData.get(oldName),oldScore==playerData.get(oldName)-val*3);
		adapter.stopGame();
	}
	
	@Test	
	public void testScoring7() {
		// TUTOR
		// Scoring of word with multiplier 2x letter + 2x word
		int testRow = 8;
		int testColumn = 8;
		adapter.initGame();
		enteringGameplayState();
	
		int oldScore = adapter.getScoreOfCurrentPlayer();
		String oldName = adapter.getNameOfCurrentPlayer();
		String nextName = adapter.getNameOfNextPlayer();
		int nextTurn = adapter.getCurrentRound() + 1;
		List<Vector2f> inventory = adapter.getLettersInInventoryPosition();
		Vector2f letterPos = inventory.get(0);
		
		Vector2f fieldPos1 = adapter.getFieldPosition(testRow,testColumn);
		Vector2f fieldPos2= adapter.getFieldPosition(testRow,testColumn+1);
		Vector2f fieldPos3 = adapter.getFieldPosition(testRow,testColumn+2);
		Vector2f fieldPos4= adapter.getFieldPosition(testRow,testColumn+3);
		Vector2f fieldPos5 = adapter.getFieldPosition(testRow,testColumn+4);
		adapter.handleMousePressed(letterPos,0,0);
		adapter.handleMousePressed(fieldPos1,0,0);
		letterPos = adapter.getLettersInInventoryPosition().get(0);
		adapter.handleMousePressed(letterPos,0,0);
		adapter.handleMousePressed(fieldPos2,0,0);
		letterPos = adapter.getLettersInInventoryPosition().get(0);
		adapter.handleMousePressed(letterPos,0,0);
		adapter.handleMousePressed(fieldPos3,0,0);
		letterPos = adapter.getLettersInInventoryPosition().get(0);
		adapter.handleMousePressed(letterPos,0,0);
		adapter.handleMousePressed(fieldPos4,0,0);
		letterPos = adapter.getLettersInInventoryPosition().get(0);
		adapter.handleMousePressed(letterPos,0,0);
		adapter.handleMousePressed(fieldPos5,0,0);
		int val = adapter.getLetterScore(adapter.getBoard()[testRow-1][testColumn-1]) 
				+ adapter.getLetterScore(adapter.getBoard()[testRow-1][testColumn])
				+ adapter.getLetterScore(adapter.getBoard()[testRow-1][testColumn+1])
				+ adapter.getLetterScore(adapter.getBoard()[testRow-1][testColumn+2])
				+ adapter.getLetterScore(adapter.getBoard()[testRow-1][testColumn+3])*2;
		adapter.handleMousePressed(adapter.getPlayButtonPosition(),0,0);		
		Map<String,Integer> playerData = adapter.getPlayerData();
		assertTrue("Wrong turn!",nextTurn == adapter.getCurrentRound());
		assertTrue("Wrong player name!",nextName == adapter.getNameOfCurrentPlayer());
		assertTrue("Score is not calculated correctly!\n"+
		"Expected:" + val*2 + "\nGet:" + playerData.get(oldName),oldScore==playerData.get(oldName)-val*2);
		adapter.stopGame();
	}
	
	
	
	
	
	
	
}
