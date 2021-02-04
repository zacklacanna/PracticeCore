package me.BoyJamal.practice.utils;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import me.BoyJamal.practice.Main;
import me.BoyJamal.practice.listeners.ArenaListeners;
import me.BoyJamal.practice.listeners.ArenaSelectorListener;
import me.BoyJamal.practice.listeners.ItemListeners;

public class StartBattle {

	private ArenaUtil arena;
	private Player one;
	private Player two;
	private KitUtil kit;
	private boolean inBattle;
	private int counter = -5;
	private int id;
	private Player winner = null;
	private Player loser = null;
	private boolean ranked;
	private int elo;
	
	public StartBattle(ArenaUtil arena, Player one, Player two, KitUtil kit, boolean ranked)
	{
		this.arena = arena;
		this.one = one;
		this.two = two;
		this.kit = kit;
		this.inBattle = true;
		this.ranked = ranked;
		if (ranked)
		{
			this.elo = 5;
			//calculate real elo
		} else {
			this.elo = 0;
		}
		
		this.id = new BukkitRunnable()
		{
			public void run()
			{
				updateScoreboard();
				counter++;
			}
		}.runTaskTimer(Main.getInstance(), 0, 20).getTaskId();
	}
	
	public Player getPlayerOne()
	{
		return one;
	}
	
	public int getElo()
	{
		return elo;
	}
	
	public boolean isRanked()
	{
		return ranked;
	}
	
	public Player getPlayerTwo()
	{
		return two;
	}
	
	public KitUtil getKit()
	{
		return kit;
	}
	
	public void setWinner(Player p)
	{
		this.winner = p;
	}
	
	public void setLoser(Player p)
	{
		this.loser = p;
	}
	
	public ArenaUtil getArena()
	{
		return arena;
	}
	
	public boolean inBattle()
	{
		return inBattle;
	}
	
	public void endBattle()
	{
		Bukkit.getScheduler().cancelTask(id);
		if (winner == null || loser == null)
		{
			return;
		}
		
		PartyUtil winnerParty = PartyMethods.getParty(winner);
		PartyUtil loserParty = PartyMethods.getParty(loser);
		if (winnerParty != null && loserParty != null)
		{
			if (!(winnerParty.getLeader().getUniqueId().equals(loserParty.getLeader().getUniqueId())))
			{
				Main.getInstance().getHider().hideEntity(winner, loser);
				Main.getInstance().getHider().hideEntity(loser, winner);
			}
		} else {
			Main.getInstance().getHider().hideEntity(winner, loser);
			Main.getInstance().getHider().hideEntity(loser, winner);
		}
		
		winner.getWorld().strikeLightning(loser.getLocation());
		MainUtils.completedMessage(winner, loser,ranked,elo);
		
		new BukkitRunnable()
		{
			public void run()
			{
				winner.getInventory().clear();
				MainUtils.onPlayerJoin(winner);
			}
		}.runTaskLater(Main.getInstance(),100);
	}
	
	public void updateScoreboard()
	{
		ScoreboardManager mang = Bukkit.getScoreboardManager();
		Scoreboard board = mang.getNewScoreboard();
		
		Objective obj = board.registerNewObjective("Title", "Title");
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		obj.setDisplayName(MainUtils.chatColor("&c&lHoshi&f&lMC"));
		
		Score line1 = obj.getScore("&f&m---------");
		line1.setScore(8);
		
		Score line3 = obj.getScore("&fKit: &c&o" + kit.getName());
		line3.setScore(7);
		
		Score line2 = obj.getScore(MainUtils.chatColor("&bDuration: &f" + MainUtils.convertTime(counter)));
		line2.setScore(6);
		
		Score line4 = obj.getScore(MainUtils.chatColor(""));
		line4.setScore(5);
		
		int playerOnePing = ((CraftPlayer)one).getHandle().ping;
		int playerTwoPing = ((CraftPlayer)two).getHandle().ping;
		
		Score line5 = obj.getScore(MainUtils.chatColor("&f" + one.getName() + "'s Ping: &c" + playerOnePing + "ms"));
		line5.setScore(4);
		
		Score line6 = obj.getScore(MainUtils.chatColor("&f" + two.getName() + "'s Ping: &c" + playerTwoPing + "ms"));
		line6.setScore(3);
		
		Score line7 = obj.getScore("");
		line7.setScore(2);
		
		Score line8 = obj.getScore(MainUtils.chatColor("&cplay.hoshimc.eu"));
		line8.setScore(1);
		
		one.setScoreboard(board);
		two.setScoreboard(board);
	}
	
	public void inBattle(boolean val)
	{
		this.inBattle = val;
	}
	
	public void startBattle()
	{
		one.sendMessage(MainUtils.chatColor("&a&lSuccess! &7Battle found with &f" + two.getPlayer().getName() + "! &7Fight will start in 5 seconds!"));
		two.getPlayer().sendMessage(MainUtils.chatColor("&a&lSuccess! &7Battle found with &f" + one.getName() + "! &7Fight will start in 5 seconds!"));
		new BukkitRunnable()
		{
			public void run()
			{
				one.getInventory().clear();
				two.getInventory().clear();
				
				Main.getInstance().getHider().showEntity(one, two);
				Main.getInstance().getHider().showEntity(two, one);
				
				for (ItemStack each : kit.getItems())
				{
					one.getInventory().addItem(each);
					two.getInventory().addItem(each);
				}
				
				one.teleport(arena.getLocations().getPlayerOneSpawn());
				two.teleport(arena.getLocations().getPlayerTwoSpawn());
			}
		}.runTaskLater(Main.getInstance(), 100);
	}
	
}
