package de.tud.jsf.scrabble.tests.students.testcases;

import static org.junit.Assert.assertTrue;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.newdawn.slick.geom.Vector2f;

import de.tud.jsf.scrabble.tests.adapter.ScrabbleTestAdapterExtended1;

public class ShowHideTest {

	ScrabbleTestAdapterExtended1 adapter;

	@Before
	public void setup() {
		adapter = new ScrabbleTestAdapterExtended1();
	}
	@After
	public void finish() {
		adapter.stopGame();
	}

	public void enteringGameplayState() {
		adapter.initGame();
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
	
	@Test
	public void testHiddenUponEntering() {
		// STUDENT
		enteringGameplayState();
		assertTrue("The letters should be hidden upon starting the game",adapter.areLettersHidden());
	}
	
	@Test
	public void testUndo() {
		// STUDENT
		enteringGameplayState();
		assertTrue("The letters should be hidden upon starting the game",adapter.areLettersHidden());
		Vector2f showPos = adapter.getShowButtonPosition();
		assertTrue("Button not found",showPos != null);
		adapter.handleMousePressed(showPos,0,0);
		assertFalse("The letters should be showed when clicking the button ",adapter.areLettersHidden());
		Vector2f undoPos = adapter.getUndoButtonPosition();
		adapter.handleMousePressed(undoPos,0,0);
		assertTrue("The letters should always be hidden after clicking undo ",adapter.areLettersHidden());
	}
	
	@Test
	public void testShowing() {
		// STUDENT
		enteringGameplayState();
		assertTrue("The letters should be hidden upon starting the game",adapter.areLettersHidden());
		Vector2f showPos = adapter.getShowButtonPosition();
		assertTrue("Button not found",showPos != null);
		adapter.handleMousePressed(showPos,0,0);
		assertFalse("The letters should be showed when clicking the button ",adapter.areLettersHidden());
	}
	
	@Test
	public void testHiding() {
		// STUDENT
		enteringGameplayState();
		assertTrue("The letters should be hidden upon starting the game",adapter.areLettersHidden());
		Vector2f showPos = adapter.getShowButtonPosition();
		assertTrue("Button not found",showPos != null);
		adapter.handleMousePressed(showPos,0,0);
		assertFalse("The letters should be showed when clicking the button ",adapter.areLettersHidden());
		adapter.handleMousePressed(showPos,0,0);
		assertTrue("The letters should be hidden when clicking the button ",adapter.areLettersHidden());
	}
	

	
	@Test
	public void testShowingExtended() {
		enteringGameplayState();
		assertTrue("The letters should be hidden upon starting the game",adapter.areLettersHidden());
		Vector2f showPos = adapter.getShowButtonPosition();
		assertTrue("Button not found",showPos != null);
		adapter.handleMousePressed(showPos,0,0);
		assertFalse("The letters should be showed when clicking the button ",adapter.areLettersHidden());
		List<Vector2f> inventory = adapter.getLettersInInventoryPosition();
		adapter.handleMousePressed(inventory.get(2),0,0);
		adapter.handleMousePressed(adapter.getFieldPosition(8,8),0,0);
		assertTrue("It should be possible to place a letter when the letters are not hidden",adapter.getBoard()[7][7] != '\u0000');
		
	}
	
	@Test
	public void testHidingExtended1() {
		// TUTOR
		enteringGameplayState();
		assertTrue("The letters should be hidden upon starting the game",adapter.areLettersHidden());
		Vector2f showPos = adapter.getShowButtonPosition();
		assertTrue("Button not found",showPos != null);
		adapter.handleMousePressed(showPos,0,0);
		assertFalse("The letters should be showed when clicking the button ",adapter.areLettersHidden());
		List<Vector2f> inventory = adapter.getLettersInInventoryPosition();
		adapter.handleMousePressed(showPos,0,0);
		assertTrue("The letters should be hidden when clicking the button ",adapter.areLettersHidden());
		adapter.handleMousePressed(inventory.get(0),0,0);
		adapter.handleMousePressed(adapter.getFieldPosition(8,8),0,0);
		assertTrue("It should not be possible to place a letter when the letters are hidden",adapter.getBoard()[7][7] == '\u0000');
		
	}
	
	@Test
	public void testHidingExtended2() {
		// TUTOR
		enteringGameplayState();
		assertTrue("The letters should be hidden upon starting the game",adapter.areLettersHidden());
		Vector2f showPos = adapter.getShowButtonPosition();
		assertTrue("Button not found",showPos != null);
		adapter.handleMousePressed(showPos,0,0);
		assertFalse("The letters should be showed when clicking the button ",adapter.areLettersHidden());
		List<Vector2f> inventory = adapter.getLettersInInventoryPosition();
		adapter.handleMousePressed(inventory.get(0),0,0);
		adapter.handleMousePressed(showPos,0,0);
		assertTrue("The letters should be hidden when clicking the button ",adapter.areLettersHidden());
		adapter.handleMousePressed(adapter.getFieldPosition(8,8),0,0);
		assertTrue("The letter currently being picked up should not be picked up anymore after hiding all letters",adapter.getBoard()[7][7] == '\u0000');
	}
	
	@Test
	public void testPlay() {
		// TUTOR
		enteringGameplayState();
		assertTrue("The letters should be hidden upon starting the game",adapter.areLettersHidden());
		Vector2f showPos = adapter.getShowButtonPosition();
		Vector2f playPos = adapter.getPlayButtonPosition();
		assertTrue("Button not found",showPos != null);
		adapter.handleMousePressed(showPos,0,0);
		assertFalse("The letters should be showed when clicking the button ",adapter.areLettersHidden());		
		adapter.handleMousePressed(playPos,0,0);
		assertEquals("Current round should be 2",2,adapter.getCurrentRound());
		assertTrue("The letters should always be hidden after moving to next round ",adapter.areLettersHidden());
		adapter.handleMousePressed(playPos,0,0);
		assertEquals("Current round should be 3",3,adapter.getCurrentRound());
		assertTrue("The letters should always be hidden after moving to next round ",adapter.areLettersHidden());
	}
	
	
	

}
