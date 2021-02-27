package de.tud.jsf.scrabble.tests.students.testcases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.io.File;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;

import de.tud.jsf.scrabble.tests.adapter.ScrabbleTestAdapterMinimal;



public class PlacingLetterTest {
	
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
		assertTrue(adapter.getStateBasedGame().getCurrentStateID() == adapter.getGameplayStateID());
		assertTrue(adapter.getCurrentNumberOfPlayers() == 4);
		
	}
	
	public boolean isBoardEmpty(char[][] board) {
		for (int i = 0 ; i < board.length ; i ++) {
			for (int j = 0 ; j < board.length ; j ++) {
				if (board[i][j] != '\u0000') return false;
			}
		}
		return true;
	}
	
	
	@Test
	public void testPlacingALetter() {
		// STUDENT
		adapter.initGame();
		int testRow = 8;
		int testColumn = 9;
		enteringGameplayState();
		assertTrue("Player does not have 7 letters at the start!",adapter.getCurrentInventorySize() == 7);
		assertTrue("Board is not empty at start!",isBoardEmpty(adapter.getBoard()));
		List<Vector2f> inventory = adapter.getLettersInInventoryPosition();	
		Vector2f letterPos = inventory.get(0);
		Vector2f fieldPos = adapter.getFieldPosition(testRow,testColumn);
		char letter = adapter.getLetter(letterPos);
		if (adapter.isLetterBlank(letter)) letter = 'A';
		adapter.handleMousePressed(fieldPos,0,0);
		assertTrue("Board should not be changed when clicking on while not holding a letter!",isBoardEmpty(adapter.getBoard()));
		adapter.handleMousePressed(letterPos,0,0);
		adapter.handleMousePressed(fieldPos,0,0);
		for (int i = 0 ; i < adapter.getBoard().length ; i ++) {
			for (int j = 0 ; j < adapter.getBoard().length ; j ++) {
				if (i == testRow-1 && j == testColumn-1) {
					assertEquals("Wrong letter on the board!",adapter.getBoard()[i][j],  letter);
				}
				else assertTrue("Field " + i +"," + j + "should be empty",adapter.getBoard()[i][j] == '\u0000');
			}
		}
		adapter.stopGame();
	}
	
	@Test
	public void testPlacingALetterOnALetter() {
		// TUTOR
		adapter.initGame();
		int testRow = 8;
		int testColumn = 8;
		enteringGameplayState();
		assertTrue("Player does not have 7 letters at the start!",adapter.getCurrentInventorySize() == 7);
		assertTrue("Board is not empty at start!",isBoardEmpty(adapter.getBoard()));
		List<Vector2f> inventory = adapter.getNonBlankLettersInInventoryPosition();	
		Vector2f letterPos = inventory.get(0);
		Vector2f fieldPos = adapter.getFieldPosition(testRow,testColumn);
		char letter = adapter.getLetter(letterPos);
		if (adapter.isLetterBlank(letter)) letter = 'A';
		adapter.handleMousePressed(letterPos,0,0);
		adapter.handleMousePressed(fieldPos,0,0);
		for (int i = 0 ; i < adapter.getBoard().length ; i ++) {
			for (int j = 0 ; j < adapter.getBoard().length ; j ++) {
				if (i == testRow-1 && j == testColumn-1) {					 
					assertEquals("Wrong letter on the board!",adapter.getBoard()[i][j],  letter);
				}
				else assertEquals("Field " + i +"," + j + "should be empty",adapter.getBoard()[i][j],  '\u0000');
			}
		}
		
		inventory = adapter.getNonBlankLettersInInventoryPosition();	
		letterPos = inventory.get(0);
		adapter.handleMousePressed(letterPos,0,0);
		adapter.handleMousePressed(fieldPos,0,0);
		for (int i = 0 ; i < adapter.getBoard().length ; i ++) {
			for (int j = 0 ; j < adapter.getBoard().length ; j ++) {
				if (i == testRow-1 && j == testColumn-1)  {
					assertTrue("Placing a letter on the board while another letter is there should not be possible!",adapter.getBoard()[i][j] == letter);
				}
				else assertTrue("Field " + i +"," + j + "should be empty",adapter.getBoard()[i][j] == '\u0000');
			}
		}
		adapter.stopGame();
	}
	
	@Test
	public void testPlacingAdjacentLetter() {
		// STUDENT
		adapter.initGame();
		int testRow = 8;
		int testColumn = 9;
		enteringGameplayState();
		assertTrue("Player does not have 7 letters at the start!",adapter.getCurrentInventorySize() == 7);
		assertTrue("Board is not empty at start!",isBoardEmpty(adapter.getBoard()));
		List<Vector2f> inventory = adapter.getLettersInInventoryPosition();	
		Vector2f letterPos = inventory.get(0);
		Vector2f fieldPos = adapter.getFieldPosition(testRow,testColumn);
		char letter = adapter.getLetter(letterPos);
		if (adapter.isLetterBlank(letter)) letter = 'A';
		adapter.handleMousePressed(letterPos,0,0);
		adapter.handleMousePressed(fieldPos,0,0);
		for (int i = 0 ; i < adapter.getBoard().length ; i ++) {
			for (int j = 0 ; j < adapter.getBoard().length ; j ++) {
				if (i == testRow-1 && j == testColumn-1) {
					assertEquals("Wrong letter on the board!",adapter.getBoard()[i][j], letter);
				}
				else assertTrue("Field " + i +"," + j + "should be empty",adapter.getBoard()[i][j] == '\u0000');
			}
		}
		inventory = adapter.getLettersInInventoryPosition();	
		letterPos = inventory.get(0);
		fieldPos = adapter.getFieldPosition(testRow+1,testColumn+1);
		adapter.handleMousePressed(letterPos,0,0);
		adapter.handleMousePressed(fieldPos,0,0);;
		for (int i = 0 ; i < adapter.getBoard().length ; i ++) {
			for (int j = 0 ; j < adapter.getBoard().length ; j ++) {
				if (i == testRow && j == testColumn) assertTrue("Placing non-adjacent letters should not be possible!",adapter.getBoard()[i][j] == '\u0000');
			}
		}
		adapter.stopGame();
	}
	
	@Test
	public void testPickingAnotherLetter() {
		// TUTOR
		adapter.initGame();
		int testRow = 8;
		int testColumn = 9;
		enteringGameplayState();
		assertTrue("Player does not have 7 letters at the start!",adapter.getCurrentInventorySize() == 7);
		assertTrue("Board is not empty at start!",isBoardEmpty(adapter.getBoard()));
		List<Vector2f> inventory = adapter.getNonBlankLettersInInventoryPosition();	
		Vector2f letterPos1 = inventory.get(0);
		Vector2f letterPos2 = inventory.get(1);
		Vector2f fieldPos = adapter.getFieldPosition(testRow,testColumn);	
		adapter.handleMousePressed(letterPos1,0,0);
		adapter.handleMousePressed(letterPos2,0,0);
		char letter = adapter.getLetter(letterPos2);
		if (adapter.isLetterBlank(letter)) letter = 'A';
		adapter.handleMousePressed(fieldPos,0,0);
		for (int i = 0 ; i < adapter.getBoard().length ; i ++) {
			for (int j = 0 ; j < adapter.getBoard().length ; j ++) {
				if (i == testRow-1 && j == testColumn-1) assertTrue("Field " + i +"," + j + "should have" + letter,adapter.getBoard()[i][j] == letter);
				else assertTrue("Field " + i +"," + j + "should be empty",adapter.getBoard()[i][j] == '\u0000');
			}
		}
		adapter.stopGame();
	}
	
	
	
	
	
	
	
	
	

}
