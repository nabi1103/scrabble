package de.tud.jsf.scrabble.tests.tutors.testcases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.newdawn.slick.Input;

import de.tud.jsf.scrabble.tests.adapter.ScrabbleTestAdapterExtended1;


public class TradeTest {
	
	ScrabbleTestAdapterExtended1 adapter;
	
	@Before
	public void setup() {
		adapter = new ScrabbleTestAdapterExtended1();
		adapter.initGame();
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
	}
	
	@Test
	public void resetTradeZoneTest() {
		// TUTOR
		enteringGameplayState();
		adapter.handleMousePressed(adapter.getStartButtonPosition(),0,0);
		assertTrue("Wrong state",adapter.getStateBasedGame().getCurrentStateID() == adapter.getGameplayStateID());
		assertTrue("Wrong number of players",adapter.getCurrentNumberOfPlayers() == 4);	
		assertTrue("Trade zone should be empty at the start!",adapter.isTradeZoneEmpty());
		char letter = adapter.getLetter(adapter.getLettersInInventoryPosition().get(0));
		adapter.handleMousePressed(adapter.getLettersInInventoryPosition().get(0),0,0);
		adapter.handleMousePressed(adapter.getTradeZonePosition(),0,0);
		assertEquals("There should be 1 letter in the trade zone!",adapter.getNumberOfLettersInTradeZone(),1 );
		assertEquals("There should be 1 letter in the trade zone!",adapter.getLettersInTradeZone().size(),1);
		assertEquals("Inventory size should be 6!" ,adapter.getCurrentInventorySize() , 6);
		assertEquals("Mismatching letter!",adapter.getLettersInTradeZone().get(0), Character.valueOf(letter));
		adapter.handleMousePressed(adapter.getUndoButtonPosition(),0,0);
		assertTrue("Trade zone should be empty after clicking undo!",adapter.isTradeZoneEmpty());
		assertEquals("Inventory size should be 7!" ,adapter.getCurrentInventorySize() , 7);
		assertEquals("There should be no letter in the trade zone!",adapter.getNumberOfLettersInTradeZone(),0 );
		assertEquals("There should be no letter in the trade zone!",adapter.getLettersInTradeZone().size(),0);
	}
	
	@Test
	public void testRandomTrade() {
		// STUDENT
		enteringGameplayState();
		if (adapter.isMinigameTradeActivated()) adapter.handleMousePressed(adapter.getDifficultySelectButtonPosition(),0,0);
		adapter.handleMousePressed(adapter.getStartButtonPosition(),0,0);
		assertTrue("Wrong state",adapter.getStateBasedGame().getCurrentStateID() == adapter.getGameplayStateID());
		assertTrue("Wrong number of players",adapter.getCurrentNumberOfPlayers() == 4);	
		int bagSize = adapter.getRemainingLettersInBag();
		assertTrue("Trade zone should be empty at the start!",adapter.isTradeZoneEmpty());
		assertTrue("Trade mode random should be activated!" , adapter.isRandomTradeActivated());
		assertFalse("Trade mode minigame should not be activated!" , adapter.isMinigameTradeActivated());
		char letter = adapter.getLetter(adapter.getLettersInInventoryPosition().get(0));
		adapter.handleMousePressed(adapter.getLettersInInventoryPosition().get(0),0,0);
		adapter.handleMousePressed(adapter.getTradeZonePosition(),0,0);
		assertEquals("There should be 1 letter in the trade zone!",adapter.getNumberOfLettersInTradeZone(),1 );
		assertEquals("There should be 1 letter in the trade zone!",adapter.getLettersInTradeZone().size(),1);
		assertEquals("Inventory size should be 6!" ,adapter.getCurrentInventorySize() , 6);
		assertEquals("Mismatching letter!",adapter.getLettersInTradeZone().get(0), Character.valueOf(letter));
		assertFalse("Trade mode minigame should not be activated!" , adapter.isMinigameTradeActivated());
		assertTrue("Trade mode random should be activated!" , adapter.isRandomTradeActivated());
		String nextName = adapter.getNameOfNextPlayer();
		adapter.handleMousePressed(adapter.getTradeButtonPosition(),0,0);
		assertTrue("Wrong state",adapter.getStateBasedGame().getCurrentStateID() == adapter.getGameplayStateID());
		assertEquals("Player making the trade should have their turn skipped",adapter.getCurrentRound(),2);
		assertEquals("Wrong next player",adapter.getNameOfCurrentPlayer(),nextName);
		assertEquals("Wrong bag size", adapter.getRemainingLettersInBag(),bagSize);
		for (int i = 0 ; i < 3 ; i ++) {
			assertTrue("Trade zone should be empty!",adapter.isTradeZoneEmpty());
			adapter.handleMousePressed(adapter.getPlayButtonPosition(),0,0);
		}
		assertTrue("Trade zone should be empty!",adapter.isTradeZoneEmpty());
		assertEquals("Inventory size should be 7!" ,adapter.getCurrentInventorySize() , 7);
	}
	
	
	@Test
	public void testMinigameTrade1() {
		// STUDENT
		enteringGameplayState();
		if (adapter.isRandomTradeActivated()) adapter.handleMousePressed(adapter.getDifficultySelectButtonPosition(),0,0);
		adapter.handleMousePressed(adapter.getStartButtonPosition(),0,0);
		assertTrue("Wrong state",adapter.getStateBasedGame().getCurrentStateID() == adapter.getGameplayStateID());
		assertTrue("Wrong number of players",adapter.getCurrentNumberOfPlayers() == 4);	
		int bagSize = adapter.getRemainingLettersInBag();
		assertTrue("Trade zone should be empty at the start!",adapter.isTradeZoneEmpty());
		assertFalse("Trade mode random should not be activated!" , adapter.isRandomTradeActivated());
		assertTrue("Trade mode minigame should be activated!" , adapter.isMinigameTradeActivated());
		char letter = adapter.getLetter(adapter.getLettersInInventoryPosition().get(0));
		adapter.handleMousePressed(adapter.getLettersInInventoryPosition().get(0),0,0);
		adapter.handleMousePressed(adapter.getTradeZonePosition(),0,0);
		assertEquals("There should be 1 letter in the trade zone!",adapter.getNumberOfLettersInTradeZone(),1 );
		assertEquals("There should be 1 letter in the trade zone!",adapter.getLettersInTradeZone().size(),1);
		assertEquals("Inventory size should be 6!" ,adapter.getCurrentInventorySize() , 6);
		assertEquals("Mismatching letter!",adapter.getLettersInTradeZone().get(0), Character.valueOf(letter));
		String nextName = adapter.getNameOfNextPlayer();
		adapter.handleMousePressed(adapter.getTradeButtonPosition(),0,0);
		assertTrue("Wrong state",adapter.getStateBasedGame().getCurrentStateID() == adapter.getTradeStateID());
		adapter.handleMousePressed(adapter.getMinigameStartButtonPosition(),0,0);
		while (!adapter.minigameEnded()) {
			adapter.runGame(1000);
		}
		assertEquals("Wrong number of letters won in minigame",adapter.getNumberOfLettersWonInMinigame(),1);
		adapter.handleMousePressed(adapter.getMinigameReturnButtonPosition(),0,0);
		assertTrue("Wrong state",adapter.getStateBasedGame().getCurrentStateID() == adapter.getGameplayStateID());
		assertEquals("Player making the trade should have their turn skipped",adapter.getCurrentRound(),2);
		assertEquals("Wrong next player",adapter.getNameOfCurrentPlayer(),nextName);
		assertEquals("Wrong bag size", adapter.getRemainingLettersInBag(),bagSize);
		for (int i = 0 ; i < 3 ; i ++) {
			assertTrue("Trade zone should be empty!",adapter.isTradeZoneEmpty());
			adapter.handleMousePressed(adapter.getPlayButtonPosition(),0,0);
		}
		assertTrue("Trade zone should be empty!",adapter.isTradeZoneEmpty());
		assertEquals("Inventory size should be 7!" ,adapter.getCurrentInventorySize() , 7);
	}
	@Test
	public void testMinigameTrade2() {
		// TUTOR
		enteringGameplayState();
		if (adapter.isRandomTradeActivated()) {
			adapter.handleMousePressed(adapter.getDifficultySelectButtonPosition(),0,0);
		}
		adapter.handleMousePressed(adapter.getStartButtonPosition(),0,0);
		assertTrue("Wrong state",adapter.getStateBasedGame().getCurrentStateID() == adapter.getGameplayStateID());
		assertTrue("Wrong number of players",adapter.getCurrentNumberOfPlayers() == 4);	
		assertTrue("Trade zone should be empty at the start!",adapter.isTradeZoneEmpty());
		assertFalse("Trade mode random should not be activated!" , adapter.isRandomTradeActivated());
		assertTrue("Trade mode minigame should be activated!" , adapter.isMinigameTradeActivated());
		char letter = adapter.getLetter(adapter.getLettersInInventoryPosition().get(0));
		adapter.handleMousePressed(adapter.getLettersInInventoryPosition().get(0),0,0);
		adapter.handleMousePressed(adapter.getTradeZonePosition(),0,0);
		assertEquals("There should be 1 letter in the trade zone!",adapter.getNumberOfLettersInTradeZone(),1 );
		assertEquals("There should be 1 letter in the trade zone!",adapter.getLettersInTradeZone().size(),1);
		assertEquals("Inventory size should be 6!" ,adapter.getCurrentInventorySize() , 6);
		assertEquals("Mismatching letter!",adapter.getLettersInTradeZone().get(0), Character.valueOf(letter));
		adapter.handleMousePressed(adapter.getTradeButtonPosition(),0,0);
		adapter.handleMousePressed(adapter.getMinigameStartButtonPosition(),0,0);
		assertTrue("Wrong state",adapter.getStateBasedGame().getCurrentStateID() == adapter.getTradeStateID());
		adapter.handleMousePressed(adapter.getMinigameReturnButtonPosition(),0,0);
		assertTrue("Wrong state",adapter.getStateBasedGame().getCurrentStateID() == adapter.getTradeStateID());
		
	}
	

}