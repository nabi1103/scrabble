package de.tud.jsf.scrabble.ui.entity;

import org.newdawn.slick.geom.Vector2f;

import de.tud.jsf.scrabble.constants.GameParameters;

import java.util.HashMap;

public class Board implements GameParameters{
	Vector2f pos;
	Tile[][] board;
	
	final int NUMBER_T_LETTERS = 3;
	final int NUMBER_D_WORDS = 4;
	final int NUMBER_D_LETTERS = 7;
	final int NUMBER_T_WORDS = 3;
	
	public Board(Vector2f startPos) {
		this.pos = startPos;
	}
	
	public Tile[][] buildBoard() {
		HashMap<String,String> tileTypeMapper = new HashMap<>();
		String[] tletter = new String[NUMBER_T_LETTERS];
		String[] dletter = new String[NUMBER_D_LETTERS];
		String[] dword = new String[NUMBER_D_WORDS];
		String[] tword = new String[NUMBER_T_WORDS];
		tletter[0] = "1_5"; tletter[1] = "5_5" ; tletter[2] = "5_1";
		dword[0] = "1_1"; dword[1] = "2_2"; dword[2] = "3_3"; dword[3] = "4_4";
		dletter[0] = "0_3" ; dletter[1] = "2_6" ; dletter[2] ="3_0" ; dletter[3] = "6_2"; dletter[4] = "6_6"; dletter[5] = "7_3"; dletter[6] = "3_7";
		tword[0] = "0_0"; tword[1] ="0_7" ; tword[2] = "7_0";
		for (int i = 0 ; i < tletter.length ; i ++) tileTypeMapper.put(tletter[i],"tletter");			 		 
		for (int i = 0 ; i < dletter.length ; i ++) tileTypeMapper.put(dletter[i],"dletter");		 
		for (int i = 0 ; i < dword.length ; i ++) tileTypeMapper.put(dword[i],"dword");			 		 
		for (int i = 0 ; i < tword.length ; i ++)  tileTypeMapper.put(tword[i],"tword");

		board = new Tile[BOARDSIZE][BOARDSIZE];
		for (int i = 0 ; i < BOARDSIZE ; i ++) {
			for (int j = 0 ; j < BOARDSIZE ; j ++) {
				String tileID =  i + "_" + j;
				String tileType = tileTypeMapper.get(tileID);
				if (tileType == null) {
					int xFlip = i; int yFlip = j;					
					if (xFlip > BOARDSIZE/2) xFlip = BOARDSIZE - xFlip - 1;
					if (yFlip > BOARDSIZE/2) yFlip = BOARDSIZE - yFlip - 1;
					tileType = tileTypeMapper.get(xFlip + "_" + yFlip);
					if (tileType == null) tileType = "blank";
				}
				Vector2f temp = new Vector2f(pos.getY() + j * TILE_WIDTH, pos.getX() + i * TILE_HEIGHT);
				//Vector2f temp = new Vector2f(pos.getY() + j * TILE_HEIGHT, pos.getX() + i * TILE_WIDTH);
				board[i][j] = new Tile(tileID, tileType, temp);
			}
		}
		/*for (int i = 0 ; i < BOARDSIZE ;  i++) {
			for (int j = 0 ; j < BOARDSIZE ; j ++) {
				System.out.print(board[i][j].getID());
				System.out.print(" ");
				
			}
			System.out.println();
		}*/
		return board;	
	
	}
}
