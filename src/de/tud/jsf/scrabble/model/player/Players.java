package de.tud.jsf.scrabble.model.player;

import java.util.LinkedList;
import java.util.Random;



public class Players {
	
	private static LinkedList<Player> players= new LinkedList<Player>(); 
	public static Player currentPlayer;
	
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
	public static LinkedList<Player> getPlayers(){
		
		return players; 
	}
	
	public static int getNumberOfPlayers() {
		return players.size();
	}
	/**
	 * resets the current list of Players 
	 */
	public static void resetPlayers() {
		
		players = new LinkedList<Player>();
	}
	
	public static void initFirstPlayer() {
		if (players.size() <= 1) System.err.println("Not enough players!");
		else {
			currentPlayer = players.get(new Random().nextInt(getNumberOfPlayers()));
		}
	}
	
	
	public static Player nextPlayer() {
		currentPlayer = players.get((currentPlayer.getID() + 1) % getNumberOfPlayers());
		return currentPlayer;
	}
	
	

}
