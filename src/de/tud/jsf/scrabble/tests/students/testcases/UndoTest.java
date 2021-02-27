package de.tud.jsf.scrabble.tests.students.testcases;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.newdawn.slick.geom.Vector2f;

import de.tud.jsf.scrabble.tests.adapter.ScrabbleTestAdapterMinimal;

public class UndoTest {
	ScrabbleTestAdapterMinimal adapter;
	Vector2f undoButtonPos;
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
		undoButtonPos = adapter.getUndoButtonPosition();
		assertTrue("Undo button not found!",undoButtonPos != null);
		
		
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
	public void undoTest1() {
		// STUDENT
		adapter.initGame();
		enteringGameplayState();
		char[][] charGrid = adapter.getBoard();
		String oldName = adapter.getNameOfCurrentPlayer();
		assertTrue("Board is not empty at start!",isBoardEmpty(charGrid));
		adapter.handleMousePressed(undoButtonPos,0,0);
		assertTrue("Current round is not the same!",adapter.getCurrentRound() == 0);
		assertTrue("Current player is not the same",adapter.getNameOfCurrentPlayer() == oldName);
		assertTrue("Error when clicking undo while board is empty",isBoardEmpty(charGrid));	
		adapter.stopGame();
	}
	
	@Test
	public void undoTest2() {
		// STUDENT
		adapter.initGame();
		enteringGameplayState();
		char[][] charGrid = adapter.getBoard();
		List<Character> inventoryBefore = new ArrayList<Character>();
		List<Character> inventoryAfter = new ArrayList<Character>();
		for (Vector2f letterPos : adapter.getLettersInInventoryPosition()) {
			inventoryBefore.add(adapter.getLetter(letterPos));
		}
		assertTrue("Board is not empty at start!",isBoardEmpty(charGrid));
		adapter.handleMousePressed(adapter.getLettersInInventoryPosition().get(0),0,0);
		adapter.handleMousePressed(adapter.getFieldPosition(12,3),0,0);
		adapter.handleMousePressed(adapter.getLettersInInventoryPosition().get(0),0,0);
		adapter.handleMousePressed(adapter.getFieldPosition(12,4),0,0);
		adapter.handleMousePressed(undoButtonPos,0,0);
		assertTrue("Board is not the same as start of round!",isBoardEmpty(charGrid));					
		assertTrue("Inventory size after clicking undo not correct!",adapter.getCurrentInventorySize() == 7);
		for (Vector2f letterPos : adapter.getLettersInInventoryPosition()) {
			inventoryAfter.add(adapter.getLetter(letterPos));
		}
		assertTrue("Inventory is not the same after clicking undo!",inventoryBefore.equals(inventoryAfter));
		adapter.stopGame();
	}
	
	@Test
	public void undoTest3() {
		// TUTOR
		adapter.initGame();
		enteringGameplayState();
		char[][] charGrid = adapter.getBoard();
		adapter.handleMousePressed(adapter.getLettersInInventoryPosition().get(0),0,0);
		adapter.handleMousePressed(adapter.getFieldPosition(8,8),0,0);
		adapter.handleMousePressed(adapter.getLettersInInventoryPosition().get(0),0,0);
		adapter.handleMousePressed(adapter.getFieldPosition(8,9),0,0);
		adapter.handleMousePressed(adapter.getPlayButtonPosition(),0,0);
		List<Character> inventoryBefore = new ArrayList<Character>();
		List<Character> inventoryAfter = new ArrayList<Character>();
		for (Vector2f letterPos : adapter.getLettersInInventoryPosition()) {
			inventoryBefore.add(adapter.getLetter(letterPos));
		}
		adapter.handleMousePressed(undoButtonPos,0,0);
		assertTrue("Board is not the same as start of the round after clicking undo!",adapter.getBoard()[7][7] != '\u0000'
				&& adapter.getBoard()[7][8] != '\u0000');
		adapter.handleMousePressed(adapter.getLettersInInventoryPosition().get(0),0,0);
		adapter.handleMousePressed(adapter.getFieldPosition(5,2),0,0);		
		adapter.handleMousePressed(undoButtonPos,0,0);
		charGrid = adapter.getBoard();
		for (int i = 0 ; i < charGrid.length ; i ++) {
			for (int j = 0 ; j < charGrid.length ; j++) {
				if (i == 7 && (j ==7 || j ==8)) assertTrue("Board is not the same as start of the round after clicking undo!",charGrid[i][j] != '\u0000');
				else assertTrue("Board is not the same as start of the round after clicking undo!",charGrid[i][j] == '\u0000');					
			}
		}		
		assertTrue("Inventory size after clicking undo not correct!",adapter.getCurrentInventorySize() == 7);
		for (Vector2f letterPos : adapter.getLettersInInventoryPosition()) {
			inventoryAfter.add(adapter.getLetter(letterPos));
		}
		assertTrue("Inventory is not the same after clicking undo!",inventoryBefore.equals(inventoryAfter));
		adapter.stopGame();
		
	}
	
	
}
