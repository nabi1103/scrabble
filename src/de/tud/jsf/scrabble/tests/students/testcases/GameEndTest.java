package de.tud.jsf.scrabble.tests.students.testcases;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.tud.jsf.scrabble.tests.adapter.ScrabbleTestAdapterMinimal;

public class GameEndTest {
	
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
		adapter.initGame();
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
	public void testWinByPassing() {
		enteringGameplayState();
		adapter.handleMousePressed(adapter.getLettersInInventoryPosition().get(0),0,0);
		adapter.handleMousePressed(adapter.getFieldPosition(8,8),0,0);
		adapter.handleMousePressed(adapter.getLettersInInventoryPosition().get(0),0,0);
		adapter.handleMousePressed(adapter.getFieldPosition(8,9),0,0);
		adapter.handleMousePressed(adapter.getPlayButtonPosition(),0,0);
		assertTrue("Wrong state",adapter.getStateBasedGame().getCurrentStateID() == adapter.getGameplayStateID());
		adapter.handleMousePressed(adapter.getPlayButtonPosition(),0,0);
		assertTrue("Wrong state",adapter.getStateBasedGame().getCurrentStateID() == adapter.getGameplayStateID());
		adapter.handleMousePressed(adapter.getPlayButtonPosition(),0,0);
		assertTrue("Wrong state",adapter.getStateBasedGame().getCurrentStateID() == adapter.getGameplayStateID());
		adapter.handleMousePressed(adapter.getPlayButtonPosition(),0,0);
		assertTrue("Wrong state",adapter.getStateBasedGame().getCurrentStateID() == adapter.getGameplayStateID());
		adapter.handleMousePressed(adapter.getPlayButtonPosition(),0,0);
		assertTrue("Wrong state",adapter.getStateBasedGame().getCurrentStateID() == adapter.getGameplayStateID());
		adapter.handleMousePressed(adapter.getPlayButtonPosition(),0,0);
		assertTrue("Wrong state",adapter.getStateBasedGame().getCurrentStateID() == adapter.getGameplayStateID());
		adapter.handleMousePressed(adapter.getPlayButtonPosition(),0,0);
		assertTrue("Wrong state",adapter.getStateBasedGame().getCurrentStateID() == adapter.getGameplayStateID());
		adapter.handleMousePressed(adapter.getPlayButtonPosition(),0,0);
		assertTrue("Wrong state",adapter.getStateBasedGame().getCurrentStateID() == adapter.getGameplayStateID());
		adapter.handleMousePressed(adapter.getPlayButtonPosition(),0,0);
		adapter.runGame(1);
		assertTrue("Game should have ended because the 8th pass is called",adapter.getStateBasedGame().getCurrentStateID() == adapter.getMainMenuStateID());
	}
	
}