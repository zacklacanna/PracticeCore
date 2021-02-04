package me.BoyJamal.practice.listeners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.BoyJamal.practice.Main;
import me.BoyJamal.practice.commands.ArenaCMD;
import me.BoyJamal.practice.utils.ArenaUtil;
import me.BoyJamal.practice.utils.MainUtils;
import me.BoyJamal.practice.utils.QueueUtil;
import me.BoyJamal.practice.utils.StartBattle;
import me.BoyJamal.practice.utils.StorageManager;
import net.md_5.bungee.api.ChatColor;

public class ArenaListeners implements Listener {
	
	public static List<StartBattle> activeBattles = new ArrayList<>();
	private List<String> respawnPlayers = new ArrayList<>();
	
	@EventHandler
	public void respawnEvent(EntitySpawnEvent evt)
	{
		if (!(evt.getEntity() instanceof Player))
		{
			return;
		}
		
		Player p = (Player)evt.getEntity();
		if (respawnPlayers.contains(p.getUniqueId().toString()))
		{
			MainUtils.onPlayerJoin(p);
			respawnPlayers.remove(p.getUniqueId().toString());
			return;
		} else {
			return;
		}
		
	}
	
	@EventHandler
	public void onKill(EntityDeathEvent evt)
	{
		if (!(evt.getEntity() instanceof Player && evt.getEntity().getKiller() instanceof Player))
		{
			return;
		}
		
		Player killer = evt.getEntity().getKiller();
		Player dead = (Player)evt.getEntity();
		
		evt.getDrops().clear();
		respawnPlayers.add(dead.getUniqueId().toString());
		
		StartBattle battle = null;
		for (StartBattle battles : activeBattles)
		{
			if (battles.getPlayerOne().getName().equalsIgnoreCase(killer.getName()) 
					|| battles.getPlayerOne().getName().equalsIgnoreCase(dead.getName()))
			{
				battle = battles;
			}
		}
		
		if (battle == null)
		{
			return;
		}
		
		battle.setLoser(dead);
		battle.setWinner(killer);
		battle.endBattle();
		return;
	}
	
	@EventHandler
	public void onchat(AsyncPlayerChatEvent evt)
	{
		Player p = evt.getPlayer();
		if (ArenaCMD.finalCreation.containsKey(p.getUniqueId().toString()))
		{
			evt.setCancelled(true);
			String message = ChatColor.stripColor(evt.getMessage());
			ArenaUtil util = new ArenaUtil(ArenaCMD.finalCreation.get(p.getUniqueId().toString()),message,StorageManager.loadedArenas.size());
			StorageManager.loadedArenas.add(util);
			p.sendMessage(MainUtils.chatColor("&a&lCongrats! &7&oYou have created Arena #" + util.getID() + " called " + util.getName()));
			ArenaCMD.finalCreation.remove(p.getUniqueId().toString());
			StorageManager.saveArenas();
			return;
		}
	}
	
	@EventHandler
	public void onLogout(PlayerQuitEvent evt)
	{
		Player p = evt.getPlayer();
		for (StartBattle each : activeBattles)
		{
			if (each.getPlayerOne().getName().equalsIgnoreCase(p.getName()))
			{
				each.setLoser(each.getPlayerOne());
				each.getPlayerOne().getInventory().clear();
				each.setWinner(each.getPlayerTwo());
				each.endBattle();
				break;
			}
			
			if (each.getPlayerTwo().getName().equalsIgnoreCase(p.getName()))
			{
				each.setLoser(each.getPlayerTwo());
				each.getPlayerTwo().getInventory().clear();
				each.setWinner(each.getPlayerOne());
				each.endBattle();
				break;
			}
		}
		
		
		for (QueueUtil each : QueueUtil.queueList)
		{
			if (each.getPlayer().getUniqueId().equals(p.getUniqueId()))
			{
				QueueUtil.queueList.remove(each);
				break;
			}
		}
	}

}
