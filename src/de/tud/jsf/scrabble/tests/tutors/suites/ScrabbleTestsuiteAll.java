package de.tud.jsf.scrabble.tests.tutors.suites;

import junit.framework.Test;
import junit.framework.TestSuite;

public class ScrabbleTestsuiteAll {
	
	public static Test suite() {
		
		TestSuite suite = new TestSuite("All tutor tests for Scrabble");
		
		suite.addTest(de.tud.jsf.scrabble.tests.tutors.suites.ScrabbleTestsuiteMinimal.suite());
		suite.addTest(de.tud.jsf.scrabble.tests.tutors.suites.ScrabbleTestsuiteExtended1.suite());
		suite.addTest(de.tud.jsf.scrabble.tests.tutors.suites.ScrabbleTestsuiteExtended2.suite());
		suite.addTest(de.tud.jsf.scrabble.tests.tutors.suites.ScrabbleTestsuiteExtended3.suite());
		
		return suite;
	}
}
