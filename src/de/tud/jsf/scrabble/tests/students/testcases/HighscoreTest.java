package de.tud.jsf.scrabble.tests.students.testcases;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.tud.jsf.scrabble.tests.adapter.ScrabbleTestAdapterExtended2;
import de.tud.jsf.scrabble.tests.adapter.ScrabbleTestAdapterMinimal;

public class HighscoreTest {
	
	ScrabbleTestAdapterExtended2 adapter;
	
	@Before
	public void setup() {
		adapter = new ScrabbleTestAdapterExtended2();
		adapter.resetHighscore();
	}
	@After
	public void finish() {
		adapter.stopGame();
	}
	
	
	public void testCreateHighscore() {
		// STUDENT
		assertEquals("Highscore list should be empty after reset!", 0, adapter.getHighscoreListSize());
		
		adapter.addHighscore("P1", 12, 34);
		
		assertEquals("There should be only 1 entry after adding 1!", 1, adapter.getHighscoreListSize());
		assertEquals("Player name is incorrect!", "P1", adapter.getPlayerNameInHighscore(0));
		assertEquals("Score of player is incorrect!", 12, adapter.getScoreOfPlayerInHighscore(0));
		assertEquals("Round won is incorrect!", 34, adapter.getRoundOfPlayerInHighscore(0));	
	}
	
	
	@Test
	public void testSortHighscoreList1() {
		// Score sorting - higher priority
		// STUDENT
		assertEquals("Highscore list should be empty after reset!", 0, adapter.getHighscoreListSize());
	
		
		adapter.addHighscore("5", 5, 1);
		adapter.addHighscore("1", 1, 1);
		adapter.addHighscore("2", 2, 1);
		adapter.addHighscore("3", 3, 1);
		adapter.addHighscore("4", 4, 1);
		
		assertEquals("There should only be 5 highscores!", 5, adapter.getHighscoreListSize());
		
		for (int i = 0 ; i < 5 ; i ++) {
			assertEquals("Position of player in list is wrong!",5-i,adapter.getScoreOfPlayerInHighscore(i));
			assertEquals("Position of player in list is wrong!","" + (5-i),adapter.getPlayerNameInHighscore(i));
			assertEquals("Wrong round number!",1,adapter.getRoundOfPlayerInHighscore(i));
		}				
	}
}
	