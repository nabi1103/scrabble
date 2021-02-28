package de.tud.jsf.scrabble.tests.tutors.suites;


import de.tud.jsf.scrabble.tests.tutors.testcases.TradeTest;
import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestSuite;

public class ScrabbleTestsuiteExtended1 {
public static Test suite() {
		
		TestSuite suite = new TestSuite("Tutor tests for Scrabble - Extended 1");
		suite.addTest(new JUnit4TestAdapter(TradeTest.class));
		return suite;
	}

}
