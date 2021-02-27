package de.tud.jsf.scrabble.model.highscore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HighscoreList {

	private static final HighscoreList highscoreList = new HighscoreList();
	private List<Highscore> highscores;
	private boolean loaded;

	private HighscoreList() {
		highscores = new ArrayList<Highscore>();
		loaded = false;
	}

	public void addHighscore(Highscore highscore) {
		highscores.add(highscore);
		sort();
		if (highscores.size() > 10) {
			highscores.remove(10);
		}
		save();
	}

	public void sort() {
		Comparator<Highscore> c = new Comparator<Highscore>() {
			@Override
			public int compare(Highscore arg0, Highscore arg1) {
				return arg1.getScore() - arg0.getScore();
			}
		};
		Collections.sort(highscores, c);
	}

	@Override
	public String toString() {
		String res = "";
		for (Highscore h : highscores) {
			res += h.toString() + "\n";
		}
		return res;
	}

	public static HighscoreList getInstance() {
		return highscoreList;
	}

	public List<Highscore> getHighscores() {
		sort();
		return highscores;
	}

	public void save() {
		String fileName = "highscores.txt";
		try {
			FileOutputStream file = new FileOutputStream(fileName);
			ObjectOutputStream oos = new ObjectOutputStream(file);
			oos.writeObject(highscores);
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void load() {
		File f = new File("highscores.txt");
		if (f.exists()) {
			try {
				if (f.length() != 0) {
					FileInputStream file = new FileInputStream(f);
					ObjectInputStream ois = new ObjectInputStream(file);
					this.highscores = (List<Highscore>) ois.readObject();
					ois.close();
					loaded = true;
					System.out.println("Highscore successfully loaded.");
				}

			} catch (FileNotFoundException e) {
				System.out.println("No highscore file found for this map.");
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			try {
				System.out.println("Creating new highscore.txt file");
				f.createNewFile();
			} catch (IOException exc) {
				exc.printStackTrace();
			}
		}
	}

	public boolean isNewHighscore(Highscore hsc, Comparator comp) {

		// List is not full
		if (highscores.size() < 10) {
			return true;
		}


		Highscore lastHighscore = highscores.get(9);


		int result = comp.compare(hsc, lastHighscore);

		if (result == 1) 
			return true;
		else 
			return false;
		
	}

	public boolean hasHighscoreLoaded() {
		return loaded;
	}

}
