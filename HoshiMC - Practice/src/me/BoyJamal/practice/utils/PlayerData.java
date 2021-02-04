package me.BoyJamal.practice.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PlayerData {

	private int kills;
	private int hitsLanded;
	private double damageDealt;
	private int wins;
	private int losses;
	private int deaths;
	private double kdr;
	private double wlr;
	private String uuid;
	private int elo;
	
	public PlayerData(String uuid)
	{
		this.uuid = uuid;
		this.kills = 0;
		this.hitsLanded = 0;
		this.damageDealt = 0;
		this.wins = 0;
		this.losses = 0;
		this.kdr = 0;
		this.wlr = 0;
		this.elo = 0;
		this.deaths = 0;
	}
	
	public PlayerData(String uuid, int deaths, int kills, int hits, double damage, int wins, int losses, int elo)
	{
		this.uuid = uuid;
		this.kills = kills;
		this.hitsLanded = hits;
		this.damageDealt = damage;
		this.deaths = deaths;
		this.wins = wins;
		this.losses = losses;
		this.elo = elo;
		this.deaths = 0;
		
		getKillDeathRatio();
		getWinLossRatio();
	}
	
	public String getUUID()
	{
		return uuid;
	}
	
	public int getKills()
	{
		return kills;
	}
	
	public void addKills(int amount)
	{
		this.kills +=amount;
	}
	
	public double getDamageDealt()
	{
		return damageDealt;
	}
	
	public void addDamageDealt(double amount)
	{
		this.damageDealt += amount;
	}
	
	public int getHitsLanded()
	{
		return hitsLanded;
	}
	
	public void addHitsLanded(int amount)
	{
		this.hitsLanded += amount;
	}
	
	public int getWins()
	{
		return wins;
	}
	
	public void addWins(int amount)
	{
		this.wins += amount;
	}
	
	public int getLosses()
	{
		return losses;
	}
	
	public void addLosses(int amount)
	{
		this.losses += amount;
	}
	
	public void addDeath(int amount)
	{
		this.deaths += amount;
	}
	
	public int getDeaths()
	{
		return deaths;
	}
	
	public double getKillDeathRatio()
	{
		if (wins+losses == 0)
		{
			return 0.0;
		}
		
		this.kdr = kills/(wins+losses);
		
		BigDecimal bd = BigDecimal.valueOf(kdr);
	    bd = bd.setScale(2, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
	
	public double getWinLossRatio()
	{
		if (losses == 0)
		{
			if (wins != 0)
			{
				return 1.0;
			} else {
				return 0.0;
			}
		}
		
		this.wlr = wins/(losses+wins);
		
		BigDecimal bd = BigDecimal.valueOf(wlr);
	    bd = bd.setScale(2, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
	
	public int getElo()
	{
		return elo;
	}
	
	public void changeElo(int amount)
	{
		if (elo + amount <= 0)
		{
			this.elo = 0;
		} else {
			elo += amount;
		}
	}
	
}
