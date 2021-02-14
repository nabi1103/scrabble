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
		assertTrue(adapter.getStateBasedGame().getCurrentStateID() == adapter.getMainMenuStateID());
		adapter.handleMousePressed(adapter.getNewgameButtonX(),adapter.getNewgameButtonY(),0,0);
		int x = adapter.getNewgameButtonX();
		int y = adapter.getNewgameButtonY();
		assertTrue("" + x + "," + y,adapter.getStateBasedGame().getCurrentStateID() == adapter.getPlayerSelectStateID());
		adapter.stopGame();
		
	}
	
	@Test
	public void testBackButton() {
		adapter.initGame();
		assertTrue(adapter.getStateBasedGame().getCurrentStateID() == adapter.getMainMenuStateID());
		adapter.handleMousePressed(adapter.getNewgameButtonX(),adapter.getNewgameButtonY(),0,0);
		assertTrue(adapter.getStateBasedGame().getCurrentStateID() == adapter.getPlayerSelectStateID());
		adapter.handleMousePressed(adapter.getBackButtonX(),adapter.getBackButtonY(),0,0);
		assertTrue(adapter.getStateBasedGame().getCurrentStateID() == adapter.getMainMenuStateID());
		adapter.stopGame();
	}
	
	@Test
	public void testSelectPlayer() {
		adapter.initGame();
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
		adapter.stopGame();
				
	}
	@Test
	public void testSelectPlayerDoubleClick() {
		adapter.initGame();
		assertTrue(adapter.getStateBasedGame().getCurrentStateID() == adapter.getMainMenuStateID());
		adapter.handleMousePressed(adapter.getNewgameButtonX(),adapter.getNewgameButtonY(),0,0);
		assertTrue(adapter.getStateBasedGame().getCurrentStateID() == adapter.getPlayerSelectStateID());
		for (int i = 0 ; i < adapter.getPlayerSelectFieldsX().size() ; i ++) {
			int x = adapter.getPlayerSelectFieldsX().get(i);
			int y = adapter.getPlayerSelectFieldsY().get(i);
			adapter.handleMousePressed(x,y,0,0);
			if (i == 0) adapter.handleMousePressed(x,y,0,0);
		}
		adapter.handleMousePressed(adapter.getStartButtonX(),adapter.getStartButtonY(),0,0);
		assertTrue(adapter.getStateBasedGame().getCurrentStateID() == adapter.getGameplayStateID());
		assertTrue(adapter.getCurrentNumberOfPlayers() == 3);
		adapter.stopGame();
				
	}
	@Test
	public void testSelectPlayerError() {
		adapter.initGame();
		assertTrue(adapter.getStateBasedGame().getCurrentStateID() == adapter.getMainMenuStateID());
		adapter.handleMousePressed(adapter.getNewgameButtonX(),adapter.getNewgameButtonY(),0,0);
		assertTrue(adapter.getStateBasedGame().getCurrentStateID() == adapter.getPlayerSelectStateID());
		for (int i = 0 ; i < adapter.getPlayerSelectFieldsX().size() ; i ++) {
			int x = adapter.getPlayerSelectFieldsX().get(i);
			int y = adapter.getPlayerSelectFieldsY().get(i);
			adapter.handleMousePressed(x,y,0,0);
			if (i != 0) adapter.handleMousePressed(x,y,0,0);
		}
		adapter.handleMousePressed(adapter.getStartButtonX(),adapter.getStartButtonY(),0,0);
		assertTrue(adapter.getStateBasedGame().getCurrentStateID() == adapter.getPlayerSelectStateID());
		adapter.handleMousePressed(adapter.getPlayerSelectFieldsX().get(0),adapter.getPlayerSelectFieldsY().get(0),0,0);
		adapter.handleMousePressed(adapter.getStartButtonX(),adapter.getStartButtonY(),0,0);
		assertTrue(adapter.getStateBasedGame().getCurrentStateID() == adapter.getPlayerSelectStateID());
		adapter.stopGame();				
	}
	
			
	
	
}
