package de.tud.jsf.scrabble.tests.students.suites;


import de.tud.jsf.scrabble.tests.students.testcases.TradeTest;
import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestSuite;

public class ScrabbleTestsuiteExtended1 {
public static Test suite() {
		
		TestSuite suite = new TestSuite("Student tests for Scrabble - Extended 1");
		suite.addTest(new JUnit4TestAdapter(TradeTest.class));
		//suite.addTest(new JUnit4TestAdapter(PlacingLetterTest.class));
		//suite.addTest(new JUnit4TestAdapter(ChallengeTest.class));
		return suite;
	}

}
