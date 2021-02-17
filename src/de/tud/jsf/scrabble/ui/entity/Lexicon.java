package de.tud.jsf.scrabble.ui.entity;

import java.io.File; 
import java.io.FileNotFoundException;
import java.util.Scanner;

import de.tud.jsf.scrabble.constants.GameParameters;

import java.util.ArrayList;

public class Lexicon implements GameParameters{
	ArrayList<String> lex = new ArrayList<>();
	
	public Lexicon() {
		readLexicon();
	}
	
	public ArrayList<String> getLexicon() {
		return lex;
	}
	
	public boolean check(String word) {
		String w = word.toLowerCase();
		return lex.contains(w);
	}
	
	public void readLexicon() {
		try {
		      File f = new File(LEXICON);
		      Scanner scanner = new Scanner(f);
		      while (scanner.hasNextLine()) {
		        String data = scanner.nextLine();
		        lex.add(data.toLowerCase());
		      }
		      scanner.close();
		    } catch (FileNotFoundException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
	}
}
