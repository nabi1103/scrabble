package de.tud.jsf.scrabble.tests.students.testcases;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.tud.jsf.scrabble.tests.adapter.ScrabbleTestAdapterExtended1;
import de.tud.jsf.scrabble.tests.adapter.ScrabbleTestAdapterMinimal;

public class HighscoreTest {
	
	ScrabbleTestAdapterExtended1 adapter;
	
	@Before
	public void setup() {
		adapter = new ScrabbleTestAdapterExtended1();
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
	public void testMaximumHighscoreListSize() {
		// TUTOR
		assertEquals("Highscore list should be empty after reset!", 0, adapter.getHighscoreListSize());
		
		for (int i = 0 ; i < 10 ; i ++ ) {
			adapter.addHighscore("" + i,i*2,i*5);
			assertEquals("Highscore list size is incorrect!", i+1, adapter.getHighscoreListSize());
		}
		
		adapter.addHighscore("TestPlayer", 10, 10);
		assertEquals("List size passed the maximum!", 10, adapter.getHighscoreListSize());
	}
	
	@Test
	public void testSortHighscoreList1() {
		// Score sorting - higher priority
		// TUTOR
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
	@Test
	public void testSortHighscoreList2() {
		// Round sorting
		// TUTOR
		assertEquals("Highscore list should be empty after reset!", 0, adapter.getHighscoreListSize());
	
		adapter.addHighscore("5", 2, 5);
		adapter.addHighscore("3", 1, 3);
		adapter.addHighscore("4", 1, 4);
		adapter.addHighscore("1", 1, 1);
		adapter.addHighscore("2", 1, 2);
		
		assertEquals("There should only be 5 highscores!", 5, adapter.getHighscoreListSize());
		assertEquals("Position of player in list is wrong!",2,adapter.getScoreOfPlayerInHighscore(0));
		assertEquals("Position of player in list is wrong!","5",adapter.getPlayerNameInHighscore(0));
		assertEquals("Wrong round number!",5,adapter.getRoundOfPlayerInHighscore(0));
		assertEquals("Position of player in list is wrong!",1,adapter.getScoreOfPlayerInHighscore(1));
		assertEquals("Position of player in list is wrong!","1",adapter.getPlayerNameInHighscore(1));
		assertEquals("Wrong round number!",1,adapter.getRoundOfPlayerInHighscore(1));
		assertEquals("Position of player in list is wrong!",1,adapter.getScoreOfPlayerInHighscore(2));
		assertEquals("Position of player in list is wrong!","2",adapter.getPlayerNameInHighscore(2));
		assertEquals("Wrong round number!",2,adapter.getRoundOfPlayerInHighscore(2));
		assertEquals("Position of player in list is wrong!",1,adapter.getScoreOfPlayerInHighscore(3));
		assertEquals("Position of player in list is wrong!","3",adapter.getPlayerNameInHighscore(3));
		assertEquals("Wrong round number!",3,adapter.getRoundOfPlayerInHighscore(3));
		assertEquals("Position of player in list is wrong!",1,adapter.getScoreOfPlayerInHighscore(4));
		assertEquals("Position of player in list is wrong!","4",adapter.getPlayerNameInHighscore(4));
		assertEquals("Wrong round number!",4,adapter.getRoundOfPlayerInHighscore(4));
						
	}
	
	
	

}
