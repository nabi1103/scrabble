package de.tud.jsf.scrabble.tests.students.testcases;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.newdawn.slick.geom.Vector2f;

import de.tud.jsf.scrabble.tests.adapter.ScrabbleTestAdapterExtended1;


public class ChallengeTest {
	
	ScrabbleTestAdapterExtended1 adapter;
	Vector2f challengeButtonPos;
	
	@Before
	public void setup() {
		adapter = new ScrabbleTestAdapterExtended1();
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
		challengeButtonPos = adapter.getChallengeButtonPosition();		
	}
	

	public void testChallenge1() {
		// STUDENT
		// Pressing challenge while there is nothing to check should not be possible
		adapter.initGame();
		enteringGameplayState();
		String oldName = adapter.getNameOfCurrentPlayer();
		adapter.handleMousePressed(challengeButtonPos,0,0);
		assertTrue("Challenging while there are no previous words is not possible!",adapter.getCurrentRound() == 1);
		assertTrue("Challenging while there are no previous words is not possible!",adapter.getNameOfCurrentPlayer() == oldName );
		adapter.handleMousePressed(adapter.getPlayButtonPosition(),0,0);
		oldName = adapter.getNameOfCurrentPlayer();
		adapter.handleMousePressed(challengeButtonPos,0,0);
		assertTrue("Challenging while there are no previous words is not possible!",adapter.getCurrentRound() == 2);
		assertTrue("Challenging while there are no previous words is not possible!",adapter.getNameOfCurrentPlayer() == oldName );
		adapter.stopGame();
	}
	

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
		adapter.handleMousePressed(challengeButtonPos,0,0);
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
		adapter.stopGame();				
	}
	
	@Test
	public void  testChallenge3() {
		// TUTOR
		// Can only challenge words formed immediately before current turn
		// When challenging check for all words formed
		adapter.initGame();
		enteringGameplayState();
		// P1 TURN
		String p1Name = adapter.getNameOfCurrentPlayer();
		adapter.handleMousePressed(adapter.getLettersInInventoryPosition().get(0),0,0);
		adapter.handleMousePressed(adapter.getFieldPosition(8,8),0,0);
		adapter.handleMousePressed(adapter.getLettersInInventoryPosition().get(0),0,0);
		adapter.handleMousePressed(adapter.getFieldPosition(8,9),0,0);
		adapter.handleMousePressed(adapter.getPlayButtonPosition(),0,0);
		int p1Score = adapter.getPlayerData().get(p1Name);
		// P2 TURN
		String p2Name = adapter.getNameOfCurrentPlayer();
		int p2OldScore = adapter.getScoreOfCurrentPlayer();
		List<Character> oldInventory = new ArrayList<Character>();
		for (Vector2f pos : adapter.getLettersInInventoryPosition()) {
			oldInventory.add(adapter.getLetter(pos));
		}
		adapter.handleMousePressed(adapter.getLettersInInventoryPosition().get(0),0,0);
		adapter.handleMousePressed(adapter.getFieldPosition(8,10),0,0);
		adapter.handleMousePressed(adapter.getLettersInInventoryPosition().get(0),0,0);
		adapter.handleMousePressed(adapter.getFieldPosition(9,10),0,0);
		adapter.handleMousePressed(adapter.getPlayButtonPosition(),0,0);
		adapter.testing();
		// P3 TURN
		String word1 = "" + adapter.getBoard()[7][7] + adapter.getBoard()[7][8] + adapter.getBoard()[7][9];
		String word2 = "" + adapter.getBoard()[7][9] + adapter.getBoard()[8][9];
		int currentRound = adapter.getCurrentRound();
		String p3Name = adapter.getNameOfCurrentPlayer();
		String p4Name = adapter.getNameOfNextPlayer();
		adapter.handleMousePressed(challengeButtonPos,0,0);
		adapter.testing();
		//System.out.println("Words are " + word1 + " and " + word2);
		assertTrue("Can only challenge words formed immediately before current turn!" +
		"Expected "+ p1Score + "\nActual:" + adapter.getPlayerData().get(p1Name),p1Score == adapter.getPlayerData().get(p1Name));
		if (!adapter.isWordLegal(word1) || !adapter.isWordLegal(word2)) {
			//System.out.println("CHALLENGE SUCCEED");
			assertTrue("After a successful challenge the player making it can also place letters!",
					adapter.getCurrentRound() == currentRound && adapter.getNameOfCurrentPlayer() == p3Name);
			assertTrue("After a successful challenge the challenged player should lose that turn!",p2OldScore == adapter.getPlayerData().get(p2Name));
			while (adapter.getNameOfCurrentPlayer() != p2Name) adapter.handleMousePressed(adapter.getPlayButtonPosition(),0,0);
			List<Character> newInventory = new ArrayList<Character>();
			for (Vector2f pos : adapter.getLettersInInventoryPosition()) {
				newInventory.add(adapter.getLetter(pos));
			}
			assertTrue("The letters of the illegal word(s) should return to the owner after the correct challenge!",newInventory.equals(oldInventory));
			
		}
		else {
			//System.out.println("CHALLENGE FAILED");
			assertTrue("After a failed challenge the player making it loses their turn!",adapter.getCurrentRound() == currentRound + 1 && p4Name == adapter.getNameOfCurrentPlayer());
			
		}
		adapter.stopGame();
	}
	

	public void testChallenge4() {
		// TUTOR
		// Challenging after a pass should not be possible
		adapter.initGame();
		enteringGameplayState();
		// P1 TURN
		String p1Name = adapter.getNameOfCurrentPlayer();
		adapter.handleMousePressed(adapter.getLettersInInventoryPosition().get(0),0,0);
		adapter.handleMousePressed(adapter.getFieldPosition(8,8),0,0);
		adapter.handleMousePressed(adapter.getLettersInInventoryPosition().get(0),0,0);
		adapter.handleMousePressed(adapter.getFieldPosition(8,9),0,0);
		adapter.handleMousePressed(adapter.getPlayButtonPosition(),0,0);
		adapter.handleMousePressed(adapter.getPlayButtonPosition(),0,0);
		int p1Score = adapter.getPlayerData().get(p1Name);
		int currentRound = adapter.getCurrentRound();
		String p3Name = adapter.getNameOfCurrentPlayer();
		adapter.handleMousePressed(challengeButtonPos,0,0);
		assertTrue("Challenging while there are no previous words is not possible!",adapter.getCurrentRound() == currentRound);
		assertTrue("Challenging while there are no previous words is not possible!",adapter.getNameOfCurrentPlayer() == p3Name);
		assertTrue("Challenging while there are no previous words is not possible!",p1Score == adapter.getPlayerData().get(p1Name));
		adapter.stopGame();
	}
	
	

}
