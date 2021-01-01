package de.tud.jsf.scrabble.model.player;

import java.util.LinkedList;



public class Players {
	
private static LinkedList<Player> players= new LinkedList<Player>(); 
	
	/**
	 * adds a player to the static list
	 * @param player
	 */
	public static void addPlayer(Player player) {	
		players.add(player); 
	}
	
	/**
	 * returns the current Players
	 * @return
	 */
	public static LinkedList<Player> getPLayers(){
		
		return players; 
	}
	/**
	 * resets the current list of Players 
	 */
	public static void resetPlayers() {
		
		players = new LinkedList<Player>();
	}

}
