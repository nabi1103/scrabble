package de.tud.jsf.scrabble.tests.tutors.testcases;

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
	
	@Test
	public void testWinByEmptyingBag1() {
		enteringGameplayState();
		// Fill middle horizontal line
		for (int i = 0 ; i < 7 ; i ++) {
			adapter.handleMousePressed(adapter.getLettersInInventoryPosition().get(0),0,0);
			adapter.handleMousePressed(adapter.getFieldPosition(8,8+i),0,0);			
		}
		adapter.handleMousePressed(adapter.getPlayButtonPosition(),0,0);
		assertTrue("Wrong state",adapter.getStateBasedGame().getCurrentStateID() == adapter.getGameplayStateID());
		adapter.handleMousePressed(adapter.getLettersInInventoryPosition().get(0),0,0);
		adapter.handleMousePressed(adapter.getFieldPosition(8,15),0,0);
		for (int i = 0 ; i < 6 ; i ++) {
			adapter.handleMousePressed(adapter.getLettersInInventoryPosition().get(0),0,0);
			adapter.handleMousePressed(adapter.getFieldPosition(8,8-i-1),0,0);
		}
		adapter.handleMousePressed(adapter.getPlayButtonPosition(),0,0);
		assertTrue("Wrong state",adapter.getStateBasedGame().getCurrentStateID() == adapter.getGameplayStateID());
		
		// Fill vertical lines
		for (int i = 0 ; i < 10 ; i ++) {
			for (int j = 0 ; j < adapter.getCurrentInventorySize() ; j ++) {
				adapter.handleMousePressed(adapter.getLettersInInventoryPosition().get(0),0,0);
				adapter.handleMousePressed(adapter.getFieldPosition(8-j-1,i+2),0,0);				
			}
			adapter.handleMousePressed(adapter.getPlayButtonPosition(),0,0);
			adapter.runGame(1);
			assertTrue("Wrong state",adapter.getStateBasedGame().getCurrentStateID() == adapter.getGameplayStateID());
		}
		adapter.handleMousePressed(adapter.getPlayButtonPosition(),0,0);
		adapter.handleMousePressed(adapter.getPlayButtonPosition(),0,0);
		adapter.runGame(1);
		assertTrue("Checking for game end should happens before filling a player's inventory",adapter.getStateBasedGame().getCurrentStateID() == adapter.getGameplayStateID());
		adapter.handleMousePressed(adapter.getPlayButtonPosition(),0,0);
		adapter.runGame(1);
		assertTrue("Game should have ended",adapter.getStateBasedGame().getCurrentStateID() == adapter.getMainMenuStateID());
	
		
		
	}
	@Test
	public void testWinByEmptyingBag2() {
		enteringGameplayState();
		// Fill middle horizontal line
		for (int i = 0 ; i < 7 ; i ++) {
			adapter.handleMousePressed(adapter.getLettersInInventoryPosition().get(0),0,0);
			adapter.handleMousePressed(adapter.getFieldPosition(8,8+i),0,0);			
		}
		adapter.handleMousePressed(adapter.getPlayButtonPosition(),0,0);
		assertTrue("Wrong state",adapter.getStateBasedGame().getCurrentStateID() == adapter.getGameplayStateID());
		adapter.handleMousePressed(adapter.getLettersInInventoryPosition().get(0),0,0);
		adapter.handleMousePressed(adapter.getFieldPosition(8,15),0,0);
		for (int i = 0 ; i < 6 ; i ++) {
			adapter.handleMousePressed(adapter.getLettersInInventoryPosition().get(0),0,0);
			adapter.handleMousePressed(adapter.getFieldPosition(8,8-i-1),0,0);
		}
		adapter.handleMousePressed(adapter.getPlayButtonPosition(),0,0);
		assertTrue("Wrong state",adapter.getStateBasedGame().getCurrentStateID() == adapter.getGameplayStateID());
		
		// Fill vertical lines
		for (int i = 0 ; i < 10 ; i ++) {
			for (int j = 0 ; j < adapter.getCurrentInventorySize() ; j ++) {
				adapter.handleMousePressed(adapter.getLettersInInventoryPosition().get(0),0,0);
				adapter.handleMousePressed(adapter.getFieldPosition(8-j-1,i+2),0,0);
				adapter.handleMousePressed(adapter.getPlayButtonPosition(),0,0);
			}
		
			adapter.runGame(1);
			assertTrue("Wrong state",adapter.getStateBasedGame().getCurrentStateID() == adapter.getGameplayStateID());
		}
		adapter.handleMousePressed(adapter.getPlayButtonPosition(),0,0);
		adapter.runGame(1);
		assertTrue("Wrong state",adapter.getStateBasedGame().getCurrentStateID() == adapter.getGameplayStateID());
				
		
	}
	
	
	

}
