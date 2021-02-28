package de.tud.jsf.scrabble.tests.tutors.suites;



import de.tud.jsf.scrabble.tests.tutors.testcases.GameEndTest;
import de.tud.jsf.scrabble.tests.tutors.testcases.PlacingLetterTest;
import de.tud.jsf.scrabble.tests.tutors.testcases.PlayButtonTest;
import de.tud.jsf.scrabble.tests.tutors.testcases.PlayerSelectTest;
import de.tud.jsf.scrabble.tests.tutors.testcases.UndoTest;
import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestSuite;

public class ScrabbleTestsuiteMinimal {
	
	public static Test suite() {
		
		TestSuite suite = new TestSuite("Tutor tests for Scrabble - Minimal");
		suite.addTest(new JUnit4TestAdapter(PlayerSelectTest.class));
		suite.addTest(new JUnit4TestAdapter(PlacingLetterTest.class));
		suite.addTest(new JUnit4TestAdapter(PlayButtonTest.class));
		suite.addTest(new JUnit4TestAdapter(UndoTest.class));
		suite.addTest(new JUnit4TestAdapter(GameEndTest.class));
		return suite;
	}
}
