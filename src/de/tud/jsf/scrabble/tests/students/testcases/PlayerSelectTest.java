package de.tud.jsf.scrabble.tests.students.testcases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.newdawn.slick.Input;

import de.tud.jsf.scrabble.tests.adapter.ScrabbleTestAdapterMinimal;

public class PlayerSelectTest {
	
	ScrabbleTestAdapterMinimal adapter;
	
	@Before
	public void setup() {
		adapter = new ScrabbleTestAdapterMinimal();
	}
	@After
	public void finish() {
		adapter.stopGame();
	}
	
	@Test
	public void testInitGame() {
		adapter.initGame();
		assertTrue("Wrong state",adapter.getStateBasedGame().getCurrentStateID() == adapter.getMainMenuStateID());
		adapter.handleMousePressed(adapter.getNewgameButtonPosition(),0,0);
		assertTrue("Wrong state",adapter.getStateBasedGame().getCurrentStateID() == adapter.getPlayerSelectStateID());
		adapter.stopGame();
		
	}
	
	@Test
	public void testBackButton() {
		adapter.initGame();
		assertTrue("Wrong state",adapter.getStateBasedGame().getCurrentStateID() == adapter.getMainMenuStateID());
		adapter.handleMousePressed(adapter.getNewgameButtonPosition(),0,0);
		assertTrue("Wrong state",adapter.getStateBasedGame().getCurrentStateID() == adapter.getPlayerSelectStateID());
		adapter.handleMousePressed(adapter.getBackButtonPosition(),0,0);
		assertTrue("Wrong state",adapter.getStateBasedGame().getCurrentStateID() == adapter.getMainMenuStateID());
		adapter.stopGame();
	}
	
	@Test
	public void testSelectPlayer() {
		adapter.initGame();
		assertTrue("Wrong state",adapter.getStateBasedGame().getCurrentStateID() == adapter.getMainMenuStateID());
		adapter.handleMousePressed(adapter.getNewgameButtonPosition(),0,0);
		assertTrue("Wrong state",adapter.getStateBasedGame().getCurrentStateID() == adapter.getPlayerSelectStateID());
		for (int i = 0 ; i < adapter.getPlayerSelectFieldsPosition().size() ; i ++) {
			adapter.handleMousePressed(adapter.getPlayerSelectFieldsPosition().get(i),0,0);
		}
		adapter.handleMousePressed(adapter.getStartButtonPosition(),0,0);
		assertTrue("Wrong state",adapter.getStateBasedGame().getCurrentStateID() == adapter.getGameplayStateID());
		assertTrue("Number of players should be 4 after selecting",adapter.getCurrentNumberOfPlayers() == 4);
		adapter.stopGame();
				
	}
	@Test
	public void testSelectPlayerDoubleClick() {
		adapter.initGame();
		assertTrue("Wrong state",adapter.getStateBasedGame().getCurrentStateID() == adapter.getMainMenuStateID());
		adapter.handleMousePressed(adapter.getNewgameButtonPosition(),0,0);
		assertTrue("Wrong state",adapter.getStateBasedGame().getCurrentStateID() == adapter.getPlayerSelectStateID());
		for (int i = 0 ; i < adapter.getPlayerSelectFieldsPosition().size() ; i ++) {
			adapter.handleMousePressed(adapter.getPlayerSelectFieldsPosition().get(i),0,0);
		}
		adapter.handleMousePressed(adapter.getPlayerSelectFieldsPosition().get(2),0,0);
		adapter.handleMousePressed(adapter.getStartButtonPosition(),0,0);
		assertTrue("Wrong state",adapter.getStateBasedGame().getCurrentStateID() == adapter.getGameplayStateID());
		assertTrue("Number of players should be 3 after selecting",adapter.getCurrentNumberOfPlayers() == 3);
		adapter.stopGame();
				
	}
	@Test
	public void testSelectPlayerError() {
		adapter.initGame();
		assertTrue("Wrong state",adapter.getStateBasedGame().getCurrentStateID() == adapter.getMainMenuStateID());
		adapter.handleMousePressed(adapter.getNewgameButtonPosition(),0,0);
		assertTrue("Wrong state",adapter.getStateBasedGame().getCurrentStateID() == adapter.getPlayerSelectStateID());
		for (int i = 0 ; i < adapter.getPlayerSelectFieldsPosition().size() ; i ++) {			
			adapter.handleMousePressed(adapter.getPlayerSelectFieldsPosition().get(i),0,0);
			if (i != 0) adapter.handleMousePressed(adapter.getPlayerSelectFieldsPosition().get(i),0,0);
		}
		adapter.handleMousePressed(adapter.getStartButtonPosition(),0,0);
		assertTrue("Wrong state",adapter.getStateBasedGame().getCurrentStateID() == adapter.getPlayerSelectStateID());
		adapter.handleMousePressed(adapter.getPlayerSelectFieldsPosition().get(0),0,0);
		adapter.handleMousePressed(adapter.getStartButtonPosition(),0,0);
		assertTrue("Wrong state",adapter.getStateBasedGame().getCurrentStateID() == adapter.getPlayerSelectStateID());
		adapter.stopGame();				
	}
	
			
	
	
}
