package de.tud.jsf.scrabble.constants;

public interface GameParameters {
	// Window Settings
	public static final int WINDOW_WIDTH = 960;
	public static final int WINDOW_HEIGHT = 720;
	public static final int FRAME_RATE = 60;

	// Game States
	public static final int MAINMENU_STATE = 0;
	public static final int GAMEPLAY_STATE = 1;
	public static final int CE_STATE = 2;
	public static final int ABOUT_STATE = 3;
	
	// Board
	public static final int BOARDSIZE = 15;
	public static final float TILE_SCALE_FACTOR = 0.8f;
	public static final float BOARD_START_X = 100;
	public static final float BOARD_START_y = 100;
	
	// Tiles
	public static final float TILE_WIDTH = 50 * TILE_SCALE_FACTOR;
	public static final float TILE_HEIGHT = 50 * TILE_SCALE_FACTOR;
	
	public static final String BLANK_TILE = "assets/scrabble/tile/tile_white.png";
	public static final String BLUE_TILE = "assets/scrabble/tile/tile_blue.png";
	public static final String RED_TILE = "assets/scrabble/tile/tile_red.png";
	public static final String CYAN_TILE = "assets/scrabble/tile/tile_cyan.png";
	public static final String PINK_TILE = "assets/scrabble/tile/tile_pink.png";
	public static final String STAR_TILE = "assets/scrabble/tile/tile_star.png";
	

	// Letter
	public static final float LETTER_SCALE_FACTOR = 0.15f;
	
	public static final String LETTER_BLANK = "assets/scrabble/letter/letter__.png";
	public static final String LETTER_A = "assets/scrabble/letter/letter_A.png";
	public static final String LETTER_B = "assets/scrabble/letter/letter_B.png";
	public static final String LETTER_C = "assets/scrabble/letter/letter_C.png";
	public static final String LETTER_D = "assets/scrabble/letter/letter_D.png";
	public static final String LETTER_E = "assets/scrabble/letter/letter_E.png";
	public static final String LETTER_F = "assets/scrabble/letter/letter_F.png";
	public static final String LETTER_G = "assets/scrabble/letter/letter_G.png";
	public static final String LETTER_H = "assets/scrabble/letter/letter_H.png";
	public static final String LETTER_I = "assets/scrabble/letter/letter_I.png";
	public static final String LETTER_J = "assets/scrabble/letter/letter_J.png";
	public static final String LETTER_K = "assets/scrabble/letter/letter_K.png";
	public static final String LETTER_L = "assets/scrabble/letter/letter_L.png";
	public static final String LETTER_M = "assets/scrabble/letter/letter_M.png";
	public static final String LETTER_N = "assets/scrabble/letter/letter_N.png";
	public static final String LETTER_O = "assets/scrabble/letter/letter_O.png";
	public static final String LETTER_P = "assets/scrabble/letter/letter_P.png";
	public static final String LETTER_Q = "assets/scrabble/letter/letter_Q.png";
	public static final String LETTER_R = "assets/scrabble/letter/letter_R.png";
	public static final String LETTER_S = "assets/scrabble/letter/letter_S.png";
	public static final String LETTER_T = "assets/scrabble/letter/letter_T.png";
	public static final String LETTER_U = "assets/scrabble/letter/letter_U.png";
	public static final String LETTER_V = "assets/scrabble/letter/letter_V.png";
	public static final String LETTER_W = "assets/scrabble/letter/letter_W.png";
	public static final String LETTER_X = "assets/scrabble/letter/letter_X.png";
	public static final String LETTER_Y = "assets/scrabble/letter/letter_Y.png";
	public static final String LETTER_Z = "assets/scrabble/letter/letter_Z.png";
	
	public static final String[] LETTERS = {LETTER_BLANK, LETTER_A, LETTER_B, LETTER_C, LETTER_D, LETTER_E, LETTER_F, LETTER_G, LETTER_H, LETTER_I, LETTER_J, LETTER_K, LETTER_L,
			LETTER_M ,LETTER_N, LETTER_O, LETTER_P, LETTER_Q, LETTER_R, LETTER_S, LETTER_T, LETTER_U, LETTER_V, LETTER_W, LETTER_X, LETTER_Y, LETTER_Z};
	
	// Lexicon
	public static final String LEXICON = "assets/scrabble/lexicon/Collins Scrabble Words (2019).txt";
	
	// UI
	public static final String DBOX = "assets/scrabble/ui/dialogue_box.png";
	public static final String DBOX_YES = "assets/scrabble/ui/grey_boxCheckmark.png";
	public static final String DBOX_NO = "assets/scrabble/ui/grey_boxCross.png";
	
	public static final String COMMIT_BUTTON = "assets/scrabble/ui/commit_button.png";
	public static final String UNDO_BUTTON = "assets/scrabble/ui/undo_button.png";
	public static final String CHECK_BUTTON = "assets/scrabble/ui/check_button.png";
	
	public static final float DBOX_WIDTH = 400;
	public static final float DBOX_HEIGHT = 200;
	
	public static final float DBOX_START_X = 400;
	public static final float DBOX_START_Y= 360;
}
