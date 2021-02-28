package de.tud.jsf.scrabble.tests.tutors.suites;

import de.tud.jsf.scrabble.tests.tutors.testcases.ChallengeTest;
import de.tud.jsf.scrabble.tests.tutors.testcases.HighscoreTest;
import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestSuite;

public class ScrabbleTestsuiteExtended2 {
public static Test suite() {
		
		TestSuite suite = new TestSuite("Tutor tests for Scrabble - Extended 2");
		suite.addTest(new JUnit4TestAdapter(ChallengeTest.class));
		suite.addTest(new JUnit4TestAdapter(HighscoreTest.class));
		return suite;
	}

}
