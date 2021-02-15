package de.tud.jsf.scrabble.tests.students.suites;



import de.tud.jsf.scrabble.tests.students.testcases.PlacingLetterTest;
import de.tud.jsf.scrabble.tests.students.testcases.PlayButtonTest;
import de.tud.jsf.scrabble.tests.students.testcases.PlayerSelectTest;
import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestSuite;

public class ScrabbleTestsuiteMinimal {
	
	public static Test suite() {
		
		TestSuite suite = new TestSuite("Student tests for Scrabble - Minimal");
		//suite.addTest(new JUnit4TestAdapter(PlayerSelectTest.class));
		//suite.addTest(new JUnit4TestAdapter(PlacingLetterTest.class));
		suite.addTest(new JUnit4TestAdapter(PlayButtonTest.class));
		return suite;
	}
}
