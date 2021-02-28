package de.tud.jsf.scrabble.tests.students.testcases;

import static org.junit.Assert.assertTrue;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.newdawn.slick.geom.Vector2f;

import de.tud.jsf.scrabble.tests.adapter.ScrabbleTestAdapterExtended3;

public class ShowHideTest {

	ScrabbleTestAdapterExtended3 adapter;

	@Before
	public void setup() {
		adapter = new ScrabbleTestAdapterExtended3();
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
			

}
