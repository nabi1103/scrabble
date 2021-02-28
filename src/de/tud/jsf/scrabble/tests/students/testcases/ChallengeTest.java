package de.tud.jsf.scrabble.tests.students.testcases;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.newdawn.slick.geom.Vector2f;

import de.tud.jsf.scrabble.tests.adapter.ScrabbleTestAdapterExtended2;


public class ChallengeTest {
	
	ScrabbleTestAdapterExtended2 adapter;

	
	@Before
	public void setup() {
		adapter = new ScrabbleTestAdapterExtended2();
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
	
	@Test
	public void testChallenge1() {
		// STUDENT
		// Pressing challenge while there is nothing to check should not be possible
		adapter.initGame();
		enteringGameplayState();
		String oldName = adapter.getNameOfCurrentPlayer();
		adapter.handleMousePressed(adapter.getCheckButtonPosition(),0,0);
		assertTrue("Challenging while there are no previous words is not possible!",adapter.getCurrentRound() == 1);
		assertTrue("Challenging while there are no previous words is not possible!",adapter.getNameOfCurrentPlayer() == oldName );
		adapter.handleMousePressed(adapter.getPlayButtonPosition(),0,0);
		oldName = adapter.getNameOfCurrentPlayer();
		adapter.handleMousePressed(adapter.getCheckButtonPosition(),0,0);
		assertTrue("Challenging while there are no previous words is not possible!",adapter.getCurrentRound() == 2);
		assertTrue("Challenging while there are no previous words is not possible!",adapter.getNameOfCurrentPlayer() == oldName );
	}
	
	@Test
	public void testChallenge2() {
		// STUDENT
		adapter.initGame();
		enteringGameplayState();
		String oldName = adapter.getNameOfCurrentPlayer();
		String nextName= adapter.getNameOfNextPlayer();
		int oldScore = adapter.getScoreOfCurrentPlayer();
		List<Character> oldInventory = new ArrayList<Character>();
		for (Vector2f pos : adapter.getLettersInInventoryPosition()) {
			oldInventory.add(adapter.getLetter(pos));
		}
		adapter.handleMousePressed(adapter.getLettersInInventoryPosition().get(0),0,0);
		adapter.handleMousePressed(adapter.getFieldPosition(8,8),0,0);
		adapter.handleMousePressed(adapter.getLettersInInventoryPosition().get(0),0,0);
		adapter.handleMousePressed(adapter.getFieldPosition(8,9),0,0);
		adapter.handleMousePressed(adapter.getPlayButtonPosition(),0,0);
		String nextName2 = adapter.getNameOfNextPlayer();
		String word = adapter.getBoard()[7][7] + "" + adapter.getBoard()[7][8];
		adapter.handleMousePressed(adapter.getCheckButtonPosition(),0,0);
		int score = adapter.getPlayerData().get(oldName);
		//System.out.println("Word is " + "" + word);
		if (!adapter.isWordLegal(word)) {
			//System.out.println("CHALLENGE SUCCEED");
			assertTrue("After a successful challenge the player making it can also place letters!",
					adapter.getCurrentRound() == 2 && adapter.getNameOfCurrentPlayer() == nextName);
			assertTrue("After a successfull challenge the challenged player should lose that turn!",oldScore == score);
			while (adapter.getNameOfCurrentPlayer() != oldName) adapter.handleMousePressed(adapter.getPlayButtonPosition(),0,0);
			List<Character> newInventory = new ArrayList<Character>();
			for (Vector2f pos : adapter.getLettersInInventoryPosition()) {
				newInventory.add(adapter.getLetter(pos));
			}
			assertTrue("The letters of the illegal word(s) should return to the owner after the correct challenge!",newInventory.equals(oldInventory));
			
		}
		else {
			//System.out.println("CHALLENGE FAILED");
			assertTrue("After a failed challenge the player making it loses their turn!",adapter.getCurrentRound() == 3 && nextName2 == adapter.getNameOfCurrentPlayer() );
			
		}				
	}
	
}