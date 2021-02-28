package de.tud.jsf.scrabble.tests.students.suites;

import junit.framework.Test;
import junit.framework.TestSuite;

public class ScrabbleTestsuiteAll {
	
	public static Test suite() {
		
		TestSuite suite = new TestSuite("All student tests for Scrabble");
		
		suite.addTest(de.tud.jsf.scrabble.tests.students.suites.ScrabbleTestsuiteMinimal.suite());
		suite.addTest(de.tud.jsf.scrabble.tests.students.suites.ScrabbleTestsuiteExtended1.suite());
		suite.addTest(de.tud.jsf.scrabble.tests.students.suites.ScrabbleTestsuiteExtended2.suite());
		suite.addTest(de.tud.jsf.scrabble.tests.students.suites.ScrabbleTestsuiteExtended3.suite());
		
		return suite;
	}
}
