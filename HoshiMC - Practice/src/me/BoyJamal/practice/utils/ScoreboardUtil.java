package me.BoyJamal.practice.utils;

import org.bukkit.entity.Player;

public class ScoreboardUtil implements Runnable {

	private Player p;
	
	public ScoreboardUtil(Player p)
	{
		this.p = p;
	}
	
	@Override
	public void run() {
		p.setScoreboard(MainUtils.lobbyScoreboard(p));
	}

}
