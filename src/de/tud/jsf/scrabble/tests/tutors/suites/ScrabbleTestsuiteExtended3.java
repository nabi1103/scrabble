package de.tud.jsf.scrabble.tests.tutors.suites;


import de.tud.jsf.scrabble.tests.tutors.testcases.ShowHideTest;
import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestSuite;

public class ScrabbleTestsuiteExtended3 {
public static Test suite() {
		
		TestSuite suite = new TestSuite("Tutor tests for Scrabble - Extended 3");
		suite.addTest(new JUnit4TestAdapter(ShowHideTest.class));
		return suite;
	}

}
