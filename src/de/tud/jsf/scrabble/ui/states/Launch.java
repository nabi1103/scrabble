package de.tud.jsf.scrabble.ui.states;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import eea.engine.entity.StateBasedEntityManager;

import de.tud.jsf.scrabble.constants.GameParameters;

public class Launch extends StateBasedGame implements GameParameters{
	
	public static boolean debug = false;
	
	public Launch(boolean debug)
    {
        super("Scrabble");
        setDebug(debug);
    }
	
	public static void setDebug(boolean debugging) {
		debug = debugging;
	}
	

	@Override
	public void initStatesList(GameContainer arg0) throws SlickException {
		addState(new MainMenuState(MAINMENU_STATE));
        addState(new GameplayState(GAMEPLAY_STATE));
        addState(new CEState(CE_STATE));
        addState(new PlayerSelectState(PLAYER_SELECT_STATE));
        addState(new HighscoreState(HIGHSCORE_STATE));

        StateBasedEntityManager.getInstance().addState(MAINMENU_STATE);
        StateBasedEntityManager.getInstance().addState(GAMEPLAY_STATE);
        StateBasedEntityManager.getInstance().addState(CE_STATE);
        StateBasedEntityManager.getInstance().addState(PLAYER_SELECT_STATE);
        StateBasedEntityManager.getInstance().addState(HIGHSCORE_STATE);
	}
	
	public static void main(String args[]) throws SlickException {
		if (System.getProperty("os.name").toLowerCase().contains("windows")) {
    		System.setProperty("org.lwjgl.librarypath",System.getProperty("user.dir") + "/native/windows");
		} else if (System.getProperty("os.name").toLowerCase().contains("mac")) {
    		System.setProperty("org.lwjgl.librarypath",System.getProperty("user.dir") + "/native/macosx");
    	} else {
    		System.setProperty("org.lwjgl.librarypath",System.getProperty("user.dir") + "/native/" +System.getProperty("os.name").toLowerCase());
    	}
		//System.setProperty("org.lwjgl.opengl.Display.allowSoftwareOpenGL", "true");
		
		AppGameContainer app = new AppGameContainer(new Launch(false));
        app.setDisplayMode(WINDOW_WIDTH, WINDOW_HEIGHT, false);
        app.setTargetFrameRate(FRAME_RATE);
        app.start();
	}
}
