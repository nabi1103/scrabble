package de.tud.jsf.scrabble.tests.students.suites;

import junit.framework.Test;
import junit.framework.TestSuite;

public class ScrabbleTestsuiteAll {
	
	public static Test suite() {
		
		TestSuite suite = new TestSuite("All student tests for Scrabble");
		
		suite.addTest(de.tud.jsf.scrabble.tests.students.suites.ScrabbleTestsuiteMinimal.suite());
		//suite.addTest(de.tu_darmstadt.gdi1.tanks.tests.students.suites.TanksTestsuiteExtended1.suite());
		//suite.addTest(de.tu_darmstadt.gdi1.tanks.tests.students.suites.TanksTestsuiteExtended2.suite());
		//suite.addTest(de.tu_darmstadt.gdi1.tanks.tests.students.suites.TanksTestsuiteExtended3.suite());
		
		return suite;
	}
}
