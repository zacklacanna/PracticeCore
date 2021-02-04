package me.BoyJamal.practice.utils;

import org.bukkit.Location;

public class LocationUtil {

	private Location playerOneSpawn;
	private Location playerTwoSpawn;
	
	public LocationUtil(Location playerOne, Location playerTwo)
	{
		this.playerOneSpawn = playerOne;
		this.playerTwoSpawn = playerTwo;
	}
	
	public Location getPlayerOneSpawn()
	{
		return playerOneSpawn;
	}
	
	public Location getPlayerTwoSpawn()
	{
		return playerTwoSpawn;
	}
	
}
